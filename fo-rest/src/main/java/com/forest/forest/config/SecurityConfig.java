package com.forest.forest.config;

import javax.sql.DataSource;

import org.apache.catalina.valves.JDBCAccessLogValve;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
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
        .authorizeRequests()
          .antMatchers("/", "/home").permitAll() 
          .anyRequest().authenticated() 
          .and()
       .formLogin() 
        .loginProcessingUrl("/login")
         .permitAll()
         .and()
      .logout()
        .logoutUrl("/logout") 
        .permitAll()
        .invalidateHttpSession(true)
        .deleteCookies("JSESSIONID");
      http  
      .httpBasic() 
      .and()
      .csrf().disable()
      .logout();

      return http.build();
  }

  @Bean
	public JdbcDaoImpl  userDetailsService(){
    JdbcDaoImpl jdbcDaoImpl = new JdbcDaoImpl();
    jdbcDaoImpl.setDataSource(dataSource);
    jdbcDaoImpl.setUsersByUsernameQuery("SELECT username, pass, true FROM users WHERE username=?");
    jdbcDaoImpl.setAuthoritiesByUsernameQuery("SELECT username, 'ROLE_USER' FROM users WHERE username=?");
    return jdbcDaoImpl;
	}

  @Bean
  public PasswordEncoder passwordEncoder() {
    return NoOpPasswordEncoder.getInstance();
  }

}