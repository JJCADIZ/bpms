package com.example.bpm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.CalendarView.OnDateChangeListener;

public class OkrKrDirAddActivity extends Activity{
	
	ConnectionClass connectionClass;
	EditText edtKrName,edtKrImpact;
	Button btnAddKrMain;
	CalendarView calendar;
	
	Date date2;
	public String newDateString;
	public String objID,krID,userID;
	String DAY;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.okr_kr);
		 
		 connectionClass = new ConnectionClass();
		 
		 edtKrName = (EditText) findViewById(R.id.edtKrName);
		 edtKrImpact = (EditText) findViewById(R.id.edtKrImpact);
		 calendar = (CalendarView) findViewById(R.id.clndrDueDate);
		 btnAddKrMain = (Button) findViewById(R.id.btnAddKrmain);
		 
	        Bundle KrBundle = getIntent().getExtras();
	        objID = KrBundle.getString("objID"); 
	        krID  = KrBundle.getString("krID");
	        userID  = KrBundle.getString("userID");
		 
	        
	        
	        
	        btnAddKrMain.setOnClickListener(new View.OnClickListener(){

	            @Override
	            public void onClick(View v) {
	            	
	            	AddKr  addkr= new AddKr();
	            	addkr.execute("");
	                
	            	// INSERT ASYNC HERE
	            } 
	        });
	        
	        calendar.setOnDateChangeListener(new OnDateChangeListener() {

	            
	            public void onSelectedDayChange(CalendarView view, int year_date, int month_date,
	                    int dayOfMonth) {
	            	
	                int day = dayOfMonth;
	                int month = month_date+=1;
	                int year = year_date;
	                
	                if(day<10){
	                	
	                }else{
	                	
	                }
	                
	                DAY=String.valueOf(year)+String.valueOf(month)+String.valueOf(day);

	            }
	        });
	}
	
	

	public class AddKr extends AsyncTask<String, String, String> {
		
		String Krname = edtKrName.getText().toString();
		String Krimpact = edtKrImpact.getText().toString();

        String z = "";
        Boolean isSuccess = false;

        
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String r) {

            Toast.makeText(OkrKrDirAddActivity.this, r, Toast.LENGTH_SHORT).show();
            if(isSuccess==true) {
            	
            	edtKrName.setText(null);
            	edtKrImpact.setText(null);
            	
            }

        }

		@Override
		protected String doInBackground(String... params) {
			if (Krname.trim().equals("") || Krimpact.isEmpty())
	            z = "Please fill all the fields";
	        else {
	            try {
	                Connection con = connectionClass.CONN();
	                if (con == null) {
	                    z = "Error in connection with SQL server";
	                } else {

	                	date2 = new SimpleDateFormat("yyyyMd", Locale.ENGLISH).parse(DAY);
	                	newDateString = new SimpleDateFormat("yyyy/M/d",Locale.ENGLISH).format(date2);

	                	String query = "INSERT INTO keyresults(kr_name,kr_impact,kr_duedate,kr_raised,kr_progress,kr_status,user_id)"+
	                	"VALUES('"+Krname+"','"+Krimpact+"','"+newDateString+"',GETDATE(),0,99,'"+userID+"')";                                            
	                	PreparedStatement preparedStatement = con.prepareStatement(query);
	                    preparedStatement.executeUpdate();
	                    
	                    String query2 = "SELECT kr_id from keyresults WHERE kr_name LIKE '"+Krname+"' AND kr_impact LIKE '"+Krimpact+"' AND kr_duedate LIKE '"+newDateString+"'";                                            
	                	PreparedStatement preparedStatement2 = con.prepareStatement(query2);
	                    ResultSet rs = preparedStatement2.executeQuery();
	                    
	                    if(rs.next())
	                    {
	                    	String krID = rs.getString("kr_id");
	                    	
	                    	String query3 = "INSERT INTO krobj(kr_id,obj_id,user_id) VALUES('"+krID+"','"+objID+"','"+userID+"')";                                            
	        	                	PreparedStatement preparedStatement3 = con.prepareStatement(query3);
	        	                    preparedStatement3.executeUpdate();
	                    }
	                    
	                    
	                    z = "Added Successfully";
	                    isSuccess = true;
	                }
	            } catch (Exception ex) {
	                isSuccess = false;
	                z = "Exceptions";
	                Log.e("MYAPP", "exception", ex);
	            }
	        }
	        return z;
	    }
			
		}
	

}
