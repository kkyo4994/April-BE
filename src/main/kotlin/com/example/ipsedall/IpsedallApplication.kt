package com.example.ipsedall

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

internal const val BASE_PACKAGE = "com.example.ipsedall"

@ConfigurationPropertiesScan
@SpringBootApplication
class IpsedallApplication

fun main(args: Array<String>) {
    runApplication<IpsedallApplication>(*args)
}
