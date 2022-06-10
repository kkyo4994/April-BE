package com.example.ipsedall.security

import com.example.ipsedall.jwt.JwtTokenParser
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val userDetailsService: UserDetailsService,
    private val jwtTokenParser: JwtTokenParser
) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http
            .csrf().disable()
            .formLogin().disable()
            .cors()
            .and()
            .authorizeRequests()
            .antMatchers("/oauth/google").permitAll()
            .anyRequest().authenticated()
            .and()
            .addFilterAt(TokenFitter(jwtTokenParser, userDetailsService), UsernamePasswordAuthenticationFilter::class.java)
    }
}
