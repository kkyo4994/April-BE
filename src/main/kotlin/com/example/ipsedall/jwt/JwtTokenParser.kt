package com.example.ipsedall.jwt

import com.example.ipsedall.jwt.properties.JwtProperties
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtTokenParser (private val jwtProperties: JwtProperties) {
    fun parseToken (token : String): Claims {
        return Jwts.parser().setSigningKey(Base64.getEncoder().encode(jwtProperties.secretKey.toByteArray())).parseClaimsJws(token).body
    }
}
