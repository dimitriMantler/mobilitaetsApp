package de.fhbielefeld.ifm.adapter;

import de.fhbielefeld.ifm.R;
import de.fhbielefeld.ifm.logic.*;
import de.fhbielefeld.ifm.util.ShareBar;
import de.fhbielefeld.ifm.util.ShareBar.Size;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
 
public class OtherPlayerHighscoreListAdapter extends ArrayAdapter<OtherPlayer>{
    private ArrayList<OtherPlayer> entries;
    private Activity activity;
    private ArrayList<Long> ranks;
 
    public OtherPlayerHighscoreListAdapter(Activity a, int textViewResourceId, ArrayList<OtherPlayer> entries, ArrayList<Long> ranks) {
        super(a, textViewResourceId, entries);
        this.entries = entries;
        this.activity = a;
        this.ranks =ranks;
    }
    
    public static class ViewHolder{
    	public TextView tvRank;
        public TextView tvName;
        public TextView tvPoints;
        public TextView tvLevel;
        public ImageView ivShares;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder holder;
        if (v == null) {
            LayoutInflater vi =(LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.listitem_otherplayerhighscore, null);
            holder = new ViewHolder();
            holder.tvRank = (TextView) v.findViewById(R.id.tvRank);
            holder.tvName = (TextView) v.findViewById(R.id.tvName);
            holder.tvPoints = (TextView) v.findViewById(R.id.tvValue);
            holder.tvLevel = (TextView) v.findViewById(R.id.tvLevel);
            holder.ivShares = (ImageView) v.findViewById(R.id.ivShares);
            v.setTag(holder);
        }
        else
            holder=(ViewHolder)v.getTag();
 
        final OtherPlayer custom = entries.get(position);
        if (custom != null) {
        	holder.tvRank.setText(ranks.get(position)+".");
        	holder.tvName.setText(custom.getName());
        	holder.tvPoints.setText(custom.getPoints()+"");
        	holder.tvLevel.setText(custom.getLevel()+"");
            holder.ivShares.setImageBitmap(ShareBar.createShareBar(activity, custom.getShareGood(), custom.getShareMedium(), custom.getShareBad(), Size.SMALL));
        }
        return v;
    }
}