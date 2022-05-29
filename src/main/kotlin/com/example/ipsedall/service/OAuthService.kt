package com.example.ipsedall.service

import com.example.ipsedall.entity.RefreshToken
import com.example.ipsedall.entity.RefreshTokenRepository
import com.example.ipsedall.entity.User
import com.example.ipsedall.entity.UserRepository
import com.example.ipsedall.infrastructure.GoogleTokenClient
import com.example.ipsedall.infrastructure.GoogleUserInfoClient
import com.example.ipsedall.infrastructure.payload.response.GoogleUserInfoResponse
import com.example.ipsedall.jwt.JwtTokenProvider
import com.example.ipsedall.payload.response.JwtTokenResponse
import com.example.ipsedall.property.GoogleOAuthProperty
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.util.UriComponentsBuilder

@Service
class OAuthService(
    private val googleOAuthProperty: GoogleOAuthProperty,
    private val googleTokenClient: GoogleTokenClient,
    private val googleUserInfoClient: GoogleUserInfoClient,
    private val userRepository: UserRepository,
    private val jwtTokenProvider: JwtTokenProvider,
    private val refreshTokenRepository: RefreshTokenRepository
) {

    fun getAuthorizeUri(): String {
        val authorizeParam = buildAuthorizeParam()
        return getAuthUri(authorizeParam)
    }

    private fun buildAuthorizeParam(): MultiValueMap<String, String> {
        val authorizeParam = LinkedMultiValueMap<String, String>()
        authorizeParam["response_type"] = "code"
        authorizeParam["client_id"] = googleOAuthProperty.clientId
        authorizeParam["scope"] = googleOAuthProperty.scope
        authorizeParam["redirect_uri"] = googleOAuthProperty.redirectUri
        return authorizeParam
    }

    private fun getAuthUri(param: MultiValueMap<String, String>): String {
        return UriComponentsBuilder
            .fromHttpUrl("https://accounts.google.com/o/oauth2/v2")
            .path("/auth")
            .queryParams(param)
            .build()
            .toUriString()
    }

    fun getOAuthToken(code: String): JwtTokenResponse {
        val googleOAuthTokenParam = buildTokenParam(code)
        val tokenResponse = googleTokenClient.getOAuthToken(googleOAuthTokenParam)
        val userInfoResponse = googleUserInfoClient.getGoogleUserInfo("Bearer ${tokenResponse.accessToken}")
        val user = userRepository.findByEmail(userInfoResponse.email) ?: saveUser(userInfoResponse)
        val accessToken = jwtTokenProvider.generateToken(user.email, JwtTokenProvider.ACCESS_TOKEN_TYPE)
        val refreshToken = jwtTokenProvider.generateToken(user.email, JwtTokenProvider.REFRESH_TOKEN_TYPE)

        val refreshTokenEntity = RefreshToken(
            tokenValue = refreshToken,
            subject = user.email
        )

        refreshTokenRepository.save(refreshTokenEntity)
        return JwtTokenResponse(
            accessToken = "Bearer $accessToken",
            refreshToken = "Bearer $refreshToken"
        )
    }

    private fun buildTokenParam(code: String): MultiValueMap<String, String> {
        val authorizeParam = LinkedMultiValueMap<String, String>()
        authorizeParam["client_id"] = googleOAuthProperty.clientId
        authorizeParam["client_secret"] = googleOAuthProperty.clientSecret
        authorizeParam["grant_type"] = "authorization_code"
        authorizeParam["code"] = code
        authorizeParam["redirect_uri"] = googleOAuthProperty.redirectUri
        return authorizeParam
    }

    private fun saveUser(userInfoResponse: GoogleUserInfoResponse): User {
        val user = User(
            email = userInfoResponse.email,
            name = userInfoResponse.name
        )

        return userRepository.save(user)
    }
}
