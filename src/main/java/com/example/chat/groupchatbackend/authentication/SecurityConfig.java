package com.example.chat.groupchatbackend.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Configuration
@Component
public class SecurityConfig extends WebSecurityConfigurerAdapter {
@Autowired
private DbUserDetailsService dbUserDetailsService;

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        return  dbUserDetailsService;
    }

    @Override
    protected void configure (HttpSecurity http) throws Exception{

        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(dbUserDetailsService);
        http.csrf().disable()
                .authenticationProvider(authenticationProvider)
                .authorizeRequests()
                .antMatchers("/registration").permitAll()
                .anyRequest().authenticated()
                .and().httpBasic();
    }

}