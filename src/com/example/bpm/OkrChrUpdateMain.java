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

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class OkrChrUpdateMain extends Activity{
	
	Button updateOkrNow;
	ConnectionClass connectionClass;
	EditText objdesc,objdept;
	
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.update_okr_chr_main);
		 
		 List<Map<String, String>> objlist  = new ArrayList<Map<String, String>>();
		 objdesc = (EditText) findViewById(R.id.edtobj);
		 objdept = (EditText) findViewById(R.id.edtdept);

		 connectionClass = new ConnectionClass();
		 
		 Bundle okrbundle = getIntent().getExtras();
			
			
		String objID = okrbundle.getString("objid"); 
		String objDesc = okrbundle.getString("objdesc");
		
		try {
            Connection con = connectionClass.CONN();
            if (con == null) {
                
            } else {                
                String query = "select department_code from departments where department_id IN (SELECT dept_id FROM csy_dept_obj WHERE obj_id = '"+objID+"')";
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();
                
                while (rs.next()) {
                    Map<String, String> datanum = new HashMap<String, String>();
                    datanum.put("dept_code", rs.getString("department_code"));
                    objlist.add(datanum);
                    
                    
                }



            }
        } catch (Exception ex) {

            Log.e("MYAPP", "exception", ex);

        }
		
		objdesc.setText(objDesc);
		String depts = String.valueOf(objlist);
		String deptsTrim = depts.replace("{dept_code=","").replace("}","").replace("[","").replace("]","");
		objdept.setText(deptsTrim);

			
		
	}

    

}
