package de.fhbielefeld.ifm;

import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import de.fhbielefeld.ifm.adapter.FriendrequestListAdapter;
import de.fhbielefeld.ifm.adapter.OtherPlayerListAdapter;
import de.fhbielefeld.ifm.logic.Friend;
import de.fhbielefeld.ifm.logic.OtherPlayer;
import de.fhbielefeld.ifm.logic.Singleton;
import de.fhbielefeld.ifm.logic.WebserviceInterface;
import de.fhbielefeld.ifm.persistence.DbAccess;
import de.fhbielefeld.ifm.util.Dummydata;
import de.fhbielefeld.ifm.util.Util;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

/**
 * This class holds the FriendlistActivity. It displays a ListView
 * with befriended users and another ListView with pending friendrequests. 
 * 
 * @author Dimitri Mantler
 */
public class FriendlistActivity extends SherlockActivity implements OnItemClickListener, OnItemLongClickListener{

	@SuppressWarnings("rawtypes")
	ArrayList friends;
	OtherPlayerListAdapter friendadapter;
	ListView lvFriends;
	
	ArrayList<OtherPlayer> friendrequests;
	FriendrequestListAdapter friendrequestadapter;
	ListView lvFriendrequests;
	
	/**
	 * This method will be automatically called in the Activity-lifecycle
	 * and initializes the GUI.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friendlist);
		lvFriends=(ListView)findViewById(R.id.lvFriends);
		lvFriends.setOnItemClickListener(this);
		lvFriends.setOnItemLongClickListener(this);
		lvFriendrequests=(ListView)findViewById(R.id.lvFriendrequests);
		
		update();
		
				
	}

	/**
	 * This method will be automatically called in the Activity-lifecycle
	 * and adds clickable menuitems to the actionbar.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		menu.add("search")
	    .setIcon(R.drawable.ic_search)
	    .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		
		menu.add("update")
	    .setIcon(R.drawable.ic_update_applications_inverse)
	    .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}

	/**
	 * This method will be automatically called if a clickevent is registered
	 * on one of the items inside the actionbar.
	 * search: starts the SearchFriendActivity
	 * update: requests fresh data and calls the update-method
	 */
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	if(item.getTitle().equals("search")){
    		startActivity(new Intent(this, SearchFriendActivity.class));
    	}
    	else if(item.getTitle().equals("update")){
    		//TODO dummytest
    		if(!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("checkbox_dummydata", true)&&Util.networkAvailable()){
    			new WebserviceInterface().getMyData(-1);
    		}
    		update();
    	}
        return true;
    }

	/**
	 * This method will will be triggered if an item was clicked.
	 * It starts the FriendDetailsActivity and adds the position of the friend.
	 */
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent i=new Intent(this, FriendDetailsActivity.class);
		i.putExtra("position", arg2);
		startActivity(i);
		
	}
	
	/**
	 * This method sends a positive response to the webservice.
	 * The trigger is defined in the layout.
	 * 
	 * @param v the clicked View/Button which holds the index of the item
	 */
	public void acceptRequest(View v){
		int index=Integer.parseInt(v.getTag()+"");
		
		//TODO dummytest
		if(!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("checkbox_dummydata", true)&&Util.networkAvailable()){
			new WebserviceInterface().answerFriendRequest(friendrequests.get(index).getId(), true);
			new WebserviceInterface().getMyData(-1);
		}
		update();
//		Toast.makeText(this, "acceptRequest "+v.getTag(), Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * This method sends a negative response to the webservice.
	 * The trigger is defined in the layout.
	 * 
	 * @param v the clicked View/Button which holds the index of the item
	 */
	public void denyRequest(View v){
		int index=Integer.parseInt(v.getTag()+"");
		
		//TODO dummytest
		if(!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("checkbox_dummydata", true)&&Util.networkAvailable()){
			new WebserviceInterface().answerFriendRequest(friendrequests.get(index).getId(), false);
			new WebserviceInterface().getMyData(-1);
		}
		update();
//		Toast.makeText(this, "denyRequest "+v.getTag(), Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * This method requests fresh data from the webservice and updates the GUI with that data
	 */
	@SuppressWarnings("unchecked")
	public void update(){
		friends=Singleton.getInstance().getPlayer().getFriends();
		
		//TODO dummytest
		if(PreferenceManager.getDefaultSharedPreferences(this).getBoolean("checkbox_dummydata", true)){
			friendrequests =new ArrayList<OtherPlayer>();
			friendrequests.add(Dummydata.getOtherPlayer(0));
		}
		else if(Util.networkAvailable()){
			friendrequests=new WebserviceInterface().getFriendRequests();
		}
		friendadapter=new OtherPlayerListAdapter(this, R.id.lvFriends, friends);
		lvFriends.setAdapter(friendadapter);
		
		if(friendadapter==null)
			friendrequests =new ArrayList<OtherPlayer>();
		friendrequestadapter=new FriendrequestListAdapter(this,R.id.lvFriendrequests,friendrequests);
		lvFriendrequests.setAdapter(friendrequestadapter);
	}

	/**
	 * This method will be triggered if an item was pressed for 2 seconds.
	 * It displays an AlertDialog with the question if the user want to remove 
	 * the selected user from his/her friendlist.
	 */
	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		final int index=arg2;
		Friend friend=(Friend)friends.get(index);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(getString(R.string.remove_friend_1)+" "+friend.getName()+" "+getString(R.string.remove_friend_2))
		.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() 
		{
		    @Override
		    public void onClick(DialogInterface dialog, int which) 
		    {
		    	//TODO dummytest
				if(!PreferenceManager.getDefaultSharedPreferences(Singleton.getInstance().getContext())
						.getBoolean("checkbox_dummydata", true)&&Util.networkAvailable()){
			    	Friend friend=(Friend)friends.get(index);
			    	new WebserviceInterface().removeFriend(friend.getId());
			    	new WebserviceInterface().getMyData(-1);
			    	update();
				}
		    }
		})
		.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() 
		{
		    @Override
		    public void onClick(DialogInterface dialog, int which) 
		    {
		    	
		    }
		}).show();
		return false;
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
