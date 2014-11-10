package de.fhbielefeld.ifm.adapter;

import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockFragment;
import com.koushikdutta.urlimageviewhelper.UrlImageViewCallback;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import de.fhbielefeld.ifm.R;
import de.fhbielefeld.ifm.logic.Medal;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.ViewGroup;
//import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MedalGridViewAdapter extends BaseAdapter {  //implements OnClickListener
    ArrayList<Medal> medals=null;
    Context mContext=null;
    Fragment fragment=null;
     
    // Gets the context so it can be used later  
    public MedalGridViewAdapter(Context c, ArrayList<Medal> medals, SherlockFragment f) {  
     this.mContext = c;  
     this.medals=medals;
     this.fragment=f;
    }  
     
    // Total number of things contained within the adapter  
    public int getCount() { 
    	return medals.size();
    }  
     
     // Require for structure, not really used in my code.  
    public Object getItem(int position) {  
     return null;  
    }  
     
    // Require for structure, not really used in my code. Can  
    // be used to get the id of an item in the adapter for  
    // manual control.  
    public long getItemId(int position) {  
     return position;  
    }  
     
    public View getView(int position,View convertView, ViewGroup parent) {
    	 
	     ImageButton btn;  
	     if (convertView == null) {  
	      // if it's not recycled, initialize some attributes  
	      btn = new ImageButton(mContext);  
	      btn.setFocusable(false);
	      btn.setFocusableInTouchMode(false);
	      btn.setClickable(false);
	      btn.setLayoutParams(new GridView.LayoutParams(100, 100));  
	      }  
	     else {  
	      btn = (ImageButton) convertView;  
	     }   
	    
	     btn.setBackgroundResource(R.drawable.ic_medal_back_normal);
	     
	     if(medals.get(position).getCreator().equals("Server"))
	    	 btn.setBackgroundResource(R.drawable.ic_medal_back_special);
		else if(medals.get(position).getCreator().equals(""))
			btn.setBackgroundResource(R.drawable.ic_medal_back_mysterious);
		else
			btn.setBackgroundResource(R.drawable.ic_medal_back_normal);
	     
	     try{
	     UrlImageViewHelper.setUrlDrawable(btn, medals.get(position).getImageurl(),R.drawable.ic_no_medal, new UrlImageViewCallback() {
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
	     catch(Exception e){
	    	 btn.setImageResource(R.drawable.ic_no_medal);
	     }
	     btn.setId(position);
	     return btn;  
    }

//	@Override
//	public void onClick(View v) {
//		Toast.makeText(mContext, fragment+"", Toast.LENGTH_SHORT).show();
////		if(mContext.getClass()==OwnMedalsActivity.class){
////			((OwnMedalsActivity)mContext).updateItemdetails(v.getId());
////		}
////		else if(mContext.getClass()==EarnedMedalsActivity.class){
////			((EarnedMedalsActivity)mContext).updateItemdetails(v.getId());
////		}
//	}  
	}