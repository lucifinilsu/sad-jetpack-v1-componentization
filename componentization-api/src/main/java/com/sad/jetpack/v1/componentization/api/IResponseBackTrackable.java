package com.sad.jetpack.v1.componentization.api;

public interface IResponseBackTrackable {

    default String backTrackableId(){return "";}

    default void onBackTrackResponse(IComponentChain chain) throws Exception{
        chain.proceedResponse();
    }

}
