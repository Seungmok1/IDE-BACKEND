package everyide.webide.config;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpHost;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.apache.http.client.config.RequestConfig;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

@Configuration
public class RestTemplateConfig {

    @Bean(name = "proxyRestTemplate")
    public RestTemplate restTemplate() {
        // 프록시 설정
        HttpHost proxy = new HttpHost("krmp-proxy.9rum.cc", 3128);

        // 요청 구성을 위한 RequestConfig 생성
        RequestConfig requestConfig = RequestConfig.custom()
                .build();

        // HttpClient에 프록시 및 RequestConfig 적용
        CloseableHttpClient httpClient = HttpClients.custom()
                .setProxy(proxy)
                .build();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory((HttpClient) httpClient);

        // RestTemplate 인스턴스 생성 및 반환
        return new RestTemplate(factory);
    }
}