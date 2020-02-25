package com.stocks.demo.security;

import com.stocks.demo.ApplicationContextHolder;
import com.stocks.demo.components.DataAccessService;
import com.stocks.demo.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class MyUserDetailsService implements UserDetailsService {


    @Override
    public UserDetails loadUserByUsername(String username) {
        DataAccessService dataAccessService = ApplicationContextHolder.getContext().getBean(DataAccessService.class);
        User user = dataAccessService.findUserByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new MyUserPrincipal(user);
    }
}