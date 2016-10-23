package com.example.bpm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class TimeandVolume extends Fragment {
	ConnectionClass connectionClass;
	private Spinner product_spinner, subprocess_spinner, activity_spinner, volume_spinner;
	private Button play_btn;
	List<String> product_list,subprocess_list,activity_list,volume_list;
	String user_id,toast_string,department_id,department_name,
	department_code,product_spinner_value,
	subprocess_spinner_value,activity_spinner_value,volume_spinner_value,
	first_name,last_name;
	double standard_time;
	Context context;
	Toast toast;
	int duration,work_status;
	boolean visible = false;
	Handler handler = new Handler();
	public TimeandVolume() {
		// TODO Auto-generated constructor stub
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		connectionClass = new ConnectionClass();
        View rootView = inflater.inflate(R.layout.tv_tracker, container, false);
        
        product_spinner = (Spinner) rootView.findViewById(R.id.tracker_product);
        subprocess_spinner = (Spinner) rootView.findViewById(R.id.tracker_subprocess);
        activity_spinner = (Spinner) rootView.findViewById(R.id.tracker_activity);
        volume_spinner = (Spinner) rootView.findViewById(R.id.tracker_volume);
        context = rootView.getContext();
        duration = Toast.LENGTH_SHORT;
        play_btn = (Button) rootView.findViewById(R.id.tracker_main_play);
        if(visible == true){
        	product_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

    			@Override
    			public void onItemSelected(AdapterView<?> adapter, View view,
    					int position, long id) {
    				// TODO Auto-generated method stub
    				try{
    					Connection con = connectionClass.CONN();
    					if(con == null){
    						Toast.makeText(getActivity(), "Error in connection", Toast.LENGTH_SHORT).show();
    					}else{
    						//handler.post(subprocessSpinner);
    						new addSubProcessSpinner().execute("");
    					}
    					//addItemsOnSubProcessSpinner(adapter.getItemAtPosition(position).toString());
    				}catch(Exception e){
    					
    				}
    			}

    			@Override
    			public void onNothingSelected(AdapterView<?> arg0) {
    				// TODO Auto-generated method stub
    				
    			}
    		});
            
            subprocess_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

    			@Override
    			public void onItemSelected(AdapterView<?> adapter, View view,
    					int position, long arg3) {
    				// TODO Auto-generated method stub
    				try {
    					Connection con = connectionClass.CONN();
    					if(con == null){
    						Toast.makeText(getActivity(), "Error in connection", Toast.LENGTH_SHORT).show();
    					}else{
    						//handler.post(activitySpinner);
    						new addActivitySpinner().execute("");
    					}
    					//addItemsOnActivity(adapter.getItemAtPosition(position).toString());
    				} catch (Exception e) {
    					// TODO: handle exception
    				}
    			}

    			@Override
    			public void onNothingSelected(AdapterView<?> arg0) {
    				// TODO Auto-generated method stub
    				
    			}
            	
    		});
            play_btn.setOnClickListener(new OnClickListener() {
    		
    			@Override
    			public void onClick(View view) {
    				// TODO Auto-generated method stub
    				try {
    					product_spinner_value = product_spinner.getSelectedItem().toString();				
    					if(product_spinner_value == "Select Product"){
    						toast_string = "Product is still empty";
    					}else{
    						subprocess_spinner_value = subprocess_spinner.getSelectedItem().toString();
    						if(subprocess_spinner_value == null || subprocess_spinner_value == "Select Sub Process"){
    							toast_string = "Sub Process is still empty";
    						}else{
    							activity_spinner_value = activity_spinner.getSelectedItem().toString();
    							if(activity_spinner_value == null || activity_spinner_value == "Select Activity"){
    								toast_string = activity_spinner_value;
    							}else{
    								volume_spinner_value = volume_spinner.getSelectedItem().toString();
    								toast_string = "Completed ";
    								//insertTimeAndVolume(product_spinner_value,subprocess_spinner_value,activity_spinner_value,volume_spinner_value);
    								try {
    									Connection con = connectionClass.CONN();
    			    					if(con == null){
    			    						Toast.makeText(getActivity(), "Error in connection", Toast.LENGTH_SHORT).show();
    			    					}else{
    			    						//handler.post(inserTV);
    			    						new insertTV().execute("");
    			    					}
									} catch (Exception e) {
										// TODO: handle exception
									}
    							}
    						}
    					}
    					toast = Toast.makeText(context, toast_string, duration);
    					toast.show();
    				} catch (Exception e) {
    					// TODO: handle exception
    				}
    			}
    		});
            //addItemsOnProductSpinner();
            //addItemsOnVolumeSpinner();
            //handler.post(volumeSpinner);
            //handler.post(productSpinner);
            new addVolumeSpinner().execute("");
            new addproductSpinner().execute("");
        }
        return rootView;
    }
	private class addVolumeSpinner extends AsyncTask<String, Void, String>{
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			volume_list = new ArrayList<String>();
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			for(int i = 1; i < 21;i++){
				volume_list.add(i+"");
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, volume_list);
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			volume_spinner.setAdapter(dataAdapter);
			super.onPostExecute(result);
		}
		
		
	}
	private Runnable volumeSpinner = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			volume_list = new ArrayList<String>();
			for(int i = 1; i < 21;i++){
				volume_list.add(i+"");
			}
			// Create an ArrayAdapter using the string array and a default spinner layout
			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, volume_list);
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			volume_spinner.setAdapter(dataAdapter);
			
		}
	};
	public void addItemsOnVolumeSpinner(){
		volume_list = new ArrayList<String>();
		for(int i = 1; i < 21;i++){
			volume_list.add(i+"");
		}
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, volume_list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		volume_spinner.setAdapter(dataAdapter);
	}
	
	private class addproductSpinner extends AsyncTask<String, Void, String>{
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			product_list = new ArrayList<String>();
			SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref",Context. MODE_PRIVATE); 
	        user_id = pref.getString("IDD", null); 
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			try {
	        	Connection con = connectionClass.CONN();
	        	
	        	if(con == null){
	        		toast_string = "Error in connection";
	        	}else{
	        		int id = Integer.parseInt(user_id);
	        		String user_query = "select department_id from users where user_id = "+id;
	        		Statement user_stmt = con.createStatement();
	        		ResultSet user_rs = user_stmt.executeQuery(user_query);
	        		
	        		if(user_rs.next()){
	        			department_id = user_rs.getString("department_id");
	        		 }
	        		
	        		String department_query = "select department_code from departments where department_id = "+department_id;
	        		Statement department_stmt = con.createStatement();
	        		ResultSet department_rs = department_stmt.executeQuery(department_query);
	        		
	        		if(department_rs.next()){
	        			department_code = department_rs.getString("department_code");
	        		}
	        		
	        		String taxonomy_query = "select distinct product from taxonomy where core_process = '"+department_code+"'";
	        		Statement taxonomy_stmt = con.createStatement();
	        		ResultSet taxonomy_rs = taxonomy_stmt.executeQuery(taxonomy_query);
	        		
	        		product_list.add("Select Product");
	        		while(taxonomy_rs.next()){
	        			product_list.add(taxonomy_rs.getString("product"));
	        		}
	        		
	        	}
			} catch (Exception e) {
				// TODO: handle exception
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, product_list);
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			product_spinner.setAdapter(dataAdapter);
			super.onPostExecute(result);
		}
		
	}
	
	private Runnable productSpinner = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			product_list = new ArrayList<String>();
			SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref",Context. MODE_PRIVATE); 
	        user_id = pref.getString("IDD", null); 
	        int id = Integer.parseInt(user_id);
	        try {
	        	Connection con = connectionClass.CONN();
	        	
	        	if(con == null){
	        		toast_string = "Error in connection";
	        	}else{
	        		String user_query = "select department_id from users where user_id = "+id;
	        		Statement user_stmt = con.createStatement();
	        		ResultSet user_rs = user_stmt.executeQuery(user_query);
	        		
	        		if(user_rs.next()){
	        			department_id = user_rs.getString("department_id");
	        		 }
	        		
	        		String department_query = "select department_code from departments where department_id = "+department_id;
	        		Statement department_stmt = con.createStatement();
	        		ResultSet department_rs = department_stmt.executeQuery(department_query);
	        		
	        		if(department_rs.next()){
	        			department_code = department_rs.getString("department_code");
	        		}
	        		
	        		String taxonomy_query = "select distinct product from taxonomy where core_process = '"+department_code+"'";
	        		Statement taxonomy_stmt = con.createStatement();
	        		ResultSet taxonomy_rs = taxonomy_stmt.executeQuery(taxonomy_query);
	        		
	        		product_list.add("Select Product");
	        		while(taxonomy_rs.next()){
	        			product_list.add(taxonomy_rs.getString("product"));
	        		}
	        		
	        	}
			} catch (Exception e) {
				// TODO: handle exception
			}
	        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, product_list);
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			product_spinner.setAdapter(dataAdapter);
		}
	};
	public void addItemsOnProductSpinner(){
		product_list = new ArrayList<String>();
		getItemsOnProductSpinner();
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, product_list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		product_spinner.setAdapter(dataAdapter);
	}
	
	
	
	public void getItemsOnProductSpinner(){
		SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref",Context. MODE_PRIVATE); 
        user_id = pref.getString("IDD", null); 
        int id = Integer.parseInt(user_id);
        try {
        	Connection con = connectionClass.CONN();
        	
        	if(con == null){
        		toast_string = "Error in connection with SQL server";
        	}else{
        		String user_query = "select department_id from users where user_id = "+id;
        		Statement user_stmt = con.createStatement();
        		ResultSet user_rs = user_stmt.executeQuery(user_query);
        		
        		if(user_rs.next()){
        			department_id = user_rs.getString("department_id");
        		 }
        		
        		String department_query = "select department_code from departments where department_id = "+department_id;
        		Statement department_stmt = con.createStatement();
        		ResultSet department_rs = department_stmt.executeQuery(department_query);
        		
        		if(department_rs.next()){
        			department_code = department_rs.getString("department_code");
        		}
        		
        		String taxonomy_query = "select distinct product from taxonomy where core_process = '"+department_code+"'";
        		Statement taxonomy_stmt = con.createStatement();
        		ResultSet taxonomy_rs = taxonomy_stmt.executeQuery(taxonomy_query);
        		
        		product_list.add("Select Product");
        		while(taxonomy_rs.next()){
        			product_list.add(taxonomy_rs.getString("product"));
        		}
        		
        	}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	private class addSubProcessSpinner extends AsyncTask<String, Void, String>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			subprocess_list = new ArrayList<String>();
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			try {
	        	Connection con = connectionClass.CONN();
	        	
	        	if(con == null){
	        		toast_string = "Error in connection";
	        	}else{
	        		if(product_spinner_value == "Select Product"){
	        			activity_list.clear();
	        			subprocess_list.clear();
	        			addItemsOnActivity("Select Sub Process");
	        			addItemsOnSubProcessSpinner("Select Product");
	        		}else{
	        			String taxonomy_query = "select distinct sub_process from taxonomy where product = '"+product_spinner_value+"'";
	            		Statement taxonomy_stmt = con.createStatement();
	            		ResultSet taxonomy_rs = taxonomy_stmt.executeQuery(taxonomy_query);
	            		
	            		subprocess_list.add("Select Sub Process");
	            		while(taxonomy_rs.next()){
	            			subprocess_list.add(taxonomy_rs.getString("sub_process"));
	            		}
	        		}
	        		
	        	}
			} catch (Exception e) {
				// TODO: handle exception
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, subprocess_list);
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			subprocess_spinner.setAdapter(dataAdapter);
			super.onPostExecute(result);
		}
		
	}
	
	private Runnable subprocessSpinner = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			subprocess_list = new ArrayList<String>();
			try {
	        	Connection con = connectionClass.CONN();
	        	
	        	if(con == null){
	        		toast_string = "Error in connection";
	        	}else{
	        		if(product_spinner_value == "Select Product"){
	        			activity_list.clear();
	        			subprocess_list.clear();
	        			addItemsOnActivity("Select Sub Process");
	        			addItemsOnSubProcessSpinner("Select Product");
	        		}else{
	        			String taxonomy_query = "select distinct sub_process from taxonomy where product = '"+product_spinner_value+"'";
	            		Statement taxonomy_stmt = con.createStatement();
	            		ResultSet taxonomy_rs = taxonomy_stmt.executeQuery(taxonomy_query);
	            		
	            		subprocess_list.add("Select Sub Process");
	            		while(taxonomy_rs.next()){
	            			subprocess_list.add(taxonomy_rs.getString("sub_process"));
	            		}
	        		}
	        		
	        	}
			} catch (Exception e) {
				// TODO: handle exception
			}
			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, subprocess_list);
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			subprocess_spinner.setAdapter(dataAdapter);
		}
	};
	
	public void addItemsOnSubProcessSpinner(String product){
		subprocess_list = new ArrayList<String>();
		getItemsOnSubProcessSpinner(product);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, subprocess_list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		subprocess_spinner.setAdapter(dataAdapter);
	}
	
	public void getItemsOnSubProcessSpinner(String product){
        try {
        	Connection con = connectionClass.CONN();
        	
        	if(con == null){
        		toast_string = "Error in connection with SQL server";
        	}else{
        		if(product == "Select Product"){
        			activity_list.clear();
        			subprocess_list.clear();
        			addItemsOnActivity("Select Sub Process");
        			addItemsOnSubProcessSpinner("Select Product");
        		}else{
        			String taxonomy_query = "select distinct sub_process from taxonomy where product = '"+product+"'";
            		Statement taxonomy_stmt = con.createStatement();
            		ResultSet taxonomy_rs = taxonomy_stmt.executeQuery(taxonomy_query);
            		
            		subprocess_list.add("Select Sub Process");
            		while(taxonomy_rs.next()){
            			subprocess_list.add(taxonomy_rs.getString("sub_process"));
            		}
        		}
        		
        	}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	private class addActivitySpinner extends AsyncTask<String, Void, String>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			activity_list = new ArrayList<String>();
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			try {
	        	Connection con = connectionClass.CONN();
	        	
	        	if(con == null){
	        		toast_string = "Error in connection with SQL server";
	        	}else{
	        		if(subprocess_spinner_value == "Select Sub Process" || subprocess_spinner_value == null){
	        			activity_list.clear();
	        		}else{
	        			String taxonomy_query = "select distinct activity from taxonomy where sub_process = '"+subprocess_spinner_value+"'";
	            		Statement taxonomy_stmt = con.createStatement();
	            		ResultSet taxonomy_rs = taxonomy_stmt.executeQuery(taxonomy_query);
	            		
	            		activity_list.add("Select Activity");
	            		while(taxonomy_rs.next()){
	            			activity_list.add(taxonomy_rs.getString("activity"));
	            		}
	        		}
	        		
	        	}
			} catch (Exception e) {
				// TODO: handle exception
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, activity_list);
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			activity_spinner.setAdapter(dataAdapter);
		}
	}
	
	private Runnable activitySpinner = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			activity_list = new ArrayList<String>();
			try {
	        	Connection con = connectionClass.CONN();
	        	
	        	if(con == null){
	        		toast_string = "Error in connection with SQL server";
	        	}else{
	        		if(subprocess_spinner_value == "Select Sub Process" || subprocess_spinner_value == null){
	        			activity_list.clear();
	        		}else{
	        			String taxonomy_query = "select distinct activity from taxonomy where sub_process = '"+subprocess_spinner_value+"'";
	            		Statement taxonomy_stmt = con.createStatement();
	            		ResultSet taxonomy_rs = taxonomy_stmt.executeQuery(taxonomy_query);
	            		
	            		activity_list.add("Select Activity");
	            		while(taxonomy_rs.next()){
	            			activity_list.add(taxonomy_rs.getString("activity"));
	            		}
	        		}
	        		
	        	}
			} catch (Exception e) {
				// TODO: handle exception
			}
			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, activity_list);
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			activity_spinner.setAdapter(dataAdapter);
		}
	};
	
	public void addItemsOnActivity(String subprocess){
		activity_list = new ArrayList<String>();
		getItemsOnActivitySpinner(subprocess);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, activity_list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		activity_spinner.setAdapter(dataAdapter);
	}
	
	public void getItemsOnActivitySpinner(String subprocess) {
		try {
        	Connection con = connectionClass.CONN();
        	
        	if(con == null){
        		toast_string = "Error in connection with SQL server";
        	}else{
        		if(subprocess == "Select Sub Process" || subprocess == null){
        			activity_list.clear();
        		}else{
        			String taxonomy_query = "select distinct activity from taxonomy where sub_process = '"+subprocess+"'";
            		Statement taxonomy_stmt = con.createStatement();
            		ResultSet taxonomy_rs = taxonomy_stmt.executeQuery(taxonomy_query);
            		
            		activity_list.add("Select Activity");
            		while(taxonomy_rs.next()){
            			activity_list.add(taxonomy_rs.getString("activity"));
            		}
        		}
        		
        	}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	private class insertTV extends  AsyncTask<String, Void, String>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref",Context. MODE_PRIVATE); 
	        user_id = pref.getString("IDD", null);
	  
		}
		
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			try {
	        	Connection con = connectionClass.CONN();
	        	if(con == null){
	        		toast_string = "Error in connection with SQL server";
	        	}else{
	        		int id = Integer.parseInt(user_id);
	        		String user_query = "select user_fname,user_lname,department_id from users where user_id = "+id;
	        		Statement user_stmt = con.createStatement();
	        		ResultSet user_rs = user_stmt.executeQuery(user_query);
	        		
	        		if(user_rs.next()){
	        			first_name = user_rs.getString("user_fname");
	        			last_name = user_rs.getString("user_lname");
	        			department_id = user_rs.getString("department_id");
	        		 }
	        		
	        		String department_query = "select department_code from departments where department_id = "+department_id;
	        		Statement department_stmt = con.createStatement();
	        		ResultSet department_rs = department_stmt.executeQuery(department_query);
	        		
	        		if(department_rs.next()){
	        			department_code = department_rs.getString("department_code");
	        		}
	        		
	        		String standardtime_query = "select standard_time from standard_time where department_code = '"+department_code+"'";
	        		Statement standardtime_stmt = con.createStatement();
	        		ResultSet standardtime_rs = standardtime_stmt.executeQuery(standardtime_query);
	        		
	        		if(standardtime_rs.next()){
	        			if(Integer.parseInt(standardtime_rs.getString(5)) == 0){
	        				standard_time = 0.00;
	        			}else{
	        				standard_time = Double.parseDouble(standardtime_rs.getString("standard_time"));
	        			}
	        		}
	        		
	        		String taxonomy_query = "select status from taxonomy where " +
	        				"core_process = '"+department_code+"' AND product = '"+product_spinner_value+"'" +
	        				"AND sub_process = '"+subprocess_spinner_value+"' AND activity = '"+activity_spinner_value+"'";
	        		Statement taxonomy_stmt = con.createStatement();
	        		ResultSet taxonomy_rs = taxonomy_stmt.executeQuery(taxonomy_query);
	        		
	        		if(taxonomy_rs.next()){
	        			work_status = Integer.parseInt(taxonomy_rs.getString("status"));
	        		}
	        		
	        		String check_query = "select tv_id from timeandvolume where " +
	        				"(employee_number = "+id+
	        				" AND core_process = '"+department_code+
	        				"' AND product = '"+product_spinner_value+
	        				"' AND sub_process = '"+subprocess_spinner_value+
	        				"' AND activity = '"+activity_spinner_value+"' AND (status = 'running' OR status = 'pause'))" +
	        						"OR" +
	        				"(employee_number = "+id+
	        				"  AND status = 'running')";
	        		Statement check_stmt = con.createStatement();
	        		ResultSet check_rs = check_stmt.executeQuery(check_query);
	        		
	        		int rs_row = 0;
					while(check_rs.next()){
						rs_row++;
					}
					
	        		if(rs_row > 0){
	        			toast_string = "You have still have running activities or paused activity like this";
	        			toast = Toast.makeText(getActivity(), toast_string, duration);
	        			toast.show();
	        		}else{
	        			String insert_query = "insert into " +
	        					"timeandvolume(employee_number,surname,given_name,core_process,product,sub_process,activity,standard_time,volume,status,work_status)" +
	        					"values("+id+",'"+last_name+"','"+first_name+"','"+
	        					department_code+"','"+product_spinner_value+"','"+subprocess_spinner_value+"','"+activity_spinner_value+"',"+standard_time+","+Integer.parseInt(volume_spinner_value)+",'running',"+work_status+")";
	        			PreparedStatement insert_stmt = con.prepareStatement(insert_query);
	        			insert_stmt.executeUpdate();
	        			toast_string = "Successfully Played";
	        			toast = Toast.makeText(getActivity(), toast_string, duration);
	        			toast.show();
	        		}
	        	}
			} catch (Exception e) {
				// TODO: handle exception
				toast_string = e.getMessage();
	            Toast ttt = Toast.makeText(context, toast_string, duration);
	            ttt.show();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}
		
	}
	
	private Runnable inserTV = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref",Context. MODE_PRIVATE); 
	        user_id = pref.getString("IDD", null);
	        int id = Integer.parseInt(user_id);
			try {
	        	Connection con = connectionClass.CONN();
	        	if(con == null){
	        		toast_string = "Error in connection with SQL server";
	        	}else{
	        		String user_query = "select user_fname,user_lname,department_id from users where user_id = "+id;
	        		Statement user_stmt = con.createStatement();
	        		ResultSet user_rs = user_stmt.executeQuery(user_query);
	        		
	        		if(user_rs.next()){
	        			first_name = user_rs.getString("user_fname");
	        			last_name = user_rs.getString("user_lname");
	        			department_id = user_rs.getString("department_id");
	        		 }
	        		
	        		String department_query = "select department_code from departments where department_id = "+department_id;
	        		Statement department_stmt = con.createStatement();
	        		ResultSet department_rs = department_stmt.executeQuery(department_query);
	        		
	        		if(department_rs.next()){
	        			department_code = department_rs.getString("department_code");
	        		}
	        		
	        		String standardtime_query = "select standard_time from standard_time where department_code = '"+department_code+"'";
	        		Statement standardtime_stmt = con.createStatement();
	        		ResultSet standardtime_rs = standardtime_stmt.executeQuery(standardtime_query);
	        		
	        		if(standardtime_rs.next()){
	        			if(Integer.parseInt(standardtime_rs.getString(5)) == 0){
	        				standard_time = 0.00;
	        			}else{
	        				standard_time = Double.parseDouble(standardtime_rs.getString("standard_time"));
	        			}
	        		}
	        		
	        		String taxonomy_query = "select status from taxonomy where " +
	        				"core_process = '"+department_code+"' AND product = '"+product_spinner_value+"'" +
	        				"AND sub_process = '"+subprocess_spinner_value+"' AND activity = '"+activity_spinner_value+"'";
	        		Statement taxonomy_stmt = con.createStatement();
	        		ResultSet taxonomy_rs = taxonomy_stmt.executeQuery(taxonomy_query);
	        		
	        		if(taxonomy_rs.next()){
	        			work_status = Integer.parseInt(taxonomy_rs.getString("status"));
	        		}
	        		
	        		String check_query = "select tv_id from timeandvolume where " +
	        				"(employee_number = "+id+
	        				" AND core_process = '"+department_code+
	        				"' AND product = '"+product_spinner_value+
	        				"' AND sub_process = '"+subprocess_spinner_value+
	        				"' AND activity = '"+activity_spinner_value+"' AND (status = 'running' OR status = 'pause'))" +
	        						"OR" +
	        				"(employee_number = "+id+
	        				"  AND status = 'running')";
	        		Statement check_stmt = con.createStatement();
	        		ResultSet check_rs = check_stmt.executeQuery(check_query);
	        		
	        		int rs_row = 0;
					while(check_rs.next()){
						rs_row++;
					}
					
	        		if(rs_row > 0){
	        			toast_string = "You have still have running activities or paused activity like this";
	        			toast = Toast.makeText(getActivity(), toast_string, duration);
	        			toast.show();
	        		}else{
	        			String insert_query = "insert into " +
	        					"timeandvolume(employee_number,surname,given_name,core_process,product,sub_process,activity,standard_time,volume,status,work_status)" +
	        					"values("+id+",'"+last_name+"','"+first_name+"','"+
	        					department_code+"','"+product_spinner_value+"','"+subprocess_spinner_value+"','"+activity_spinner_value+"',"+standard_time+","+Integer.parseInt(volume_spinner_value)+",'running',"+work_status+")";
	        			PreparedStatement insert_stmt = con.prepareStatement(insert_query);
	        			insert_stmt.executeUpdate();
	        			toast_string = "Successfully Played";
	        			toast = Toast.makeText(getActivity(), toast_string, duration);
	        			toast.show();
	        		}
	        	}
			} catch (Exception e) {
				// TODO: handle exception
				toast_string = e.getMessage();
	            Toast ttt = Toast.makeText(context, toast_string, duration);
	            ttt.show();
			}
		}
	};
	
	public void insertTimeAndVolume(String x, String y, String z, String volume) {
		SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref",Context. MODE_PRIVATE); 
        user_id = pref.getString("IDD", null);
        int id = Integer.parseInt(user_id);
		try {
        	Connection con = connectionClass.CONN();
        	if(con == null){
        		toast_string = "Error in connection with SQL server";
        	}else{
        		String user_query = "select user_fname,user_lname,department_id from users where user_id = "+id;
        		Statement user_stmt = con.createStatement();
        		ResultSet user_rs = user_stmt.executeQuery(user_query);
        		
        		if(user_rs.next()){
        			first_name = user_rs.getString("user_fname");
        			last_name = user_rs.getString("user_lname");
        			department_id = user_rs.getString("department_id");
        		 }
        		
        		String department_query = "select department_code from departments where department_id = "+department_id;
        		Statement department_stmt = con.createStatement();
        		ResultSet department_rs = department_stmt.executeQuery(department_query);
        		
        		if(department_rs.next()){
        			department_code = department_rs.getString("department_code");
        		}
        		
        		String standardtime_query = "select standard_time from standard_time where department_code = '"+department_code+"'";
        		Statement standardtime_stmt = con.createStatement();
        		ResultSet standardtime_rs = standardtime_stmt.executeQuery(standardtime_query);
        		
        		if(standardtime_rs.next()){
        			if(Integer.parseInt(standardtime_rs.getString(5)) == 0){
        				standard_time = 0.00;
        			}else{
        				standard_time = Double.parseDouble(standardtime_rs.getString("standard_time"));
        			}
        		}
        		
        		String taxonomy_query = "select status from taxonomy where " +
        				"core_process = '"+department_code+"' AND product = '"+x+"'" +
        				"AND sub_process = '"+y+"' AND activity = '"+z+"'";
        		Statement taxonomy_stmt = con.createStatement();
        		ResultSet taxonomy_rs = taxonomy_stmt.executeQuery(taxonomy_query);
        		
        		if(taxonomy_rs.next()){
        			work_status = Integer.parseInt(taxonomy_rs.getString("status"));
        		}
        		
        		String check_query = "select tv_id from timeandvolume where " +
        				"(employee_number = "+id+
        				" AND core_process = '"+department_code+
        				"' AND product = '"+x+
        				"' AND sub_process = '"+y+
        				"' AND activity = '"+z+"' AND (status = 'running' OR status = 'pause'))" +
        						"OR" +
        				"(employee_number = "+id+
        				"  AND status = 'running')";
        		Statement check_stmt = con.createStatement();
        		ResultSet check_rs = check_stmt.executeQuery(check_query);
        		
        		int rs_row = 0;
				while(check_rs.next()){
					rs_row++;
				}
				
        		if(rs_row > 0){
        			toast_string = "You have still have running activities or paused activity like this";
        			toast = Toast.makeText(getActivity(), toast_string, duration);
        			toast.show();
        		}else{
        			String insert_query = "insert into " +
        					"timeandvolume(employee_number,surname,given_name,core_process,product,sub_process,activity,standard_time,volume,status,work_status)" +
        					"values("+id+",'"+last_name+"','"+first_name+"','"+department_code+"','"+x+"','"+y+"','"+z+"',"+standard_time+","+Integer.parseInt(volume)+",'running',"+work_status+")";
        			PreparedStatement insert_stmt = con.prepareStatement(insert_query);
        			insert_stmt.executeUpdate();
        			toast_string = "Successfully Played";
        			toast = Toast.makeText(getActivity(), toast_string, duration);
        			toast.show();
        		}
        		//toast_string = check_rs.getRow()+"";
                //Toast ttt = Toast.makeText(context, toast_string, duration);
                //ttt.show();
        		
        		
        		
        	}
		} catch (Exception e) {
			// TODO: handle exception
			toast_string = e.getMessage();
            Toast ttt = Toast.makeText(context, toast_string, duration);
            ttt.show();
		}
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub
		super.setUserVisibleHint(isVisibleToUser);
		if(isVisibleToUser){
			Toast.makeText(getActivity(), "Tracker", Toast.LENGTH_SHORT).show();
			visible = true;
		}else{
			visible = false;
		}
	}
	
	
	
}
