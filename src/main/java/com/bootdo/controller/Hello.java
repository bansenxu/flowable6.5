package com.bootdo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/test")
public class Hello {
    @RequestMapping(path = "/hello")
    public HelloDTO hello() {
        HelloDTO dto = new HelloDTO();
        dto.setComCode("0000");
        dto.setComName("腾讯");
        dto.setIpoDate("2019-09-19");
        dto.setTime(System.currentTimeMillis());
        return dto;
    }
}

