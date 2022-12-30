package com.sad.jetpack.v1.componentization.demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.sad.jetpack.v1.componentization.api.BodyImpl;
import com.sad.jetpack.v1.componentization.api.ComponentClassInfo;
import com.sad.jetpack.v1.componentization.api.IBody;
import com.sad.jetpack.v1.componentization.api.IComponentCallable;
import com.sad.jetpack.v1.componentization.api.IComponentCallableInitializeListener;
import com.sad.jetpack.v1.componentization.api.IComponentProcessorCallListener;
import com.sad.jetpack.v1.componentization.api.IRequest;
import com.sad.jetpack.v1.componentization.api.IResponse;
import com.sad.jetpack.v1.componentization.api.InstancesRepository;
import com.sad.jetpack.v1.componentization.api.LogcatUtils;
import com.sad.jetpack.v1.componentization.api.ParasiticComponentRepositoryFactory;
import com.sad.jetpack.v1.componentization.api.RequestImpl;
import com.sad.jetpack.v1.componentization.api.SCore;
import com.sad.jetpack.v1.datamodel.api.DataModelProducerImpl;
import com.sad.jetpack.v1.datamodel.api.DataModelRequestImpl;
import com.sad.jetpack.v1.datamodel.api.IDataModelObtainedCallback;
import com.sad.jetpack.v1.datamodel.api.IDataModelObtainedExceptionListener;
import com.sad.jetpack.v1.datamodel.api.IDataModelRequest;
import com.sad.jetpack.v1.datamodel.api.IDataModelResponse;
import com.sad.jetpack.v1.datamodel.api.extension.engine.OkhttpEngineForStringByStringBody;
import com.sad.jetpack.v1.datamodel.api.extension.interceptor.LogDataModelInterceptor;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Object objectHost;
    private AppCompatButton btn_test;
    private TextView tv_log;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //SCore.registerParasiticComponentFromHost(this);
        initScore();
        if (objectHost==null){
            objectHost=SCore.getFirstInstance(getApplicationContext(),"demo://component.org/1/1.json");
        }
        SCore.registerParasiticComponentFromHost(objectHost);
        //SimpleNetDataMaster.registerNetDataCenter(getApplicationContext());
        initView();
    }

    public void initView(){
        tv_log=findViewById(R.id.test_txt_log);
        btn_test=findViewById(R.id.test_btn);
        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                testPostMsgToLocal();
                /*SimpleNetDataMaster.get(getApplicationContext(), "https://www.baidu.com", "rid:" + RandomStringUtils.randomAlphabetic(5), new SimpleNetDataMaster.IDataObtainedCallback() {
                    @Override
                    public void onDataObtainedCompleted(IRequest request, int code, String s) {
                        tv_log.append(s);
                    }

                    @Override
                    public void onDataObtainedException(IRequest request, Throwable throwable, String processorId) {
                        tv_log.append(throwable.getMessage());
                    }
                });*/
            }
        });
    }

    private void testDataModel(){
        IDataModelRequest request= DataModelRequestImpl.newCreator()
                .method(IDataModelRequest.Method.GET)
                .url("https://www.baidu.com")
                .create();
        LogDataModelInterceptor logInterceptor=LogDataModelInterceptor.newInstance();
        DataModelProducerImpl.<String>newInstance()
                .request(request)
                .addInputInterceptor(logInterceptor)
                .addOutputInterceptor(logInterceptor)
                .callback(new IDataModelObtainedCallback<String>() {
                    @Override
                    public void onDataObtainedCompleted(IDataModelResponse<String> response) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv_log.append(response.body());
                            }
                        });

                    }
                })
                .exceptionListener(new IDataModelObtainedExceptionListener() {
                    @Override
                    public void onDataObtainedException(IDataModelRequest request, Throwable throwable) {
                        throwable.printStackTrace();
                    }
                })
                .engine(new OkhttpEngineForStringByStringBody())
                .execute();
    }

    private void initScore(){
        SCore.enableLogUtils(true);
        SCore.enableInternalLog(true);
        SCore.initIPC(getApplicationContext());
    }

    private void testPostMsgToLocal(){
        try {
            IBody body= BodyImpl.newBuilder()
                    .addData("xxx","666666")
                    .build();
            IRequest request= RequestImpl.newBuilder("c")
                    .body(body)
                    .build()
                    ;
            InstancesRepository instancesRepository=SCore.getCluster(getApplicationContext())
                    .instancesRepositoryFactory(ParasiticComponentRepositoryFactory.newInstance())
                    .instanceInitializeListener(new IComponentCallableInitializeListener() {
                        @Override
                        public void onComponentClassFound(ComponentClassInfo info) {

                        }

                        @Override
                        public IComponentCallable onComponentCallableInstanceObtained(IComponentCallable componentCallable) {
                            LogcatUtils.e("------------>遍历到有效动态组件："+ Arrays.asList(componentCallable.component().urls()));
                            return null;
                        }

                        @Override
                        public Object onObjectInstanceObtained(String curl, Object object) {
                            return null;
                        }

                        @Override
                        public void onTraverseCRMException(Throwable e) {

                        }
                    })
                    .repository("demo://ipcchat.org/1/")
                    ;
            List<IComponentCallable> callables=instancesRepository.componentCallableInstances();
            SCore
                    .asSequenceProcessor("5")
                    .listener(new IComponentProcessorCallListener() {
                        @Override
                        public boolean onProcessorReceivedResponse(IResponse response, String processorId, boolean intercepted) {
                            LogcatUtils.e("------->demo://ipcchat.org/已处理回调:"+response.body().dataContainer().getMap());
                            tv_log.append(response.body().dataContainer().getMap().toString()+"\n");
                            return false;
                        }

                        @Override
                        public IResponse onProcessorMergeResponses(ConcurrentLinkedHashMap<IResponse, String> responses, String processorId) {
                            return null;
                        }

                        @Override
                        public void onProcessorException(IRequest request, Throwable throwable, String processorId) {

                        }
                    })
                    .build()
                    .join(callables)
                    .submit(request);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SCore.unregisterParasiticComponentFromHost(this);
        SCore.unregisterParasiticComponentFromHost(objectHost);
    }
}