package com.example.bpm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.bpm.A3Update.FillList;
import com.example.bpm.PathfinderUpdateMain.ApprovePathfinder;
import com.example.bpm.PathfinderUpdateMain.DeletePro;
import com.example.bpm.PathfinderUpdateMain.UpdatePro;

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
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class A3ActionPlanView extends Activity{
	
	ConnectionClass connectionClass;
	String actionID,projID,name;
	ListView ActionList;
	Button addAction;
	int userID;
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a3_actionplan_view);
		
		connectionClass = new ConnectionClass();
		Bundle actionPlan = getIntent().getExtras();
		
		projID = actionPlan.getString("a3IDD");
		
		SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref",Context. MODE_PRIVATE); 
        Editor editor = pref.edit();
        name = pref.getString("IDD", null);
        userID = Integer.parseInt(name);
		
		addAction = (Button) findViewById(R.id.btnAddA3Action);
		ActionList = (ListView) findViewById(R.id.listAction);
		
		try {
            Connection con = connectionClass.CONN();
            if (con == null) {
                
            } else {

				String query = "select proj_lead from a3projects where proj_id='"+projID+"'";

                PreparedStatement preparedStatement = con.prepareStatement(query);
                ResultSet RS = preparedStatement.executeQuery();
                
                if(RS.next())
                {
                	String lead =  RS.getString("proj_lead");
                	int int_lead = Integer.parseInt(lead);
                	if(int_lead == userID)
                	{
                		addAction.setVisibility(Button.VISIBLE);
                    	
                	}else if(int_lead != userID)
                	{
                		addAction.setVisibility(Button.GONE);
                    	
                	}
                }
                
            }
        } catch (Exception ex) {
            
            Log.e("MYAPP", "exception", ex);
        }

        connectionClass = new ConnectionClass();
        
        FillList Fill = new FillList();
        Fill.execute("");
      
        
        addAction.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
            	Bundle actionPlanMain = new Bundle();
                
            	actionPlanMain.putString("a3_ID", projID);
            	

                Intent ActionPlanMain = new Intent(A3ActionPlanView.this, A3ActionPlanMain.class);
                ActionPlanMain.putExtras(actionPlanMain);
            	startActivity(ActionPlanMain);
                
            		
            }
        });
        

		
 
    }
	

    public void onResume(){
        super.onResume();
        FillList Fill = new FillList();
        Fill.execute("");

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
            String[] from = { "a3ActionProgress", "a3Desc", };
            int[] views = { R.id.a3desc, R.id.a3status };
            final SimpleAdapter ADAP = new SimpleAdapter(A3ActionPlanView.this,
                    projlist, R.layout.actionplan_list, from,
                    views);
            ActionList.setAdapter(ADAP);
            ActionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
                    @SuppressWarnings("unchecked")
					HashMap<String, Object> a3obj = (HashMap<String, Object>) ADAP
                            .getItem(arg2);
                    actionID = (String) a3obj.get("a3ActionID");
                    String desc = (String) a3obj.get("a3Desc");
                    String proj_background = (String) a3obj.get("a3ActionStat");
                    String progress = (String) a3obj.get("a3ActionProgress");

                    
                    Bundle actionProgress = new Bundle();
                    
                    actionProgress.putString("action_id", actionID);
                    actionProgress.putString("action_name", desc);
                    actionProgress.putString("action_progress", progress);


                    Intent updateA3Main = new Intent(A3ActionPlanView.this, A3ActionPlanProgress.class);
                    updateA3Main.putExtras(actionProgress);
                	startActivity(updateA3Main);
                    
                    
                    
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
                    String query = "select * from a3action where proj_id = '"+projID+"' order by action_id ASC";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    ArrayList<String> data2 = new ArrayList<String>();
                    while (rs.next()) {
                        Map<String, String> datanum = new HashMap<String, String>();
                        datanum.put("a3ActionID", rs.getString("action_id"));
                        datanum.put("a3Desc", rs.getString("action_desc"));
                        datanum.put("a3ActionStat", rs.getString("action_status"));
                        datanum.put("a3ActionProgress", rs.getString("action_progress"));
                        
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
	

}
