package de.fhbielefeld.ifm;

import com.actionbarsherlock.app.SherlockActivity;

import de.fhbielefeld.ifm.logic.Singleton;
import de.fhbielefeld.ifm.logic.WebserviceInterface;
import de.fhbielefeld.ifm.persistence.DbAccess;
import de.fhbielefeld.ifm.util.Util;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;

/**
 * This class holds the ReportActivity. It offers the possibility to report
 * medals for inappropriate content, therefor the user can choose a reason by clicking a radiobutton
 * and confirm with the button.
 * 
 * @author Dimitri Mantler
 */
public class ReportActivity extends SherlockActivity implements OnClickListener{

	 RadioGroup rg;
	 Button bReport;
	 
	 int index=0;
	 
	 /**
	  * This method will be automatically called in the Activity-lifecycle
	  * and initializes the GUI.
	  */ 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report);
		rg=(RadioGroup) findViewById(R.id.rgReason);
		bReport = (Button) findViewById(R.id.bReport); 
		bReport.setOnClickListener(this);
			
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
	 * report-button: sends the report and finishes the Activity.
	 */
	@Override
	public void onClick(View v) {
		//TODO dummytest
		if(!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("checkbox_dummydata", true)&&Util.networkAvailable()){
			new WebserviceInterface().reportMedal(getIntent().getExtras().getLong("medalid"),
					rg.indexOfChild(rg.findViewById(rg.getCheckedRadioButtonId())));
		}
		finish();
	}
}
