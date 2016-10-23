package com.example.bpm;
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



public class PromotionDirActivity extends Fragment {

    ConnectionClass connectionClass;
    ListView lstpro;
    String userID;
    int deptID;
    
    
    

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
 
        View rootView = inflater.inflate(R.layout.promotion_dir, container, false); 
        

        connectionClass = new ConnectionClass();
        lstpro = (ListView) rootView.findViewById(R.id.lstPromote);
        userID = "";
        
        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref",Context. MODE_PRIVATE); 
        Editor editor = pref.edit();
        deptID = pref.getInt("DeptID", 0); 
        



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
        	
           
        	
            String[] from = { "userID", "employee_name"};
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
                    userID = (String) obj.get("userID");
                    String empName = (String) obj.get("employee_name");
                    String empPos = (String) obj.get("employee_position");
                    
                    
                    Integer Position = Integer.parseInt(empPos);
                    Integer userid = Integer.parseInt(userID);

                    
                    Bundle bundle = new Bundle();
                    
                    bundle.putString("ID", userID);
                    bundle.putString("empName", empName);
                    bundle.putString("pos", empPos);

                    Intent updateMain = new Intent(getActivity(), PromotionDirActivityMain.class);
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
                    String query = "select * from users where department_id = '"+deptID+"'ORDER BY user_id ASC";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    ArrayList<String> data1 = new ArrayList<String>();
                    while (rs.next()) {
                    	
                        Map<String, String> datanum = new HashMap<String, String>();
                        datanum.put("userID", rs.getString("user_id"));
                        
                        String fname = rs.getString("user_fname");
                        String lname = rs.getString("user_lname");
                        String name = fname+" "+lname;
                        
                        datanum.put("employee_name", name);
                        datanum.put("employee_position", rs.getString("user_position"));
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
