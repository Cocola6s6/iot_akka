package com.akka.test.callback;

import com.google.common.util.concurrent.*;

import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;


public class AsyncCallbackTemplate {
    public static <T> void withCallbackAndTimeout(ListenableFuture<T> future,
                                                  Consumer<T> onSuccess,
                                                  Consumer<Throwable> onFailure,
                                                  long timeoutInMs,
                                                  ScheduledExecutorService timeoutExecutor,
                                                  Executor callbackExecutor) {
        future = Futures.withTimeout(future, timeoutInMs, TimeUnit.MILLISECONDS, timeoutExecutor);
        withCallback(future, onSuccess, onFailure, callbackExecutor);
    }

    public static <T> void withCallback(T message, Consumer<T> onSuccess,
                                        Consumer<Throwable> onFailure, Executor executor) {
        SettableFuture<T> future = SettableFuture.create();
        future.set(message);
        withCallback(future, onSuccess,
                onFailure, executor);
    }


    public static <T> void withCallback(ListenableFuture<T> future, Consumer<T> onSuccess,
                                        Consumer<Throwable> onFailure, Executor executor) {
        FutureCallback<T> callback = new FutureCallback<T>() {
            @Override
            public void onSuccess(T result) {
                try {
                    onSuccess.accept(result);
                } catch (Throwable th) {
                    onFailure(th);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                onFailure.accept(t);
            }
        };
        if (executor != null) {
            Futures.addCallback(future, callback, executor);
        } else {
            Futures.addCallback(future, callback, MoreExecutors.directExecutor());
        }
    }
}
