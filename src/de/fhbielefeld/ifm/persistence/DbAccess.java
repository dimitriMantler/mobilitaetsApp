package de.fhbielefeld.ifm.persistence;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Scanner;

import android.database.Cursor;
import de.fhbielefeld.ifm.logic.Enums;
import de.fhbielefeld.ifm.logic.Friend;
import de.fhbielefeld.ifm.logic.LogbookEntry;
import de.fhbielefeld.ifm.logic.Medal;
import de.fhbielefeld.ifm.logic.MonthRace;
import de.fhbielefeld.ifm.logic.Player;
import de.fhbielefeld.ifm.logic.Singleton;
import de.fhbielefeld.ifm.logic.Stats;

/**
 * This class is used for loading and saving the Player-object.
 * It assembles the retrieved data.
 * 
 * @author Dimitri Mantler
 *
 */
public class DbAccess {

	/**
	 * This method loads all own medals from the local database
	 * and returns them in an ArrayList
	 * @param mDbHelper
	 * @return an ArrayList with all own medals
	 */
    private static ArrayList<Medal> restoreOwnMedals(DbAdapter mDbHelper) {
    	ArrayList<Medal> medals=new ArrayList<Medal>();
        Cursor c = mDbHelper.fetchOwnMedals();
        c.moveToFirst();
        for(int i=0;i<c.getCount();i++){
        	medals.add(restoreMedal(c));
        	c.moveToNext();
        }
        return medals;
    }
    
    /**
	 * This method loads all earned medals from the local database
	 *  and returns them in an ArrayList
	 * @param mDbHelper
	 * @return an ArrayList with all earned medals
	 */
    private static ArrayList<Medal> restoreEarnedMedals(DbAdapter mDbHelper) {
    	ArrayList<Medal> medals=new ArrayList<Medal>();
        Cursor c = mDbHelper.fetchEarnedMedals();
        c.moveToFirst();
        for(int i=0;i<c.getCount();i++){
        	medals.add(restoreMedal(c));
        	c.moveToNext();
        }
        return medals;
    }
 
    /**
	 * This method loads a Statsobject from the local database
	 * @param mDbHelper
	 * @return a Stats-object
	 */
    private static Stats restoreStats(DbAdapter mDbHelper){
   	 Cursor c = mDbHelper.fetchStats();
        c.moveToFirst();
   	Scanner scan=new Scanner(c.getString(0));
   	scan.useDelimiter(",");
   	ArrayList<Float> shares= new ArrayList<Float>();
   	while(scan.hasNext()){
   		shares.add(Float.parseFloat(scan.next()));
   	}
   	scan.close();
   	scan=new Scanner(c.getString(1));
   	scan.useDelimiter(",");
   	ArrayList<Integer> waysCount= new ArrayList<Integer>();
   	while(scan.hasNextInt()){
   		waysCount.add(scan.nextInt());
   	}
   	scan.close();
   	scan=new Scanner(c.getString(2));
   	scan.useDelimiter(",");
   	ArrayList<Float> waysDistance= new ArrayList<Float>();
   	while(scan.hasNext()){
   		waysDistance.add(Float.parseFloat(scan.next()));
   	}
   	scan.close(); 
   	
   	Stats stats=new Stats();
   	stats.setShares(shares);
   	stats.setWaysCount(waysCount);
   	stats.setWaysDistance(waysDistance);
   	
   	return stats;
   }
    
    /**
	 * This method loads a Player-object from the local database
	 * @param mDbHelper
	 * @return a Player-object
	 */
    private static Player restorePlayer(DbAdapter mDbHelper){
    	GregorianCalendar lastUpdate=new GregorianCalendar();
    	
    	Cursor c = mDbHelper.fetchPlayer();
        c.moveToFirst();
        
        Player player=new Player();
        player.setId(c.getLong(0));
        player.setName(c.getString(1));
        player.setEmail(c.getString(2));
        player.setPassword(c.getString(3));
        player.setLevel(c.getInt(4));
        player.setPoints(c.getInt(5));
        player.setToNext(c.getInt(6));
        player.setRank(c.getLong(7));
        lastUpdate.setTimeInMillis(c.getLong(8));
        player.setLastUpdate(lastUpdate);
        
        
      	return player;
      }
    
    /**
	 * This method loads all friends from the local database
	 *  and returns them in an ArrayList
	 * @param mDbHelper
	 * @return an ArrayList with all friends
	 */
    private static ArrayList<Friend> restoreFriends(DbAdapter mDbHelper) {
    	ArrayList<Friend> friends=new ArrayList<Friend>();
        Cursor c = mDbHelper.fetchFriends();
        c.moveToFirst();
        for(int i=0;i<c.getCount();i++){
        	friends.add(restoreFriend(c));
        	c.moveToNext();
        }
        return friends;
    }
    
    /**
	 * This method loads all LogbookEntries from the local database
	 *  and returns them in an ArrayList
	 * @param mDbHelper
	 * @return an ArrayList with all LogbookEntries
	 */
    private static ArrayList<LogbookEntry> restoreLogbookEntries(DbAdapter mDbHelper) {
    	ArrayList<LogbookEntry> logs=new ArrayList<LogbookEntry>();
        Cursor c = mDbHelper.fetchLogbookEntries();
        c.moveToFirst();
        for(int i=0;i<c.getCount();i++){
        	logs.add(restoreLogbookEntry(c));
        	c.moveToNext();
        }
        return logs;
    }
    
    /**
	 * This method loads all monthraces from the local database
	 *  and returns them in an ArrayList
	 * @param mDbHelper
	 * @return an ArrayList with all monthraces
	 */
    private static ArrayList<MonthRace> restoreMonthRaces(DbAdapter mDbHelper) {
    	ArrayList<MonthRace> races=new ArrayList<MonthRace>();
        Cursor c = mDbHelper.fetchMonthRaces();
        c.moveToFirst();
        for(int i=0;i<c.getCount();i++){
        	races.add(restoreMonthRace(c));
        	c.moveToNext();
        }
        return races;
    }

//-----------------    
    
    /**
     * This method loads a single Medal from the local database
     * @param c the current Cursor
     * @return a Medal-object
     */
    private static Medal restoreMedal(Cursor c){
    	GregorianCalendar gc=new GregorianCalendar();
    	Medal m=new Medal();
    	
    	m.setId(c.getLong(0));
    	m.setName(c.getString(1));
    	m.setDescription(c.getString(2));
    	m.setCreator(c.getString(3));
    	gc.setTimeInMillis(c.getLong(4));
    	m.setCreationDate(gc);
    	m.setPrize(c.getInt(5));
    	m.setImageurl(c.getString(6));
    	return m;
    }
    
    /**
     * This method loads a single LogbookEntry from the local database
     * @param c the current Cursor
     * @return a LogbookEntry-object
     */
    private static LogbookEntry restoreLogbookEntry(Cursor c){
    	GregorianCalendar date=new GregorianCalendar();
    	GregorianCalendar lastUpdate=new GregorianCalendar();
    	LogbookEntry log=new LogbookEntry();
    	
    	log.setId(c.getLong(0));
    	log.setPath(c.getString(1));
    	log.setName(c.getString(2));
    	log.setDescription(c.getString(3));
    	log.setDuracy(c.getLong(4));
    	log.setAverageSpeed(c.getLong(5));
    	log.setDistance(c.getFloat(6));
    	date.setTimeInMillis(c.getLong(7));
    	log.setDate(date);
    	log.setFrequency(Enums.Frequency.valueOf(c.getString(8)));
    	log.setTransport(Enums.Transportation.valueOf(c.getString(9)));
    	log.setShare(Enums.Share.valueOf(c.getString(10)));
    	if(c.getInt(11)==1)log.setSync(true);
    	else log.setSync(false);
    	lastUpdate.setTimeInMillis(c.getLong(12));
    	log.setLastUpdate(lastUpdate);
    	return log;
    }
    
    /**
     * This method loads a single MonthRace from the local database
     * @param c the current Cursor
     * @return a MonthRace-object
     */
    private static MonthRace restoreMonthRace(Cursor c){
    	Scanner scan=new Scanner(c.getString(2));
    	scan.useDelimiter(",");
    	ArrayList<Long> participants= new ArrayList<Long>();
    	while(scan.hasNextLong()){
    		participants.add(scan.nextLong());
    	}
    	scan.close();
    	scan=new Scanner(c.getString(7));
    	scan.useDelimiter(",");
    	ArrayList<Long> medals= new ArrayList<Long>();
    	while(scan.hasNextLong()){
    		medals.add(scan.nextLong());
    	}
    	scan.close();
    	GregorianCalendar startDate=new GregorianCalendar();
    	GregorianCalendar lastUpdate=new GregorianCalendar();
    	MonthRace race=new MonthRace();
    	
    	race.setId(c.getLong(0));
    	race.setName(c.getString(1));
    	race.setParticipants(participants);
    	race.setAdmin(c.getString(3));
    	race.setShare(Enums.Share.valueOf(c.getString(4)));
    	startDate.setTimeInMillis(c.getLong(5));
    	race.setStartDate(startDate);
    	race.setLimit(c.getInt(6));
    	race.setMedals(medals);
    	race.setAveragePoints(c.getInt(8));
    	race.setMinLevel(c.getInt(9));
    	race.setPot(c.getInt(10));
    	lastUpdate.setTimeInMillis(c.getLong(11));
    	race.setLastUpdate(lastUpdate);
    	return race;
    }
    
    /**
     * This method loads a single friend from the local database
     * @param c the current Cursor
     * @return a Friend-object
     */
    private static Friend restoreFriend(Cursor c){
    	Scanner scan=new Scanner(c.getString(7));
    	scan.useDelimiter(",");
    	ArrayList<Long> sharedLogs= new ArrayList<Long>();
    	while(scan.hasNextLong()){
    		sharedLogs.add(scan.nextLong());
    	}
    	scan.close();
    	scan=new Scanner(c.getString(8));
    	scan.useDelimiter(",");
    	ArrayList<Long> medals= new ArrayList<Long>();
    	while(scan.hasNextLong()){
    		medals.add(scan.nextLong());
    	}
    	scan.close();
    	Friend friend=new Friend();
    	
    	friend.setId(c.getLong(0));
    	friend.setName(c.getString(1));
    	friend.setLevel(c.getInt(2));
    	friend.setPoints(c.getInt(3));
    	friend.setShareGood(c.getFloat(4));
    	friend.setShareMedium(c.getFloat(5));
    	friend.setShareBad(c.getFloat(6));
    	friend.setSharedLogs(sharedLogs);
    	friend.setMedals(medals);
    	friend.setWaysCountCurrent(c.getInt(9));
    	friend.setWaysDistanceCurrent(c.getFloat(10));
    	friend.setWaysCountTotal(c.getInt(11));
    	friend.setWaysDistanceTotal(c.getFloat(12));
    	return friend;
    }

//----------------- 
    
    /**
     * This method saves the current playerobject from Singleton
     * to the local sqlite-database
     */
    public static void saveData(){
    	Player player = Singleton.getInstance().getPlayer();
    	GregorianCalendar gc=new GregorianCalendar(); 
    	System.out.println("Save data..");
    	DbAdapter mDbHelper=new DbAdapter(Singleton.getInstance().getContext());
		mDbHelper.deleteDatabase();
        mDbHelper.open();
        
        mDbHelper.storePlayer(player);
        mDbHelper.storeStats(player.getStats());
        for(int i=0;i<player.getOwnMedals().size();i++){
        	mDbHelper.storeOwnMedal(player.getOwnMedals().get(i));
        }
        for(int i=0;i<player.getEarnedMedals().size();i++){
        	mDbHelper.storeEarnedMedal(player.getEarnedMedals().get(i));
        }
        for(int i=0;i<player.getMonthraces().size();i++){
        	mDbHelper.storeMonthRace(player.getMonthraces().get(i));
        }
        for(int i=0;i<player.getLogbook().size();i++){
        	mDbHelper.storeLogbookEntry(player.getLogbook().get(i));
        }
        for(int i=0;i<player.getFriends().size();i++){
        	mDbHelper.storeFriend(player.getFriends().get(i));
        }
        mDbHelper.close();
        System.out.println("Data saved in "+((float)(new GregorianCalendar()
        	.getTimeInMillis()-gc.getTimeInMillis())/1000)+" seconds!");
    }
    
    /**
     * This method loads playerdata from the local sqlite database
     * and sets them to Singleton
     * @return the loaded Playerobject
     */
    public static Player loadData(){
//    	ProgressDialog pd=new ProgressDialog(Singleton.getInstance().getContext());
//    	pd.setTitle(Singleton.getInstance().getContext().getString(R.string.loading_data));
//    	pd.show();
    	DbAdapter mDbHelper=new DbAdapter(Singleton.getInstance().getContext());
    	mDbHelper.open();
    	GregorianCalendar gc=new GregorianCalendar(); 
    	System.out.println("Load data..");
    	Player player=restorePlayer(mDbHelper);
    	player.setOwnMedals(restoreOwnMedals(mDbHelper));
    	player.setEarnedMedals(restoreEarnedMedals(mDbHelper));
    	player.setLogbook(restoreLogbookEntries(mDbHelper));
    	player.setFriends(restoreFriends(mDbHelper));
    	player.setMonthraces(restoreMonthRaces(mDbHelper));
    	player.setStats(restoreStats(mDbHelper));
    	System.out.println("Data loaded in "+((float)(new GregorianCalendar()
    		.getTimeInMillis()-gc.getTimeInMillis())/1000)+" seconds!");
    	mDbHelper.close();
    	Singleton.getInstance().setPlayer(player);
//    	pd.dismiss();
    	return player;
    }
	
}
