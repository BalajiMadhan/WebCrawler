package com.ge.webcrawler.model;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Internet {
    private List<Page>                pages;
    private static final ObjectMapper mapper = new ObjectMapper();

    public static Internet mappingFromJsonFile(String jsonString) throws IOException {
        return mapper.readValue(jsonString, Internet.class);
    }

    public Optional<Page> findPage(String address) {
        return pages.stream().filter(page -> page.getAddress().equals(address)).findFirst();
    }

}