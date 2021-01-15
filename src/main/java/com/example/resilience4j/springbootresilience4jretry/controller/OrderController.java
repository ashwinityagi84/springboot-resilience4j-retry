package com.example.resilience4j.springbootresilience4jretry.controller;


import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    private static final String ORDERSERVICE ="orderService" ;
    @Autowired
    private RestTemplate restTemplate;

    private int attempts=1;

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @GetMapping("/order")
    @Retry(name = ORDERSERVICE,fallbackMethod = "fallback_retry")
    public ResponseEntity<String> createOrder(){
        logger.info("item service call attempted:::"+ attempts++);
        String response = restTemplate.getForObject("http://localhost:8081/item", String.class);
        logger.info("item service called");
        return new ResponseEntity<String>(response, HttpStatus.OK);
    }
public ResponseEntity<String> fallback_retry(Exception e){
        attempts=1;
    return new ResponseEntity<String>("Item service is down", HttpStatus.INTERNAL_SERVER_ERROR);

}


}
