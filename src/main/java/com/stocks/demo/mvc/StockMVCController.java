package com.stocks.demo.mvc;

import com.stocks.demo.alphavantage.apiconnector.AlphaVantageAPIConnector;
import com.stocks.demo.components.UserService;
import com.stocks.demo.model.Alarm;
import com.stocks.demo.model.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@Controller
@RequestMapping("/stock")


public class StockMVCController {

    @Autowired
    private UserService userService;

    private Stock stock;

    @GetMapping(value = "/info/{stockSymbol}")
    public String displayStock(@PathVariable String stockSymbol, Model model) {
        AlphaVantageAPIConnector stocksApi = new AlphaVantageAPIConnector();
        stock = stocksApi.getStockInfoFromKeyword(stockSymbol);
        model.addAttribute("stock", stock);
        model.addAttribute("alarm", new Alarm());
        return "stock-information";
    }

    @PostMapping(value="/process/")
    public String addAlarm(@ModelAttribute("alarm") Alarm alarm){
        UUID userId = userService.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).getUserId();
        alarm.setUserId(userId);
        alarm.setInitialStockPrice(new AlphaVantageAPIConnector().getStockPrice(stock.getSymbol()));
        alarm.setStockSymbol(stock.getSymbol());
        alarm.setActive(true);
        userService.addNewAlarm(alarm);
        return "redirect:/";
    }

}
