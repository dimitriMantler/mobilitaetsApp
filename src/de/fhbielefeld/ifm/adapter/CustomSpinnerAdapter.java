package de.fhbielefeld.ifm.adapter;

import de.fhbielefeld.ifm.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomSpinnerAdapter extends ArrayAdapter<String> {

	int[] images;
	String[] strings;
	LayoutInflater inflater;
   
    public CustomSpinnerAdapter(Context context, int textViewResourceId,String[] objects, int[] images, LayoutInflater inflater) {//Context, int, String[], int[], LayoutInflater
        super(context, textViewResourceId, objects);
        this.images=images;
        this.strings=objects;
        this.inflater=inflater;
    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        
        View row=inflater.inflate(R.layout.spinner_adapter, parent, false);

        ImageView icon=(ImageView)row.findViewById(R.id.questicon);
        icon.setImageResource(images[position]);
        TextView tvName= (TextView)row.findViewById(R.id.tvName);
        tvName.setText(strings[position]);
        
        return row;
        
    }
}