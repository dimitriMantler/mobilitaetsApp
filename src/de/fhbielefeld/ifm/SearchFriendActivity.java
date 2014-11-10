package de.fhbielefeld.ifm;

import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockActivity;

import de.fhbielefeld.ifm.adapter.OtherPlayerListAdapter;
import de.fhbielefeld.ifm.logic.*;
import de.fhbielefeld.ifm.persistence.DbAccess;
import de.fhbielefeld.ifm.util.Dummydata;
import de.fhbielefeld.ifm.util.Util;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

/**
 * This class holds the SearchFriendActivity which displays a 
 * textfield where the searchstring can be entered, 
 * a button to start the search and a listview to display the results.
 * 
 * @author Dimitri Mantler
 */
public class SearchFriendActivity extends SherlockActivity implements OnClickListener, OnItemClickListener{

	ImageButton ibSearch;
	EditText etSearchstring;
	ListView lvResults;
	OtherPlayerListAdapter adapter;
	
	ArrayList<OtherPlayer> resultsPlayers;
	
	
	/**
	 * This method will be automatically called in the Activity-lifecycle
	 * and initializes the GUI.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_friend);
		
		ibSearch=(ImageButton)findViewById(R.id.ibSearch);
		ibSearch.setOnClickListener(this);
		etSearchstring=(EditText)findViewById(R.id.etSearchstring);
		lvResults=(ListView)findViewById(R.id.lvResults);
		lvResults.setOnItemClickListener(this);
		
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
	 * This method will will be triggered if an item was clicked.
	 * It starts the dialog-method.
	 */
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		dialog(resultsPlayers.get(arg2).getName(),arg2);
	}

	/**
	 * This method will be triggered by clicking a button.
	 * search-button: starts the search for the entered string 
	 * 				  if the EditText-field isn't empty.
	 */
	@Override
	public void onClick(View v) {
		String searchString=etSearchstring.getText()+"";
		if(!searchString.equals("")){
			//TODO dummytest
			if(PreferenceManager.getDefaultSharedPreferences(this).getBoolean("checkbox_dummydata", true)){
				resultsPlayers=new ArrayList<OtherPlayer>();
				resultsPlayers.add(Dummydata.getOtherPlayer(0));
				resultsPlayers.add(Dummydata.getOtherPlayer(0));
				resultsPlayers.add(Dummydata.getOtherPlayer(0));
			}
			else if(Util.networkAvailable()){
				resultsPlayers = new WebserviceInterface().searchFriend(etSearchstring.getText()+"");
			}
			adapter=new OtherPlayerListAdapter(this, R.id.lvResults, resultsPlayers);
			lvResults.setAdapter(adapter);
		}
		else{
			Toast.makeText(this, getText(R.string.enter_searchstring), Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * This method displays an AlertDialog with the question, 
	 * if the user want to send the selected player a friendrequest
	 * 
	 * @param name The name of the selected player
	 * @param index The index of the selected player in the result-ArrayList
	 */
	public void dialog(String name, int index){
		final int index2=index;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder
		.setMessage(getString(R.string.send_request_1)+" "+name+" "+getString(R.string.send_request_2))
		.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() 
		{
		    @Override
		    public void onClick(DialogInterface dialog, int which) 
		    {
		    	if(!PreferenceManager.getDefaultSharedPreferences(Singleton.getInstance().getContext()).getBoolean("checkbox_dummydata", true)){
					new WebserviceInterface().sendFriendRequest(resultsPlayers.get(index2).getId());
		    	}
		    }
		})
		.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() 
		{
		    @Override
		    public void onClick(DialogInterface dialog, int which) 
		    {
		    	
		    }
		}).show();

	}
}
