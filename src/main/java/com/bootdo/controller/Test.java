package com.bootdo.controller;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/test")
public class Test {

    @RequestMapping(path = "/getid/{id}")
    private void getId(@PathVariable String id) {
        if (!StringUtils.isEmpty(id)) {
            System.out.println(id);
        }
    }
}
