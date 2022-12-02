package com.sad.jetpack.v1.componentization.demo.module1;

import static com.sad.jetpack.v1.componentization.demo.module1.SimpleNetDataCenterComponent.*;

import androidx.annotation.NonNull;

import com.sad.core.async.ISADTaskProccessListener;
import com.sad.core.async.SADTaskRunnable;
import com.sad.core.async.SADTaskSchedulerClient;
import com.sad.jetpack.v1.componentization.annotation.Component;
import com.sad.jetpack.v1.componentization.annotation.IPCChat;
import com.sad.jetpack.v1.componentization.api.BodyImpl;
import com.sad.jetpack.v1.componentization.api.IResponseSession;
import com.sad.jetpack.v1.componentization.api.MapTraverseUtils;
import com.sad.jetpack.v1.componentization.api.ResponseImpl;

import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.util.HashMap;
import java.util.Map;
@Deprecated
@Component(url = BASE_KEY_HOST+"class.json",description = "简易的网络数据供给中心示例,这里使用Component进行注解仅仅是方便获取对象而已，真正的调用是下面的方法IPC注解生成的寄生组件")
public class SimpleNetDataCenterComponent {

    private static final int DEFAULT_TIMEOUT=1000;
    protected static final String BASE_KEY_HOST = "demo://dataCenter.org/";

    public interface IRequestDataHandler{
        void onHandle(Connection connection);
    }
    private void request(String url, Connection.Method method, Object data, @NonNull IRequestDataHandler dataHandler,int timeout, IResponseSession session){
        SADTaskSchedulerClient.newInstance()
                        .execute(new SADTaskRunnable<Map<String,Object>>("dataHandler", new ISADTaskProccessListener<Map<String,Object>>() {
                            @Override
                            public void onSuccess(Map<String,Object> result) {

                                session.postResponseData(
                                        ResponseImpl.newBuilder()
                                                .body(BodyImpl.newBuilder()
                                                        .addData(result)
                                                        .build())
                                                .build());
                            }

                            @Override
                            public void onFail(Throwable throwable) {
                                session.throwException(throwable,"");
                            }

                            @Override
                            public void onCancel() {

                            }
                        }) {
                            @Override
                            public Map<String,Object> doInBackground() throws Exception {
                                Connection connection=Jsoup.connect(url)
                                        .method(method)
                                        .timeout(timeout)
                                        .followRedirects(true)
                                        ;
                                ;
                                dataHandler.onHandle(connection);
                                Connection.Response response=connection.execute();
                                String resource=response.body();
                                int code=response.statusCode();
                                Map<String,Object> resultContainer=new HashMap<>();
                                resultContainer.put("resultResource",resource);
                                resultContainer.put("resultCode",code);
                                return resultContainer;
                            }
                        });

    }
    @IPCChat(url = BASE_KEY_HOST+"post/postJson/method1.json")
    public void postJson(String url, JSONObject json,IResponseSession session){
        postJson(url,json,DEFAULT_TIMEOUT,session);
    }
    @IPCChat(url = "demo://dataCenter.org/post/postJson/method0.json")
    public void postJson(String url, JSONObject json,int timeout, IResponseSession session){
        request(url,Connection.Method.POST,json, new IRequestDataHandler() {
            @Override
            public void onHandle(Connection connection) {
                connection
                        .header("Content-Type", "application/json")
                        .requestBody(json.toString());
            }
        },timeout, session);
    }
    @IPCChat(url = "demo://dataCenter.org/get/get/method1.json")
    public void get(String url,Map<String,String> data,IResponseSession session){
        get(url,data,DEFAULT_TIMEOUT,session);
    }
    @IPCChat(url = "demo://dataCenter.org/get/get/method0.json")
    public void get(String url, Map<String,String> data,int timeout, IResponseSession session){
        request(url,Connection.Method.GET,data, new IRequestDataHandler() {
            @Override
            public void onHandle(Connection connection) {
                if (data!=null){
                    MapTraverseUtils.traverseGroup(data, new MapTraverseUtils.ITraverseAction<String, String>() {

                        @Override
                        public void onTraversed(String o, String o2) {
                            connection.data(data);
                        }
                    });
                }
            }
        },timeout, session);
    }
}
