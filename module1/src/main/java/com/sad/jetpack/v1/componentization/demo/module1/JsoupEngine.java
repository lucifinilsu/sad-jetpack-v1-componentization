package com.sad.jetpack.v1.componentization.demo.module1;

import com.sad.core.async.ISADTaskProccessListener;
import com.sad.core.async.SADTaskRunnable;
import com.sad.core.async.SADTaskSchedulerClient;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

public abstract class JsoupEngine<RQ,RP> implements INetDataProductEngine<RQ,RP>{
    @Override
    public void onEngineExecute(INetDataRequest<RQ> request, INetDataChainOutput<RQ,RP> chainOutput) throws Exception {
        SADTaskSchedulerClient.newInstance()
                .execute(new SADTaskRunnable<Connection.Response>("xxxx", new ISADTaskProccessListener<Connection.Response>() {
                    @Override
                    public void onSuccess(Connection.Response jsoupResponse) {
                        onHandleJsoupResponse(request,jsoupResponse,chainOutput);
                    }

                    @Override
                    public void onFail(Throwable throwable) {
                        if (chainOutput.exceptionListener()!=null){
                            chainOutput.exceptionListener().onDataObtainedException(request,throwable);
                        }
                    }

                    @Override
                    public void onCancel() {

                    }
                }) {
                    @Override
                    public Connection.Response doInBackground() throws Exception {
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

    public abstract void onHandleJsoupResponse(INetDataRequest<RQ> request,Connection.Response jsoupResponse, INetDataChainOutput<RQ,RP> chainOutput);
    public void onResetJsoupConnection(INetDataRequest<RQ> request, Connection connection) {
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
