package de.fhbielefeld.ifm;

import com.actionbarsherlock.app.SherlockActivity;

import de.fhbielefeld.ifm.logic.Enums;
import de.fhbielefeld.ifm.logic.Singleton;
import de.fhbielefeld.ifm.persistence.DbAccess;
import de.fhbielefeld.ifm.util.Util;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * This class holds the TransportationListActivity which 
 * displays a radiorgoup with each kind of 
 * transportation the user has to choose from.
 * 
 * @author Dimitri Mantler
 */
public class TransportationListActivity extends SherlockActivity implements OnClickListener{

	 RadioGroup rg;
	 RadioButton rb;
	 ImageView iv;
	 Button bContinue;
	 LinearLayout ll;
	 
	 
	 LocationManager locationManager;
	 
	 int index=0;
	 
	 /**
	  * This method will be automatically called in the Activity-lifecycle
	  * and initializes the GUI. 
	  * Each way of transportation contains the name and an icon.
	  */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transportation_list);
		rg=(RadioGroup) findViewById(R.id.radioGroup1);
		bContinue = (Button) findViewById(R.id.bStartStop); 
		bContinue.setOnClickListener(this);
		ll =new LinearLayout(this);
		ll.setOrientation(LinearLayout.HORIZONTAL);
		for(int i=0;i<Enums.Transportation.values().length;i++){
			rb = new RadioButton(this);
			rb.setText(String.format("%1$8s",
					Util.getTransportStringResource(Enums.Transportation.values()[i])));
			rb.setCompoundDrawablesWithIntrinsicBounds(0,0,
					Util.getTransportIconResource(Enums.Transportation.values()[i]),0);
			rb.setPadding(0, 0, 16, 0);
			rg.addView(rb);
		}
		
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
	    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
	      alertDialog();
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
	 * continue-button: calls the startRecording-method if a 
	 * transportation has been chosen.
	 */
	@Override
	public void onClick(View v) {
		index=rg.indexOfChild(rg.findViewById(rg.getCheckedRadioButtonId()));
		if(index!=-1)
			startRecording();
		else
			Toast.makeText(this, "Wähle eine Fortbewegungsart", Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * This method displays an AlertDialog which asks if the user want to enable GPS
	 * and goes to the settings if yes was chosen.
	 */
	public void alertDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder
		.setMessage(getString(R.string.message_activate_gps))
		.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() 
		{
		    @Override
		    public void onClick(DialogInterface dialog, int which) 
		    {
		    	Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			    startActivity(intent);
		    }
		})
		.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() 
		{
		    @Override
		    public void onClick(DialogInterface dialog, int which) 
		    {}
		}).show();
	}

	/**
	 * This method starts the TrackingActivity and ads the transportation
	 */
	public void startRecording(){
		Intent startRecord=new Intent(this,TrackingActivity.class);
		startRecord.putExtra("transport", index);
		startActivity(startRecord);
		finish();
	}
	
}
