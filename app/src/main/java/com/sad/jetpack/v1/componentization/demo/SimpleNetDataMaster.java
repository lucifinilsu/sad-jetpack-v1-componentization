package com.sad.jetpack.v1.componentization.demo;

import android.content.Context;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.sad.jetpack.v1.componentization.api.BodyImpl;
import com.sad.jetpack.v1.componentization.api.IBody;
import com.sad.jetpack.v1.componentization.api.IComponentProcessorCallListener;
import com.sad.jetpack.v1.componentization.api.IRequest;
import com.sad.jetpack.v1.componentization.api.IResponse;
import com.sad.jetpack.v1.componentization.api.InstancesRepository;
import com.sad.jetpack.v1.componentization.api.LogcatUtils;
import com.sad.jetpack.v1.componentization.api.ParasiticComponentRepositoryFactory;
import com.sad.jetpack.v1.componentization.api.RequestImpl;
import com.sad.jetpack.v1.componentization.api.SCore;

import java.util.HashMap;
import java.util.Map;

public class SimpleNetDataMaster {
    private static Object host;
    public static void registerNetDataCenter(Context context){
        if (host==null){
            host=SCore.getFirstInstance(context,"demo://dataCenter.org/class.json");
            SCore.registerParasiticComponentFromHost(host);
        }
    }
    public interface IDataObtainedCallback{
        void onDataObtainedCompleted(IRequest request,int code,String s);
        void onDataObtainedException(IRequest request,Throwable throwable, String processorId);
    }

    private Context context;
    private String url;
    private Object data;
    private int timeout=6000;
    private IDataObtainedCallback callback;

    public static void get(Context context,String url,String rid,IDataObtainedCallback callback){//注意，每一次访问网站或者接口，其行为都具有唯一性,用于校验响应端的响应权限和必要性，这个唯一性用组件的请求id来标记
        try {
            IBody body= BodyImpl.newBuilder()
                    .addData("url",url)
                    .addData("timeout",6000)
                    .addData("data",new HashMap<>())
                    .build();
            IRequest request= RequestImpl.newBuilder(rid)
                    .body(body)
                    .build()
                    ;
            InstancesRepository instancesRepository= SCore.getCluster(context)
                    .instancesRepositoryFactory(ParasiticComponentRepositoryFactory.newInstance())
                    .repository("demo://dataCenter.org/get/get/method1.json")
                    ;
            SCore
                    .asSequenceProcessor("requestNetData:"+url)
                    .listener(new IComponentProcessorCallListener() {
                        @Override
                        public boolean onProcessorReceivedResponse(IResponse response, String processorId, boolean intercepted) {
                            //LogcatUtils.e("------->网络数据中心返回数据:"+);
                            Map r=response.body().dataContainer().getMap();
                            int code= (int) r.get("resultCode");
                            String res= (String) r.get("resultResource");
                            if (callback!=null){
                                callback.onDataObtainedCompleted(response.request(),code,res);
                            }
                            return false;
                        }

                        @Override
                        public IResponse onProcessorMergeResponses(ConcurrentLinkedHashMap<IResponse, String> responses, String processorId) {
                            return null;
                        }

                        @Override
                        public void onProcessorException(IRequest request, Throwable throwable, String processorId) {
                            if (callback!=null){
                                callback.onDataObtainedException(request,throwable,processorId);
                            }
                        }
                    })
                    .build()
                    .join(instancesRepository.componentCallableInstances())
                    .submit(request);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
