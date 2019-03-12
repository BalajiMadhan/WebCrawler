package com.ge.webcrawler.model;

import java.util.LinkedHashSet;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class WebCrawlerResults {

    private final Set<String> success;
    private final Set<String> skipped;
    private final Set<String> errors;

    public WebCrawlerResults() {
        success = new LinkedHashSet<>();
        skipped = new LinkedHashSet<>();
        errors = new LinkedHashSet<>();
    }

    public boolean addSuccess(String nextAddress) {
        return success.add(nextAddress);
    }

    public boolean addSkipped(String nextAddress) {
        return skipped.add(nextAddress);
    }

    public boolean addError(String nextAddress) {
        return errors.add(nextAddress);
    }

}
