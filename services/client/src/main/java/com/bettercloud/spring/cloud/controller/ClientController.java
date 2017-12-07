package com.bettercloud.spring.cloud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController("/client")
public class ClientController {

    private DiscoveryClient discoveryClient;
    private RestTemplate restTemplate;

    @Autowired
    public ClientController(final DiscoveryClient discoveryClient, final RestTemplate restTemplate) {
        this.discoveryClient = discoveryClient;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/services/{applicationName}")
    public List<ServiceInstance> serviceInstances(@PathVariable String applicationName) {
        return this.discoveryClient.getInstances(applicationName);
    }

    @GetMapping
    public String getService() {
        return restTemplate.getForEntity("http://service/me", String.class).getBody();
    }

}
