package com.stocks.demo.scheduler;

import com.stocks.demo.ApplicationContextHolder;
import com.stocks.demo.alphavantage.apiconnector.AlphaVantageAPIConnector;
import com.stocks.demo.emailsender.MailHelper;
import com.stocks.demo.model.Alarm;
import com.stocks.demo.model.DataAccessService;
import com.stocks.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

@Controller
public class AlarmsScheduler {
    private final DataAccessService dataAccessService;
    private AlphaVantageAPIConnector alphaVantageAPIConnector;

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

    boolean isAlarmSatisfied(Alarm alarm) {
        int target = alarm.getTargetAlarmPercentage();
        float current = alarm.getCurrentStockPrice();
        float initial = alarm.getInitialStockPrice();

        float stockChange = (current / initial - 1) * 100;

        if (target < 0 && stockChange < 0 && stockChange <= target) { //if alarm's target is met
            return true;
        }
        if (target > 0 && stockChange > 0 && stockChange >= target) {
            return true;
        }
        return false;

    }


    @Scheduled(fixedRate = 1800000)
        //30 minutes
    void updateCurrentStockPriceOfAlarms() {
        dataAccessService.selectAllAlarms().stream().filter(Alarm::isActive).forEach(alarm -> {
            String stockSymbol = alarm.getStockSymbol();
            float actualCurrentStockPrice = alphaVantageAPIConnector.getStockPriceIntraDay(stockSymbol, 5);
            if (actualCurrentStockPrice == 0f) { //due to API call limitations (5 calls / minute, 500 calls / day) sometimes we can't fetch data
                return;
            }
            dataAccessService.updateAlarmCurrentStockPrice(alarm.getAlarmId(), actualCurrentStockPrice);
            if (isAlarmSatisfied(alarm)) {
                handleSatisfiedAlarm(alarm);
            }

        });

    }
}
