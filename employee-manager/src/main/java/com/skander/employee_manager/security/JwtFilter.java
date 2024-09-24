/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.skander.employee_manager.security;

import static org.springframework.http.HttpHeaders.*;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
class JwtFilter extends OncePerRequestFilter{


    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;


  
    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain)
        throws ServletException, IOException {


            // we check if it's the auth path
            if (request.getServletPath().contains("/api/v1/auth")) {    
                filterChain.doFilter(request, response);
                return;
            }

            //we check ken there is a token bearer or something
            final String authHeader = request.getHeader(AUTHORIZATION);
            final String jwt;
            final String userMail;
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            //// here we definitely think there is a token and we process it ?
            //get everything after bearer:
            jwt = authHeader.substring(7);
            
            userMail = jwtService.extractUsername(jwt);
            //check if the user exists and if he is, is he authenticated
            if (userMail!= null && SecurityContextHolder.getContext().getAuthentication()== null){
                //loads the userdetails from the username
                UserDetails userDetails = userDetailsService.loadUserByUsername(userMail);
                //check if the token is valid
                if  (jwtService.isTokenValid(jwt, userDetails)){
                    //create an auth token maghir el credentials (null)
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    //set token details using the request (el IP, etc...)
                    authToken.setDetails(
                                    new WebAuthenticationDetailsSource().buildDetails(request)

                    );
                    //set the authentification object authtoken after we put details in it
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            //go to next filter
            filterChain.doFilter(request, response);
            

    }

}
}
