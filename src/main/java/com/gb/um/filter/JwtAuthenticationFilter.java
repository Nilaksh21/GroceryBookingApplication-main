package com.gb.um.filter;

import com.gb.um.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Autowired
    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull  HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        //retrieve Authorization Token
        final String authHeader = request.getHeader("Authorization");
        log.info("Authorization : {}",authHeader);

        if(authHeader == null || authHeader.isBlank() || !authHeader.startsWith("Bearer")){
            filterChain.doFilter(request,response);
            return;
        }

        //extract JWT Token from header
        final String token = authHeader.substring(7);

        //extract username form JWT
        final String username = jwtService.extractUsername(token);

        //if not authenticated add authentication
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){

            //perform authentication
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            boolean isValidJWT = jwtService.isTokenValid(token,userDetails);
            log.info("is valid JWT : {}",isValidJWT);

            //add authentication if valid JWT
            if(isValidJWT){
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        token,
                        userDetails.getAuthorities()
                );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        filterChain.doFilter(request,response);
    }
}
