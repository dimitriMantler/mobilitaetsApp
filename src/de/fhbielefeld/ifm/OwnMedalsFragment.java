package de.fhbielefeld.ifm;

import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockFragment;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import de.fhbielefeld.ifm.adapter.MedalGridViewAdapter;
import de.fhbielefeld.ifm.logic.Medal;
import de.fhbielefeld.ifm.logic.Singleton;
import de.fhbielefeld.ifm.logic.WebserviceInterface;
import de.fhbielefeld.ifm.util.Dummydata;
import de.fhbielefeld.ifm.util.Util;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class OwnMedalsFragment extends SherlockFragment implements OnItemClickListener, OnClickListener{

	private GridView gvItems;
	private TextView tvDate;
	private TextView tvName;
	private TextView tvDesc;
	private TextView tvCreator;
	private TextView tvValue;
	private Button bAddReport;
	private ImageButton ibIcon;
	
	private int focusedItem=-1;
	private ArrayList<Medal> medals=new ArrayList<Medal>();
	private int source=-1; // 0 = mymedals, 1 = monthrace, 2 = friend 
	private long raceid=-1;

	public OwnMedalsFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		View rootView = inflater.inflate(R.layout.activity_ownmedals, container, false);
		bAddReport=(Button)rootView.findViewById(R.id.bAddReport);
		bAddReport.setOnClickListener(this);
		
		medals=Singleton.getInstance().getPlayer().getOwnMedals();
		
		source=getSherlockActivity().getIntent().getExtras().getInt("source");
		
		switch(source){
		case 0:
			bAddReport.setVisibility(View.GONE);
			medals=Singleton.getInstance().getPlayer().getOwnMedals();
			break;
		case 1:
			raceid=getSherlockActivity().getIntent().getExtras().getLong("raceid");
			bAddReport.setVisibility(View.VISIBLE);
			bAddReport.setText(getString(R.string.donate));
			break;
		case 2:
			bAddReport.setVisibility(View.GONE);
			int position = getSherlockActivity().getIntent().getExtras().getInt("position");
			
			ArrayList<Long> medalIds=Singleton.getInstance().getPlayer().getFriends().get(position).getMedals();
			medals=new ArrayList<Medal>();
			
			//TODO dummytest
			if(PreferenceManager.getDefaultSharedPreferences(getSherlockActivity()).getBoolean("checkbox_dummydata", true)){
				for(int i=0;i<medalIds.size();i++){
					medals.add(Dummydata.getMedal(medalIds.get(i)));
				}
			}
			else if(Util.networkAvailable()){
				ArrayList<Long> medalIds2=new ArrayList<Long>();
				for(int i=0;i<medalIds.size();i++){
					medalIds2.add(medalIds.get(i));
				}
				medals=new WebserviceInterface().getMedals(medalIds2);
			}
			
			break;
		}

		
	
		gvItems=(GridView)rootView.findViewById(R.id.gvItems);
		MedalGridViewAdapter m=new MedalGridViewAdapter(getSherlockActivity(),medals,this);
		gvItems.setAdapter(m);
		gvItems.setOnItemClickListener(this);
		ibIcon=(ImageButton)rootView.findViewById(R.id.ibItemIcon);
		ibIcon.setBackgroundResource(R.drawable.ic_medal_back_normal);
		tvName=(TextView)rootView.findViewById(R.id.tvName);
		tvCreator=(TextView)rootView.findViewById(R.id.tvCreator);
		tvValue=(TextView)rootView.findViewById(R.id.tvValue);
		tvDate=(TextView)rootView.findViewById(R.id.tvDate);
		tvDesc=(TextView)rootView.findViewById(R.id.tvDesc);
		

		
		return rootView;
	}
	
	public void updateItems(){
		gvItems.invalidateViews();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		UrlImageViewHelper.setUrlDrawable(ibIcon, medals.get(arg2).getImageurl());
		tvName.setText(medals.get(arg2).getName());
		tvCreator.setText(medals.get(arg2).getCreator());
		tvValue.setText(medals.get(arg2).getPrize()+" "+getString(R.string.li_points));
		tvDate.setText(Util.getDateAsString(medals.get(arg2).getCreationDate()));
		tvDesc.setText(medals.get(arg2).getDescription());
		if(medals.get(arg2).getCreator().equals("Server"))
			ibIcon.setBackgroundResource(R.drawable.ic_medal_back_special);
		else if(medals.get(arg2).getCreator().equals(""))
			ibIcon.setBackgroundResource(R.drawable.ic_medal_back_mysterious);
		else
			ibIcon.setBackgroundResource(R.drawable.ic_medal_back_normal);
		focusedItem=arg2;
		
	}

	@Override
	public void onClick(View v) {
		if(focusedItem==-1){
			Toast.makeText(getSherlockActivity(),getString(R.string.no_medal_chosen), Toast.LENGTH_SHORT).show();
		}
		else{
			//TODO dummytest
			if(!PreferenceManager.getDefaultSharedPreferences(getSherlockActivity())
					.getBoolean("checkbox_dummydata", true)&&Util.networkAvailable()){
				new WebserviceInterface().addMedalToRace(raceid,Singleton.getInstance().getPlayer().getOwnMedals().get(focusedItem).getId());// raceID, medalID
				new WebserviceInterface().getMyData(-1);
			}
			
			getSherlockActivity().finish();
		}
		
	}

}
