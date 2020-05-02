package com.stocks.demo.mvc;

import com.stocks.demo.components.UserService;
import com.stocks.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class WebController {

    @Autowired
    private UserService userService;

    @RequestMapping("/")
    public String homePage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentLoggedInUser = authentication.getName();
        model.addAttribute("loggedEmail", currentLoggedInUser);
        return "homepage";
    }

    @RequestMapping(value = "/user/registration", method = RequestMethod.GET)
    public String showRegistrationForm(Model model) {
        User user = new User();
        model.addAttribute("newUser", user);
        return "registration";
    }

    @RequestMapping(value = "/user/registration", method = RequestMethod.POST)
    public String registerUserAccount(
            @ModelAttribute("newUser") @Valid User account, HttpServletRequest request) {
        userService.addNewUser(account);
        try {
            request.login(account.getEmail(), account.getPassword());
            return "redirect:/";
        } catch (ServletException e) {
            e.printStackTrace();
            return "registration";
        }
    }
}
