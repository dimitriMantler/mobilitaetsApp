package de.fhbielefeld.ifm.persistence;

import de.fhbielefeld.ifm.logic.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * This class manages the tables, stores and loads objects and values
 * from the local sqlite-database
 * 
 * @author Dimitri Mantler
 *
 */
public class DbAdapter {
	
	//general
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_SHARE = "share";
    public static final String KEY_LASTUPDATE = "lastupdate";
    public static final String KEY_LEVEL = "level";
    public static final String KEY_POINTS = "points";
    //medals
    public static final String KEY_CREATOR = "creator";
    public static final String KEY_CREATIONDATE = "creationdate";
    public static final String KEY_PRIZE = "prize";
    public static final String KEY_IMAGEURL = "imageurl";
    //logbookentries
    public static final String KEY_DURACY = "duracy";
    public static final String KEY_PATH = "path";
    public static final String KEY_AVERAGESPEED = "averagespeed";
    public static final String KEY_DISTANCE = "distance";
    public static final String KEY_DATE = "date";
    public static final String KEY_FREQUENCY = "frequency";
    public static final String KEY_TRANSPORT = "transport";
    public static final String KEY_SYNCED = "synced";
    //monthraces
    public static final String KEY_PARTICIPANTS = "participants";
    public static final String KEY_ADMIN = "admin";
    public static final String KEY_STARTDATE = "startdate";
    public static final String KEY_LIMIT = "playerlimit"; //limit is reseved word
    public static final String KEY_MEDALS = "medals";
    public static final String KEY_AVERAGEPOINTS = "averagepoints";
    public static final String KEY_MINLEVEL = "minlevel";
    public static final String KEY_POT = "pot";
    //friends 
    public static final String KEY_SHAREGOOD = "sharegood";
    public static final String KEY_SHAREMEDIUM = "sharemedium";
    public static final String KEY_SHAREBAD = "sharebad";
    public static final String KEY_SHAREDLOGS = "sharedlogs";
    public static final String KEY_WAYSCOUNTCURRENT = "wayscountcurrent";
    public static final String KEY_WAYSDISTANCECURRENT = "waysdistancecurrent";
    public static final String KEY_WAYSCOUNTTOTAL = "wayscounttotal";
    public static final String KEY_WAYSDISTANCETOTAL = "waysdistancetotal";
    //stats
    public static final String KEY_SHARES = "shares";
    public static final String KEY_WAYSCOUNT = "wayscount";
    public static final String KEY_WAYSDISTANCE = "waysdistance";
    //player
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_TONEXT = "tonext";
    public static final String KEY_RANK = "rank";
    
    private static final String TAG = "DbAdapter";
    private DatabaseHelper mDbHelper; 
    private SQLiteDatabase mDb; 

    private static final String DATABASE_CREATE_TABLE_OWNMEDALS ="create table ownmedals (id integer not null, name text not null, " +
    																					  "description text not null, " +
    																					  "creator text not null, " +
    																					  "creationdate integer not null," +
    																					  "prize integer not null, " +
    																					  "imageurl text not null);";
    
    private static final String DATABASE_CREATE_TABLE_EARNEDMEDALS ="create table earnedmedals (id integer not null, name text not null, " +
																								"description text not null, " +
																								"creator text not null, " +
																								"creationdate integer not null," +
																								"prize integer not null, " +
																								"imageurl text not null);";
    
    private static final String DATABASE_CREATE_TABLE_LOGBOOKENTRIES ="create table logbookentries (id integer not null, " +
    																								"path text not null, " +
																									"name text not null, " +
																									"description text not null, " +
																									"duracy integer not null, " +
																									"averageSpeed real not null," +
																									"distance real not null, " +
																									"date integer not null," +
																									"frequency text not null," +
																									"transport text not null," +
																									"share text not null," +
																									"synced integer not null," +
																									"lastupdate integer not null);";
    
    private static final String DATABASE_CREATE_TABLE_MONTHRACES ="create table monthraces (id integer not null, " +
																							"name text not null, " +
																							"participants text not null, " +//indexes separated by comma  i.E. 336345,4757575,474747,45747,
																							"admin text not null, " +
																							"share text not null," +
																							"startdate integer not null, " +
																							"playerlimit integer not null," +
																							"medals text not null," +//indexes separated by comma  i.E. 336345,4757575,474747,45747,
																							"averagepoints integer not null," +
																							"minlevel integer not null," +
																							"pot integer not null," +
																							"lastupdate integer not null);";
    
    private static final String DATABASE_CREATE_TABLE_FRIENDS ="create table friends (id integer not null, " +
																					  "name text not null, " +
																					  "level integer not null, " +
																					  "points integer not null, " +
																					  "sharegood real not null," +
																					  "sharemedium real not null, " +
																					  "sharebad real not null," +
																					  "sharedlogs text not null," +//indexes separated by comma  i.E. 336345,4757575,474747,45747,
																					  "medals text not null," +//indexes separated by comma  i.E. 336345,4757575,474747,45747,
																					  "wayscountcurrent integer not null," +
																					  "waysdistancecurrent real not null," +
																					  "wayscounttotal integer not null," +
																					  "waysdistancetotal real not null);";
    
    private static final String DATABASE_CREATE_TABLE_STATS ="create table stats (shares text not null, " +//values separated by comma  i.E. 336345,4757575,474747,45747,
			  																	  "wayscount text not null, " +//values separated by comma  i.E. 336345,4757575,474747,45747,
			  																	  "waysdistance text not null);";//values separated by comma  i.E. 336345,4757575,474747,45747,
    
    private static final String DATABASE_CREATE_TABLE_PLAYER ="create table player (id integer not null, " +
			  																		"name text not null, " +
			  																		"email text not null," +
			  																		"password text not null," +
			  																		"level integer not null," +
			  																		"points integer not null," +
			  																		"tonext integer not null," +
			  																		"rank integer not null," +
			  																		"lastupdate integer not null);";
    
    private static final String DATABASE_NAME = "data"; 
    private static final String DATABASE_TABLE_OWNMEDALS = "ownmedals";
    private static final String DATABASE_TABLE_EARNEDMEDALS = "earnedmedals";
    private static final String DATABASE_TABLE_LOGBOOKENTRIES = "logbookentries";
    private static final String DATABASE_TABLE_MONTHRACES = "monthraces";
    private static final String DATABASE_TABLE_FRIENDS = "friends";
    private static final String DATABASE_TABLE_STATS = "stats";
    private static final String DATABASE_TABLE_PLAYER = "player";
    private static final int DATABASE_VERSION = 2;

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(DATABASE_CREATE_TABLE_OWNMEDALS);
            db.execSQL(DATABASE_CREATE_TABLE_EARNEDMEDALS);
            db.execSQL(DATABASE_CREATE_TABLE_LOGBOOKENTRIES);
            db.execSQL(DATABASE_CREATE_TABLE_MONTHRACES);
            db.execSQL(DATABASE_CREATE_TABLE_FRIENDS);
            db.execSQL(DATABASE_CREATE_TABLE_STATS);
            db.execSQL(DATABASE_CREATE_TABLE_PLAYER);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS ownmedals");
            db.execSQL("DROP TABLE IF EXISTS earnedmedals");
            db.execSQL("DROP TABLE IF EXISTS monthraces");
            db.execSQL("DROP TABLE IF EXISTS friends");
            db.execSQL("DROP TABLE IF EXISTS stats");
            db.execSQL("DROP TABLE IF EXISTS logbookentries");
            db.execSQL("DROP TABLE IF EXISTS player");
            onCreate(db);
        }
        
    }
    
    public DbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the  database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public DbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void deleteDatabase(){
    	
    	mCtx.deleteDatabase(DATABASE_NAME);
    }
    
    public void close() {
        mDbHelper.close();
    }

//==============store===============
    /**
     * This method stores a ContentValues-object into the table
     * 
     * @param medal the Medal
     * @return rowId or -1 if failed
     */
    public long storeOwnMedal(Medal medal) {
    	ContentValues initialValues=storeMedal(medal);
        return mDb.insert(DATABASE_TABLE_OWNMEDALS, null, initialValues);
    }

    /**
     * This method stores a ContentValues-object into the table
     * 
     * @param medal the Medal
     * @return rowId or -1 if failed
     */
    public long storeEarnedMedal(Medal medal) {
    	ContentValues initialValues=storeMedal(medal);
        return mDb.insert(DATABASE_TABLE_EARNEDMEDALS, null, initialValues);
    }
    
    /**
     * This method stores a medal-object into a ContentValues-object
     * 
     * @param medal the Medal
     * @return rowId or -1 if failed
     */
    private ContentValues storeMedal(Medal medal){
    	ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ID, medal.getId());
        initialValues.put(KEY_NAME, medal.getName());
        initialValues.put(KEY_DESCRIPTION, medal.getDescription());
        initialValues.put(KEY_CREATOR, medal.getCreator());
        initialValues.put(KEY_CREATIONDATE, medal.getCreationDate().getTimeInMillis());
        initialValues.put(KEY_PRIZE, medal.getPrize());
        initialValues.put(KEY_IMAGEURL, medal.getImageurl());
        
        return initialValues;
    }
    
    /**
     * This method stores a LogbookEntry-object into the table
     * 
     * @param log the LogbookEntry
     * @return rowId or -1 if failed
     */
    public long storeLogbookEntry(LogbookEntry log){
    	ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ID, log.getId());
        initialValues.put(KEY_PATH, log.getPath());
        initialValues.put(KEY_NAME, log.getName());
        initialValues.put(KEY_DESCRIPTION, log.getDescription());
        initialValues.put(KEY_DURACY, log.getDuracy());
        initialValues.put(KEY_AVERAGESPEED, log.getAverageSpeed());
        initialValues.put(KEY_DISTANCE, log.getDistance());
        initialValues.put(KEY_DATE, log.getDate().getTimeInMillis());
        initialValues.put(KEY_FREQUENCY, log.getFrequency().toString());
        initialValues.put(KEY_TRANSPORT, log.getTransport().toString());
        initialValues.put(KEY_SHARE, log.getShare().toString());
        initialValues.put(KEY_SYNCED, log.getSync());
        initialValues.put(KEY_LASTUPDATE, log.getLastUpdate().getTimeInMillis());
        
        return mDb.insert(DATABASE_TABLE_LOGBOOKENTRIES, null, initialValues);
    }
    
    /**
     * This method stores a MonthRace-object into the table
     * 
     * @param race the MonthRace
     * @return rowId or -1 if failed
     */
    public long storeMonthRace(MonthRace race){
    	String participants="";
    	String medals="";
    	for(int i=0;i<race.getParticipants().size();i++){
    		participants+=race.getParticipants().get(i)+",";
    	}
    	for(int i=0;i<race.getMedals().size();i++){
    		medals+=race.getMedals().get(i)+",";
    	}
    	ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ID, race.getId());
        initialValues.put(KEY_NAME, race.getName());
        initialValues.put(KEY_PARTICIPANTS, participants);
        initialValues.put(KEY_ADMIN, race.getAdmin());
        initialValues.put(KEY_SHARE, race.getShare().toString());
        initialValues.put(KEY_STARTDATE, race.getStartDate().getTimeInMillis());
        initialValues.put(KEY_LIMIT, race.getLimit());
        initialValues.put(KEY_MEDALS, medals);
        initialValues.put(KEY_AVERAGEPOINTS, race.getAveragePoints());
        initialValues.put(KEY_MINLEVEL, race.getMinLevel());
        initialValues.put(KEY_POT, race.getPot());
        initialValues.put(KEY_LASTUPDATE, race.getLastUpdate().getTimeInMillis());
        
        return mDb.insert(DATABASE_TABLE_MONTHRACES, null, initialValues);
    }
    
    /**
     * This method stores a Friend-object into the table
     * 
     * @param friend the Friend
     * @return rowId or -1 if failed
     */
    public long storeFriend(Friend friend){
    	String sharedLogs="";
    	String medals="";
    	for(int i=0;i<friend.getSharedLogs().size();i++){
    		sharedLogs+=friend.getSharedLogs().get(i)+",";
    	}
    	for(int i=0;i<friend.getMedals().size();i++){
    		medals+=friend.getMedals().get(i)+",";
    	}
    	ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ID, friend.getId());
        initialValues.put(KEY_NAME, friend.getName());
        initialValues.put(KEY_LEVEL, friend.getLevel());
        initialValues.put(KEY_POINTS, friend.getPoints());
        initialValues.put(KEY_SHAREGOOD, friend.getShareGood());
        initialValues.put(KEY_SHAREMEDIUM, friend.getShareMedium());
        initialValues.put(KEY_SHAREBAD, friend.getShareBad());
        initialValues.put(KEY_SHAREDLOGS, sharedLogs);
        initialValues.put(KEY_MEDALS, medals);
        initialValues.put(KEY_WAYSCOUNTCURRENT, friend.getWaysCountCurrent());
        initialValues.put(KEY_WAYSDISTANCECURRENT,friend.getWaysDistanceCurrent());
        initialValues.put(KEY_WAYSCOUNTTOTAL, friend.getWaysCountTotal());
        initialValues.put(KEY_WAYSDISTANCETOTAL, friend.getWaysDistanceTotal());
        
        return mDb.insert(DATABASE_TABLE_FRIENDS, null, initialValues);
    }
    
    /**
     * This method stores a Stats-object into the table
     * 
     * @param stats the Stats
     * @return rowId or -1 if failed
     */
    public long storeStats(Stats stats){
    	String shares="";
    	String waysCount="";
    	String waysDistance="";
    	for(int i=0;i<stats.getShares().size();i++){
    		shares+=stats.getShares().get(i)+",";
    	}
    	for(int i=0;i<stats.getWaysCount().size();i++){
    		waysCount+=stats.getWaysCount().get(i)+",";
    	}
    	for(int i=0;i<stats.getWaysDistance().size();i++){
    		waysDistance+=stats.getWaysDistance().get(i)+",";
    	}
    	ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_SHARES, shares);
        initialValues.put(KEY_WAYSCOUNT, waysCount);
        initialValues.put(KEY_WAYSDISTANCE, waysDistance);
        
        return mDb.insert(DATABASE_TABLE_STATS, null, initialValues);
    }
    
    /**
     * This method stores a Player-object into the table
     * 
     * @param player the Player
     * @return rowId or -1 if failed
     */
    public long storePlayer(Player player){

    	ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ID, player.getId());
        initialValues.put(KEY_NAME, player.getName());
        initialValues.put(KEY_EMAIL, player.getEmail());
        initialValues.put(KEY_PASSWORD, player.getPassword());
        initialValues.put(KEY_LEVEL, player.getLevel());
        initialValues.put(KEY_POINTS, player.getPoints());
        initialValues.put(KEY_TONEXT, player.getToNext());
        initialValues.put(KEY_RANK, player.getRank());
        initialValues.put(KEY_LASTUPDATE, player.getLastUpdate().getTimeInMillis());
        
        return mDb.insert(DATABASE_TABLE_PLAYER, null, initialValues);
    }
    
//==============fetch===============
    /**
     * Return a Cursor over the list of all own medals in the database
     * 
     * @return Cursor over all own medals
     */
    public Cursor fetchOwnMedals() {

        return mDb.query(DATABASE_TABLE_OWNMEDALS, new String[] {KEY_ID,
                KEY_NAME, KEY_DESCRIPTION, KEY_CREATOR, KEY_CREATIONDATE, 
                KEY_PRIZE, KEY_IMAGEURL}, null, null, null, null, null);
    }

    /**
     * Return a Cursor over the list of all earned medals in the database
     * 
     * @return Cursor over all earned medals
     */
    public Cursor fetchEarnedMedals() {

        return mDb.query(DATABASE_TABLE_EARNEDMEDALS, new String[] {KEY_ID,
                KEY_NAME, KEY_DESCRIPTION, KEY_CREATOR, KEY_CREATIONDATE, 
                KEY_PRIZE, KEY_IMAGEURL}, null, null, null, null, null);
    }
    
    /**
     * Return a Cursor over the list of all LogbookEntries in the database
     * 
     * @return Cursor over all LogbookEntries
     */
    public Cursor fetchLogbookEntries() {

        return mDb.query(DATABASE_TABLE_LOGBOOKENTRIES, new String[] {KEY_ID,
                KEY_PATH, KEY_NAME, KEY_DESCRIPTION, KEY_DURACY, KEY_AVERAGESPEED, 
                KEY_DISTANCE, KEY_DATE, KEY_FREQUENCY, KEY_TRANSPORT, KEY_SHARE, 
                KEY_SYNCED, KEY_LASTUPDATE}, null, null, null, null, null);
    }
    
    /**
     * Return a Cursor over the list of all MonthRaces in the database
     * 
     * @return Cursor over all MonthRaces
     */
    public Cursor fetchMonthRaces() {

        return mDb.query(DATABASE_TABLE_MONTHRACES, new String[] {KEY_ID,
                KEY_NAME, KEY_PARTICIPANTS, KEY_ADMIN, KEY_SHARE, KEY_STARTDATE, 
                KEY_LIMIT, KEY_MEDALS, KEY_AVERAGEPOINTS, KEY_MINLEVEL, KEY_POT, 
                KEY_LASTUPDATE}, null, null, null, null, null);
    }
    
    /**
     * Return a Cursor over the list of all Friends in the database
     * 
     * @return Cursor over all Friends
     */
    public Cursor fetchFriends() {

        return mDb.query(DATABASE_TABLE_FRIENDS, new String[] {KEY_ID,
                KEY_NAME, KEY_LEVEL, KEY_POINTS, KEY_SHAREGOOD, KEY_SHAREMEDIUM, 
                KEY_SHAREBAD, KEY_SHAREDLOGS, KEY_MEDALS, KEY_WAYSCOUNTCURRENT, 
                KEY_WAYSDISTANCECURRENT, KEY_WAYSCOUNTTOTAL, KEY_WAYSDISTANCETOTAL}, 
                null, null, null, null, null);
    }
    
    /**
     * Return a Cursor over the Stats in the database
     * 
     * @return Cursor over the Stats
     */
    public Cursor fetchStats() {

        return mDb.query(DATABASE_TABLE_STATS, new String[] {KEY_SHARES,
        		KEY_WAYSCOUNT, KEY_WAYSDISTANCE}, null, null, null, null, null);
    }
    
    /**
     * Return a Cursor over the playerdata in the database
     * 
     * @return Cursor over the playerdata
     */
    public Cursor fetchPlayer() {

        return mDb.query(DATABASE_TABLE_PLAYER, new String[] {KEY_ID,
                KEY_NAME, KEY_EMAIL, KEY_PASSWORD, KEY_LEVEL, KEY_POINTS, KEY_TONEXT, KEY_RANK, 
                KEY_LASTUPDATE}, null, null, null, null, null);
    }

}
