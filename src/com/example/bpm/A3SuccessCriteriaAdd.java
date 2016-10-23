package com.example.bpm;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
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
import android.widget.Toast;

public class A3SuccessCriteriaAdd extends Activity{
	
	
	Button addCriteria;
	ConnectionClass connectionClass;
	EditText edtDesc,edtCurr,edtTarg;
	
	int UserType;
	String projID;
	
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.a3_successcriteria_edit);

		 addCriteria = (Button) findViewById(R.id.btnAddCriteriaNow);
		 edtDesc = (EditText) findViewById(R.id.edtCriteriaDesc);
		 edtCurr = (EditText) findViewById(R.id.edtCriteriaCurrent);
		 edtTarg = (EditText) findViewById(R.id.edtCriteriaTarget);
		 
		 
		 
		 connectionClass = new ConnectionClass();
		 
		 Bundle criteriaMain = getIntent().getExtras();
			
		projID = criteriaMain.getString("ProjID");	

        addCriteria.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
            	AddCriteriaNow addCriteriaN = new AddCriteriaNow();
            	addCriteriaN.execute("");

            }
        });
		
	}
	
	public class AddCriteriaNow extends AsyncTask<String, String, String> {

        boolean isSuccess = false;
        String z = "";
        
        String Description = edtDesc.getText().toString();
        String CurrentString = edtCurr.getText().toString();
        String TargetString = edtTarg.getText().toString();
        
        int CurrentInt = Integer.parseInt(CurrentString);
        int TargetInt = Integer.parseInt(TargetString);
  
        
        @Override
        protected void onPreExecute() {

        }
        
        @Override
        protected void onPostExecute(String r) {
        	Toast.makeText(A3SuccessCriteriaAdd.this, r, Toast.LENGTH_SHORT).show();
            finish();
        }
        @Override
        protected String doInBackground(String... params) {
        	if (edtDesc.equals("") || edtDesc.equals("") || edtTarg.equals("") || CurrentInt>100 || TargetInt>100)
	            z = "Please fill all the fields";
            else {
            	
                try {
                    Connection con = connectionClass.CONN();
                    if (con == null) {
                        z = "Error in connection with SQL server";
                    } else {

						String query = "insert into a3metric(metric_desc,metric_curr,metric_target,proj_id)" +
								"values('"+Description+"','"+CurrentInt+"','"+TargetInt+"','"+projID+"')";

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
            }
            return z;
        }
    }
    

}
