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
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AlarmsScheduler {
    private final DataAccessService dataAccessService;
    private final AlphaVantageAPIConnector alphaVantageAPIConnector;

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
        return target == 0;

    }


    @Scheduled(fixedRateString = "${console.fetchMetrics}", initialDelay = 0)
        //30 minutes
    void updateCurrentStockPriceOfAlarms() {
        HashMap<String, Double> stockNameToPrice = new HashMap<>();
        List<Alarm> existingActiveAlarms = dataAccessService.selectAllAlarms().stream().filter(Alarm::isActive).collect(Collectors.toList());

        existingActiveAlarms.stream().map(Alarm::getStockSymbol).distinct() // get all distinct active alarms
                .forEach(alarm -> {
                    double currentStockPrice = alphaVantageAPIConnector.getStockPrice(alarm);
                    if (currentStockPrice != 0d) { //due to API call limitations (5 calls / minute, 500 calls / day) sometimes we can't fetch data
                        stockNameToPrice.put(alarm, currentStockPrice);
                    }
                });


        existingActiveAlarms.stream().filter(alarm -> stockNameToPrice.containsKey(alarm.getStockSymbol()))
                .forEach(alarm -> {
                    dataAccessService.updateAlarmCurrentStockPrice(alarm.getAlarmId(), stockNameToPrice.get(alarm.getStockSymbol()));
                    if (isAlarmSatisfied(alarm)) {
                        handleSatisfiedAlarm(alarm);
                    }

                });

    }
}
