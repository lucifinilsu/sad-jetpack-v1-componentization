package com.sad.jetpack.v1.componentization.demo.module1;

import java.util.ArrayList;
import java.util.List;

public class InternalNetDataChainOutput<RQ,RP> implements INetDataChainOutput<RQ,RP>{
    private INetDataResponse<RQ, RP> response;
    private List<INetDataInterceptorOutput<RQ,RP>> interceptorOutputs=new ArrayList<>();
    private int currIndex=-1;
    private INetDataObtainedCallback<RQ,RP> callback;
    private INetDataObtainedExceptionListener<RQ> exceptionListener;

    public InternalNetDataChainOutput(
            List<INetDataInterceptorOutput<RQ, RP>> interceptorOutputs,
            INetDataObtainedCallback<RQ, RP> callback,
            INetDataObtainedExceptionListener<RQ> exceptionListener
    ) {
        this.interceptorOutputs = interceptorOutputs;
        this.callback = callback;
        this.exceptionListener=exceptionListener;
    }

    @Override
    public INetDataResponse<RQ, RP> response() {
        return response;
    }

    @Override
    public INetDataObtainedCallback<RQ, RP> callback() {
        return this.callback;
    }

    @Override
    public INetDataObtainedExceptionListener<RQ> exceptionListener() {
        return this.exceptionListener;
    }


    @Override
    public void proceed(INetDataResponse<RQ, RP> response) {
        this.response=response;
        this.currIndex++;
        if (currIndex>interceptorOutputs.size()-1){
            if (callback!=null){
                callback.onDataObtainedCompleted(response);
            }
        }
        else {
            INetDataInterceptorOutput<RQ,RP> interceptorOutput=interceptorOutputs.get(currIndex);
            try {
                interceptorOutput.onInterceptedOutput(this);
            }catch (Exception e){
                e.printStackTrace();
                if (exceptionListener!=null){
                    exceptionListener.onDataObtainedException(response!=null?response.request():null,e);
                }
            }

        }
    }
}
