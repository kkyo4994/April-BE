package com.example.ipsedall.infrastructure

import com.example.ipsedall.infrastructure.payload.response.GoogleUserInfoResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(name = "googleUserInfoClient", url = "https://www.googleapis.com/oauth2/v2")
interface GoogleUserInfoClient {
    @GetMapping("/userinfo")
    fun getGoogleUserInfo(@RequestHeader(HttpHeaders.AUTHORIZATION) authorization: String): GoogleUserInfoResponse
}
