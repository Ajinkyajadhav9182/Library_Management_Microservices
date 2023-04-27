package com.admin.config;

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
                .antMatchers(HttpMethod.GET, "/showAll", "/show/{id}", "/issued").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/delete/{id}", "/deleteAll").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/update/{id}").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/addBook").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/showAllR", "/bookid/{bookid}").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("123").password("123").roles("ADMIN");
        auth.inMemoryAuthentication().withUser("321").password("321").roles("ADMIN");
    }

    @Bean
    public PasswordEncoder passe() {
        return NoOpPasswordEncoder.getInstance();
    }
}