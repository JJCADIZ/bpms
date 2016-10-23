package com.example.bpm;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import com.example.bpm.PathfinderUpdateMain.ApprovePathfinder;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class A3ApproveClose extends Activity{
	
	
	Button approveNow,addComments,update,decline;
	ConnectionClass connectionClass;
	EditText projName2,projBackground2,projRecommendation2,projVision2,projIssues2;
	
	LinearLayout actionPlan,Criteria;
	
	int UserType;
	String projID,userID;
	
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.update_a3_main);
		 
		 
		 actionPlan = (LinearLayout) findViewById(R.id.ActionPlanLayout);
		 Criteria = (LinearLayout) findViewById(R.id.CriteriaLayout);
		 
		 projName2 = (EditText) findViewById(R.id.edtProj2);
		 projBackground2 = (EditText) findViewById(R.id.edtBackground2);
		 projRecommendation2 = (EditText) findViewById(R.id.edtRecommendation2);
		 projVision2 = (EditText) findViewById(R.id.edtVision2);
		 projIssues2 = (EditText) findViewById(R.id.edtIssues2);
		 

		 approveNow = (Button) findViewById(R.id.btnA3Approve);
		 addComments = (Button) findViewById(R.id.btnAddcomments);
		 
		 decline = (Button) findViewById(R.id.btnA3Decline);
		 update = (Button) findViewById(R.id.btnUpdateA3);

		 
		 connectionClass = new ConnectionClass();
		 
		 Bundle a3bundle = getIntent().getExtras();
			
		projID = a3bundle.getString("a3ID");	
		String projName = a3bundle.getString("a3Name"); 
		String projBackground = a3bundle.getString("a3Background");
		String projRecommendation = a3bundle.getString("a3Recommendation");
		String projVision = a3bundle.getString("a3Vision");
		String projIssues = a3bundle.getString("a3Issues");
		
		projName2.setText(projName);
		projBackground2.setText(projBackground);
		projRecommendation2.setText(projRecommendation);
		projVision2.setText(projVision);
		projIssues2.setText(projIssues);
		
		SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref",Context. MODE_PRIVATE); 
        Editor editor = pref.edit();
        UserType = pref.getInt("usrtp", 0);
        userID = pref.getString("IDD", null); 
        
        actionPlan.setVisibility(LinearLayout.GONE);
		 Criteria.setVisibility(LinearLayout.GONE);
		 update.setVisibility(Button.GONE);
		
        /*
		if(UserType == 3)
        {
			approveNow.setVisibility(View.GONE);
        	
        }else if(UserType == 2)
        {
        	approveNow.setVisibility(View.VISIBLE);
        }
		*/

		
		addComments.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
            	Bundle actionPlan = new Bundle();
                
            	actionPlan.putString("a3IDD", projID);
            

                Intent A3Comments = new Intent(A3ApproveClose.this, A3Comment.class);
                A3Comments.putExtras(actionPlan);
            	startActivity(A3Comments);
                

            }
        });
		
		approveNow.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
            	ApproveA3 approve = new ApproveA3();
            	approve.execute("");

            }
        });
		
		decline.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
            	DeclineA3 Decline = new DeclineA3();
            	Decline.execute("");

            }
        });
			
		
	}
	
public class ApproveA3 extends AsyncTask<String, String, String> {
	
        String z = "";
        Boolean isSuccess = false;
        @Override
        protected void onPreExecute() {
       
        }
        @Override
        protected void onPostExecute(String r) {
  
            Toast.makeText(A3ApproveClose.this, r, Toast.LENGTH_SHORT).show();
            if(isSuccess==true) {
            	approveNow.setVisibility(View.GONE);
            	decline.setVisibility(View.GONE);
            }
        }
        @Override
        protected String doInBackground(String... params) {
                try {
                    Connection con = connectionClass.CONN();
                    if (con == null) {
                        z = "Error in connection with SQL server";
                    } else {
                        String query = "Update a3team set closure_approved = 1 where user_id = '"+userID+"' AND proj_id = '"+projID+"'";
                        PreparedStatement preparedStatement = con.prepareStatement(query);
                        preparedStatement.executeUpdate();
                       

                        String comp = "select count(proj_approvers) from a3projects where proj_id = '"+projID+"'";
                        PreparedStatement preparedStatement3 = con.prepareStatement(comp);
                        ResultSet equal = preparedStatement3.executeQuery();
                        if(equal.next())
                        {
                        	int approver = equal.getInt("proj_approvers");
                        	int approved = equal.getInt("proj_approved");
                        	
                        		if(approver == approved)
                        		{
                        			String query4 = "Update a3projects set proj_status = 11 where proj_id = '"+projID+"'";
                                    PreparedStatement preparedStatement4 = con.prepareStatement(query4);
                                    preparedStatement4.executeUpdate();
                        		}
                        }
                        z = "Approved Successfully";
                        isSuccess = true;
                    }
                } catch (Exception ex) {
                    isSuccess = false;
                    z = "Exceptions";
                    Log.e("MYAPP", "exception", ex);
                }
            
            return z;
        }
    }
    

public class DeclineA3 extends AsyncTask<String, String, String> {

    String z = "";
    Boolean isSuccess = false;
    @Override
    protected void onPreExecute() {
   
    }
    @Override
    protected void onPostExecute(String r) {

        Toast.makeText(A3ApproveClose.this, r, Toast.LENGTH_SHORT).show();
        if(isSuccess==true) {
        	approveNow.setVisibility(View.GONE);
        	decline.setVisibility(View.GONE);
        }
    }
    @Override
    protected String doInBackground(String... params) {
            try {
                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {
                    String query = "Update a3team set approved = 2 where user_id = '"+userID+"' AND proj_id = '"+projID+"'";
                    
                    PreparedStatement preparedStatement = con.prepareStatement(query);
                 
                    preparedStatement.executeUpdate();
                    
                    
                    z = "Declined Successfully";
                    isSuccess = true;
                }
            } catch (Exception ex) {
                isSuccess = false;
                z = "Exceptions";
                Log.e("MYAPP", "exception", ex);
            }
        
        return z;
    }
}



}
