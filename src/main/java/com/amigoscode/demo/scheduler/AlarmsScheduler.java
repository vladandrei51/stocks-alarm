package com.amigoscode.demo.scheduler;

import com.amigoscode.demo.ApplicationContextHolder;
import com.amigoscode.demo.alphavantage.apiconnector.AlphaVantageAPIConnector;
import com.amigoscode.demo.emailsender.MailHelper;
import com.amigoscode.demo.model.Alarm;
import com.amigoscode.demo.model.DataAccessService;
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


    //    @Async
    @Scheduled(fixedRate = 50000)
    void updateCurrentStockPriceOfAlarms() {
        System.out.println("Scheduled updating of the alarms stock price started");
        dataAccessService.selectAllAlarms().stream().filter(Alarm::isActive).forEach(alarm -> {
            String stockSymbol = alarm.getStockSymbol();
            dataAccessService.updateAlarmCurrentStockPrice(alarm.getAlarmId(), alphaVantageAPIConnector.getStockPriceIntraDay(stockSymbol, 5));

            int target = alarm.getTargetAlarmPercentage();
            float current = alarm.getCurrentStockPrice();
            float initial = alarm.getInitialStockPrice();

            System.out.println(String.format("NW target=%s, current=%s, initial=%s, calc=%s", target, current, initial, (current / initial - 1) * 100));

            if (target >= (current / initial - 1) * 100) { //if alarm's target is met
                System.out.println(String.format("W target=%s, current=%s, initial=%s, calc=%s", target, current, initial, (current / initial - 1) * 100));
                dataAccessService.updateAlarmActive(alarm.getAlarmId(), false); // set isActive to false
                MailHelper mailHelper = ApplicationContextHolder.getContext().getBean(MailHelper.class);
                mailHelper.sendEmail(); //send mail
            }

        });

    }
}
