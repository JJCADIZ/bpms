package com.example.bpm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.example.bpm.A3UpdateMain.UpdateA3;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class OkrDirLinkObj extends Activity{
	
	EditText edtObjName;
	Button btnLink;
	ConnectionClass connectionClass;
	TextView linked;
	Spinner impact;
	
	public String objName,objID,ID;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.okr_link_obj);
		 connectionClass = new ConnectionClass();
		 
		 edtObjName = (EditText) findViewById(R.id.edtObjNameLink);
		 btnLink = (Button) findViewById(R.id.btnObjLink);
		 linked = (TextView) findViewById(R.id.tvLinked);
		 impact = (Spinner) findViewById(R.id.spinImpactLink);
		 
		 Bundle LinkObj = getIntent().getExtras();
		 
		 objName = LinkObj.getString("LinkName");
		 objID = LinkObj.getString("linkID");
		 SharedPreferences pref = this.getApplicationContext().getSharedPreferences("MyPref",Context. MODE_PRIVATE); 
	        Editor editor = pref.edit();
	        ID = pref.getString("IDD", null);
	        
	        try {
                Connection con = connectionClass.CONN();
                if (con == null) {
                    
                } else {

					String query = "SELECT * FROM directorobj where dir_obj_name = '"+objName+"' AND user_id = '"+ID+"'";

                    PreparedStatement preparedStatement = con.prepareStatement(query);
                    ResultSet rs = preparedStatement.executeQuery();
                    if(rs.next())
                    {
                    	btnLink.setVisibility(View.GONE);
                    	linked.setVisibility(View.VISIBLE);
                    }else{
                    	btnLink.setVisibility(View.VISIBLE);
                    	linked.setVisibility(View.GONE);
                    }
               
                }
            } catch (Exception ex) {
                
                Log.e("MYAPP", "exception", ex);
            }
	        
		 edtObjName.setText(objName);
		 
		 btnLink.setOnClickListener(new View.OnClickListener(){

	            @Override
	            public void onClick(View v) {
	            	LinkObj LinkNow = new LinkObj();
	            	LinkNow.execute("");

	            }
	        });
	}
	
	public class LinkObj extends AsyncTask<String, String, String> {

		Bundle a3bundle = getIntent().getExtras();
		String projID = a3bundle.getString("a3ID"); 
        boolean isSuccess = false;
        String z = "";
        
        String impakto = impact.getSelectedItem().toString();
        
        @Override
        protected void onPreExecute() {

        }
        
        @Override
        protected void onPostExecute(String r) {
        	Toast.makeText(OkrDirLinkObj.this, r, Toast.LENGTH_SHORT).show();
            
        }
        @Override
        protected String doInBackground(String... params) {
        	if (edtObjName.equals(""))
	            z = "Please fill all the fields";
            else {
            	
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
	                    	String okrGetID = rs2.getString("okr_id");
						String query = "INSERT INTO directorobj(dir_obj_name,user_id,dir_obj_raised,dir_obj_impact,okr_id,dir_obj_status,dir_obj_subprocess_id,obj_id)"+
						"VALUES('"+objName+"','"+ID+"',GETDATE(),'"+impakto+"','"+okrGetID+"',4,3,'"+objID+"')";

                        PreparedStatement preparedStatement = con.prepareStatement(query);
                        preparedStatement.executeUpdate();
                        
	                    }
                        z = "Linked Successfully";

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
