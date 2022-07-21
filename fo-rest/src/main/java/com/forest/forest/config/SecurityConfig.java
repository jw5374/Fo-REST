package com.forest.forest.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import lombok.var;



@Configuration
public class SecurityConfig {

  @Autowired
  private DataSource dataSource;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
        //HTTP Basic authentication
        .httpBasic()
        .and()
        .authorizeRequests()
        .antMatchers(HttpMethod.POST, "/login").permitAll()
        .and()
        .csrf().disable()
        .formLogin().loginProcessingUrl("/login");

      return http.build();
  }

  @Bean
  public UserDetailsService userDetailsService() {
    var userDetailsService =
        new InMemoryUserDetailsManager();

    var user = User.withUsername("user")
            .password("password")
            .authorities("USER_ROLE")
            .build();

    userDetailsService.createUser(user); 

    return userDetailsService;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return NoOpPasswordEncoder.getInstance();
  }

}