package moa.moabackend.infra.portone

import moa.moabackend.infra.portone.config.PortOneProperties
import moa.moabackend.infra.portone.dto.response.PaymentData
import moa.moabackend.infra.portone.dto.response.PortOnePaymentResponse
import moa.moabackend.infra.portone.dto.response.PortOneTokenResponse
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
@EnableConfigurationProperties(PortOneProperties::class)
class PortOneClient(
    private val portOneProperties: PortOneProperties
) {
    private val restTemplate = RestTemplate()
    private val baseUrl = "https://api.iamport.kr"

    fun getAccessToken(): String {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON

        val body = mapOf(
            "imp_key" to portOneProperties.apiKey,
            "imp_secret" to portOneProperties.apiSecret
        )

        val request = HttpEntity(body, headers)
        val response = restTemplate.postForObject("$baseUrl/users/getToken", request, PortOneTokenResponse::class.java)

        return response?.response?.access_token ?: throw RuntimeException("Failed to get PortOne access token")
    }

    fun getPaymentData(impUid: String, accessToken: String): PaymentData {
        val headers = HttpHeaders()
        headers.setBearerAuth(accessToken)

        val request = HttpEntity<Unit>(headers)
        val response = restTemplate.postForObject("$baseUrl/payments/$impUid", request, PortOnePaymentResponse::class.java)

        return response?.response ?: throw RuntimeException("Failed to get PortOne payment data")
    }

    fun refund(impUid: String, accessToken: String, reason: String, amount: Int? = null) {
        val headers = HttpHeaders()
        headers.setBearerAuth(accessToken)
        headers.contentType = MediaType.APPLICATION_JSON

        val body = mutableMapOf<String, Any>(
            "imp_uid" to impUid,
            "reason" to reason
        )
        amount?.let { body["amount"] = it }

        val request = HttpEntity(body, headers)
        restTemplate.postForObject("$baseUrl/payments/cancel", request, Map::class.java)
    }
}
