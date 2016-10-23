package com.example.bpm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PromotionChrMain extends Activity{
	
	ConnectionClass connectionClass;
	EditText recommendation;
	TextView empName,empPosition,endorser;
	Button PromoteNow;
	
	String promID,empID,promote,recommend,status,endorse;
	
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.promotion_chr_main);
        
        recommendation = (EditText) findViewById(R.id.edtPromotionRecommend);
        empName = (TextView) findViewById(R.id.tvPromotionEmp);
        empPosition = (TextView) findViewById(R.id.tvPromotionPosition);
        endorser = (TextView) findViewById(R.id.tvPromotionEndorser);
        PromoteNow = (Button) findViewById(R.id.btnPromoteEmpNow);
        
        connectionClass = new ConnectionClass();
        SetEmployee();
        SetPosition();
        SetEndorser();

        
    }
	
	public void SetEmployee()
	{
		
		Bundle Promotions = getIntent().getExtras();
        
        promID = Promotions.getString("promID");
        empID = Promotions.getString("empID");
        promote = Promotions.getString("Promote");
        recommend = Promotions.getString("recommendation");
        status = Promotions.getString("status");
        endorse = Promotions.getString("endorsed");
		
		PreparedStatement ps;
		ResultSet rs;
		
		String Fname,Lname,name;
		
		Toast.makeText(this, endorse, Toast.LENGTH_SHORT).show();

		
		recommendation.setText(recommend);
		
		try {
            Connection con = connectionClass.CONN();
            if (con == null) {
                Toast.makeText(getApplicationContext(), "CONNECTION FAIL", Toast.LENGTH_LONG).show();
            } else {
                String query = "select user_fname,user_lname from users,promotion  where promotion.employee_id = users.user_id AND promotion.employee_id = '"+empID+"'";
                ps = con.prepareStatement(query);
                rs = ps.executeQuery();

                ArrayList<String> data1 = new ArrayList<String>();
                while (rs.next()) {
                	
                	
                	
                	Fname = rs.getString("user_fname");
                	Lname = rs.getString("user_lname");
                	name = String.valueOf(Fname) + " "+ String.valueOf(Lname);
                	
                	empName.setText(name);
                	
                	
                    }
                
                


                Toast.makeText(getApplicationContext(), "FETCH SUCCESS", Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex) {
        	Toast.makeText(getApplicationContext(), "FETCH FAIL", Toast.LENGTH_LONG).show();
            Log.e("MYAPP", "exception", ex);

        }
		
		
		
	}
	
	public void SetPosition()
	{
		Bundle Promotions = getIntent().getExtras();
        
        promID = Promotions.getString("promID");
        empID = Promotions.getString("empID");
        promote = Promotions.getString("Promote");
        recommend = Promotions.getString("recommendation");
        status = Promotions.getString("status");
        endorse = Promotions.getString("endorsed");
		
		PreparedStatement ps;
		ResultSet rs;
		
		String Position;

		
		try {
            Connection con = connectionClass.CONN();
            if (con == null) {
                Toast.makeText(getApplicationContext(), "CONNECTION FAIL", Toast.LENGTH_LONG).show();
            } else {
                String query = "select position_name from position,promotion where promotion.promote_to = position.position_id AND position.position_id = '"+promote+"'";
                ps = con.prepareStatement(query);
                rs = ps.executeQuery();

                ArrayList<String> data1 = new ArrayList<String>();
                while (rs.next()) {
                	
                	
                	
                	Position = rs.getString("position_name");

                	
                	empPosition.setText(Position);
                	
                	
                    }
                

                Toast.makeText(getApplicationContext(), "FETCH SUCCESS", Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex) {
        	Toast.makeText(getApplicationContext(), "FETCH FAIL", Toast.LENGTH_LONG).show();
            Log.e("MYAPP", "exception", ex);

        }
		
	}

	public void SetEndorser()
	{
Bundle Promotions = getIntent().getExtras();
        
        promID = Promotions.getString("promID");
        empID = Promotions.getString("empID");
        promote = Promotions.getString("Promote");
        recommend = Promotions.getString("recommendation");
        status = Promotions.getString("status");
        endorse = Promotions.getString("endorsed");
		
		PreparedStatement ps;
		ResultSet rs;
		
		String Fname,Lname,name;

		
		recommendation.setText(recommend);
		
		try {
            Connection con = connectionClass.CONN();
            if (con == null) {
                Toast.makeText(getApplicationContext(), "CONNECTION FAIL", Toast.LENGTH_LONG).show();
            } else {
                String query = "select user_fname,user_lname from users,promotion  where promotion.employee_id = users.user_id AND promotion.employee_id = '"+endorse+"'";
                ps = con.prepareStatement(query);
                rs = ps.executeQuery();

                ArrayList<String> data1 = new ArrayList<String>();
                while (rs.next()) {
                	
                	
                	
                	Fname = rs.getString("user_fname");
                	Lname = rs.getString("user_lname");
                	name = String.valueOf(Fname) + " "+ String.valueOf(Lname);
                	
                	endorser.setText(name);
                	
                	
                    }
                
                
                


                Toast.makeText(getApplicationContext(), "FETCH SUCCESS", Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex) {
        	Toast.makeText(getApplicationContext(), "FETCH FAIL", Toast.LENGTH_LONG).show();
            Log.e("MYAPP", "exception", ex);

        }
	}
}
