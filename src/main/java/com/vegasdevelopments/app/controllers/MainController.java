package com.vegasdevelopments.app.controllers;

import com.vegasdevelopments.app.repositories.AppRepository;
import com.vegasdevelopments.app.repositories.DBRepository;
import com.vegasdevelopments.app.validators.RequestValidators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin
public class MainController {
    @Autowired
    AppRepository repository;

    @GetMapping
    public ResponseEntity<Map<String, Object>> author() {
        return GetController.author();
    }

    @GetMapping({"/api/{table}", "/api/{table}/{id}"})
    public ResponseEntity<Map<String, Object>> getTable(@RequestHeader Map<String, String> headers, @PathVariable Map<String, String> path) {
        return RequestValidators.requestValidator(headers, () -> {
            DBRepository.init(repository);
            if (!path.containsKey("id")) {
                return GetController.getTable(headers.get("db"), path.get("table"));
            }
            return GetController.getTableById(headers.get("db"), path.get("table"), path.get("id"));
        });
    }

    @PostMapping("/api/{table}")
    public ResponseEntity<Map<String, Object>> postTable(@RequestHeader Map<String, String> headers, @PathVariable String table, @RequestBody Map<String, Object> body) {
        return RequestValidators.requestValidator(headers, () -> {
            DBRepository.init(repository);
            return PostController.postTable(headers.get("db"), table, body);
        });
    }
}
