package com.sad.jetpack.v1.componentization.demo.module1;


public interface INetDataProcessorMaster<RQ,RP> {

    INetDataProcessorMaster request(INetDataRequest<RQ> request);

    INetDataProcessorMaster callback(IDataObtainedCallback callback);

    INetDataProcessorMaster engine(IEngine engine);

    void execute();

    interface IDataObtainedCallback<RQ,RP>{
        void onDataObtainedCompleted(INetDataResponse<RQ,RP> response);
        void onDataObtainedException(INetDataRequest<RQ> request,Throwable throwable);
    }

    interface IEngine<RQ,RP> {

        void onDoExecute(INetDataProcessorMasterHolder<RQ,RP> holder);

    }

    interface INetDataProcessorMasterHolder<RQ,RP>{

        INetDataRequest<RQ> request();

        IDataObtainedCallback<RQ,RP> callback();

    }
}
