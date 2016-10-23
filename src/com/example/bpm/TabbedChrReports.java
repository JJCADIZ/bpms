package com.example.bpm;

import java.util.Locale;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TabbedChrReports extends Fragment {
	public static final String TAG = TabbedTandVFragment.class.getSimpleName();
	SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    
    public static TabbedChrReports newInstance(){
		return new TabbedChrReports();
    }
    
	public TabbedChrReports() {
		// TODO Auto-TabbedChrReports constructor stub
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_tabbed, container, false);
        mSectionsPagerAdapter = new SectionsPagerAdapter(
                getChildFragmentManager());
         
        mViewPager = (ViewPager) v.findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
         
        return v;
	}

	public class SectionsPagerAdapter extends FragmentPagerAdapter {
		 
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }
 
        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new TabbedContentFragment();
            Bundle args = new Bundle();
            args.putInt(TabbedContentFragment.ARG_SECTION_NUMBER, position + 1);
           
                switch(position) {
                case 0:
                  fragment = new OKR_Reports_CHR();
                  break;
                case 1:
                  fragment = new A3_Reports_CHR();
                  break;
                case 2:
                  fragment = new PathfinderReportsCHR();
                  break;
                }
                
                
                fragment.setArguments(args);
                return fragment;
            }
        
 
        @Override
        public int getCount() {
            return 3;
        }
 
        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
            case 0:
                return getString(R.string.reports_okr).toUpperCase(l);
               
            case 1:
                return getString(R.string.reports_a3).toUpperCase(l);
            case 2:
                return getString(R.string.reports_pathfinder).toUpperCase(l);
            }
            return null;
        }
    }
 
    public static class TabbedContentFragment extends Fragment {
 
        public static final String ARG_SECTION_NUMBER = "section_number";
 
        public TabbedContentFragment() {
        }
 
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.okr_reports,
                    container, false);
            TextView dummyTextView = (TextView) rootView
                    .findViewById(R.id.section_label);
            dummyTextView.setText(Integer.toString(getArguments().getInt(
                    ARG_SECTION_NUMBER)));
            return rootView;
        }
    }
}
