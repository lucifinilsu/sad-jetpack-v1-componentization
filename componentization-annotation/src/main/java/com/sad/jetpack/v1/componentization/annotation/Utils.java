package com.sad.jetpack.v1.componentization.annotation;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static boolean isURL(String str){
        return true;
        /*if (ObjectUtils.isEmpty(str)){
            return false;
        }
        str = str.toLowerCase();
        String regex = "^(([0-9a-z_!~*'().;?:@&=+$,%#-]+)?://)"
                + "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?" //ftp的user@
                + "(([0-9]{1,3}\\.){3}[0-9]{1,3}" // IP形式的URL- 199.194.52.184
                + "|" // 允许IP和DOMAIN（域名）
                + "([0-9a-z_!~*'()-]+\\.)*" // 域名- www.
                + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\." // 二级域名
                + "[a-z]{2,6})" // first level domain- .com or .museum
                + "(:[0-9]{1,4})?" // 端口- :80
                + "((/?)|" // a slash isn't required if there is no file name
                + "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$";
        return str.matches(regex);*/
    }
    @Deprecated
    public static List<String> ermPaths(String url){
        List<String> elist=new ArrayList<>();
        URI uri=URI.create(url);
        String path=uri.getPath();
        StringBuilder ermPath=new StringBuilder();
        ermPath
                .append("erm"/*+ File.separator*/)
                //.append(InternalContextHolder.get().getContext().getPackageName())
                .append(path.replace("/",File.separator));
        String e=ermPath.toString();
        elist.add(e);
        return elist;
    }

    public static String crmPaths(String crm,String url){
        URI uri=URI.create(url);
        String scheme=uri.getScheme();
        String host=uri.getHost();
        String path="/"+scheme+"/"+host+uri.getPath();//uri.getPath();
        StringBuilder crmPath=new StringBuilder();
        crmPath
                .append(crm/*+ File.separator*/)
                //.append(InternalContextHolder.get().getContext().getPackageName())
                .append(path.replace("/",File.separator));
        String e=crmPath.toString();
        return e;
    }

    public static String creatExposedWorkerClassName(String exposedServiceCN,String url){
        return "ExposedServiceWorker$$"+exposedServiceCN+"$$"+ ValidUtils.encryptMD5ToString(url);
    }

    public static String getURL(String strURL)
    {
        String strPage=null;
        String[] arrSplit=null;

        strURL=strURL.trim().toLowerCase();

        arrSplit=strURL.split("[?]");
        if(strURL.length()>0)
        {
            if(arrSplit.length>1)
            {
                if(arrSplit[0]!=null)
                {
                    strPage=arrSplit[0];
                }
            }
        }

        return strPage;
    }
}
