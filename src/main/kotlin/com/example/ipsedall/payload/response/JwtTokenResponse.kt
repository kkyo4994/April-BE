package com.example.ipsedall.payload.response

data class JwtTokenResponse(
    val accessToken: String,
    val refreshToken: String
)
