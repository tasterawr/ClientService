package org.loktevik.netcracker.service;

import org.loktevik.netcracker.controllers.dto.OrderDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Future;

public interface OrderService {
    Future<ResponseEntity> createNewOrderAsync(RestTemplate restTemplate, String url, HttpEntity<OrderDto> request);
}
