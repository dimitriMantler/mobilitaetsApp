package de.fhbielefeld.ifm;

import java.util.GregorianCalendar;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import de.fhbielefeld.ifm.adapter.CustomSpinnerAdapter;
import de.fhbielefeld.ifm.logic.Enums.*;
import de.fhbielefeld.ifm.logic.LogbookEntry;
import de.fhbielefeld.ifm.logic.Singleton;
import de.fhbielefeld.ifm.persistence.DbAccess;
import de.fhbielefeld.ifm.util.Util;

import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

/**
 * This class holds the EditEntryActivity. It displays a MapView
 * and the customizable data from the selected LogbookEntry-object 
 * which are name, description, frequency and sharing-option. 
 * 
 * @author Dimitri Mantler
 */
public class EditEntryActivity extends SherlockFragmentActivity implements OnClickListener{

	
	EditText etName;
	EditText etDescription;
	Spinner sFrequency;
	Spinner sShare;
	Button bSave;
	int position=0;
	PolylineOptions path;
	LogbookEntry log;
	ImageButton ibMapType;
	ImageButton ibMyLocation;
	
	GoogleMap map;
	Marker MyPosMarker;
	
	/**
	 * This method will be automatically called in the Activity-lifecycle
	 * and initializes the GUI.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_entry);
		
		position = getIntent().getExtras().getInt("position");
		log =Singleton.getInstance().getPlayer().getLogbook().get(position);
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
		
		etName = (EditText) findViewById(R.id.etName);
		etDescription = (EditText) findViewById(R.id.etDescription);
		sFrequency = (Spinner) findViewById(R.id.sFrequency);
		CustomSpinnerAdapter adapterF = new CustomSpinnerAdapter(
				this, R.layout.spinner_adapter, Util.getFrequencyStrings(), 
				Util.getFrequencyIconResources(), getLayoutInflater());
		sFrequency.setAdapter(adapterF);
		sShare = (Spinner) findViewById(R.id.sShare);
		CustomSpinnerAdapter adapterS = new CustomSpinnerAdapter(
				this, R.layout.spinner_adapter, Util.getShareStrings(), 
				Util.getShareIconResources(), getLayoutInflater());		
		sShare.setAdapter(adapterS);
		bSave = (Button) findViewById(R.id.bSave);
		bSave.setOnClickListener(this);
		ibMapType = (ImageButton) this.findViewById(R.id.ibMapType);
		ibMapType.setOnClickListener(this);
		ibMyLocation = (ImageButton) this.findViewById(R.id.ibMyLocation);
		ibMyLocation.setOnClickListener(this);
		getWindow().setSoftInputMode(
			      WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		fillForm();
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
	 * save-button: overwrites the old LogbookEntry.
	 * maptype-button: switches between normal- and hybridmode. 
	 * mylocation-button: moves the camera to the marker.
	 */
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.bSave:
			Singleton.getInstance().getPlayer().getLogbook().set(position, generateEntry());
			DbAccess.saveData();
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
	
	/**
	 * This method fills the form with data from the selected LogbookEntry.
	 */
	public void fillForm(){
		LogbookEntry log= Singleton.getInstance().getPlayer().getLogbook().get(position);
		etName.setText(log.getName());
		etDescription.setText(log.getDescription());
		sFrequency.setSelection(log.getFrequency().ordinal());
		sShare.setSelection(log.getShare().ordinal());
	}
	
	/**
	 * This method creates a new LogbookEntry-object from the data of 
	 * the old LogbookEntry-object and the displayed name, description, 
	 * frequency, sharing-option.
	 * 
	 * @return the new/edited LogbookEntry
	 */
	public LogbookEntry generateEntry(){
		GregorianCalendar c=new GregorianCalendar();
		LogbookEntry newLog=null;
		LogbookEntry oldLog=null;
		
		oldLog=Singleton.getInstance().getPlayer().getLogbook().get(position);

		String name=etName.getText()+"";
		if(name.equals(""))
			name="Strecke "+Util.getDateAsString(c)+""+Util.getTimeAsString(c);
		if(etDescription.getText().equals(""))
			etDescription.append(" ");
		int frequency=sFrequency.getSelectedItemPosition();
		int share=sShare.getSelectedItemPosition();
		
		newLog=new LogbookEntry(oldLog.getId(),oldLog.getPath(), name, etDescription.getText()+"", oldLog.getDuracy(), oldLog.getAverageSpeed(), oldLog.getDistance(), oldLog.getDate(), Frequency.values()[frequency], oldLog.getTransport(), Share.values()[share], false, c);

		return newLog;
	}
}
