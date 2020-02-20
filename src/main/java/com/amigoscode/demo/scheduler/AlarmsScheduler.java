package com.amigoscode.demo.scheduler;

import com.amigoscode.demo.ApplicationContextHolder;
import com.amigoscode.demo.alphavantage.apiconnector.AlphaVantageAPIConnector;
import com.amigoscode.demo.emailsender.MailHelper;
import com.amigoscode.demo.model.Alarm;
import com.amigoscode.demo.model.DataAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
public class AlarmsScheduler {
    private final DataAccessService dataAccessService;
    private AlphaVantageAPIConnector alphaVantageAPIConnector;

    @Autowired
    public AlarmsScheduler(DataAccessService dataAccessService) {
        this.dataAccessService = dataAccessService;
        alphaVantageAPIConnector = new AlphaVantageAPIConnector();
    }

    void handleSatisfiedAlarm(UUID alarm) {
        dataAccessService.updateAlarmActive(alarm, false); // set isActive to false
        MailHelper mailHelper = ApplicationContextHolder.getContext().getBean(MailHelper.class);
        mailHelper.sendEmail(); //sends mail; make sure no restrictive firewall is on
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


    @Scheduled(fixedRate = 5000)
    void updateCurrentStockPriceOfAlarms() {
        dataAccessService.selectAllAlarms().stream().filter(Alarm::isActive).forEach(alarm -> {
            String stockSymbol = alarm.getStockSymbol();
            float actualCurrentStockPrice = alphaVantageAPIConnector.getStockPriceIntraDay(stockSymbol, 5);
            if (actualCurrentStockPrice == 0f) { //due to API call limitations (5 calls / minute, 500 calls / day) sometimes we can't fetch data
                return;
            }
            dataAccessService.updateAlarmCurrentStockPrice(alarm.getAlarmId(), actualCurrentStockPrice);

            if (isAlarmSatisfied(alarm)) {
                handleSatisfiedAlarm(alarm.getAlarmId());
            }

        });

    }
}
