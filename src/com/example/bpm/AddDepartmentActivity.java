package com.example.bpm;


import java.io.Serializable;
import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;



public class AddDepartmentActivity extends Activity{
	
	ListView myListView;
	Button getResult;
	ConnectionClass connectionClass;

	    private ArrayList<String> dept_names_list = new ArrayList<String>();
	    public ArrayList<Integer> emp_id_list = new ArrayList<Integer>();

	MyArrayAdapter myArrayAdapter;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_departments);
        connectionClass = new ConnectionClass();
        
        
        //PAGKUHA NG RESULTS SA DB
        try {
            Connection con = connectionClass.CONN();
            if (con == null) {
                Toast.makeText(getApplicationContext(), "CONNECTION FAIL", Toast.LENGTH_LONG).show();
            } else {
                String query = "select * from departments";
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();

                ArrayList<String> data1 = new ArrayList<String>();
                while (rs.next()) {
                	String code =rs.getString("department_code");

                	dept_names_list.add(code);
                	
                }


                Toast.makeText(getApplicationContext(), "FETCH SUCCESS", Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex) {
        	Toast.makeText(getApplicationContext(), "FETCH FAIL", Toast.LENGTH_LONG).show();
            Log.e("MYAPP", "exception", ex);

        }

        myListView = (ListView)findViewById(R.id.member_list);
        
        //PARA SA LAYOUT
        myArrayAdapter = new MyArrayAdapter(
        		this,
        		R.layout.name_row,
        		android.R.id.text1,
        		dept_names_list
        		);
        
        myListView.setAdapter(myArrayAdapter);
        myListView.setOnItemClickListener(myOnItemClickListener);
        
        getResult = (Button)findViewById(R.id.getresult);
        getResult.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				String result = "";
				
				/*
				//getCheckedItemPositions
				List<Integer> resultList = myArrayAdapter.getCheckedItemPositions();
				for(int i = 0; i < resultList.size(); i++){
					result += String.valueOf(resultList.get(i)) + " ";
				}
				*/
				
				//getCheckedItems
				List<String> resultList = myArrayAdapter.getCheckedItems();
				for(int i = 0; i < resultList.size(); i++){
					result += String.valueOf(resultList.get(i)) + "\n";
				}
				
				myArrayAdapter.getCheckedItemPositions().toString();
				
				String samp = "";
            	samp = myArrayAdapter.getCheckedItems().toString().trim();
				
				
			 	 SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE); 
				 Editor editor = pref.edit();
				 editor.putString("departments", samp );  
				 editor.commit(); // commit changesed
				 finish();
				
			}});
        
    }
    
    OnItemClickListener myOnItemClickListener = new OnItemClickListener(){

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			myArrayAdapter.toggleChecked(position);
			
			
		}};
    
    private class MyArrayAdapter extends ArrayAdapter<String>{
    	
    	private HashMap<Integer, Boolean> myChecked = new HashMap<Integer, Boolean>();

		public MyArrayAdapter(Context context, int resource,
				int textViewResourceId, List<String> objects) {
			super(context, resource, textViewResourceId, objects);
			
			for(int i = 0; i < objects.size(); i++){
				myChecked.put(i, false);
			}
		}
    	
		public void toggleChecked(int position){
			if(myChecked.get(position)){
				myChecked.put(position, false);
			}else{
				myChecked.put(position, true);
			}
			
			notifyDataSetChanged();
		}
		
		public List<Integer> getCheckedItemPositions(){
			List<Integer> checkedItemPositions = new ArrayList<Integer>();
			
			for(int i = 0; i < myChecked.size(); i++){
				if (myChecked.get(i)){
					(checkedItemPositions).add(i);
				}
			}
			
			return checkedItemPositions;
		}
		
		public List<String> getCheckedItems(){
			List<String> checkedItems = new ArrayList<String>();
			
			for(int i = 0; i < myChecked.size(); i++){
				if (myChecked.get(i)){
					(checkedItems).add(dept_names_list.get(i));
				}
			}
			
			return checkedItems;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			
			if(row==null){
				LayoutInflater inflater=getLayoutInflater();
				row=inflater.inflate(R.layout.name_row, parent, false); 	
			}
			
			CheckedTextView checkedTextView = (CheckedTextView)row.findViewById(R.id.checkedTextView);
			checkedTextView.setText(dept_names_list.get(position));
			
			Boolean checked = myChecked.get(position);
			if (checked != null) {
				checkedTextView.setChecked(checked);
            }
			
			return row;
		}
		
    }
    
   

}