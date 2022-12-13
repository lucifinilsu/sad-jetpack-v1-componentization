package com.sad.jetpack.v1.componentization.demo.module1;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class OkhttpEngineForStringByStringBody extends OkhttpEngineForString<String>{
    @Override
    public void onRestOkhttpRequest(INetDataRequest<String> request, Request.Builder okhttpRequestBuilder) {
        if (request.method()== INetDataRequest.Method.POST){
            okhttpRequestBuilder=okhttpRequestBuilder.post(RequestBody.create(request.body(), MediaType.parse("application/json;charset=utf-8")));
        }
    }
}
