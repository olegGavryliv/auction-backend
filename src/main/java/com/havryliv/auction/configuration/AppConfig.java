package com.havryliv.auction.configuration;

import com.havryliv.auction.enums.BadWordsCheck;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties("app")
@Data
@PropertySource(factory = YamlPropertySourceFactory.class, value = "classpath:settings/app-configs.yml")
public class AppConfig {

    private String name;
    private String environment;
    private List<LimitationSettings> limitationSettings = new ArrayList<>();
    private AuctionSettings auctionSettings = new AuctionSettings();

    @Data
    public static class LimitationSettings {
        private Long maxAllowedProducts;
        private BadWordsCheck badWordsCheck;
        private List<String> reservedProductNames = new ArrayList<>();
    }

    @Data
    public static class AuctionSettings {
        private Duration averageAuctionDuration;
        private List<String> categories = new ArrayList<>();

    }

}
