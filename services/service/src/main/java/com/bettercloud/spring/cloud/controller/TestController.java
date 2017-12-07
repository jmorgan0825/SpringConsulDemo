package com.bettercloud.spring.cloud.controller;

import com.bettercloud.spring.cloud.feign.TestFeign;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/ping")
public class TestController {

    @Value("${spring.application.name}")
    private String appName;

    private final RestTemplate restTemplate;
    private final TestFeign testFeign;

    public TestController(RestTemplate restTemplate,
                          TestFeign testFeign) {
        this.restTemplate = restTemplate;
        this.testFeign = testFeign;
    }

    @RequestMapping
    public String doAlive() {
        return "Alive!";
    }

    @RequestMapping("/rest")
    public String doRestAlive() {
        return new RestTemplate().getForObject("http://localhost:8080/ping", String.class);
    }

    @RequestMapping("/rest/ribbon")
    public String doRestAliveUsingEurekaAndRibbon() {
        String url = "http://${spring.application.name}/ping";
        System.out.println("url: "+url);
        return restTemplate.getForObject(url, String.class);
    }

    @RequestMapping("/rest/feign")
    public String doRestAliveUsingFeign() {
        return testFeign.doAlive();
    }
}