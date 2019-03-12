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

    /**
     * @param jsonString
     * @throws IOException
     * @method mappingFromJsonFile
     * @Description This method map the string extracted from internet file to
     *              Internet object
     */
    public static Internet mappingFromJsonFile(String jsonString) throws IOException {
        return mapper.readValue(jsonString, Internet.class);
    }

    /**
     * @param address
     * @method
     * @Description This method find the page if the requested address present in
     *              internet object
     */
    public Optional<Page> findPage(String address) {
        return pages.stream().filter(page -> page.getAddress().equals(address)).findFirst();
    }

}