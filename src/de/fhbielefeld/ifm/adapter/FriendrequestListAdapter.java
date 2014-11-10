package de.fhbielefeld.ifm.adapter;

import java.util.ArrayList;

import de.fhbielefeld.ifm.R;
import de.fhbielefeld.ifm.logic.OtherPlayer;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class FriendrequestListAdapter extends ArrayAdapter<OtherPlayer>{
	    private ArrayList<OtherPlayer> entries;
	    private Activity activity;
	    int buttonindex;
	 
	    public FriendrequestListAdapter(Activity a, int textViewResourceId, ArrayList<OtherPlayer> entries) {
	        super(a, textViewResourceId, entries);
	        this.entries = entries;
	        this.activity = a;
	    }
	 
	    public static class ViewHolder{
	        public TextView tvName;
	        public ImageButton ibAccept;
	        public ImageButton ibDeny;
	    }
	 
	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	        View v = convertView;
	        ViewHolder holder;
	        if (v == null) {
	            LayoutInflater vi =(LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            v = vi.inflate(R.layout.listitem_friendrequest, null);
	            holder = new ViewHolder();
	            holder.tvName = (TextView) v.findViewById(R.id.tvName);
	            holder.ibAccept = (ImageButton) v.findViewById(R.id.ibAccept);
	            holder.ibAccept.setFocusable(false);
	            holder.ibDeny = (ImageButton) v.findViewById(R.id.ibDeny);
	            holder.ibDeny.setFocusable(false);
	            v.setTag(holder);
	        }
	        else
	            holder=(ViewHolder)v.getTag();
	 
	        if (entries != null) {
	            holder.tvName.setText(entries.get(position).getName());
	            holder.ibAccept.setTag(position);
	            holder.ibDeny.setTag(position);
	            
	        }
	        return v;
	    }
	    
	    @Override
	    public int getCount(){
	    	if(entries!=null)
	    		return entries.size();
	    	else 
	    		return 0;
	    }
}
