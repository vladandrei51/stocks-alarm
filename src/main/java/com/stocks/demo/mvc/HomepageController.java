package com.stocks.demo.mvc;

import com.stocks.demo.components.UserService;
import com.stocks.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomepageController {

    @Autowired
    private UserService userService;

    @RequestMapping("/")
    public String homePage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentLoggedInUser = authentication.getName();
        model.addAttribute("loggedEmail", currentLoggedInUser);

        User user = userService.findUserByEmail(currentLoggedInUser);
        model.addAttribute("alarms", userService.getAllAlarms(user.getUserId()));

        return "homepage";
    }

}
