package com.sad.jetpack.v1.componentization.api;

import static com.sad.jetpack.v1.componentization.api.RemoteAction.REMOTE_ACTION_CREATE_REMOTE_IPC_CHAT;
import static com.sad.jetpack.v1.componentization.api.RemoteAction.REMOTE_ACTION_CREATE_REMOTE_IPC_CUSTOMER;
import static com.sad.jetpack.v1.componentization.api.RemoteAction.REMOTE_ACTION_REGISTER_TO_MESSENGERS_POOL;
import static com.sad.jetpack.v1.componentization.api.RemoteAction.REMOTE_ACTION_UNREGISTER_FROM_MESSENGERS_POOL;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@IntDef({REMOTE_ACTION_REGISTER_TO_MESSENGERS_POOL, REMOTE_ACTION_UNREGISTER_FROM_MESSENGERS_POOL,REMOTE_ACTION_CREATE_REMOTE_IPC_CHAT,REMOTE_ACTION_CREATE_REMOTE_IPC_CUSTOMER})
@Retention(RetentionPolicy.SOURCE)
public @interface RemoteAction {

    int REMOTE_ACTION_REGISTER_TO_MESSENGERS_POOL=1;

    int REMOTE_ACTION_UNREGISTER_FROM_MESSENGERS_POOL=2;

    int REMOTE_ACTION_CREATE_REMOTE_IPC_CHAT =4;

    int REMOTE_ACTION_CREATE_REMOTE_IPC_CUSTOMER = 8;
}
