package com.sad.jetpack.v1.componentization.api;

public interface IConstructor {

    <T> T instance(Class<T> cls) throws Exception;
}
