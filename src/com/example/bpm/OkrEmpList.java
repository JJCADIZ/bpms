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
import android.widget.Spinner;
import android.widget.Toast;

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



public class OkrEmpList extends Fragment {

    ConnectionClass connectionClass;
    Spinner filter;
    EditText edtObj;
    Button btnAdd;
    ListView lstob,lstobj;
    String objid;
    public String ID;
    public int filterSelect,deptID;
    

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
 
        View rootView = inflater.inflate(R.layout.okr_3, container, false); 
        

        connectionClass = new ConnectionClass();
        lstobj = (ListView) rootView.findViewById(R.id.lstObjList);
        lstob = (ListView) rootView.findViewById(R.id.lstproducts);
        filter = (Spinner) rootView.findViewById(R.id.spinFilter);

        
        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref",Context. MODE_PRIVATE); 
        Editor editor = pref.edit();
        ID = pref.getString("IDD", null);
        deptID = pref.getInt("DeptID", 0);

        FillListMine fillList1 = new FillListMine();
        fillList1.execute("");
        
        return rootView;

    }
    
    @Override
    public void onResume(){
        super.onResume();
        
        	FillListMine fillList1 = new FillListMine();
            fillList1.execute("");

    }
    


   
    public class FillListMine extends AsyncTask<String, String, String> {
        String z = "";

        List<Map<String, String>> objlist  = new ArrayList<Map<String, String>>();

        @Override
        protected void onPreExecute() {

            //old pbbar
        }

        @Override
        protected void onPostExecute(String r) {
            String[] from = { "dir_obj_id", "dir_obj_name"};
            int[] views = { R.id.lblprojid, R.id.lblprojtitle };
            final SimpleAdapter ADAP = new SimpleAdapter(getActivity(),
            		objlist, R.layout.lsttemplateokrdir, from,
                    views);
            lstobj.setAdapter(ADAP);
            lstobj.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
                    @SuppressWarnings("unchecked")
					HashMap<String, Object> a3obj = (HashMap<String, Object>) ADAP
                            .getItem(arg2);
                    objid = (String) a3obj.get("dir_obj_id");
                    String obj_name = (String) a3obj.get("dir_obj_name");

                    
                    Bundle LinkBundle = new Bundle();
                    
                    LinkBundle.putString("linkID", objid);
                    LinkBundle.putString("LinkName", obj_name);

                    
                    Intent LinkObj = new Intent(getActivity(), OkrDirLinkObj.class);
                    LinkObj.putExtras(LinkBundle);
                	startActivity(LinkObj);
                    
                    
                    
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
                    String query = "select * from directorobj ";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    ArrayList<String> data2 = new ArrayList<String>();
                    while (rs.next()) {
                        Map<String, String> datanum = new HashMap<String, String>();
                        datanum.put("dir_obj_name", rs.getString("dir_obj_name"));
                        datanum.put("dir_obj_id", rs.getString("dir_obj_id"));
            

                        objlist.add(datanum);
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
