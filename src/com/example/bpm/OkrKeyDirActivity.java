package com.example.bpm;

import android.app.Activity;
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



public class OkrKeyDirActivity extends Activity {

    ConnectionClass connectionClass2;
    EditText edtObjName;
    Button btnAddKr;
    ListView lstKr,lstKrObj;
    String objid;
    public String ID,DirObjID,objName;


    public void onCreate(Bundle savedInstanceState) {
    	
    	super.onCreate(savedInstanceState);
		 setContentView(R.layout.okr_edit_obj);
		
        

        
        btnAddKr = (Button) findViewById(R.id.btnAddKr);
        lstKrObj = (ListView) findViewById(R.id.lstKr);
        lstKr = (ListView) findViewById(R.id.lstproducts);
        edtObjName = (EditText) findViewById(R.id.edtObjKr);
        
        SharedPreferences pref = this.getApplicationContext().getSharedPreferences("MyPref",Context. MODE_PRIVATE); 
        Editor editor = pref.edit();
        ID = pref.getString("IDD", null);
        
        Bundle objBundle = getIntent().getExtras();
		
		
        DirObjID = objBundle.getString("objID"); 
        objName = objBundle.getString("objName"); 
        
        edtObjName.setText(objName);

        FillList fillList = new FillList();
        fillList.execute("");
        
        
        btnAddKr.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
            	
            	Bundle KrBundle = new Bundle();
                
                KrBundle.putString("krID", objid);
                KrBundle.putString("objID", DirObjID);
                KrBundle.putString("userID", ID);

                
                Intent insertKr = new Intent(OkrKeyDirActivity.this, OkrKrDirAddActivity.class);
                insertKr.putExtras(KrBundle);
            	startActivity(insertKr);


            }
        });
        

    }
    
    @Override
    public void onResume(){
        super.onResume();
        FillList Fill = new FillList();
        Fill.execute("");

    }
    


    
    public class FillList extends AsyncTask<String, String, String> {
        String z = "";

        List<Map<String, String>> krlist  = new ArrayList<Map<String, String>>();

        @Override
        protected void onPreExecute() {

            //old pbbar
        }

        @Override
        protected void onPostExecute(String r) {
            String[] from = { "kr_id", "kr_name"};
            int[] views = { R.id.lblprojid, R.id.lblprojtitle };
            final SimpleAdapter ADAP = new SimpleAdapter(OkrKeyDirActivity.this,
            		krlist, R.layout.lsttemplateokrdir, from,
                    views);
            lstKrObj.setAdapter(ADAP);
            lstKrObj.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
                    @SuppressWarnings("unchecked")
					HashMap<String, Object> a3obj = (HashMap<String, Object>) ADAP
                            .getItem(arg2);
                    objid = (String) a3obj.get("kr_id");
                    String krName = (String) a3obj.get("kr_name");

                    /*
                    Bundle KrBundle = new Bundle();
                    
                    KrBundle.putString("krID", objid);
                    KrBundle.putString("objID", DirObjID);

                    
                    Intent insertKr = new Intent(OkrKeyDirActivity.this, OkrKrDirAddActivity.class);
                    insertKr.putExtras(KrBundle);
                	startActivity(insertKr);
					*/
                }
            });



        }

        @Override
        protected String doInBackground(String... params) {

            try {
            	connectionClass2 = new ConnectionClass();
                Connection con = connectionClass2.CONN();
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {
                    String query = "select kr_id,kr_name from keyresults where kr_id = (select kr_id from krobj where obj_id = '"+DirObjID+"')";
                    
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    ArrayList<String> data2 = new ArrayList<String>();
                    while (rs.next()) {
                        Map<String, String> datanum = new HashMap<String, String>();
                        datanum.put("kr_id", rs.getString(1));
                        datanum.put("kr_name", rs.getString(2));

                        krlist.add(datanum);
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
