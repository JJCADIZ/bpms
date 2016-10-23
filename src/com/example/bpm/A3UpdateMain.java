package com.example.bpm;


import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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

public class A3UpdateMain extends Activity{
	
	
	Button updateA3Now,approveNow,addAction,addCriteria,addComments,decline;
	ConnectionClass connectionClass;
	EditText projName2,projBackground2,projRecommendation2,projVision2,projIssues2;
	
	LinearLayout actionPlan,Criteria;
	
	int UserType,ID;
	String projID,userID;
	
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.update_a3_main);
		 
		 connectionClass = new ConnectionClass();
		 
		 
		 actionPlan = (LinearLayout) findViewById(R.id.ActionPlanLayout);
		 Criteria = (LinearLayout) findViewById(R.id.CriteriaLayout);
		 
		 projName2 = (EditText) findViewById(R.id.edtProj2);
		 projBackground2 = (EditText) findViewById(R.id.edtBackground2);
		 projRecommendation2 = (EditText) findViewById(R.id.edtRecommendation2);
		 projVision2 = (EditText) findViewById(R.id.edtVision2);
		 projIssues2 = (EditText) findViewById(R.id.edtIssues2);
		 
		 updateA3Now = (Button) findViewById(R.id.btnUpdateA3);
		 approveNow = (Button) findViewById(R.id.btnA3Approve);
		 addAction = (Button) findViewById(R.id.btnAddActionPlan);
		 addCriteria = (Button) findViewById(R.id.btnAddA3Criteria);
		 addComments = (Button) findViewById(R.id.btnAddcomments);
		 decline = (Button) findViewById(R.id.btnA3Decline);

		 
		 approveNow.setVisibility(View.GONE);
		 decline.setVisibility(View.GONE);
		 
		 SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref",Context. MODE_PRIVATE); 
	        Editor editor = pref.edit();
	        UserType = pref.getInt("usrtp", 0);
	        userID = pref.getString("IDD", null); 
	        ID = Integer.parseInt(userID);
	        
	        Bundle a3bundle = getIntent().getExtras();
			
			projID = a3bundle.getString("a3ID");
			
			try {
                Connection con = connectionClass.CONN();
                if (con == null) {
                    
                } else {

					String query = "select proj_status,proj_lead from a3projects where proj_id='"+projID+"'";

                    PreparedStatement preparedStatement = con.prepareStatement(query);
                    ResultSet RS = preparedStatement.executeQuery();
                    
                    if(RS.next())
                    {
                    	String statas =  RS.getString("proj_status");
                    	int status = Integer.parseInt(statas);
                    	
                    	String lead = RS.getString("proj_lead");
                    	int leadint = Integer.parseInt(lead);
                    	
                    	if(status == 11 || status == 1 || status == 4)
                    	{
	                    	actionPlan.setVisibility(View.VISIBLE);
	                    	Criteria.setVisibility(View.VISIBLE);
                    	}else if(status != 11 || status != 1 || status != 4)
                    	{
                    		actionPlan.setVisibility(View.GONE);
	                    	Criteria.setVisibility(View.GONE);
                    	}
                    	
                    	if(status == 11)
                    	{
                    		String query2 = "select count(proj_id) from a3action where proj_id='"+projID+"'";

                            PreparedStatement preparedStatement2 = con.prepareStatement(query2);
                            ResultSet RS2 = preparedStatement2.executeQuery();
                            
                            if(RS2.next())
                            {
                            	String count = RS2.getString(1);
                            	int countInt = Integer.parseInt(count);
                            	
                            		if(countInt >= 1)
                            		{
                            			String query3 = "update a3projects set proj_status = 1 where proj_id='"+projID+"'";
                                        PreparedStatement preparedStatement3 = con.prepareStatement(query3);
                                        preparedStatement3.executeUpdate();
                            		}
                            }
                    	}
                    	
                    	if(status == 1)
                    	{
                    		String query2 = "select count(proj_id) from a3action where action_progress = 100 AND proj_id = '"+projID+"'";
                    		
                    		
                            PreparedStatement preparedStatement2 = con.prepareStatement(query2);
                            ResultSet RS2 = preparedStatement2.executeQuery();
                            
                            if(RS2.next())
                            {
                            	String count = RS2.getString(1);
                            	int countInt = Integer.parseInt(count);
                            	
                            	String query3 = "select count(proj_id) from a3action where proj_id = '"+projID+"'";
                            	PreparedStatement preparedStatement3 = con.prepareStatement(query3);
                                ResultSet RS3 = preparedStatement3.executeQuery();
                                
                                if(RS3.next())
                                {
                                	String count2 = RS2.getString(1);
                                	int countInt2 = Integer.parseInt(count2);
                                	
                                	if(countInt == countInt2)
                            		{
                            			String query4 = "update a3projects set proj_status = 5, proj_progress = 100 where proj_id='"+projID+"'";
                            			
                                        PreparedStatement preparedStatement4 = con.prepareStatement(query4);
                                        preparedStatement4.executeUpdate();
                            		}
                                }
                                
                            		
                            }
                    	}
                    	
                    	if(status == 5)
                    	{
                    		String query2 = "select count(proj_id) from a3action where action_progress = 100 AND proj_id = '"+projID+"'";
                    		
                    		
                            PreparedStatement preparedStatement2 = con.prepareStatement(query2);
                            ResultSet RS2 = preparedStatement2.executeQuery();
                            
                            if(RS2.next())
                            {
                            	String count = RS2.getString(1);
                            	int countInt = Integer.parseInt(count);
                            	
                            	String query3 = "select count(proj_id) from a3action where proj_id = '"+projID+"'";
                            	PreparedStatement preparedStatement3 = con.prepareStatement(query3);
                                ResultSet RS3 = preparedStatement3.executeQuery();
                                
                                if(RS3.next())
                                {
                                	String count2 = RS2.getString(1);
                                	int countInt2 = Integer.parseInt(count2);
                                	
                                	if(countInt != countInt2)
                            		{
                                		String query5 = "select proj_progress from a3action where proj_id = '"+projID+"'";
                                    	PreparedStatement preparedStatement5 = con.prepareStatement(query5);
                                        ResultSet RS5 = preparedStatement5.executeQuery();
                                        
                                        if(RS5.next())
                                        {
                                        	List<Integer> progress = new ArrayList<Integer>();
                                            progress.add(RS5.getInt("proj_progress"));
                                            
                                            double sum = 0;
                                            for(int i = 0; i < progress.size(); i++)
                                            {
                                                sum += progress.get(i);
                                            }
                                            double final_sum = sum/progress.size();
                                            
                                            String query4 = "update a3projects set proj_status = 1, proj_progress = '"+final_sum+"' where proj_id='"+projID+"'";
                                			
                                            PreparedStatement preparedStatement4 = con.prepareStatement(query4);
                                            preparedStatement4.executeUpdate();
                                        }
                                		
                            			
                            		}
                                }
                                
                            		
                            }
                    	}
                    	
                    	if(leadint == ID)
                    	{
                    		updateA3Now.setVisibility(View.VISIBLE);
                    	}else if(leadint != ID)
                    	{
                    		updateA3Now.setVisibility(View.GONE);
                    	}
                    }
                    
                }
            } catch (Exception ex) {
                
                Log.e("MYAPP", "exception", ex);
            }
			
		 

		 
		 	
		String projName = (a3bundle.getString("a3Name")).replace("<p>", "").replace("</p>", ""); 
		String projBackground = (a3bundle.getString("a3Background")).replace("<p>", "").replace("</p>", "");
		String projRecommendation = (a3bundle.getString("a3Recommendation")).replace("<p>", "").replace("</p>", "");
		String projVision = (a3bundle.getString("a3Vision")).replace("<p>", "").replace("</p>", "");
		String projIssues = (a3bundle.getString("a3Issues")).replace("<p>", "").replace("</p>", "");
		
		projName2.setText(projName);
		projBackground2.setText(projBackground);
		projRecommendation2.setText(projRecommendation);
		projVision2.setText(projVision);
		projIssues2.setText(projIssues);
		
		
		
        /*
		if(UserType == 3)
        {
			approveNow.setVisibility(View.GONE);
        	
        }else if(UserType == 2)
        {
        	approveNow.setVisibility(View.VISIBLE);
        }
		*/
        
		updateA3Now.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
            	UpdateA3 updateA3Now = new UpdateA3();
            	updateA3Now.execute("");

            }
        });
		
		addAction.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
            	Bundle actionPlan = new Bundle();
                
            	actionPlan.putString("a3IDD", projID);
            

                Intent ActionPlanView = new Intent(A3UpdateMain.this, A3ActionPlanView.class);
                ActionPlanView.putExtras(actionPlan);
            	startActivity(ActionPlanView);
                

            }
        });
		
		addCriteria.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
            	Bundle actionPlan = new Bundle();
                
            	actionPlan.putString("a3IDD", projID);
            

                Intent ActionPlanCriteria = new Intent(A3UpdateMain.this, A3SuccessCriteriaView.class);
                ActionPlanCriteria.putExtras(actionPlan);
            	startActivity(ActionPlanCriteria);
                

            }
        });
		
		
		addComments.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
            	Bundle actionPlan = new Bundle();
                
            	actionPlan.putString("a3IDD", projID);
            

                Intent A3Comments = new Intent(A3UpdateMain.this, A3Comment.class);
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
			
		
	}
	
	public class UpdateA3 extends AsyncTask<String, String, String> {

		Bundle a3bundle = getIntent().getExtras();
		String projID = a3bundle.getString("a3ID"); 
        boolean isSuccess = false;
        String z = "";
        
        String projNameUp = projName2.getText().toString();
        String projBackgroundUp = projBackground2.getText().toString();
        String projRecommendationUp = projRecommendation2.getText().toString();
        String projVisionUp = projVision2.getText().toString();
        String projIssuesUp = projIssues2.getText().toString();
  
        
        @Override
        protected void onPreExecute() {

        }
        
        @Override
        protected void onPostExecute(String r) {
        	Toast.makeText(A3UpdateMain.this, r, Toast.LENGTH_SHORT).show();
            
        }
        @Override
        protected String doInBackground(String... params) {
        	if (projID.equals(""))
	            z = "Please fill all the fields";
            else {
            	
                try {
                    Connection con = connectionClass.CONN();
                    if (con == null) {
                        z = "Error in connection with SQL server";
                    } else {

						String query = "Update a3projects set proj_title='"+projNameUp+"',proj_background='"+projBackgroundUp+"'" +
                        ",proj_recommendation='"+projRecommendationUp+"',proj_issue='"+projIssuesUp+"',proj_vision='"+projVisionUp+"' WHERE proj_id ='"+projID+"'";

                        PreparedStatement preparedStatement = con.prepareStatement(query);
                        preparedStatement.executeUpdate();
                        z = "Updated Successfully";

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
	
public class ApproveA3 extends AsyncTask<String, String, String> {
	
        String z = "";
        Boolean isSuccess = false;
        @Override
        protected void onPreExecute() {
       
        }
        @Override
        protected void onPostExecute(String r) {
  
            Toast.makeText(A3UpdateMain.this, r, Toast.LENGTH_SHORT).show();
            if(isSuccess==true) {
            	approveNow.setVisibility(View.GONE);
            }
        }
        @Override
        protected String doInBackground(String... params) {
                try {
                    Connection con = connectionClass.CONN();
                    if (con == null) {
                        z = "Error in connection with SQL server";
                    } else {
                        String query = "Update a3projects set proj_approved='"+ 2 +"' where proj_id="+projID;
                        PreparedStatement preparedStatement = con.prepareStatement(query);
                        preparedStatement.executeUpdate();
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
    

	public void CheckStatus()
	{
		connectionClass = new ConnectionClass();
		try {
            Connection con = connectionClass.CONN();
            if (con == null) {
               
            } else {
                String query = "select proj_status from a3projects where proj_id = 79";
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                
                if(rs.next())
                {
                	String status = rs.getString("proj_status");
                	int statusInt = Integer.parseInt(status);
                	
                	if(statusInt != 13)
                	{
                		actionPlan.setVisibility(LinearLayout.GONE);
                		Criteria.setVisibility(LinearLayout.GONE);
                	}else if(statusInt == 13)
                	{
                		actionPlan.setVisibility(LinearLayout.VISIBLE);
                		Criteria.setVisibility(LinearLayout.VISIBLE);
                	}
                }
                
            }
        } catch (Exception ex) {
            
            Log.e("MYAPP", "exception", ex);
        }
	}
	
	public void checkOwner()
	{
		connectionClass = new ConnectionClass();
		try {
            Connection con = connectionClass.CONN();
            if (con == null) {
               
            } else {
                String query = "select proj_lead from a3projects where proj_id = 79";
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                
                if(rs.next())
                {
                	String owner = rs.getString("proj_lead");
                	
                	
                	if(owner.equals(userID))
                	{
                		updateA3Now.setVisibility(Button.VISIBLE);
                		
                	}else 
                	{
                		updateA3Now.setVisibility(Button.GONE);
                	}
                }
                
            }
        } catch (Exception ex) {
            
            Log.e("MYAPP", "exception", ex);
        }
	}
	
	public void checkDirector()
	{
		connectionClass = new ConnectionClass();
		try {
            Connection con = connectionClass.CONN();
            if (con == null) {
               
            } else {
                String query = "select user_type from users where user_id = '"+userID+"'";
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                
                if(rs.next())
                {
                	String type = rs.getString("user_type");
                	int typeInt = Integer.parseInt(type);
                	
                	
                	if(typeInt == 2)
                	{
                		approveNow.setVisibility(Button.VISIBLE);
                		
                	}else if (typeInt == 3)
                	{
                		approveNow.setVisibility(Button.GONE);
                	}else
                	{
                		approveNow.setVisibility(Button.VISIBLE);
                	}
                }
                
            }
        } catch (Exception ex) {
            
            Log.e("MYAPP", "exception", ex);
        }
	}

}
