package com.sad.jetpack.v1.componentization.api;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

abstract class AbsInternalComponentProcessor implements IComponentProcessor,IComponentProcessor.Builder,IResponseBackTrackable{

    protected String processorId="";
    protected IComponentProcessorCallListener callListener;
    protected ICallerConfig callerConfig;
    protected List<Object> units =new LinkedList<>();
    protected boolean listenerCrossed=false;
    protected int priority=0;

    protected AtomicBoolean isIntercepted=new AtomicBoolean(false);
    protected AtomicInteger indexIntercepted=new AtomicInteger(0);
    @Override
    public String backTrackableId() {
        return processorId();
    }

    @Override
    public int priority() {
        return this.priority;
    }

    @Override
    public Builder priority(int priority) {
        this.priority=priority;
        return this;
    }

    @Override
     public boolean listenerCrossed() {
         return this.listenerCrossed;
     }

     @Override
    public String processorId() {
        return processorId;
    }

    @Override
    public IComponentProcessorCallListener listener() {
        return callListener;
    }

    @Override
    public ICallerConfig callerConfig() {
        return callerConfig;
    }

    @Override
    public int childrenCount() {
        return units.size();
    }

    @Override
    public IComponentProcessor remove(String url,boolean traverseChildren) {
        Iterator<Object> iterator = units.iterator();
        while (iterator.hasNext()) {
            Object s = iterator.next();
            if (s instanceof IComponentCallable){
                if (((IComponentCallable) s).componentId().equals(url)){
                    iterator.remove();
                }
            }
            else if (s instanceof IComponentProcessor){
                if (((IComponentProcessor) s).processorId().equals(url)){
                    iterator.remove();
                }
                else {
                    if (traverseChildren){
                        IComponentProcessor ss=((IComponentProcessor) s).remove(url);
                        if (ss.childrenCount()==0){
                            iterator.remove();
                        }
                    }
                }
            }
        }
        return this;
    }

    @Override
    public IComponentProcessor join(IComponentCallable componentCallable) {
        units.add(componentCallable);
        return this;
    }

    @Override
    public IComponentProcessor join(List<IComponentCallable> componentCallables) {
        units.addAll(componentCallables);
        return this;
    }

    @Override
    public IComponentProcessor join(IComponentProcessor processor) {
        units.add(processor);
        return this;
    }


    @Override
    public Builder toBuilder() {
        return this;
    }

    @Override
    public Builder listener(IComponentProcessorCallListener listener) {
        this.callListener=listener;
        return this;
    }

     @Override
     public Builder listenerCrossed(boolean crossed) {
         this.listenerCrossed=crossed;
         return this;
     }

     @Override
    public Builder callerConfig(ICallerConfig callerConfig) {
        this.callerConfig=callerConfig;
        return this;
    }

    @Override
    public IComponentProcessor build() {
        return this;
    }

    protected boolean needDelay(){
        return callerConfig!=null && callerConfig.delay()>0;
    }
    protected boolean needCheckTimeout(){
        return callerConfig!=null && callerConfig.timeout()>0;
    }
}
