package com.example.bpm;

import android.app.Fragment;
import android.content.Intent;
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



public class OkrChrUpdate extends Fragment {

    ConnectionClass connectionClass;
    EditText edtproname, edtprodesc;
    Button btnrefresh2;
    ListView lstobj;
    String objid;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
 
        View rootView = inflater.inflate(R.layout.okr_chr_update, container, false); 
        

        connectionClass = new ConnectionClass();
        btnrefresh2 = (Button) rootView.findViewById(R.id.btnrefresh2);
        lstobj = (ListView) rootView.findViewById(R.id.lstokr_chr);

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

        List<Map<String, String>> objlist  = new ArrayList<Map<String, String>>();

        @Override
        protected void onPreExecute() {

            //old pbbar
        }

        @Override
        protected void onPostExecute(String r) {
            String[] from = { "objID", "objDesc"};
            int[] views = { R.id.lblobjid, R.id.lblobjdesc };
            final SimpleAdapter ADAP = new SimpleAdapter(getActivity(),
            		objlist, R.layout.lsttemplate_okr_chr, from,
                    views);
            lstobj.setAdapter(ADAP);
            lstobj.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
                    @SuppressWarnings("unchecked")
					HashMap<String, Object> a3obj = (HashMap<String, Object>) ADAP
                            .getItem(arg2);
                    objid = (String) a3obj.get("objID");
                    String obj_desc = (String) a3obj.get("objDesc");

                    //pass to other activity
                    Bundle okrbundle = new Bundle();
                    
                    okrbundle.putString("objid", objid);
                    okrbundle.putString("objdesc", obj_desc);

                    Intent updateOkrChrMain = new Intent(getActivity(), OkrChrUpdateMain.class);
                    updateOkrChrMain.putExtras(okrbundle);
                	startActivity(updateOkrChrMain);
                    
                    
                    
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
                    String query = "select * from chrheadobj order by obj_id ASC";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        Map<String, String> datanum = new HashMap<String, String>();
                        datanum.put("objID", rs.getString("obj_id"));
                        datanum.put("objDesc", rs.getString("obj_desc"));
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