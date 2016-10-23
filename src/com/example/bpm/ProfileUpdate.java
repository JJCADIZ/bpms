package com.example.bpm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ProfileUpdate extends Activity{
	
	Button updateNow;
	ConnectionClass connectionClass;
	EditText Email,Username,Password;
	
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.update_profile);
		 
		 Email = (EditText) findViewById(R.id.edtEmail);
		 Username = (EditText) findViewById(R.id.edtUser);
		 Password = (EditText) findViewById(R.id.edtPass);
		 updateNow = (Button) findViewById(R.id.btnUpdateProf);
		 connectionClass = new ConnectionClass();
		 
		 Bundle bundle = getIntent().getExtras();
			
			
		String email = bundle.getString("email"); 
		String user = bundle.getString("user");
		String pass = bundle.getString("pass");
		
		Email.setText(email);
		Username.setText(user);
		Password.setText(pass);
		
        updateNow.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                UpdateProfile updatePro = new UpdateProfile();
                updatePro.execute("");

            }
        });
			
		
	}
	
	public class UpdateProfile extends AsyncTask<String, String, String> {

		Bundle bundle = getIntent().getExtras();
		String ID = bundle.getString("userIDD"); 
        boolean isSuccess = false;
        String z = "";
        
        String emailUp = Email.getText().toString();
        String userUp = Username.getText().toString();
        String passUp = Password.getText().toString();
  
        
        @Override
        protected void onPreExecute() {

        }
        
        @Override
        protected void onPostExecute(String r) {
        	Toast.makeText(ProfileUpdate.this, r, Toast.LENGTH_SHORT).show();
            
        }

       

        @Override
        protected String doInBackground(String... params) {
        	if (ID.equals(""))
	            z = "Please fill all the fields";
            else {
            	
                try {
                    Connection con = connectionClass.CONN();
                    if (con == null) {
                        z = "Error in connection with SQL server";
                    } else {

						String query = "Update users set user_email='"+emailUp+"',user_username='"+userUp+"'" +
                        ",user_password='"+passUp+"'where user_id='"+ID+"'";

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

}
