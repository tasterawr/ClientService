package org.loktevik.netcracker.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.loktevik.netcracker.controllers.dto.CustomerInfoDto;
import org.loktevik.netcracker.controllers.dto.CustomerUpdateDto;
import org.loktevik.netcracker.security.utils.AccessTokenHandler;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Controller
@RequiredArgsConstructor
public class CustomerController {
    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/home")
    public ModelAndView homePage(HttpServletRequest request) throws JsonProcessingException {
        String url = "http://localhost:8081/customers";
        ResponseEntity<CustomerInfoDto> response = restTemplate.exchange(
                url, HttpMethod.GET, AccessTokenHandler.getHttpEntity(CustomerInfoDto.class, request.getCookies()), CustomerInfoDto.class
        );

        ModelAndView mv = new ModelAndView("customer/customer_home");
        HashMap<String,Object> result = new ObjectMapper().readValue(new JSONObject(response.getBody()).toString(), HashMap.class);
        mv.addObject("customerInfo", result);
        mv.addObject("paidTypes", result.get("paidTypes"));
        mv.addObject("address", result.get("address"));
        return mv;
    }
    
    @PostMapping("/update-info")
    public ResponseEntity<?> updateCustomer(HttpServletRequest request, @RequestParam String param){
        String newValue = request.getParameter("newValue");
        CustomerUpdateDto updateDto = new CustomerUpdateDto();
        updateDto.setParameter(param);
        updateDto.setNewValue(newValue);

        HttpEntity<CustomerUpdateDto> nextRequest = AccessTokenHandler.getHttpEntity(updateDto, request.getCookies());
        String url = "http://localhost:8081/customers";
        restTemplate.put(url, nextRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
