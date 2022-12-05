package com.sad.jetpack.v1.componentization.demo.module1;


import java.util.List;

public interface INetDataProcessorMaster<RQ,RP> {

    INetDataProcessorMaster<RQ,RP> request(INetDataRequest<RQ> request);

    INetDataProcessorMaster<RQ,RP> addInputInterceptor(INetDataInterceptorInput<RQ,RP> input);

    INetDataProcessorMaster<RQ,RP> interceptorInputs(List<INetDataInterceptorInput<RQ,RP>> interceptorInputs);

    INetDataProcessorMaster<RQ,RP> addOutputInterceptor(INetDataInterceptorOutput<RQ,RP> output);

    INetDataProcessorMaster<RQ,RP> interceptorOutputs(List<INetDataInterceptorOutput<RQ,RP>> interceptorOutputs);

    INetDataProcessorMaster<RQ,RP> callback(INetDataObtainedCallback<RQ,RP> callback);

    INetDataProcessorMaster<RQ,RP> exceptionListener(INetDataObtainedExceptionListener<RQ> exceptionListener);

    INetDataProcessorMaster<RQ,RP> engine(INetDataProductEngine<RQ,RP> engine);

    void execute();

}
