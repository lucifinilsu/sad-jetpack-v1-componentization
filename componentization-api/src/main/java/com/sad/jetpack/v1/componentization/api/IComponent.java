package com.sad.jetpack.v1.componentization.api;

import com.sad.jetpack.v1.componentization.annotation.Component;

import java.lang.annotation.Annotation;

public interface IComponent extends Comparable<IComponent>,ISortable,IResponseBackTrackable{

    default void onCall(IRequest request, IResponseSession session) throws Exception{};

    default <T> T toDo(IRequest request) throws Exception{return null;}

    default String[] urls(){
        Component component=info();
        if (component!=null){
            return component.url();
        }
        return null;
    };

    default String description(){
        Component component=info();
        if (component!=null){
            return component.description();
        }
        return "";
    }

    default int version(){
        Component component=info();
        if (component!=null){
            return component.version();
        }
        return 1;
    }


    default Component info(){
        Annotation[] annotations=getClass().getDeclaredAnnotations();
        Component component = null;
        if (annotations!=null && annotations.length>0){
            for (Annotation a :annotations) {
                if (a instanceof Component){
                    component= (Component) a;
                    break;
                }
            }
            return component;
        }
        else {
            return null;
        }
    }

    @Override
    default int compareTo(IComponent o){
        return o.priority()-this.priority();
    }

    //default String instanceOrgUrl(){return urls()!=null && urls().length>0?urls()[0]:"";};

    default int priority(){return 0;};
}
