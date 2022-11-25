package com.sad.jetpack.v1.componentization.api;

import static com.sad.jetpack.v1.componentization.api.ProcessorMode.PROCESSOR_MODE_CONCURRENCY;
import static com.sad.jetpack.v1.componentization.api.ProcessorMode.PROCESSOR_MODE_SEQUENCE;
import static com.sad.jetpack.v1.componentization.api.ProcessorMode.PROCESSOR_MODE_SINGLE;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@IntDef({PROCESSOR_MODE_SEQUENCE, PROCESSOR_MODE_CONCURRENCY,PROCESSOR_MODE_SINGLE})
@Retention(RetentionPolicy.SOURCE)
public @interface ProcessorMode {

    int PROCESSOR_MODE_SEQUENCE=0;

    int PROCESSOR_MODE_CONCURRENCY=1;

    int PROCESSOR_MODE_SINGLE=-1;
}