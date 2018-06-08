package com.apulbere.lagos.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Group {
    private String name;
    private List<Long> customerIds = new ArrayList<>();

    public Group() {
    }

    public Group(String name, List<Long> customerIds) {
        this.name = name;
        this.customerIds = customerIds;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getCustomerIds() {
        return customerIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return Objects.equals(name, group.name) &&
                Objects.equals(customerIds, group.customerIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, customerIds);
    }
}
