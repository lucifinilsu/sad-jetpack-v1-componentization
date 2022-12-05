package com.sad.jetpack.v1.componentization.demo.module1;

import static com.sad.jetpack.v1.componentization.demo.module1.SimpleNetDataCenterComponent.BASE_KEY_HOST;

import com.sad.jetpack.v1.componentization.annotation.Component;

import org.jsoup.Connection;

import java.util.ArrayList;
import java.util.List;

@Component(url = BASE_KEY_HOST+"class.json",description = "简易的网络数据供给中心示例,这里使用Component进行注解仅仅是方便获取对象而已，真正的调用是下面的方法IPC注解生成的寄生组件")
public class NetDataProcessorMasterImpl<RQ,RP> implements INetDataProcessorMaster<RQ,RP> {

    private INetDataRequest<RQ> request;
    private INetDataObtainedCallback<RQ,RP> callback;
    private INetDataObtainedExceptionListener<RQ> exceptionListener;
    private INetDataProductEngine<RQ,RP> engine;
    private List<INetDataInterceptorInput<RQ,RP>> interceptorInputs=new ArrayList<>();
    private List<INetDataInterceptorOutput<RQ,RP>> interceptorOutputs=new ArrayList<>();

    private NetDataProcessorMasterImpl(){

    }

    public static <RQ,RP> INetDataProcessorMaster<RQ,RP> newInstance(){
        return new NetDataProcessorMasterImpl<RQ,RP>();
    }

    @Override
    public INetDataProcessorMaster<RQ,RP> request(INetDataRequest request) {
        this.request=request;
        return this;
    }

    @Override
    public INetDataProcessorMaster<RQ,RP> addInputInterceptor(INetDataInterceptorInput input) {
        this.interceptorInputs.add(input);
        return this;
    }

    @Override
    public INetDataProcessorMaster<RQ,RP> addOutputInterceptor(INetDataInterceptorOutput output) {
        interceptorOutputs.add(output);
        return this;
    }

    @Override
    public INetDataProcessorMaster<RQ,RP> callback(INetDataObtainedCallback callback) {
        this.callback=callback;
        return this;
    }

    @Override
    public INetDataProcessorMaster<RQ,RP> exceptionListener(INetDataObtainedExceptionListener<RQ> exceptionListener) {
        this.exceptionListener=exceptionListener;
        return this;
    }

    @Override
    public INetDataProcessorMaster<RQ,RP> engine(INetDataProductEngine<RQ,RP> engine) {
        this.engine=engine;
        return this;
    }

    @Override
    public void execute() {
        addInputInterceptor(new InternalTerminalNetDataInterceptorInput(interceptorOutputs,callback,engine));
        InternalNetDataChainInput<RQ,RP> chainInput=new InternalNetDataChainInput<>(interceptorInputs,callback,exceptionListener);
        chainInput.proceed(request);
    }

    @Override
    public INetDataProcessorMaster<RQ,RP> interceptorOutputs(List<INetDataInterceptorOutput<RQ,RP>> list) {
        this.interceptorOutputs=list;
        return this;
    }

    @Override
    public INetDataProcessorMaster<RQ,RP> interceptorInputs(List<INetDataInterceptorInput<RQ,RP>> list) {
        this.interceptorInputs=list;
        return this;
    }



    /*@Override
    public INetDataProcessorMaster request(INetDataRequest request) {
        this.request=request;
        return this;
    }

    @Override
    public INetDataProcessorMaster callback(INetDataObtainedCallback callback) {
        this.callback=callback;
        return this;
    }

    @Override
    public INetDataProcessorMaster engine(IEngine engine) {
        this.engine=engine;
        return this;
    }

    @Override
    public void execute() {
        if (this.engine==null){
            this.engine=new JsoupNetDataEngine() {
                @Override
                public void onResetJsoupConnection(INetDataRequest request, Connection connection) {

                }
            };
        }
        this.engine.onDoExecute(this);
    }

    @Override
    public INetDataRequest request() {
        return this.request;
    }

    @Override
    public INetDataObtainedCallback callback() {
        return this.callback;
    }

    public IEngine engine() {
        return this.engine;
    }*/
}
