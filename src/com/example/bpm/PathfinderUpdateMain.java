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

public class PathfinderUpdateMain extends Activity{
	

	ConnectionClass connectionClass;
    EditText edtideaname, edtbenefit,edtobservation,edtquickwin,date;
    Button btnupdate,btnremove,btnApprove,btnComments;
    TextView tvApprove,tvApproval;
    Spinner ideatype, benefittype;
    ProgressBar pbbar;
    String proid;
    private ListView mDrawerList;
    ListView lstpro;
    String pathid,name;
    int UserType,userID;
    
    Integer pathfinder_idea_id;
    String pathfinder_id;
	
	public PathfinderUpdateMain(){}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_main_pathfinder);
		
		
		Bundle bundle = getIntent().getExtras();
		
		
		String pathfinder_name = bundle.getString("name"); 
		 pathfinder_id = bundle.getString("id2"); 
		Double pathfinder_eqv = bundle.getDouble("eqv");
		String pathfinder_quick = (bundle.getString("quick")).replace("<p>", "").replace("</p>", "");
		String pathfinder_obs = (bundle.getString("observation")).replace("<p>", "").replace("</p>", "");
		pathfinder_idea_id = bundle.getInt("idea_id");
		Integer pathfinder_approval = bundle.getInt("ApprovalStat");
		Integer pathfinder_benefit_id = bundle.getInt("benefit_id");
		String target = bundle.getString("closure");
		Integer path_owner = bundle.getInt("ownerID");
		

        connectionClass = new ConnectionClass();
        
        edtideaname = (EditText) findViewById(R.id.edtideaname2);
        edtbenefit = (EditText)  findViewById(R.id.edtbenefit2);
        date = (EditText) findViewById(R.id.target2);
        edtobservation = (EditText) findViewById(R.id.edyobservation2);
        edtquickwin = (EditText) findViewById(R.id.edtquickwin2);
        ideatype = (Spinner) findViewById(R.id.spinner12);
        benefittype = (Spinner) findViewById(R.id.spinner22);
        btnupdate = (Button)  findViewById(R.id.btnupdate);
        pbbar = (ProgressBar)  findViewById(R.id.pbbar2);
        btnremove = (Button) findViewById(R.id.btndelete);
        btnApprove = (Button) findViewById(R.id.btnApprove);
        btnComments = (Button) findViewById(R.id.btnPathComments);
        pbbar.setVisibility(View.GONE);
        proid = "path_id";
        
        edtideaname.setText(pathfinder_name);
        edtbenefit.setText(String.valueOf(pathfinder_eqv));
        date.setText(target);
        edtquickwin.setText(pathfinder_quick);
        edtobservation.setText(pathfinder_obs);
        ideatype.setSelection((pathfinder_idea_id - 1));
        benefittype.setSelection((pathfinder_benefit_id - 1));
        
        tvApproval = (TextView) findViewById(R.id.tvApproved);
		SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref",Context. MODE_PRIVATE); 
        Editor editor = pref.edit();
        name = pref.getString("IDD", null); 
        userID = Integer.parseInt(name);
        UserType = pref.getInt("usrtp", 0);
        
        if(path_owner == userID)
        {
        	btnupdate.setVisibility(View.VISIBLE);
        	btnremove.setVisibility(View.VISIBLE);
        }else if(path_owner != userID)
        {
        	btnupdate.setVisibility(View.GONE);
        	btnremove.setVisibility(View.GONE);
        }
        
        if(pathfinder_approval == 10)
        {
        	tvApproval.setText("Pending");
        }else if(pathfinder_approval == 3)
        {
        	tvApproval.setText("Not yet Approved");
        }
        
        if(UserType == 3)
        {
        	btnApprove.setVisibility(Button.GONE);
        	
        }else if(UserType == 2)
        {
        	btnApprove.setVisibility(Button.VISIBLE);
        }
        
      
        
        btnApprove.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
            	ApprovePathfinder approve = new ApprovePathfinder();
            	approve.execute("");

            }
        });
        
        
        btnupdate.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                UpdatePro updatePro = new UpdatePro();
                updatePro.execute("");

            }
        });
        
        
        btnComments.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
            	Bundle actionPlan = new Bundle();
                
            	actionPlan.putString("pathID", pathfinder_id);
            

                Intent PathComments = new Intent(PathfinderUpdateMain.this, PathfinderComments.class);
                PathComments.putExtras(actionPlan);
            	startActivity(PathComments);

            }
        });
        
        btnremove.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
            	DeletePro delete = new DeletePro();
                delete.execute("");

            }
        });
        
        
        
        

        
      
        
    }
	
	
	public class UpdatePro extends AsyncTask<String, String, String> {



        String z = "";
        Boolean isSuccess = false;


        String observation = edtobservation.getText().toString();
        String quickwin = edtquickwin.getText().toString();
        String ideaname = edtideaname.getText().toString();
        String benefit = edtbenefit.getText().toString();
        String process = ideatype.getSelectedItem().toString();
        String benefitType = benefittype.getSelectedItem().toString();
        String target = date.getText().toString();
        
        Integer benefit_type = benefittype.getSelectedItemPosition();
        Integer idea_type = ideatype.getSelectedItemPosition();
        Integer idea_id;
    	Integer benefit_id;
        
        
        Bundle bundle2 = getIntent().getExtras();
        String path_id2 = bundle2.getString("id2"); 
        
        @Override
        protected void onPreExecute() {
            pbbar.setVisibility(View.VISIBLE);
        }
        
        @Override
        protected void onPostExecute(String r) {
            pbbar.setVisibility(View.GONE);
            Toast.makeText(PathfinderUpdateMain.this, r, Toast.LENGTH_SHORT).show();
            if(isSuccess==true) {
            	Fragment fragment = new PathfinderUpdate();
            }
        }

       

        @Override
        protected String doInBackground(String... params) {
        	if (ideaname.trim().equals("") || benefit.isEmpty()  || observation.trim().equals("") || quickwin.trim().equals(""))
	            z = "Please fill all the fields";
            else {
            	double benefitInt = Double.parseDouble(benefit);
                try {
                    Connection con = connectionClass.CONN();
                    if (con == null) {
                        z = "Error in connection with SQL server";
                    } else {

                        String dates = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH)
                                .format(Calendar.getInstance().getTime());
                        
                        switch (idea_type)
	                	{
	                	case 0:
	                		idea_id = 1;
	                		break;
	                	case 1:
	                		idea_id = 2;
	                		break;
	                	case 2:
	                		idea_id = 3;
	                		break;
	                	case 3:
	                		idea_id = 4;
	                		break;
	                	case 4:
	                		idea_id = 5;
	                		break;
	                	default:
	                		idea_id = 1;
	                		break;
	                	}
	                	
	                	switch(benefit_type)
	                	{
	                	case 0:
	                		benefit_id = 1;
	                		break;
	                	case 1:
	                		benefit_id = 2;
	                		break;
	                	default:
	                		benefit_id = 1;
	                		break;
	                	}

                        
						String query = "Update pathfinder set pathfinder_name='"+ideaname+"',pathfinder_potential_eqv='"+benefitInt+"'" +
                        ",benefit_id='"+benefit_id+"',pathfinder_quickwin='"+quickwin+"',pathfinder_observation='"+observation+"'" +
                        ",idea_id='"+idea_id+"',pathfinder_target_closure='"+target+"'where pathfinder_id='"+path_id2+"'";

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
	
	
	public class DeletePro extends AsyncTask<String, String, String> {
		
		Bundle bundle2 = getIntent().getExtras();
        String path_id2 = bundle2.getString("id2"); 
        String z = "";
        Boolean isSuccess = false;
        @Override
        protected void onPreExecute() {
            pbbar.setVisibility(View.VISIBLE);
        }
        @Override
        protected void onPostExecute(String r) {
            pbbar.setVisibility(View.GONE);
            Toast.makeText(PathfinderUpdateMain.this, r, Toast.LENGTH_SHORT).show();
            if(isSuccess==true) {
            	
            }
        }
        @Override
        protected String doInBackground(String... params) {
                try {
                    Connection con = connectionClass.CONN();
                    if (con == null) {
                        z = "Error in connection with SQL server";
                    } else {
                  
                        String query = "delete from pathfinder where pathfinder_id="+path_id2;
                        PreparedStatement preparedStatement = con.prepareStatement(query);
                        preparedStatement.executeUpdate();
                        z = "Pathfinder has now been Approved";
                        isSuccess = true;
                    }
                } catch (Exception ex) {
                    isSuccess = false;
                    z = "Exceptions";
                    Log.e("MYAPP", "exception", ex);
                }
            
            return z;
        }
    }
	
	public class ApprovePathfinder extends AsyncTask<String, String, String> {
		
		Bundle bundle2 = getIntent().getExtras();
        String path_id2 = bundle2.getString("id2"); 
        String z = "";
        Boolean isSuccess = false;
        @Override
        protected void onPreExecute() {
            pbbar.setVisibility(View.VISIBLE);
        }
        @Override
        protected void onPostExecute(String r) {
            pbbar.setVisibility(View.GONE);
            Toast.makeText(PathfinderUpdateMain.this, r, Toast.LENGTH_SHORT).show();
            if(isSuccess==true) {
            	tvApproval.setText("Already Approved");
            }
        }
        @Override
        protected String doInBackground(String... params) {
                try {
                    Connection con = connectionClass.CONN();
                    if (con == null) {
                        z = "Error in connection with SQL server";
                    } else {
                        String query = "Update pathfinder set pathfinder_approval_stat='"+ 1 +"' where pathfinder_id="+path_id2;
                        PreparedStatement preparedStatement = con.prepareStatement(query);
                        preparedStatement.executeUpdate();
                        z = "Approved Successfully";
                        isSuccess = true;
                    }
                } catch (Exception ex) {
                    isSuccess = false;
                    z = "Exceptions";
                    Log.e("MYAPP", "exception", ex);
                }
            
            return z;
        }
    }
	
	
}


