package de.fhbielefeld.ifm.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Scanner;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import de.fhbielefeld.ifm.R;
import de.fhbielefeld.ifm.logic.LogbookEntry;
import de.fhbielefeld.ifm.logic.Player;
import de.fhbielefeld.ifm.logic.Singleton;
import de.fhbielefeld.ifm.logic.Stats;
import de.fhbielefeld.ifm.logic.Enums.Frequency;
import de.fhbielefeld.ifm.logic.Enums.Share;
import de.fhbielefeld.ifm.logic.Enums.Transportation;

public class Util {
	
//---------Enums----------	
	
	/**
	 * This method returns the drawable-resource-id 
	 * for the requested Frequency
	 * @param f the Frequency
	 * @return the drawable-resource-id
	 */
	public static int getFrequencyIconResource(Frequency f){
		switch(f){
		case once:
			return R.drawable.ic_frequency_once;
		case regular:
			return R.drawable.ic_frequency_regular;
		case sometimes:
			return R.drawable.ic_frequency_sometimes;
		default://So the compiler will shut up
			return 0;
		}
	}
	
	/**
	 * This method returns the string-resource-id 
	 * for the requested Frequency
	 * @param f the Frequency
	 * @return the string-resource-id
	 */
	public static String getFrequencyStringResource(Frequency f){
		Context c=Singleton.getInstance().getContext();
		switch(f){
		case once:
			return c.getString(R.string.f_once);
		case regular:
			return c.getString(R.string.f_regular);
		case sometimes:
			return c.getString(R.string.f_sometimes);
		default://So the compiler will shut up
			return "";
		}
	}
	
	/**
	 * This method returns the drawable-resource-id 
	 * for the requested Transportation
	 * @param t the Transportation
	 * @return the drawable-resource-id
	 */
	public static int getTransportIconResource(Transportation t){
		switch(t){
		case bicycle:
			return R.drawable.ic_transport_bicycle_inverse;
		case bus:
			return R.drawable.ic_transport_bus_inverse;
		case car:
			return R.drawable.ic_transport_car_inverse;
		case foot:
			return R.drawable.ic_transport_foot_inverse;
		case motocycle:
			return R.drawable.ic_transport_motorcycle_inverse;
		case other1:
			return R.drawable.ic_transport_other1;
		case other2:
			return R.drawable.ic_transport_other2;
		case other3:
			return R.drawable.ic_transport_other3;
		case plane:
			return R.drawable.ic_transport_plane_inverse;
		case ship:
			return R.drawable.ic_transport_ship_inverse;
		case train:
			return R.drawable.ic_transport_train_inverse;
		default:
			return 0;
		}
	}
	
	/**
	 * This method returns the string-resource-id 
	 * for the requested Transportation
	 * @param t the Transportation
	 * @return the string-resource-id
	 */
	public static String getTransportStringResource(Transportation t){
		Context c=Singleton.getInstance().getContext();
		switch(t){
		case bicycle:
			return c.getString(R.string.t_bicycle);
		case bus:
			return c.getString(R.string.t_bus);
		case car:
			return c.getString(R.string.t_car);
		case foot:
			return c.getString(R.string.t_foot);
		case motocycle:
			return c.getString(R.string.t_motocycle);
		case other1:
			return c.getString(R.string.t_other1);
		case other2:
			return c.getString(R.string.t_other2);
		case other3:
			return c.getString(R.string.t_other3);
		case plane:
			return c.getString(R.string.t_plane);
		case ship:
			return c.getString(R.string.t_ship);
		case train:
			return c.getString(R.string.t_train);
		default:
			return "";
		}
	}
	
	/**
	 * This method returns the drawable-resource-id 
	 * for the requested Share
	 * @param s the Share
	 * @return the drawable-resource-id
	 */
	public static int getShareIconResource(Share s){
		switch(s){
		case friends:
			return R.drawable.ic_share_friends;
		case global:
			return R.drawable.ic_share_global;
		case no:
			return R.drawable.ic_share_no;
		default:
			return 0;
		
		}
	}
	
	/**
	 * This method returns the string-resource-id 
	 * for the requested Share
	 * @param s the Share
	 * @return the string-resource-id
	 */
	public static String getShareStringResource(Share s){
		Context c=Singleton.getInstance().getContext();
		switch(s){
		case friends:
			return c.getString(R.string.s_friends);
		case global:
			return c.getString(R.string.s_global);
		case no:
			return c.getString(R.string.s_no);
		default:
			return "";
		
		}
	}
	
	/**
	 * This method returns the string-resource-ids
	 * for Frequency
	 * @return the string-resource-ids
	 */
	public static String[] getFrequencyStrings(){
		String[] strings = new String[Frequency.values().length];
		for(int i=0;i<Frequency.values().length;i++){
			strings[i]=getFrequencyStringResource(Frequency.values()[i]);
		}
		return strings;
	}
	
	/**
	 * This method returns the string-resource-ids
	 * for Transportation
	 * @return the string-resource-ids
	 */
	public static String[] getTransportStrings(){
		String[] strings = new String[Transportation.values().length];
		for(int i=0;i<Transportation.values().length;i++){
			strings[i]=getTransportStringResource(Transportation.values()[i]);
		}
		return strings;
	}
	
	/**
	 * This method returns the string-resource-ids
	 * for Share
	 * @return the string-resource-ids
	 */
	public static String[] getShareStrings(){
		String[] strings = new String[Share.values().length];
		for(int i=0;i<Share.values().length;i++){
			strings[i]=getShareStringResource(Share.values()[i]);
		}
		return strings; 
	}

	/**
	 * This method returns the drawable-resource-ids
	 * for Transportation
	 * @return the drawable-resource-ids
	 */
	public static int[] getTransportIconResources(){
		int[] res = new int[Transportation.values().length];
		for(int i=0;i<Transportation.values().length;i++){
			res[i]=getTransportIconResource(Transportation.values()[i]);
		}
		return res;
	}
	
	/**
	 * This method returns the drawable-resource-ids
	 * for Share
	 * @return the drawable-resource-ids
	 */
	public static int[] getShareIconResources(){
		int[] res = new int[Share.values().length];
		for(int i=0;i<Share.values().length;i++){
			res[i]=getShareIconResource(Share.values()[i]);
		}
		return res;
	}
	
	/**
	 * This method returns the drawable-resource-ids
	 * for Frequency
	 * @return the drawable-resource-ids
	 */
	public static int[] getFrequencyIconResources(){
		int[] res = new int[Frequency.values().length];
		for(int i=0;i<Frequency.values().length;i++){
			res[i]=getFrequencyIconResource(Frequency.values()[i]);
		}
		return res;
	}
	
//---------general-----------	
	
	/**
	 * This method reduces the float-value 
	 * to an accuracy of one digit after the decimal point
	 * 
	 * @param f the float-value to reduce the accuracy
	 * @return the float-value with an accuracy of one digit after the decimal point
	 */
	public static float cutFloat(float f){
		float temp=f*10;
		float temp2=(int)temp;
		f=temp2/10;
		return f;
	}
	
	/**
	 * This method return the date from the Calendar-object as String
	 * formatted like "dd.MM.yyyy" as String
	 * @param c the Calendar-object the date should be extracted from
	 * @return the formatted date string
	 */
	public static String getDateAsString(Calendar c){
		return String.format(Locale.getDefault(),"%02d.%02d.%d", c.get(Calendar.DATE),c.get(Calendar.MONTH)+1,c.get(Calendar.YEAR));
	}
	
	/**
	 * This method return the time from the Calendar-object as String
	 * formatted like "hh:mm" as String
	 * @param c the Calendar-object the date should be extracted from
	 * @return the formatted time string
	 */
	public static String getTimeAsString(Calendar c){
		return String.format(Locale.getDefault(),"%02d:%02d", c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE));
	}
	
	/**
	 * This method return a time string generated from the parameters
	 * formatted like "hh:mm:ss"
	 * @param hours the hour count
	 * @param minutes the minute count
	 * @param seconds the second count
	 * @return the formatted time string
	 */
	public static String getTimeAsString(int hours, int minutes, int seconds){
		return String.format(Locale.getDefault(),"%02d:%02d:%02d",hours,minutes,seconds);
	}
	
	/**
	 * This method return a time string calculated from the parameter
	 * formatted like "hh:mm:ss"
	 * @param seconds the second count
	 * @return the formatted time string
	 */
	public static String getTimeAsString(int seconds){
		int minutes=seconds/60;
		int hours=minutes/60;
		seconds=seconds%60;
		minutes=minutes%60;
		return String.format(Locale.getDefault(),"%02d:%02d:%02d",hours,minutes,seconds);
	}
	
	public static long getCalendarDifferenceInSeconds(Calendar c1, Calendar c2){
		if(c1.compareTo(c2)>=0)
			return (c1.getTimeInMillis() - c2.getTimeInMillis())/1000;
		else
			return (c2.getTimeInMillis() - c1.getTimeInMillis())/1000;
	}
	
	/**
	 * This method generates a name with the current system-date and time
	 * @return generated name with the current system-date and time
	 */
	public static String generateName(){
		GregorianCalendar gc= new GregorianCalendar();
		return "Strecke "+Util.getDateAsString(gc)+"  "+Util.getTimeAsString(gc);
	}
	
	/**
	 * This method sorts the LogbookEntry-objects by creation-time
	 * @param p the Player-object containing the LogbookEntry-objects
	 */
	public static void sortEntries(Player p){
		ArrayList<LogbookEntry> entries = p.getLogbook();
		LogbookEntry temp;
		for(int i=0;i<entries.size();i++){
			for(int j=0;j<entries.size();j++){
				if(entries.get(j).getDate().compareTo(entries.get(i).getDate())==-1){
					temp = entries.get(i);
					entries.set(i, entries.get(j));
					entries.set(j, temp);
				}
			}
		}
	}

	/**
	 * A simple algorithm to calculate the points needed to reach the next level
	 * @param level the next level
	 * @return how many points are needed to reach the next level
	 */
	public static int PointsPerLevel(int level){
		int value=5;
		int add=3;
		
		for(int i=0;i<level-1;i++){
//			System.out.print(value+",");
			add+=(value/add)/2;
			value+=add;
			
		}
		return value;
	}
	
	/**
	 * This method converts the duracy in seconds into a time string
	 * formatted like "hh:mm:ss"
	 * @param duracy the duracy in seconds
	 * @return the time string
	 */
	public static String getDuracyAsTimeString(long duracy){
		int hours=(int)duracy/3600;
		int minutes=(int)(duracy%3600)/60;
		int seconds=(int)(duracy%3600)%60;
		return hours+":"+minutes+":"+seconds;
	}

	/**
	 * This method extracts a PolylineOptions-object from a string
	 * with a width of 6 and red color
	 * @param path the string containing the longitude  and latitude
	 * 			separated by comma
	 * @return a PolylineOptions-object with a width of 6 and red color
	 */
	public static PolylineOptions getPolylineFromString(String path){
		PolylineOptions line=new PolylineOptions();
		line.width(6);
		line.color(Color.RED);
		Scanner scan=new Scanner(path);
		scan.useDelimiter(",");
		while(scan.hasNext()){
			line.add(new LatLng(Double.parseDouble(scan.next()),Double.parseDouble(scan.next())));
		}
		scan.close();
		return line;
	}
	
	/**
	 * This method generates a string from a PolylineOptions-object
	 * @param line PolylineOptions-object which should be converted
	 * @return the gerated string containing the longitude  and latitude
	 * 			separated by comma
	 */
	public static String getStringFromPolyline(PolylineOptions line){
		String path="";
		LatLng point;
		for(int i=0;i<line.getPoints().size();i++){
			point=line.getPoints().get(i);
			path+=point.latitude+",";
			path+=point.longitude+",";
		}
		return path;
	}
	
	/**
	 * This method validates the the average speed of the current track
	 * to prevent cheating.
	 * @param averageSpeed the average speed of the current track
	 * @param t the current way of transportation
	 * @return if valid true, else false
	 */
	public static boolean validateTrack(float averageSpeed, Transportation t){
		switch(t){
		case foot:
			if(averageSpeed>15)
				return false;
			break;
		case bicycle:
			if(averageSpeed>30)
				return false;
			break;
		case other1:
			//Other good ways of transportation won't be faster than bicycle
			//I.e InlineSkates, Skateboard, ...
			if(averageSpeed>30)
				return false;
			break;
		default:
			return true;
		}
		return true;
	}
	
	/**
	 * This method returns a drawable-resource-id depending on the current
	 * shares(of the current month) from the Stats-object of the Player-object
	 * @param player the player
	 * @return a drawable-resource-id
	 */
	public static int getCurrentAvatar(Player player){
		Stats s=player.getStats();
		float sharegood = s.getShares().get(3);
		float sharemedium = s.getShares().get(4);
		float sharebad = s.getShares().get(5);
		if(sharegood==0&&sharemedium==0&&sharebad==0){
			return R.drawable.avatar_empty;
		}
		else if(sharegood>=sharemedium&&sharegood>sharebad){
			return R.drawable.avatar_best;
		}
		else if(sharemedium>=sharegood&&sharemedium>sharebad){
			return R.drawable.avatar_good;
		}
		else{
			return R.drawable.avatar_bad;
		}
	}
	
	/**
	 * This method checks if WIFI or 3G are activated and connected
	 * @return true if connected else false
	 */
	public static boolean networkAvailable(){
		ConnectivityManager connManager = (ConnectivityManager) Singleton.getInstance().getContext().
				getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo m3G = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		
		if (mWifi.isConnected()) {
		    return true;
		}
		else if (m3G.isConnected()) {
			return true;
		}
		else{
			Toast.makeText(Singleton.getInstance().getContext(), "no Connection", Toast.LENGTH_SHORT).show();
			return false;
		}
	}
}
