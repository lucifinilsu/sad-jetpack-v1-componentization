package com.sad.jetpack.v1.componentization.demo.module1;

public interface INetDataInterceptorOutput<RQ,RP> {

    void onInterceptedOutput(INetDataChainOutput<RQ,RP> chainOutput) throws Exception;

}
