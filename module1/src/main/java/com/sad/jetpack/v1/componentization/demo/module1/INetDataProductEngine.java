package com.sad.jetpack.v1.componentization.demo.module1;

public interface INetDataProductEngine<RQ,RP> {

    void onEngineExecute(INetDataRequest<RQ> request,INetDataChainOutput<RQ,RP> chainOutput) throws Exception;

}
