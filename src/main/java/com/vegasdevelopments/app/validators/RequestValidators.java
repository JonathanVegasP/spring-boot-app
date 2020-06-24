package com.vegasdevelopments.app.validators;

import com.vegasdevelopments.app.enums.HeadersState;
import com.vegasdevelopments.app.repositories.MD5Repository;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.concurrent.Callable;

public class RequestValidators {
    private RequestValidators() {
    }

    public static ResponseEntity<Map<String, Object>> responseEntity(Map<String, String> headers,Callable<ResponseEntity<Map<String, Object>>> callable) {
        try {
            switch (validateHeaders(headers)) {
                case missing_db:
                    return ResponseEntity
                            .badRequest()
                            .header("error", "missing db header value")
                            .build();
                case missing_key:
                    return ResponseEntity
                            .badRequest()
                            .header("error", "missing key header value")
                            .build();
                case invalid_key:
                    return ResponseEntity
                            .badRequest()
                            .header("error", "invalid key header value")
                            .build();
                case valid_key:
                    return callable.call();
                default:
                    return ResponseEntity
                            .badRequest()
                            .build();

            }
        } catch (Exception exception) {
            return ResponseEntity
                    .badRequest()
                    .header("error", exception.toString())
                    .build();
        }
    }

    private static HeadersState validateHeaders(Map<String, String> headers) throws Exception {
        if (!headers.containsKey("db")) {
            return HeadersState.missing_db;
        }
        if (!headers.containsKey("key")) {
            return HeadersState.missing_key;
        }
        final String db = headers.get("db") + ":key";
        final String key = headers.get("key");
        final String rawKey = MD5Repository.encode(db);
        if (!rawKey.equalsIgnoreCase(key)) {
            return HeadersState.invalid_key;
        }
        return HeadersState.valid_key;
    }
}
