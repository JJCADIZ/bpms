package com.example.bpm;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.renderscript.Element.DataType;
import android.support.v13.app.FragmentCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {
	
 ConnectionClass connectionClass;
 EditText edtuserid,edtpass;
 Button btnlogin;
 ProgressBar pbbar;

 public int test2;
 public String tesla = "";
 String user_fname;
 String user_lname;
 int dept_id;
 String test;
 String user_email;
 String user_password;
 //String user_username;
 Bundle profileBundle;
 String userid;
 String password;
 int userIDD;
 int userType;
 int qwe = 5;
UserSessionManager session;
Handler handler = new Handler();
ProgressDialog loading;
 

 @Override
 protected void onCreate(Bundle savedInstanceState) {
 super.onCreate(savedInstanceState);
 setContentView(R.layout.activity_main);
 connectionClass = new ConnectionClass();
 edtuserid = (EditText) findViewById(R.id.edtuserid);
 edtpass = (EditText) findViewById(R.id.edtpass);
 btnlogin = (Button) findViewById(R.id.btnlogin);
 pbbar = (ProgressBar) findViewById(R.id.pbbar);
 pbbar.setVisibility(View.GONE);
 
 session = new UserSessionManager(getApplicationContext());

 Toast.makeText(getApplicationContext(), 
 		"User Login Status: " + session.isUserLoggedIn(), 
 		Toast.LENGTH_LONG).show();
 
 		if(session.isUserLoggedIn())
 		{
 			SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref",Context. MODE_PRIVATE);
 			handler.post(goBackgroundService);
 			//startService(new Intent(this, NotifyService.class));
 		     session.createUserLoginSession("Android Example", "androidexample84@gmail.com");
 			try {
 				Connection con = connectionClass.CONN();
 				 if (con == null) {
 					Toast.makeText(getApplicationContext(), "Error in connection", Toast.LENGTH_SHORT).show();
 				 } else {
	    	         int id = Integer.parseInt(pref.getString("IDD", null));
 					 String query = "select evaluationscore_id from evaluationscore where evaluated_by = '"+id+"' and year = YEAR(getdate())";
 					 Statement stmt = con.createStatement();
 					 ResultSet rs = stmt.executeQuery(query);
 					 int i = 0;
 					 while(rs.next()){
 						 i++;
 					 }
 					 String getSettings = "select setting_value from setting where setting_name = 'per_eval_status'";
 					 ResultSet rs1 = stmt.executeQuery(getSettings);
 					 if(rs1.next()){
 						if(rs1.getString("setting_value").equalsIgnoreCase("0") || i > 0 ){
 	 						// Starting MainActivity
 	 			 			Intent x = new Intent(getApplicationContext(), OtherActivity.class);
 	 			 			x.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
 	 			 			
 	 			 			// Add new Flag to start new Activity
 	 			 			x.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
 	 			 			startActivity(x);
 	 			 			
 	 			 			finish();
 	 					 }else{
 	 						// Starting MainActivity
 	 			 			Intent x = new Intent(getApplicationContext(), PerformanceEvaluation.class);
 	 			 			x.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
 	 			 			
 	 			 			// Add new Flag to start new Activity
 	 			 			x.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
 	 			 			startActivity(x);
 	 			 			
 	 			 			finish();
 	 					 }
 					 }
 				 }
			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
			}
 		}

 btnlogin.setOnClickListener(new View.OnClickListener() {
 @Override
 public void onClick(View v) {
	 userid = edtuserid.getText().toString();
	 password = edtpass.getText().toString();
	 
	 SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE); 
	 Editor editor = pref.edit();
	 editor.putString("IDD", userid );  

	 editor.commit(); // commit changes
	 

 DoLogin doLogin = new DoLogin();
 doLogin.execute("");
 
 }
 });
 }
 
 
 public class DoLogin extends AsyncTask<String,String,String>
 {
int b;	 
 String z = "";
 Boolean isSuccess = false;
 @Override
 public void onPreExecute() {
 pbbar.setVisibility(View.VISIBLE);
 }
 @Override
 public void onPostExecute(String r) {
 pbbar.setVisibility(View.GONE);
 

 if(isSuccess) {
	 handler.post(goBackgroundService);
	 //DITO ANG REDIRECTION
     session.createUserLoginSession("Android Example", "androidexample84@gmail.com");
     try {
			Connection con = connectionClass.CONN();
			 if (con == null) {
				Toast.makeText(getApplicationContext(), "Error in connection", Toast.LENGTH_SHORT).show();
			 } else {
				 SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref",Context. MODE_PRIVATE);
				 int id = Integer.parseInt(pref.getString("IDD", null));
				 String query = "select evaluationscore_id from evaluationscore where evaluated_by = '"+id+"' and year = YEAR(getdate())";
				 Statement stmt = con.createStatement();
				 ResultSet rs = stmt.executeQuery(query);
				 int i = 0;
				 while(rs.next()){
					 i++;
				 }
				 String getSetting = "select setting_value from setting where setting_name = 'per_eval_status'";
				 ResultSet rs1 = stmt.executeQuery(getSetting);
				 if(rs1.next()){
						if(rs1.getString("setting_value").equalsIgnoreCase("0") || i > 0 ){
	 						// Starting MainActivity
	 			 			Intent x = new Intent(getApplicationContext(), OtherActivity.class);
	 			 			x.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	 			 			
	 			 			// Add new Flag to start new Activity
	 			 			x.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	 			 			startActivity(x);
	 			 			
	 			 			finish();
	 					 }else{
	 						// Starting MainActivity
	 			 			Intent x = new Intent(getApplicationContext(), PerformanceEvaluation.class);
	 			 			x.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	 			 			
	 			 			// Add new Flag to start new Activity
	 			 			x.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	 			 			startActivity(x);
	 			 			
	 			 			finish();
	 					 }
					 }
			 }
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
		}
     
 }
 }
 @Override
 public String doInBackground(String... params) {
	 
 if(userid.trim().equals("")|| password.trim().equals(""))
 z = "Please enter User Id and Password";
 else
 {
 try {
 Connection con = connectionClass.CONN();
 if (con == null) {
 z = "Error in connection with SQL server";
 } else {
	 
 String query = "select user_id, user_fname, user_type, user_lname, department_id, user_email, user_password from users where user_status = 1 and user_type != 0 and user_id='" + userid + "' and user_password='" + password + "'";
 Statement stmt = con.createStatement();
 ResultSet rs = stmt.executeQuery(query);
 if(rs.next())
 {
	 	userIDD = rs.getInt(1);
	 	
	 	String wUsr="";
	 	String deptId = "";
	 	
	 	deptId = rs.getString("department_id");
	 	wUsr = rs.getString("user_type");
	 	dept_id = Integer.parseInt(deptId);
	 	userType = Integer.parseInt(wUsr);
	 	/*
	    user_fname = rs.getString(2);
	    user_lname = rs.getString(3);
	    dept_id = rs.getInt(4);
	    user_email = rs.getString(5);
	    user_password = rs.getString(6);
	    user_username = rs.getString(7);
	    */
	 	 SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE); 
		 Editor editor = pref.edit();
		 editor.putInt("usrtp", userType );
		 editor.putInt("userID", userIDD );
		 editor.putInt("DeptID", dept_id );  
		 editor.commit();
	    
	 z = "The ID is " + userIDD;
	
 isSuccess=true;
 }
 else
 {
 z = "Invalid Credentials";
 isSuccess = false;
 }
 }
 }
 catch (Exception ex)
 {
 isSuccess = false;
 z = "Error Somewhere";
 Log.e("MYAPP", "exception", ex);
 }
 }
 return z;
 }
 
 
 }
 
 	public String getMyData() {
 		
 		String test4 = tesla;
 		return test4;

 }
 	
 private Runnable goBackgroundService = new Runnable() {
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		startService(new Intent(getApplicationContext(), NotifyService.class));
	}
};
 	
}
