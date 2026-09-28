package com.bankapp.util;

import com.bankapp.service.CustomerUserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
        private final JwtProvider jwtProvider;
        private final CustomerUserService customerUserService;

        public JwtAuthenticationFilter(JwtProvider jwtProvider, CustomerUserService customerUserService, CustomerUserService customerUserService1) {
            this.jwtProvider = jwtProvider;
            this.customerUserService = customerUserService1;
        }
        @Override

        protected void doFilterInternal ( HttpServletRequest request, HttpServletResponse response,
                                         FilterChain filterChain) throws ServletException, IOException {
            String requestValue = request.getHeader("Authorization");
            if (requestValue != null && requestValue.startsWith("Bearer ")) {
                requestValue = requestValue.substring(7);
                boolean isValid = jwtProvider.validateToken(requestValue);
                if (isValid) {
                   String email = jwtProvider.getSubject(requestValue);
                   UserDetails userDetails = customerUserService.loadUserByUsername(email);
                   UsernamePasswordAuthenticationToken authToken =  new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                   SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            filterChain.doFilter(request, response);

        }
}
