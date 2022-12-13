package com.sad.jetpack.v1.componentization.demo.module1;

import com.sad.jetpack.v1.componentization.api.MapTraverseUtils;

import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

public class OkhttpEngineForStringByFormBody extends OkhttpEngineForString<Map<String,String>>{
    @Override
    public void onRestOkhttpRequest(INetDataRequest<Map<String,String>> request, Request.Builder okhttpRequestBuilder) {
        if (request.method()== INetDataRequest.Method.POST){
            Map<String,String> orgRequestBody=request.body();
            MultipartBody.Builder builder=new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);
            MapTraverseUtils.traverseGroup(orgRequestBody, new MapTraverseUtils.ITraverseAction<String, String>() {
                @Override
                public void onTraversed(String s, String s2) {
                    builder.addFormDataPart(s,s2);
                }
            });
            MultipartBody formBody = builder.build();
            okhttpRequestBuilder=okhttpRequestBuilder.post(formBody);
        }
    }
}
