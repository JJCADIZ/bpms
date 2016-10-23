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

public class PatfinderReports extends Fragment {
	ConnectionClass connectionClass;
	Spinner pf_spinner;
	ArrayList<String> pf_list;
	BarChart barchart; 
	BarData bardata;
	View rootView;
	int department_id;
	boolean visible = false;
	public PatfinderReports() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		connectionClass = new ConnectionClass();
		rootView = inflater.inflate(R.layout.patfinder_reports, container, false);
		barchart = (BarChart) rootView.findViewById(R.id.pathfinder_chart);
		pf_spinner = (Spinner) rootView.findViewById(R.id.pathfiner_reports_spinner);
		pf_list = new ArrayList<String>();
		pf_list.addAll(Arrays.asList("My Ideas","All Ideas"));
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, pf_list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		pf_spinner.setAdapter(dataAdapter);
			pf_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> adapter, View view,
						int position, long arg3) {
					// TODO Auto-generated method stub
					try {
						if(visible == true){
							getPathfinderDataSet(adapter.getItemAtPosition(position).toString());
						}else{
							
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
	
	private Runnable myIdeas = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
	    		Connection con = connectionClass.CONN();
	    		ArrayList<BarDataSet> dataSets = null;
	    		BarEntry v1e1;
	    		ArrayList<BarEntry> valueSet1 = new ArrayList<BarEntry>();
	    		ArrayList<String> xAxis = new ArrayList<String>();
	    		if(con == null){
	    			Toast.makeText(getActivity(), "Error in connection", Toast.LENGTH_SHORT).show();
	    		}else{
	    			SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref",Context. MODE_PRIVATE);
	  	          	int id = Integer.parseInt(pref.getString("IDD", null));
	  	          	String get_pathfinder_data = "select * from pathfinder join pathfinderBenefits on " +
	  	          			"pathfinderBenefits.benefit_id = pathfinder.benefit_id join pathfinderIdea on " +
	  	          			"pathfinderIdea.idea_id = pathfinder.idea_id join users on " +
	  	          			"pathfinder.user_id = users.user_id";
	  	          	String get_department = "select department_id from users where user_id = "+ id;
	  	          	Statement dept_stmt = con.createStatement();
	  	          	Statement stmt = con.createStatement();
	  	          	ResultSet ideas,department;
	  	          	int count[];
	  	          	department = dept_stmt.executeQuery(get_department);
	  	          	while(department.next()){
	  	          		department_id = department.getInt("department_id");
	  	          	}
	  	          ideas = stmt.executeQuery(get_pathfinder_data);
	          		count = new int[]{0,0,0,0,0,0,0};
	          		while(ideas.next()){
	          			if(ideas.getInt("pathfinder_status") <= 10){
	          				if(ideas.getInt("user_id") == id){
	          					if(ideas.getInt("pathfinder_status") == 1){
	          						count[0] += 1;
	          					}else if(ideas.getInt("pathfinder_status") == 2){
	          						count[1] += 1;	
	  	          				}else if(ideas.getInt("pathfinder_status") == 3){
	  	          					count[2] += 1;
	  	          				}else if(ideas.getInt("pathfinder_status") == 0){
	  	          					count[3] += 1;
	  	          				}else if(ideas.getInt("pathfinder_status") == 9){
	  	          					count[4] += 1;		
	  	          				}else if(ideas.getInt("pathfinder_status") == 8){
	  	          					count[5] += 1;		
	  	          				}else if(ideas.getInt("pathfinder_status") == 10){
	  	          					count[6] += 1;		
	  	          				}else{
	  	          					
	  	          				}
	          				}
	          			}
	          		}
	          		for(int i = 0; i < count.length; i++){
      				v1e1 = new BarEntry(count[i], i); 
      				valueSet1.add(v1e1);
      			}
      			xAxis = new ArrayList<String>();
      			xAxis.addAll(Arrays.asList("On Track","At Risk","Delayed","Closed","Pending Closure","Declined","Not Started"));
      			BarChart chart = (BarChart) rootView.findViewById(R.id.pathfinder_chart);
	            Legend legend = chart.getLegend();
	            legend.setWordWrapEnabled(true);
              	BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Legend");
              	barDataSet1.setColors(ColorTemplate.COLORFUL_COLORS);
              	dataSets = new ArrayList<BarDataSet>();
              	dataSets.add(barDataSet1);
			    BarData data = new BarData(xAxis, dataSets);
			    chart.setData(data);
			    chart.setDescription("My ideas");
			    chart.animateXY(2000, 2000);
			    chart.invalidate();  	
	    		}
			}catch(Exception e){
				Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
			}
		}
	};
	
private Runnable allIdeas = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
	    		Connection con = connectionClass.CONN();
	    		ArrayList<BarDataSet> dataSets = null;
	    		BarEntry v1e1;
	    		ArrayList<BarEntry> valueSet1 = new ArrayList<BarEntry>();
	    		ArrayList<String> xAxis = new ArrayList<String>();
	    		if(con == null){
	    			Toast.makeText(getActivity(), "Error in connection", Toast.LENGTH_SHORT).show();
	    		}else{
	    			SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref",Context. MODE_PRIVATE);
	  	          	int id = Integer.parseInt(pref.getString("IDD", null));
	  	          	String get_pathfinder_data = "select * from pathfinder join pathfinderBenefits on " +
	  	          			"pathfinderBenefits.benefit_id = pathfinder.benefit_id join pathfinderIdea on " +
	  	          			"pathfinderIdea.idea_id = pathfinder.idea_id join users on " +
	  	          			"pathfinder.user_id = users.user_id";
	  	          	String get_department = "select department_id from users where user_id = "+ id;
	  	          	Statement dept_stmt = con.createStatement();
	  	          	Statement stmt = con.createStatement();
	  	          	ResultSet ideas,department;
	  	          	int count[];
	  	          	department = dept_stmt.executeQuery(get_department);
	  	          	while(department.next()){
	  	          		department_id = department.getInt("department_id");
	  	          	}
	  	          ideas = stmt.executeQuery(get_pathfinder_data);
	          		count = new int[]{0,0,0,0};
	          		while(ideas.next()){
	          			if(ideas.getInt("pathfinder_status") <= 10){
	        				if(ideas.getInt("pathfinder_status") == 1){
	        					count[0] += 1;
	        				}else if(ideas.getInt("pathfinder_status") == 2){
	        					count[1] += 1;	
		          			}else if(ideas.getInt("pathfinder_status") == 3){
		          				count[2] += 1;
		          			}else if(ideas.getInt("pathfinder_status") == 0){
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
      			BarChart chart = (BarChart) rootView.findViewById(R.id.pathfinder_chart);
	            Legend legend = chart.getLegend();
	            legend.setWordWrapEnabled(true);
              	BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Legend");
              	barDataSet1.setColors(ColorTemplate.COLORFUL_COLORS);
              	dataSets = new ArrayList<BarDataSet>();
              	dataSets.add(barDataSet1);
			    BarData data = new BarData(xAxis, dataSets);
			    chart.setData(data);
			    chart.setDescription("All ideas");
			    chart.animateXY(2000, 2000);
			    chart.invalidate();  	
	    		}
			}catch(Exception e){
				Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	private Runnable pendingIdeas = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
	    		Connection con = connectionClass.CONN();
	    		ArrayList<BarDataSet> dataSets = null;
	    		BarEntry v1e1;
	    		ArrayList<BarEntry> valueSet1 = new ArrayList<BarEntry>();
	    		ArrayList<String> xAxis = new ArrayList<String>();
	    		if(con == null){
	    			Toast.makeText(getActivity(), "Error in connection", Toast.LENGTH_SHORT).show();
	    		}else{
	    			SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref",Context. MODE_PRIVATE);
	  	          	int id = Integer.parseInt(pref.getString("IDD", null));
	  	          	String get_pathfinder_data = "select * from pathfinder join pathfinderBenefits on " +
	  	          			"pathfinderBenefits.benefit_id = pathfinder.benefit_id join pathfinderIdea on " +
	  	          			"pathfinderIdea.idea_id = pathfinder.idea_id join users on " +
	  	          			"pathfinder.user_id = users.user_id";
	  	          	String get_department = "select department_id from users where user_id = "+ id;
	  	          	Statement dept_stmt = con.createStatement();
	  	          	Statement stmt = con.createStatement();
	  	          	ResultSet ideas,department;
	  	          	int count[];
	  	          	department = dept_stmt.executeQuery(get_department);
	  	          	while(department.next()){
	  	          		department_id = department.getInt("department_id");
	  	          	}
	  	          ideas = stmt.executeQuery(get_pathfinder_data);
        			count = new int[]{0,0,0};
        			while(ideas.next()){
        				if(ideas.getInt("department_id") == department_id){
        					if(ideas.getInt("pathfinder_status") >= 10){
        						if(ideas.getInt("pathfinder_status") == 10 || ideas.getInt("pathfinder_status") == 13){
          						count[0] += 1;
          					}else if(ideas.getInt("pathfinder_status") == 12 || ideas.getInt("pathfinder_status") == 14){
          						count[1] += 1;	
  	          				}else if(ideas.getInt("pathfinder_status") == 11){
  	          					count[2] += 1;
  	          				}else{
  	          					
  	          				}
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
      			BarChart chart = (BarChart) rootView.findViewById(R.id.pathfinder_chart);
	            Legend legend = chart.getLegend();
	            legend.setWordWrapEnabled(true);
              	BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Legend");
              	barDataSet1.setColors(ColorTemplate.COLORFUL_COLORS);
              	dataSets = new ArrayList<BarDataSet>();
              	dataSets.add(barDataSet1);
			    BarData data = new BarData(xAxis, dataSets);
			    chart.setData(data);
			    chart.setDescription("Pending ideas");
			    chart.animateXY(2000, 2000);
			    chart.invalidate();  	
	    		}
			}catch(Exception e){
				Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
			}
		}
	};
	protected void getPathfinderDataSet(String value) {
		// TODO Auto-generated method stub
    	try {
    		Connection con = connectionClass.CONN();
    		if(con == null){
    			Toast.makeText(getActivity(), "Error in connection", Toast.LENGTH_SHORT).show();
    		}else{
  	          	if(value == "My Ideas"){
  	          		new Handler().post(myIdeas);
  	          	}else if(value == "All Ideas"){
  	          		new Handler().post(allIdeas);
  	          	}else if(value == "Pending Ideas"){
  	          		//new Handler().post(pendingIdeas);
  	          	}else{
  	          		
  	          	}
  	          	
    		}
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub
		if(isVisibleToUser == true){
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
    			Toast.makeText(getActivity(), "Error in connection", Toast.LENGTH_SHORT).show();
    		}else{
  	          	new Handler().post(myIdeas);
    		}
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}
	
	
}
