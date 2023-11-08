package com.project.projectVoucher.security;

import com.project.projectVoucher.service.CustomUserDetails;
import com.project.projectVoucher.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private final JWTService jwtService;

    @Autowired
    private final CustomUserDetailsService customUserDetailsService;

    public JWTAuthenticationFilter(JWTService jwtService, CustomUserDetailsService customUserDetailsService) {
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        if (header==null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.split(" ")[1].trim();
        String userId = jwtService.extractUserId(token);

        if(userId != null && SecurityContextHolder.getContext().getAuthentication()==null){
            CustomUserDetails c = (CustomUserDetails) customUserDetailsService.loadUserById(userId);
            if(c!=null && jwtService.isTokenValid(token, c)){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(c, null, c.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);

    }



}
