package com.example.ipsedall.jwt

import com.example.ipsedall.jwt.properties.JwtProperties
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtTokenProvider(
    private  val jwtProperties: JwtProperties
) {

    companion object {
        const val ACCESS_TOKEN_TYPE = "access_token"
        const val REFRESH_TOKEN_TYPE = "refresh_token"
    }

    fun generateToken(subject: String, type: String): String {
        val expirationAsSecond = if (type == ACCESS_TOKEN_TYPE) {
            jwtProperties.accessExp
        } else {
            jwtProperties.refreshExp
        }

        return Jwts.builder()
            .setSubject(subject)
            .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encode(jwtProperties.secretKey.toByteArray()))
            .claim("type", type)
            .setExpiration(Date(System.currentTimeMillis() + expirationAsSecond * 1000))
            .setIssuedAt(Date())
            .compact()
    }
}