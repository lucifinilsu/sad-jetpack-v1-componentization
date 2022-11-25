package com.sad.jetpack.v1.componentization.annotation;

/**
 * Created by Administrator on 2019/4/3 0003.
 */

public class NameUtils {

    public static String getParasiticComponentClassSimpleName(String srcCanonicalName,String componentName,String separator){
        String prefix="ParasiticIPCChatProxy";
        return getNewComponentClassSimpleName(prefix,srcCanonicalName,componentName,separator);
    }

    public static String getActivityRouteProxyComponentClassSimpleName(String srcCanonicalName,String componentName,String separator){
        String prefix="ActivityClassSupplierProxy";
        return getNewComponentClassSimpleName(prefix,srcCanonicalName,componentName,separator);
    }

    public static String getServiceRouteProxyComponentClassSimpleName(String srcCanonicalName,String componentName,String separator){
        String prefix="SerivceRouteProxy";
        return getNewComponentClassSimpleName(prefix,srcCanonicalName,componentName,separator);
    }

    public static String getNewComponentClassSimpleName(String prefix,String srcCanonicalName,String componentName,String separator){
        String s1=srcCanonicalName.replace(".",separator);
        String s2=prefix+separator+s1+separator+componentName;
        return s2;
    }

}
