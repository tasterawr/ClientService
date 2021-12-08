package org.loktevik.netcracker.controllers;

import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.loktevik.netcracker.controllers.dto.CustomerInfoDto;
import org.loktevik.netcracker.controllers.dto.PaidTypeDto;
import org.loktevik.netcracker.controllers.dto.UserLoginDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class EntryController {
    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/sign-in")
    public ModelAndView signInPage(){
        String url = "http://localhost:8081/paidtypes";
        List<PaidTypeDto> paidTypeDtos = restTemplate.getForObject(url, List.class);
        ModelAndView model = new ModelAndView("login_sign_in/sign-in");
        model.addObject("paidTypes", paidTypeDtos);
        return model;
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> saveCustomer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        CustomerInfoDto info = new CustomerInfoDto();
        info.setFirstName(request.getParameter("firstName"));
        info.setLastName(request.getParameter("lastName"));
        info.setEmail(request.getParameter("email"));
        info.setPhone(request.getParameter("phone"));
        info.setUsername(request.getParameter("username"));
        info.setPassword(request.getParameter("password"));
        info.setRoles(new String[]{"USER"});
        info.setAddress(new String[]{
                request.getParameter("country"),
                request.getParameter("state"),
                request.getParameter("city")
        });
        String url = "http://localhost:8081/paidtypes";
        List<LinkedHashMap> paidTypeDtos = restTemplate.getForObject(url, List.class);
        List<String> paidTypeNames = new ArrayList<>();

        for (LinkedHashMap dto : paidTypeDtos){
            if (request.getParameter(dto.get("id").toString()) != null){
                paidTypeNames.add((String) dto.get("name"));
            }
        }
        info.setPaidTypes(paidTypeNames.toArray(new String[0]));
        HttpEntity<CustomerInfoDto> nextRequest = new HttpEntity<>(info);
        url = "http://localhost:8081/customers";
        restTemplate.postForLocation(url, nextRequest, String.class);

        response.sendRedirect("/sign-in/success");
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/sign-in/success")
    public ModelAndView signInSuccessPage(){
        return new ModelAndView("login_sign_in/sign-in-success");
    }

    @GetMapping("/login")
    public ModelAndView logInPage(){
        return new ModelAndView("login_sign_in/log-in");
    }

    @PostMapping("/login")
    public String doLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Cookie[] cookies = request.getCookies();
        UserLoginDto userLoginDto = new UserLoginDto(request.getParameter("username"), request.getParameter("password"));
        HttpEntity<UserLoginDto> nextRequest = new HttpEntity<>(userLoginDto);

        String url = "http://localhost:8081/login";
        ResponseEntity<String> postResponse = restTemplate.postForEntity(url, nextRequest, String.class);
        JSONObject jsonObject = new JSONObject(postResponse.getBody());
        response.addCookie(new Cookie("access_token", jsonObject.getString("access_token")));
        response.addCookie(new Cookie("refresh_token", jsonObject.getString("refresh_token")));

        return "redirect:/home";
    }
}
