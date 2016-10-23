package com.example.bpm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.WakefulBroadcastReceiver;

public class NotifyBroadcast extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
	    if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
	    	Intent service = new Intent(context, NotifyService.class);
		    context.startService(service);
        }else{
        	Intent service = new Intent(context, NotifyService.class);
		    context.stopService(service);
        }
	}
}
