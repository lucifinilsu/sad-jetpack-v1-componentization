package com.sad.jetpack.v1.componentization.demo.module1;

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
        chainOutput.proceed(netDataResponse);
    }
}
