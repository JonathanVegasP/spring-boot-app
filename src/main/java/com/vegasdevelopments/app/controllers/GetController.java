package com.vegasdevelopments.app.controllers;

import com.vegasdevelopments.app.repositories.DBRepository;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class GetController {
    private GetController() {
    }

    public static ResponseEntity<Map<String, Object>> author() {
        final Map<String, Object> author = new HashMap<>();
        author.put("name", "Jonathan Vegas Peixoto");
        author.put("email", "jopxoto12@gmail.com");
        author.put("phone", "+55 11 94674-5479");
        author.put("linkedin", "https://www.linkedin.com/in/jonathan-vegas-peixoto-5064a216a/");
        author.put("github", "https://github.com/JonathanVegasP");
        author.put("website", "https://www.vegasdevelopments.com");
        return ResponseEntity.ok(author);
    }

    public static ResponseEntity<Map<String, Object>> getTable(String db, String table) throws Exception {
        final Map<String, Object> body = DBRepository.getTable(db, table);
        DBRepository.dispose();
        if(body == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(body);
    }

    public static ResponseEntity<Map<String, Object>> getTableById(String db, String table, String id) throws Exception {
        final Map<String, Object> body = DBRepository.getTableById(db, table, id);
        DBRepository.dispose();
        if(body == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(body);
    }
}
