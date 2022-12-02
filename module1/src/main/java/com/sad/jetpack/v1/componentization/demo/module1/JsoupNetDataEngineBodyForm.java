package com.sad.jetpack.v1.componentization.demo.module1;

import org.jsoup.Connection;

import java.util.Map;

public class JsoupNetDataEngineBodyForm extends JsoupNetDataEngine<Map<String,String>,String>{

    @Override
    public void onResetJsoupConnection(INetDataRequest<Map<String, String>> request, Connection connection) {
        Map<String,String> body=request.body();
        if (body!=null){
            connection.data(body);
        }
    }
}
