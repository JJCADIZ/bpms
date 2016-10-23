package com.example.bpm;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

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



public class OkrDirAdd extends Fragment {

    ConnectionClass connectionClass;
    EditText edtObj;
    Button btnAdd;
    ListView lstob,lstobj;
    String objid;
    public String ID;
    
    Spinner impact;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
 
        View rootView = inflater.inflate(R.layout.okr_2, container, false); 
        

        connectionClass = new ConnectionClass();
        btnAdd = (Button) rootView.findViewById(R.id.btnAddOkrDir);
        lstobj = (ListView) rootView.findViewById(R.id.lstobjper);
        lstob = (ListView) rootView.findViewById(R.id.lstproducts);
        edtObj = (EditText) rootView.findViewById(R.id.edtAddObjDir);
        impact = (Spinner) rootView.findViewById(R.id.spinImpact);
        
        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref",Context. MODE_PRIVATE); 
        Editor editor = pref.edit();
        ID = pref.getString("IDD", null);

        FillList fillList = new FillList();
        fillList.execute("");
        
        
        btnAdd.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
            	
                FillList Fill = new FillList();
                Fill.execute("");
                
                AddObj Addobj = new AddObj();
                Addobj.execute("");

            }
        });
        


        
        return rootView;

    }
    
    @Override
    public void onResume(){
        super.onResume();
        FillList Fill = new FillList();
        Fill.execute("");

    }
    


    
    public class FillList extends AsyncTask<String, String, String> {
        String z = "";

        List<Map<String, String>> objlist  = new ArrayList<Map<String, String>>();

        @Override
        protected void onPreExecute() {

            //old pbbar
        }

        @Override
        protected void onPostExecute(String r) {
            String[] from = { "dir_obj_id", "obj_name"};
            int[] views = { R.id.lblprojid, R.id.lblprojtitle };
            final SimpleAdapter ADAP = new SimpleAdapter(getActivity(),
            		objlist, R.layout.lsttemplateokrdir, from,
                    views);
            lstobj.setAdapter(ADAP);
            lstobj.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
                    @SuppressWarnings("unchecked")
					HashMap<String, Object> a3obj = (HashMap<String, Object>) ADAP
                            .getItem(arg2);
                    objid = (String) a3obj.get("dir_obj_id");
                    String obj_name = (String) a3obj.get("obj_name");

                    
                    Bundle objBundle = new Bundle();
                    
                    objBundle.putString("objID", objid);
                    objBundle.putString("objName", obj_name);

                    
                    Intent KrDir = new Intent(getActivity(), OkrKeyDirActivity.class);
                    KrDir.putExtras(objBundle);
                	startActivity(KrDir);
                    
                    
                    
               //     qty.setText(qtys);
                }
            });



        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {
                    String query = "select * from directorobj where user_id ='"+ID+"' order by dir_obj_id ASC";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    ArrayList<String> data2 = new ArrayList<String>();
                    while (rs.next()) {
                        Map<String, String> datanum = new HashMap<String, String>();
                        datanum.put("dir_obj_id", rs.getString("dir_obj_id"));
                        datanum.put("obj_name", rs.getString("dir_obj_name"));
                        datanum.put("date_raised", rs.getString("dir_obj_raised"));
                        datanum.put("userID", rs.getString("user_id"));
                        datanum.put("obj_id", rs.getString("obj_id"));

                        objlist.add(datanum);
                    }


                    z = "Success";
                }
            } catch (Exception ex) {
                z = "Error retrieving data from table";
                Log.e("MYAPP", "exception", ex);

            }
            return z;
        }
    }
    
    
public class AddObj extends AsyncTask<String, String, String> {
		
	String Objective=edtObj.getText().toString();
	
		

        String z = "";
        Boolean isSuccess = false;
        
        String impakto = impact.getSelectedItem().toString();
        
        

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String r) {

            Toast.makeText(OkrDirAdd.this.getActivity(), r, Toast.LENGTH_SHORT).show();
            if(isSuccess==true) {
            	edtObj.setText(null);
            	
            	FillList Fill = new FillList();
                Fill.execute("");
                
                Toast.makeText(OkrDirAdd.this.getActivity(), "Add Success", Toast.LENGTH_SHORT).show();
            }

        }

		@Override
		protected String doInBackground(String... params) {
			if (edtObj.equals("")){
	            z = "Please fill all the fields";
	            isSuccess = false;
			}else {
	            try {
	                Connection con = connectionClass.CONN();
	                if (con == null) {
	                    z = "Error in connection with SQL server";
	                } else {
	                	
	                	String getOKR = "select okr_id from okr where user_id = '"+ID+"'";
	                    PreparedStatement ps2 = con.prepareStatement(getOKR);
	                    ResultSet rs2 = ps2.executeQuery();
	                    
	                    if(rs2.next())
	                    {
	                    	String okrID = rs2.getString("okr_id");
	                    	
	                    	String query = "Insert into directorobj(dir_obj_name,user_id,dir_obj_raised,dir_obj_impact,okr_id,dir_obj_status,dir_obj_subprocess_id)" + "Values('"+Objective+"','"+ID+"',GETDATE(),'"+impakto+"','"+okrID+"',1,3)";                                            
		                	PreparedStatement preparedStatement = con.prepareStatement(query);
		                    preparedStatement.executeUpdate();
		                    
	                    }
	                    else if(!rs2.next())
		                    {
		                    	String query2 = "Insert into okr(okr_datesent,user_id,okr_status,okr_progress,okr_eval,okr_year)" + "Values(GETDATE(),'"+ID+"',0,0,0,2016)";                                            
			                	PreparedStatement preparedStatement2 = con.prepareStatement(query2);
			                    preparedStatement2.executeUpdate();
			                	
			                    String getNewOKR = "select IDENT_CURRENT('okr')";
			                    PreparedStatement ps3 = con.prepareStatement(getNewOKR);
			                    ResultSet rs3 = ps3.executeQuery();
			                    
			                    if(rs3.next())
			                    {
			                    	String okrGetID = rs2.getString(1);
			                    	
			                    	String query4 = "Insert into directorobj(dir_obj_name,user_id,dir_obj_raised,dir_obj_impact,okr_id,dir_obj_status,dir_obj_subprocess_id)" + "Values('"+Objective+"','"+ID+"',GETDATE(),'"+impakto+"','"+okrGetID+"',1,3)";                                            
				                	PreparedStatement prep = con.prepareStatement(query4);
				                	prep.executeUpdate();
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
