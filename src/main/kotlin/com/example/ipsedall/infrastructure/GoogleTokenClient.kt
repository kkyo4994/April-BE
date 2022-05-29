package com.example.ipsedall.infrastructure

import com.example.ipsedall.infrastructure.payload.response.GoogleTokenResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.cloud.openfeign.SpringQueryMap
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.PostMapping

@FeignClient(name = "googleTokenClient", url = "https://oauth2.googleapis.com")
interface GoogleTokenClient {
    @PostMapping("/token")
    fun getOAuthToken(@SpringQueryMap param: MultiValueMap<String, String>): GoogleTokenResponse
}
