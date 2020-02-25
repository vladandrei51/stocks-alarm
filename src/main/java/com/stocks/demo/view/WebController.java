package com.stocks.demo.view;

import com.stocks.demo.components.UserService;
import com.stocks.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class WebController {


    private final UserService userService;

    @Autowired
    public WebController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/")
    @ResponseBody
    public String index() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return "That's pretty basic, " + currentPrincipalName;
    }

    @RequestMapping(value = "/user/registration", method = RequestMethod.GET)
    public String showRegistrationForm(WebRequest request, Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "registration";
    }

    @RequestMapping(value = "/user/registration", method = RequestMethod.POST)
    public String registerUserAccount(
            @ModelAttribute("user") @Valid User account, BindingResult result, HttpServletRequest request) {
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
