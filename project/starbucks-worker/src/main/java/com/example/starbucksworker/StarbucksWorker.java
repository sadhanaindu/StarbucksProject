package com.example.starbucksworker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

@Component
@RabbitListener(queues = "starbucks")
public class StarbucksWorker {

    private static final Logger log = LoggerFactory.getLogger(StarbucksWorker.class);
    @Autowired
    private StarbucksOrderRepository orders ;
    @Autowired
    RestTemplate eRepo;

    private static final String api_host = System.getenv("API_HOST") != null ? System.getenv("API_HOST") : "34.68.215.55";
    private static final String api_endpoint = "http://" + api_host + ":80/api";
    private static final String api_key = System.getenv("API_KEY") != null ? System.getenv("API_KEY") : "Zkfokey2311";

    @RabbitHandler
    public void processStarbucksOrders(Long id) {
        log.info( "Received  Order # " + id) ;

        // Sleeping to simulate buzy work
        try {
            Thread.sleep(60000); // 60 seconds
        } catch (Exception e) {}


        // /rabbit/order/{regid}
        String getEndpoint = api_endpoint + "/rabbit/order/" + String.valueOf(id) + "?apikey=" + api_key;

        StarbucksOrder order = eRepo.getForObject(getEndpoint, StarbucksOrder.class, String.valueOf(id));
        if(order != null) {
            log.info("Processed Order # " + id);
        } else {
            log.info("[ERROR] Order # " + id + " Not Found!");
        }

    }
}