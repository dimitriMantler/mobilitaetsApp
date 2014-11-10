package de.fhbielefeld.ifm;

import java.util.ArrayList;
import com.actionbarsherlock.app.SherlockFragment;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import de.fhbielefeld.ifm.adapter.MedalGridViewAdapter;
import de.fhbielefeld.ifm.logic.Medal;
import de.fhbielefeld.ifm.logic.Singleton;
import de.fhbielefeld.ifm.util.Util;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This class holds the EarnedMedalsFragment which is nested 
 * in the MyMedalsActivity. It displays the users earned medals and 
 * uses the same layout as OwnMedalsFragment due to similar content. 
 * 
 * @author Dimitri Mantler
 */
public class EarnedMedalsFragment extends SherlockFragment implements OnItemClickListener, OnClickListener{

	private GridView gvItems;
	private TextView tvDate;
	private TextView tvName;
	private TextView tvDesc;
	private TextView tvCreator;
	private TextView tvValue;
	private Button bAddReport;
	private ImageButton ibIcon;
	
	private int focusedItem=-1;
	private ArrayList<Medal> medals=null;

	public EarnedMedalsFragment() {
	}

	/**
	 * This method will be automatically called in the Fragment-lifecycle
	 * and initializes the GUI.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_ownmedals, container, false);
		medals=Singleton.getInstance().getPlayer().getEarnedMedals();
	
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
		bAddReport=(Button)rootView.findViewById(R.id.bAddReport);
		bAddReport.setText(getString(R.string.report));
		bAddReport.setOnClickListener(this);
		
		return rootView;
	}

	/**
	 * This method will be triggered by clicking an item in the gridview.
	 * The lower half of the GUI displays the data of the selected medal.
	 */
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

	/**
	 * This method will be triggered by clicking a button.
	 * The report-button starts the ReportActivity if a medal is selected.
	 */
	@Override
	public void onClick(View v) {
		if(focusedItem==-1){
			Toast.makeText(getSherlockActivity(),getString(R.string.no_medal_chosen), Toast.LENGTH_SHORT).show();
		}
		else{
			Intent i=new Intent(getSherlockActivity(),ReportActivity.class);
			i.putExtra("medalid", medals.get(focusedItem).getId());
			getSherlockActivity().startActivity(i);
		}
			
		
	}
}
