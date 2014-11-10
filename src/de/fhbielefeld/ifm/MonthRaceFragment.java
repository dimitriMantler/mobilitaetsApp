package de.fhbielefeld.ifm;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import de.fhbielefeld.ifm.adapter.MonthRaceExpandableListAdapter;
import de.fhbielefeld.ifm.logic.MonthRace;
import de.fhbielefeld.ifm.logic.Singleton;
import de.fhbielefeld.ifm.logic.WebserviceInterface;
import de.fhbielefeld.ifm.util.Dummydata;
import de.fhbielefeld.ifm.util.Util;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

/**
 * This class holds the MonthRaceFragment which is nested in the MainMenuActivity. 
 * It displays an ExpandableListView containing 
 * MonthRace-objects divided into three groups. The first and second group 
 * contain the races the user has signed up to. the third contains the available races.
 * 
 * @author Dimitri Mantler
 */
public class MonthRaceFragment extends SherlockFragment implements OnChildClickListener{

	ExpandableListView elv1;
	private MonthRaceExpandableListAdapter adapter2;
	List<String> listDataHeader;
    HashMap<String, List<MonthRace>> listDataChild;
	ArrayList<MonthRace> entries;

	public MonthRaceFragment() {
	}

	/**
	 * This method will be automatically called in the Activity-lifecycle
	 * and initializes the GUI.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		View rootView = inflater.inflate(R.layout.activity_monthrace, container, false);
		elv1 = (ExpandableListView) rootView.findViewById(R.id.expandableListView1);
		elv1.setOnChildClickListener(this);
		entries=new ArrayList<MonthRace>();
		entries.addAll(Singleton.getInstance().getPlayer().getMonthraces());
		
		update();
		
		return rootView;
	}
	
	/**
	 * this method divides all the MonthRaces from the user into those 
	 * which are currently running, depending on the starttime for each 
	 * race and the local systemtime. 
	 * The available races will be put into the third group.
	 */
	public void prepareData(){
    	
    	entries.clear();
		entries.addAll(Singleton.getInstance().getPlayer().getMonthraces());
		
    	listDataHeader =new ArrayList<String>();
    	listDataChild = new HashMap<String, List<MonthRace>>();
    	
    	ArrayList<MonthRace> running=new ArrayList<MonthRace>();
    	ArrayList<MonthRace> signedup=new ArrayList<MonthRace>();
    	
    	listDataHeader.add(getText(R.string.running)+"");
    	listDataHeader.add(getText(R.string.signedup)+"");
    	listDataHeader.add(getText(R.string.available)+"");
    	
    	GregorianCalendar gc=new GregorianCalendar();
    	
    	//Adding all signed up raced 
    	for(int i=0;i<entries.size();i++){
    		if((gc.compareTo(entries.get(i).getStartDate())>0))
    			running.add(entries.get(i));
    		else
    			signedup.add(entries.get(i));
    	}
    	
    	listDataChild.put(listDataHeader.get(0), running);
    	listDataChild.put(listDataHeader.get(1), signedup);
    	
    	//Adding available races
    	//TODO dummytest
		if(PreferenceManager.getDefaultSharedPreferences(getSherlockActivity()).getBoolean("checkbox_dummydata", true)){
			listDataChild.put(listDataHeader.get(2), Dummydata.getAvailableDummyRaces());
		}
		else if(Util.networkAvailable()){
			listDataChild.put(listDataHeader.get(2),new WebserviceInterface().getAvailableRaces());
		}
	}

	/**
	 * This method will be triggered by clicking an item in the ExpandableListView.
	 * It starts the MonthRaceDetails and adds the id of the item. 
	 * A boolean value will be added depending on which group contains the clicked item.
	 */
	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
			long raceid=Long.parseLong(v.getTag().toString());
			Intent details=new Intent(getSherlockActivity(), MonthRaceDetailsActivity.class);
			details.putExtra("id", raceid);
			System.out.println(v.getTag());
			if(groupPosition<2)
				details.putExtra("signedUp", true);
			else
				details.putExtra("signedUp", false);
			this.startActivity(details);
		return false;
	}
	
	/**
	 * This method sets the title for this fragment.
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    super.onCreateOptionsMenu(menu, inflater);
	    getSherlockActivity().setTitle(getString(R.string.title_activity_month_race_fragment));
	}
	
	/**
	 * This method will be automatically called if a clickevent is registered
	 * on one of the items inside the actionbar.
	 * sync: requests fresh data and calls the update-method
	 */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	if(item.getTitle().equals("sync")){
    		//TODO dummytest
    		if(!PreferenceManager.getDefaultSharedPreferences(getSherlockActivity())
    				.getBoolean("checkbox_dummydata", true)&&Util.networkAvailable()){
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
    	if(entries.size()>0){
			prepareData();
			adapter2 = new MonthRaceExpandableListAdapter(getSherlockActivity(),listDataHeader,listDataChild,entries);
			elv1.setAdapter(adapter2);
			elv1.expandGroup(0);
			elv1.collapseGroup(2);
		}
		adapter2.notifyDataSetChanged();
    }
}
