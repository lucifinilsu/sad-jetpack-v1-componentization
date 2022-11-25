package com.sad.jetpack.v1.componentization.api;

import static com.sad.jetpack.v1.componentization.api.RemoteActionResultState.REMOTE_ACTION_RESULT_STATE_FAILURE;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@IntDef({RemoteActionResultState.REMOTE_ACTION_RESULT_STATE_SUCCESS, REMOTE_ACTION_RESULT_STATE_FAILURE})
@Retention(RetentionPolicy.SOURCE)
public @interface RemoteActionResultState {

    int REMOTE_ACTION_RESULT_STATE_SUCCESS=200;

    int REMOTE_ACTION_RESULT_STATE_FAILURE=500;

}
