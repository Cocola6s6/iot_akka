package com.akka.test.callback;

public interface SimpleServiceCallback<T> extends CallBack {

    void onSuccess(T msg);

    void onError(Throwable e);

    SimpleServiceCallback<Void> EMPTY = new SimpleServiceCallback<Void>() {
        @Override
        public void onSuccess(Void msg) {
//
        }

        @Override
        public void onError(Throwable e) {
//
        }
    };
}
