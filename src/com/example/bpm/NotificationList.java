package com.example.bpm;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Fragment;
import android.content.ClipData.Item;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class NotificationList extends Fragment {
	ConnectionClass connectionClass;
	ListView notiflist;
	ArrayAdapter<String> adapter;
	List<String> notifs;
	List<Integer> notifs_id;
	List<Item> notifdata;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.notificationlist, container, false);
		connectionClass = new ConnectionClass();
		notiflist = (ListView) rootView.findViewById(R.id.notificationlist);
		notiflist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position,
					long arg3) {
				// TODO Auto-generated method stub
				
			}
			
		});
		addNotifs();
		return rootView;
	}
	
	public void addNotifs(){
		notifdata = new ArrayList<Item>();
		notifs = new ArrayList<String>();
        notifs_id = new ArrayList<Integer>();
        getNotifs();
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1 , notifs);
        notiflist.setAdapter(adapter);
	}
	
	public void getNotifs(){
		SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref",Context. MODE_PRIVATE); 
        int id = Integer.parseInt(pref.getString("IDD", null));
        try {
        	Connection con = connectionClass.CONN();
        	if(con == null){
        		Toast.makeText(getActivity(), "Erron in connection", Toast.LENGTH_SHORT).show();
        	}else{
        		String getnotifs = "select * from notifications where notification_recipient = "+id+ 
        				" order by notification_datecreated desc";
        		Statement notif_stmt = con.createStatement();
        		ResultSet notif_rs = notif_stmt.executeQuery(getnotifs);
        		while(notif_rs.next()){
        			notifs_id.add(notif_rs.getInt("notification_id"));
        			notifs.add(Html.fromHtml(notif_rs.getString("notification_subject")).toString());
        			//notifdata.add(new Item("as","asda",""));
        		}
        	}
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		addNotifs();
		super.onResume();
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

}
