package com.fengwenyi.springbootsecurity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Erwin Feng
 * @since 2019-04-23 09:44
 */
//@Service
public class CustomUserService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        if (s.equals("user")) {
            String username = "user";
            String password = "pwd";
            Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
            grantedAuthorities.add(new SimpleGrantedAuthority("user"));
            //throw new Us
            return new User(username, password, grantedAuthorities);
        }
        throw new UsernameNotFoundException("admin: " + s + " do not exist!");
    }
}
