package com.hackers.core.models;

import java.io.Serializable;

/**
 * Created by SR on 25/11/17.
 */

public class TopStories implements Serializable {

    private String id;

    public TopStories(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
