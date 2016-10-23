package com.example.bpm;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class TVEmployeeMenu extends Fragment {
	View rootView;
	Button tracker_btn,running_btn,paused_btn,report_btn;
	ProgressDialog loading;
	public TVEmployeeMenu() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.tv_main, container, false);
		tracker_btn = (Button) rootView.findViewById(R.id.employee_tracker);
		running_btn = (Button) rootView.findViewById(R.id.employee_running);
		paused_btn = (Button) rootView.findViewById(R.id.employee_paused);
		report_btn = (Button) rootView.findViewById(R.id.employee_report);
		paused_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent goPaused = new Intent(getActivity(), pausedActivity.class);
            	startActivity(goPaused);
			}
		});
		tracker_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent goTracker = new Intent(getActivity(), trackerActivity.class);
				startActivity(goTracker);
			}
		});
		running_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent goRunning = new Intent(getActivity(), runningActivity.class);
				startActivity(goRunning);
			}
		});
		report_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent goReport = new Intent(getActivity(), myreportActivity.class);
				startActivity(goReport);
			}
		});
		return rootView;
	}
	
}
