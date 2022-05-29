package com.example.ipsedall.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "oauth.google")
class GoogleOAuthProperty(
    val clientId: String,
    val clientSecret: String,
    val redirectUri: String,
    val scope: String
)
