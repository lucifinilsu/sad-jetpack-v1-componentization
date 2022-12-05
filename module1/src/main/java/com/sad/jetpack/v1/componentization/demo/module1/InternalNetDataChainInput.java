package com.sad.jetpack.v1.componentization.demo.module1;

import java.util.ArrayList;
import java.util.List;

public class InternalNetDataChainInput<RQ,RP> implements INetDataChainInput<RQ,RP>{
    private INetDataRequest<RQ> request;
    private List<INetDataInterceptorInput<RQ,RP>> interceptorInputs=new ArrayList<>();
    private int currIndex=-1;
    private INetDataObtainedCallback<RQ,RP> callback;
    private INetDataObtainedExceptionListener<RQ> exceptionListener;

    public InternalNetDataChainInput(
            List<INetDataInterceptorInput<RQ,RP>> interceptorInputs,
            INetDataObtainedCallback<RQ, RP> callback,
            INetDataObtainedExceptionListener<RQ> exceptionListener
    ) {
        this.interceptorInputs = interceptorInputs;
        this.callback = callback;
        this.exceptionListener=exceptionListener;
    }


    @Override
    public INetDataRequest<RQ> request() {
        return this.request;
    }

    @Override
    public void proceed(INetDataRequest<RQ> request) {
        this.request=request;
        this.currIndex++;
        if (currIndex>interceptorInputs.size()-1){
            //几乎不会到达这里，因为最后一个是默认内部拦截器（引擎）
            return;
        }
        else {
            INetDataInterceptorInput<RQ,RP> interceptorInput=interceptorInputs.get(currIndex);
            try {
                interceptorInput.onInterceptedInput(this);
            }catch (Exception e){
                e.printStackTrace();
                if (exceptionListener!=null){
                    exceptionListener.onDataObtainedException(request,e);
                }
            }

        }
    }

    @Override
    public INetDataObtainedCallback<RQ, RP> callback() {
        return this.callback;
    }

    @Override
    public INetDataObtainedExceptionListener<RQ> exceptionListener() {
        return this.exceptionListener;
    }

}
