package com.apulbere.lagos.pipedstream;

import java.io.InputStream;
import java.util.concurrent.Callable;

public abstract class CallableWithInputStream<T> implements Callable<T> {
    protected InputStream inputStream;

    void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
}
