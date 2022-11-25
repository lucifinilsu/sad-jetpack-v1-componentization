package com.sad.jetpack.v1.componentization.api.router;

import android.content.Context;
import android.content.Intent;

public interface ILaunchActivityAction {

    boolean doStartActivities(Context context, Intent[] intent, IActivityRouterParameters params) throws Exception;

    boolean doStartActivity(Context context, Intent intent, IActivityRouterParameters params) throws Exception;

}
