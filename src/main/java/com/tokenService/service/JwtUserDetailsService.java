package com.tokenService.service;

import com.tokenService.dao.User;
import com.tokenService.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<User> users = userRepository.findAll();
        org.springframework.security.core.userdetails.User currentUser = null;
        Iterator<User> iter = users.iterator();

        while (iter.hasNext()) {
            User usr = iter.next();
            if (usr.getUsername().contentEquals(username)) {
                currentUser = new org.springframework.security.core.userdetails.User(username, usr.getPassword(),
                        new ArrayList<>());
            }
        }
        if (currentUser == null)
            throw new UsernameNotFoundException("User not found with username: " + username);
        return currentUser;
    }
}