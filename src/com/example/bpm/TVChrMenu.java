package com.example.bpm;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TVChrMenu extends Fragment {
	View rootView;
	Button tracker_btn,running_btn,paused_btn,director_report_btn,employee_report_btn;
	ProgressDialog loading;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.tvchrmenu, container, false);
		director_report_btn = (Button) rootView.findViewById(R.id.chr_director_report);
		employee_report_btn = (Button) rootView.findViewById(R.id.chr_employee_report);
		director_report_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent goDirectorReport = new Intent(getActivity(), ChrDirectorReport.class);
				startActivity(goDirectorReport);
			}
		});
		employee_report_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent goReport = new Intent(getActivity(), ChrEmployeeReport.class);
				startActivity(goReport);
			}
		});
		return rootView;
	}
}
