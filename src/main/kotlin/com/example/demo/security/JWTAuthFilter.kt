package com.example.demo.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JWTAuthFilter(
    private val jwtService : JWTService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            if(jwtService.validateAccessToken(authHeader)){
                val userId = jwtService.getUserIdFromToken(authHeader)
                val auth = UsernamePasswordAuthenticationToken(userId,null,emptyList())
                SecurityContextHolder.getContext().authentication = auth
            }
        }

        filterChain.doFilter(request,response)
    }
}