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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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



public class OkrChrAdd extends Fragment {
	
	ConnectionClass connectionClass;
    EditText ChrObjective,ChrDepartments;
    Button AddObj, AddDept; 
    String depts,dept_name,deptsTrim;
    
    public String dis;
    List<Integer> deptNum;
    
	public OkrChrAdd(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		 
        View rootView = inflater.inflate(R.layout.okr_chr, container, false); 
        
        
        connectionClass = new ConnectionClass();
        AddObj = (Button) rootView.findViewById(R.id.btnAddObj);
        AddDept = (Button) rootView.findViewById(R.id.btnDept);
        ChrObjective = (EditText) rootView.findViewById(R.id.edtChrObj);
        ChrDepartments = (EditText) rootView.findViewById(R.id.edtDepartments);
 
        
        
        AddObj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            	AddObjs AddNow = new AddObjs();
            	AddNow.execute("");
            	
                //Toast.makeText(A3Add.this.getActivity(), haha, Toast.LENGTH_SHORT).show();
            	
                
            }
        });
        
        AddDept.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent add_dept = new Intent(getActivity(),AddDepartmentActivity.class);
				startActivity(add_dept);
				
				
			}
		});

        
        return rootView;
        
    }
	
	/*
	@Override
	public void onResume()
	{
		super.onResume();
        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref",Context. MODE_PRIVATE); 
        Editor editor = pref.edit();
        depts = pref.getString("departments", null); 
        if(depts.isEmpty())
        {
        	Toast.makeText(getActivity(), "depts is empty", Toast.LENGTH_SHORT).show();
        }else
        {
		deptsTrim =  String.valueOf(depts.replace("[","").replace("]",""));
        ChrDepartments.setText(deptsTrim);
        }
	}
	*/
	
public class AddObjs extends AsyncTask<String, String, String> {

        String z = "";
        Boolean isSuccess = false;
        
        String objective = ChrObjective.getText().toString();

        
        @Override
        protected void onPreExecute() {
            
        }

        @Override
        protected void onPostExecute(String r) {
            Toast.makeText(OkrChrAdd.this.getActivity(), r, Toast.LENGTH_SHORT).show();
            if(isSuccess==true) {
            	ChrObjective.setText(null);
            	ChrDepartments.setText(null);
            }

        }

		@Override
		protected String doInBackground(String... params) {
			if (ChrObjective.equals("") || ChrDepartments.equals(""))
	            z = "Please fill all the fields";
	        else {
	        	try {
	                Connection con = connectionClass.CONN();
	                if (con == null) {
	                    z = "Error in connection with SQL server";
	                } else {
	                	
	                	Calendar calendar = Calendar.getInstance();
	                	int year = calendar.get(Calendar.YEAR);
	                	
	                	String objective = ChrObjective.getText().toString();
	                	
	                	String query = "insert into chrheadobj (obj_desc,okr_year)" + 
	                			"values ('" +objective+ "','"+year+"')";                                            
	                	PreparedStatement preparedStatement = con.prepareStatement(query);
	                    preparedStatement.executeUpdate();
	                    z = "Added Successfully";
	                    isSuccess = true;
	                }
	            } catch (Exception ex) {
	                isSuccess = false;
	                z = "Exceptions";
	                Log.e("MYAPP", "exception", ex);
	            }
	        	
	            try {
	                Connection con = connectionClass.CONN();
	                if (con == null) {
	                    z = "Error in connection with SQL server";
	                } else {
	                    
	                    ArrayList<String> dept_names = new ArrayList<String>();
                    	
                    	SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref",Context. MODE_PRIVATE); 
                        Editor editor = pref.edit();
                        depts = pref.getString("departments", null); 
                        
          
                        dis = String.valueOf(depts.replace("[","").replace("]",""));
                        
                        dept_names.add(dis);
                        
                        String code = "";
                        
                        int code_id;
                        
                        List<String> data12 = new ArrayList<String>(Arrays.asList(dis.replace("[","").replace("]","").split(",")));
                        deptNum = new ArrayList<Integer>();

                        for(String name : data12)
                        {
                        	code = name;  
                        	
                        	String objective = ChrObjective.getText().toString();
                        	
                        	String query6 = "select department_id from departments WHERE'"+code+"' LIKE Concat(Concat('%',department_code),'%')";
                        	  PreparedStatement select_id = con.prepareStatement(query6);
                        	  ResultSet rs = select_id.executeQuery();
                        	  

                        	  while(rs.next())
                        	  {
                        		  deptNum.add(rs.getInt("department_id"));
                        		  
                        		  //Toast.makeText(getActivity(), "IINSERT ID SUCCESS", Toast.LENGTH_LONG).show();
                        		  
                        	  }
                        }
                        
                        int deptID;
                        
                        
                        for(Integer broid : deptNum)
                        {
                        	deptID = broid;
                        	
                        	String query = "select obj_id from chrheadobj where obj_desc = '"+objective+"'";
                            PreparedStatement ps = con.prepareStatement(query);
                            ResultSet rs2 = ps.executeQuery();
                        	
                            while(rs2.next())
                            {
                            	
                            int obj_id = rs2.getInt("obj_id");
                            	
                                String query7 = "insert into csy_dept_obj(obj_id, dept_id)"+"values('"+obj_id+"','"+deptID+"')";
                                PreparedStatement insert_members = con.prepareStatement(query7);
                                insert_members.executeUpdate();
                            }  
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
	
	
	

    


