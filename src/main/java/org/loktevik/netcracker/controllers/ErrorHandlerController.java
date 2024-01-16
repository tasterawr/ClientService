package org.loktevik.netcracker.controllers;

import org.loktevik.netcracker.controllers.dto.ExceptionDto;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@Controller
public class ErrorHandlerController implements ErrorController {

    @RequestMapping("/error")
    public ModelAndView handleError(HttpServletRequest request){
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Enumeration<String> params = request.getParameterNames();

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                return new ModelAndView("errors/not_found");
            }
            else if(statusCode == HttpStatus.BAD_REQUEST.value()) {
                return new ModelAndView("errors/bad_request").addObject("errorMessage", "mes");
            }
        }

        return new ModelAndView();
    }
}
