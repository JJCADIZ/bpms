package com.example.bpm;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Locale;

import com.example.bpm.A3ActionPlanMain.AddAction;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class A3ActionPlanProgress extends Activity{

	 SeekBar progress;
	 TextView progress_current;
	 Button updateProgress;
	 EditText Title;
	 
	 String title,ID,Prog;
	 int progresso = 0,ProgInt;
	 ConnectionClass connectionClass;
	 
	 

	 @Override
	 protected void onCreate(Bundle savedInstanceState) {
		 
	  super.onCreate(savedInstanceState);
	  
	  setContentView(R.layout.a3_actionplan_progress);
	  progress = (SeekBar) findViewById(R.id.seekProgress);
	  progress_current = (TextView) findViewById(R.id.tvProgress);
	  updateProgress = (Button) findViewById(R.id.btnAddProgress);
	  Title = (EditText) findViewById(R.id.edtActionTitle);
	  connectionClass = new ConnectionClass();
	  
	  
	  Bundle actionProgress = getIntent().getExtras();
		
	  title = actionProgress.getString("action_name");
	  ID = actionProgress.getString("action_id");
	  Prog = actionProgress.getString("action_progress");
	  
	  ProgInt = Integer.parseInt(Prog);
	  
	  Title.setText(title);

	  
	  
	  progress.setMax(100);
	  progress.setProgress(ProgInt);
	  
	  // Initialize the textview with '0'.
	  progress_current.setText("Covered: " + progress.getProgress() + "/" + progress.getMax()+"%");
	  
	  progress.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
		  
		  
		  @Override
		  public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
			  progresso = progresValue;
			  
		  }
		
		  @Override
		  public void onStartTrackingTouch(SeekBar seekBar) {
			  
		  }
		
		  @Override
		  public void onStopTrackingTouch(SeekBar seekBar) {
			  progress_current.setText("Covered: " + progresso + "/" + seekBar.getMax());
			 
		  }
	   });
	 
	 
	  updateProgress.setOnClickListener(new View.OnClickListener(){

         @Override
         public void onClick(View v) {
  
             
        	 UpdateProgress update = new UpdateProgress();
        	 update.execute("");
         }
     });

	 

	 }
	 
	 public class UpdateProgress extends AsyncTask<String, String, String> {
			
			

	        String z = "";
	        Boolean isSuccess = false;
	        int progresoso = progress.getProgress();


	        @Override
	        protected void onPreExecute() {
	            
	        }

	        @Override
	        protected void onPostExecute(String r) {
	            
	            Toast.makeText(A3ActionPlanProgress.this, r, Toast.LENGTH_SHORT).show();
	            if(isSuccess==true) {
	            	finish();
	            }

	        }

			@Override
			protected String doInBackground(String... params) {
				if (progress.getProgress() < ProgInt)
		            z = "Progress can't be lower";
		        else {
		            try {
		                Connection con = connectionClass.CONN();
		                if (con == null) {
		                    z = "Error in connection with SQL server";
		                } else {
		                	
		                	String query = "Update a3action set action_progress = '"+progresoso+"' where action_id = '"+ID+"'";                                            
		                	PreparedStatement preparedStatement = con.prepareStatement(query);
		                	preparedStatement.executeUpdate();
		                	
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
