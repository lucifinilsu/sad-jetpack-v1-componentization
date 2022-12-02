package com.sad.jetpack.v1.componentization.demo.module1;

public interface INetDataEngineResponseContinuer<RQ,RP> {

    void continueHandleResponse(INetDataResponse<RQ,RP> response);

}
