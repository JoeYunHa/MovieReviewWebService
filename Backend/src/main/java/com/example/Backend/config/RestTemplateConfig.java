package com.example.Backend.config;

import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.util.Timeout;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestTemplateConfig {
    /**
     * TMDB API 호출을 위한 RestTemplate Bean 생성
     * => 커넥션 풀 최적화
     */
    @Bean
    public RestTemplate restTemplate(){
        // Connection pool manager 설정
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(100); // 최대 커넥션 수 : 100
        connectionManager.setDefaultMaxPerRoute(20); // 라우트당 최대 커넥션 수

        // request 설정
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(Timeout.of(Duration.ofSeconds(5))) // connection pool에서 connection을 얻는 타임아웃
                .setConnectTimeout(Timeout.of(Duration.ofSeconds(10))) // server와의 연결 timeout
                .setResponseTimeout(Timeout.of(Duration.ofSeconds(30)))
                .build();

        // HttpClient 생성
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .build();

        // HttpComponentsClientHttpRequestFactory 설정
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setHttpClient(httpClient);

        // Rest Template 생성 및 반환
        return new RestTemplate(factory);
    }
}
