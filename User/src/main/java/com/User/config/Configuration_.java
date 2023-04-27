package com.User.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class Configuration_ extends WebSecurityConfigurerAdapter {

    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/showbooks", "/{bookid}", "/issuedata").hasRole("NORMAL")
                .antMatchers(HttpMethod.POST, "/issueBook/{id}").hasRole("NORMAL")
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("abc").password("abc").roles("NORMAL");
        auth.inMemoryAuthentication().withUser("abcd").password("abcd").roles("NORMAL");
        auth.inMemoryAuthentication().withUser("abcc").password("abcc").roles("NORMAL");
    }

    @Bean
    public PasswordEncoder passe() {
        return NoOpPasswordEncoder.getInstance();
    }
}