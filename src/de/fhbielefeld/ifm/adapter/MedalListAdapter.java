package de.fhbielefeld.ifm.adapter;

import de.fhbielefeld.ifm.R;
import de.fhbielefeld.ifm.logic.*;

import java.util.ArrayList;

import com.koushikdutta.urlimageviewhelper.UrlImageViewCallback;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
 
public class MedalListAdapter extends ArrayAdapter<Medal>{
    private ArrayList<Medal> entries;
    private Activity activity;
 
    public MedalListAdapter(Activity a, int textViewResourceId, ArrayList<Medal> entries) {
        super(a, textViewResourceId, entries);
        this.entries = entries;
        this.activity = a;
    }
    
    public static class ViewHolder{
        public TextView tvName;
        public TextView tvValue; 
        public ImageView ivIcon;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder holder;
        if (v == null) {
            LayoutInflater vi =(LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.listitem_medal, null);
            holder = new ViewHolder();
            holder.tvName = (TextView) v.findViewById(R.id.tvName);
            holder.tvValue = (TextView) v.findViewById(R.id.tvValue);
            holder.ivIcon = (ImageView) v.findViewById(R.id.ivIcon);
            v.setTag(holder);
        }
        else
            holder=(ViewHolder)v.getTag();
 
        final Medal custom = entries.get(position);
        if (custom != null) {
        	holder.tvName.setText(custom.getName());
        	holder.tvValue.setText(custom.getPrize()+" Punkte");
        	
        	UrlImageViewHelper.setUrlDrawable(holder.ivIcon, custom.getImageurl(),R.drawable.ic_no_medal, new UrlImageViewCallback() {
                @Override
                public void onLoaded(ImageView imageView, Bitmap loadedBitmap, String url, boolean loadedFromCache) {
                    if (!loadedFromCache) {
                        ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1, ScaleAnimation.RELATIVE_TO_SELF, .5f, ScaleAnimation.RELATIVE_TO_SELF, .5f);
                        scale.setDuration(300);
                        scale.setInterpolator(new OvershootInterpolator());
                        imageView.startAnimation(scale);
                    }
                }
            });
        }
        return v;
    }
}