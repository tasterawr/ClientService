package org.loktevik.netcracker.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.loktevik.netcracker.controllers.dto.*;
import org.loktevik.netcracker.controllers.utils.URLProvider;
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
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class CustomerController {
    private final RestTemplate restTemplate;

    @GetMapping("/home")
    public ModelAndView homePage(HttpServletRequest request) throws JsonProcessingException {
        HashMap<String,Object> result = getCustomerInfo(request);
        ModelAndView mv = new ModelAndView("customer/customer_home");

        String url = URLProvider.getCustomerServiceUrl() + "/paidtypes";
        List<LinkedHashMap> paidTypeDtos = restTemplate.getForObject(url, List.class);
        List<CheckedPaidTypeDto> checkedPaidTypes = new ArrayList<>();
        List<String> customerPaidTypes = (List<String>)result.get("paidTypes");
        for (LinkedHashMap map : paidTypeDtos){
            if (customerPaidTypes.contains(map.get("name"))){
                checkedPaidTypes.add(new CheckedPaidTypeDto(map.get("name").toString(), true));
            } else {
                checkedPaidTypes.add(new CheckedPaidTypeDto(map.get("name").toString(), false));
            }
        }


        mv.addObject("customerInfo", result);
        mv.addObject("paidTypes", checkedPaidTypes);
        mv.addObject("address", result.get("address"));
        return mv;
    }
    
    @PostMapping("/update-info")
    public String updateCustomer(HttpServletRequest request, HttpServletResponse response, @RequestParam String param) throws IOException {
        String newValue = "";
        if (param.equals("paidtypes")){
            Enumeration<String> parameterNames = request.getParameterNames();
            String url = URLProvider.getCustomerServiceUrl() + "/paidtypes";
            List<LinkedHashMap> paidTypeMaps = restTemplate.getForObject(url, List.class);
            StringJoiner joiner = new StringJoiner("-");

            while (parameterNames.hasMoreElements()){
                String name = parameterNames.nextElement();
                for (LinkedHashMap map : paidTypeMaps){
                    if (map.get("name").equals(name)){
                        joiner.add(name);
                    }
                }
            }
            newValue = joiner.toString();
        } else {
            newValue = request.getParameter("newValue");
        }

        CustomerUpdateDto updateDto = new CustomerUpdateDto();
        updateDto.setParameter(param);
        updateDto.setNewValue(newValue);

        HttpEntity<CustomerUpdateDto> nextRequest = AccessTokenHandler.getHttpEntity(updateDto, request.getCookies());
        String url = URLProvider.getCustomerServiceUrl() + "/customers";
        restTemplate.put(url, nextRequest);

        if (param.equals("username") || param.equals("password")){
            return "redirect:/logout";
        }

        return "";
    }

    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(HttpServletRequest request){
        String offerId = request.getParameter("offerId");
        String amount = request.getParameter("amount");
        OrderDto orderInfo = new OrderDto();
        orderInfo.setOfferId(Long.parseLong(offerId));
        orderInfo.setAmount(Integer.parseInt(amount));

        HttpEntity<OrderDto> nextRequest = AccessTokenHandler.getHttpEntity(orderInfo, request.getCookies());
        String url = URLProvider.getCustomerServiceUrl() + "/customers/orders";
        restTemplate.postForEntity(url, nextRequest, Object.class);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/catalogue")
    public ModelAndView cataloguePage(HttpServletRequest request) throws JsonProcessingException {
        String url = URLProvider.getCustomerServiceUrl() + "/customers/offers";
        String paidTypesUrl = URLProvider.getCustomerServiceUrl() + "/paidtypes";
        ResponseEntity<OffersResponseBody> response = restTemplate.exchange(
                url, HttpMethod.GET, AccessTokenHandler.getHttpEntity(OffersResponseBody.class, request.getCookies()), OffersResponseBody.class
        );
        HashMap<String, Object> result = new ObjectMapper().readValue(new JSONObject(response.getBody()).toString(), HashMap.class);
        List<PaidTypeDto> paidTypeDtos = restTemplate.getForObject(paidTypesUrl, List.class);
        HashMap<String,Object> customerInfo = getCustomerInfo(request);

        ModelAndView mv = new ModelAndView("/customer/catalogue");
        mv.addObject("customerInfo", customerInfo);
        mv.addObject("paidtypes", paidTypeDtos);
        mv.addObject("categories", result.get("categories"));
        mv.addObject("offers", result.get("offers"));
        return mv;
    }

    @GetMapping("/basket")
    public ModelAndView basketPage(HttpServletRequest request) throws JsonProcessingException {
        HashMap<String,Object> customerInfo = getCustomerInfo(request);
        String url = String.format(URLProvider.getCustomerServiceUrl() + "/customers/%s/orders", customerInfo.get("id"));
        ResponseEntity<CustomerOrdersDto> response = restTemplate.exchange(
                url, HttpMethod.GET, AccessTokenHandler.getHttpEntity(CustomerOrdersDto.class, request.getCookies()), CustomerOrdersDto.class
        );

        HashMap<String, Object> result = new ObjectMapper().readValue(new JSONObject(response.getBody()).toString(), HashMap.class);

        ModelAndView mv = new ModelAndView("/customer/basket_page");
        mv.addObject("customerInfo", customerInfo);
        mv.addObject("orders", result.get("orders"));

        return mv;
    }

    @PostMapping("/payment")
    public ModelAndView paymentPage(HttpServletRequest request) throws JsonProcessingException {
        HashMap<String,Object> customerInfo = getCustomerInfo(request);
        String[] params = request.getParameter("orderInfo").split(" ");
        String orderId = params[0];
        String offerId = params[1];
        String orderUrl = String.format(URLProvider.getCustomerServiceUrl() + "/customers/order/%s", orderId);
        String offerUrl = String.format(URLProvider.getCustomerServiceUrl() + "/customers/offer/%s", offerId);

        ResponseEntity<OrderDto> orderResponse = restTemplate.exchange(
                orderUrl, HttpMethod.GET, AccessTokenHandler.getHttpEntity(OrderDto.class, request.getCookies()), OrderDto.class
        );
        ResponseEntity<OfferDto> offerResponse = restTemplate.exchange(
                offerUrl, HttpMethod.GET, AccessTokenHandler.getHttpEntity(OfferDto.class, request.getCookies()), OfferDto.class
        );

        HashMap<String, Object> orderMap = new ObjectMapper().readValue(new JSONObject(orderResponse.getBody()).toString(), HashMap.class);
        HashMap<String, Object> offerMap = new ObjectMapper().readValue(new JSONObject(offerResponse.getBody()).toString(), HashMap.class);

        ModelAndView mv = new ModelAndView("/customer/payment-page");
        mv.addObject("customerInfo", customerInfo);
        mv.addObject("order", orderMap);
        mv.addObject("offer", offerMap);

        return mv;
    }

    @PostMapping("/payment-complete")
    public ModelAndView paymentCompletePage(HttpServletRequest request) throws JsonProcessingException {
        HashMap<String,Object> customerInfo = getCustomerInfo(request);
        String orderId = request.getParameter("orderId");
        String orderUrl = String.format(URLProvider.getCustomerServiceUrl() + "/customers/order/%s/pay", orderId);
        restTemplate.postForEntity(orderUrl, AccessTokenHandler.getHttpEntity(OrderDto.class, request.getCookies()), OrderDto.class);

        ModelAndView mv = new ModelAndView("/customer/payment-complete");
        mv.addObject("customerInfo", customerInfo);

        return mv;
    }

    @PostMapping("/delete-order")
    public ResponseEntity<?> deleteOrder(HttpServletRequest request){
        String[] params = request.getParameter("orderInfo").split(" ");
        String orderId = params[0];
        String url = String.format(URLProvider.getCustomerServiceUrl() + "/customers/order/%s/delete", orderId);
        restTemplate.postForEntity(url, AccessTokenHandler.getHttpEntity(OrderDto.class, request.getCookies()), OrderDto.class);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public HashMap<String,Object> getCustomerInfo(HttpServletRequest request) throws JsonProcessingException {
        String url = URLProvider.getCustomerServiceUrl() + "/customers";
        ResponseEntity<CustomerInfoDto> response = restTemplate.exchange(
                url, HttpMethod.GET, AccessTokenHandler.getHttpEntity(CustomerInfoDto.class, request.getCookies()), CustomerInfoDto.class
        );

        return new ObjectMapper().readValue(new JSONObject(response.getBody()).toString(), HashMap.class);
    }
}