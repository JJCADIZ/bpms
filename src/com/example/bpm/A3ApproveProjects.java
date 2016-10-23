package com.example.bpm;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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

public class A3ApproveProjects extends Fragment {

    ConnectionClass connectionClass;
    EditText edtproname, edtprodesc;
    Button btnrefresh2;
    ListView lstproj,lstpro;
    String projid,name;
    int userID;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
 
        View rootView = inflater.inflate(R.layout.update_a3, container, false); 
        

        connectionClass = new ConnectionClass();
        btnrefresh2 = (Button) rootView.findViewById(R.id.btnrefresh2);
        lstproj = (ListView) rootView.findViewById(R.id.lsta3);
        lstpro = (ListView) rootView.findViewById(R.id.lstproducts);

        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref",Context. MODE_PRIVATE); 
        Editor editor = pref.edit();
        name = pref.getString("IDD", null);        
        userID = Integer.parseInt(name);


        FillList fillList = new FillList();
        fillList.execute("");
        
        
        btnrefresh2.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                FillList Fill = new FillList();
                Fill.execute("");

            }
        });
        


        
        return rootView;

    }
    
    @Override
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
            String[] from = { "a3ProjID", "a3ProjName"};
            int[] views = { R.id.lblprojid, R.id.lblprojtitle };
            final SimpleAdapter ADAP = new SimpleAdapter(getActivity(),
                    projlist, R.layout.lsttemplate2, from,
                    views);
            lstproj.setAdapter(ADAP);
            lstproj.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
                    @SuppressWarnings("unchecked")
					HashMap<String, Object> a3obj = (HashMap<String, Object>) ADAP
                            .getItem(arg2);
                    projid = (String) a3obj.get("a3ProjID");
                    String proj_name = (String) a3obj.get("a3ProjName");
                    String proj_background = (String) a3obj.get("a3ProjBackground");
                    String proj_vision = (String) a3obj.get("a3ProjVision");
                    String proj_recommendation = (String) a3obj.get("a3ProjRecommendation");
                    String proj_issues = (String) a3obj.get("a3ProjIssues");
                    
                    Bundle a3bundle = new Bundle();
                    
                    a3bundle.putString("a3ID", projid);
                    a3bundle.putString("a3Name", proj_name);
                    a3bundle.putString("a3Background", proj_background);
                    a3bundle.putString("a3Vision", proj_vision);
                    a3bundle.putString("a3Recommendation", proj_recommendation);
                    a3bundle.putString("a3Issues", proj_issues);

                    Intent updateA3Main = new Intent(getActivity(), A3ApproveDir.class);
                    updateA3Main.putExtras(a3bundle);
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
                	
                	String select_obj = "select object_id from notifications where notification_recipient = '"+userID+"' AND notification_type = 2";
                	PreparedStatement ps2 = con.prepareStatement(select_obj);
                    ResultSet rs2 = ps2.executeQuery();
                    
                    if(rs2.next())
                    {
                    	String obj_id = rs2.getString("object_id");
                    	String query = "select * from a3projects a join a3team b on a.proj_id = b.proj_id join users c on a.proj_lead = c.user_id where b.user_id = '"+userID+"' and b.approved  = 0 and a.proj_status = 10 or (b.user_id = '"+userID+"' and a.proj_status = 5)order by proj_dateupdate DESC";
                        PreparedStatement ps = con.prepareStatement(query);
                        ResultSet rs = ps.executeQuery();

                        ArrayList<String> data2 = new ArrayList<String>();
                        while (rs.next()) {
                            Map<String, String> datanum = new HashMap<String, String>();
                            datanum.put("a3ProjID", rs.getString("proj_id"));
                            datanum.put("a3ProjName", rs.getString("proj_title"));
                            datanum.put("a3ProjBackground", rs.getString("proj_background"));
                            datanum.put("a3ProjVision", rs.getString("proj_vision"));
                            datanum.put("a3ProjIssues", rs.getString("proj_issue"));
                            datanum.put("a3ProjRecommendation", rs.getString("proj_recommendation"));
                            
                            projlist.add(datanum);
                        }
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

