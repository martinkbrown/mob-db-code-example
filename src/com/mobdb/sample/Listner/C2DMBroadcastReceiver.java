/*
 */
package com.mobdb.sample.Listner;

import com.mobdb.sample.module.C2DMReceiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Helper class to handle BroadcastReciver behavior.
 * - can only run for a limited amount of time - it must start a real service 
 * for longer activity
 */
public class C2DMBroadcastReceiver extends BroadcastReceiver {
    
    @Override
    public final void onReceive(Context context, Intent intent) {
        // To keep things in one place.
       
        setResult(Activity.RESULT_OK, null /* data */, null /* extra */);
        intent.setClassName(context.getPackageName(), C2DMReceiver.class.getName());
        context.startService(intent);
    }
}


