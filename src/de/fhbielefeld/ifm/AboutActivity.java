package de.fhbielefeld.ifm;

import com.actionbarsherlock.app.SherlockActivity;

import android.os.Bundle;

/**
 * This class holds the AboutActivity which displays the
 * applications- logo, name, current version and the creationdate .
 * 
 * @author Dimitri Mantler
 */
public class AboutActivity extends SherlockActivity {

	/**
	 * This method will be automatically called in the Activity-lifecycle
	 * and initializes the GUI.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
	}

}
