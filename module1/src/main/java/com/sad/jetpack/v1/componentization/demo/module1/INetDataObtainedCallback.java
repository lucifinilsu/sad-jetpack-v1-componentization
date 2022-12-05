package com.sad.jetpack.v1.componentization.demo.module1;

public interface INetDataObtainedCallback<RQ,RP> {

    void onDataObtainedCompleted(INetDataResponse<RQ,RP> response);

}
