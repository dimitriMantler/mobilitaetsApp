package de.fhbielefeld.ifm;

import com.actionbarsherlock.app.SherlockActivity;

import android.os.Bundle;

/**
 * This class holds the HelpActivity which displays some questions and answers.
 * 
 * @author Dimitri Mantler
 */
public class HelpActivity extends SherlockActivity {

	/**
	 * This method will be automatically called in the Activity-lifecycle
	 * and initializes the GUI.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
	}

}
