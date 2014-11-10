package de.fhbielefeld.ifm;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import de.fhbielefeld.ifm.logic.Player;
import de.fhbielefeld.ifm.logic.Singleton;
import de.fhbielefeld.ifm.logic.WebserviceInterface;
import de.fhbielefeld.ifm.util.Util;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * This class holds the HomeFragment which is nested in the MainMenuActivity. 
 * It displays rank, name, available points, progress and an avatar. 
 * 
 * @author Dimitri Mantler
 */
public class HomeFragment extends SherlockFragment implements OnClickListener{

	ImageView ivAvatar;
	TextView tvRank;
	TextView tvLevel;
	TextView tvPoints;
	TextView tvNextLevel;
	ProgressBar pbNextLevel;
	Button bMedals;
	Button bFriendslist;
	Button bHighscore;
	
	Player player=null;
	
	public static final String ARG_SECTION_NUMBER = "section_number";

	
	public HomeFragment() {
	}

	/**
	 * This method will be automatically called in the Activity-lifecycle
	 * and initializes the GUI.
	 */
	@Override 
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		View rootView = inflater.inflate(R.layout.activity_home, container, false);
		
		player=Singleton.getInstance().getPlayer();
		
		ivAvatar=(ImageView)rootView.findViewById(R.id.ivAvatar);
		tvRank=(TextView)rootView.findViewById(R.id.tvRank);
		tvLevel=(TextView)rootView.findViewById(R.id.tvLevel);
		tvPoints=(TextView)rootView.findViewById(R.id.tvPoints);
		tvNextLevel=(TextView)rootView.findViewById(R.id.tvNextLevel);
		
		bHighscore=(Button)rootView.findViewById(R.id.bHighscore);
		bHighscore.setOnClickListener(this);
		bFriendslist=(Button)rootView.findViewById(R.id.bFriendslist);
		bFriendslist.setOnClickListener(this);
		bMedals=(Button)rootView.findViewById(R.id.bMedals);
		bMedals.setOnClickListener(this);
		pbNextLevel=(ProgressBar)rootView.findViewById(R.id.pbNextLevel);
		
		update();
		return rootView;
	}
	
	/**
	 * This method sets the title for this fragment.
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    super.onCreateOptionsMenu(menu, inflater);
	    getSherlockActivity().setTitle(getString(R.string.title_activity_home_fragment));
	}
	
	/**
	 * This method will be triggered by clicking a button.
	 * medals-button: starts the MyMedalsActivity and adds the source.
	 * highscore-button: starts the HighscoreActivity.
	 * friends-button: starts the FriendlistActivity.
	 */
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.bMedals:
			Intent i=new Intent(getSherlockActivity(),MyMedalsActivity.class);
			i.putExtra("source", 0);//from Home/MyMedals
			getSherlockActivity().startActivity(i);
			break;
		case R.id.bHighscore:
			getSherlockActivity().startActivity(new Intent(getSherlockActivity(),HighscoreActivity.class));
			break;
		case R.id.bFriendslist:
			getSherlockActivity().startActivity(new Intent(getSherlockActivity(),FriendlistActivity.class));
			break;
		}
		
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
	 * This method updates the GUI.
	 */
    public void update(){
    	ivAvatar.setImageResource(Util.getCurrentAvatar(Singleton.getInstance().getPlayer()));
    	float progress=((float)player.getToNext()/(float)Util.PointsPerLevel(player.getLevel())*100);
		pbNextLevel.setProgress((int)progress);
		tvNextLevel.setText("Nächster Level in "+(Util.PointsPerLevel(player.getLevel())-player.getToNext())+" Punkten\nFortschritt: "+(int)progress+"% ("+player.getToNext()+" von "+Util.PointsPerLevel(player.getLevel())+")");
		tvRank.setText("#"+player.getRank());
		tvLevel.setText(getString(R.string.li_level)+" "+player.getLevel());
		tvPoints.setText(player.getPoints()+" "+getText(R.string.li_points));
    }
}
