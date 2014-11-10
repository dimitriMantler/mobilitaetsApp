package de.fhbielefeld.ifm;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import de.fhbielefeld.ifm.logic.Singleton;
import de.fhbielefeld.ifm.logic.Stats;
import de.fhbielefeld.ifm.logic.WebserviceInterface;
import de.fhbielefeld.ifm.util.ShareBar;
import de.fhbielefeld.ifm.util.Util;
import de.fhbielefeld.ifm.util.ShareBar.Size;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * This class holds the StatisticsFragment which is nested in the MainMenuActivity. 
 * It displays stats for the last and current month and also total values. 
 * 
 * @author Dimitri Mantler
 */
public class StatisticsFragment extends SherlockFragment {

	Stats stats;
	
	ImageView ivSharesTotal;
	ImageView ivSharesCurrent;
	ImageView ivSharesLast;
	
	TableLayout tlTotal;
	TableLayout tlCurrent;
	TableLayout tlLast;
	TextView tvTransport;
	TextView tvCount;
	TextView tvDistance;
	TableRow trRow;
	//TODO should be more dynamic, from Transportation-Enum
	String[] transport={"Strecken","zu Fuﬂ","Fahrrad","Anders1","Bus","Zug","Schiff","Anders2","Auto","Motorad","Flugzeug","Anders3"};
	int counter=0;
	
	public static final String ARG_SECTION_NUMBER = "section_number";

	public StatisticsFragment() {
	}

	/**
	 * This method will be automatically called in the Activity-lifecycle
	 * and initializes the GUI.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		View rootView = inflater.inflate(R.layout.activity_statistics, container, false);
		
		tlTotal=(TableLayout)rootView.findViewById(R.id.tlTotal);
		tlCurrent=(TableLayout)rootView.findViewById(R.id.tlCurrent);
		tlLast=(TableLayout)rootView.findViewById(R.id.tlLast);
		
		ivSharesTotal=(ImageView)rootView.findViewById(R.id.ivSharesTotal);
		ivSharesCurrent=(ImageView)rootView.findViewById(R.id.ivSharesCurrent);
		ivSharesLast=(ImageView)rootView.findViewById(R.id.ivSharesLast);

		update();
		
		return rootView;
	}
	
	/**
	 * This method sets the title for this fragment.
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    super.onCreateOptionsMenu(menu, inflater);
	    getSherlockActivity().setTitle(getString(R.string.title_activity_statistics_fragment));
	}
	
	/**
	 * This method will be automatically called if a clickevent is registered
	 * on one of the items inside the actionbar.
	 * update: requests fresh data and calls the update-method
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
     * This method fills the TableViews with data from the Stats-object and creates ShareBars.
     */
    public void update(){
    	stats=Singleton.getInstance().getPlayer().getStats();
    	
    	TextView tvTitelCurrent=new TextView(getSherlockActivity());
    	tvTitelCurrent.setText(getSherlockActivity().getText(R.string.current));
    	tvTitelCurrent.setTextSize(20);
    	TextView tvTitelLast=new TextView(getSherlockActivity());
    	tvTitelLast.setText(getSherlockActivity().getText(R.string.last));
    	tvTitelLast.setTextSize(20);
    	TextView tvTitelTotal=new TextView(getSherlockActivity());
    	tvTitelTotal.setText(getSherlockActivity().getText(R.string.total));
    	tvTitelTotal.setTextSize(20);
    	
    	tlCurrent.removeAllViews();
    	tlLast.removeAllViews();
    	tlTotal.removeAllViews();
    	
    	tlCurrent.addView(tvTitelCurrent);
    	tlLast.addView(tvTitelLast);
    	tlTotal.addView(tvTitelTotal);
    	
    	ivSharesTotal.setImageBitmap(ShareBar.createShareBar(getSherlockActivity(), stats.getShares().get(0), stats.getShares().get(1), stats.getShares().get(2), Size.WIDE));
		ivSharesCurrent.setImageBitmap(ShareBar.createShareBar(getSherlockActivity(), stats.getShares().get(3), stats.getShares().get(4), stats.getShares().get(5), Size.WIDE));
		ivSharesLast.setImageBitmap(ShareBar.createShareBar(getSherlockActivity(), stats.getShares().get(6), stats.getShares().get(7), stats.getShares().get(8), Size.WIDE));
		
		counter=0;
		for(int i=0;i<(stats.getWaysCount().size()/3);i++){
			tvTransport = new TextView(getSherlockActivity());
			tvTransport.setWidth(200);
			tvCount = new TextView(getSherlockActivity());
			tvCount.setWidth(200);
			tvDistance = new TextView(getSherlockActivity());
			tvDistance.setWidth(200);
			tvTransport.setText(transport[counter]);
			tvCount.setText(stats.getWaysCount().get(i)+"");
			tvDistance.setText(stats.getWaysDistance().get(i)+" km");
			
			trRow = new TableRow(getSherlockActivity());
			trRow.addView(tvTransport);
			trRow.addView(tvCount);
			trRow.addView(tvDistance);
			
			tlTotal.addView(trRow);
			counter++;
		}
		counter=0;
		for(int i=stats.getWaysCount().size()/3;i<((stats.getWaysCount().size()/3)*2);i++){
			tvTransport = new TextView(getSherlockActivity());
			tvTransport.setWidth(200);
			tvCount = new TextView(getSherlockActivity());
			tvCount.setWidth(200);
			tvDistance = new TextView(getSherlockActivity());
			tvDistance.setWidth(200);
			tvTransport.setText(transport[counter]);
			tvCount.setText(stats.getWaysCount().get(i)+"");
			tvDistance.setText(stats.getWaysDistance().get(i)+" km");
			
			trRow = new TableRow(getSherlockActivity());
			trRow.addView(tvTransport);
			trRow.addView(tvCount);
			trRow.addView(tvDistance);
			
			tlCurrent.addView(trRow);
			counter++;
		}
		counter=0;
		for(int i=(stats.getWaysCount().size()/3)*2;i<stats.getWaysCount().size();i++){
			tvTransport = new TextView(getSherlockActivity());
			tvTransport.setWidth(200);
			tvCount = new TextView(getSherlockActivity());
			tvCount.setWidth(200);
			tvDistance = new TextView(getSherlockActivity());
			tvDistance.setWidth(200);
			tvTransport.setText(transport[counter]);
			tvCount.setText(stats.getWaysCount().get(i)+"");
			tvDistance.setText(stats.getWaysDistance().get(i)+" km");
			
			trRow = new TableRow(getSherlockActivity());
			trRow.addView(tvTransport);
			trRow.addView(tvCount);
			trRow.addView(tvDistance);
			
			tlLast.addView(trRow);
			counter++;
		}
    }
}
