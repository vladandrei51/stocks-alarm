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

//        if (!authentication.isAuthenticated())     -- Not needed
//            return "login-form";

        String currentLoggedInUser = authentication.getName();
        User user = userService.findUserByEmail(currentLoggedInUser);

        String name = user.getFirstName();
        name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
        model.addAttribute("loggedFirstName", name);
        model.addAttribute("alarms", userService.getAllAlarms(user.getUserId()));

        return "homepage";
    }

}
