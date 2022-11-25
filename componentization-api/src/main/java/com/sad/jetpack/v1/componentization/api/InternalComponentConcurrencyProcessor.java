package com.sad.jetpack.v1.componentization.api;


import android.os.Handler;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.googlecode.concurrentlinkedhashmap.Weighers;
import com.sad.core.async.ISADTaskProccessListener;
import com.sad.core.async.SADTaskRunnable;
import com.sad.core.async.SADTaskSchedulerClient;

import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

final class InternalComponentConcurrencyProcessor extends AbsInternalComponentProcessor {
    private CountDownLatch countDownLatch;
    private ConcurrentLinkedHashMap<IResponse,String> responses;
    private AtomicReference<Throwable> currThrowable=new AtomicReference<>();
    protected static IComponentProcessor.Builder newBuilder(String id){
        return new InternalComponentConcurrencyProcessor(id);
    }
    protected static IComponentProcessor newInstance(String id){
        return new InternalComponentConcurrencyProcessor(id);
    }
    private InternalComponentConcurrencyProcessor(String id){
        this.processorId=id;
        responses=new ConcurrentLinkedHashMap.Builder<IResponse,String>()
                .maximumWeightedCapacity(999)
                .weigher(Weighers.singleton())
                .build();
    }
    @Override
    public void submit(IRequest request) {
        if (callListener!=null){
            request=callListener.onProcessorInputRequest(request,processorId);
        }
        IRequest r=request;
        if (units.isEmpty()){
            Exception e= new Exception("the units of ur remote task'target is empty !!!");
            if (callListener!=null){
                callListener.onProcessorException(request,e,processorId);
            }
            return;
        }
        //排序
        Collections.sort(units, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                if (o1 instanceof ISortable && o2 instanceof ISortable){
                    return ((ISortable) o2).priority()-((ISortable) o1).priority();
                }
                return 0;
            }
        });
        indexIntercepted.set(units.size());
        countDownLatch=new CountDownLatch(units.size());
        SADTaskSchedulerClient.newInstance().execute(new SADTaskRunnable<ConcurrentLinkedHashMap<IResponse,String>>("PROCESSOR_COUNTDOWN", new ISADTaskProccessListener<ConcurrentLinkedHashMap<IResponse, String>>() {
            @Override
            public void onSuccess(ConcurrentLinkedHashMap<IResponse, String> result) {
                if (callListener!=null){
                    IResponse response=callListener.onProcessorMergeResponses(result,processorId);
                    callListener.onProcessorReceivedResponse(response, processorId,isIntercepted.get());
                }
            }

            @Override
            public void onFail(Throwable throwable) {
                if (callListener!=null){
                    callListener.onProcessorException(r,throwable,processorId);
                }
            }

            @Override
            public void onCancel() {

            }
        }) {
            @Override
            public ConcurrentLinkedHashMap<IResponse, String> doInBackground() throws Exception {
                if (needCheckTimeout()){
                    if (countDownLatch.await(callerConfig.timeout(), TimeUnit.MILLISECONDS)){
                        return responses;
                    }
                    else {
                        throw new TimeoutException("the task of the processor whose id is '"+processorId+"' is timeout !!!");
                    }
                }
                else {
                    countDownLatch.await();
                }
                return responses;
            }
        });
        if (needDelay()){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doSubmit(r);
                }
            },callerConfig.delay());
        }
        else {
            doSubmit(r);
        }
    }

    private void doSubmit(IRequest request){
        try {

            for (Object o:units
                 ) {
                IRequest r= RequestImpl.newBuilder(request.id())
                        .body(request.body())
                        .fromApp(request.fromApp())
                        .fromProcess(request.fromProcess())
                        .build()
                        ;
                if (o instanceof IComponentCallable){
                    IComponentCallable callable= (IComponentCallable) o;
                    IComponentCallListener listenerSelf=callable.listener();
                    IComponentCallable callablePro=callable.toBuilder()
                            .listener(new IComponentCallListener() {
                                @Override
                                public IRequest onComponentInputRequest(IRequest request, String componentId) {
                                    if (listenerSelf!=null){
                                        request= listenerSelf.onComponentInputRequest(request,componentId);
                                    }
                                    if (callListener!=null){
                                        request = callListener.onChildComponentInputRequest(request,componentId);
                                    }
                                    return request;
                                }

                                @Override
                                public boolean onComponentReceivedResponse(IResponse response, IRequestSession session, String componentId, boolean intercepted) {
                                    if (listenerSelf!=null){
                                        listenerSelf.onComponentReceivedResponse(response,session,componentId,intercepted);
                                    }
                                    if (callListener!=null){
                                        callListener.onChildComponentReceivedResponse(response,session,componentId,intercepted);
                                    }
                                    responses.put(response, callable.componentId());
                                    countDownLatch.countDown();
                                    return false;
                                }

                                @Override
                                public void onComponentException(IRequest request, Throwable throwable,String componentId) {
                                    if (listenerSelf!=null){
                                        listenerSelf.onComponentException(request,throwable,componentId);
                                    }
                                    if (callListener!=null){
                                        callListener.onChildComponentException(request,throwable,componentId);
                                    }
                                    countDownLatch.countDown();
                                }
                            })
                            .build()
                    ;
                    callablePro.call(r);
                }
                else if (o instanceof  IComponentProcessor){
                    IComponentProcessor processor= (IComponentProcessor) o;
                    IComponentProcessorCallListener listenerSelf=processor.listener();
                    IComponentProcessor processorPro=processor.toBuilder()
                            .listener(new IComponentProcessorCallListener() {
                                @Override
                                public IRequest onProcessorInputRequest(IRequest request, String processorId) {
                                    if (listenerSelf!=null){
                                        request=listenerSelf.onProcessorInputRequest(request,processorId);
                                    }
                                    if (callListener!=null){
                                        request=callListener.onChildProcessorInputRequest(request,processorId);
                                    }
                                    return request;
                                }

                                @Override
                                public IRequest onChildComponentInputRequest(IRequest request, String componentId) {
                                    if (listenerSelf!=null){
                                        request=listenerSelf.onChildComponentInputRequest(request,componentId);
                                    }
                                    if (listenerCrossed && callListener!=null){
                                        request = callListener.onChildComponentInputRequest(request,componentId);
                                    }
                                    return request;
                                }

                                @Override
                                public boolean onChildComponentReceivedResponse(IResponse response, IRequestSession session, String componentId, boolean intercepted) {
                                    if (listenerSelf!=null){
                                        listenerSelf.onChildComponentReceivedResponse(response,session,componentId,intercepted);
                                    }
                                    if (listenerCrossed && callListener!=null){
                                        callListener.onChildComponentReceivedResponse(response,session,componentId,intercepted);
                                    }
                                    return false;
                                }

                                @Override
                                public void onChildComponentException(IRequest request, Throwable throwable, String componentId) {
                                    if (listenerSelf!=null){
                                        listenerSelf.onChildComponentException(request,throwable,componentId);
                                    }
                                    if (listenerCrossed && callListener!=null){
                                        callListener.onChildComponentException(request,throwable,componentId);
                                    }
                                }

                                @Override
                                public IRequest onChildProcessorInputRequest(IRequest request, String processorId) {
                                    if (listenerSelf!=null){
                                        request=listenerSelf.onChildProcessorInputRequest(request,processorId);
                                    }
                                    if (listenerCrossed && callListener!=null){
                                        request=callListener.onChildProcessorInputRequest(request,processorId);
                                    }
                                    return request;
                                }

                                @Override
                                public boolean onChildProcessorReceivedResponse(IResponse response, String childProcessorId, boolean intercepted) {
                                    if (listenerSelf!=null){
                                        listenerSelf.onChildProcessorReceivedResponse(response,childProcessorId,intercepted);
                                    }
                                    if (listenerCrossed && callListener!=null){
                                        callListener.onChildProcessorReceivedResponse(response,childProcessorId,intercepted);
                                    }
                                    return false;
                                }

                                @Override
                                public IResponse onChildProcessorMergeResponses(ConcurrentLinkedHashMap<IResponse, String> responses, IResponse childResponse, String childProcessorId) {
                                    IResponse response=childResponse;
                                    if (listenerSelf!=null){
                                        response=listenerSelf.onChildProcessorMergeResponses(responses,response,childProcessorId);
                                    }
                                    if (listenerCrossed && callListener!=null){
                                        response=callListener.onChildProcessorMergeResponses(responses,response,childProcessorId);
                                    }
                                    return response;
                                }

                                @Override
                                public void onChildProcessorException(IRequest request, Throwable throwable, String childProcessorId) {
                                    if (listenerSelf!=null){
                                        listenerSelf.onChildProcessorException(request,throwable,childProcessorId);
                                    }
                                    if (listenerCrossed && callListener!=null){
                                        callListener.onChildProcessorException(request,throwable,childProcessorId);
                                    }
                                }

                                @Override
                                public boolean onProcessorReceivedResponse(IResponse response, String processorId, boolean intercepted) {
                                    if (listenerSelf!=null){
                                        listenerSelf.onProcessorReceivedResponse(response,processorId,intercepted);
                                    }
                                    if (callListener!=null){
                                        callListener.onChildProcessorReceivedResponse(response,processorId,intercepted);
                                    }
                                    responses.put(response, processorId);
                                    countDownLatch.countDown();
                                    return false;
                                }

                                @Override
                                public IResponse onProcessorMergeResponses(ConcurrentLinkedHashMap<IResponse, String> responses, String processorId) {
                                    IResponse response=null;
                                    if (listenerSelf!=null){
                                        response = listenerSelf.onProcessorMergeResponses(responses,processorId);
                                    }
                                    if (callListener!=null){
                                        response=callListener.onChildProcessorMergeResponses(responses,response,processorId);
                                    }
                                    return response;
                                }

                                @Override
                                public void onProcessorException(IRequest request, Throwable throwable, String processorId) {
                                    if (listenerSelf!=null){
                                        listenerSelf.onProcessorException(request,throwable,processorId);
                                    }
                                    if (callListener!=null){
                                        callListener.onChildProcessorException(request,throwable,processorId);
                                    }
                                    countDownLatch.countDown();
                                }

                            })
                            .build();
                    processorPro.submit(r);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            if (callListener!=null){
                callListener.onProcessorException(request,e,processorId);
            }
        }
    }
}
