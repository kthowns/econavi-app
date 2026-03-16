package com.example.econavi.auth.security;

import com.example.econavi.auth.exception.AuthAuthenticationEntryPoint;
import com.example.econavi.member.entity.Member;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    private final JwtUtil jwtUtil;
    private final JwtBlacklistService jwtBlacklistService;
    private final AuthAuthenticationEntryPoint authAuthenticationEntryPoint;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //JWT 파싱
        String token = resolveToken(request);

        try{
            if (StringUtils.hasText(token) &&
                    jwtUtil.validateToken(token)
                    && !jwtBlacklistService.isTokenLogout(token)
                    && !jwtBlacklistService.isTokenWithdrawn(token)
            ) {
                Authentication authentication = jwtUtil.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (AuthenticationException e){
            SecurityContextHolder.clearContext();
            authAuthenticationEntryPoint.commence(request, response, e);
            return;
        }

        filterChain.doFilter(request, response);
    }
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
