package com.apulbere.lagos.model;

public class Customer {
    private Long id;
    private String name;
    private Long parentId;

    public Customer(Long id, String name, Long parentId) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getParentId() {
        return parentId;
    }
}
