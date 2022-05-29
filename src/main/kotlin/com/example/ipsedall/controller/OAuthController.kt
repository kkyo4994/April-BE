package com.example.ipsedall.controller

import com.example.ipsedall.payload.response.JwtTokenResponse
import com.example.ipsedall.service.OAuthService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletResponse

@RequestMapping("/oauth/google")
@RestController
class OAuthController(
    private val oAuthService: OAuthService
) {
    @GetMapping
    fun oAuthLoginPage(response: HttpServletResponse) {
        val oAuthUri = oAuthService.getAuthorizeUri()
        response.sendRedirect(oAuthUri)
    }

    @PostMapping
    fun getOAuthToken(@RequestParam("code") code: String): JwtTokenResponse {
        return oAuthService.getOAuthToken(code)
    }
}
