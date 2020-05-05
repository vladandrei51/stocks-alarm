package com.stocks.demo.mvc;

import com.stocks.demo.components.UserService;
import com.stocks.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping("/user")
public class WebUserController {

    @Autowired
    private UserService userService;


    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registerUserAccount(
            @ModelAttribute("user") @Valid User account, BindingResult bindingResult, HttpServletRequest request) {
        if (!bindingResult.hasErrors()) {
            userService.addNewUser(account);
            try {
                request.login(account.getEmail(), account.getPassword());
                return "redirect:/";
            } catch (ServletException e) {
                e.printStackTrace();
                return "registration";
            }
        } else {
            return "registration";
        }
    }

}
