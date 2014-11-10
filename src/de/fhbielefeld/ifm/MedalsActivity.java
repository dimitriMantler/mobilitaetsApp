package de.fhbielefeld.ifm;

import com.actionbarsherlock.app.SherlockFragmentActivity;

import de.fhbielefeld.ifm.logic.Singleton;
import de.fhbielefeld.ifm.persistence.DbAccess;

import android.os.Bundle;

/**
 * This class holds the MedalsActivity which holds the OwnMedalsFragment.
 * 
 * @author Dimitri Mantler
 */
public class MedalsActivity extends SherlockFragmentActivity {

	int source;
	int position;
	
	/**
	 * This method will be automatically called in the Activity-lifecycle.
	 * Depending of the source a different title will be displayed.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		source = getIntent().getExtras().getInt("source");
		if(source==2){//from FriendDetailsActivity
			position = getIntent().getExtras().getInt("position");
			setTitle(Singleton.getInstance().getPlayer().getFriends().get(position).getName()+"'s "+getString(R.string.medals));
		}
		else 
			setTitle(getString(R.string.medals)+" "+getString(R.string.donate));
		if(savedInstanceState == null)
            getSupportFragmentManager().beginTransaction().add(android.R.id.content, new OwnMedalsFragment()).commit();
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
