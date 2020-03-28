package com.stocks.demo.scheduler;

import com.stocks.demo.ApplicationContextHolder;
import com.stocks.demo.alphavantage.apiconnector.AlphaVantageAPIConnector;
import com.stocks.demo.components.DataAccessService;
import com.stocks.demo.emailsender.MailHelper;
import com.stocks.demo.model.Alarm;
import com.stocks.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

@Controller
public class AlarmsScheduler {
    private final DataAccessService dataAccessService;
    private AlphaVantageAPIConnector alphaVantageAPIConnector;

    private <T> Predicate<T> DISTINCT_BY_KEY(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }


    @Autowired
    public AlarmsScheduler(DataAccessService dataAccessService) {
        this.dataAccessService = dataAccessService;
        alphaVantageAPIConnector = new AlphaVantageAPIConnector();
    }

    void handleSatisfiedAlarm(Alarm alarm) {
        dataAccessService.updateAlarmActive(alarm.getAlarmId(), false); // set isActive to false
        MailHelper mailHelper = ApplicationContextHolder.getContext().getBean(MailHelper.class);
        User user = dataAccessService.findUserByUUID(alarm.getUserId());
        mailHelper.sendEmail(user.getEmail(), alarm.getStockSymbol()); //sends mail; make sure no restrictive firewall is on
    }

    public static boolean isAlarmSatisfied(Alarm alarm) {
        int target = alarm.getTargetAlarmPercentage();
        double current = alarm.getCurrentStockPrice();
        double initial = alarm.getInitialStockPrice();

        if (current == 0d || target == 0) return false;

        double stockChange = (current / initial - 1) * 100;

        if (target < 0 && stockChange < 0 && stockChange <= target) { //if alarm's target is met
            return true;
        }
        if (target > 0 && stockChange > 0 && stockChange >= target) {
            return true;
        }
        if (target == 0) {
            return true;
        }
        return false;

    }


    @Scheduled(fixedRateString = "${console.fetchMetrics}", initialDelay = 1000)
        //30 minutes
    void updateCurrentStockPriceOfAlarms() {
        HashMap<String, Double> stockNameToPrice = new HashMap<>();
        dataAccessService.selectAllAlarms().stream().filter(Alarm::isActive).filter(DISTINCT_BY_KEY(Alarm::getStockSymbol))
                .map(Alarm::getStockSymbol)
                .forEach(alarm -> {
                    double actualCurrentStockPrice = alphaVantageAPIConnector.getStockPriceIntraDay(alarm);
                    if (actualCurrentStockPrice == 0d) { //due to API call limitations (5 calls / minute, 500 calls / day) sometimes we can't fetch data
                        return;
                    }
                    stockNameToPrice.put(alarm, actualCurrentStockPrice);
                });

        dataAccessService.selectAllAlarms().stream().filter(Alarm::isActive)
                .filter(alarm -> stockNameToPrice.containsKey(alarm.getStockSymbol()))
                .forEach(alarm -> {
                    dataAccessService.updateAlarmCurrentStockPrice(alarm.getAlarmId(), stockNameToPrice.get(alarm.getStockSymbol()));
                    if (isAlarmSatisfied(alarm)) {
                        handleSatisfiedAlarm(alarm);
                    }

                });

    }
}
