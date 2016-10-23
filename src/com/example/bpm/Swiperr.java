package com.example.bpm;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class Swiperr extends Fragment
{
    View rootView;
    
    

public Swiperr () 
{
    // Empty constructor required for fragment subclasses
}

@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
{   
	
	

// Apply the layout for the fragment
	int tabCount = getActivity().getActionBar().getTabCount();
	
	if (tabCount == 3)
	{
		getActivity().getActionBar().removeAllTabs();
	}else if (tabCount == 0 )
	{
		getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	}
	
	

rootView = inflater.inflate(R.layout.addpathfinder, container, false);


getActivity().setTitle("Pathfinder");


ActionBar.TabListener tabListener = new ActionBar.TabListener() {
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        // show the given tab
    }

    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        // hide the given tab
    }

    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
        // probably ignore this event
    }
};

// Add 3 tabs, specifying the tab's text and TabListener

    getActivity().getActionBar().addTab(
            getActivity().getActionBar().newTab()
            .setText("LOL Form ")
            .setTabListener(tabListener));
    
    getActivity().getActionBar().addTab(
            getActivity().getActionBar().newTab()
            .setText("QWE Form ")
            .setTabListener(tabListener));
    
    getActivity().getActionBar().addTab(
            getActivity().getActionBar().newTab()
            .setText("ASD Form ")
            .setTabListener(tabListener));


return rootView;
}
}