package de.fhbielefeld.ifm;

import java.util.Locale;

import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.*;
import com.actionbarsherlock.view.*;

import de.fhbielefeld.ifm.logic.Singleton;
import de.fhbielefeld.ifm.persistence.DbAccess;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.*;
import android.support.v4.view.ViewPager;

/**
 * This class holds the MainMenuActivity. It displays the HomeFragment,
 * LogbookFragment, MonthRaceFragment and StatisticsFragment. 
 * 
 * @author Dimitri Mantler
 */
public class MainMenuActivity extends SherlockFragmentActivity implements ActionBar.TabListener {
 
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */ 
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
		setContentView(R.layout.activity_mainmenu); 
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
						.setIcon(R.drawable.ic_tab_home_inverse)
						.setTabListener(this));
				break;
			case 1:	
				actionBar.addTab(actionBar.newTab()
						.setIcon(R.drawable.ic_tab_logbook_inverse)
						.setTabListener(this));
				break;
			case 2:	
				actionBar.addTab(actionBar.newTab()
						.setIcon(R.drawable.ic_tab_monthraces_inverse)
						.setTabListener(this));
				break;
			case 3:	
				actionBar.addTab(actionBar.newTab()
						.setIcon(R.drawable.ic_tab_statistics_inverse)
						.setTabListener(this));
				break;
			}
		}
	}

	/**
	 * This method will be automatically called in the Activity-lifecycle
	 * and adds clickable menuitems to the actionbar.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		 menu.add("settings")
         .setIcon(R.drawable.ic_settings_applications_inverse)
         .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		 
		 menu.add("sync")
         .setIcon(R.drawable.ic_update_applications_inverse)
         .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
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
	 * This method will be automatically called if a clickevent is registered
	 * on one of the items inside the actionbar.
	 * settings: starts PreferenceActivity
	 */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	if(item.getTitle().equals("settings")){
    		Intent settingsIntent = new Intent(this, PreferenceActivity.class);
            this.startActivity(settingsIntent);
    	}
        return false;
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
				fragment= new HomeFragment();
				break;
			case 1:
				fragment= new LogbookFragment();
				break;
			case 2:
				fragment= new MonthRaceFragment();
				break;
			case 3:
				fragment= new StatisticsFragment();
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
			// Show 4 pages total.
			return 4;
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
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			case 3:
				return getString(R.string.title_section4).toUpperCase(l);
			}
			return null;
		}
	}

}
