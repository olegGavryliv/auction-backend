package com.havryliv.auction.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.havryliv.auction.exception.BusinessException;
import com.havryliv.auction.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtTokenFilter extends OncePerRequestFilter {


    private JwtTokenProvider jwtTokenProvider;

    private UserService userService;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = jwtTokenProvider.resolveToken(httpServletRequest);

            if (token != null && jwtTokenProvider.validateToken(token)) {
                UserDetails userDetails = userService.loadUserByUsername(jwtTokenProvider.getUsername(token));
                Authentication auth = jwtTokenProvider.getAuthentication(userDetails);
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                SecurityContextHolder.clearContext();
            }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }



}
