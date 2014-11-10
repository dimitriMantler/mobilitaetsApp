package de.fhbielefeld.ifm;

import de.fhbielefeld.ifm.logic.*;
import de.fhbielefeld.ifm.persistence.DbAccess;
import de.fhbielefeld.ifm.util.Dummydata;
import de.fhbielefeld.ifm.util.Util;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * This class holds the LoginActivity which offers the possibility
 * to validate the entered email and password, and load userdata which are
 * needed for the application to function.
 * 
 * @author Dimitri Mantler
 */
public class LoginActivity extends SherlockActivity implements OnClickListener{

	Button bLogin;
	Button bRegister;
	EditText etEmail;
	EditText etPassword;
	boolean firstlogin=true;
	boolean termsAccepted=false;
	long id=-1;
	String registerUrl="http://www.example.com";
	
	/**
	 * This method will be automatically called in the Activity-lifecycle
	 * and initializes the GUI. Also the preferences will be set und the 
	 * current context will be saved for later use .
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		//takes effect only once during first launch
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false); 
		SharedPreferences myPreference=PreferenceManager.getDefaultSharedPreferences(this);
		Singleton.getInstance().setContext(this);
		try{
    		DbAccess.loadData();
    	}
    	catch(Exception e){
    		System.out.println("no stored data");
    	}
		
		etEmail=(EditText)findViewById(R.id.etEmail);
		etPassword=(EditText)findViewById(R.id.etPassword);
		bLogin=(Button)findViewById(R.id.bLogin);
		bLogin.setOnClickListener(this);
		bRegister=(Button)findViewById(R.id.bRegister);
		bRegister.setOnClickListener(this);
		
		if(Singleton.getInstance().getPlayer()!=null){
			firstlogin=false;
			
			if(myPreference.getBoolean("checkbox_setlogindata", false)){
				etEmail.setText(Singleton.getInstance().getPlayer().getEmail());
				etPassword.setText(Singleton.getInstance().getPlayer().getPassword());
				
				if(myPreference.getBoolean("checkbox_autologin", false)){
					if(validateLogin())
						startApp();
				}
			}
		}
	}
	
	/**
	 * This method will be triggered by clicking a button.
	 * login-button: triggers the validation-process.
	 * register-button: starts an intent to be used by a browser.
	 */
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.bLogin:
			if(validateLogin()){
				if(firstlogin)
					alertDialog();
				else
					startApp();
			}
			else{
				
			}
			break;
		case R.id.bRegister:
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(registerUrl));
			startActivity(i);
			break;
		}
	}

	/**
	 * This method validates the login-credentials and requests the unique session-ID. 
	 * If the session is valid, indicated by a value greater than -1, the corresponding
	 * userdata will be loaded.
	 *  
	 * @return true if login-credentials are valid, else false
	 */
	public boolean validateLogin(){
		//TODO dummytest
		if(PreferenceManager.getDefaultSharedPreferences(this).getBoolean("checkbox_dummydata", true)){
			if(Dummydata.validateLogin(etEmail.getText()+"",etPassword.getText()+"")){
				Dummydata.loadPlayerData();
				return true;
			}
			else{
				Toast.makeText(this, getText(R.string.wrong_login_credentials), Toast.LENGTH_SHORT).show();
				return false;
			}
		}
		else{
			String email=etEmail.getText()+"";
			String pass=etPassword.getText()+"";
			if(!(email.equals("")||pass.equals(""))&&Util.networkAvailable()){
				id = new WebserviceInterface().getAccess(email,pass);
				if(id>-1){
					new WebserviceInterface().getMyData(id);
					return true;
				}
				else{
					Toast.makeText(this, getText(R.string.wrong_login_credentials), Toast.LENGTH_SHORT).show();
					return false;
				}
			}
			else{
				Toast.makeText(this, getText(R.string.empty_field), Toast.LENGTH_SHORT).show();
				return false;
			}
			
		}
	}
	
	/**
	 * This method saves the current userdata to the local sqlite-database
	 * and starts the MainMenuActivity.
	 */
	public void startApp(){
		DbAccess.saveData();
        startActivity(new Intent(this,MainMenuActivity.class));
        finish();
	}
	
	/**
	 * This method displays an AlertDialog with terms & conditions
	 * and gives the user a choice between accepting them or not.
	 * Accepting will call the startApp-method.
	 */
	public void alertDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.title_agb))
		.setMessage(getString(R.string.message_agb))
		.setPositiveButton(R.string.accept_agb, new DialogInterface.OnClickListener() 
		{
		    @Override
		    public void onClick(DialogInterface dialog, int which) 
		    {
		    	startApp();
		    }
		})
		.setNegativeButton(R.string.deny_agb, new DialogInterface.OnClickListener() 
		{
		    @Override
		    public void onClick(DialogInterface dialog, int which) 
		    {
		    	
		    }
		}).show();
	}
	
	//  TODO only for debugging purpose.
	//  Grants access to preferences without authentification,
	//  specially to developer-options
	/**
	 * This method will be automatically called in the Activity-lifecycle
	 * and adds a clickable menuitem to the actionbar.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		 menu.add("settings")
         .setIcon(R.drawable.ic_settings_applications_inverse)
         .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}
	
	/**
	 * This method will be automatically called if a clickevent is registered
	 * on one of the items inside the actionbar.
	 * settings: starts PreferenceActivity
	 */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	if(item.getTitle().equals("settings")){
            startActivity(new Intent(this, PreferenceActivity.class));
    	}
        return false;
    }
}
