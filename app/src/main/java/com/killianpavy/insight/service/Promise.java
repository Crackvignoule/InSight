package com.killianpavy.insight.service;

public class Promise<T> {

    Callback<T> promiseThen;
    Callback<Exception> promiseCatch;

    public Promise<T> then(Callback<T> promiseThen) {
        this.promiseThen = promiseThen;
        return this;
    }

    public Promise<T> catchError(Callback<Exception> promiseCatch) {
        this.promiseCatch = promiseCatch;
        return this;
    }


}
