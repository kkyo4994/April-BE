package com.example.ipsedall.security

import com.example.ipsedall.jwt.JwtTokenParser
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class TokenFitter(private val jwtTokenParser: JwtTokenParser, private val userDetailsService: UserDetailsService) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION)
        if(bearerToken == null) {
            filterChain.doFilter(request, response)
            return
        }

        val pureJwtToken = bearerToken.removeTokenType()
        val tokenClaims = jwtTokenParser.parseToken(pureJwtToken)
        val userEmail = tokenClaims.subject
        val userDetails = userDetailsService.loadUserByUsername(userEmail)

        val authentication = UsernamePasswordAuthenticationToken(userDetails.username, "", userDetails.authorities)
        SecurityContextHolder.getContext().authentication = authentication

        filterChain.doFilter(request, response)
    }

    private fun String.removeTokenType(): String {
        return this.split(" ")[1]
    }

}
