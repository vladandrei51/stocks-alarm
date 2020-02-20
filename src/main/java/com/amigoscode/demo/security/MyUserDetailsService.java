package com.amigoscode.demo.security;

import com.amigoscode.demo.ApplicationContextHolder;
import com.amigoscode.demo.model.DataAccessService;
import com.amigoscode.demo.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class MyUserDetailsService implements UserDetailsService {


    @Override
    public UserDetails loadUserByUsername(String username) {
        DataAccessService dataAccessService = ApplicationContextHolder.getContext().getBean(DataAccessService.class);
        User user = dataAccessService.findUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new MyUserPrincipal(user);
    }
}