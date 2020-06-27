package com.vegasdevelopments.app.models;

import javax.persistence.*;
import java.nio.charset.StandardCharsets;

@Entity
public class App {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(columnDefinition = "jsonb")
    private byte[] json;

    public App() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getJson() {
        return new String(json, StandardCharsets.UTF_8);
    }

    public void setJson(String json) {
        this.json = json.getBytes(StandardCharsets.UTF_8);
    }
}
