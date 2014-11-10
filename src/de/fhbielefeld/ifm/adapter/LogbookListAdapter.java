package de.fhbielefeld.ifm.adapter;

import de.fhbielefeld.ifm.R;
import de.fhbielefeld.ifm.logic.*;
import de.fhbielefeld.ifm.util.Util;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
 
public class LogbookListAdapter extends ArrayAdapter<LogbookEntry>{
    private ArrayList<LogbookEntry> entries;
    private Activity activity;
// 
    public LogbookListAdapter(Activity a, int textViewResourceId, ArrayList<LogbookEntry> entries) {
        super(a, textViewResourceId, entries);
        this.entries = entries;
        this.activity = a;
    }
    
    public static class ViewHolder{
        public TextView tvName;
        public TextView tvDate;
        public TextView tvDuracy;
        public TextView tvAverageSpeed;
        public TextView tvDistance;
        public TextView tvSynced;
        public ImageView ivTransport;
        public ImageView ivFrequency;
        public ImageView ivShare;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder holder;
        if (v == null) {
            LayoutInflater vi =(LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.listitem_logbookentry, null);
            holder = new ViewHolder();
            holder.tvName = (TextView) v.findViewById(R.id.tvName);
            holder.tvDate = (TextView) v.findViewById(R.id.tvDate);
            holder.tvDuracy = (TextView) v.findViewById(R.id.tvDuracy);
            holder.tvAverageSpeed = (TextView) v.findViewById(R.id.tvAverageSpeed);
            holder.tvDistance = (TextView) v.findViewById(R.id.tvDistance);
            holder.tvSynced = (TextView) v.findViewById(R.id.tvSynced);
            holder.ivTransport = (ImageView) v.findViewById(R.id.ivMedalicon);
            holder.ivFrequency = (ImageView) v.findViewById(R.id.ivFrequency);
            holder.ivShare = (ImageView) v.findViewById(R.id.ivShare);
            v.setTag(holder);
        }
        else
            holder=(ViewHolder)v.getTag();
 
        final LogbookEntry custom = entries.get(position);
        if (custom != null) {
        	Calendar c=null;
	     	Context con=Singleton.getInstance().getContext();
	     	holder.tvName.setText(custom.getName());
	     	c=custom.getDate();
	     	holder.tvDate.setText(Util.getDateAsString(c)+"   "+Util.getTimeAsString(c));
	     	holder.tvDuracy.setText(Util.getDuracyAsTimeString(custom.getDuracy()));
	     	holder.tvAverageSpeed.setText(Util.cutFloat(custom.getAverageSpeed()) +" km/h");
	     	holder.tvDistance.setText(Util.cutFloat((float)Math.round(custom.getDistance())/1000) +" km/h");
	         if(custom.getSync()){
	        	 holder.tvSynced.setTextColor(Color.WHITE);
	        	 holder.tvSynced.setText(con.getString(R.string.li_synced));
	         }
	         else{
	        	 holder.tvSynced.setTextColor(Color.DKGRAY);
	        	 holder.tvSynced.setText(con.getString(R.string.li_not_synced));
	         }
	         holder.ivTransport.setImageResource(Util.getTransportIconResource(custom.getTransport()));
	         holder.ivFrequency.setImageResource(Util.getFrequencyIconResource(custom.getFrequency()));
	         holder.ivShare.setImageResource(Util.getShareIconResource(custom.getShare()));
        }
        return v;
    }
}