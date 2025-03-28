package com.killianpavy.insight.service;

public interface Callback<T> {
    void onMessage(T t);
}
