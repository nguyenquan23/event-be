package com.example.localguidebe;

import com.example.localguidebe.crypto.configuration.CoinpaymentsProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

@Slf4j
@EnableFeignClients
@EnableConfigurationProperties(CoinpaymentsProperties.class)
@SpringBootApplication
public class LocalGuideBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(LocalGuideBeApplication.class, args);
    }

}
