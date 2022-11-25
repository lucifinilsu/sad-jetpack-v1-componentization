package com.sad.jetpack.v1.componentization.demo;

import androidx.annotation.NonNull;

import com.sad.jetpack.v1.componentization.annotation.Component;
import com.sad.jetpack.v1.componentization.api.IComponent;
import com.sad.jetpack.v1.componentization.api.IComponentChain;
import com.sad.jetpack.v1.componentization.api.IRequest;
import com.sad.jetpack.v1.componentization.api.IResponseSession;
import com.sad.jetpack.v1.componentization.api.LogcatUtils;
import com.sad.jetpack.v1.componentization.api.ResponseImpl;

@Component(url = "test://tsc/2",description = "回溯链成员2")
public class TestStaticComponent2 implements IComponent {

    @NonNull
    @Override
    public String toString() {
        return description();
    }

    @Override
    public void onCall(IRequest request, IResponseSession session) throws Exception {
        session.postResponseData(ResponseImpl.newBuilder().request(request).build(),false);
    }

    @Override
    public void onBackTrackResponse(IComponentChain chain) throws Exception {
        LogcatUtils.e(">>>回溯链响应2："+chain.response());
        chain.proceedResponse();
    }

    @Override
    public int priority() {
        return 888;
    }
}
