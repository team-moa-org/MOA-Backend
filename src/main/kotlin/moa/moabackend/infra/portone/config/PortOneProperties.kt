package moa.moabackend.infra.portone.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "portone")
data class PortOneProperties(
    val apiKey: String,
    val apiSecret: String
)
