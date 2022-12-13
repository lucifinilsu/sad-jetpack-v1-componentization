package com.sad.jetpack.v1.componentization.demo.module1;

public abstract class CacheInterceptor<RQ,String> implements INetDataInterceptorInput<RQ,String>,INetDataInterceptorOutput<RQ,String>{
    @Override
    public void onInterceptedInput(INetDataChainInput<RQ, String> chainInput) throws Exception {

    }

    @Override
    public void onInterceptedOutput(INetDataChainOutput<RQ, String> chainOutput) throws Exception {

    }
}
