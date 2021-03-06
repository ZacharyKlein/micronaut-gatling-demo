package com.objectcomputing;

import io.micronaut.core.annotation.Introspected;

@Introspected
public class WidgetDTO {

    private String name;
    private Integer size;

    public WidgetDTO(String name, Integer size) {
        this.name = name;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }



}
