package de.fhbielefeld.ifm;

import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import de.fhbielefeld.ifm.adapter.MedalListAdapter;
import de.fhbielefeld.ifm.adapter.OtherPlayerListAdapter;
import de.fhbielefeld.ifm.logic.Medal;
import de.fhbielefeld.ifm.logic.MonthRace;
import de.fhbielefeld.ifm.logic.OtherPlayer;
import de.fhbielefeld.ifm.logic.Singleton;
import de.fhbielefeld.ifm.logic.WebserviceInterface;
import de.fhbielefeld.ifm.persistence.DbAccess;
import de.fhbielefeld.ifm.util.Dummydata;
import de.fhbielefeld.ifm.util.Util;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

/**
 * This class holds the MonthRaceDetailsActivity. 
 * It displays detailed information about the selected monthrace. 
 * 
 * @author Dimitri Mantler
 */
public class MonthRaceDetailsActivity extends SherlockActivity implements OnClickListener{

	TextView tvName;
	TextView tvAdmin;
	TextView tvDate;
	TextView tvAveragePoints;
	TextView tvRacePoints;
	TextView tvMinLevel;
	TextView tvAccess;
	TextView tvParticipants;
	
	private OtherPlayerListAdapter playerlist;
	private MedalListAdapter medallist;
	private ArrayList<OtherPlayer> players;
	private ArrayList<Medal> medals;
	private ListView lvPlayers;
	private ListView lvMedals;
	private Button button;
	private boolean signedUp;
	
	private MonthRace race=null;
	public long raceId=0;
	
	/**
	 * This method will be automatically called in the Activity-lifecycle,
	 * initializes the GUI, and loads the Monthrace if its just available,
	 * but not signedUp. Also loads the players and medals separately.
	 * The nested ListViews disable the touchevents 
	 * for the parent scrollview if they are touched.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_month_race_details);
		raceId=this.getIntent().getExtras().getLong("id");
		signedUp=this.getIntent().getExtras().getBoolean("signedUp");
		medals = new ArrayList<Medal>();
		players = new ArrayList<OtherPlayer>();
		if(signedUp){
			for(int i=0;i<Singleton.getInstance().getPlayer().getMonthraces().size();i++){
				if(raceId==Singleton.getInstance().getPlayer().getMonthraces().get(i).getId())
					race=Singleton.getInstance().getPlayer().getMonthraces().get(i);
			}
		}
		else{
			//TODO dummytest
			if(PreferenceManager.getDefaultSharedPreferences(this).getBoolean("checkbox_dummydata", true)){
				for(int i=0;i<Dummydata.getAvailableDummyRaces().size();i++){
					if(raceId==Dummydata.getAvailableDummyRaces().get(i).getId())
						race=Dummydata.getAvailableDummyRaces().get(i);
				}
			}
			else if(Util.networkAvailable()){
				ArrayList<MonthRace> races=new WebserviceInterface().getAvailableRaces();
				for(int i=0;i<races.size();i++){
					if(raceId==races.get(i).getId())
						race=races.get(i);
				}
			}
			
		}
		
		tvName=(TextView)findViewById(R.id.tvName);
		tvAdmin=(TextView)findViewById(R.id.tvAdmin);
		tvDate=(TextView)findViewById(R.id.tvDate);
		tvAveragePoints=(TextView)findViewById(R.id.tvAveragePoints);
		tvRacePoints=(TextView)findViewById(R.id.tvRacePoints);
		tvMinLevel=(TextView)findViewById(R.id.tvMinLevel);
		tvAccess=(TextView)findViewById(R.id.tvAccess);
		tvParticipants=(TextView)findViewById(R.id.tvParticipants);
		
		tvName.setText(race.getName());
		tvAdmin.setText(race.getAdmin());
		tvDate.setText(Util.getDateAsString(race.getStartDate())+" "+Util.getTimeAsString(race.getStartDate()));
		tvAveragePoints.setText(race.getAveragePoints()+"");
		tvRacePoints.setText(race.getPot()+"");
		tvMinLevel.setText(race.getMinLevel()+"");
		tvAccess.setText(Util.getShareStringResource(race.getShare()));
		tvParticipants.setText(getString(R.string.participants)+" "+race.getParticipants().size()+"/"+race.getLimit());
		
		players= new ArrayList<OtherPlayer>();
		
		//TODO dummytest
		if(PreferenceManager.getDefaultSharedPreferences(this).getBoolean("checkbox_dummydata", true)){
			for(int i=0;i<race.getParticipants().size();i++){
				players.add(Dummydata.getOtherPlayer(race.getParticipants().get(i)));
			}
		}
		else if(Util.networkAvailable()){
			ArrayList<Long> playerIds= new ArrayList<Long>();
			for(int i=0;i<race.getParticipants().size();i++){
				playerIds.add(race.getParticipants().get(i));
			}	
			players=new WebserviceInterface().getPlayerList(playerIds);
		}
			
		lvPlayers=(ListView)findViewById(R.id.lvMyscore);
		playerlist = new OtherPlayerListAdapter(this, R.id.lvMyscore,players);
		lvPlayers.setAdapter(playerlist);
		lvPlayers.setOnTouchListener(new ListView.OnTouchListener() {
	        @Override
	        public boolean onTouch(View v, MotionEvent event) {
	            int action = event.getAction();
	            switch (action) {
	            case MotionEvent.ACTION_DOWN:
	                // Disallow ScrollView to intercept touch events.
	                v.getParent().requestDisallowInterceptTouchEvent(true);
	                break;

	            case MotionEvent.ACTION_UP:
	                // Allow ScrollView to intercept touch events.
	                v.getParent().requestDisallowInterceptTouchEvent(false);
	                break;
	            }

	            // Handle ListView touch events.
	            v.onTouchEvent(event);
	            return true;
	        }
	    });
		
		medals= new ArrayList<Medal>();
		
		//TODO dummytest
		if(PreferenceManager.getDefaultSharedPreferences(this).getBoolean("checkbox_dummydata", true)){
			for(int i=0;i<race.getMedals().size();i++){
				medals.add(Dummydata.getMedal(race.getMedals().get(i)));
			}	
		}
		else if(Util.networkAvailable()){
			ArrayList<Long> medalIds= new ArrayList<Long>();
			for(int i=0;i<race.getMedals().size();i++){
				medalIds.add(race.getMedals().get(i));
			}	
			medals=new WebserviceInterface().getMedals(medalIds);
		}
			
		lvMedals=(ListView)findViewById(R.id.lvHighscore);
		medallist = new MedalListAdapter(this, R.id.lvHighscore,medals);
		lvMedals.setAdapter(medallist);
		lvMedals.setOnTouchListener(new ListView.OnTouchListener() {
	        @Override
	        public boolean onTouch(View v, MotionEvent event) {
	            int action = event.getAction();
	            switch (action) {
	            case MotionEvent.ACTION_DOWN:
	                // Disallow ScrollView to intercept touch events.
	                v.getParent().requestDisallowInterceptTouchEvent(true);
	                break;

	            case MotionEvent.ACTION_UP:
	                // Allow ScrollView to intercept touch events.
	                v.getParent().requestDisallowInterceptTouchEvent(false);
	                break;
	            }

	            // Handle ListView touch events.
	            v.onTouchEvent(event);
	            return true;
	        }
	    });
	
		button=(Button)findViewById(R.id.bSave);
		button.setOnClickListener(this);
		if(signedUp)
			button.setText(R.string.leave);
		else
			button.setText(R.string.sign_up);
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
	 * This method will be triggered by clicking a button.
	 * leave/signUp-button: calls the signUp-method if not already signed up, or else
	 * it will call the leave-method.
	 */
	@Override
	public void onClick(View v) {
		if(signedUp)
			leaveRace();
		else
			signUpRace();
	}
	
	/**
	 * This method displays an AlertDialog with informations for leaving this monthrace. 
	 * If the user is sure about leaving the race, he/she has to confirm the decision by
	 * typing the word "leave" into the textfield and click the yes-button.
	 * Confirmation will send a leave-request to the webservice and finish the Activity.
	 */
	public void leaveRace(){
		final EditText et=new EditText(this);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder
		.setMessage(getString(R.string.message_leave_race))
		.setView(et)
		.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() 
		{
		    @Override
		    public void onClick(DialogInterface dialog, int which) 
		    {
		    	String value=et.getText()+"";
		    	if(value.equals("leave")){
		    		//TODO dummytest
		    		if(!PreferenceManager.getDefaultSharedPreferences(Singleton.getInstance().getContext())
		    				.getBoolean("checkbox_dummydata", true)&&Util.networkAvailable()){
			    		new WebserviceInterface().leaveRace(raceId);
				    	new WebserviceInterface().getMyData(-1);
		    		}
		    		finish();
		    	}
		    	else
		    		leaveRace();
		    }
		})
		.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() 
		{
		    @Override
		    public void onClick(DialogInterface dialog, int which){}
		}).show();
	}
	
	/**
	 * This method displays an AlertDialog with conditions for signing up 
	 * for this monthrace, and gives the user a choice between signing up or not.
	 * Confirmation will the user sign up for that race and finish the Activity.
	 */
	public void signUpRace(){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder
		.setMessage(getString(R.string.message_signup_race_1)+" "+getSignUpPrice()+" "+getString(R.string.message_signup_race_2))
		.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() 
		{
		    @Override
		    public void onClick(DialogInterface dialog, int which) 
		    {
		    	//TODO dummytest
		    	if(!PreferenceManager.getDefaultSharedPreferences(Singleton.getInstance().getContext())
		    			.getBoolean("checkbox_dummydata", true)&&Util.networkAvailable()){
			    	new WebserviceInterface().signUpRace(raceId);
			    	new WebserviceInterface().getMyData(-1);
		    	}
		    	finish();
		    }
		})
		.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() 
		{
		    @Override
		    public void onClick(DialogInterface dialog, int which){}
		}).show();
	}
	
	public int getSignUpPrice(){
		return Singleton.getInstance().getPlayer().getLevel()/5;
	}
	
	/**
	 * This method will be automatically called in the Activity-lifecycle
	 * and adds a clickable menuitem to the actionbar.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//gc.compareTo(race.getStartDate())<0&&  <-- dont allow adding medals to running races, 
		//but people can cheat by changing the systemtime. Adding running-token as workaround would work
		if(signedUp==true)
		 menu.add("add")
         .setIcon(R.drawable.ic_add_applications_inverse)
         .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}

	/**
	 * This method will be automatically called if a clickevent is registered
	 * on one of the items inside the actionbar.
	 * add: starts MedalsActivity and adds the source and raceid
	 */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	if(item.getTitle().equals("add")){
    		Intent i = new Intent(this, MedalsActivity.class);
    		i.putExtra("source", 1);//from MonthRaceDetails
    		i.putExtra("raceid", race.getId());
            this.startActivity(i);
    	}
    	
        return true;
    }
}
