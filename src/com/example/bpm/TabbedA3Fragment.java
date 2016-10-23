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
 
public class TabbedA3Fragment extends Fragment {
 
    public static final String TAG = TabbedA3Fragment.class.getSimpleName();
    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
 
     
    public static TabbedA3Fragment newInstance() {
        return new TabbedA3Fragment();
    }
     
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
                  fragment = new A3EmpAdd();
                  break;
                case 1:
                  fragment = new A3Update();
                  break;
                case 2:
                  fragment = new A3ApproveProjects();
                  break;
                case 3:
                    fragment = new A3ApproveClosure();
                    break;
                }
                
                
                fragment.setArguments(args);
                return fragment;
            }
        
 
        @Override
        public int getCount() {
            return 4;
        }
 
        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
            case 0:
                return getString(R.string.add_path).toUpperCase(l);
               
            case 1:
                return getString(R.string.view).toUpperCase(l);
            case 2:
                return getString(R.string.penda3).toUpperCase(l);
            case 3:
                return getString(R.string.penda4).toUpperCase(l);
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
            View rootView = inflater.inflate(R.layout.addpathfinder,
                    container, false);
            TextView dummyTextView = (TextView) rootView
                    .findViewById(R.id.section_label);
            dummyTextView.setText(Integer.toString(getArguments().getInt(
                    ARG_SECTION_NUMBER)));
            return rootView;
        }
    }
 
}