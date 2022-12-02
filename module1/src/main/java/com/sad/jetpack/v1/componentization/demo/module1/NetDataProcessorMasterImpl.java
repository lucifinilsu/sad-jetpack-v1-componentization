package com.sad.jetpack.v1.componentization.demo.module1;

import static com.sad.jetpack.v1.componentization.demo.module1.SimpleNetDataCenterComponent.BASE_KEY_HOST;

import com.sad.jetpack.v1.componentization.annotation.Component;

import org.jsoup.Connection;

@Component(url = BASE_KEY_HOST+"class.json",description = "简易的网络数据供给中心示例,这里使用Component进行注解仅仅是方便获取对象而已，真正的调用是下面的方法IPC注解生成的寄生组件")
public class NetDataProcessorMasterImpl implements INetDataProcessorMaster, INetDataProcessorMaster.INetDataProcessorMasterHolder {

    private INetDataRequest request;
    private IDataObtainedCallback callback;
    private IEngine engine;
    @Override
    public INetDataProcessorMaster request(INetDataRequest request) {
        this.request=request;
        return this;
    }

    @Override
    public INetDataProcessorMaster callback(IDataObtainedCallback callback) {
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
    public IDataObtainedCallback callback() {
        return this.callback;
    }

    public IEngine engine() {
        return this.engine;
    }
}
