package com.sad.jetpack.v1.componentization.demo.module1;

import android.util.Log;

import org.jsoup.Connection;

public class JsoupEngineForString<RQ> extends JsoupEngine<RQ,String>{
    @Override
    public void onHandleJsoupResponse(INetDataRequest<RQ> request, Connection.Response jsoupResponse, INetDataChainOutput<RQ, String> chainOutput) {
        int code=jsoupResponse.statusCode();
        DataSource dataSource=DataSource.NET;
        INetDataResponse<RQ,String> netDataResponse=NetDataResponseImpl.<RQ,String>newCreator()
                .code(code)
                .body(jsoupResponse.body())
                .dataSource(dataSource)
                .request(request)
                .headers(jsoupResponse.headers())
                .create();
        //Log.e("sad-jetpack-v1","---------------->开始调用输出链");
        chainOutput.proceed(netDataResponse);
    }
}
