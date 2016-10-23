package com.example.bpm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

public class pausedActivity extends Activity {
	ConnectionClass connectionClass;
	private Handler myHandler = new Handler();
	ListView paused_list;
	ArrayAdapter<String> adapter;
	List<String> paused_list_value, tv_id_list_value;
	Context context;
	Toast toast;
	int duration;
	String tv[][];
	String user_id, toast_string;
	ProgressDialog loading = null;
	public pausedActivity() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.paused_tasks);
		connectionClass = new ConnectionClass();
		paused_list  = (ListView) findViewById(R.id.paused_listview);
		//myHandler.post(updatePausedTask);
        addPausedListItems();
        paused_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position,
					long arg3) {
				// TODO Auto-generated method stub
				
				toast_string = "Hold me longer!";
        		toast = Toast.makeText(getApplicationContext(), toast_string, duration);
        		toast.show();
				
			}
			
        	
		});
        
        /** Registering context menu for the listview */
        registerForContextMenu(paused_list);
        //addPausedListItems();
        //myHandler.post(updatePausedTask);
        duration = Toast.LENGTH_SHORT;
        context = getApplicationContext();
        getActionBar().setHomeButtonEnabled(true);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
        	case android.R.id.home:
        		// app icon in action bar clicked; goto parent activity.
        		myHandler.removeCallbacks(updatePausedTask);
            	this.finish();
            	return true;
        	default:
        		return super.onOptionsItemSelected(item);
		}
	}
	
private Runnable updatePausedTask = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				Connection con = connectionClass.CONN();
				if(con == null){
					Toast.makeText(getApplicationContext(), "Error in connection", Toast.LENGTH_SHORT).show();
					myHandler.removeCallbacks(updatePausedTask);
				}else{
					SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref",Context. MODE_PRIVATE); 
			        user_id = pref.getString("IDD", null); 
			        int id = Integer.parseInt(user_id);
					String query = "select * from timeandvolume where employee_number = "+id+" AND status = 'pause'";
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery(query);
					int rs_count = 0;
					while(rs.next()){
						rs_count++;
					}
					if(paused_list.getAdapter().getCount() != rs_count){
						addPausedListItems();
						myHandler.post(updatePausedTask);
					}else{
						//myHandler.removeCallbacks(updatePausedTask);
					}
					//myHandler.postDelayed(updatePausedTask, 1000);
				}
				
			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		getMenuInflater().inflate(R.menu.tv_paused_floating_menu , menu);
	}
	
	

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		 
        switch(item.getItemId()){
            case R.id.float_tv_play:
                //Toast.makeText(this, "Edit : " + paused_list[info.position]  , Toast.LENGTH_SHORT).show();
            	tvPlay(tv_id_list_value.get(info.position));
            	Toast.makeText(context, "Play: "+tv_id_list_value.get(info.position), duration).show();
            	adapter.notifyDataSetChanged();
            	finish();
            	Intent goDirectorReport = new Intent(pausedActivity.this, runningActivity.class);
				startActivity(goDirectorReport);
                break;
            case R.id.float_tv_stop:
                //Toast.makeText(this, "Delete : " + countries[info.position]  , Toast.LENGTH_SHORT).show();
            	tvStop(tv_id_list_value.get(info.position));
            	Toast.makeText(context, "Stop: "+tv_id_list_value.get(info.position), duration).show();
            	adapter.notifyDataSetChanged();
                break;
            case R.id.float_tv_cancel:
                //Toast.makeText(this, "Share : " + countries[info.position]  , Toast.LENGTH_SHORT).show();
            	//Toast.makeText(context, "Cancel"+tv_id_list_value.get(info.position), duration).show();
                break;
 
        }
        return true;
	}

	public void addPausedListItems(){
		paused_list_value = new ArrayList<String>();
        tv_id_list_value = new ArrayList<String>();
		getPausedListItems();
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1 , paused_list_value);
        paused_list.setAdapter(adapter);
        myHandler.post(updatePausedTask);
	}
	
	public void getPausedListItems(){
		SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref",Context. MODE_PRIVATE); 
        user_id = pref.getString("IDD", null); 
        int id = Integer.parseInt(user_id);
        try {
        	Connection con = connectionClass.CONN();
        	
        	if(con == null){
        		toast_string = "Error in connection";
        		toast = Toast.makeText(getApplicationContext(), toast_string, duration);
        		toast.show();
        	}else{
        		String get_query = "select tv_id, activity from timeandvolume where employee_number = "+id+" AND status = 'pause'";
        		Statement get_stmt = con.createStatement();
        		ResultSet get_rs = get_stmt.executeQuery(get_query);
        		while(get_rs.next()){
        			tv_id_list_value.add(get_rs.getString("tv_id"));
        			paused_list_value.add(get_rs.getString("activity"));
        		}
        	}
		} catch (Exception e) {
			// TODO: handle exception
			toast_string = e.getMessage();
    		toast = Toast.makeText(getApplicationContext(), toast_string, duration);
    		toast.show();
		}
	}
	
	public void tvPlay(String id){
		try {
			Connection con = connectionClass.CONN();
			if(con == null){
				toast_string = "Error in connection";
        		toast = Toast.makeText(getApplicationContext(), toast_string, duration);
        		toast.show();
			}else{
				String query = "select tv_id from timeandvolume where tv_id = "+id;
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				int rs_row = 0;
				while(rs.next()){
					rs_row++;
				}
				if(rs_row > 0){
					String select_query = "select datediff(day,start_date,getdate()) as diffday,"+
							 "datediff(second,start_date,getdate()) as diffmin,"+ 
							 "getdate() as end_date, datediff(second,start_date,getdate())/volume as cycle_time, * "+
							 "from timeandvolume where tv_id = "+Integer.parseInt(id);
					Statement query_stmt = con.createStatement();
					ResultSet query_rs = query_stmt.executeQuery(select_query);
					int query_rs_row = 0;
					while(query_rs.next()){
						query_rs_row++;
					}
					if(query_rs_row > 0){
						ResultSet query_rs1 = query_stmt.executeQuery(select_query);
						if(query_rs1.next()){
							String check_query = "select tv_id from timeandvolume where " +
			        				"(employee_number = "+query_rs1.getString("employee_number")+
			        				" AND core_process = '"+query_rs1.getString("core_process")+
			        				"' AND product = '"+query_rs1.getString("product")+
			        				"' AND sub_process = '"+query_rs1.getString("sub_process")+
			        				"' AND activity = '"+query_rs1.getString("activity")+"' AND (status = 'running'))" +
			        						"OR" +
			        				"(employee_number = "+query_rs1.getString("employee_number")+
			        				"  AND status = 'running')";
			        		Statement check_stmt = con.createStatement();
			        		ResultSet check_rs = check_stmt.executeQuery(check_query);
			        		int check_rs_row = 0;
			        		while(check_rs.next()){
			        			check_rs_row++;
			        		}
							
							String update_query = "update timeandvolume set end_date = '"+query_rs1.getString("end_date")+
									"', elapsed_time = "+Double.parseDouble(query_rs1.getString("diffmin"))/60+
									", cycle_time = "+Double.parseDouble(query_rs1.getString("cycle_time"))/60+
									", status = 'paused' where tv_id = "+Integer.parseInt(id);
							PreparedStatement update_stmt = con.prepareStatement(update_query);
							if(check_rs_row==0){
								update_stmt.executeUpdate();
								toast_string = "Updated";
								toast = Toast.makeText(context, toast_string, duration);
								toast.show();
								String get_query = "select * from timeandvolume where tv_id = "+Integer.parseInt(id);
								Statement get_stmt = con.createStatement();
								ResultSet get_rs = get_stmt.executeQuery(get_query);
								
								if(get_rs.next()){
									String insert_query = "insert into " +
				        					"timeandvolume(employee_number,surname,given_name,core_process,product,sub_process,activity,standard_time,volume,status,work_status)" +
				        					"values("+Integer.parseInt(get_rs.getString("employee_number"))+
				        					",'"+get_rs.getString("surname")+"','"+get_rs.getString("given_name")+
				        					"','"+get_rs.getString("core_process")+"','"+get_rs.getString("product")+
				        					"','"+get_rs.getString("sub_process")+"','"+get_rs.getString("activity")+"',"+Double.parseDouble(get_rs.getString("standard_time"))+
				        					","+Integer.parseInt(get_rs.getString("volume"))+",'running',"+Integer.parseInt(get_rs.getString("work_status"))+")";
				        			PreparedStatement insert_stmt = con.prepareStatement(insert_query);
				        			insert_stmt.executeUpdate();
				        			toast_string = "Activity has been play";
									toast = Toast.makeText(context, toast_string, duration);
									toast.show();
								}else{
									toast_string = "Unsuccess playing";
									toast = Toast.makeText(context, toast_string, duration);
									toast.show();
								}
							}else{
								toast_string = "You have still running activity";
								toast = Toast.makeText(context, toast_string, duration);
								toast.show();
							}
						}else{
							toast_string = "No row selected Query 3";
							toast = Toast.makeText(context, toast_string, duration);
							toast.show();
						}
					}else{
						toast_string = "No row selected Query 2";
						toast = Toast.makeText(context, toast_string, duration);
						toast.show();
					}
				}else{
					toast_string = "No row selected Query 1";
					toast = Toast.makeText(context, toast_string, duration);
					toast.show();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			toast_string = e.getMessage();
    		toast = Toast.makeText(getApplicationContext(), toast_string, duration);
    		toast.show();
		}
	}
	
	public void tvStop(String id){
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
						 "from timeandvolume where tv_id = "+Integer.parseInt(id);
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
								", status = 'stop' where tv_id = "+Integer.parseInt(id);
						PreparedStatement update_stmt = con.prepareStatement(update_query);
						update_stmt.executeUpdate();
						toast_string = "Activity has been stop";
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

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		myHandler.removeCallbacks(updatePausedTask);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//myHandler.postDelayed(updatePausedTask, 1000); 
		myHandler.removeCallbacks(updatePausedTask);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try {
        	Connection con = connectionClass.CONN();
        	if(con == null){
        		//loading = ProgressDialog.show(context, "Loading", "Connecting", true, true);
        		//myHandler.postDelayed(updatePausedTask, 1000); 
        		myHandler.removeCallbacks(updatePausedTask);
        	}else{
        		//loading = ProgressDialog.show(context, "Loading", "Connecting", true, true);
        		//loading.dismiss();
        		//myHandler.post(updatePausedTask); 
        		//addPausedListItems();
        	}
		} catch (Exception e) {
			// TODO: handle exception
			//loading = ProgressDialog.show(context, "Loading", "Connecting", true, true);
		}
	}
}
