package com.sad.jetpack.v1.componentization.demo.module1;

import android.text.TextUtils;

import org.jsoup.Connection;

public class JsoupNetDataEngineBodyString extends JsoupNetDataEngine<String,String>{

    @Override
    public void onResetJsoupConnection(INetDataRequest<String> request, Connection connection) {
        String body= request.body();
        if (!TextUtils.isEmpty(body)){
            connection.requestBody(body);
        }
    }
}