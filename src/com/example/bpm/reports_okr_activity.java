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

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class reports_okr_activity extends Activity {
	Spinner okr_spinner;
	ArrayList<String> okr_list;
	ConnectionClass connectionClass;
	ProgressDialog loading = null;
	Handler handler = new Handler();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.okr_reports);
		connectionClass = new ConnectionClass();
		okr_spinner = (Spinner) findViewById(R.id.okr_reports_spinner);
		okr_list = new ArrayList<String>();
		okr_list.addAll(Arrays.asList("My OKR","Department OKR","Corporate Objectives"));
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, okr_list);
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
			  	        	Toast.makeText(getApplicationContext(), "Error in Connection", Toast.LENGTH_SHORT).show();
			  	        }else{
			  	        	//getOkrDataSet(adapter.getItemAtPosition(position).toString());
			  	        }
					} catch (Exception e) {
						// TODO: handle exception
						Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}
				
			});
	}
	
	public void getOkrDataSet(String value){
    	try {
  	      Connection con = connectionClass.CONN();
  	        
  	        if(con == null){
  	        	Toast.makeText(getApplicationContext(), "Error in Connection", Toast.LENGTH_SHORT).show();
  	        }else{
  	          	if(value == "My OKR"){
  	          		handler.post(getMyOKR);
  	          	}else if(value == "Department OKR"){
  	          		//okr = stmt.executeQuery(query);
  	          		//loading = ProgressDialog.show(getApplicationContext(), "Please wait", "Processing", true, false);
  	          		//getDepartmentOKR(value);
  	          		//loading.dismiss();
  	          		//TestData();
  	          	}else if(value == "Corporate Objectives"){
  	          		//okr = stmt.executeQuery(query);
  	          		//loading = ProgressDialog.show(getApplicationContext(), "Please wait", "Processing", true, false);
  	          		//getCorporateOKR(value);
  	          		//loading.dismiss();
  	          		//TestData();
  	          	}else{
  	          		
  	          	}
  	        }
    	}catch(Exception e){
    		Toast.makeText(getApplicationContext(), e.getMessage()+"errorhuhu", Toast.LENGTH_SHORT).show();
    	}
	}
	
	private Runnable getMyOKR = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			ArrayList<BarDataSet> dataSets = null;
	    	BarEntry v1e1;
	    	ArrayList<BarEntry> valueSet1 = new ArrayList<BarEntry>();
	    	ArrayList<String> xAxis = new ArrayList<String>();
	    	try {
	    		Connection con = connectionClass.CONN();
	    		if(con == null){
	  	        	Toast.makeText(getApplicationContext(), "Error in Connection", Toast.LENGTH_SHORT).show();
	  	        }else{
	  	        	SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref",Context. MODE_PRIVATE); 
	  	        	Editor editor = pref.edit();
	  	          	int id = Integer.parseInt(pref.getString("IDD", null));
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
	              	BarChart chart = (BarChart) findViewById(R.id.okr_chart);
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
	  	        }
			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
			}
			
		}
	};
}
