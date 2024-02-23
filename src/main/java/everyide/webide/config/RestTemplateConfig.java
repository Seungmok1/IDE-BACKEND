package everyide.webide.config;

import org.apache.hc.client5.http.classic.HttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

@Configuration
public class RestTemplateConfig {

    @Bean(name = "proxyRestTemplate")
    public RestTemplate restTemplate() {
        // 프록시 설정
        HttpHost proxy = new HttpHost("krmp-proxy.9rum.cc", 3128);

        // 요청 구성을 위한 RequestConfig 생성
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(10000)  // 읽기(소켓) 타임아웃을 10초로 설정
                .setConnectTimeout(5000)  // 연결 타임아웃을 5초로 설정
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