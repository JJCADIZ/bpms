package com.example.bpm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.bpm.A3UpdateMain.ApproveA3;
import com.example.bpm.A3UpdateMain.UpdateA3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class A3Comment extends Activity{

	ConnectionClass connectionClass;
	
	EditText edtComment;
	Button submitComment;
	ListView lstComments; 
	
	String a3ID,userID;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.a3_comments);
		 
		 lstComments = (ListView) findViewById(R.id.lstComments);
		 submitComment = (Button) findViewById(R.id.btnAddComment);
		 edtComment = (EditText) findViewById(R.id.edtComment);
		 
		 connectionClass = new ConnectionClass();
		 
		 Bundle a3bundle = getIntent().getExtras();
		 
		 a3bundle.getString("a3IDD");
			
		a3ID = a3bundle.getString("a3IDD");	

		
		FillList Fill = new FillList();
        Fill.execute("");
        
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref",Context. MODE_PRIVATE); 
        Editor editor = pref.edit();
        userID = pref.getString("IDD", null); 
        
        
        
		submitComment.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
            	
                CommentNow comment = new CommentNow();
                comment.execute("");

            }
        });
		
			
		
	}
	
	
	

    public class FillList extends AsyncTask<String, String, String> {
        String z = "";

        List<Map<String, String>> projlist  = new ArrayList<Map<String, String>>();

        @Override
        protected void onPreExecute() {

            //old pbbar
        }

        @Override
        protected void onPostExecute(String r) {
            String[] from = { "text"};
            int[] views = { R.id.lblprojid, R.id.lblprojtitle };
            final SimpleAdapter ADAP = new SimpleAdapter(getApplicationContext(),
                    projlist, R.layout.lsttemplate2, from,
                    views);
            lstComments.setAdapter(ADAP);
            lstComments.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
                    @SuppressWarnings("unchecked")
					HashMap<String, Object> a3obj = (HashMap<String, Object>) ADAP
                            .getItem(arg2);
                    //projid = (String) a3obj.get("a3ProjID");
                    String proj_name = (String) a3obj.get("a3ProjName");
                    String proj_background = (String) a3obj.get("a3ProjBackground");
                    String proj_vision = (String) a3obj.get("a3ProjVision");
                    String proj_recommendation = (String) a3obj.get("a3ProjRecommendation");
                    String proj_issues = (String) a3obj.get("a3ProjIssues");
                    
                    
                    
                    
                    
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
                    String query = "select * from comments where comment_obj_id = '"+a3ID+"'";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    ArrayList<String> data2 = new ArrayList<String>();
                    while (rs.next()) {
                        Map<String, String> datanum = new HashMap<String, String>();
                        datanum.put("text", rs.getString("comment_text").replace("<p>", "").replace("</p>", ""));
                        datanum.put("from", rs.getString("comment_from"));
                        
                        projlist.add(datanum);
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

    

	public class CommentNow extends AsyncTask<String, String, String> {

		Bundle a3bundle = getIntent().getExtras();
		String projID = a3bundle.getString("a3ID"); 
        boolean isSuccess = false;
        String z = "";
        
        String comment = edtComment.getText().toString();
        String tagged = "<p>"+comment+"</p>";

        @Override
        protected void onPreExecute() {

        }
        
        @Override
        protected void onPostExecute(String r) {
        	Toast.makeText(A3Comment.this, r, Toast.LENGTH_SHORT).show();
        	edtComment.setText(null);
            
        }
        @Override
        protected String doInBackground(String... params) {
        	if (edtComment.equals(""))
	            z = "Please fill all the fields";
            else {
            	
                try {
                    Connection con = connectionClass.CONN();
                    if (con == null) {
                        z = "Error in connection with SQL server";
                    } else {

						String query = "insert into comments(comment_text,comment_type,comment_obj_id,comment_from,comment_date) values('"+tagged+"',2,'"+a3ID+"','"+userID+"',GETDATE())";

                        PreparedStatement preparedStatement = con.prepareStatement(query);
                        preparedStatement.executeUpdate();
                        z = "Commented Successfully";

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
