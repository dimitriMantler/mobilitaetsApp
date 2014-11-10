package de.fhbielefeld.ifm;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;

import com.actionbarsherlock.app.SherlockPreferenceActivity;

/**
 * This class holds the PreferenceActivity. 
 * 
 * @author Dimitri Mantler
 */
public class PreferenceActivity extends SherlockPreferenceActivity implements OnPreferenceChangeListener{

	/**
	 * This method will be automatically called in the Activity-lifecycle
	 * The preferences will be loaded from the preference.xml.
	 * The summarystring for the listpreference 
 	 * will be set corresponting to the choosen item by triggering 
 	 * the onPreferenceChange-listener.
	 */
	@SuppressWarnings("deprecation")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
        
        ListPreference listPreference = (ListPreference) findPreference("default_sharing");
        if(listPreference.getValue()==null) {
            // to ensure we don't get a null value
            // set first value by default
            listPreference.setValueIndex(0);
        }
        listPreference.setSummary(listPreference.getValue().toString());
        listPreference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary(newValue.toString());
                return true;
            }
        });
    }
	
	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		// TODO Auto-generated method stub
		return true;
	}
}
