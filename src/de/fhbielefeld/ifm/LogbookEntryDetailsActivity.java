package de.fhbielefeld.ifm;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import de.fhbielefeld.ifm.logic.LogbookEntry;
import de.fhbielefeld.ifm.logic.Singleton;
import de.fhbielefeld.ifm.logic.WebserviceInterface;
import de.fhbielefeld.ifm.persistence.DbAccess;
import de.fhbielefeld.ifm.util.Dummydata;
import de.fhbielefeld.ifm.util.Util;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * This class holds the LogbookEntryDetailsActivity. It displays detailed
 * data of the selected LogbookEntry-object. 
 * 
 * @author Dimitri Mantler
 */
public class LogbookEntryDetailsActivity extends SherlockFragmentActivity implements OnClickListener{

	LogbookEntry log;
	int index;
	int friendposition;
	int source=-1;
	TextView tvName;
	TextView tvDate;
	TextView tvDuracy;
	TextView tvDistance;
	TextView tvFrequency;
	TextView tvTransport;
	TextView tvShare;
	TextView tvDescription;
	Button bEdit;
	ImageButton ibMapType;
	ImageButton ibMyLocation;
	
	private GoogleMap map;
	Marker MyPosMarker;
	PolylineOptions path;
	
	/**
	 * This method will be automatically called in the Activity-lifecycle
	 * and initializes the GUI.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logbook_entry_details);
		source = getIntent().getExtras().getInt("source");
		index = getIntent().getExtras().getInt("position");
		switch(source){
		case 0://from Logbookfragment
			log =Singleton.getInstance().getPlayer().getLogbook().get(index);
			break;
		case 1://from FriendDetailsActivity
			friendposition= getIntent().getExtras().getInt("friendposition");
			//TODO dummytest
			if(PreferenceManager.getDefaultSharedPreferences(this).getBoolean("checkbox_dummydata", true)){
				log = Dummydata.getLog(Singleton.getInstance().getPlayer().getFriends()
						.get(friendposition).getSharedLogs().get(index));
			}
			else if(Util.networkAvailable()){
				log = new WebserviceInterface().getLog(Singleton.getInstance().getPlayer()
						.getFriends().get(friendposition).getSharedLogs().get(index));
			}
			break;
		}
		SupportMapFragment fm = (SupportMapFragment)   getSupportFragmentManager().findFragmentById(R.id.map);
		path=Util.getPolylineFromString(log.getPath());
	    map = fm.getMap();
	    
	    map.moveCamera(CameraUpdateFactory.newLatLngZoom(path.getPoints().get(0), 15));
	    
	    MyPosMarker = map.addMarker(new MarkerOptions()
        .position(path.getPoints().get(0))
        .icon(BitmapDescriptorFactory
	            .fromResource(R.drawable.ic_position_start)));
	    map.addPolyline(path);
	    map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		tvName=(TextView) this.findViewById(R.id.aled_name);
		tvDate=(TextView) this.findViewById(R.id.aled_date);
		tvDuracy=(TextView) this.findViewById(R.id.aled_duracy);
		tvDistance=(TextView) this.findViewById(R.id.aled_distance);
		tvFrequency=(TextView) this.findViewById(R.id.aled_frequency);
		tvTransport=(TextView) this.findViewById(R.id.aled_transport);
		tvShare=(TextView) this.findViewById(R.id.aled_share);
		tvDescription=(TextView) this.findViewById(R.id.aled_description);
		bEdit = (Button) this.findViewById(R.id.aled_edit);
		bEdit.setOnClickListener(this);
		if(source==1)
			bEdit.setVisibility(View.GONE);
		ibMapType = (ImageButton) this.findViewById(R.id.ibMapType);
		ibMapType.setOnClickListener(this);
		ibMyLocation = (ImageButton) this.findViewById(R.id.ibMyLocation);
		ibMyLocation.setOnClickListener(this);
		
		tvName.setText(log.getName());
		tvDate.setText(Util.getDateAsString(log.getDate())+"   "+Util.getTimeAsString(log.getDate()));
		tvDuracy.setText(Util.getDuracyAsTimeString(log.getDuracy()));
		tvDistance.setText((float)Math.round(log.getDistance())/1000+" Km");
		tvFrequency.setText(Util.getFrequencyStringResource(log.getFrequency()));
		tvTransport.setText(Util.getTransportStringResource(log.getTransport()));
		tvShare.setText(Util.getShareStringResource(log.getShare()));
		if(log.getDescription().equals("")||log.getDescription().equals(" ")){
			tvDescription.setTextColor(Color.DKGRAY);
			tvDescription.setText(getText(R.string.no_description));
		}
		else{
			tvDescription.setTextColor(Color.WHITE);
			tvDescription.setText(log.getDescription());
		}
		
		
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
	 * edit-button: starts the EditEntryActivity and adds the position.
	 * maptype-button: switches between normal- and hybridmode. 
	 * mylocation-button: moves the camera to the marker.
	 */
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.aled_edit:
			Intent editEntry = new Intent(this, EditEntryActivity.class);
			editEntry.putExtra("position", index);
			startActivity(editEntry);
			finish();
			break;
		case R.id.ibMapType:
			if(map.getMapType()==GoogleMap.MAP_TYPE_NORMAL)
				map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			else
				map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			break;
			
		case R.id.ibMyLocation:
			map.animateCamera(CameraUpdateFactory.newLatLngZoom(MyPosMarker.getPosition(),map.getCameraPosition().zoom), 2000, null);
			break;
		}
		
	}

}
