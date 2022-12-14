package com.sad.jetpack.v1.componentization.demo.module1;

import com.sad.jetpack.v1.componentization.annotation.Component;
import com.sad.jetpack.v1.componentization.annotation.IPCChat;
import com.sad.jetpack.v1.componentization.api.BodyImpl;
import com.sad.jetpack.v1.componentization.api.IResponseSession;
import com.sad.jetpack.v1.componentization.api.LogcatUtils;
@Component(url = "demo://component.org/1/1.json",description = "测试获取静态实例")
public class SimpleObjectHost{
    @IPCChat(url = "demo://ipcchat.org/1/1.json",priority = 5)
    public void onReceivedMsg(String xxx, IResponseSession session){
        LogcatUtils.e("------->demo://ipcchat.org/1/1.json收到信息："+xxx);
        session.postResponseData(BodyImpl.newBuilder().addData("yyy","回调信息").build());
    }
    @IPCChat(url = "demo://ipcchat.org/1/2.json",priority = 4)
    public void onReceivedMsg(String xxx){
        LogcatUtils.e("------->demo://ipcchat.org/1/2.json收到信息："+xxx);
        //session.postResponseData(BodyImpl.newBuilder().addData("pppp","回调信息2").build());
    }
}
