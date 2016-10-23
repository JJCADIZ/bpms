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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class PromotionChr extends Fragment{
	
	ConnectionClass connectionClass;
	String promID,projID;
	ListView PromotionList;

	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.promotion_chr, container, false);


		PromotionList = (ListView) rootView.findViewById(R.id.lstPromotion);

		

        connectionClass = new ConnectionClass();
        
        FillList Fill = new FillList();
        Fill.execute("");
      

        

		
        return rootView;
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
            String[] from = { "empID", "Promote", };
            int[] views = { R.id.a3desc, R.id.a3status };
            final SimpleAdapter ADAP = new SimpleAdapter(getActivity(),
                    projlist, R.layout.actionplan_list, from,
                    views);
            PromotionList.setAdapter(ADAP);
            PromotionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
                    @SuppressWarnings("unchecked")
					HashMap<String, Object> a3obj = (HashMap<String, Object>) ADAP
                            .getItem(arg2);
                    promID = (String) a3obj.get("promID");
                    String empID = (String) a3obj.get("empID");
                    String promote = (String) a3obj.get("Promote");
                    String recommend = (String) a3obj.get("recommendation");
                    String status = (String) a3obj.get("status");
                    String endorsed = (String) a3obj.get("endorsed");

                    
                    Bundle Promotions = new Bundle();
                    
                    Promotions.putString("promID", promID);
                    Promotions.putString("empID", empID);
                    Promotions.putString("Promote", promote);
                    Promotions.putString("recommendation", recommend);
                    Promotions.putString("status", status);
                    Promotions.putString("endorsed", endorsed);


                    Intent updateA3Main = new Intent(getActivity(), PromotionChrMain.class);
                    updateA3Main.putExtras(Promotions);
                	startActivity(updateA3Main);

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
                    String query = "select * from promotion";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    ArrayList<String> data2 = new ArrayList<String>();
                    while (rs.next()) {

                        Map<String, String> datanum = new HashMap<String, String>();
                        
                        datanum.put("promID", rs.getString("promotion_id"));
                        datanum.put("empID", rs.getString("employee_id"));
                        datanum.put("Promote", rs.getString("promote_to"));
                        datanum.put("recommendation", rs.getString("recommendation"));
                        datanum.put("status", rs.getString("status"));
                        datanum.put("endorsed", rs.getString("endrosed_by"));
                        
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
