package com.vegasdevelopments.app.controllers;

import com.vegasdevelopments.app.validators.RequestValidators;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@CrossOrigin
public class MainController {
    @GetMapping
    public ResponseEntity<Map<String, Object>> author(@RequestHeader Map<String, String> headers) {
        return RequestValidators.responseEntity(headers, GetController::author);
    }
}
