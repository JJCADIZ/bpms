package com.example.bpm;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class EmployeeReports extends Fragment {
	View rootView;
	Button okr_btn,a3_btn,pathfinder_btn,tv_btn;
	ProgressDialog loading;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.reports_employee_menu, container, false);
		okr_btn = (Button) rootView.findViewById(R.id.employee_okr_reports);
		a3_btn = (Button) rootView.findViewById(R.id.employee_a3_reports);
		pathfinder_btn = (Button) rootView.findViewById(R.id.employee_pathfinder_reports);
		tv_btn = (Button) rootView.findViewById(R.id.employee_tv_reports);
		okr_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent goOkr = new Intent(getActivity(), reports_okr_activity.class);
            	startActivity(goOkr);
			}
		});
		a3_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent goPaused = new Intent(getActivity(), pausedActivity.class);
            	startActivity(goPaused);
			}
		});
		pathfinder_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent goPaused = new Intent(getActivity(), pausedActivity.class);
            	startActivity(goPaused);
			}
		});
		tv_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent goPaused = new Intent(getActivity(), pausedActivity.class);
            	startActivity(goPaused);
			}
		});
		return rootView;
	}
	
}
