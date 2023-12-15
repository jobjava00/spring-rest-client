package com.jobjava.spring.restclient.configration

import com.jobjava.spring.restclient.CustomException
import org.apache.hc.client5.http.config.RequestConfig
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder
import org.apache.hc.core5.util.Timeout
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatusCode
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestClient
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.support.RestClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory
import java.util.concurrent.TimeUnit

@Configuration
class RestTemplateConfiguration {

    @Bean
    fun restTemplate(): RestTemplate {
        val factory = createRequestFactory(300, 3000)
        return RestTemplate(factory)
    }

    @Bean
    fun restClient(restTemplate: RestTemplate): RestClient {
        return RestClient.builder(restTemplate)
            .defaultStatusHandler(
                { statusCode: HttpStatusCode -> statusCode.isError },
                { request, response -> throw CustomException("${request.uri.path} ${request.method.name()} - ${response.statusCode.value()}") }
            ).build()
    }

    @Bean
    fun httpServiceProxyFactory(restClient: RestClient, env: Environment): HttpServiceProxyFactory {
        return HttpServiceProxyFactory
            .builderFor(RestClientAdapter.create(restClient))
            .embeddedValueResolver(env::resolvePlaceholders)
            .build()
    }

    private fun createRequestFactory(
        connectionTimeout: Long,
        readTimeout: Long
    ): HttpComponentsClientHttpRequestFactory {
        val requestConfig = createRequestConfig(connectionTimeout, readTimeout)
        val connectionManager = createConnectionManager()

        val httpClient = HttpClientBuilder.create()
            .setConnectionManager(connectionManager)
            .setDefaultRequestConfig(requestConfig)
            .build()

        return HttpComponentsClientHttpRequestFactory(httpClient)
    }

    private fun createRequestConfig(connectionTimeout: Long, readTimeout: Long): RequestConfig {
        return RequestConfig.custom()
            .setConnectionRequestTimeout(Timeout.of(connectionTimeout, TimeUnit.MILLISECONDS))
            .setResponseTimeout(Timeout.of(readTimeout, TimeUnit.MILLISECONDS))
            .build()
    }

    private fun createConnectionManager(): PoolingHttpClientConnectionManager {
        return PoolingHttpClientConnectionManagerBuilder.create()
            .setMaxConnTotal(20)
            .setMaxConnPerRoute(5)
            .build()
    }
}
