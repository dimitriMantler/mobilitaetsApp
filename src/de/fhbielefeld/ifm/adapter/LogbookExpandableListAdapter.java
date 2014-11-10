package de.fhbielefeld.ifm.adapter;

import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import de.fhbielefeld.ifm.R;
import de.fhbielefeld.ifm.logic.LogbookEntry;
import de.fhbielefeld.ifm.logic.Singleton;
import de.fhbielefeld.ifm.util.Util;
 
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class LogbookExpandableListAdapter extends BaseExpandableListAdapter{
	 
    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<LogbookEntry>> _listDataChild;
//    private ArrayList<LogbookEntry> entries;
 
    public LogbookExpandableListAdapter(Context context, List<String> _listDataHeader,HashMap<String, List<LogbookEntry>> _listDataChild, ArrayList<LogbookEntry>entries) {
        this._context = context;
        this._listDataHeader=_listDataHeader;
        this._listDataChild=_listDataChild;
//        this.entries=entries;
    }
 
    @Override
    public Object getChild(int groupPosition, int childPosititon) {
    	
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }
 
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
 
    @Override
    public View getChildView(int groupPosition, final int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {
 
    	int index=0;
    	for(int i=0;i<=groupPosition;i++){
    		if(i==groupPosition){
    			index+=childPosition;
    			break;
    		}
    		else{
    			index+=getChildrenCount(i);
    		}
    	}
    	
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.listitem_logbookentry, null);
        }
 
         TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
         TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);
         TextView tvDuracy = (TextView) convertView.findViewById(R.id.tvDuracy);
         TextView tvAverageSpeed = (TextView) convertView.findViewById(R.id.tvAverageSpeed);
         TextView tvDistance = (TextView) convertView.findViewById(R.id.tvDistance);
         TextView tvSynced = (TextView) convertView.findViewById(R.id.tvSynced);
         ImageView ivTransport = (ImageView) convertView.findViewById(R.id.ivMedalicon);
         ImageView ivFrequency = (ImageView) convertView.findViewById(R.id.ivFrequency);
         ImageView ivShare = (ImageView) convertView.findViewById(R.id.ivShare);
      
         final LogbookEntry custom = (LogbookEntry) getChild(groupPosition, childPosition);
         
         if (custom != null) {
	         Calendar c=null;
	     	 Context con=Singleton.getInstance().getContext();
	     	 tvName.setText(custom.getName());
	     	 c=custom.getDate();
	         tvDate.setText(Util.getDateAsString(c)+"   "+Util.getTimeAsString(c));
	         tvDuracy.setText(Util.getDuracyAsTimeString(custom.getDuracy()));
	         tvAverageSpeed.setText(Util.cutFloat(custom.getAverageSpeed()) +" km/h");
	         tvDistance.setText(Util.cutFloat((float)Math.round(custom.getDistance())/1000) +" km/h");
	         if(custom.getSync()){
	         	tvSynced.setTextColor(Color.WHITE);
	         	tvSynced.setText(con.getString(R.string.li_synced));
	         }
	         else{
	         	tvSynced.setTextColor(Color.DKGRAY);
	         	tvSynced.setText(con.getString(R.string.li_not_synced));
	         }
	         ivTransport.setImageResource(Util.getTransportIconResource(custom.getTransport()));
	         ivFrequency.setImageResource(Util.getFrequencyIconResource(custom.getFrequency()));
	         ivShare.setImageResource(Util.getShareIconResource(custom.getShare()));
         }
         convertView.setTag(Integer.valueOf(index));
        return convertView;
    }
 
    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }
 
    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }
 
    @Override
    public int getGroupCount() {
        return _listDataHeader.size();
    }
 
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
 
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }
 
        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);
 
        
        
        return convertView;
    }
 
    @Override
    public boolean hasStableIds() {
        return false;
    }
 
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}