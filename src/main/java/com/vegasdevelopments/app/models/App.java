package com.vegasdevelopments.app.models;

import javax.persistence.*;
import java.util.Map;

@Entity
public class App {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(columnDefinition = "jsonb")
    private Map<String,Object> json;

    public App() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Map<String,Object> getJson() {
        return json;
    }

    public void setJson(Map<String,Object> json) {
        this.json = json;
    }
}
