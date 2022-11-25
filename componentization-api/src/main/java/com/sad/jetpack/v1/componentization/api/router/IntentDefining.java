package com.sad.jetpack.v1.componentization.api.router;

import android.content.Context;
import android.content.Intent;

public interface IntentDefining {

    Intent intentFromParameters(Context context, Intent intent, IActivityRouterParameters parameters, ClassLoader loader);
}
