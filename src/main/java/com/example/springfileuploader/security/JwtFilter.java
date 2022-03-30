package com.example.springfileuploader.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtil jwtUtil;

    @Autowired
    public JwtFilter(UserDetailsServiceImpl userDetailsService, JwtUtil jwtUtil){
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Checks the incoming request for a JWT in the authorization header, validates the JWT,
     * and adds the authenticated user to the {@link SecurityContextHolder}. If no token is found,
     * or an authenticated user already exists, nothing is added to the {@link SecurityContextHolder}.
     * @param request the incoming request.
     * @param response the outgoing response.
     * @param filterChain the current filter chain of the servlet.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(header == null || !header.startsWith("Bearer")){
            filterChain.doFilter(request, response);
            return;
        }

        String jwtToken = header.substring("Bearer ".length());
        if(jwtToken.isEmpty() || jwtToken.isBlank()){
            filterChain.doFilter(request, response);
            return;
        }

        String email = jwtUtil.validateJWTAndRetrieveUser(jwtToken);
        UserDetailsImpl userDetails = userDetailsService.loadUserByUsername(email);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(),
                userDetails.getPassword(),
                userDetails.getAuthorities()
        );

        if(SecurityContextHolder.getContext().getAuthentication() == null) {
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(request, response);
    }

}
