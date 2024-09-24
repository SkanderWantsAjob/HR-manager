/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.skander.employee_manager.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
@Service
public class JwtService {
    @Value("${custom.security.jwt.secret-key}")
    private String secretKey;
    @Value("${custom.security.jwt.expiration}")
    private long jwtExpiration;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // it returns a generic type
    public <T> T extractClaim(  String token,
                                Function<Claims, T> claimsResolver // claim resolver is actually a method that takes claims as input and T as output
                            )  {
                                        final Claims claims = extractAllClaims(token); 
                                        return claimsResolver.apply(claims); // it means to execute the method with claims as the output
                                }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    private String buildToken(
            Map<String, Object> extraClaims, //the claims not usually put like sub, iat ...
            UserDetails userDetails, // nekhdhou menhom el basic claims
            long expiration
    ) {
        //get the authorities (or roles i guess ?)
        var authorities = userDetails.getAuthorities()
                .stream().
                map(GrantedAuthority::getAuthority)
                .toList();
        return Jwts
                .builder()//start building the jwt with the claims and stuff
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .claim("authorities", authorities)
                .signWith(getSignInKey())
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        //it checks if it is the same user and if the token is still valid
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        // when you do new Date(), it creates a java Data with the current time so it checks if it expired or nah
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        //use the getExpiration method reference to put as input in the extractClaim() method above
        return extractClaim(token, Claims::getExpiration);
    }

    //claims is a map of key-value pairs representing the claims contained within the JWT.
    private Claims extractAllClaims(String token) {
        return Jwts
        //parsebuilder configures how the jwt is gonna be parsed
                .parserBuilder()
                //retrieve the secret key to check if it is correct
                .setSigningKey(getSignInKey())
                .build()// finalizing the parser
                .parseClaimsJws(token)//this method does the actual parsing (ken fama haja ghalta yoddhorli hedhi li t9ollek)
                .getBody(); //finally extract the claims that contain stuff like the subject, issuer, expiration time, and any custom claims)
    }



    private Key getSignInKey() {
        //it takes the base64 encoded string key we have into its original binary form in a bye array
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        //take the bye array and turn it into a key object suitable for hmac( hash based message authentication code) signing
        return Keys.hmacShaKeyFor(keyBytes);
    }
}