package org.loktevik.netcracker.service;

import lombok.SneakyThrows;
import org.loktevik.netcracker.controllers.dto.OrderDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Future;

@Service
public class OrderServiceImpl implements OrderService{
    @SneakyThrows
    @Async
    @Override
    public Future<ResponseEntity> createNewOrderAsync(RestTemplate restTemplate, String url, HttpEntity<OrderDto> nextRequest) {
        Thread.sleep(5000);
        return new AsyncResult<ResponseEntity>(restTemplate.postForEntity(url, nextRequest, Object.class));
    }
}
