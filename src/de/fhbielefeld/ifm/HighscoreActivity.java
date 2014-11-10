package de.fhbielefeld.ifm;

import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import de.fhbielefeld.ifm.adapter.OtherPlayerHighscoreListAdapter;
import de.fhbielefeld.ifm.logic.OtherPlayer;
import de.fhbielefeld.ifm.logic.Player;
import de.fhbielefeld.ifm.logic.Singleton;
import de.fhbielefeld.ifm.logic.WebserviceInterface;
import de.fhbielefeld.ifm.persistence.DbAccess;
import de.fhbielefeld.ifm.util.Dummydata;
import de.fhbielefeld.ifm.util.Util;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ListView;

/**
 * This class holds the FriendlistActivity. 
 * It displays the users rank and in a ListView the 100 top players. 
 * 
 * @author Dimitri Mantler
 */
public class HighscoreActivity extends SherlockActivity {
	private ListView lvMe;
	private OtherPlayerHighscoreListAdapter myscorelist;
	private ArrayList<OtherPlayer> myscore;
	private ArrayList<Long> myrank;
	
	private ListView lvHighScore;
	private OtherPlayerHighscoreListAdapter scorelist;
	private ArrayList<OtherPlayer> highscore;
	private ArrayList<Long> ranks;
	
	
	/**
	 * This method will be automatically called in the Activity-lifecycle
	 * and initializes the GUI.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		highscore = new ArrayList<OtherPlayer>();
		setContentView(R.layout.activity_highscore);
		lvMe=(ListView)findViewById(R.id.lvMyscore);
		lvHighScore=(ListView)findViewById(R.id.lvHighscore);
		update();
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
	 * This method will be automatically called in the Activity-lifecycle
	 * and adds a clickable menuitem to the actionbar.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("update")
        .setIcon(R.drawable.ic_update_applications_inverse)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}
	
	/**
	 * This method will be automatically called if a clickevent is registered
	 * on one of the items inside the actionbar.
	 * update: requests fresh data and calls the update-method
	 */
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	if(item.getTitle().equals("update")){
    		//TODO dummytest
    		if(!PreferenceManager.getDefaultSharedPreferences(this)
    				.getBoolean("checkbox_dummydata", true)&&Util.networkAvailable()){
    			new WebserviceInterface().getMyData(-1);
    		}
    		update();
    	}
        return false;
    }

	/**
	 * This method requests fresh data from the webservice and updates the GUI with that data
	 */
	public void update(){
		Player player=Singleton.getInstance().getPlayer();
		myrank=new ArrayList<Long>();
		myrank.add(player.getRank());
		myscore=new ArrayList<OtherPlayer>();
		myscore.add(new OtherPlayer(0, player.getName(), player.getLevel(), player.getPoints(), 
									player.getStats().getShares().get(0), player.getStats().getShares().get(1), 
									player.getStats().getShares().get(2)));
		ranks=new ArrayList<Long>();
		for(long i=1;i<=100;i++){
			ranks.add(i);
		}
		//TODO dummytest
		if(PreferenceManager.getDefaultSharedPreferences(this).getBoolean("checkbox_dummydata", true)){
			highscore=new ArrayList<OtherPlayer>();
			for(int i=0;i<100;i++){
				highscore.add(Dummydata.getOtherPlayer(0));
			}
		}
		else if(Util.networkAvailable()){
			// 1 to 100
			highscore=new WebserviceInterface().getRanking(0, 99); 
		}
		myscorelist = new OtherPlayerHighscoreListAdapter(this, R.id.lvMyscore,myscore,myrank);
		lvMe.setAdapter(myscorelist);
		
		scorelist = new OtherPlayerHighscoreListAdapter(this, R.id.lvMyscore,highscore,ranks);
		lvHighScore.setAdapter(scorelist);
	}
}
