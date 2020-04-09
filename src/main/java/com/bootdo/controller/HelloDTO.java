package com.bootdo.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HelloDTO {
    private String comName;
    private String comCode;
    private String ipoDate;
    private Long time;
}
