package de.fhbielefeld.ifm;

import java.util.Locale;

import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.*;

import de.fhbielefeld.ifm.logic.Singleton;
import de.fhbielefeld.ifm.persistence.DbAccess;

import android.os.Bundle;
import android.support.v4.app.*;
import android.support.v4.view.ViewPager;

/**
 * This class holds the MyMedalsActivity. It displays the OwnMedalsFragment,
 * EarnedMedalsFragment. 
 * 
 * @author Dimitri Mantler
 */
public class MyMedalsActivity extends SherlockFragmentActivity implements ActionBar.TabListener {
 
	SectionsPagerAdapter mSectionsPagerAdapter; 

	ViewPager mViewPager; 

	/**
	 * This method will be automatically called in the Activity-lifecycle
	 * and initializes the ViewPager to display the given fragments, 
	 * also adds tab- and swipe-navigation.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.activity_mymedals); 
		Singleton.getInstance().setContext(this);
		
		
		// Set up the action bar.
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});
		
		
		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			//create a tab with the corresponding icon
			switch(i){
			case 0:
				actionBar.addTab(actionBar.newTab()
						.setText(R.string.own_medals)
						.setTabListener(this));
				break;
			case 1:	
				actionBar.addTab(actionBar.newTab()
						.setText(R.string.earned_medals)
						.setTabListener(this));
				break;
			}
		}
	}
    
	/**
	 * This method will be automatically called in the Activity-lifecycle
	 * and loads the userdata from the local database, if the userdata has been 
	 * deleted by the system.
	 */
	@Override
	public void onStart(){
		if(Singleton.getInstance().getPlayer()==null)
			DbAccess.loadData();
		super.onStart();
	}
	
	/**
     * This method sets the ViewPager to the selectes tab
     */
	@Override
	public void onTabSelected(Tab tab, android.support.v4.app.FragmentTransaction ft) {
		// When the given tab is selected, switch to the corresponding page in
				// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}
	
	@Override
	public void onTabUnselected(Tab tab, android.support.v4.app.FragmentTransaction ft) {

	}

	@Override
	public void onTabReselected(Tab tab, android.support.v4.app.FragmentTransaction ft) {

	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment= null;
			switch(position){
			case 0:
				fragment= new OwnMedalsFragment();
				break;
			case 1:
				fragment= new EarnedMedalsFragment();
				break;
			}
			
			Bundle args = new Bundle();
			args.putInt(HomeFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			return fragment;
		}

		/**
		 * This method returns the pagecount
		 * @return The pagecount
		 */
		@Override
		public int getCount() {
			// Show 2 pages total.
			return 2 ;
		}

		/**
		 * This method returns the title for the requestet fragment
		 * @return String containing the title
		 */
		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.own_medals).toUpperCase(l);
			case 1:
				return getString(R.string.earned_medals).toUpperCase(l);
			}
			return null;
		}
	}

}
