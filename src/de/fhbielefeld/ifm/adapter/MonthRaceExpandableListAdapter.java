package de.fhbielefeld.ifm.adapter;

import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.fhbielefeld.ifm.R;
import de.fhbielefeld.ifm.logic.MonthRace;
import de.fhbielefeld.ifm.logic.Singleton;
import de.fhbielefeld.ifm.util.Util;
 
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MonthRaceExpandableListAdapter extends BaseExpandableListAdapter{
	 
    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<MonthRace>> _listDataChild;
//    private ArrayList<MonthRace> entries;
 
    public MonthRaceExpandableListAdapter(Context context, List<String> _listDataHeader,HashMap<String, List<MonthRace>> _listDataChild, ArrayList<MonthRace>entries) {
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
 
//    	int index=0;
//    	for(int i=0;i<=groupPosition;i++){
//    		if(i==groupPosition){
//    			index+=childPosition;
//    			break;
//    		}
//    		else{
//    			index+=_listDataChild.get(this._listDataHeader.get(groupPosition)).size();
//    		}
//    	}
    	
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.listitem_month_race, null);
        }
 
         TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
         TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);
         TextView tvPlayerCount = (TextView) convertView.findViewById(R.id.tvPlayerCount);
         TextView tvAveragePoints = (TextView) convertView.findViewById(R.id.tvAveragePoints);
         TextView tvMinLevel = (TextView) convertView.findViewById(R.id.tvMinLevel);
         TextView tvAdmin = (TextView) convertView.findViewById(R.id.tvAdmin);
         ImageView ivShare = (ImageView) convertView.findViewById(R.id.ivShare);
      
         final MonthRace custom = (MonthRace) getChild(groupPosition, childPosition);
         
         if (custom != null) {
	     	 tvName.setText(custom.getName());
	         tvDate.setText(Util.getDateAsString(custom.getStartDate()));
	         tvPlayerCount.setText(custom.getParticipants().size()+"/"+custom.getLimit());
	         tvAveragePoints.setText(custom.getAveragePoints()+"");
	         tvMinLevel.setText("Level "+custom.getMinLevel());
	         tvAdmin.setText(custom.getAdmin());
	         if(custom.getAdmin().equals(Singleton.getInstance().getPlayer().getName())){
	         	tvAdmin.setTextColor(Color.rgb(150, 150, 0));
	         }
	         else{
	        	 tvAdmin.setTextColor(Color.WHITE);
	         }
	         ivShare.setImageResource(Util.getShareIconResource(custom.getShare()));
         }
         convertView.setTag(custom.getId());
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