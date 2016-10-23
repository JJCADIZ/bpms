package com.example.bpm;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TV_Running extends Fragment {
	ConnectionClass connectionClass;
	private TextView timer,activity;
	private Button stopButton;
	private Button pauseButton;
	private long startTime;
	private Handler myHandler = new Handler();
	long timeInMillies = 0L;
	long timeSwap = 0L;
	long finalTime = 0L;
	long timeDeductor = 0L;
	
	long timeInMilliseconds = 0L;
	long timeSwapBuff = 0L;
	long updatedTime = 0L;
	String user_id,toast_string;
	Toast toast;
	int duration,tv_id;
	Context context;
	ProgressDialog loading = null;
	boolean visible = false;
	public TV_Running() {
		// TODO Auto-generated constructor stub
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		connectionClass = new ConnectionClass();
        View rootView = inflater.inflate(R.layout.running_task, container, false);
         timer = (TextView) rootView.findViewById(R.id.running_timer);
         activity = (TextView) rootView.findViewById(R.id.tv_activity);
         stopButton = (Button) rootView.findViewById(R.id.running_stop);
         pauseButton = (Button) rootView.findViewById(R.id.running_pause);
         duration = Toast.LENGTH_SHORT;
         context = rootView.getContext();
         if(visible == true){
        	 try {
              	Connection con = connectionClass.CONN();
              	if(con == null){
              		loading = ProgressDialog.show(context, "Loading", "Connecting", true, true);
              		loading.dismiss();
             		Toast.makeText(getActivity(), "Unable to Connect", Toast.LENGTH_SHORT);
              	}else{
              		loading = ProgressDialog.show(context, "Loading", "Connecting", true, true);
              		loading.dismiss();
             		Toast.makeText(getActivity(), "Connected", Toast.LENGTH_SHORT);
              		try {
                     	 getStartTime();
             		} catch (Exception e) {
             			// TODO: handle exception
             			toast = Toast.makeText(context, e.getMessage(), duration);
             			toast.show();
             			System.out.print(e.getMessage());
             		}
                      if(startTime == 0L){
                     	 timeInMillies = 0L;
             	 		 timeSwap = 0L;
             	 		 finalTime = 0L;
             	 		 startTime = 0L;
                      }else{
                     	 myHandler.postDelayed(updateTimerMethod, 0); 
                     	 
                           timeSwap += timeInMillies;
                      }
                      stopButton.setOnClickListener(new OnClickListener() {
               			
               			@Override
               			public void onClick(View arg0) {
               				if(startTime == 0L){
               		        	 timeInMillies = 0L;
               			 		 timeSwap = 0L;
               			 		 finalTime = 0L;
               			 		 startTime = 0L;
               		         }else{
               		        	 myHandler.postDelayed(updateTimerMethod, 0); 
               		        	 
               		        	timeInMillies = 0L;
               	  	 			timeSwap = 0L;
               	  	 			finalTime = 0L;
               	  	 			startTime = 0L;
               	  				activityStop();
               		         }
               			}
               		});
                        
                        pauseButton.setOnClickListener(new OnClickListener() {
               			
               			@Override
               			public void onClick(View v) {
               				// TODO Auto-generated method stub
               			   activityPause();
               			  	if(startTime == 0L){
             		        	 timeInMillies = 0L;
             			 		 timeSwap = 0L;
             			 		 finalTime = 0L;
             			 		 startTime = 0L;
             		         }else{
             		        	 myHandler.postDelayed(updateTimerMethod, 0); 
             		        	 
             		        	timeInMillies = 0L;
             	  	 			timeSwap = 0L;
             	  	 			finalTime = 0L;
             	  	 			startTime = 0L;
             	  	 			activityPause();
             		         }
               			  
               			}
               		});
              	}
      		} catch (Exception e) {
      			// TODO: handle exception
      			loading = ProgressDialog.show(context, "Loading", "Connecting", true, true);
      		}
         }
        return rootView;
    }
	
	private Runnable updateRunningTask = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			getStartTime();
			myHandler.postDelayed(this, 0);
			
		}
	};
	
	private Runnable updateTimerMethod = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			//timeInMillies = SystemClock.uptimeMillis() - startTime;
			timeInMillies = startTime;
			if(timeInMillies == 0){
				finalTime = 0;
			}else{
				finalTime = 1 + timeInMillies;
			}
			   //int seconds = (int) (finalTime / 1000);
			   int seconds = (int) (finalTime);
			   int minutes = seconds / 60;
			   int hours = minutes / 60;
			   int days = hours / 24;
			   seconds = seconds % 60;
			   int milliseconds = (int) (finalTime % 1000);
			   timer.setText(days + ":" + String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":"
			     + String.format("%02d", seconds));
			   //+ String.format("%02d", milliseconds)
			   myHandler.postDelayed(this, 0);

		}
	};
	
	public void getStartTime(){
		SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref",Context. MODE_PRIVATE); 
        Editor editor = pref.edit();
        user_id = pref.getString("IDD", null);
        int id = Integer.parseInt(user_id);
        try {
			Connection con = connectionClass.CONN(); 
			if(con == null){
				toast_string = "Error in connection with SQL server";
				toast = Toast.makeText(context, toast_string, duration);
				toast.show();
			}else{
				String get_runningactivity_query = "select activity,start_date,tv_id from timeandvolume where employee_number = "+id+" AND status = 'running'";
				Statement get_runningactivity_stmt = con.createStatement();
				ResultSet get_runningactivity_rs = get_runningactivity_stmt.executeQuery(get_runningactivity_query);
				
				int rs_row = 0;
				while(get_runningactivity_rs.next()){
					rs_row++;
				}
				
				if(rs_row > 0){
					ResultSet get_runningactivity_rs1 = get_runningactivity_stmt.executeQuery(get_runningactivity_query);
					if(get_runningactivity_rs1.next()){
						activity.setText(get_runningactivity_rs1.getString("activity"));
						//startTime = Long.parseLong(get_runningactivity_rs1.getString(2));
						
						//startTime = get_runningactivity_rs1.getDate(2).getTime();
						tv_id = Integer.parseInt(get_runningactivity_rs1.getString("tv_id"));
						String get_time_query = "select datediff(day,start_date,getdate()) as diffday,"+
						 "datediff(second,start_date,getdate()) as diffmin, "+
						 "getdate() as end_date, datediff(second,start_date,getdate())/volume as cycle_time "+
						 "from timeandvolume where tv_id = "+tv_id;
						Statement get_time_stmt = con.createStatement();
						ResultSet get_time_rs = get_time_stmt.executeQuery(get_time_query);
						if(get_time_rs.next()){
							//startTime = get_time_rs.getDate(2).getTime()/60;
							startTime = Long.parseLong(get_time_rs.getString("diffmin"));
							//timeDeductor = get_time_rs.getDate(2).getTime()/60
						}
					}else{
						toast_string = get_runningactivity_rs1.getString(1);
						toast = Toast.makeText(context, toast_string, duration);
						toast.show();
					}
				}else{
					activity.setText("No running activity");
					startTime = 0L;
				}
				
			}
		} catch (Exception e) {
			// TODO: handle exception
			toast = Toast.makeText(context, e.getMessage(), duration);
			toast.show();
		}
	}
	
	public void activityStop(){
		SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref",Context. MODE_PRIVATE); 
        Editor editor = pref.edit();
        user_id = pref.getString("IDD", null);
        int id = Integer.parseInt(user_id);
        try {
			Connection con = connectionClass.CONN();
			if(con == null){
				toast_string = "Error in connection with SQL server";
				toast = Toast.makeText(context, toast_string, duration);
				toast.show();
			}else{
				String select_query = "select datediff(day,start_date,getdate()) as diffday,"+
						 "datediff(second,start_date,getdate()) as diffmin,"+ 
						 "getdate() as end_date, datediff(second,start_date,getdate())/volume as cycle_time "+
						 "from timeandvolume where tv_id = "+tv_id;
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(select_query);
				int rs_row = 0;
				while(rs.next()){
					rs_row++;
				}
				
				if(rs_row > 0){
					ResultSet rs1 = stmt.executeQuery(select_query);
					if(rs1.next()){
						String update_query = "update timeandvolume set end_date = '"+rs1.getString("end_date")+
								"', elapsed_time = "+Double.parseDouble(rs1.getString("diffmin"))/60+
								", cycle_time = "+Double.parseDouble(rs1.getString("cycle_time"))/60+
								", status = 'stop' where tv_id = "+tv_id;
						PreparedStatement update_stmt = con.prepareStatement(update_query);
						update_stmt.executeUpdate();
						toast_string = "Activity has been stop";
						toast = Toast.makeText(context, toast_string, duration);
						toast.show();
					}else{
						toast_string = "Unsuccess Stopping of Activity";
						toast = Toast.makeText(context, toast_string, duration);
						toast.show();
					}
				}else{
					toast_string = "Unsuccess Stopping of Activity";
					toast = Toast.makeText(context, toast_string, duration);
					toast.show();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			toast = Toast.makeText(context, e.getMessage(), duration);
			toast.show();
		}
	}
	
	public void activityPause(){
		SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref",Context. MODE_PRIVATE); 
        Editor editor = pref.edit();
        user_id = pref.getString("IDD", null);
        int id = Integer.parseInt(user_id);
        try {
			Connection con = connectionClass.CONN();
			if(con == null){
				toast_string = "Error in connection with SQL server";
				toast = Toast.makeText(context, toast_string, duration);
				toast.show();
			}else{
				String select_query = "select datediff(day,start_date,getdate()) as diffday,"+
						 "datediff(second,start_date,getdate()) as diffmin,"+ 
						 "getdate() as end_date, datediff(second,start_date,getdate())/volume as cycle_time "+
						 "from timeandvolume where tv_id = "+tv_id;
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(select_query);
				int rs_row = 0;
				while(rs.next()){
					rs_row++;
				}
				if(rs_row > 0){
					ResultSet rs1 = stmt.executeQuery(select_query);
					if(rs1.next()){
						String update_query = "update timeandvolume set end_date = '"+rs1.getString("end_date")+
								"', elapsed_time = "+Double.parseDouble(rs1.getString("diffmin"))/60+
								", cycle_time = "+Double.parseDouble(rs1.getString("cycle_time"))/60+
								", status = 'pause' where tv_id = "+tv_id;
						PreparedStatement update_stmt = con.prepareStatement(update_query);
						update_stmt.executeUpdate();
						toast_string = "Activity has been pause";
						toast = Toast.makeText(context, toast_string, duration);
						toast.show();
					}else{
						toast_string = "Unsuccess Pausing of Activity";
						toast = Toast.makeText(context, toast_string, duration);
						toast.show();
					}
				}else{
					toast_string = "Unsuccess Pausing of Activity";
					toast = Toast.makeText(context, toast_string, duration);
					toast.show();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			toast = Toast.makeText(context, e.getMessage(), duration);
			toast.show();
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		myHandler.postDelayed(updateRunningTask, 1000);
		myHandler.removeCallbacks(updateRunningTask);
		myHandler.postDelayed(updateTimerMethod, 1000);
		myHandler.removeCallbacks(updateTimerMethod);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		myHandler.postDelayed(updateRunningTask, 1000);
		myHandler.removeCallbacks(updateRunningTask);
		myHandler.postDelayed(updateTimerMethod, 1000);
		myHandler.removeCallbacks(updateTimerMethod);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try {
        	Connection con = connectionClass.CONN();
        	if(con == null){
        		loading = ProgressDialog.show(context, "Loading", "Connecting", true, true);
        		myHandler.removeCallbacks(updateRunningTask);
        		myHandler.removeCallbacks(updateTimerMethod);
        	}else{
        		loading = ProgressDialog.show(context, "Loading", "Connecting", true, true);
        		loading.dismiss();
        		myHandler.postDelayed(updateRunningTask, 1000);
        		myHandler.postDelayed(updateTimerMethod, 0); 
        	}
		} catch (Exception e) {
			// TODO: handle exception
			loading = ProgressDialog.show(context, "Loading", "Connecting", true, true);
		}
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		try {
        	Connection con = connectionClass.CONN();
        	if(con == null){
        		loading = ProgressDialog.show(context, "Loading", "Connecting", true, true);
        		myHandler.postDelayed(updateRunningTask, 1000);
        		myHandler.removeCallbacks(updateRunningTask);
        		myHandler.postDelayed(updateTimerMethod, 1000);
        		myHandler.removeCallbacks(updateTimerMethod);
        	}else{
        		loading = ProgressDialog.show(context, "Loading", "Connecting", true, true);
        		loading.dismiss();
        		Toast.makeText(getActivity(), "Connected", Toast.LENGTH_SHORT);
        		myHandler.postDelayed(updateRunningTask, 1000);
        		myHandler.postDelayed(updateTimerMethod, 0); 
        	}
		} catch (Exception e) {
			// TODO: handle exception
			loading = ProgressDialog.show(context, "Loading", "Connecting", true, true);
			myHandler.postDelayed(updateRunningTask, 1000);
			myHandler.removeCallbacks(updateRunningTask);
			myHandler.postDelayed(updateTimerMethod, 1000);
			myHandler.removeCallbacks(updateTimerMethod);
		}
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		myHandler.postDelayed(updateRunningTask, 1000);
		myHandler.removeCallbacks(updateRunningTask);
		myHandler.postDelayed(updateTimerMethod, 1000);
		myHandler.removeCallbacks(updateTimerMethod);
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub
		super.setUserVisibleHint(isVisibleToUser);
		if(isVisibleToUser){
			Toast.makeText(getActivity(), "Running", Toast.LENGTH_SHORT).show();
			visible = true;
		}else{
			visible = false;
		}
	}
	
}
