package com.sad.jetpack.v1.componentization.demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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

import org.apache.commons.lang3.RandomStringUtils;

public class MainActivity extends AppCompatActivity {
    private Object objectHost;
    private AppCompatButton btn_test;
    private TextView tv_log;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SCore.registerParasiticComponentFromHost(this);
        initScore();
        if (objectHost==null){
            objectHost=SCore.getFirstInstance(getApplicationContext(),"demo://component.org/1/1.json");
        }
        SCore.registerParasiticComponentFromHost(objectHost);
        SimpleNetDataMaster.registerNetDataCenter(getApplicationContext());
        initView();
    }

    public void initView(){
        tv_log=findViewById(R.id.test_txt_log);
        btn_test=findViewById(R.id.test_btn);
        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //testPostMsgToLocal();
                SimpleNetDataMaster.get(getApplicationContext(), "https://www.baidu.com", "rid:" + RandomStringUtils.randomAlphabetic(5), new SimpleNetDataMaster.IDataObtainedCallback() {
                    @Override
                    public void onDataObtainedCompleted(IRequest request, int code, String s) {
                        tv_log.append(s);
                    }

                    @Override
                    public void onDataObtainedException(IRequest request, Throwable throwable, String processorId) {
                        tv_log.append(throwable.getMessage());
                    }
                });
            }
        });
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
                    .repository("demo://ipcchat.org/1/")
                    ;
            SCore
                    .asSequenceProcessor("5")
                    .listener(new IComponentProcessorCallListener() {
                        @Override
                        public boolean onProcessorReceivedResponse(IResponse response, String processorId, boolean intercepted) {
                            LogcatUtils.e("------->demo://ipcchat.org/1/1.json已处理回调:"+response.body().dataContainer().getMap());
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
                    .join(instancesRepository.componentCallableInstances())
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