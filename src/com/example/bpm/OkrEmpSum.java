package com.example.bpm;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;

import com.example.bpm.MainActivity.DoLogin;
import com.example.bpm.PathfinderAdd.AddPro;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;



public class OkrEmpSum extends Fragment {
	
	TextView year,objleft,dept,director;
	ConnectionClass connectionClass;
	
	public int user_id,dept_id,years,counter;
	public String count,depart,dirFname,dirLname,dirFull,deptName,depname;
	
	
	
	
	public OkrEmpSum(){}
	
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.okr_1, container, false);
        year = (TextView) rootView.findViewById(R.id.tvyear);
        objleft = (TextView) rootView.findViewById(R.id.tvobjleft);
        dept = (TextView) rootView.findViewById(R.id.tvdept);
        
        
        
        connectionClass = new ConnectionClass();

        
        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref",Context. MODE_PRIVATE); 
        Editor editor = pref.edit();
        user_id = pref.getInt("userID", 0);
        dept_id = pref.getInt("DeptID", 0); 
        //deptName = pref.getString("DeptName", null);

        DoFetch Fetch = new DoFetch();
        Fetch.execute("");

        return rootView;
    }
	
	@Override
	public void onResume(){
		
	    super.onResume();

        DoFetch Fetch = new DoFetch();
        Fetch.execute("");
        
        try {
       	 Connection con = connectionClass.CONN();
       	 if (con == null) {

       	 } else {


       	 
       	 String query2 = "SELECT COUNT(user_id) FROM employeeobj where user_id = '"+user_id+"'";
       	 Statement stmt2 = con.createStatement();
       	 ResultSet rs2 = stmt2.executeQuery(query2);
       	 
       	 if (rs2.next())
       	 {
       		 count = rs2.getString(1);
       		objleft.setText(count);
       	 }

       	 }
       	 }
       	 catch (Exception ex)
       	 {
       	 
       	 Log.e("MYAPP", "exception", ex);
       	 }

	}
	
	@Override
	public void onStart(){
		
	    super.onStart();

        DoFetch Fetch = new DoFetch();
        Fetch.execute("");

	}


	
	
	public class DoFetch extends AsyncTask<String,String,String>
	 { 
	 String z = "";
	 Boolean isSuccess = false;
	 @Override
	 public void onPreExecute() {
		 
		 
	 }
	 @Override
	 public void onPostExecute(String r) {

	
		 
	        Calendar c = Calendar.getInstance();
	        int yeared = c.get(Calendar.YEAR);
	        
	        Connection con = connectionClass.CONN();
	    
	        	
	        
	        String year_current = String.valueOf(yeared);
	   	 
	        year.setText(year_current);
	        objleft.setText(count);
	        dept.setText(depname);
		 
	
	 }
	 @Override
	 public String doInBackground(String... params) {
		 
	        
	
	 z = "Fetch Error";

	 {
	 try {
	 Connection con = connectionClass.CONN();
	 if (con == null) {
	 z = "Error in connection with SQL server";
	 } else {


	 
	 String query2 = "SELECT COUNT(user_id) FROM employeeobj where user_id = '"+user_id+"'";
	 String query3 = "SELECT users.user_fname, users.user_lname, departments.department_name, users.user_email, users.user_password, users.user_id FROM departments INNER JOIN users ON departments.department_id = users.department_id  WHERE users.user_id = '"+user_id+"'";
	 Statement stmt2 = con.createStatement();
	 Statement stmt3 = con.createStatement();
	 ResultSet rs2 = stmt2.executeQuery(query2);
	 ResultSet rs3 = stmt3.executeQuery(query3);
	 
	 if (rs2.next())
	 {
		 count = rs2.getString(1);
		 
	 }
	 if(rs3.next())
	 {
		 
		 depname = rs3.getString(3);
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
	
}
