package com.sad.jetpack.v1.componentization.demo.module1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InternalTerminalNetDataInterceptorInput<RQ,RP> implements INetDataInterceptorInput<RQ,RP>{

    private List<INetDataInterceptorOutput<RQ,RP>> interceptorOutputs=new ArrayList<>();
    private INetDataObtainedCallback<RQ,RP> callback;
    private INetDataObtainedExceptionListener<RQ> exceptionListener;
    private INetDataProductEngine<RQ,RP> engine;

    public InternalTerminalNetDataInterceptorInput(List<INetDataInterceptorOutput<RQ, RP>> interceptorOutputs, INetDataObtainedCallback<RQ, RP> callback, INetDataProductEngine<RQ, RP> engine) {
        this.interceptorOutputs = interceptorOutputs;
        this.callback = callback;
        this.engine = engine;
    }

    @Override
    public void onInterceptedInput(INetDataChainInput<RQ,RP> chainInput) throws Exception {
        INetDataRequest<RQ> request=chainInput.request();
        this.exceptionListener=chainInput.exceptionListener();
        INetDataInterceptorOutput<RQ,RP>[] units_reSort=new INetDataInterceptorOutput[interceptorOutputs.size()];
        //重新倒序
        for (INetDataInterceptorOutput<RQ,RP> interceptorOutput:interceptorOutputs
        ) {
            units_reSort[interceptorOutputs.size()-1-interceptorOutputs.indexOf(interceptorOutput)]=interceptorOutput;
        }
        List<INetDataInterceptorOutput<RQ,RP>> units_reSort_list=new ArrayList<>(Arrays.asList(units_reSort));
        INetDataChainOutput<RQ,RP> chainOutput=new InternalNetDataChainOutput<RQ,RP>(units_reSort_list,callback,exceptionListener);
        if (engine!=null){
            engine.onEngineExecute(request,chainOutput);
        }
        else {
            throw new Exception("no data engine!!!!!!!");
        }
    }
}
