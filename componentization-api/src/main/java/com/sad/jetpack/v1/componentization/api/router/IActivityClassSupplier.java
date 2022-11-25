package com.sad.jetpack.v1.componentization.api.router;

import android.app.Activity;

public interface IActivityClassSupplier {

    String INTENT_PRIORITY="INTENT_PRIORITY";

    Class<? extends Activity> activityClass();

    int intentPriority();

}
