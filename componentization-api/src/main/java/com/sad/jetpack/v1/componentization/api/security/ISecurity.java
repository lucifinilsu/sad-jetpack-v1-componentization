package com.sad.jetpack.v1.componentization.api.security;

public interface ISecurity {

    String token();

    ISecurityVerify securityVerify();

}
