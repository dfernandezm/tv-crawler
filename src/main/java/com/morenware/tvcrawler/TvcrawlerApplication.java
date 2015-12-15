package com.morenware.tvcrawler;

import com.morenware.tvcrawler.config.MainConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(MainConfig.class)
public class TvCrawlerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TvCrawlerApplication.class, args);
    }
}
