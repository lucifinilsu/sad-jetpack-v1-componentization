package com.sad.jetpack.v1.componentization.demo.module1;

public interface INetDataObtainedExceptionListener<RQ> {

    void onDataObtainedException(INetDataRequest<RQ> request,Throwable throwable);

}
