package com.stocks.demo.mvc;

import com.stocks.demo.alphavantage.apiconnector.AlphaVantageAPIConnector;
import com.stocks.demo.model.Stock;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/stock")
public class StockMVCController {


    @GetMapping(value = "/info/{stockSymbol}")
    public String userLogin(@PathVariable String stockSymbol, Model model) {
        AlphaVantageAPIConnector stocksApi = new AlphaVantageAPIConnector();
        Stock clickedStock = stocksApi.getStockInfoFromKeyword(stockSymbol);
        model.addAttribute("stock", clickedStock);

        return "stock-information";
    }

}
