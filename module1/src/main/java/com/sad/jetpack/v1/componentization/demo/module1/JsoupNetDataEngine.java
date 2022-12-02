package com.sad.jetpack.v1.componentization.demo.module1;

import com.sad.core.async.ISADTaskProccessListener;
import com.sad.core.async.SADTaskRunnable;
import com.sad.core.async.SADTaskSchedulerClient;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

public abstract class JsoupNetDataEngine<RQ,RP> implements INetDataProcessorMaster.IEngine<RQ,RP>, INetDataEngineResponseContinuer<RQ,RP> {

    @Override
    public void onDoExecute(INetDataProcessorMaster.INetDataProcessorMasterHolder<RQ,RP> holder) {
        this.callback= holder.callback();
        SADTaskSchedulerClient.newInstance()
                .execute(new SADTaskRunnable<Connection.Response>("xxxx", new ISADTaskProccessListener<Connection.Response>() {
                    @Override
                    public void onSuccess(Connection.Response jsoupResponse) {
                        int code=jsoupResponse.statusCode();
                        DataSource dataSource=DataSource.NET;
                        INetDataResponse<RQ,RP> preNetDataResponse=NetDataResponseImpl.<RQ,RP>newCreator()
                                .code(code)
                                .dataSource(dataSource)
                                .request(holder.request())
                                .headers(jsoupResponse.headers())
                                .create();
                        onHandleJsoupResponse(preNetDataResponse,jsoupResponse,JsoupNetDataEngine.this);
                    }

                    @Override
                    public void onFail(Throwable throwable) {
                        if (holder.callback()!=null){
                            holder.callback().onDataObtainedException(holder.request(),throwable);
                        }
                    }

                    @Override
                    public void onCancel() {

                    }
                }) {
                    @Override
                    public Connection.Response doInBackground() throws Exception {
                        INetDataRequest request= holder.request();
                        Connection connection= Jsoup.connect(request.url())
                                .method(methodTransform(request.method()))
                                .timeout((int) request.timeout())
                                .followRedirects(true)
                                ;
                        ;
                        onResetJsoupConnection(request,connection);
                        Connection.Response response=connection.execute();
                        return response;
                    }
                });
    }

    public abstract void onResetJsoupConnection(INetDataRequest<RQ> request, Connection connection);
    public void onHandleJsoupResponse(INetDataResponse<RQ,RP> preResponse, Connection.Response jsoupResponse, INetDataEngineResponseContinuer<RQ,RP> continuer){
        continueHandleResponse(preResponse);
    }
    private INetDataProcessorMaster.IDataObtainedCallback<RQ,RP> callback=null;
    @Override
    public void continueHandleResponse(INetDataResponse<RQ, RP> response) {
        if (callback!=null){
            callback.onDataObtainedCompleted(response);
        }
    }

    public Connection.Method methodTransform(INetDataRequest.Method method){
        if (method == INetDataRequest.Method.GET){
            return Connection.Method.GET;
        }
        else if (method == INetDataRequest.Method.POST){
            return Connection.Method.POST;
        }
        else if (method == INetDataRequest.Method.HEAD){
            return Connection.Method.HEAD;
        }
        return Connection.Method.GET;
    }
}
