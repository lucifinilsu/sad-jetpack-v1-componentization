package com.sad.jetpack.v1.componentization.annotation;

public interface IDataConverter {

    <T> T convert(Object o);
}
