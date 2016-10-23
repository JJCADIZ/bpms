package com.example.bpm;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.CursorJoiner.Result;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class OKR_Reports extends Fragment {
	Spinner okr_spinner;
	ArrayList<String> okr_list;
	ConnectionClass connectionClass;
	View rootView;
	ProgressDialog loading;
	boolean visible = false;
	public OKR_Reports() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.okr_reports, container, false);
		connectionClass = new ConnectionClass();
		loading = new ProgressDialog(getActivity());
		okr_spinner = (Spinner) rootView.findViewById(R.id.okr_reports_spinner);
		okr_list = new ArrayList<String>();
		okr_list.addAll(Arrays.asList("My OKR","Department OKR","Corporate Objectives"));
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, okr_list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		okr_spinner.setAdapter(dataAdapter);
			okr_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> adapter, View view,
						int position, long arg3) {
					// TODO Auto-generated method stub
					try {
						Connection con = connectionClass.CONN();
			  	        
			  	        if(con == null){
			  	        	Toast.makeText(getActivity(), "Error in Connection", Toast.LENGTH_SHORT).show();
			  	        }else{
			  	        	if(visible == true){
			  	        		loading = ProgressDialog.show(getActivity(), "Please wait...", "Getting data ...", true);
			  	        		loading.setCancelable(false);
			  	        		loading.show();
			  	        		getOkrDataSet(adapter.getItemAtPosition(position).toString());
			  	        	}else{
			  	        		
			  	        	}
			  	        }
					} catch (Exception e) {
						// TODO: handle exception
						Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}
				
			});
		return rootView;
	}
	
	public void getOkrDataSet(String value){
		ArrayList<BarDataSet> dataSets = null;
    	BarEntry v1e1;
    	ArrayList<BarEntry> valueSet1 = new ArrayList<BarEntry>();
    	ArrayList<String> xAxis = new ArrayList<String>();
    	try {
  	      Connection con = connectionClass.CONN();
  	        
  	        if(con == null){
  	        	Toast.makeText(getActivity(), "Error in Connection", Toast.LENGTH_SHORT).show();
  	        }else{
  	          	int count[];
  	          	if(value == "My OKR"){
  	          		new Handler().post(myOkr);
  	          	}else if(value == "Department OKR"){
  	          		new Handler().post(departmentOkr);
  	          	}else if(value == "Corporate Objectives"){
  	          	new Handler().post(corporateOkr);
  	          	}else{
  	          		
  	          	}
  	        }
    	}catch(Exception e){
    		Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
    	}
	}

	
	private Runnable myOkr = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub	
			ArrayList<BarDataSet> dataSets = null;
	    	BarEntry v1e1;
	    	ArrayList<BarEntry> valueSet1 = new ArrayList<BarEntry>();
	    	ArrayList<String> xAxis = new ArrayList<String>();
	        try {
	        	Connection con = connectionClass.CONN();
	        	SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref",Context. MODE_PRIVATE); 
		        Editor editor = pref.edit();
		        int id = Integer.parseInt(pref.getString("IDD", null));
	        	if(con == null){
	        		loading.dismiss();
	        		Toast.makeText(getActivity(), "Error in connection", Toast.LENGTH_SHORT).show();
	        	}else{
	        		String query = "select emp_obj_id, emp_obj_status from employeeobj join okr on " +
		          			"employeeobj.okr_id = okr.okr_id where okr_year = year(getdate()) and " +
		          			"employeeobj.user_id = "+id;
			        Statement stmt = con.createStatement();
			        ResultSet okr;
			        int count[];
	        		okr = stmt.executeQuery(query);
	          		count = new int[]{0,0,0,0};
	          		while(okr.next()){
	          			if(okr.getInt("emp_obj_status") < 10){
	          				if(okr.getInt("emp_obj_status") == 1){
	          					count[0] += 1;
	          				}else if(okr.getInt("emp_obj_status") == 2){
	          					count[1] += 1;
	  	          			}else if(okr.getInt("emp_obj_status") == 3){
	  	          				count[2] += 1;	
	  	          			}else if(okr.getInt("emp_obj_status") == 0){
	  	          				count[3] += 1;
	  	          			}else{
	  	          				
	  	          			}
	          			}else{
	          				
	          			}
	          		}
	          		for(int i = 0; i < count.length; i++){
            	  	v1e1 = new BarEntry(count[i], i); 
                  	valueSet1.add(v1e1);
	          		}
	          		xAxis = new ArrayList<String>();
	              	xAxis.addAll(Arrays.asList("On Track","At Risk","Delayed","Closed"));
	              	BarChart chart = (BarChart) rootView.findViewById(R.id.okr_chart);
		            Legend legend = chart.getLegend();
		            legend.setWordWrapEnabled(true);
	              	BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Legend");
	              	barDataSet1.setColors(ColorTemplate.COLORFUL_COLORS);
	              	dataSets = new ArrayList<BarDataSet>();
	              	dataSets.add(barDataSet1);
				    BarData data = new BarData(xAxis, dataSets);
				    chart.setData(data);
				    chart.setDescription("My OKR");
				    chart.animateXY(2000, 2000);
				    chart.invalidate();
				    loading.dismiss();
				}
			} catch (Exception e) {
				// TODO: handle exception
				loading.dismiss();
				Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	private Runnable departmentOkr = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			ArrayList<BarDataSet> dataSets = null;
	    	BarEntry v1e1,v2e2,v3e3,v4e4;
	    	ArrayList<BarEntry> valueSet1 = new ArrayList<BarEntry>();
	    	ArrayList<BarEntry> valueSet2 = new ArrayList<BarEntry>();
	    	ArrayList<BarEntry> valueSet3 = new ArrayList<BarEntry>();
	    	ArrayList<BarEntry> valueSet4 = new ArrayList<BarEntry>();
	    	ArrayList<String> xAxis = new ArrayList<String>();
	    	try {
	    		Connection con = connectionClass.CONN();
	        	if(con == null){
	        		loading.dismiss();
	    	        Toast.makeText(getActivity(), "Error in Connection", Toast.LENGTH_SHORT).show();
	    	        //TestData();
	    	    }else{
	    	    	SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref",Context. MODE_PRIVATE); 
	  	          	int id = Integer.parseInt(pref.getString("IDD", null));
	  	          	Statement stmt = con.createStatement();
	  	          Statement q_stmt = con.createStatement();
	  	          	int dept_id = 0;
	  	          	String get_user_details = "select department_id from users where user_id = "+id;
		           	
		          	ResultSet okr,user_details,rs;
		          	user_details = stmt.executeQuery(get_user_details);
		          	while(user_details.next()){
		          		dept_id = user_details.getInt("department_id");
		          	}
		          	String query = "select * from users where department_id = "+dept_id+" and " +
		          			"user_type != 1 and user_type != 0";
		          	int ctr = 0, on_track = 0, risk = 0,delayed = 0, closed = 0;
		          	okr = q_stmt.executeQuery(query);
		          	xAxis = new ArrayList<String>();
		          	ArrayList<Integer> on_track_list,risk_list,delayed_list,closed_list;
		          	on_track_list = new ArrayList<Integer>();
		          	risk_list = new ArrayList<Integer>();
		          	delayed_list = new ArrayList<Integer>();
		          	closed_list = new ArrayList<Integer>();
		          	while(okr.next()){
		          		xAxis.add(okr.getString("user_fname")+" "+okr.getString("user_lname"));
		          		on_track = 0;
	        			risk = 0;
	        			delayed = 0;
	        			closed 	= 0;
	        			on_track_list.add(0);
		          		risk_list.add(0);
		          		delayed_list.add(0);
		          		closed_list.add(0);
		          		if(okr.getInt("user_type")==3){
		          			String employee_query = "select emp_obj_id, emp_obj_status from employeeobj join okr on " +
		      	          			"employeeobj.okr_id = okr.okr_id where okr_year = year(getdate()) "+
		          					"and employeeobj.user_id = "+okr.getInt("user_id");
		          			Statement e_stmt = con.createStatement();
		          			ResultSet e_rs = e_stmt.executeQuery(employee_query);
		          			while(e_rs.next()){
		  	          			if(e_rs.getInt("emp_obj_status") < 10){
		  	          				if(e_rs.getInt("emp_obj_status") == 1){
		  	          					on_track += 1;
		  	          				}else if(e_rs.getInt("emp_obj_status") == 2){
		  	          					risk += 1;
		  	  	          			}else if(e_rs.getInt("emp_obj_status") == 3){
		  	  	          				delayed += 1;
		  	  	          			}else if(e_rs.getInt("emp_obj_status") == 0){
		  	  	          				closed += 1;
		  	  	          			}else{
		  	  	          				
		  	  	          			}
		  	          			}else{
		  	          				
		  	          			}
		  	          		}
		          		}else if(okr.getInt("user_type")==2){
		          			String director_query = "select dir_obj_id, dir_obj_status from directorobj a join okr b on " +
		      	          			"a.okr_id = b.okr_id where okr_year = YEAR(getdate()) " +
		      	          			"and a.user_id = "+okr.getInt("user_id");
		          			Statement d_stmt = con.createStatement();
		          			ResultSet d_rs = d_stmt.executeQuery(director_query);
		          			while(d_rs.next()){
		  	          			if(d_rs.getInt("dir_obj_status") < 10){
		  	          				if(d_rs.getInt("dir_obj_status") == 1){
		  	          					on_track += 1;
		  	          				}else if(d_rs.getInt("dir_obj_status") == 2){
		  	          					risk += 1;
		  	  	          			}else if(d_rs.getInt("dir_obj_status") == 3){
		  	  	          				delayed += 1;
		  	  	          			}else if(d_rs.getInt("dir_obj_status") == 0){
		  	  	          				closed += 1;
		  	  	          			}else{
		  	  	          				
		  	  	          			}
		  	          			}else{

		  	          			}
		  	          		}
		          		}else{
		          			
		          		}
		          		/*on_track_list.add(on_track);
		          		risk_list.add(risk);
		          		delayed_list.add(delayed);
		          		closed_list.add(closed);*/
		          		on_track_list.set(ctr, on_track);
		          		risk_list.set(ctr, risk);
		          		delayed_list.set(ctr, delayed);
		          		closed_list.set(ctr, closed);
		          		ctr++;
		          	}
		          	for(int i = 0; i < on_track_list.size(); i++){
		          		//v1e1 = new BarEntry(on_track[i], i); 
		          		v1e1 = new BarEntry(on_track_list.get(i).intValue(), i);
	                  	valueSet1.add(v1e1);
		          	}
		          	for(int i = 0; i < risk_list.size(); i++){
		          		//v2e2 = new BarEntry(risk[i], i); 
		          		v2e2 = new BarEntry(risk_list.get(i).intValue(), i); 
	                  	valueSet2.add(v2e2);
		          	}
		          	for(int i = 0; i < delayed_list.size(); i++){
	                  	//v3e3 = new BarEntry(delayed[i], i); 
		          		v3e3 = new BarEntry(delayed_list.get(i).intValue(), i); 
	                  	valueSet3.add(v3e3);
		          	}
		          	for(int i = 0; i < closed_list.size(); i++){
	                  	//v4e4 = new BarEntry(closed[i], i); 
		          		v4e4 = new BarEntry(closed_list.get(i).intValue(), i); 
	                  	valueSet4.add(v4e4);
		          	}
	            	BarDataSet barDataSet1 = new BarDataSet(valueSet1, "On Track");
	            	barDataSet1.setColor(Color.rgb(255, 0, 255));
	            	
	            	BarDataSet barDataSet2 = new BarDataSet(valueSet2, "At Risk");
	            	barDataSet2.setColor(Color.rgb(218, 112, 214));
	            	
	            	BarDataSet barDataSet3 = new BarDataSet(valueSet3, "Delayed");
	            	barDataSet3.setColor(Color.rgb(74, 43, 146));
	            	
	            	BarDataSet barDataSet4 = new BarDataSet(valueSet4, "Closed");
	            	barDataSet4.setColor(Color.rgb(20, 172, 71));
	            	
	            	dataSets = new ArrayList<BarDataSet>();
	            	dataSets.add(barDataSet1);
	            	dataSets.add(barDataSet2);
	            	dataSets.add(barDataSet3);
	            	dataSets.add(barDataSet4);
				    BarData data = new BarData(xAxis, dataSets);
				    BarChart chart = (BarChart) rootView.findViewById(R.id.okr_chart);
		            Legend legend = chart.getLegend();
		            legend.setWordWrapEnabled(true);
				    chart.setData(data);
				    chart.setDescription("Department OKR");
				    chart.animateXY(2000, 2000);
				    chart.invalidate();
				    loading.dismiss();
	    	    }
			} catch (Exception e) {
				// TODO: handle exception
				loading.dismiss();
				Toast.makeText(getActivity(), e.getMessage()+"error", Toast.LENGTH_SHORT).show();
			}
			
		}
	};
	
	private Runnable corporateOkr = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			ArrayList<BarDataSet> dataSets = null;
	    	BarEntry v1e1,v2e2,v3e3,v4e4;
	    	ArrayList<BarEntry> valueSet1 = new ArrayList<BarEntry>();
	    	ArrayList<BarEntry> valueSet2 = new ArrayList<BarEntry>();
	    	ArrayList<BarEntry> valueSet3 = new ArrayList<BarEntry>();
	    	ArrayList<BarEntry> valueSet4 = new ArrayList<BarEntry>();
	    	ArrayList<String> xAxis = new ArrayList<String>();
	    	try {
	    		Connection con = connectionClass.CONN();
	        	if(con == null){
	        		loading.dismiss();
	    	        Toast.makeText(getActivity(), "Error in Connection", Toast.LENGTH_SHORT).show();
	    	        //TestData();
	    	    }else{
	    	    	
	    	    	SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref",Context. MODE_PRIVATE); 
	  	          	int id = Integer.parseInt(pref.getString("IDD", null));
	  	          	String get_dept_details = "select department_id,department_code from departments where department_status = 1 ";
	  	          	Statement stmt = con.createStatement();
	  	          	Statement stmt1 = con.createStatement();
	  	          	ResultSet rs,users;
	  	          	rs = stmt.executeQuery(get_dept_details);
	  	          	xAxis = new ArrayList<String>();
	  	          	ArrayList<Integer> dept_id = new ArrayList<Integer>();
	  	          	ArrayList<Integer> on_track_list,risk_list,delayed_list,closed_list;
		          	on_track_list = new ArrayList<Integer>();
		          	risk_list = new ArrayList<Integer>();
		          	delayed_list = new ArrayList<Integer>();
		          	closed_list = new ArrayList<Integer>();
		          	int ctr = 0, on_track = 0, risk = 0,delayed = 0, closed = 0;
	  	          	while(rs.next()){
	  	          		xAxis.add(rs.getString("department_code"));
	  	          		String user_query = "select user_type,department_id,user_id from users where department_id = "+rs.getInt("department_id");
	  	          		users = stmt1.executeQuery(user_query);
	  	          		on_track = 0;
	    				risk = 0;
	    				delayed = 0;
	    				closed 	= 0;
	    				on_track_list.add(0);
	          			risk_list.add(0);
	          			delayed_list.add(0);
	          			closed_list.add(0);
	  	          		while(users.next()){
	  	          			if(users.getInt("user_type")==3){
			          			if(users.getInt("department_id") == rs.getInt("department_id")){
			          				String employee_query = "select emp_obj_id, emp_obj_status from employeeobj join okr on " +
				      	          			"employeeobj.okr_id = okr.okr_id where okr_year = year(getdate()) "+
				          					"and employeeobj.user_id = "+users.getInt("user_id");
				          			Statement e_stmt = con.createStatement();
				          			ResultSet e_rs = e_stmt.executeQuery(employee_query);
				          			while(e_rs.next()){
				          				if(e_rs.getInt("emp_obj_status") < 10){
				  	          				if(e_rs.getInt("emp_obj_status") == 1){
				  	          					on_track += 1;
				  	          				}else if(e_rs.getInt("emp_obj_status") == 2){
				  	          					risk += 1;
				  	  	          			}else if(e_rs.getInt("emp_obj_status") == 3){
				  	  	          				delayed += 1;
				  	  	          			}else if(e_rs.getInt("emp_obj_status") == 0){
				  	  	          				closed += 1;
				  	  	          			}else{
				  	  	          				
				  	  	          			}
				  	          			}else{
				  	          				
				  	          			}
				          			}
			          			}
	  	          			}else if(users.getInt("user_type")==2){
			          			if(users.getInt("department_id") == rs.getInt("department_id")){
			  	          			String director_query = "select dir_obj_id, dir_obj_status from directorobj a join okr b on " +
				      	          			"a.okr_id = b.okr_id where okr_year = YEAR(getdate()) " +
				      	          			"and a.user_id = "+users.getInt("user_id");
				          			Statement d_stmt = con.createStatement();
				          			ResultSet d_rs = d_stmt.executeQuery(director_query);
				          			while(d_rs.next()){
				          				if(d_rs.getInt("dir_obj_status") < 10){
				  	          				if(d_rs.getInt("dir_obj_status") == 1){
				  	          					on_track += 1;
				  	          				}else if(d_rs.getInt("dir_obj_status") == 2){
				  	          					risk += 1;
				  	  	          			}else if(d_rs.getInt("dir_obj_status") == 3){
				  	  	          				delayed += 1;
				  	  	          			}else if(d_rs.getInt("dir_obj_status") == 0){
				  	  	          				closed += 1;
				  	  	          			}else{
				  	  	          				
				  	  	          			}
				  	          			}else{

				  	          			}
				          			}
			          			}
	  	          			}else{
	  	          				
	  	          			}
	  	          		}
		  	          	on_track_list.set(ctr, on_track);
		          		risk_list.set(ctr, risk);
		          		delayed_list.set(ctr, delayed);
		          		closed_list.set(ctr, closed);
		          		ctr++;
	  	          	}
	  	          for(int i = 0; i < on_track_list.size(); i++){
		          		//v1e1 = new BarEntry(on_track[i], i); 
		          		v1e1 = new BarEntry(on_track_list.get(i).intValue(), i);
	                	valueSet1.add(v1e1);
		          	}
		          	for(int i = 0; i < risk_list.size(); i++){
		          		//v2e2 = new BarEntry(risk[i], i); 
		          		v2e2 = new BarEntry(risk_list.get(i).intValue(), i); 
	                	valueSet2.add(v2e2);
		          	}
		          	for(int i = 0; i < delayed_list.size(); i++){
	                	//v3e3 = new BarEntry(delayed[i], i); 
		          		v3e3 = new BarEntry(delayed_list.get(i).intValue(), i); 
	                	valueSet3.add(v3e3);
		          	}
		          	for(int i = 0; i < closed_list.size(); i++){
	                	//v4e4 = new BarEntry(closed[i], i); 
		          		v4e4 = new BarEntry(closed_list.get(i).intValue(), i); 
	                	valueSet4.add(v4e4);
		          	}
		          	BarDataSet barDataSet1 = new BarDataSet(valueSet1, "On Track");
		          	barDataSet1.setColor(Color.rgb(255, 0, 255));
		          	
		          	BarDataSet barDataSet2 = new BarDataSet(valueSet2, "At Risk");
		          	barDataSet2.setColor(Color.rgb(218, 112, 214));
		          	
		          	BarDataSet barDataSet3 = new BarDataSet(valueSet3, "Delayed");
		          	barDataSet3.setColor(Color.rgb(74, 43, 146));
		          	
		          	BarDataSet barDataSet4 = new BarDataSet(valueSet4, "Closed");
		          	barDataSet4.setColor(Color.rgb(20, 172, 71));
		          	
		          	dataSets = new ArrayList<BarDataSet>();
		          	dataSets.add(barDataSet1);
		          	dataSets.add(barDataSet2);
		          	dataSets.add(barDataSet3);
		          	dataSets.add(barDataSet4);
				    BarData data = new BarData(xAxis, dataSets);
				    BarChart chart = (BarChart) rootView.findViewById(R.id.okr_chart);
		            Legend legend = chart.getLegend();
		            legend.setWordWrapEnabled(true);
				    chart.setData(data);
				    chart.setDescription("Corporate OKR");
				    chart.animateXY(2000, 2000);
				    chart.invalidate();
				    loading.dismiss();
	    	    }
			} catch (Exception e) {
				// TODO: handle exception
				loading.dismiss();
				Toast.makeText(getActivity(), e.getMessage()+"error", Toast.LENGTH_SHORT).show();
			}
		}
	};

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub
		if(isVisibleToUser){
			visible = true;
		}else{
			visible = false;
		}
		super.setUserVisibleHint(isVisibleToUser);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try {
    		Connection con = connectionClass.CONN();
        	if(con == null){
    	        Toast.makeText(getActivity(), "Error in Connection", Toast.LENGTH_SHORT).show();
    	        //TestData();
    	    }else{
    	    	loading = ProgressDialog.show(getActivity(), "Please wait...", "Getting data ...", true);
    	    	loading.setCancelable(false);
	        	loading.show();
	        	new Handler().post(myOkr);
    	    }
		}catch(Exception e){
			
		}
	}
	
	
}
