package com.sad.jetpack.v1.componentization.api;

import android.os.Parcelable;

public interface ICallerConfig extends Parcelable {

    long delay();

    long timeout();

    Builder toBuilder();

    interface Builder{

        Builder delay(long delay);

        Builder timeout(long timeout);

        ICallerConfig build();
    }
}
