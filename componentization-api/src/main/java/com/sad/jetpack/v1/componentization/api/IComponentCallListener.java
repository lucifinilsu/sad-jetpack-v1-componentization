package com.sad.jetpack.v1.componentization.api;

public interface IComponentCallListener {

    default IRequest onComponentInputRequest(IRequest request,String componentId){
        return request;
    }

    boolean onComponentReceivedResponse(IResponse response, IRequestSession session, String componentId, boolean intercepted);

    void onComponentException(IRequest request, Throwable throwable,String componentId);
}
