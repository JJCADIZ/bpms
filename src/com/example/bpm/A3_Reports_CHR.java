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
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class A3_Reports_CHR extends Fragment {
    
	public A3_Reports_CHR() {
		// TODO Auto-generated constructor stub
	}
	ConnectionClass connectionClass;
	Spinner a3_spinner;
	ArrayList<String> a3_list;
	BarChart barchart; 
	BarData bardata;
	View rootView;
	boolean visible = false;
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
    	connectionClass = new ConnectionClass();
		rootView = inflater.inflate(R.layout.a3_reports, container, false);
		barchart = (BarChart) rootView.findViewById(R.id.a3_chart);
		a3_spinner = (Spinner) rootView.findViewById(R.id.a3_reports_spinner);
		a3_list = new ArrayList<String>();
		a3_list.addAll(Arrays.asList("All Projects","Pending Projects"));
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, a3_list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		a3_spinner.setAdapter(dataAdapter);
			a3_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> adapter, View view,
						int position, long arg3) {
					// TODO Auto-generated method stub
					try {
						Connection con = connectionClass.CONN();
		    	        
		    	        if(con == null){
		    	        	Toast.makeText(getActivity(), "Error in connection", Toast.LENGTH_SHORT).show();
		    	        }else{
		    	        	getProjectDataSet(adapter.getItemAtPosition(position).toString());
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
    
    public void getProjectDataSet(String value){
    	ArrayList<BarDataSet> dataSets = null;
    	BarEntry v1e1;
    	ArrayList<BarEntry> valueSet1 = new ArrayList<BarEntry>();
    	ArrayList<String> xAxis = new ArrayList<String>();
    	try {
    	      Connection con = connectionClass.CONN();
    	        
    	        if(con == null){
    	          
    	        }else{
    	          SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref",Context. MODE_PRIVATE); 
    	          Editor editor = pref.edit();
    	          int id = Integer.parseInt(pref.getString("IDD", null));
    	          //String get_members = "";
    	          String get_members = "SELECT DISTINCT proj_id, members = STUFF(( "+
    	                  	"SELECT N', ' + t.Ful "+
    	                  	"FROM (SELECT a3Team.proj_id, "+
    	                  	"( users.user_fname + ' ' + users.user_lname ) as Ful "+
    	              		"FROM a3Team JOIN users " +
    	              		"ON users.user_id = a3Team.user_id ) t "+
    	              		"WHERE t.proj_id = u.proj_id FOR XML PATH(''), TYPE).value('.', 'NVARCHAR(MAX)'), 1, 1, '') "+
    	              		"FROM (SELECT a3Team.proj_id, ( users.user_fname  + ' ' + users.user_lname ) as Ful "+
    	              		"FROM a3Team JOIN users ON users.user_id = a3Team.user_id ) u";
    	           String get_projects_data = "select * from a3projects join users on " +
    	                "users.user_id = a3projects.proj_lead order by proj_id";
    	           String get_name = "select * from users where user_id = "+id;
    	           Statement stmt = con.createStatement();
    	           Statement mem_stmt = con.createStatement();
    	           ResultSet projects, rs_name,members;
    	           rs_name = stmt.executeQuery(get_name);
    	           String fullname="";
    	           
    	      while(rs_name.next()){
    	        fullname = rs_name.getString("user_fname")+' '+rs_name.getString("user_lname");
    	      }
    	            int count[];
    	          if(value == "All Projects"){
    	              projects = stmt.executeQuery(get_projects_data);
    	              count = new int[]{0,0,0,0};
    	              while(projects.next()){
    	                if(projects.getInt("proj_status") < 10){
    	                  if(projects.getInt("proj_status") == 1){
    	                       count[0] += 1;
    	                    }else if(projects.getInt("proj_status") == 2){
    	                       count[1] += 1;
    	                    }else if(projects.getInt("proj_status") == 3){
    	                       count[2] += 1;
    	                    }else if(projects.getInt("proj_status") == 0){
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
    	            }else if(value == "Pending Projects"){
    	              projects = stmt.executeQuery(get_projects_data);
    	              count = new int[]{0,0,0};
    	              while(projects.next()){
    	                if(projects.getInt("proj_status") >= 10){
    	                  if(projects.getInt("proj_status") == 10 || projects.getInt("proj_status") == 13){
    	                       count[0] += 1;
    	                    }else if(projects.getInt("proj_status") == 12 || projects.getInt("proj_status") == 14){
    	                       count[1] += 1;
    	                    }else if(projects.getInt("proj_status") == 11){
    	                       count[2] += 1;
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
    	              xAxis.addAll(Arrays.asList("Pending","Declined","Not Started"));
    	            }
    	              
    	            BarChart chart = (BarChart) rootView.findViewById(R.id.a3_chart);
    	            Legend legend = chart.getLegend();
    	            legend.setWordWrapEnabled(true);
  	              	BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Legend");
  	              	barDataSet1.setColors(ColorTemplate.COLORFUL_COLORS);
  	              	dataSets = new ArrayList<BarDataSet>();
  	              	dataSets.add(barDataSet1);
				    BarData data = new BarData(xAxis, dataSets);
				    chart.setData(data);
				    chart.setDescription(value);
				    chart.animateXY(2000, 2000);
				    chart.invalidate();
    	        }
    	  } catch (Exception e) {
    	    // TODO: handle exception
    		  Toast.makeText(getActivity(), e.getMessage() + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
    	  }
    }
	
	
	
}
