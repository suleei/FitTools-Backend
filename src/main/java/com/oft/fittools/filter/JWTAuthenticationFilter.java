package com.oft.fittools.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    @Value("${jwt.signatureKey}")
    String signatureKey;

    private HandlerExceptionResolver handlerExceptionResolver;
    @Autowired

    public JWTAuthenticationFilter(HandlerExceptionResolver handlerExceptionResolver){
        this.handlerExceptionResolver=handlerExceptionResolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = request.getHeader("Authorization");
        if(!StringUtils.hasText(jwt)){
            filterChain.doFilter(request,response);
            return;
        }
        JWTVerifier jwtVerifier= JWT.require(Algorithm.HMAC256(signatureKey)).build();
        String username=null;
        try {
            DecodedJWT decodedJWT =  jwtVerifier.verify(jwt);
            username=decodedJWT.getClaim("username").asString();
        }catch (Exception e){
            handlerExceptionResolver.resolveException(request,response,null,e);
            return;
        }
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken= new UsernamePasswordAuthenticationToken(username,null,null);
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        filterChain.doFilter(request,response);
    }
}
