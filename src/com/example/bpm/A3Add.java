package com.example.bpm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import com.example.bpm.PathfinderAdd.AddPro;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;



public class A3Add extends Fragment {
	
	ConnectionClass connectionClass;
    EditText edtprojname, edtbackground,edtrecommend,edtvision,edtissues;
    Button btnAddA3,btnMember,btnDirector;
    Spinner members,directors,coach;
    public String name,type,miyembro,dis,approver,approver_main;
    public int userID,userType,approveCount;
    PreparedStatement stmt1,stmt2,stmt3,getEmpID,getDirID,getCoachID,getProjID;
    ResultSet rs1,rs2,rs3,empID,dirID,coachID,projID;
    List<Integer> empNum;
    List<Integer> approverNum;
    
   public  String empFname;
   public  String empLname;
   public  String empName;
   public String headFname;
   public String headLname;
   public String headName;
   public String coachFname; 
   public String coachLname; 
   
   public String memStringID;
   public int memberID;
   
   public String dirStringID;
   public int directorID;
   
   public String coachStringID;
   public int projCoachID;
   
   public String projStringID;
   public int projIntID;
    
    //int idd = ((SetID) getActivity().getApplication()).getID();
    //((MainActivity) getActivity()).getData();
	
	public A3Add(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		 
        View rootView = inflater.inflate(R.layout.add_a3, container, false); 
        
        
        connectionClass = new ConnectionClass();
        edtprojname = (EditText) rootView.findViewById(R.id.edtProj);
        edtbackground = (EditText)  rootView.findViewById(R.id.edtBackground);
        edtrecommend = (EditText) rootView.findViewById(R.id.edtRecommendation);
        edtvision = (EditText) rootView.findViewById(R.id.edtVision);
        edtissues = (EditText) rootView.findViewById(R.id.edtIssues);
        coach = (Spinner) rootView.findViewById(R.id.spinCoach);
        btnAddA3 = (Button) rootView.findViewById(R.id.btnAddA3);
        btnMember = (Button) rootView.findViewById(R.id.btnAddMember);
        btnDirector = (Button) rootView.findViewById(R.id.btnAddDirector);
        
        
        
        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref",Context. MODE_PRIVATE); 
        Editor editor = pref.edit();
        name = pref.getString("IDD", null);        
        userID = Integer.parseInt(name);

        
        Toast.makeText(A3Add.this.getActivity(), name, Toast.LENGTH_SHORT).show();
        
        connectionClass = new ConnectionClass();
        String emp = "select user_fname,user_lname from users where user_type=3";
        String head = "select user_fname,user_lname from users where user_type = 2";
        //String chr = "select user_fname,user_lname from users where user_type = 0";
        try {
        	Connection con = connectionClass.CONN();
        	
            stmt1 = con.prepareStatement(emp);
            stmt2 = con.prepareStatement(head);
            
            rs1 = stmt1.executeQuery();
            rs2 = stmt2.executeQuery();
            
            ArrayList<String> empData = new ArrayList<String>();
            ArrayList<String> headData = new ArrayList<String>();
            ArrayList<String> chrData = new ArrayList<String>();
            
            while (rs1.next() && rs2.next()) {
                 empFname = rs1.getString("user_fname");
                 empLname = rs1.getString("user_lname");
                 empName = String.valueOf(empFname)+" "+String.valueOf(empLname); 
                
                
                 headFname = rs2.getString("user_fname");
                 headLname = rs2.getString("user_lname");
                 headName = String.valueOf(headFname)+" "+String.valueOf(headLname); 
                 
                 coachFname = rs2.getString("user_fname");
                 coachLname = rs2.getString("user_lname");
               
                empData.add(empName);
                headData.add(headName);
                chrData.add(headName);
            }
            
            String[] empArray = empData.toArray(new String[0]);
            String[] headArray = headData.toArray(new String[0]);

            
            ArrayAdapter empAdapter = new ArrayAdapter(getActivity(),
                    android.R.layout.simple_list_item_1, empData);
            
            
            ArrayAdapter headAdapter = new ArrayAdapter(getActivity(),
                    android.R.layout.simple_list_item_1, headData);

            
           
            coach.setAdapter(headAdapter);
            
        } catch (SQLException ex) {
            ex.printStackTrace();
            Log.e("MYAPP", "exception", ex);
        }
        
        btnAddA3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            	AddA3Dir AddNow = new AddA3Dir();
            	AddNow.execute("");
            	
            }
        });
        
        btnMember.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent add_member = new Intent(getActivity(),AddMemberActivity.class);
				startActivity(add_member);

				
			}
		});
        
        btnDirector.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent add_approver = new Intent(getActivity(),AddApprovers.class);
				startActivity(add_approver);

				
			}
		});
        
        
        return rootView;
        
    }
	
	
	
public class AddA3Dir extends AsyncTask<String, String, String> {

        String z = "";
        Boolean isSuccess = false;
        
        String projName = edtprojname.getText().toString();
        String background = edtbackground.getText().toString();
        String recommendation = edtrecommend.getText().toString();
        String vision = edtvision.getText().toString();
        String issues = edtissues.getText().toString();

        String projCoach = coach.getSelectedItem().toString();

        
        @Override
        protected void onPreExecute() {
            
        }

        @Override
        protected void onPostExecute(String r) {
            Toast.makeText(A3Add.this.getActivity(), r, Toast.LENGTH_SHORT).show();
            Toast.makeText(getActivity(), projStringID, Toast.LENGTH_SHORT).show();
            if(isSuccess==true) {
            	edtprojname.setText(null);
            	edtbackground.setText(null);
            	edtrecommend.setText(null);
            	edtvision.setText(null);
            	edtissues.setText(null);

            }

        }

		@Override
		protected String doInBackground(String... params) {
			if (projName.trim().equals("") || background.trim().equals("")  || recommendation.trim().equals("") || vision.trim().equals("") || issues.trim().equals(""))
	            z = "Please fill all the fields";
	        else {
	            try {
	                Connection con = connectionClass.CONN();
	                if (con == null) {
	                    z = "Error in connection with SQL server";
	                } else {
	                	
	                	
	                	String query3 ="select * from users WHERE'"+projCoach+"' LIKE Concat(Concat('%',user_fname),'%') AND '"+projCoach+"' LIKE  Concat(Concat('%',user_lname),'%')";
	                	
	                	
	                	getCoachID = con.prepareStatement(query3);
	                	
	                	
	                	
	                	coachID = getCoachID.executeQuery();
	                	
	                	while(coachID.next())
	                	{
	                		
	                		coachStringID = coachID.getString("user_id");
	                		
	                		
	                		projCoachID = Integer.parseInt(coachStringID);
	                	
	                	
	                	
	                		SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref",Context. MODE_PRIVATE); 
	                        Editor editor = pref.edit();
	                        approveCount = pref.getInt("counter", 0);
	                	
	                	String query ="insert into a3projects(proj_title,proj_lead,proj_vision,proj_recommendation,proj_background,proj_issue,proj_coach,proj_status,proj_approvers,proj_approved,proj_dateupdate,proj_declined,proj_progress,proj_subprocess_id)"+
	                			"VALUES('"+projName+"','"+userID+"','<p>"+vision+"</p>','<p>"+recommendation+"</p>','<p>"+background+"</p>','<p>"+issues+"</p>','"+projCoachID+"',10,'"+approveCount+"',0,SYSDATETIME(),0,0,37)";                                              
	                	
	                	
	                	PreparedStatement preparedStatement = con.prepareStatement(query);
	                    preparedStatement.executeUpdate();
	                	}
	                 
	                    
	                    String query4 ="select proj_id from a3projects WHERE proj_title='"+projName+"' AND proj_lead='"+userID+"' AND proj_vision LIKE'<p>"+vision+"</p>' AND proj_recommendation LIKE'<p>"+recommendation+"</p>' AND proj_background LIKE'<p>"+background+"</p>' AND proj_issue LIKE'<p>"+issues+"</p>' AND proj_coach='"+projCoachID+"'";
	                	
	                	getProjID = con.prepareStatement(query4);
	                	projID = getProjID.executeQuery();
	                	
	                	}
	                	
	                	while(projID.next())
	                	{
	                		projStringID = projID.getString("proj_id");
	                		projIntID = Integer.parseInt(projStringID);
	                		
	                		
	                	}
	                    
	                    //INSERT MEMBER INTO A3 TEAM
	                    
	                    ArrayList<String> member_names = new ArrayList<String>();
	                    ArrayList<String> approver_names = new ArrayList<String>();
                    	
                    	SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref",Context. MODE_PRIVATE); 
                        Editor editor = pref.edit();
                        miyembro = pref.getString("member", null); 
                        approver = pref.getString("approver", null);
                        approveCount = pref.getInt("count", 0);
                        
                        approver_main = String.valueOf(approver.replace("[","").replace("]",""));
                        dis = String.valueOf(miyembro.replace("[","").replace("]",""));
                        
                        member_names.add(dis);
                        approver_names.add(approver_main);
                        
                        String names = "";
                        String Approver = "";
                        
                        int lols;
                        
                        List<String> data12 = new ArrayList<String>(Arrays.asList(dis.replace("[","").replace("]","").split(",")));
                        List<String> data13 = new ArrayList<String>(Arrays.asList(approver_main.replace("[","").replace("]","").split(",")));
                        empNum = new ArrayList<Integer>();
                        approverNum = new ArrayList<Integer>();
                        
                        for(String name : data13)
                        {
                        	Approver = name;  
                        	
                        	
                        	String query6 = "select user_id from users WHERE'"+Approver+"' LIKE Concat(Concat('%',user_fname),'%') AND '"+Approver+"' LIKE  Concat(Concat('%',user_lname),'%')";
                        	  PreparedStatement select_id = con.prepareStatement(query6);
                        	  ResultSet rs = select_id.executeQuery();
                        	  
                        	  while(rs.next())
                        	  {
                        		  approverNum.add(rs.getInt("user_id"));
                        		  
                        		  //Toast.makeText(getActivity(), "IINSERT ID SUCCESS", Toast.LENGTH_LONG).show();
                        		  
                        	  }
                        }
	                    
                        for(String name : data12)
                        {
                        	names = name;  
                        	
                        	
                        	String query6 = "select user_id from users WHERE'"+names+"' LIKE Concat(Concat('%',user_fname),'%') AND '"+names+"' LIKE  Concat(Concat('%',user_lname),'%')";
                        	  PreparedStatement select_id = con.prepareStatement(query6);
                        	  ResultSet rs = select_id.executeQuery();
                        	  
                        	  while(rs.next())
                        	  {
                        		  empNum.add(rs.getInt("user_id"));
                        		  
                        		  //Toast.makeText(getActivity(), "IINSERT ID SUCCESS", Toast.LENGTH_LONG).show();
                        		  
                        	  }
                        }
                        
                        
                        for(Integer broid : empNum)
                        {
                        	lols = broid;
                        String query7 = "insert into a3team(proj_id,user_id)"+"values('"+projIntID+"', '"+lols+"')";
                                PreparedStatement insert_members = con.prepareStatement(query7);
                                insert_members.executeUpdate();
                                
                        }
                        
                        
                        
                        for(Integer appID : approverNum)
                        {
                        	lols = appID;
                        String query7 = "insert into a3team(proj_id,user_id,approved)"+"values('"+projIntID+"', '"+lols+"',0)";
                                PreparedStatement insert_members = con.prepareStatement(query7);
                                insert_members.executeUpdate();
                                
                        }
	                    
	                    
	                    
	                    z = "Added Successfully";
	                    isSuccess = true;
	                
	            } catch (Exception ex) {
	                isSuccess = false;
	                z = "Exceptions";
	                Log.e("MYAPP", "exception", ex);
	            }
	            
	        

	        return z;
	    }
			return z;
		}
	}
}
	
	
