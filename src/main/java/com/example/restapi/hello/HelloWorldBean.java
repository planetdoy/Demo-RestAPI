package com.example.restapi.hello;

import lombok.Data;

@Data
public class HelloWorldBean {
    private String message;

    public HelloWorldBean(String message) {
        this.message = message;
    }
}
