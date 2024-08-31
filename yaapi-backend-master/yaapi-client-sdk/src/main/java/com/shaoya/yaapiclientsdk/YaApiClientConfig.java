package com.shaoya.yaapiclientsdk;

import com.shaoya.yaapiclientsdk.client.YaApiClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ComponentScan
@ConfigurationProperties("yaapi.client")
public class YaApiClientConfig {
    private String accessKey;
    private String secretKey;
    @Bean
    public YaApiClient yaApiClient() {
        return new YaApiClient(accessKey, secretKey);
    }
}
