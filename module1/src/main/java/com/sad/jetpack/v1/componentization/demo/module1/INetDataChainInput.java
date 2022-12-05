package com.sad.jetpack.v1.componentization.demo.module1;

public interface INetDataChainInput<RQ,RP> {

    INetDataRequest<RQ> request();

    void proceed(INetDataRequest<RQ> request);

    INetDataObtainedCallback<RQ,RP> callback();

    INetDataObtainedExceptionListener<RQ> exceptionListener();

    default void proceed(){
        proceed(request());
    }
}
