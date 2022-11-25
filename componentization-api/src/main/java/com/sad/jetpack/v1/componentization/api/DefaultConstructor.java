package com.sad.jetpack.v1.componentization.api;

import java.lang.reflect.Constructor;

public class DefaultConstructor implements IConstructor {

    @Override
    public <T> T instance(Class<T> cls) throws Exception {
        Constructor<T> constructor=cls.getDeclaredConstructor();
        constructor.setAccessible(true);
        return constructor.newInstance();
    }
}
