package com.example.bpm;

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

import com.example.bpm.PathfinderAdd.AddPro;
import com.example.bpm.PathfinderUpdate.FillList;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class PromotionDirActivityMain extends Activity{
	

	ConnectionClass connectionClass;
	TextView empName,empPosition,empPromote;
	EditText recommendation;
	Button submitRequest;
	
	String userID,position;
	
	public PromotionDirActivityMain(){}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.promote_dir_main);
		
		empName = (TextView) findViewById(R.id.tvName);
		empPosition = (TextView) findViewById(R.id.tvPosition);
		empPromote = (TextView) findViewById(R.id.tvPromoteTo);
		
		recommendation = (EditText) findViewById(R.id.edtRecommendation);
		submitRequest = (Button) findViewById(R.id.btnPromoteRequest);
		
		
		Bundle bundle = getIntent().getExtras();

		String EmpName = bundle.getString("empName"); 
		userID = bundle.getString("ID");
		position = bundle.getString("pos");

		empName.setText(EmpName);
		SetCurrPos();
		SetPromoteTo();
        connectionClass = new ConnectionClass();


      
        
        submitRequest.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
            	SendRequest approve = new SendRequest();
            	approve.execute("");

            }
        });
        

    }
	
	public void SetCurrPos()
	{
		connectionClass = new ConnectionClass();
		try {
            Connection con = connectionClass.CONN();
            if (con == null) {
            	Toast.makeText(this, "Error in connection with SQL server", Toast.LENGTH_SHORT).show();
                
            } else {
                String query = "select * from position where position_value = '"+position+"'";
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();

               ;
                while (rs.next()) {
                	String curr_pos = rs.getString("position_name");
                	empPosition.setText(curr_pos);
                	
                    
                }


               
            }
        } catch (Exception ex) {
           
            Log.e("MYAPP", "exception", ex);

        }
	}
	
	public void SetPromoteTo()
	{
		connectionClass = new ConnectionClass();
		try {
            Connection con = connectionClass.CONN();
            if (con == null) {
            	Toast.makeText(this, "Error in connection with SQL server", Toast.LENGTH_SHORT).show();
                
            } else {
            	int nextPos = (Integer.parseInt(position) - 1);
            	
                String query = "select * from position where position_value = '"+nextPos+"'";
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();

               ;
                while (rs.next()) {
                	String next_pos = rs.getString("position_name");
                	empPromote.setText(next_pos);
                	
                    
                }


               
            }
        } catch (Exception ex) {
           
            Log.e("MYAPP", "exception", ex);

        }
	}
	
	
	public class SendRequest extends AsyncTask<String, String, String> {


		int nextPos = (Integer.parseInt(position) - 1);
		String rec = recommendation.getText().toString();
		
		SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref",Context. MODE_PRIVATE); 
        Editor editor = pref.edit();
        String ID = pref.getString("IDD", null); 
        
        String z = "";
        Boolean isSuccess = false;
        
        

        
        @Override
        protected void onPreExecute() {
            
        }
        
        @Override
        protected void onPostExecute(String r) {
            
            Toast.makeText(PromotionDirActivityMain.this, r, Toast.LENGTH_SHORT).show();
            if(isSuccess==true) {
            	recommendation.setText(null);
            	finish();
            }
        }

       

        @Override
        protected String doInBackground(String... params) {
        	if (recommendation.equals(""))
	            z = "Please fill all the fields";
            else {
            	
                try {
                    Connection con = connectionClass.CONN();
                    if (con == null) {
                        z = "Error in connection with SQL server";
                    } else {

                       
						String query = "insert into promotion(employee_id,promote_to,recommendation,status,endorsed_by) values('"+userID+"','"+nextPos+"','"+rec+"',0,'"+ID+"')";

                        PreparedStatement preparedStatement = con.prepareStatement(query);
                        preparedStatement.executeUpdate();
                        z = "Request sent Successfully";

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


