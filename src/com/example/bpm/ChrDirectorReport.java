package com.example.bpm;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CalendarView.OnDateChangeListener;

public class ChrDirectorReport extends Activity {
	private Button pick_from_date, pick_to_date;
	TextView from_text, to_text;
	private DatePickerDialog fromDatePickerDialog;
	private DatePickerDialog toDatePickerDialog;
	
	static final int DATE_DIALOG_ID = 0;
	private int pYear;
    private int pMonth;
    private int pDay;
	ConnectionClass connectionClass;
	Spinner type_of_report, kind_of_report,employee_spinner;
	Button generate;
	LineChart tv_line_chart;
	ArrayList<String> type_of_report_list,kind_of_report_list,employee_list;
	ArrayList<Integer> employee_id_list;
	ArrayAdapter<String> type_adapter, kind_adapter,employee_adapter;
	int year_start,month_start,day_start;
	int year_end,month_end,day_end;
	CalendarView calendar_start_date,calendar_end_date;
	public String start_date, end_date;
	String kind_value, type_value, toast_string,employee_value;
	Toast toast;
	int id=0,employee_user_id;
	ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
	Entry v1e1;
	ArrayList<Entry> valueSet1 = new ArrayList<Entry>();
	ArrayList<String> xAxis = new ArrayList<String>();
	EditText to,from;
	ScrollView scroll;
	
	public ChrDirectorReport() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.employee_tv_report);
		connectionClass = new ConnectionClass();
		SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref",Context. MODE_PRIVATE); 
        id = Integer.parseInt(pref.getString("IDD", null));
		type_of_report = (Spinner) findViewById(R.id.employee_typeofreport);
		kind_of_report = (Spinner) findViewById(R.id.employee_kindofreport);
		employee_spinner = (Spinner) findViewById(R.id.employee_spinner);
		generate = (Button) findViewById(R.id.employee_tv_generate);
		tv_line_chart = (LineChart) findViewById(R.id.employee_tv_chart);
		scroll = (ScrollView) findViewById(R.id.employee_tv_reports_scroll);
		
		employee_list = new ArrayList<String>();
		employee_id_list = new ArrayList<Integer>();
		setEmployeeList();
		
		type_of_report_list = new ArrayList<String>();
		type_of_report_list.addAll(Arrays.asList("Select Report","Time Log","Volume Date","Loss Time","Availability","Efficiency","Utilization"));
		kind_of_report_list = new ArrayList<String>();
		
		employee_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> adapter, View view,
					int position, long arg3) {
				// TODO Auto-generated method stub
				employee_value = adapter.getItemAtPosition(position).toString();
				employee_user_id = employee_id_list.get(position);
				Toast.makeText(getApplicationContext(), employee_value + " "+employee_user_id, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		type_adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, type_of_report_list);
		type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		type_of_report.setAdapter(type_adapter);
		
		type_of_report.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> adapter, View view,
					int position, long arg3) {
				// TODO Auto-generated method stub
				String sw = adapter.getItemAtPosition(position).toString();
				type_value = adapter.getItemAtPosition(position).toString();
				kind_of_report_list = new ArrayList<String>();
				if(sw == "Select Report"){
					//resetDataSet();
					//kind_of_report_list = new ArrayList<String>();
				}else if(sw == "Time Log"){
					kind_of_report_list.addAll(Arrays.asList("Elapsed Time", "Volume","Cycle Time"));
					//timeLogDataSet();
				}else if(sw == "Volume Date"){
					kind_of_report_list.addAll(Arrays.asList("Standard Cycle Time", "Time@std","Activiy Time","Actual Cycle Time","Volume"));
					//volumeDateDataSet();
				}else if(sw == "Loss Time"){
					kind_of_report_list.addAll(Arrays.asList("Elapsed Time", "Volume","Cycle Time"));
					//lossTimeDataSet();
				}else if(sw == "Availability"){
					kind_of_report_list.addAll(Arrays.asList("Processing Time", "Total Time","Availability"));
					//availabilityDataSet();
				}else if(sw == "Efficiency"){
					kind_of_report_list.addAll(Arrays.asList("Time@std", "Processing Time","Efficiency"));
				}else if(sw == "Utilization"){
					kind_of_report_list.addAll(Arrays.asList("Availability", "Efficiency","Utilization"));
				}else{
					kind_of_report_list = new ArrayList<String>();
				}
				kind_adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, kind_of_report_list);
				kind_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				kind_of_report.setAdapter(kind_adapter);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		kind_of_report.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> adapter, View view,
					int position, long arg3) {
				// TODO Auto-generated method stub
				kind_value = adapter.getItemAtPosition(position).toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		from_text = (TextView) findViewById(R.id.employee_tv_from);
		to_text = (TextView) findViewById(R.id.employee_to);
		pick_from_date = (Button) findViewById(R.id.employee_btn_from);
		pick_from_date.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				final Calendar c = Calendar.getInstance();
	            pYear = c.get(Calendar.YEAR);
	            pMonth = c.get(Calendar.MONTH);
	            pDay = c.get(Calendar.DAY_OF_MONTH);
	 
	            // Launch Date Picker Dialog
	            DatePickerDialog dpd = new DatePickerDialog(ChrDirectorReport.this, new DatePickerDialog.OnDateSetListener() {
	 
	                        @Override
	                        public void onDateSet(DatePicker view, int year,
	                                int monthOfYear, int dayOfMonth) {
	                            // Display Selected date in textbox
	                            //txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
	                        	from_text.setText("From:  " + dayOfMonth + " - " + (monthOfYear + 1) + " - " + year);
	                        	int day_start = dayOfMonth;
	                            int month_start = monthOfYear + 1;
	                            int year_start = year;
	                            
	                            start_date = String.valueOf(String.format("%04d", year_start)) + "-" + String.valueOf(String.format("%02d", month_start)) + "-" + String.valueOf(String.format("%02d", day_start));
	 
	                        }
	                    }, pYear, pMonth, pDay);
	            dpd.show();
			}
		});
		pick_to_date = (Button) findViewById(R.id.employee_btn_to);
		pick_to_date.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final Calendar c = Calendar.getInstance();
	            pYear = c.get(Calendar.YEAR);
	            pMonth = c.get(Calendar.MONTH);
	            pDay = c.get(Calendar.DAY_OF_MONTH);
	 
	            // Launch Date Picker Dialog
	            DatePickerDialog dpd = new DatePickerDialog(ChrDirectorReport.this, new DatePickerDialog.OnDateSetListener() {
	 
	                        @Override
	                        public void onDateSet(DatePicker view, int year,
	                                int monthOfYear, int dayOfMonth) {
	                            // Display Selected date in textbox
	                            //txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
	                        	to_text.setText("To:  " + dayOfMonth + " - " + (monthOfYear + 1) + " - " + year);
	                        	int day_end = dayOfMonth;
	                            int month_end = monthOfYear + 1;
	                            int year_end = year;
	                            
	                            end_date = String.valueOf(String.format("%04d", year_end)) + "-" + String.valueOf(String.format("%02d", month_end)) + "-" + String.valueOf(String.format("%02d", day_end));
	                        }
	                    }, pYear, pMonth, pDay);
	            dpd.show();
			}
		});
			generate.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					try {
						if(employee_value == null){
							toast_string = "Please Select Name";
						}else{
							//type_value = type_of_report.getSelectedItem().toString();
							if(type_value == "Select Type"){
								toast_string = "Please Select Type";
							}else{
								//kind_value = kind_of_report.getSelectedItem().toString();
								if(type_value == "Time Log"){
									if(kind_value == null){
										toast_string = "Please Select Kind";
									}else{
										if(start_date != null && end_date != null){
											try {
												Connection con = connectionClass.CONN();
									        	if(con == null){
									        		toast_string = "Error in connection";
									        	}else{
									        		try {
									        			getTimeLog(kind_value);	
													} catch (Exception e) {
														// TODO: handle exception
														toast_string = e.getMessage();
													}
									        		
									        	}
											} catch (Exception e) {
												// TODO: handle exception
												toast_string = e.getMessage();
											}
										}else{
											toast_string = "Date range must not be empty";
										}
									}
								}else if(type_value == "Volume Date"){
									if(start_date != null && end_date != null){
										try {
											Connection con = connectionClass.CONN();
								        	if(con == null){
								        		toast_string = "Error in connection";
								        	}else{
								        		try {
								        			getVolumeDate(kind_value);	
												} catch (Exception e) {
													// TODO: handle exception
													toast_string = e.getMessage();
												}
								        		
								        	}
										} catch (Exception e) {
											// TODO: handle exception
											toast_string = e.getMessage();
										}
									}else{
										toast_string = "Date range must not be empty";
									}
								}else if(type_value == "Loss Time"){
									if(kind_value == null){
										toast_string = "Please Select Kind";
									}else{
										if(start_date != null && end_date != null){
											try {
												Connection con = connectionClass.CONN();
									        	if(con == null){
									        		toast_string = "Error in connection";
									        	}else{
									        		try {
									        			getLossTime(kind_value);	
													} catch (Exception e) {
														// TODO: handle exception
														toast_string = e.getMessage();
													}
									        		
									        	}
											} catch (Exception e) {
												// TODO: handle exception
												toast_string = e.getMessage();
											}
										}else{
											toast_string = "Date range must not be empty";
										}
									}
								}else if(type_value == "Availability"){
									if(kind_value == null){
										toast_string = "Please Select Kind";
									}else{
										if(start_date != null && end_date != null){
											try {
												Connection con = connectionClass.CONN();
									        	if(con == null){
									        		toast_string = "Error in connection";
									        	}else{
									        		try {
									        			getAvailability(kind_value);	
													} catch (Exception e) {
														// TODO: handle exception
														toast_string = e.getMessage();
													}
									        		
									        	}
											} catch (Exception e) {
												// TODO: handle exception
												toast_string = e.getMessage();
											}
										}else{
											toast_string = "Date range must not be empty";
										}
									}
								}else if(type_value == "Efficiency"){
									if(kind_value == null){
										toast_string = "Please Select Kind";
									}else{
										if(start_date != null && end_date != null){
											try {
												Connection con = connectionClass.CONN();
									        	if(con == null){
									        		toast_string = "Error in connection";
									        	}else{
									        		try {
									        			Toast.makeText(getApplicationContext(), "Go efficiency", Toast.LENGTH_SHORT).show();
									        			getEfficiency(kind_value);	
													} catch (Exception e) {
														// TODO: handle exception
														toast_string = e.getMessage();
													}
									        		
									        	}
											} catch (Exception e) {
												// TODO: handle exception
												toast_string = e.getMessage();
											}
										}else{
											toast_string = "Date range must not be empty";
										}
									}
									
								}else if(type_value == "Utilization"){
									if(kind_value == null){
										toast_string = "Please Select Kind";
									}else{
										if(start_date != null && end_date != null){
											try {
												Connection con = connectionClass.CONN();
									        	if(con == null){
									        		toast_string = "Error in connection";
									        	}else{
									        		try {
									        			getUtilization(kind_value);	
													} catch (Exception e) {
														// TODO: handle exception
														toast_string = e.getMessage();
													}
									        		
									        	}
											} catch (Exception e) {
												// TODO: handle exception
												toast_string = e.getMessage();
											}
										}else{
											toast_string = "Date range must not be empty";
										}
									}
								}else{
									toast_string = "Please Select Type";
								}
							}
						}
					} catch (Exception e) {
						// TODO: handle exception
						toast_string = e.getMessage();
					}
					Toast.makeText(getApplicationContext(), toast_string, Toast.LENGTH_SHORT).show();
				}
			});
		tv_line_chart.setHighlightPerTapEnabled(true);
		tv_line_chart.setTouchEnabled(true);
		tv_line_chart.setDragEnabled(true);
		tv_line_chart.setScaleEnabled(true);
		tv_line_chart.setDrawGridBackground(false);
		tv_line_chart.setPinchZoom(true);
		Legend l = tv_line_chart.getLegend();
        l.setForm(LegendForm.LINE);
        l.setTextColor(Color.CYAN);
        
        XAxis x1 = tv_line_chart.getXAxis();
        x1.setDrawGridLines(false);
        x1.setAvoidFirstLastClipping(true);
	}
	
	public void setEmployeeList(){
		try {
			Connection con = connectionClass.CONN();
        	if(con == null){
        		toast_string = "Error in connection";
        	}else{
        		String getdepartment = "select department_id from users where user_id = "+id;
        		Statement stmt;
        		ResultSet rs;
        		String getEmployees = "select user_id, user_fname +' '+ user_lname as full_name " +
        					"from users where user_type = 2";
        		stmt = con.createStatement();
        		rs = stmt.executeQuery(getEmployees);
        		while(rs.next()){
        			employee_id_list.add(rs.getInt("user_id"));
        			employee_list.add(rs.getString("full_name"));
        		}
        		employee_adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, employee_list);
        		employee_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        		employee_spinner.setAdapter(employee_adapter);
        		Toast.makeText(getApplicationContext(), id+"", Toast.LENGTH_SHORT).show();
        	}
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	
	public void getTimeLog(String value){
		String query = "select SUM(case when status='stop' then volume end) as total_volume, "+
				"SUM(case when status!='running' then elapsed_time end) as total_elapsed, " +
				"SUM(case when status!='running' then cycle_time end) as total_cycle,"+
				"convert(date,end_date) date from timeandvolume " +
				"where employee_number = "+employee_user_id+" and status!='running' "+
				"and convert(date,end_date) >= convert(date,'" + start_date + "') "+
				"and convert(date,end_date) <= convert(date,'" + end_date + "') "+
				"group by convert(date,end_date) order by convert(date,end_date) asc";
		Statement stmt;
		ResultSet rs;
        
		if(value == "Elapsed Time"){
			try {
				Connection con = connectionClass.CONN();
	        	if(con == null){
	        		toast_string = "Error in connection";
	        	}else{
	        		stmt = con.createStatement();
	        		rs = stmt.executeQuery(query);
	        		int i = 0;
	        		xAxis = new ArrayList<String>();
	        		Entry v1e1;
	        		valueSet1 = new ArrayList<Entry>();
	        		while(rs.next()){
	        			v1e1 = new Entry(rs.getFloat("total_elapsed"), i); 
  	                  	valueSet1.add(v1e1);
  	                  	xAxis.add(rs.getString("date").toString());
  	                  	i++;
	        		}
	        	}
			} catch (Exception e) {
				// TODO: handle exception
				toast_string = e.getMessage();
				Toast.makeText(getApplicationContext(), toast_string, Toast.LENGTH_SHORT).show();
			}
		}else if(value == "Volume"){
			try {
				Connection con = connectionClass.CONN();
	        	if(con == null){
	        		toast_string = "Error in connection";
	        	}else{
	        		stmt = con.createStatement();
	        		rs = stmt.executeQuery(query);
	        		int i = 0;
	        		xAxis = new ArrayList<String>();
	        		Entry v1e1;
	        		valueSet1 = new ArrayList<Entry>();
	        		while(rs.next()){
	        			v1e1 = new Entry(rs.getFloat("total_volume"), i); 
  	                  	valueSet1.add(v1e1);
  	                  	xAxis.add(rs.getString("date").toString());
  	                  	i++;
	        		}
	        	}
			} catch (Exception e) {
				// TODO: handle exception
				toast_string = e.getMessage();
			}
			
		}else if(value == "Cycle Time"){
			try {
				Connection con = connectionClass.CONN();
	        	if(con == null){
	        		toast_string = "Error in connection";
	        	}else{
	        		stmt = con.createStatement();
	        		rs = stmt.executeQuery(query);
	        		int i = 0;
	        		xAxis = new ArrayList<String>();
	        		Entry v1e1;
	        		valueSet1 = new ArrayList<Entry>();
	        		while(rs.next()){
	        			v1e1 = new Entry(rs.getFloat("total_cycle"), i); 
  	                  	valueSet1.add(v1e1);
  	                  	xAxis.add(rs.getString("date").toString());
  	                  	i++;
	        		}
	        	}
			} catch (Exception e) {
				// TODO: handle exception
				toast_string = e.getMessage();
			}
		}else{
			toast_string = "Please Select Kind";
		}
		LineDataSet LinedataSet1 = new LineDataSet(valueSet1, "Legend");
        LinedataSet1.setColor(ColorTemplate.getHoloBlue());
        LinedataSet1.setCircleColor(ColorTemplate.getHoloBlue());
        LinedataSet1.setLineWidth(2f);
        LinedataSet1.setCircleSize(4f);
        LinedataSet1.setFillAlpha(65);
        LinedataSet1.setFillColor(ColorTemplate.getHoloBlue());
        LinedataSet1.setValueTextSize(10f);
        LineData data = new LineData(xAxis, LinedataSet1);
        tv_line_chart.setData(data);
        tv_line_chart.setDescription(type_value+" ("+kind_value+")");
        tv_line_chart.animateXY(2000, 2000);
        tv_line_chart.invalidate();
	}
	
	public void getVolumeDate(String value){
		String query = "with tv as (select " +
				"convert(date,end_date) as date," +
				"avg(case when standard_time = 0 then cycle_time when standard_time != 0 then " +
				"standard_time end) as stand, " +
				"sum(case when status='stop' then volume end) as total_volume, " +
				"SUM(case when status!='running' then elapsed_time end) as total_elapsed, " +
				"SUM(case when status!='running' then cycle_time end) as total_cycle, " +
				"SUM(case when status!='running' then elapsed_time end) as standard_cycle " +
				"from timeandvolume where employee_number = " + employee_user_id +
				" and status!='running' and work_status = 1 and " +
				"convert(date,end_date) >= convert(date,'" + start_date + "') and " +
				"convert(date,end_date) < = convert(date,'" + end_date + "') " +
				"group by convert(date,end_date)) " +
				"select date, total_volume, stand, stand*total_volume as timeatstandard, " +
				"total_elapsed as activity_time, " +
				"total_elapsed/total_volume as actual_cycle from tv " +
				"order by date asc";
		Statement stmt;
		ResultSet rs;
		if(value == "Standard Cycle Time"){
			try {
				Connection con = connectionClass.CONN();
	        	if(con == null){
	        		toast_string = "Error in connection";
	        	}else{
	        		stmt = con.createStatement();
	        		rs = stmt.executeQuery(query);
	        		int i = 0;
	        		xAxis = new ArrayList<String>();
	        		Entry v1e1;
	        		valueSet1 = new ArrayList<Entry>();
	        		while(rs.next()){
	        			v1e1 = new Entry(rs.getFloat("stand"), i); 
  	                  	valueSet1.add(v1e1);
  	                  	xAxis.add(rs.getString("date").toString());
  	                  	i++;
	        		}
	        	}
			} catch (Exception e) {
				// TODO: handle exception
				toast_string = e.getMessage();
			}
		}else if(value == "Time@std"){
			try {
				Connection con = connectionClass.CONN();
	        	if(con == null){
	        		toast_string = "Error in connection";
	        	}else{
	        		stmt = con.createStatement();
	        		rs = stmt.executeQuery(query);
	        		int i = 0;
	        		xAxis = new ArrayList<String>();
	        		Entry v1e1;
	        		valueSet1 = new ArrayList<Entry>();
	        		while(rs.next()){
	        			v1e1 = new Entry(rs.getFloat("timeatstandard"), i); 
  	                  	valueSet1.add(v1e1);
  	                  	xAxis.add(rs.getString("date").toString());
  	                  	i++;
	        		}
	        	}
			} catch (Exception e) {
				// TODO: handle exception
				toast_string = e.getMessage();
			}
		}else if(value == "Activiy Time"){
			try {
				Connection con = connectionClass.CONN();
	        	if(con == null){
	        		toast_string = "Error in connection";
	        	}else{
	        		stmt = con.createStatement();
	        		rs = stmt.executeQuery(query);
	        		int i = 0;
	        		xAxis = new ArrayList<String>();
	        		Entry v1e1;
	        		valueSet1 = new ArrayList<Entry>();
	        		while(rs.next()){
	        			v1e1 = new Entry(rs.getFloat("activity_time"), i); 
  	                  	valueSet1.add(v1e1);
  	                  	xAxis.add(rs.getString("date").toString());
  	                  	i++;
	        		}
	        	}
			} catch (Exception e) {
				// TODO: handle exception
				toast_string = e.getMessage();
			}
		}else if(value == "Actual Cycle Time"){
			try {
				Connection con = connectionClass.CONN();
	        	if(con == null){
	        		toast_string = "Error in connection";
	        	}else{
	        		stmt = con.createStatement();
	        		rs = stmt.executeQuery(query);
	        		int i = 0;
	        		xAxis = new ArrayList<String>();
	        		Entry v1e1;
	        		valueSet1 = new ArrayList<Entry>();
	        		while(rs.next()){
	        			v1e1 = new Entry(rs.getFloat("actual_cycle"), i); 
  	                  	valueSet1.add(v1e1);
  	                  	xAxis.add(rs.getString("date").toString());
  	                  	i++;
	        		}
	        	}
			} catch (Exception e) {
				// TODO: handle exception
				toast_string = e.getMessage();
			}
		}else if(value == "Volume"){
			try {
				Connection con = connectionClass.CONN();
	        	if(con == null){
	        		toast_string = "Error in connection";
	        	}else{
	        		stmt = con.createStatement();
	        		rs = stmt.executeQuery(query);
	        		int i = 0;
	        		xAxis = new ArrayList<String>();
	        		Entry v1e1;
	        		valueSet1 = new ArrayList<Entry>();
	        		while(rs.next()){
	        			v1e1 = new Entry(rs.getFloat("total_volume"), i); 
  	                  	valueSet1.add(v1e1);
  	                  	xAxis.add(rs.getString("date").toString());
  	                  	i++;
	        		}
	        	}
			} catch (Exception e) {
				// TODO: handle exception
				toast_string = e.getMessage();
			}
		}else{
			toast_string = "Please Select Kind";
		}
		LineDataSet LinedataSet1 = new LineDataSet(valueSet1, "Legend");
        LinedataSet1.setColor(ColorTemplate.getHoloBlue());
        LinedataSet1.setCircleColor(ColorTemplate.getHoloBlue());
        LinedataSet1.setLineWidth(2f);
        LinedataSet1.setCircleSize(4f);
        LinedataSet1.setFillAlpha(65);
        LinedataSet1.setFillColor(ColorTemplate.getHoloBlue());
        LinedataSet1.setValueTextSize(10f);
        LineData data = new LineData(xAxis, LinedataSet1);
        tv_line_chart.setData(data);
        tv_line_chart.setDescription(type_value+" ("+kind_value+")");
        tv_line_chart.animateXY(2000, 2000);
        tv_line_chart.invalidate();
	}
	
	public void getLossTime(String value){
		String query = "select convert(date,end_date) as date, " +
				"SUM(case when status='stop' then volume end) as total_volume," +
				"SUM(case when status!='running' then elapsed_time end) as total_elapsed," +
				"SUM(case when status!='running' then cycle_time end) as total_cycle from " +
				"timeandvolume where employee_number = " + employee_user_id + 
				" and status = 'stop' and work_status = 2 and " +
				"convert(date,end_date) >= convert(date,'" + start_date + "') and " +
				"convert(date,end_date) <= convert(date,'" + end_date + "') " +
				"group by convert(date,end_date) order by convert(date,end_date)";
		Statement stmt;
		ResultSet rs;
        
		if(value == "Elapsed Time"){
			try {
				Connection con = connectionClass.CONN();
	        	if(con == null){
	        		toast_string = "Error in connection";
	        	}else{
	        		stmt = con.createStatement();
	        		rs = stmt.executeQuery(query);
	        		int i = 0;
	        		xAxis = new ArrayList<String>();
	        		Entry v1e1;
	        		valueSet1 = new ArrayList<Entry>();
	        		while(rs.next()){
	        			v1e1 = new Entry(rs.getFloat("total_elapsed"), i); 
  	                  	valueSet1.add(v1e1);
  	                  	xAxis.add(rs.getString("date").toString());
  	                  	i++;
	        		}
	        	}
			} catch (Exception e) {
				// TODO: handle exception
				toast_string = e.getMessage();
			}
		}else if(value == "Volume"){
			try {
				Connection con = connectionClass.CONN();
	        	if(con == null){
	        		toast_string = "Error in connection";
	        	}else{
	        		stmt = con.createStatement();
	        		rs = stmt.executeQuery(query);
	        		int i = 0;
	        		xAxis = new ArrayList<String>();
	        		Entry v1e1;
	        		valueSet1 = new ArrayList<Entry>();
	        		while(rs.next()){
	        			v1e1 = new Entry(rs.getFloat("total_volume"), i); 
  	                  	valueSet1.add(v1e1);
  	                  	xAxis.add(rs.getString("date").toString());
  	                  	i++;
	        		}
	        	}
			} catch (Exception e) {
				// TODO: handle exception
				toast_string = e.getMessage();
			}
			
		}else if(value == "Cycle Time"){
			try {
				Connection con = connectionClass.CONN();
	        	if(con == null){
	        		toast_string = "Error in connection";
	        	}else{
	        		stmt = con.createStatement();
	        		rs = stmt.executeQuery(query);
	        		int i = 0;
	        		xAxis = new ArrayList<String>();
	        		Entry v1e1;
	        		valueSet1 = new ArrayList<Entry>();
	        		while(rs.next()){
	        			v1e1 = new Entry(rs.getFloat("total_cycle"), i); 
  	                  	valueSet1.add(v1e1);
  	                  	xAxis.add(rs.getString("date").toString());
  	                  	i++;
	        		}
	        	}
			} catch (Exception e) {
				// TODO: handle exception
				toast_string = e.getMessage();
			}
		}else{
			toast_string = "Please Select Kind";
		}
		LineDataSet LinedataSet1 = new LineDataSet(valueSet1, "Legend");
        LinedataSet1.setColor(ColorTemplate.getHoloBlue());
        LinedataSet1.setCircleColor(ColorTemplate.getHoloBlue());
        LinedataSet1.setLineWidth(2f);
        LinedataSet1.setCircleSize(4f);
        LinedataSet1.setFillAlpha(65);
        LinedataSet1.setFillColor(ColorTemplate.getHoloBlue());
        LinedataSet1.setValueTextSize(10f);
        LineData data = new LineData(xAxis, LinedataSet1);
        tv_line_chart.setData(data);
        tv_line_chart.setDescription(type_value+" ("+kind_value+")");
        tv_line_chart.animateXY(2000, 2000);
        tv_line_chart.invalidate();
	}
	
	public void getAvailability(String value){
		String get_processing_time = "select convert(date,end_date) date, sum(cycle_time) total_cycle from " +
				"timeandvolume where work_status = 1 and employee_number = "+employee_user_id+" " +
						"and convert(date,end_date)>= convert(date,'" + start_date + "') and " +
						"convert(date,end_date) <= convert(date,'" + end_date + "') "+
						"group by convert(date,end_date), employee_number " +
						"order by convert(date,end_date) desc";
		String get_total_time = "select convert(date,end_date) date,SUM(cycle_time) total_cycle from " +
				"timeandvolume where work_status!=4 and employee_number = "+employee_user_id+" " +
						"and convert(date,end_date)>= convert(date,'" + start_date + "') and " +
						"convert(date,end_date) <= convert(date,'" + end_date + "') "+" group by " +
						"convert(date,end_date), employee_number order by convert(date,end_date) desc";
		Statement stmt,stmt1;
		ResultSet rs,rs1;
        
		if(kind_value == "Processing Time"){
			try {
				Connection con = connectionClass.CONN();
	        	if(con == null){
	        		toast_string = "Error in connection";
	        	}else{
	        		stmt = con.createStatement();
	        		rs = stmt.executeQuery(get_processing_time);
	        		int i = 0;
	        		xAxis = new ArrayList<String>();
	        		Entry v1e1;
	        		valueSet1 = new ArrayList<Entry>();
	        		while(rs.next()){
	        			v1e1 = new Entry(rs.getFloat("total_cycle"), i); 
  	                  	valueSet1.add(v1e1);
  	                  	xAxis.add(rs.getString("date").toString());
  	                  	i++;
	        		}
	        		
	        	}
			} catch (Exception e) {
				// TODO: handle exception
				toast_string = e.getMessage();
			}
		}else if(kind_value == "Total Time"){
			try {
				Connection con = connectionClass.CONN();
	        	if(con == null){
	        		toast_string = "Error in connection";
	        	}else{
	        		stmt = con.createStatement();
	        		rs = stmt.executeQuery(get_total_time);
	        		int i = 0;
	        		xAxis = new ArrayList<String>();
	        		Entry v1e1;
	        		valueSet1 = new ArrayList<Entry>();
	        		while(rs.next()){
	        			v1e1 = new Entry(rs.getFloat("total_cycle"), i); 
  	                  	valueSet1.add(v1e1);
  	                  	xAxis.add(rs.getString("date").toString());
  	                  	i++;
	        		}
	        	}
			} catch (Exception e) {
				// TODO: handle exception
				toast_string = e.getMessage();
			}
		}else if(kind_value == "Availability"){
			try {
				Connection con = connectionClass.CONN();
	        	if(con == null){
	        		toast_string = "Error in connection";
	        	}else{
	        		stmt = con.createStatement();
	        		stmt1 = con.createStatement();
	        		rs = stmt.executeQuery(get_processing_time);
	        		rs1 = stmt1.executeQuery(get_total_time);
	        		int i = 0;
	        		xAxis = new ArrayList<String>();
	        		Entry v1e1;
	        		valueSet1 = new ArrayList<Entry>();
	        		while(rs.next()){
	        			rs1.next();
	        			v1e1 = new Entry((rs.getFloat("total_cycle")/rs1.getFloat("total_cycle"))*100, i); 
  	                  	valueSet1.add(v1e1);
  	                  	xAxis.add(rs.getString("date").toString());
  	                  	i++;
	        		}
	        	}
			} catch (Exception e) {
				// TODO: handle exception
				toast_string = e.getMessage();
			}
		}else{
			toast_string = "Please Select Kind";
		}
		LineDataSet LinedataSet1 = new LineDataSet(valueSet1, "Legend");
        LinedataSet1.setColor(ColorTemplate.getHoloBlue());
        LinedataSet1.setCircleColor(ColorTemplate.getHoloBlue());
        LinedataSet1.setLineWidth(2f);
        LinedataSet1.setCircleSize(4f);
        LinedataSet1.setFillAlpha(65);
        LinedataSet1.setFillColor(ColorTemplate.getHoloBlue());
        LinedataSet1.setValueTextSize(10f);
        LineData data = new LineData(xAxis, LinedataSet1);
        tv_line_chart.setData(data);
        tv_line_chart.setDescription(type_value+" ("+kind_value+")");
        tv_line_chart.animateXY(2000, 2000);
        tv_line_chart.invalidate();
	}
	
	public void getEfficiency(String value){
		String get_time_at_std = "with tv as ("+
				"select "+
				"convert(date,end_date) date,"+
				"AVG(case when standard_time = 0 then cycle_time when standard_time!=0 then standard_time end) as stand,"+
				"SUM(case when status='stop' then volume end) as total_volume "+
				"from timeandvolume "+
				"where employee_number = "+employee_user_id+" AND status!='running' AND work_status = 1 "+
				"and convert(date,end_date)>= convert(date,'" + start_date + "') and " +
				"convert(date,end_date) <= convert(date,'" + end_date + "') "+
				"group by convert(date,end_date),employee_number) "+
				"select date, stand*total_volume as timestandard from tv";
		String get_processing_time = "select convert(date,end_date) date, sum(cycle_time) total_cycle from " +
				"timeandvolume where work_status = 1 and employee_number = "+employee_user_id+" " +
						"and convert(date,end_date)>= convert(date,'" + start_date + "') and " +
						"convert(date,end_date) <= convert(date,'" + end_date + "') "+
						"group by convert(date,end_date), employee_number " +
						"order by convert(date,end_date) desc";
		Statement stmt,stmt1;
		ResultSet rs,rs1;
		if(kind_value == "Time@std"){
			try {
				Connection con = connectionClass.CONN();
	        	if(con == null){
	        		toast_string = "Error in connection";
	        	}else{
	        		stmt = con.createStatement();
	        		rs = stmt.executeQuery(get_time_at_std);
	        		int i = 0;
	        		xAxis = new ArrayList<String>();
	        		Entry v1e1;
	        		valueSet1 = new ArrayList<Entry>();
	        		while(rs.next()){
	        			v1e1 = new Entry(rs.getFloat("timestandard"), i); 
  	                  	valueSet1.add(v1e1);
  	                  	xAxis.add(rs.getString("date").toString());
  	                  	i++;
	        		}
	        		
	        	}
			} catch (Exception e) {
				// TODO: handle exception
				toast_string = e.getMessage();
			}
		}else if(kind_value == "Processing Time"){
			try {
				Connection con = connectionClass.CONN();
	        	if(con == null){
	        		toast_string = "Error in connection";
	        	}else{
	        		stmt = con.createStatement();
	        		rs = stmt.executeQuery(get_processing_time);
	        		int i = 0;
	        		xAxis = new ArrayList<String>();
	        		Entry v1e1;
	        		valueSet1 = new ArrayList<Entry>();
	        		while(rs.next()){
	        			v1e1 = new Entry(rs.getFloat("total_cycle"), i); 
  	                  	valueSet1.add(v1e1);
  	                  	xAxis.add(rs.getString("date").toString());
  	                  	i++;
	        		}
	        		
	        	}
			} catch (Exception e) {
				// TODO: handle exception
				toast_string = e.getMessage();
			}
		}else if(kind_value == "Efficiency"){
			try {
				Connection con = connectionClass.CONN();
	        	if(con == null){
	        		toast_string = "Error in connection";
	        	}else{
	        		stmt = con.createStatement();
	        		stmt1 = con.createStatement();
	        		rs = stmt.executeQuery(get_time_at_std);
	        		rs1 = stmt1.executeQuery(get_processing_time);
	        		int i = 0;
	        		xAxis = new ArrayList<String>();
	        		Entry v1e1;
	        		valueSet1 = new ArrayList<Entry>();
	        		while(rs.next()){
	        			rs1.next();
	        			v1e1 = new Entry((rs.getFloat("timestandard")/rs1.getFloat("total_cycle"))*100, i); 
		                  	valueSet1.add(v1e1);
		                  	xAxis.add(rs.getString("date").toString());
		                  	i++;
	        		}
	        	}
			} catch (Exception e) {
				// TODO: handle exception
				toast_string = e.getMessage();
			}
		}else{
			toast_string = "Please Select Kind";
		}
		LineDataSet LinedataSet1 = new LineDataSet(valueSet1, "Legend");
        LinedataSet1.setColor(ColorTemplate.getHoloBlue());
        LinedataSet1.setCircleColor(ColorTemplate.getHoloBlue());
        LinedataSet1.setLineWidth(2f);
        LinedataSet1.setCircleSize(4f);
        LinedataSet1.setFillAlpha(65);
        LinedataSet1.setFillColor(ColorTemplate.getHoloBlue());
        LinedataSet1.setValueTextSize(10f);
        LineData data = new LineData(xAxis, LinedataSet1);
        tv_line_chart.setData(data);
        tv_line_chart.setDescription(type_value+" ("+kind_value+")");
        tv_line_chart.animateXY(2000, 2000);
        tv_line_chart.invalidate();
	}
	
	public void getUtilization(String value){
		String get_time_at_std = "with tv as ("+
				"select "+
				"convert(date,end_date) date,"+
				"AVG(case when standard_time = 0 then cycle_time when standard_time!=0 then standard_time end) as stand,"+
				"SUM(case when status='stop' then volume end) as total_volume "+
				"from timeandvolume "+
				"where employee_number = "+employee_user_id+" AND status!='running' AND work_status = 1 "+
				"and convert(date,end_date)>= convert(date,'" + start_date + "') and " +
				"convert(date,end_date) <= convert(date,'" + end_date + "') "+
				"group by convert(date,end_date),employee_number) "+
				"select date, stand*total_volume as timestandard from tv";
		String get_processing_time = "select convert(date,end_date) date, sum(cycle_time) total_cycle from " +
				"timeandvolume where work_status = 1 and employee_number = "+employee_user_id+" " +
						"and convert(date,end_date)>= convert(date,'" + start_date + "') and " +
						"convert(date,end_date) <= convert(date,'" + end_date + "') "+
						"group by convert(date,end_date), employee_number " +
						"order by convert(date,end_date) desc";
		String get_total_time = "select convert(date,end_date) date,SUM(cycle_time) total_cycle from " +
				"timeandvolume where work_status!=4 and employee_number = "+employee_user_id+" " +
						"and convert(date,end_date)>= convert(date,'" + start_date + "') and " +
						"convert(date,end_date) <= convert(date,'" + end_date + "') "+" group by " +
						"convert(date,end_date), employee_number order by convert(date,end_date) desc";
		Statement stmt,stmt1,stmt2;
		ResultSet rs,rs1,rs2;
		if(kind_value == "Availability"){
			try {
				Connection con = connectionClass.CONN();
	        	if(con == null){
	        		toast_string = "Error in connection";
	        	}else{
	        		stmt = con.createStatement();
	        		stmt1 = con.createStatement();
	        		rs = stmt.executeQuery(get_processing_time);
	        		rs1 = stmt1.executeQuery(get_total_time);
	        		int i = 0;
	        		xAxis = new ArrayList<String>();
	        		Entry v1e1;
	        		valueSet1 = new ArrayList<Entry>();
	        		while(rs.next()){
	        			rs1.next();
	        			v1e1 = new Entry(rs.getFloat("total_cycle")/rs1.getFloat("total_cycle"), i); 
  	                  	valueSet1.add(v1e1);
  	                  	xAxis.add(rs.getString("date").toString());
  	                  	i++;
	        		}
	        	}
			} catch (Exception e) {
				// TODO: handle exception
				toast_string = e.getMessage();
			}
		}else if(kind_value == "Efficiency"){
			try {
				Connection con = connectionClass.CONN();
	        	if(con == null){
	        		toast_string = "Error in connection";
	        	}else{
	        		stmt = con.createStatement();
	        		stmt1 = con.createStatement();
	        		rs = stmt.executeQuery(get_time_at_std);
	        		rs1 = stmt1.executeQuery(get_processing_time);
	        		int i = 0;
	        		xAxis = new ArrayList<String>();
	        		Entry v1e1;
	        		valueSet1 = new ArrayList<Entry>();
	        		while(rs.next()){
	        			rs1.next();
	        			v1e1 = new Entry(rs.getFloat("timestandard")/rs1.getFloat("total_cycle"), i); 
		                  	valueSet1.add(v1e1);
		                  	xAxis.add(rs.getString("date").toString());
		                  	i++;
	        		}
	        	}
			} catch (Exception e) {
				// TODO: handle exception
				toast_string = e.getMessage();
			}
		}else if(kind_value == "Utilization"){
			try {
				Connection con = connectionClass.CONN();
	        	if(con == null){
	        		toast_string = "Error in connection";
	        	}else{
	        		stmt = con.createStatement();
	        		stmt1 = con.createStatement();
	        		stmt2 = con.createStatement();
	        		rs = stmt.executeQuery(get_time_at_std);
	        		rs1 = stmt1.executeQuery(get_processing_time);
	        		rs2 = stmt2.executeQuery(get_total_time);
	        		int i = 0;
	        		xAxis = new ArrayList<String>();
	        		Entry v1e1;
	        		valueSet1 = new ArrayList<Entry>();
	        		while(rs.next()){
	        			rs1.next();
	        			rs2.next();
	        			float utilization = (rs1.getFloat("total_cycle")/rs2.getFloat("total_cycle"))*(rs.getFloat("timestandard")/rs1.getFloat("total_cycle"))*100;
	        			v1e1 = new Entry(utilization, i); 
		                  	valueSet1.add(v1e1);
		                  	xAxis.add(rs.getString("date").toString());
		                  	i++;
	        		}
	        	}
			} catch (Exception e) {
				// TODO: handle exception
				toast_string = e.getMessage();
			}
		}else{
			toast_string = "Please Select Kind";
		}
		LineDataSet LinedataSet1 = new LineDataSet(valueSet1, "Legend");
        LinedataSet1.setColor(ColorTemplate.getHoloBlue());
        LinedataSet1.setCircleColor(ColorTemplate.getHoloBlue());
        LinedataSet1.setLineWidth(2f);
        LinedataSet1.setCircleSize(4f);
        LinedataSet1.setFillAlpha(65);
        LinedataSet1.setFillColor(ColorTemplate.getHoloBlue());
        LinedataSet1.setValueTextSize(10f);
        LineData data = new LineData(xAxis, LinedataSet1);
        tv_line_chart.setData(data);
        tv_line_chart.setDescription(type_value+" ("+kind_value+")");
        tv_line_chart.animateXY(2000, 2000);
        tv_line_chart.invalidate();
	}
}
