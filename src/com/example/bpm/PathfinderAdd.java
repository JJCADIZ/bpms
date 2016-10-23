package com.example.bpm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;



public class PathfinderAdd extends Fragment implements OnClickListener{
	
	ConnectionClass connectionClass;
    EditText edtideaname, edtbenefit,edtobservation,edtquickwin,targetdate;
    Button btnadd;
    TextView targettv;
    Spinner spinner1, spinner2;
    ProgressBar pbbar;
    String proid;
    

    String name;
    int userID;
    int user = 0;
    
    private EditText edtDue;
	
	private DatePickerDialog dueDatePickerDialog;
	
	private SimpleDateFormat dateFormatter;
    
    
    
    //int idd = ((SetID) getActivity().getApplication()).getID();
    //((MainActivity) getActivity()).getData();
	
	public PathfinderAdd(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
		 
        View rootView = inflater.inflate(R.layout.addpathfinder, container, false); 
        
        
        connectionClass = new ConnectionClass();
        edtideaname = (EditText) rootView.findViewById(R.id.edtideaname);
        edtbenefit = (EditText)  rootView.findViewById(R.id.edtbenefit);
        edtobservation = (EditText) rootView.findViewById(R.id.edyobservation);
        edtquickwin = (EditText) rootView.findViewById(R.id.edtquickwin);
        spinner1 = (Spinner) rootView.findViewById(R.id.spinner1);
        spinner2 = (Spinner) rootView.findViewById(R.id.spinner2);
        btnadd = (Button)  rootView.findViewById(R.id.btnadd);
        pbbar = (ProgressBar)  rootView.findViewById(R.id.pbbar);
        targettv = (TextView) rootView.findViewById(R.id.tvtarget);
        pbbar.setVisibility(View.GONE);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        edtDue = (EditText) rootView.findViewById(R.id.edtDue);
        edtDue.setInputType(InputType.TYPE_NULL);
        edtDue.requestFocus();
        proid = "";
        
        
        //String userId = ((OtherActivity)getActivity()).data;
        //int sample = ((MainActivity)getActivity()).qwe;
        String strtext = getArguments().getString("edttext");
        
        MainActivity getID = new MainActivity();
        String IDD = getID.getMyData();
        
        
        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref",Context. MODE_PRIVATE); 
        Editor editor = pref.edit();
        name = pref.getString("IDD", null); 
        userID = Integer.parseInt(name);
        Toast.makeText(PathfinderAdd.this.getActivity(), name, Toast.LENGTH_SHORT).show();
        
        setDateTimeField();

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddPro addPro = new AddPro();
               
                addPro.execute("");

            }
        });
        
        edtDue.setOnFocusChangeListener(new OnFocusChangeListener() {
    		@Override
    		public void onFocusChange(View v, boolean hasFocus) {
    			if(hasFocus)
    				dueDatePickerDialog.show();
    			v.clearFocus();
    		}
    	});

        return rootView;
        
    }
	
	
	public class AddPro extends AsyncTask<String, String, String> {
		
		

        String z = "";
        Boolean isSuccess = false;

        String observation = edtobservation.getText().toString();
        String quickwin = edtquickwin.getText().toString();
        String ideaname = edtideaname.getText().toString();
        String benefit = edtbenefit.getText().toString();
        
        String process = spinner1.getSelectedItem().toString();
        String benefitType = spinner2.getSelectedItem().toString();
        String lol = "2015-11-28";
        
        Integer benefit_type = spinner2.getSelectedItemPosition();
        Integer idea_type = spinner1.getSelectedItemPosition();
        Integer idea_id;
    	Integer benefit_id;
    	
    	Integer pathfinder_id = 1;
    	Integer pathfinder_status = 10;
    	Integer pathfinder_prog = 0;
    	
    	String date = edtDue.getText().toString();
    	    	
        
        @Override
        protected void onPreExecute() {
            pbbar.setVisibility(View.VISIBLE);
            
           
        }

        @Override
        protected void onPostExecute(String r) {
            pbbar.setVisibility(View.GONE);
            Toast.makeText(PathfinderAdd.this.getActivity(), r, Toast.LENGTH_SHORT).show();
            if(isSuccess==true) {
            	edtideaname.setText(null);
                edtbenefit.setText(null);
                edtobservation.setText(null);
                edtquickwin.setText(null);
            }

        }

		@Override
		protected String doInBackground(String... params) {
			if (ideaname.trim().equals("") || benefit.isEmpty()  || observation.trim().equals("") || quickwin.trim().equals(""))
	            z = "Please fill all the fields";
	        else {
	            try {
	                Connection con = connectionClass.CONN();
	                if (con == null) {
	                    z = "Error in connection with SQL server";
	                } else {
	                	
	                	//timeFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
	                	//finalDate = timeFormat.format(targ);
	                	
	                	//sql = new java.sql.Date(targ.getTime());
	                	
	                	int lol=1;
	                	 Date date2 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date);
	                 	String newDateString = new SimpleDateFormat("yyyy/M/d",Locale.ENGLISH).format(date2);
	                	
	                	 double benefitInt = Double.parseDouble(benefit);
	                
	                	
	                	switch (idea_type)
	                	{
	                	case 0:
	                		idea_id = 1;
	                		break;
	                	case 1:
	                		idea_id = 2;
	                		break;
	                	case 2:
	                		idea_id = 3;
	                		break;
	                	case 3:
	                		idea_id = 4;
	                		break;
	                	case 4:
	                		idea_id = 5;
	                		break;
	                	default:
	                		idea_id = 1;
	                		break;
	                	}
	                	
	                	switch(benefit_type)
	                	{
	                	case 0:
	                		benefit_id = 1;
	                		break;
	                	case 1:
	                		benefit_id = 2;
	                		break;
	                	default:
	                		benefit_id = 1;
	                		break;
	                	}
	           
	                	String query = "insert into pathfinder (pathfinder_name,idea_id,benefit_id,pathfinder_potential_eqv,pathfinder_observation,pathfinder_quickwin,pathfinder_target_closure,pathfinder_status,pathfinder_progress,pathfinder_date_raised,user_id,pathfinder_requestdate)" + 
	                			"values ('" +ideaname+ "','" +idea_id+ "','" +benefit_id+ "','" +benefitInt+ "','<p>" +observation+ "</p>','<p>" +quickwin+ "</p>','" +newDateString+"','" +pathfinder_status+ "','" +pathfinder_prog+ "',GETDATE(),'" + userID+ "',GETDATE())";                                            
	                	PreparedStatement preparedStatement = con.prepareStatement(query);
	                    preparedStatement.executeUpdate();
	                    z = "Added Successfully";
	                    isSuccess = true;
	                }
	            } catch (Exception ex) {
	                isSuccess = false;
	                z = "Exceptions";
	                Log.e("MYAPP", "exception", ex);
	            }
	        }
	        return z;
	    }
			
		}
	

	private void setDateTimeField() {
		edtDue.setOnClickListener(this);

		
		Calendar newCalendar = Calendar.getInstance();
		dueDatePickerDialog = new DatePickerDialog(getActivity(), new OnDateSetListener() {

	        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
	            Calendar newDate = Calendar.getInstance();
	            newDate.set(year, monthOfYear, dayOfMonth);
	            edtDue.setText(dateFormatter.format(newDate.getTime()));
	        }

	    },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
		
		
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == edtDue) {
			dueDatePickerDialog.show();
		} 
	}
	
	
	

	
}
	
	
	

    


