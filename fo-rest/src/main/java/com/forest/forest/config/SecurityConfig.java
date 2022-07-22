package com.forest.forest.config;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class SecurityConfig {

  @Autowired
  private DataSource dataSource;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
        .csrf().disable()
        .authorizeRequests()
          .antMatchers("/", "/home", "/register**", "/products/**", "/products**").permitAll() 
          .anyRequest().authenticated() 
          .and()
       .formLogin()
       .successHandler(new AuthenticationSuccessHandler() {
          @Override
          public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
            response.setStatus(200);
            response.setContentType("application/json");
            PrintWriter res = response.getWriter();
            res.println("{ \"username\": " + "\"" + request.getParameter("username") + "\" }" );
          }
        })
        .loginProcessingUrl("/login")
         .permitAll()
         .and()
      .logout()
        .logoutUrl("/logout") 
        .permitAll()
        .invalidateHttpSession(true)
        .deleteCookies("JSESSIONID");


      return http.build();
  }

  @Bean
	public JdbcDaoImpl userDetailsService(){
    JdbcDaoImpl jdbcDaoImpl = new JdbcDaoImpl();
    jdbcDaoImpl.setDataSource(dataSource);
    jdbcDaoImpl.setUsersByUsernameQuery("SELECT username, pass, 'true' FROM users WHERE username=?");
    jdbcDaoImpl.setAuthoritiesByUsernameQuery("SELECT username, 'ROLE_USER' FROM users WHERE username=?");
    return jdbcDaoImpl;
	}

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}