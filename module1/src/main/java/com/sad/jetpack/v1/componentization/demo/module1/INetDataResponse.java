package com.sad.jetpack.v1.componentization.demo.module1;

import java.util.Map;

public interface INetDataResponse<RQ,RP> {

    INetDataRequest<RQ> request();

    int code();

    RP body();

    DataSource dataSource();

    Map<String,String> headers();

    Creator<RQ,RP> toCreator();

    interface Creator<RQ,RP>{

        Creator<RQ,RP> code(int code);

        Creator<RQ,RP> body(RP body);

        Creator<RQ,RP> headers(Map<String,String> headers);

        Creator<RQ,RP> addHeader(String key, String value);

        Creator<RQ,RP> request(INetDataRequest request);

        Creator<RQ,RP> dataSource(DataSource dataSource);

        INetDataResponse<RQ,RP> create();
    }



}
