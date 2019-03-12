package com.ge.webcrawler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.ge.webcrawler.service.WebCrawlerService;
import com.ge.webcrawler.util.WebCrawlerConstant;

@SpringBootApplication
public class WebCrawlerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebCrawlerApplication.class, args);
        WebCrawlerService crawlService;

        for (int fileNumber = 1; fileNumber < 4; fileNumber++) {
            crawlService = new WebCrawlerService(WebCrawlerConstant.INTERNET + fileNumber + WebCrawlerConstant.TYPE);
            crawlService.start();
        }

    }

}
