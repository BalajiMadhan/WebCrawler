package com.ge.webcrawler.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Page {

    private String       address;
    private List<String> links;

}
