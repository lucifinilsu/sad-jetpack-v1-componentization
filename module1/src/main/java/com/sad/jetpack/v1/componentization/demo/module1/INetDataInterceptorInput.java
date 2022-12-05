package com.sad.jetpack.v1.componentization.demo.module1;

public interface INetDataInterceptorInput<RQ,RP> {

    void onInterceptedInput(INetDataChainInput<RQ,RP> chainInput) throws Exception;

}
