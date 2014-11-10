package de.fhbielefeld.ifm;

import com.google.android.gms.maps.SupportMapFragment;

import de.fhbielefeld.ifm.logic.Singleton;
import de.fhbielefeld.ifm.persistence.DbAccess;

import com.actionbarsherlock.app.SherlockFragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.PolylineOptions;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * This class will not be used in this version
 * 
 * @author Dimitri Mantler
 */
@Deprecated
public class SetPathActivity extends SherlockFragmentActivity implements OnClickListener{

	Button b;
	ImageButton ibStart;
	ImageButton ibFinish;
	
	LatLng Mypos = new LatLng(51.551, 9.993);
	private GoogleMap map;
	String provider;
	LocationManager locationManager;
	Marker mStart;
	Marker mFinish;
	PolylineOptions path;
	CircleOptions accuracy;
	Location loc=null;
	float distance=0;
	Criteria criteria;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_path);
		SupportMapFragment fm = (SupportMapFragment)   getSupportFragmentManager().findFragmentById(R.id.map);
	    map = fm.getMap();
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(Mypos, 6), 2000, null);
		b =(Button) findViewById(R.id.bSave);
		b.setOnClickListener(this);
	}

	@Override
	public void onStart(){
		if(Singleton.getInstance().getPlayer()==null)
			DbAccess.loadData();
		super.onStart();
	}
	
	@Override
	public void onStop(){
		DbAccess.saveData();
		super.onStop();
	}
	
	@Override
	public void onClick(View v) {
		Intent i=new Intent(this, EditEntryActivity.class);
		i.putExtra("position", -1); //indicates a new entry
		startActivity(i);
		
	}


}
