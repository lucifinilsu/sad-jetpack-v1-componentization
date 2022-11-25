package com.sad.jetpack.v1.componentization.api;


public interface IComponentCallableInitializeListener {

    void onComponentClassFound(ComponentClassInfo info);

    IComponentCallable onComponentCallableInstanceObtained(IComponentCallable componentCallable);

    Object onObjectInstanceObtained(String curl, Object object);

    void onTraverseCRMException(Throwable e);
}
