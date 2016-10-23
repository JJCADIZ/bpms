package com.example.bpm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class runningActivity extends Activity {
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
	public runningActivity() {
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.running_task);
		connectionClass = new ConnectionClass();
		timer = (TextView) findViewById(R.id.running_timer);
        activity = (TextView) findViewById(R.id.tv_activity);
        stopButton = (Button) findViewById(R.id.running_stop);
        pauseButton = (Button) findViewById(R.id.running_pause);
        duration = Toast.LENGTH_SHORT;
        try {
        	Connection con = connectionClass.CONN(); 
			if(con == null){
				toast_string = "Error in connection";
				toast = Toast.makeText(context, toast_string, duration);
				toast.show();
			}else{
				getStartTime();
			}
		} catch (Exception e) {
			// TODO: handle exception
			toast = Toast.makeText(context, e.getMessage(), duration);
			toast.show();
		}
         if(startTime == 0L){
        	 timeInMillies = 0L;
	 		 timeSwap = 0L;
	 		 finalTime = 0L;
	 		 startTime = 0L;
         }else{
        	 myHandler.post(updateTimerMethod); 
        	 
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
  	  	 			try {
  	  	 				Connection con = connectionClass.CONN(); 
  	  	 				if(con == null){
  	  	 					Toast.makeText(getApplicationContext(), "Error in connection", Toast.LENGTH_SHORT).show();
  	  	 				}else{
  	  	 					myHandler.post(updateTimerMethod); 
  	  	 					timeInMillies = 0L;
  	  	  	 				timeSwap = 0L;
  	  	  	 				finalTime = 0L;
  	  	  	 				startTime = 0L;
  	  	 					activityStop();
  	  	 				}
					} catch (Exception e) {
						// TODO: handle exception
						Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
					}
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
	  	 			try {
  	  	 				Connection con = connectionClass.CONN(); 
  	  	 				if(con == null){
  	  	 					Toast.makeText(getApplicationContext(), "Error in connection", Toast.LENGTH_SHORT).show();
  	  	 				}else{
  	  	 					myHandler.post(updateTimerMethod); 
  			        	 
  			        		timeInMillies = 0L;
  		  	 				timeSwap = 0L;
  		  	 				finalTime = 0L;
  		  	 				startTime = 0L;
  	  	 					activityPause();
  	  	 				}
					} catch (Exception e) {
						// TODO: handle exception
						Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
					}
		         }
  			  
  			}
  		});
         context = getApplicationContext();
         getActionBar().setHomeButtonEnabled(true);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
        	case android.R.id.home:
        		// app icon in action bar clicked; goto parent activity.
        		//myHandler.postDelayed(updateRunningTask, 1000);
        		myHandler.removeCallbacks(updateRunningTask);
        		//myHandler.postDelayed(updateTimerMethod, 1000);
        		myHandler.removeCallbacks(updateTimerMethod);
            	this.finish();
            	return true;
        	default:
        		return super.onOptionsItemSelected(item);
		}
	}


	private Runnable updateRunningTask = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				Connection con = connectionClass.CONN(); 
				if(con == null){
					toast_string = "Error in connection";
					toast = Toast.makeText(context, toast_string, duration);
					toast.show();
				}else{
					getStartTime();
				}
				myHandler.postDelayed(this, 0);
			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
			}
			
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
			   myHandler.post(this);

		}
	};
	
	public void getStartTime(){
		SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref",Context. MODE_PRIVATE); 
        user_id = pref.getString("IDD", null);
        int id = Integer.parseInt(user_id);
        try {
			Connection con = connectionClass.CONN(); 
			if(con == null){
				toast_string = "Error in connection";
				toast = Toast.makeText(context, toast_string, duration);
				toast.show();
			}else{
				String get_runningactivity_query = "select * from timeandvolume where employee_number = "+id+" AND status = 'running'";
				Statement get_runningactivity_stmt = con.createStatement();
				ResultSet get_runningactivity_rs = get_runningactivity_stmt.executeQuery(get_runningactivity_query);
				
				int rs_row = 0;
				while(get_runningactivity_rs.next()){
					rs_row++;
				}
				
				if(rs_row > 0){
					ResultSet get_runningactivity_rs1 = get_runningactivity_stmt.executeQuery(get_runningactivity_query);
					if(get_runningactivity_rs1.next()){
						int tv_employee_number = get_runningactivity_rs1.getInt("employee_number");
						String tv_core_process = get_runningactivity_rs1.getString("core_process");
						String tv_product = get_runningactivity_rs1.getString("product");
						String tv_sub_process = get_runningactivity_rs1.getString("sub_process");
						String tv_activity = get_runningactivity_rs1.getString("activity");
						int tv_work_status = get_runningactivity_rs1.getInt("work_status");
						String tv_status = "stop";
						String query_2 = "select max(end_date) as base_date from timeandvolume " +
								"where employee_number = "+tv_employee_number+
								" AND core_process = '"+tv_core_process+"' "+
								"AND product = '"+tv_product+"' AND sub_process = '"+tv_sub_process+"' "+ 
								"AND activity = '"+tv_activity+"' AND work_status = '"+tv_work_status+"' "+
								"AND status = '"+tv_status+"' ";
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
		SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref",Context. MODE_PRIVATE); 
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
		SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref",Context. MODE_PRIVATE); 
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
		//myHandler.postDelayed(updateRunningTask, 1000);
		myHandler.removeCallbacks(updateRunningTask);
		//myHandler.postDelayed(updateTimerMethod, 1000);
		myHandler.removeCallbacks(updateTimerMethod);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//myHandler.postDelayed(updateRunningTask, 1000);
		myHandler.removeCallbacks(updateRunningTask);
		//myHandler.postDelayed(updateTimerMethod, 1000);
		myHandler.removeCallbacks(updateTimerMethod);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try {
        	Connection con = connectionClass.CONN();
        	if(con == null){
        		//loading = ProgressDialog.show(context, "Loading", "Connecting", true, true);
        		myHandler.removeCallbacks(updateRunningTask);
        		myHandler.removeCallbacks(updateTimerMethod);
        	}else{
        		//loading = ProgressDialog.show(context, "Loading", "Connecting", true, true);
        		//loading.dismiss();
        		myHandler.post(updateRunningTask);
        		myHandler.post(updateTimerMethod); 
        	}
		} catch (Exception e) {
			// TODO: handle exception
			//loading = ProgressDialog.show(context, "Loading", "Connecting", true, true);
			Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}

}
