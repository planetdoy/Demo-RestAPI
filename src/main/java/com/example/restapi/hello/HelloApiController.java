package com.example.restapi.hello;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloApiController {

    @GetMapping("/hello-world")
    public String hello() {
        return "HelloWorld";
    }

    @GetMapping("/hello-world-bean")
    public HelloWorldBean bean() {
        return new HelloWorldBean("Hello world");
    }

    @GetMapping("/path-variable/{name}")
    public HelloWorldBean bean(@PathVariable String name) {
        return new HelloWorldBean(String.format("Hello , %s", name));
    }

}
