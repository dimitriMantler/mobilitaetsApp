package de.fhbielefeld.ifm;

import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockActivity;

import de.fhbielefeld.ifm.adapter.LogbookListAdapter;
import de.fhbielefeld.ifm.logic.Friend;
import de.fhbielefeld.ifm.logic.LogbookEntry;
import de.fhbielefeld.ifm.logic.Singleton;
import de.fhbielefeld.ifm.logic.WebserviceInterface;
import de.fhbielefeld.ifm.persistence.DbAccess;
import de.fhbielefeld.ifm.util.Dummydata;
import de.fhbielefeld.ifm.util.ShareBar;
import de.fhbielefeld.ifm.util.ShareBar.Size;
import de.fhbielefeld.ifm.util.Util;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * This class holds the FriendDetailsActivity. 
 * It displays more detailed data from a selected friend, 
 * also his/her shared logbookentries.
 * 
 * @author Dimitri Mantler
 */
public class FriendDetailsActivity extends SherlockActivity implements OnClickListener, OnItemClickListener{

	TextView tvName;
	TextView tvLevel;
	TextView tvPoints;
	TextView tvThisMonth;
	TextView tvAllTime;
	Button bMedals;
	ImageView ivShares;
	ListView lvSharedLogs;
	
	LogbookListAdapter adapter;
	ArrayList<LogbookEntry> logs=new ArrayList<LogbookEntry>();
	int position;
	Friend friend;
	
	/**
	 * This method will be automatically called in the Activity-lifecycle
	 * and initializes the GUI.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_details);
		
		position=getIntent().getExtras().getInt("position");
		
		friend=Singleton.getInstance().getPlayer().getFriends().get(position);
		
		tvName=(TextView)findViewById(R.id.tvName);
		tvLevel=(TextView)findViewById(R.id.tvLevel);
		tvPoints=(TextView)findViewById(R.id.tvPoints);
		tvThisMonth=(TextView)findViewById(R.id.tvThisMonth);
		tvAllTime=(TextView)findViewById(R.id.tvAllTime);
		
		bMedals=(Button)findViewById(R.id.bMedals);
		bMedals.setOnClickListener(this);
		
		ivShares=(ImageView)findViewById(R.id.ivShares);
		
		//Logs will be loaded separately, while the Friend-object only holds the corresponding ids 
		//TODO dummytest
		if(PreferenceManager.getDefaultSharedPreferences(this).getBoolean("checkbox_dummydata", true)){
			for(int i=0;i<friend.getSharedLogs().size();i++){
				logs.add(Dummydata.getLog(friend.getSharedLogs().get(i)));
			}
		}
		else if(Util.networkAvailable()){
			ArrayList<Long> logids=new ArrayList<Long>();
			for(int i=0;i<friend.getSharedLogs().size();i++){
				logids.add(friend.getSharedLogs().get(i));
			}
			logs = new WebserviceInterface().getLogs(logids);
		}
		
		lvSharedLogs=(ListView)findViewById(R.id.lvSharedLogs);
		lvSharedLogs.setOnItemClickListener(this);
		adapter=new LogbookListAdapter(this, R.id.lvSharedLogs, logs);
		lvSharedLogs.setAdapter(adapter);
		
		tvName.setText(friend.getName());
		tvLevel.setText("Level "+friend.getLevel());
		tvPoints.setText(friend.getPoints()+" Punkte");
		tvThisMonth.setText(friend.getWaysCountCurrent()+" Strecken/ "+friend.getWaysDistanceCurrent()+"Km");
		tvAllTime.setText(friend.getWaysCountTotal()+" Strecken/ "+friend.getWaysDistanceTotal()+"Km");
		bMedals.setText(friend.getMedals().size()+" "+getText(R.string.medals));
		
		ivShares.setImageBitmap(ShareBar.createShareBar(this, friend.getShareGood(), friend.getShareMedium(), friend.getShareBad(), Size.WIDE));
		
	}

	/**
	 * This method will be triggered by clicking an item in the ListView.
	 * It starts the LogbookEntryDetailsActivity with additional data 
	 * like the source to remove the edit-button, friendposition and position 
	 * to determine which LogbookEntry should be displayed.
	 */
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent i=new Intent(this, LogbookEntryDetailsActivity.class);
		// from FriendDetailsActivity
		i.putExtra("source", 1); 
		i.putExtra("position", arg2);
		i.putExtra("friendposition", position);
		startActivity(i);
		
	}

	/**
	 * This method will be triggered by clicking a button.
	 * It starts the MedalsActivity with additional data 
	 * like the source of the Intent, and the position
	 * of the friend.
	 */
	@Override
	public void onClick(View arg0) {
		Intent i=new Intent(this, MedalsActivity.class);
		// from FriendDetailsActivity
		i.putExtra("source", 2); 
		i.putExtra("position", position);
		startActivity(i);
		
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


}
