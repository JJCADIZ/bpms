	package com.example.bpm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.widget.ProgressBar;
import android.widget.Toast;

public class NotifyService extends Service {

	ConnectionClass connectionClass;
	Handler notificationHandler = new Handler();
	String notifTitle, notifBody;
	@SuppressWarnings("rawtypes")
	PendingIntent pi;
	int i = 0,user_type,object_id;

	private void handleIntent(Intent intent){
		// obtain the wake lock
        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        
        // check the global background data setting
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (!cm.getBackgroundDataSetting()) {
            stopSelf();
            return;
        }
        // do the actual work, in a separate thread
        notificationHandler.post(sendNotif);
	}
	
	private Runnable sendNotif = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				connectionClass = new ConnectionClass();
				Connection con = connectionClass.CONN();
				SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref",Context. MODE_PRIVATE); 
		        int id = Integer.parseInt(pref.getString("IDD", null));
              	if(con == null){
             		Toast.makeText(getApplicationContext(), "Error in connection", Toast.LENGTH_SHORT).show();
              	}else{
              		String query = "select * from notifications where notification_recipient = "+id+
              				" and notification_isread = 0 and mobile_status = 0";
              		Statement stmt = con.createStatement();
              		ResultSet rs = stmt.executeQuery(query);
              		while(rs.next()){
              			notifTitle = Html.fromHtml(rs.getString("notification_subject")).toString();
              			notifBody = Html.fromHtml(rs.getString("notification_body")).toString();
              			i = rs.getInt("notification_id");
              			createNotification();
              			String update_mob_status = "update notifications set mobile_status = 1 where notification_id = "+rs.getString("notification_id");
              			PreparedStatement update_stmt = con.prepareStatement(update_mob_status);
						update_stmt.executeUpdate();
              		}
              		notificationHandler.postDelayed(this, 1000);
              	}
			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	public void createNotification(){
		PendingIntent pi = PendingIntent.getActivity(this, i, new Intent(this, NotificationList.class), i);
		Resources r = getResources();
		Notification notification = new NotificationCompat.Builder(this)
									.setTicker(notifTitle).setSmallIcon(R.drawable.greeniconv3white)
									.setContentTitle(notifTitle).setContentText(notifBody)
									.setContentIntent(pi).setAutoCancel(true).build();
		notification.defaults |= Notification.DEFAULT_SOUND;
		NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notificationmanager.notify(i, notification);
		i++;
		
	}
	
    @Override
    public void onStart(Intent intent, int startId) {
    	notificationHandler.removeCallbacks(sendNotif);
        handleIntent(intent);
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    	notificationHandler.removeCallbacks(sendNotif);
        handleIntent(intent);
        return START_STICKY;
    }

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onTaskRemoved(Intent rootIntent) {
		// TODO Auto-generated method stub
		super.onTaskRemoved(rootIntent);
		Intent intent = new Intent(NotifyService.this, NotifyBroadcast.class);
		sendBroadcast(intent);
	}
	
}
