package com.sad.jetpack.v1.componentization.api;

import android.app.ActivityManager;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;


import com.sad.jetpack.v1.componentization.annotation.EncryptUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class CommonUtils {
    private final static String SP="122savba1565_cer";
    private final static String DEFAULT_KEY="34324ffqfaw";
    public static String encodeMessengerId(String orgUrl,String mark){
        String o=orgUrl+SP+mark;
        String e= EncryptUtil.getInstance().XORencode(o,DEFAULT_KEY);
        return e;
    }
    public static String decodeMessengerId(String messengerId){
        String d= EncryptUtil.getInstance().XORdecode(messengerId,DEFAULT_KEY);
        return d;
    }
    public static String getOrgUrlFromMessengerId(String messengerId ){
        String d=decodeMessengerId(messengerId);
        String[] os=d.split(SP);
        if (os!=null && os.length>0){
            return os[0];
        }
        return "";
    }
    public static String getMarkFromMessengerId(String messengerId ){
        String d=decodeMessengerId(messengerId);
        String[] os=d.split(SP);
        if (os!=null && os.length>1){
            return os[1];
        }
        return "";
    }

//    /**
//     * 获取当前运行的进程名
//     * @param context
//     * @return
//     */
//    public static String getCurrAppProcessName(Context context) {
//        int pid = android.os.Process.myPid();
//        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
//            if (appProcess.pid == pid) {
//
//                return appProcess.processName;
//            }
//        }
//        return "";
//    }



    private static String getCurrAppProcessName2(){
        FileInputStream var1 = null;
        String var7;
        try {
            String var2 = "/proc/self/cmdline";
            var1 = new FileInputStream(var2);
            byte[] var3 = new byte[256];
            int var4;
            int var5;
            for(var4 = 0; (var5 = var1.read()) > 0 && var4 < var3.length; var3[var4++] = (byte)var5) {
            }
            if (var4 <= 0) {
                return null;
            }
            String var6 = new String(var3, 0, var4, "UTF-8");
            var7 = var6;
        } catch (Throwable var18) {
            var18.printStackTrace();
            return null;
        } finally {
            if (var1 != null) {
                try {
                    var1.close();
                } catch (IOException var17) {
                    var17.printStackTrace();
                }
            }

        }
        return var7;
    }

    private static String cacheCurrAppProcessName ="";
    private static void logCaller(){
        log(Log.getStackTraceString(new Throwable()));
    }
    private static void log(String s){
        LogcatUtils.internalLog("SAD",s);
    }
    /**
     * 获取当前运行的进程名
     * @param context
     * @return
     */
    public static String getCurrAppProcessName(Context context){
        logCaller();
        return getCurrAppProcessName(context,true);
    }
    public static String getCurrAppProcessName(Context context,boolean readCache) {
        if (readCache){
            if (!TextUtils.isEmpty(cacheCurrAppProcessName)){
                log("-------->获取进程名缓存:"+ cacheCurrAppProcessName);
                return cacheCurrAppProcessName;
            }
        }
        try {
            cacheCurrAppProcessName = getCurrAppProcessName2();
            log("-------->获取进程名v2:"+ cacheCurrAppProcessName);
            if (!TextUtils.isEmpty(cacheCurrAppProcessName)){
                return cacheCurrAppProcessName;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        if (TextUtils.isEmpty(cacheCurrAppProcessName)){
            int pid = android.os.Process.myPid();
            ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            int i=0;
            List<ActivityManager.RunningAppProcessInfo> list=mActivityManager.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo appProcess : list) {
                i++;
                if (appProcess.pid == pid) {
                    log("获取进程名v1循环:"+i);
                    cacheCurrAppProcessName = appProcess.processName;
                    return cacheCurrAppProcessName;
                }
            }
        }
        return "";
    }


    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param mContext
     * @param serviceName
     *            是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    public static boolean isServiceWork(Context mContext, String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(100);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }
}
