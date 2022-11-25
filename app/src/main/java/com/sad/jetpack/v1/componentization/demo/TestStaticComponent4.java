package com.sad.jetpack.v1.componentization.demo;

import androidx.annotation.NonNull;

import com.sad.jetpack.v1.componentization.annotation.Component;
import com.sad.jetpack.v1.componentization.api.IComponent;
import com.sad.jetpack.v1.componentization.api.IComponentChain;
import com.sad.jetpack.v1.componentization.api.IRequest;
import com.sad.jetpack.v1.componentization.api.IResponseSession;
import com.sad.jetpack.v1.componentization.api.LogcatUtils;
import com.sad.jetpack.v1.componentization.api.ResponseImpl;

@Component(url = "test://tsc/4",description = "回溯链成员4")
public class TestStaticComponent4 implements IComponent {

    @Override
    public void onCall(IRequest request, IResponseSession session) throws Exception {
        //throw new Exception(">>>模拟异常");
        session.postResponseData(ResponseImpl.newBuilder().request(request).build());
    }

    @NonNull
    @Override
    public String toString() {
        return description();
    }

    @Override
    public int priority() {
        return 666;
    }

    @Override
    public void onBackTrackResponse(IComponentChain chain) throws Exception {
        LogcatUtils.e(">>>回溯链响应4："+chain.response());
        chain.proceedResponse();
    }
}
