package com.vegasdevelopments.app.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vegasdevelopments.app.models.App;
import com.vegasdevelopments.app.validators.UserValidators;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class DBRepository {
    private static AppRepository repository;

    private DBRepository() {
    }

    public static void init(AppRepository repository) {
        DBRepository.repository = repository;
    }

    public static void dispose() {
        repository = null;
    }

    private static Optional<App> getApp() {
        if (repository == null) {
            return Optional.empty();
        }
        return repository.findById(1L);
    }

    public static Map<String, Object> getTable(String db, String table) throws Exception {
        final Optional<App> app = getApp();
        if (!app.isPresent()) {
            return null;
        }
        AesRepository.init(db);
        System.out.println(app.get().getJson());
        final Map<String, Object> json = new ObjectMapper().readValue(AesRepository.decode(app.get().getJson()), HashMap.class);
        AesRepository.dispose();
        if (!json.containsKey(db)) {
            return null;
        }
        final Map<String, Object> values = (HashMap) json.get(db);
        if (!values.containsKey(table)) {
            return null;
        }
        final Map<String, Object> body = new HashMap<>();
        body.put(table, values.get(table));
        return body;
    }

    public static Map<String, Object> getTableById(String db, String table, String id) throws Exception {
        final Map<String, List<Map<String, Object>>> json = (HashMap) getTable(db, table);
        if (json == null) {
            return null;
        }
        final Optional<Map<String, Object>> result = json
                .get(table)
                .stream()
                .filter(map -> id.equalsIgnoreCase(Long.toString((long) map.get("id"))))
                .findFirst();
        if (!result.isPresent()) {
            return null;
        }
        final Map<String, Object> body = new HashMap<>();
        body.put(table, result.get());
        return body;
    }

    public static Map<String, Object> postTable(String db, String table, Map<String, Object> requestBody) throws Exception {
        final Optional<App> required = getApp();
        final ObjectMapper mapper = new ObjectMapper();
        final App app = required.orElseGet(App::new);
        final Map<String, Object> json = new HashMap<>();
        final Map<String, Object> values = new HashMap<>();
        final List<Map<String, Object>> value = new ArrayList<>();
        final Map<String, Object> body = new HashMap<>();
        AesRepository.init(db);
        if (app.getJson() != null) {
            json.putAll(mapper.readValue(app.getJson(), HashMap.class));
        }
        if (json.containsKey(db)) {
            values.putAll((HashMap) json.get(db));
        }
        if (values.containsKey(table)) {
            value.addAll((List) values.get(table));
        }
        if (table.equalsIgnoreCase("user")) {
            if (!requestBody.containsKey("email")) {
                body.put("error", "email key is required");
                return body;
            }
            final String email = (String) requestBody.get("email");
            if (!UserValidators.emailValidator(email)) {
                body.put("error", "email key is invalid");
                return body;
            }
            if (!requestBody.containsKey("password")) {
                body.put("error", "password key is required");
                return body;
            }
            final String password = (String) requestBody.get("password");
            if (!UserValidators.passwordValidator(password)) {
                body.put("error", "password key must have 8 or more characters");
                return body;
            }
            if (value
                    .stream()
                    .anyMatch(map -> email.equalsIgnoreCase((String) map.get("email")))) {
                body.put("error", "This email is already in use");
                return body;
            }
            requestBody.put("id", AesRepository.encodeWithoutDecoder(email));
            requestBody.put("password", AesRepository.encode(password));
        } else {
            requestBody.put("id", Base64
                    .getEncoder()
                    .encodeToString(Long
                            .toString(System.currentTimeMillis())
                            .getBytes(StandardCharsets.UTF_8)));
        }
        value.add(requestBody);
        values.put(table, value);
        json.put(db, values);
        System.out.println(AesRepository.encode(mapper.writeValueAsString(json)));
        app.setJson(AesRepository.encode(mapper.writeValueAsString(json)));
        AesRepository.dispose();
        repository.save(app);
        body.put(table, requestBody);
        return body;
    }
}
