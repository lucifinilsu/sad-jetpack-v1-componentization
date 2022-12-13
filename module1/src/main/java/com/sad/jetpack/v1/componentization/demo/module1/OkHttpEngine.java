package com.sad.jetpack.v1.componentization.demo.module1;

import androidx.annotation.NonNull;

import com.sad.jetpack.v1.componentization.api.LogcatUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public abstract class OkHttpEngine<RQ,RP> implements INetDataProductEngine<RQ,RP>{
    @Override
    public void onEngineExecute(INetDataRequest<RQ> request, INetDataChainOutput<RQ, RP> chainOutput) throws Exception {
        Request.Builder okhttpRequestBuilder=new Request.Builder();
        okhttpRequestBuilder.url(request.url());
        if (request.method()== INetDataRequest.Method.GET){
            okhttpRequestBuilder=okhttpRequestBuilder.get();
        }
        onRestOkhttpRequest(request,okhttpRequestBuilder);
        OkHttpClient.Builder builder=new OkHttpClient.Builder()
                .callTimeout(request.timeout(), TimeUnit.MILLISECONDS)
                .connectTimeout(request.timeout(), TimeUnit.MILLISECONDS)
                .followRedirects(true);
        onResetOkhttpClient(request,builder);
        OkHttpClient okHttpClient=builder.build();
        okHttpClient.newCall(okhttpRequestBuilder.build())
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        if (chainOutput.exceptionListener()!=null){
                            chainOutput.exceptionListener().onDataObtainedException(request,e);
                        }
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                        onHandleOkhttpResponse(request,response,chainOutput);
                    }
                });
    }
    public void onResetOkhttpClient(INetDataRequest<RQ> request, OkHttpClient.Builder builder){

    }
    public abstract void onRestOkhttpRequest(INetDataRequest<RQ> request,Request.Builder okhttpRequestBuilder);
    public abstract void onHandleOkhttpResponse(INetDataRequest<RQ> request, Response response, INetDataChainOutput<RQ, RP> chainOutput) throws IOException;
}
