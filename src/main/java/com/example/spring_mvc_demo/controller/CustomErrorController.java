package com.example.spring_mvc_demo.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.Response;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
     public ResponseEntity<Map<String,Object>> handleError(HttpServletRequest request) {
        Map<String,Object> response = new HashMap<>();
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        response.put("status",statusCode);
        response.put("message","Something went wrong");

        return new ResponseEntity<>(response, HttpStatus.valueOf(statusCode));
    }

    public String getErrorPath()
    {
        return "/error";
    }

}
