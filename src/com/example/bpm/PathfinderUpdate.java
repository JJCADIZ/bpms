package com.example.bpm;

import android.app.Fragment;
import android.content.Intent;
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



public class PathfinderUpdate extends Fragment {

    ConnectionClass connectionClass;
    EditText edtproname, edtprodesc;
    Button btnadd,btnupdate,btndelete,btnrefresh;
    ProgressBar pbbar;
    ListView lstpro;
    String pathid;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
 
        View rootView = inflater.inflate(R.layout.update_pathfinder, container, false); 
        

        connectionClass = new ConnectionClass();
        btnupdate = (Button) rootView.findViewById(R.id.btnupdate);
        lstpro = (ListView) rootView.findViewById(R.id.lstproducts);
        pathid = "";




        FillList fillList = new FillList();
        fillList.execute("");


        
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

        List<Map<String, String>> prolist  = new ArrayList<Map<String, String>>();

        @Override
        protected void onPreExecute() {

            //old pbbar
        }

        @Override
        protected void onPostExecute(String r) {

           

            String[] from = { "pathfinder_id", "pathfinder_name"};
            int[] views = { R.id.lblproid, R.id.lblproname };
            final SimpleAdapter ADA = new SimpleAdapter(getActivity(),
                    prolist, R.layout.lsttemplate, from,views);
            lstpro.setAdapter(ADA);
            lstpro.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
                    @SuppressWarnings("unchecked")
					HashMap<String, Object> obj = (HashMap<String, Object>) ADA
                            .getItem(arg2);
                    pathid = (String) obj.get("pathfinder_id");
                    String idea_name = (String) obj.get("pathfinder_name");
                    String benefit_eqv = (String) obj.get("pathfinder_eqv");
                    String quickwin = (String) obj.get("pathfinder_quick");
                    String observe = (String) obj.get("pathfinder_obs");
                    String ideaId = (String) obj.get("pathfinder_idea_id");
                    String BenefitId = (String) obj.get("pathfinder_benefit");
                    String closure = (String) obj.get("pathfinder_closure");
                    String approve = (String) obj.get("pathfinder_approval");
                    String path_userID = (String) obj.get("pathfinder_userID");
                    
                    Integer ideaIdMain = Integer.parseInt(ideaId);
                    Integer benefitIdMain = Integer.parseInt(BenefitId);
                    Integer pathfinderId = Integer.parseInt(pathid);
                    Double benefiteqv = Double.parseDouble(benefit_eqv);
                    Integer approvalStat = Integer.parseInt(approve);
                    Integer empID = Integer.parseInt(path_userID);
                    
                    
                    Bundle bundle = new Bundle();
                    
                    bundle.putString("id2", pathid);
                    bundle.putString("name", idea_name);
                    bundle.putDouble("eqv", benefiteqv);
                    bundle.putString("quick", quickwin);
                    bundle.putString("observation", observe);
                    bundle.putInt("idea_id", ideaIdMain);
                    bundle.putInt("benefit_id", benefitIdMain);
                    bundle.putString("closure", closure);
                    bundle.putInt("id", pathfinderId);
                    bundle.putInt("ApprovalStat", approvalStat);
                    bundle.putInt("ownerID",empID);

                    Intent updateMain = new Intent(getActivity(), PathfinderUpdateMain.class);
                    updateMain.putExtras(bundle);
                	startActivity(updateMain);
                    
                    
                    
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
                    String query = "select * from pathfinder ORDER BY pathfinder_id ASC";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    ArrayList<String> data1 = new ArrayList<String>();
                    while (rs.next()) {
                    	
                        Map<String, String> datanum = new HashMap<String, String>();
                        datanum.put("pathfinder_id", rs.getString("pathfinder_id"));
                        datanum.put("pathfinder_name", rs.getString("pathfinder_name"));
                        datanum.put("pathfinder_status", rs.getString("pathfinder_status"));
                        datanum.put("pathfinder_eqv", rs.getString("pathfinder_potential_eqv"));
                        datanum.put("pathfinder_obs", rs.getString("pathfinder_observation"));
                        datanum.put("pathfinder_quick", rs.getString("pathfinder_quickwin"));
                        datanum.put("pathfinder_idea_id", rs.getString("idea_id"));
                        datanum.put("pathfinder_approval", rs.getString("pathfinder_status"));
                        datanum.put("pathfinder_benefit", rs.getString("benefit_id"));
                        datanum.put("pathfinder_userID", rs.getString("user_id"));
                        datanum.put("pathfinder_closure", rs.getString("pathfinder_target_closure"));
                        
                        prolist.add(datanum);
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
