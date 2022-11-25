package com.sad.jetpack.v1.componentization.annotation;

public class DefaultConverter implements IDataConverter{
    @Override
    public <T> T convert(Object o) {
        return (T) o;
    }
}
