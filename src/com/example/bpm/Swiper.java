package com.example.bpm;


import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class Swiper extends FragmentActivity
{
	ViewPager Tab;
    View rootView;
    ActionBar actionBar;
    TabPagerAdapter TabAdapter;
    
    

public Swiper () 
{
    // Empty constructor required for fragment subclasses
}

@Override
public void onCreate(Bundle savedInstanceState)
{   
	super.onCreate(savedInstanceState);
	 setContentView(R.layout.addpathfinder);

	
	TabAdapter = new TabPagerAdapter(getSupportFragmentManager());
	
    Tab = (ViewPager)findViewById(R.id.pager);
    Tab.setOnPageChangeListener(
            new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                   
                	actionBar = getActionBar();
                	actionBar.setSelectedNavigationItem(position);                    }
            });
    Tab.setAdapter(TabAdapter);

// Apply the layout for the fragment
	int tabCount = getActionBar().getTabCount();
	
	if (tabCount == 3)
	{
		getActionBar().removeAllTabs();
	}else if (tabCount == 0 )
	{
		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	}
	
	



getActionBar().setTitle("Pathfinder");


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

    getActionBar().addTab(
            getActionBar().newTab()
            .setText("Add Form ")
            .setTabListener(tabListener));
    
    getActionBar().addTab(
            getActionBar().newTab()
            .setText("Update Form ")
            .setTabListener(tabListener));

    getActionBar().addTab(
            getActionBar().newTab()
            .setText("Delete Form ")
            .setTabListener(tabListener));

}
}