package com.havryliv.auction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableCaching
@EnableJpaRepositories("com.havryliv.auction.repository.jpa")
public class AuctionBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuctionBackApplication.class, args);
    }

}
