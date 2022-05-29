package com.example.ipsedall.configuration

import com.example.ipsedall.BASE_PACKAGE
import feign.RequestInterceptor
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@EnableFeignClients(basePackages = [BASE_PACKAGE])
@Configuration
class FeignClientConfiguration {
    @Bean
    fun requestInterceptor(): RequestInterceptor {
        return RequestInterceptor { requestTemplate ->
            requestTemplate.header("Content-Type", "application/json")
            requestTemplate.header("Accept", "application/json")
        }
    }
}