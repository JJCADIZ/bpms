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

public class A3SuccessCriteriaView extends Activity{
	
	ConnectionClass connectionClass;
	String actionID,projID,metricID;
	ListView criteriaList;
	Button addCriteria;
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a3_successcriteria_view);
		
		addCriteria = (Button) findViewById(R.id.btnAddCriteria);
		criteriaList = (ListView) findViewById(R.id.lstCriteria);
		
		Bundle actionPlan = getIntent().getExtras();
		
		projID = actionPlan.getString("a3IDD");

		
		

        connectionClass = new ConnectionClass();
        
        FillList Fill = new FillList();
        Fill.execute("");
      
        
        addCriteria.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
            	Bundle criteriaMain = new Bundle();
                
            	criteriaMain.putString("ProjID", projID);
            

                Intent ActionPlanMain = new Intent(A3SuccessCriteriaView.this, A3SuccessCriteriaAdd.class);
                ActionPlanMain.putExtras(criteriaMain);
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
            String[] from = { "metricDesc", "metricCurr", };
            int[] views = { R.id.a3desc, R.id.a3status };
            final SimpleAdapter ADAP = new SimpleAdapter(A3SuccessCriteriaView.this,
                    projlist, R.layout.actionplan_list, from,
                    views);
            criteriaList.setAdapter(ADAP);
            criteriaList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
                    @SuppressWarnings("unchecked")
					HashMap<String, Object> a3obj = (HashMap<String, Object>) ADAP
                            .getItem(arg2);
                    metricID = (String) a3obj.get("metricID");
                    String DESC = (String) a3obj.get("metricDesc");
                    String CURR = (String) a3obj.get("metricCurr");
                    String TARG = (String) a3obj.get("metricTarget");

                    
                    Bundle editCriteria = new Bundle();
                    
                    editCriteria.putString("ProjID", projID);
                    editCriteria.putString("metric_id", metricID);
                    editCriteria.putString("metric_desc", DESC);
                    editCriteria.putString("metric_current", CURR);
                    editCriteria.putString("metric_target", TARG);


                    Intent CriteriaEdit = new Intent(A3SuccessCriteriaView.this, A3SuccessCriteriaEdit.class);
                    CriteriaEdit.putExtras(editCriteria);
                	startActivity(CriteriaEdit);
                    
                    
                    
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
                    String query = "select * from a3metric where proj_id = '"+projID+"' order by metric_id ASC";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    ArrayList<String> data2 = new ArrayList<String>();
                    while (rs.next()) {
                        Map<String, String> datanum = new HashMap<String, String>();
                        datanum.put("metricID", rs.getString("metric_id"));
                        datanum.put("metricDesc", rs.getString("metric_desc"));
                        datanum.put("metricCurr", rs.getString("metric_curr"));
                        datanum.put("metricTarget", rs.getString("metric_target"));
                        
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
