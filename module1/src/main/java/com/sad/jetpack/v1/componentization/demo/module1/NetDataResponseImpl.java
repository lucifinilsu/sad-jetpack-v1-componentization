package com.sad.jetpack.v1.componentization.demo.module1;

import java.util.HashMap;
import java.util.Map;

public class NetDataResponseImpl<RQ,RP> implements INetDataResponse<RQ,RP>, INetDataResponse.Creator<RQ,RP> {

    private INetDataRequest request;
    private int code=200;
    private RP body=null;
    private DataSource dataSource=DataSource.NET;
    private Map<String, String> headers=new HashMap<>();
    private NetDataResponseImpl(){}
    public static <RQ,RP> INetDataResponse.Creator<RQ,RP> newCreator(){
        return new NetDataResponseImpl<RQ,RP>();
    }
    @Override
    public INetDataRequest request() {
        return this.request;
    }

    @Override
    public int code() {
        return this.code;
    }

    @Override
    public RP body() {
        return this.body;
    }

    @Override
    public DataSource dataSource() {
        return this.dataSource;
    }

    @Override
    public Map<String, String> headers() {
        return this.headers;
    }

    @Override
    public Creator<RQ,RP> toCreator() {
        return this;
    }

    @Override
    public Creator<RQ,RP> code(int code) {
        this.code=code;
        return this;
    }

    @Override
    public Creator<RQ,RP> body(RP body) {
        this.body=body;
        return this;
    }

    @Override
    public Creator<RQ,RP> headers(Map<String, String> headers) {
        this.headers=headers;
        return this;
    }

    @Override
    public Creator<RQ,RP> addHeader(String key, String value) {
        this.headers.put(key,value);
        return this;
    }

    @Override
    public Creator<RQ,RP> request(INetDataRequest request) {
        this.request=request;
        return this;
    }

    @Override
    public Creator<RQ,RP> dataSource(DataSource dataSource) {
        this.dataSource=dataSource;
        return this;
    }

    @Override
    public NetDataResponseImpl create() {
        return this;
    }
}
