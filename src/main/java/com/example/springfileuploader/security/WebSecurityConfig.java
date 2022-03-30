package com.example.springfileuploader.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtFilter jwtFilter;

    @Autowired
    public WebSecurityConfig(JwtFilter jwtFilter){
        this.jwtFilter = jwtFilter;
    }

    /**
     * Configures the endpoints of the application, sets accessibility to routes based on roles,
     * and controls whether cors and csrf are used.
     */
    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/users/**").hasRole("USER")
                .antMatchers("/api/register").permitAll()
                .antMatchers("/api/login").permitAll()
                .antMatchers("/").permitAll()
                .anyRequest()
                .authenticated();

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }

    /**
     * Creates a bean of the chosen authenticationManager for the application.
     */
    @Override
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

}
