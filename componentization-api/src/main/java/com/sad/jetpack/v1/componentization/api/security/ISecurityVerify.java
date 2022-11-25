package com.sad.jetpack.v1.componentization.api.security;

public interface ISecurityVerify {

    <T> T onSecurityVerifySuccess(T t);

    boolean onSecurityVerifySuccess(Exception e);

}
