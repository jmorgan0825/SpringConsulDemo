package com.bettercloud.spring.cloud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/client")
public class ClientController {

    private ServiceClient serviceClient;

    @Autowired
    public ClientController(final ServiceClient serviceClient) {
        this.serviceClient = serviceClient;
    }

    @GetMapping
    public String getService() {
        return serviceClient.getId();
    }

    @FeignClient(name = "service-holding")
    public interface ServiceClient {

        @GetMapping("/me")
        String getId();

    }

}
