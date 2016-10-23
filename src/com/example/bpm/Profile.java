package com.example.bpm;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.example.bpm.MainActivity.DoLogin;
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
import android.widget.TextView;
import android.widget.Toast;



public class Profile extends Fragment {
	
	TextView fnametv,lnametv,depttv,emailtv,usertv,passtv;
	Button update;
	ConnectionClass connectionClass;
	
	String z = "";
	String ID,type;
	int userID;
	int retrieve;
	
	public String Fname;
	public String Lname;
	public String Dept;
	public String Email;
	public String User;
	public String Pass;
	public String id;
	
	public Profile(){}
	
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        
        connectionClass = new ConnectionClass();
        fnametv = (TextView) rootView.findViewById(R.id.tvFnameDisp);
        lnametv = (TextView) rootView.findViewById(R.id.tvLnameDisp);
        depttv = (TextView) rootView.findViewById(R.id.tvDeptDisp);
        emailtv = (TextView) rootView.findViewById(R.id.tvEmailDisp);
        //usertv = (TextView) rootView.findViewById(R.id.tvUserDisp);
        passtv = (TextView) rootView.findViewById(R.id.tvPassDisp);
        update = (Button) rootView.findViewById(R.id.updateProf);
        
        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref",Context. MODE_PRIVATE); 
        Editor editor = pref.edit();
        ID = pref.getString("IDD", null);
        //type = pref.getString("usrtp", null);
        Toast.makeText(getActivity(), ID, Toast.LENGTH_SHORT).show();

        DoFetch Fetch = new DoFetch();
        Fetch.execute("");
        
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	Toast.makeText(Profile.this.getActivity(), Fname, Toast.LENGTH_SHORT).show();
            	Bundle bundle = new Bundle();
                
                bundle.putString("email", Email);
                bundle.putString("user", User);
                bundle.putString("pass", Pass);
                bundle.putString("userIDD", id);

                Intent updateProf = new Intent(getActivity(), ProfileUpdate.class);
                updateProf.putExtras(bundle);
            	startActivity(updateProf);

            }
        });
        
        
        return rootView;
    }
	
	@Override
	public void onResume(){
		
	    super.onResume();
	    SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref",Context. MODE_PRIVATE); 
        Editor editor = pref.edit();
        ID = pref.getString("IDD", null); 
        //type = pref.getString("usrtp", null);

        DoFetch Fetch = new DoFetch();
        Fetch.execute("");

	}
	
	
	public class DoFetch extends AsyncTask<String,String,String>
	 { 
		int userded = Integer.parseInt(ID.trim());
	 String z = "";
	 Boolean isSuccess = false;
	 @Override
	 public void onPreExecute() {
	 }
	 @Override
	 public void onPostExecute(String r) {

	
		 if(isSuccess == true)
		 {
			 fnametv.setText(Fname);
			 depttv.setText(Dept);
		     lnametv.setText(Lname);
		     emailtv.setText(Email);
		     //usertv.setText(User);
		     passtv.setText(Pass);
		     
		     /*
		     SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref",Context. MODE_PRIVATE);   
			 Editor editor = pref.edit();
			 editor.putString("DeptName", Dept );  

			 editor.commit();
			 */
			 
		 }
		
	 }
	 @Override
	 public String doInBackground(String... params) {
		 
	 if(userID < 0)
	 z = "Fetch Error";
	 else
	 {
	 try {
	 Connection con = connectionClass.CONN();
	 if (con == null) {
	 z = "Error in connection with SQL server";
	 } else {
		 
	 String query = "SELECT users.user_fname, users.user_lname, departments.department_name, users.user_email, users.user_password, users.user_id FROM departments INNER JOIN users ON departments.department_id = users.department_id  WHERE users.user_id = '"+ID+"'";
	 Statement stmt = con.createStatement();
	 ResultSet rs = stmt.executeQuery(query);
	 if(rs.next())
	 {
		 	
		 	
		 	Fname = rs.getString(1);
		 	Lname = rs.getString(2);
		    Dept = rs.getString(3);
		    Email = rs.getString(4);
		    Pass = rs.getString(5);
		    id = rs.getString(6);
		 	

		
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
	
}
