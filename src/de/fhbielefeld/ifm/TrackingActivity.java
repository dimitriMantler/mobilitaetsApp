package de.fhbielefeld.ifm;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource.OnLocationChangedListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import de.fhbielefeld.ifm.logic.Enums.Frequency;
import de.fhbielefeld.ifm.logic.Enums.Share;
import de.fhbielefeld.ifm.logic.Enums.Transportation;
import de.fhbielefeld.ifm.logic.LogbookEntry;
import de.fhbielefeld.ifm.logic.Player;
import de.fhbielefeld.ifm.logic.Singleton;
import de.fhbielefeld.ifm.logic.WebserviceInterface;
import de.fhbielefeld.ifm.persistence.DbAccess;
import de.fhbielefeld.ifm.util.Util;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This class holds the TrackingActivity. It displays a MapView
 * and some information about the current track. 
 * By clicking the startbutton a new LogbookEntry can be created
 * 
 * @author Dimitri Mantler
 */
public class TrackingActivity extends SherlockFragmentActivity implements OnClickListener, OnLocationChangedListener, LocationListener{

	LatLng Mypos = new LatLng(53.551, 9.993);
	GoogleMap map;
	String provider;
	LocationManager locationManager;
	Marker MyPosMarker;
	PolylineOptions path;
	CircleOptions accuracy;
	Location loc;
	Criteria criteria;
	
	ArrayList<Float> averageSpeedValues;
	float distance=0;
	float averageSpeed=0;
	int seconds=0;
	boolean running=false;
	
	ImageButton ibMyLocation;
	ImageButton ibMapType;
	Button bStartStop;
	TextView tvDuracy;
	TextView tvDistance;
	TextView tvAverageSpeed;
	TextView tvCurrentSpeed;
	
	GregorianCalendar gc;
	Transportation transport;
	
	Thread t;
	
	/**
	 * This method will be automatically called in the Activity-lifecycle
	 * and initializes the GUI and GPS-sensor.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tracking);
		int temp = getIntent().getExtras().getInt("transport");
		transport=Transportation.values()[temp];
		gc=new GregorianCalendar();
		
		ibMyLocation= (ImageButton) findViewById(R.id.ibMyLocation);
		ibMyLocation.setOnClickListener(this);
		ibMapType= (ImageButton) findViewById(R.id.ibMapType);
		ibMapType.setOnClickListener(this);
		bStartStop = (Button) findViewById(R.id.bStartStop);
		bStartStop.setOnClickListener(this);
		tvDuracy = (TextView) findViewById(R.id.tvDuracy2);
		tvDuracy.setText("00:00:00");
		tvDistance = (TextView) findViewById(R.id.tvDistance2);
		tvDistance.setText("0.0km");
		tvAverageSpeed = (TextView) findViewById(R.id.tvAverageSpeed2);
		tvAverageSpeed.setText("0.0km/h");
		tvCurrentSpeed = (TextView) findViewById(R.id.tvCurrentSpeed2);
		tvCurrentSpeed.setText("0.0km/h");
		
		SupportMapFragment fm = (SupportMapFragment)   getSupportFragmentManager().findFragmentById(R.id.map);
	    map = fm.getMap(); 
	    averageSpeedValues=new ArrayList<Float>();
	    if(path==null){
	    	path = new PolylineOptions();
	    	path.color(Color.RED);
	    	path.width(6);
	    }
	    
	    accuracy=new CircleOptions().strokeWidth(3).strokeColor(Color.BLUE);
	    
	    locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
	    
	    boolean enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

	    if (!enabled) {
	      Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	      startActivity(intent);
	    }
	    else{
	    	criteria = new Criteria();
	    	criteria.setAccuracy(Criteria.ACCURACY_FINE); //fordert speziell GPS an
	        provider = locationManager.getBestProvider(criteria, false);
	        Location location = locationManager.getLastKnownLocation(provider);
	        if (location != null) {
	            System.out.println("Provider " + provider + " has been selected.");
	            onLocationChanged(location);
	          } else {
	            Toast.makeText(this, "Location not available", Toast.LENGTH_SHORT).show();
	          }
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
	
//	@Override
//	public void onStop(){ //TODO backgroundservice
//		Util.saveData();
//		super.onStop();
//	}
	
	/**
	 *  This method will be automatically called in the Activity-lifecycle 
	 *  and requests updates at startup
	 */
	@Override
	protected void onResume() { 
	  super.onResume();
	  System.out.println("onResume");
	  locationManager.requestLocationUpdates(provider, 400, 1, this);//updaterate 400 Millisekunden oder 1 Meter
	}
	
//	/* Remove the locationlistener updates when Activity is paused */
//	@Override
//	protected void onPause() {
//	  super.onPause();
//	  locationManager.removeUpdates(this);
//	}
	
	/**
	 * This method will be triggered by clicking a button.
	 * start/stop-button: starts or stops tracking.
	 * 					  if the stop button was clicked 
	 * 					  the tracking can't be resumed:
	 * maptype-button: switches between normal- and hybridmode. 
	 * mylocation-button: moves the camera to the marker.
	 */
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.bStartStop:
			if(running==false){
				running=true;
				startRecording();
				path.add(Mypos);
				bStartStop.setText(R.string.stop);
			}
			else{
				locationManager.removeUpdates(this);
				alertDialog();
			}
			break;
		
		case R.id.ibMapType:
			if(map.getMapType()==GoogleMap.MAP_TYPE_NORMAL)
				map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			else
				map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			break;
			
		case R.id.ibMyLocation:
			map.animateCamera(CameraUpdateFactory.newLatLngZoom(Mypos,map.getCameraPosition().zoom), 2000, null);
			break;
		}
	}
	
	/**
	 * This method displays various information about the running track
	 * and validates it. The current position will be added to the path
	 * if the user moves faster than 2kmh. The current location
	 * will be marked with a Marker.
	 */
	@Override
	  public void onLocationChanged(Location location) {
		map.clear();
		if(running){
			if(loc!=null){
				if(path.getPoints().size()<2)
					path.add(new LatLng(location.getLatitude(), location.getLongitude()));
				distance+=location.distanceTo(loc);
				int passedTime=(int)(location.getTime()-loc.getTime())/1000;
				float value=(location.distanceTo(loc)/passedTime)*3600/1000;
				if(value>2.0){
					averageSpeedValues.add(value);
					path.add(new LatLng(location.getLatitude(), location.getLongitude()));
				}
				for(int i=0;i<averageSpeedValues.size();i++){
					averageSpeed+=averageSpeedValues.get(i);
				}
				tvCurrentSpeed.setText(Util.cutFloat(value)+" km/h");
				tvAverageSpeed.setText(Util.cutFloat(averageSpeed/averageSpeedValues.size())+"km/h");
				
				if(!Util.validateTrack(Util.cutFloat(averageSpeed/averageSpeedValues.size()), transport)&&distance>1000){
					Toast.makeText(this, getText(R.string.invalid_track), Toast.LENGTH_SHORT).show();
					finish();
				}
			}
			
			tvDistance.setText((float)Math.round(distance)/1000+" Km");
			map.addPolyline(path);
		}
		loc=location;
		
	    Mypos=new LatLng(location.getLatitude(), location.getLongitude());
	    if(path.getPoints().size()<2)
	    	map.animateCamera(CameraUpdateFactory.newLatLngZoom(Mypos, 17), 2000, null);
	    map.addCircle(accuracy.center(Mypos)).setRadius(location.getAccuracy());
	    
	    MyPosMarker = map.addMarker(new MarkerOptions()
	    	.position(Mypos)
	    	.icon(BitmapDescriptorFactory
	            .fromResource(R.drawable.ic_position)));
	  }

	  @Override
	  public void onStatusChanged(String provider, int status, Bundle extras) { 
	  }

	  @Override
	  public void onProviderEnabled(String provider) {
	  }

	  @Override
	  public void onProviderDisabled(String provider) {
	  } 
	
	  /**
	   * This method creates a new thread which counts the elapsed time
	   * and sets a textview with the time
	   */
	public void startRecording(){
		t=new Thread(new Runnable() {
	        public void run() {
	        	while(running){
	        		try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
	    			seconds++;
	    			tvDuracy.post(new Runnable() {
	                    public void run() {
	                        tvDuracy.setText(Util.getTimeAsString(seconds));
	                    }});
	
	    			
	        	}
	    		
	        }
	    });
		t.start();
	}
	
	/**
	 * This method saves the userdata und finishes the Activity
	 */
	public void endRecording(){
		DbAccess.saveData();
		finish();
	}
	
	/**
	 * This method displays an AlertDialog, giving the user the choice to save or delete the recorded LogbookEntry
	 */
	public void alertDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder
		.setMessage(getString(R.string.save_or_delete))
		.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() 
		{
		    @Override
		    public void onClick(DialogInterface dialog, int which) 
		    {
		    	Player p=Singleton.getInstance().getPlayer();
		    	Share share=Share.valueOf(PreferenceManager.getDefaultSharedPreferences(Singleton.getInstance().getContext()).getString("default_sharing", "no"));
		    	
		    	if(averageSpeedValues.size()>0)
		    		averageSpeed=(float)averageSpeed/averageSpeedValues.size();
				LogbookEntry entry= new LogbookEntry(-1l, Util.getStringFromPolyline(path), Util.generateName(), " ", seconds, averageSpeed, distance, gc, Frequency.once, transport, share, false, gc);
				if(Util.networkAvailable())
					new WebserviceInterface().sendLogAnonymous(entry);
				p.addLogbookEntry(entry);
				endRecording();
		    }
		})
		.setNegativeButton(R.string.delete, new DialogInterface.OnClickListener() 
		{
		    @Override
		    public void onClick(DialogInterface dialog, int which) 
		    {
		    	endRecording();
		    }
		}).show();
	}
}
