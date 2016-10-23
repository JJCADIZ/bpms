package com.example.bpm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.example.bpm.A3Update.FillList;
import com.example.bpm.PathfinderUpdateMain.ApprovePathfinder;
import com.example.bpm.PathfinderUpdateMain.DeletePro;
import com.example.bpm.PathfinderUpdateMain.UpdatePro;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CalendarView.OnDateChangeListener;

public class A3ActionPlanMain extends Activity{
	
	ConnectionClass connectionClass;
	String actionID,empFname,empLname,empName,projID;
	Button addPlan;
	EditText Description;
	CalendarView due;
	Spinner Owner;
	
	PreparedStatement stmt1;
    ResultSet rs1;
    public String  DAY,newDateString;
    Date date2;
    
    
	
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a3_actionplan_lead);

		addPlan = (Button) findViewById(R.id.btnAddPlan);
		Owner = (Spinner) findViewById(R.id.spinOwner);
		Description = (EditText) findViewById(R.id.edtActionPlan);
		due = (CalendarView) findViewById(R.id.clndrDue);

        connectionClass = new ConnectionClass();
        
        Bundle actionPlanMain = getIntent().getExtras();
		
		projID = actionPlanMain.getString("a3_ID");
		
		due.setOnDateChangeListener(new OnDateChangeListener() {

            
            public void onSelectedDayChange(CalendarView view, int year_date, int month_date,
                    int dayOfMonth) {
            	
                int day = dayOfMonth;
                int month = month_date+=1;
                int year = year_date;
                
                if(day<10){
                	
                }else{
                	
                }
                
                DAY=String.valueOf(year)+String.valueOf(month)+String.valueOf(day);
                
                DateFormat df = new SimpleDateFormat("yyyy/M/d"); 
                Date startDate;

            }
        });
        
        
		String spin_owner = "select user_fname,user_lname from users,a3projects,a3team where a3projects.proj_id = '"+projID+"' AND a3team.user_id = users.user_id AND a3team.proj_id = a3projects.proj_id";
		
        try {
        	Connection con = connectionClass.CONN();
        	
            stmt1 = con.prepareStatement(spin_owner);
            rs1 = stmt1.executeQuery();
            
            ArrayList<String> ownData = new ArrayList<String>();

            
            while (rs1.next()) {
                 empFname = rs1.getString("user_fname");
                 empLname = rs1.getString("user_lname");
                 empName = String.valueOf(empFname)+" "+String.valueOf(empLname); 

               
                 ownData.add(empName);

            }
            
            String[] empArray = ownData.toArray(new String[0]);

            
            ArrayAdapter ownerAdapter = new ArrayAdapter(A3ActionPlanMain.this,
                    android.R.layout.simple_list_item_1, ownData);

            
           
            Owner.setAdapter(ownerAdapter);
            Owner.getSelectedItem().toString();
            
        } catch (SQLException ex) {
            ex.printStackTrace();
            Log.e("MYAPP", "exception", ex);
        }

        
        
        addPlan.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
            	AddAction action = new AddAction();
            	action.execute("");

            }
        });
		
 
    }
	
	
public class AddAction extends AsyncTask<String, String, String> {
		
		

        String z = "";
        Boolean isSuccess = false;
        String name =  Owner.getSelectedItem().toString(); 
        String desc = Description.getText().toString();

        @Override
        protected void onPreExecute() {
            
        }

        @Override
        protected void onPostExecute(String r) {
            
            Toast.makeText(A3ActionPlanMain.this, r, Toast.LENGTH_SHORT).show();
            if(isSuccess==true) {
            	Description.setText(null);
            	finish();
            }

        }

		@Override
		protected String doInBackground(String... params) {
			if (Description.equals(""))
	            z = "Please fill all the fields";
	        else {
	            try {
	                Connection con = connectionClass.CONN();
	                if (con == null) {
	                    z = "Error in connection with SQL server";
	                } else {
	                	
	                	String query = "SELECT user_id FROM users WHERE   '"+name+"' LIKE Concat(Concat('%',user_fname),'%')  AND '"+name+"' LIKE  Concat(Concat('%',user_lname),'%')";                                            
	                	PreparedStatement preparedStatement = con.prepareStatement(query);
	                    ResultSet rs = preparedStatement.executeQuery();
	                    
	                    if(rs.next())
	                    {
	                    	
	                    	String empID = rs.getString("user_id"); 
	                    	date2 = new SimpleDateFormat("yyyyMd", Locale.ENGLISH).parse(DAY);
		                	newDateString = new SimpleDateFormat("yyyy/M/d",Locale.ENGLISH).format(date2);

		                	String query2 = "insert into a3action(action_desc,action_duedate,action_owner,proj_id,action_startdate,action_lastupdate,action_status,action_progress)" +
		                	"values('"+desc+"','"+newDateString+"','"+empID+"','"+projID+"',GETDATE(),GETDATE(),1,0)";                                            
		                	PreparedStatement preparedStatement2 = con.prepareStatement(query2);
		                    preparedStatement2.executeUpdate();
		                    z = "Added Successfully";
		                    isSuccess = true;
	                    }
	                	
	           
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
