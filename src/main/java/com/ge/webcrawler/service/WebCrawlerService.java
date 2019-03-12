package com.ge.webcrawler.service;

import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ge.webcrawler.model.Internet;
import com.ge.webcrawler.model.Page;
import com.ge.webcrawler.model.WebCrawlerResults;
import com.ge.webcrawler.util.WebCrawlerConstant;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;

public class WebCrawlerService implements Runnable {

    private Logger logger = LoggerFactory.getLogger(WebCrawlerService.class);
    private String fileName;
    Thread         crawlThread;

    public WebCrawlerService(String name) {
        fileName = name;
    }

    @Override
    public void run() {
        try {
            String jsonString = loadJsonFileFromResource(fileName);
            Internet internetData = Internet.mappingFromJsonFile(jsonString);
            crawl(internetData, fileName);
        } catch (IOException e) {
            logger.error(WebCrawlerConstant.FILE_NOTFOUND_ERROR + e.getMessage());
        }
    }

    public void start() {
        if (crawlThread == null) {
            crawlThread = new Thread(this, fileName);
            crawlThread.start();
        }
    }

    public WebCrawlerResults crawl(Internet internetData, String fileName) {
        Set<String> visitedLink = new HashSet<>();
        Queue<String> pagesToVisitQueue = new LinkedList<>();
        List<Page> pagesFromInternet = internetData.getPages();
        if (!pagesFromInternet.isEmpty()) {
            pagesToVisitQueue.add(pagesFromInternet.get(0).getAddress());
        }

        WebCrawlerResults crawlerResults = new WebCrawlerResults();
        while (!pagesToVisitQueue.isEmpty()) {
            String nextAddress = pagesToVisitQueue.remove();
            if (visitedLink.contains(nextAddress)) {
                crawlerResults.addSkipped(nextAddress);
            } else {
                final Optional<Page> page = internetData.findPage(nextAddress);
                if (page.isPresent()) {
                    Page nextPage = page.get();
                    crawlerResults.addSuccess(nextPage.getAddress());
                    visitedLink.add(nextPage.getAddress());
                    List<String> links = nextPage.getLinks();
                    pagesToVisitQueue.addAll(links);
                } else {
                    crawlerResults.addError(nextAddress);
                }
            }
        }
        synchronized (WebCrawlerService.class) {
            webCrawlResultDisplay(System.out, crawlerResults, fileName);
        }
        return crawlerResults;
    }

    public void webCrawlResultDisplay(PrintStream out, WebCrawlerResults crawlerResults, String fileName) {
        ObjectMapper mapper = new ObjectMapper();
        out.println(WebCrawlerConstant.OUTPUT + fileName);
        try {
            out.println(WebCrawlerConstant.SUCCESS);
            out.println(mapper.writeValueAsString(crawlerResults.getSuccess()));
            out.println();
            out.println(WebCrawlerConstant.SKIPPED);
            out.println(mapper.writeValueAsString(crawlerResults.getSkipped()));
            out.println();
            out.println(WebCrawlerConstant.ERROR);
            out.println(mapper.writeValueAsString(crawlerResults.getErrors()));
            out.flush();
        } catch (IOException e) {
            logger.error(WebCrawlerConstant.ERROR_MESSAGE + e.getMessage());
        }
        out.println(WebCrawlerConstant.SEPERATOR + WebCrawlerConstant.END_OF_OUTPUT + WebCrawlerConstant.SEPERATOR);
    }

    public String loadJsonFileFromResource(String resourceFile) throws IOException {
        URL url = Resources.getResource(resourceFile);
        return Resources.toString(url, Charsets.UTF_8);
    }

}