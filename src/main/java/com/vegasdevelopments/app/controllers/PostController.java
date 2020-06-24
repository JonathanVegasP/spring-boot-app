package com.vegasdevelopments.app.controllers;

import com.vegasdevelopments.app.repositories.DBRepository;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public class PostController {
    private PostController() {
    }

    public static ResponseEntity<Map<String, Object>> postTable(String db, String table, Map<String, Object> body) throws Exception {
        final Map<String, Object> result = DBRepository.postTable(db, table, body);
        DBRepository.dispose();
        final String error = "error";
        if (result.containsKey(error)) {
            return ResponseEntity.badRequest().header(error, (String) result.get(error)).build();
        }
        return ResponseEntity.ok(result);
    }
}
