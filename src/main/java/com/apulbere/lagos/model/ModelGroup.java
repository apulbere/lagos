package com.apulbere.lagos.model;

import java.util.List;

public interface ModelGroup<T> {
    List<Model<T>> getChildren();
    void setValue(Model<T> value);
}
