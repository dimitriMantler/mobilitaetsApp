package de.fhbielefeld.ifm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuInflater;

import de.fhbielefeld.ifm.adapter.LogbookExpandableListAdapter;
import de.fhbielefeld.ifm.logic.LogbookEntry;
import de.fhbielefeld.ifm.logic.Singleton;
import de.fhbielefeld.ifm.logic.WebserviceInterface;
import de.fhbielefeld.ifm.util.Util;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;

/**
 * This class holds the LogbookFragment which is nested in the MainMenuActivity. 
 * It displays the users logbookentries divided into groups for each month/year. 
 * 
 * @author Dimitri Mantler
 */
public class LogbookFragment extends SherlockFragment implements OnChildClickListener{

	TextView tvNoLogs;
	private ExpandableListView elv1;
	private LogbookExpandableListAdapter adapter2;
	ArrayList<LogbookEntry> entries;
	List<String> listDataHeader;
    HashMap<String, List<LogbookEntry>> listDataChild;

	public LogbookFragment() {
	}

	/**
	 * This method will be automatically called in the Activity-lifecycle
	 * and initializes the GUI.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		View rootView = inflater.inflate(R.layout.activity_logbook, container, false);
		elv1 = (ExpandableListView) rootView.findViewById(R.id.expandableListView1); 
		tvNoLogs=(TextView)rootView.findViewById(R.id.tvNoLogs);
		
		update();
		return rootView;
	}
	
	/**
	 * This method will be automatically called in the Activity-lifecycle
	 * and updates the GUI if the fragment was paused.
	 */
	@Override
	public void onResume(){
		super.onResume();
		System.out.println("LogbookFragment onResume");
		prepareData();
		adapter2 = new LogbookExpandableListAdapter(getSherlockActivity(),listDataHeader,listDataChild,entries);
		elv1.setAdapter(adapter2);
		elv1.expandGroup(0);
	}
	
	/**
	 * This method will be automatically called in the Activity-lifecycle
	 * and adds a clickable menuitem to the actionbar.
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    super.onCreateOptionsMenu(menu, inflater);
	    getSherlockActivity().setTitle(getString(R.string.title_activity_logbook_fragment));
	    
	    menu.add("add")
	    .setIcon(R.drawable.ic_add_applications_inverse)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
	}

	/**
	 * This method will be automatically called if a clickevent is registered
	 * on one of the items inside the actionbar.
	 * add: starts the TransportationListActivity
	 * sync: synchronizes Logbookentries, requests fresh data and calls the update-method
	 */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	if(item.getTitle().equals("add")){
	        getSherlockActivity().startActivity(new Intent(getSherlockActivity(), TransportationListActivity.class));
    	}
    	else if(item.getTitle().equals("sync")){//could also be used for updates or downloading older logs //TODO
    		//TODO dummytest
    		if(!PreferenceManager.getDefaultSharedPreferences(getSherlockActivity())
    				.getBoolean("checkbox_dummydata", true)&&Util.networkAvailable()){
    			new WebserviceInterface().syncAllLogs();
    			new WebserviceInterface().getMyData(-1);
    		}
    		update();
    	}
        return true;
    }
	
    /**
	 * This method updates the GUI.
	 */
    public void update(){
    	entries= new ArrayList<LogbookEntry>();
		Util.sortEntries(Singleton.getInstance().getPlayer());
		entries.addAll(Singleton.getInstance().getPlayer().getLogbook());
		
		if(entries.size()>0){
			prepareData();
			
			adapter2 = new LogbookExpandableListAdapter(getSherlockActivity(),listDataHeader,listDataChild,entries);
			elv1.setAdapter(adapter2);
			elv1.setOnChildClickListener(this);
			elv1.expandGroup(0);
		}
		updateList();	
    }
    
    /**
     * This method makes the list containing the logbookentries visible if there are more than 0 items,
     * or else it displays a text which indicates that there are no entries.
     */
	public void updateList(){
		if(entries.size()>0){
			tvNoLogs.setVisibility(TextView.INVISIBLE);
			elv1.setVisibility(ExpandableListView.VISIBLE);
		}
		else{
			tvNoLogs.setVisibility(TextView.VISIBLE);
		}
	}
	
	/**
	 * this method divides all the logbookentries into groups depending 
	 * on the month/year of their creation, generates specific headers for each group
	 * and puts them into the HashMap.
	 */
	public void prepareData(){
        int curMonth;
    	int curYear;
    	
    	entries.clear();
		entries.addAll(Singleton.getInstance().getPlayer().getLogbook());
		
    	listDataHeader =new ArrayList<String>();
    	listDataChild = new HashMap<String, List<LogbookEntry>>();
    	
    	ArrayList<LogbookEntry> children;
    	if(entries.size()>0){
    		curYear=entries.get(0).getDate().get(Calendar.YEAR);
    		curMonth=entries.get(0).getDate().get(Calendar.MONTH);
    		
    		children=new ArrayList<LogbookEntry>();
    		children.add(entries.get(0));
    		listDataHeader.add(String.format("%1$tB %1$tY", entries.get(0).getDate()));
    		
    		//if the year or month differs put current child list in hashmap and create a new header, 
    		//also create a new list and put the first child in
    		for(int i=1;i<entries.size();i++){
    			if((curYear!=entries.get(i).getDate().get(Calendar.YEAR))||(curMonth!=entries.get(i)
    					.getDate().get(Calendar.MONTH))){
    				curYear=entries.get(i).getDate().get(Calendar.YEAR);
    				curMonth=entries.get(i).getDate().get(Calendar.MONTH);
    				listDataChild.put(listDataHeader.get(listDataHeader.size()-1), children);
    				listDataHeader.add(String.format("%1$tB %1$tY", entries.get(i).getDate()));
    				children=new ArrayList<LogbookEntry>();
    				children.add(entries.get(i));
    			}
    			else{
    				children.add(entries.get(i));
    			}
    		}
			listDataChild.put(listDataHeader.get(listDataHeader.size()-1), children);
    	}
	}

	/**
	 * This method will be triggered by clicking an item in the ExpandableListView.
	 * It starts the LogbookEntryDetailsActivity and adds the position of the item.
	 */
	@Override
	public boolean onChildClick(ExpandableListView arg0, View arg1, int arg2,
			int arg3, long arg4) {
		int i=Integer.valueOf(arg1.getTag().toString());
		Intent details=new Intent(getSherlockActivity(),LogbookEntryDetailsActivity.class);
		details.putExtra("position", i);
		startActivity(details);
		
		return false;
	}
	

}
