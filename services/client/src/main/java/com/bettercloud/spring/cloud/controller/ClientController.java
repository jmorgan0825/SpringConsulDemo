package com.bettercloud.spring.cloud.controller;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
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

    @FeignClient(name = "${bettercloud.service.name}", fallbackFactory = ClientFallbackFactory.class)
    public interface ServiceClient {

        @GetMapping("/me")
        String getId();

    }

    @Component
    static class ClientFallbackFactory implements FallbackFactory<ServiceClient> {

        @Override
        public ServiceClient create(final Throwable cause) {
            return () -> {
                log.error("Error getting id for service", cause);
                return "Failure";
            };
        }

    }

}
