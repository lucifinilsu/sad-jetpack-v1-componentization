package com.sad.jetpack.v1.componentization.demo.module1;


import androidx.annotation.NonNull;

public interface INetDataChainOutput<RQ,RP> {

    @NonNull INetDataResponse<RQ,RP> response();

    INetDataObtainedCallback<RQ,RP> callback();

    INetDataObtainedExceptionListener<RQ> exceptionListener();

    void proceed(INetDataResponse<RQ,RP> response);

    default void proceed(){
        proceed(response());
    }
}
