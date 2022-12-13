package com.sad.jetpack.v1.componentization.demo.module1;

import com.sad.jetpack.v1.componentization.api.LogcatUtils;
import com.sad.jetpack.v1.componentization.api.MapTraverseUtils;

import org.apache.commons.lang3.ObjectUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import kotlin.Pair;
import okhttp3.Headers;
import okhttp3.Response;
import okhttp3.ResponseBody;

public abstract class OkhttpEngineForString<RQ> extends OkHttpEngine<RQ,String>{
    /*@Override
    public void onRestOkhttpRequest(INetDataRequest<String> request, Request.Builder okhttpRequestBuilder) {
        if (request.method()== INetDataRequest.Method.POST){
            okhttpRequestBuilder=okhttpRequestBuilder.post(RequestBody.create(request.body(), MediaType.parse("application/json;charset=utf-8")));
        }
    }*/

    @Override
    public void onHandleOkhttpResponse(INetDataRequest<RQ> request, Response response, INetDataChainOutput<RQ, String> chainOutput) throws IOException {
        ResponseBody responseBody=response.body();
        int code=response.code();
        DataSource dataSource=DataSource.NET;
        INetDataResponse.Creator<RQ,String> creator=NetDataResponseImpl.<RQ,String>newCreator()
            .code(code)
            .body(responseBody.string())
            .dataSource(dataSource)
            .request(request);
        Headers headers=response.headers();
        Map<String,String> h=new HashMap<>();
        Iterator<Pair<String, String>> iterator=headers.iterator();
        while (iterator.hasNext()){
            Pair<String, String> entityEntry=iterator.next();
            String k=entityEntry.getFirst();
            String v=entityEntry.getSecond();
            if (v!=null){
                h.put(k,v);
            }
        }
        creator.headers(h);
        INetDataResponse<RQ,String> netDataResponse=creator.create();
        chainOutput.proceed(netDataResponse);
    }
}
