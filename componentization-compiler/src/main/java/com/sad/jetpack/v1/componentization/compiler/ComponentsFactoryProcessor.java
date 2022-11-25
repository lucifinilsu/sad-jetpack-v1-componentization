package com.sad.jetpack.v1.componentization.compiler;

import com.google.auto.service.AutoService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.sad.jetpack.v1.componentization.annotation.Component;
import com.sad.jetpack.v1.componentization.annotation.Utils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.StandardLocation;

/**
 * Created by Administrator on 2019/4/8 0008.
 */
@AutoService(Processor.class)
@SupportedSourceVersion(value = SourceVersion.RELEASE_8)
@SupportedOptions({"log"})
@SupportedAnnotationTypes({
        Constant.PACKAGE_ANNOTATION +".Component"
})
public class ComponentsFactoryProcessor extends AbsProcessor {

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        isLog=Boolean.valueOf(env.getOptions().getOrDefault("log","false"));
        log=new ProcessorLog(messager,isLog);
        log.config_err("log");
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotationedElements, RoundEnvironment roundEnv) {
        if (CollectionUtils.isNotEmpty(annotationedElements)){
            //第一步，解析Component注解的类
            Set<? extends Element> listServiceElements=roundEnv.getElementsAnnotatedWith(Component.class);
            if (!CollectionUtils.isNotEmpty(listServiceElements)){
                log.error(">>>未发现注解Component");
                return true;
            }
            for (Element element:listServiceElements
                 ) {
                if (element.getKind() != ElementKind.CLASS) {
                    log.error(">>>错误的注解类型，只有【类】才能够被该Component注解处理");
                    continue;
                }
                Set<Modifier> mod=element.getModifiers();
                if (mod.contains(Modifier.ABSTRACT)){
                   log.error(">>>"+element.getSimpleName().toString()+"是抽象类，故无法进行注册:");
                   continue;
                }

                registerERM(element);

                /*Element elementIAC=elementUtils.getTypeElement(Constant.PACKAGE_API+".IExposedWorkerService");
                if (!typeUtils.isSubtype(typeUtils.erasure(element.asType()),typeUtils.erasure(elementIAC.asType()))){
                    String note=">>>请注意"+element.getSimpleName().toString()+"不是IExposedWorkerService的实现类，无法注册其Worker。";
                    log.error(note);
                    continue;
                }
                try {
                    //appendToDoc(element);
                    createWorker(element);
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
            }

        }

        if (roundEnv.processingOver()){
            //结束解析，生成注册类

        }
        return false;
    }
    private List<String> assetsDirs=new ArrayList<>();
    private void registerERM(Element element){
        Component exposedAnnotation=element.getAnnotation(Component.class);
        if (exposedAnnotation!=null){
            String[] assetsDs=exposedAnnotation.assetsDir();
            String[] urls=exposedAnnotation.url();
            if (!ObjectUtils.isEmpty(assetsDs)){
                for (String a:assetsDs){
                    for (String url:urls
                         ) {
                        try {
                            log.error(">>> 目标url："+url);
                            createMap(((TypeElement)element).getQualifiedName().toString(),url,a,exposedAnnotation.description(),exposedAnnotation.version());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            /*String url=exposedAnnotation.url();
            if (!ObjectUtils.isEmpty(url)){
                log.error(">>> 目标url："+url);
                String[] assetsDs=exposedAnnotation.assetsDir();
                if (!ObjectUtils.isEmpty(assetsDs)){
                    for (String a:assetsDs){

                    }
                }
            }*/
        }

    }

    private void createMap(String clsName,String u,String assetsDir,String description,int version) throws Exception{

        if (ObjectUtils.isEmpty(u)){
            return;
        }
        String outPath=filer.getResource(StandardLocation.SOURCE_OUTPUT,"","xxx").getName();
        String[] paths=outPath.split("build");
        String rootPath=paths[0];
        if (!ObjectUtils.isEmpty(assetsDir) && assetsDirs.indexOf(assetsDir)==-1){
            log.error(">>> 清空目录："+assetsDir);
            StringBuilder sbCRM=new StringBuilder();
            sbCRM.append(rootPath+ File.separator)
                    .append(assetsDir)
                    .append(CRM_DIR);
            File f=new File(sbCRM.toString());
            if (f.exists()){
                org.apache.commons.io.FileUtils.deleteDirectory(f);
            }
            assetsDirs.add(assetsDir);

        }
        URI uri=new URI(u);
        String scheme=uri.getScheme();
        String host=uri.getHost();
        log.error(">>> 目标url信息：scheme="+scheme+",host="+host);
        String path="/"+scheme+"/"+host+uri.getPath();
        //log.error(">>> 目标路径："+new URL(u).getPath())
        String name=path.substring(path.lastIndexOf('/')+1);
        log.error(">>> 目标名称："+name);
        String query=uri.getQuery();
        if (!ObjectUtils.isEmpty(name)){
            log.error(">>> 模块路径："+rootPath);
            StringBuilder sbApath=new StringBuilder();
            StringBuilder crmPath=new StringBuilder();
            crmPath
                    .append(Utils.crmPaths(CRM_DIR,u)/*+File.separator*/)
                    //.append(packageName)
                    //.append(path.replace("/",File.separator))
            ;
            sbApath
                    .append(rootPath+File.separator)
                    .append(assetsDir)
                    .append(crmPath.toString());


            String aPath=sbApath.toString();
            log.error(">>> 目标映射路径："+aPath);
            File fa=new File(aPath);
            File dir=fa.getParentFile();
            if ((dir.exists() && dir.isDirectory()) || dir.mkdirs()){
                String content=createJson(crmPath.toString(),clsName,u,description,version);
                log.error(">>> 目标内容："+content);
                if(!fa.exists()){
                    fa.createNewFile();
                    //FileUtils.createFile(fa,content);
                }
                org.apache.commons.io.FileUtils.writeStringToFile(fa,content,"utf-8");
                /*else {
                    FileUtils.writeToFile(fa,content);
                }*/
            }
        }
    }

    private String createJson(String path,String clsName,String u,String description,int version){

        String j="";
        try {

            GsonBuilder gsonBuilder=new GsonBuilder();
            gsonBuilder.setPrettyPrinting();
            gsonBuilder.disableHtmlEscaping();
            Gson gson= gsonBuilder.create();
            JsonObject jsonObject=new JsonObject();
            /*jsonObject.addProperty("path",path);
            JsonObject jo_e=new JsonObject();
            jo_e.addProperty("url",u);
            jo_e.addProperty("class",clsName);
            jo_e.addProperty("description",description);
            jsonObject.add("element",jo_e);*/
            jsonObject.addProperty("url",u);
            jsonObject.addProperty("resPath",path);
            jsonObject.addProperty("className",clsName);
            jsonObject.addProperty("description",description);
            jsonObject.addProperty("version",version);
            j=gson.toJson(jsonObject);
        }catch(Exception e){
            e.printStackTrace();
        }
        return j;
    }
    @Deprecated
    private void createWorker(Element element){
        try {
            String workerPackage="androidx.work";
            Component Component=element.getAnnotation(Component.class);
            String[] urls=Component.url();
            for (String url:urls
                 ) {
                TypeSpec.Builder tb=TypeSpec.classBuilder(Utils.creatExposedWorkerClassName(element.getSimpleName().toString(),Utils.ermPaths(url).get(0)))
                        .addModifiers(Modifier.PUBLIC)
                        .superclass(ClassName.bestGuess("androidx.work.ComponentWorker"))
                        ;
                MethodSpec m_constructor=MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(
                                ParameterSpec
                                        .builder(ClassName.bestGuess("android.content.Context"),"context")
                                        .build()
                        )
                        .addParameter(ClassName.bestGuess("androidx.work.WorkerParameters"),"workerParams")
                        .addStatement("super(context,workerParams)")
                        .build();

                MethodSpec.Builder mb_serviceInstance=MethodSpec.methodBuilder("exposedWorkerServiceInstance")
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(
                                ParameterSpec
                                        .builder(ClassName.bestGuess("android.content.Context"),"context")
                                        .build()
                        )
                        .addParameter(ClassName.bestGuess("androidx.work.WorkerParameters"),"workerParams")
                        .beginControlFlow("try")
                        .addStatement("$T Component= $T.newInstance().getFirst($S).instance()"
                                ,ClassName.bestGuess(Constant.PACKAGE_API+".IExposedWorkerService")
                                ,ClassName.bestGuess(Constant.PACKAGE_API+".ComponentManager")
                                ,url
                        )
                        .addStatement("return Component")
                        .endControlFlow()
                        .beginControlFlow("catch($T e)",Exception.class)
                        .addStatement("e.printStackTrace()")
                        .endControlFlow()
                        .addStatement("return null")
                        .returns(ClassName.bestGuess(Constant.PACKAGE_API+".IExposedWorkerService"))
                        ;
                tb.addMethod(m_constructor)
                        .addMethod(mb_serviceInstance.build())
                ;

                JavaFile.Builder jb= JavaFile.builder(workerPackage,tb.build());
                jb.build().writeTo(filer);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private final static String CRM_DIR ="crm";


    /*private final static String dir="";
    private void appendToDoc(Element element) throws Exception{
        Component annotation=element.getAnnotation(Component.class);
        String u=annotation.url();
        String des=annotation.description();
        URL url=new URL(u);
        String protocol=url.getProtocol();
        String host=url.getHost();
        String path=url.getPath();
        String name=path.substring(path.lastIndexOf('/')+1);
        String query=url.getQuery();


    }*/


}
