package de.fhbielefeld.ifm.logic;

import java.util.*;

import org.simpleframework.xml.*;

@Root
@Default(DefaultType.FIELD) 
public class Player{

	long id;
	String name;
	String email;
	String password;
	int level;
	int points;
	int toNext;
	long rank;
	ArrayList<LogbookEntry> logbook;
	ArrayList<MonthRace> monthraces;
	ArrayList<Friend> friends;
	ArrayList<Medal> ownMedals;
	ArrayList<Medal> earnedMedals;
	Stats stats;
	GregorianCalendar lastUpdate;
	
	public Player(){}
	
	public Player(long id, String name, String email, String password, int level, int points, int toNext, long rank, ArrayList<LogbookEntry> logbook, ArrayList<MonthRace> monthraces, ArrayList<Friend> friends, ArrayList<Medal> ownMedals,  ArrayList<Medal> earnedMedals, Stats stats, GregorianCalendar lastUpdate){
		this.id=id;
		this.name=name;
		this.email=email;
		this.password=password;
		this.level=level;
		this.points=points;
		this.toNext=toNext;
		this.rank=rank;
		this.logbook=logbook;
		this.monthraces=monthraces;
		this.friends=friends;
		this.ownMedals=ownMedals;
		this.earnedMedals=earnedMedals;
		this.stats=stats;
		this.lastUpdate=lastUpdate;
	}
	
	public Player(String name){//Dummiplayer
		this.id=new Random().nextInt();
		this.name=name;
		this.email="max_m@gmail.com";
		this.password="123456";
		this.logbook=new ArrayList<LogbookEntry>();
		this.monthraces=new ArrayList<MonthRace>();
		this.friends=new ArrayList<Friend>(); //just the names will be saved
		this.ownMedals=new ArrayList<Medal>();
		this.earnedMedals=new ArrayList<Medal>();
		this.stats=new Stats();
		this.lastUpdate=new GregorianCalendar();
	}
	
//============getter==================
	
	public long getId(){
		return this.id;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getEmail(){
		return this.email;
	}
	
	public String getPassword(){
		return this.password;
	}
	
	public int getLevel(){
		return this.level;
	}
	
	public int getPoints(){
		return this.points;
	}
	
	public int getToNext(){
		return this.toNext;
	}
	
	public long getRank(){
		return this.rank;
	}
	
	public ArrayList<LogbookEntry> getLogbook(){
		return this.logbook;
	}
	
	public ArrayList<MonthRace> getMonthraces(){
		return this.monthraces;
	}
	
	public ArrayList<Friend> getFriends(){
		return this.friends;
	}
	
	public ArrayList<Medal> getOwnMedals(){
		return this.ownMedals;
	}
	
	public ArrayList<Medal> getEarnedMedals(){
		return this.earnedMedals;
	}
	
	public Stats getStats(){
		return this.stats;
	}
	
	public GregorianCalendar getLastUpdate(){
		return this.lastUpdate;
	}
	
//============setter==================	
	
	public void setId(long id){
		this.id=id;
	}
	
	public void setName(String name){
		this.name=name;
	}
	
	public void setEmail(String email){
		this.email=email;
	}
	
	public void setPassword(String password){
		this.password=password;
	}
	
	public void setLevel(int level){
		this.level=level;
	}
	
	public void setPoints(int points){
		this.points=points;
	}
	
	public void setToNext(int toNext){
		this.toNext=toNext;
	}
	
	public void setRank(long rank){
		this.rank=rank;
	}
	
	public void setLogbook(ArrayList<LogbookEntry> logbook){
		this.logbook=logbook;
	}
	
	public void setMonthraces(ArrayList<MonthRace> monthraces){
		this.monthraces=monthraces;
	}
	
	public void setFriends(ArrayList<Friend> friends){
		this.friends=friends;
	}
	
	public void setOwnMedals(ArrayList<Medal> medals){
		this.ownMedals=medals;
	}
	
	public void setEarnedMedals(ArrayList<Medal> medals){
		this.earnedMedals=medals;
	}
	
	public void setStats(Stats stats){
		this.stats=stats;
	}
	
	public void setLastUpdate(GregorianCalendar lastUpdate){
		this.lastUpdate=lastUpdate;
	}
	
//==============================
	public void addFriend(Friend friend){
		this.friends.add(friend);
	}
	
	public void removeFriend(int index){
		this.friends.remove(index);
	}
	
	public void addLogbookEntry(LogbookEntry entry){
		this.logbook.add(entry);
	}
	
	public void addMonthlyRace(MonthRace monthRace) {
		this.monthraces.add(monthRace);
	}
	
	public String toString(){
		return this.id+"\n"+this.name+"\n"+this.email+"\n"+this.password+"\n"+this.level+"\n"+this.points+"\n"+this.toNext+"\n"+this.rank+"\n"+this.logbook+"\n"+this.monthraces+"\n"+this.friends+"\n"+this.ownMedals+"\n"+this.earnedMedals+"\n"+this.stats+"\n"+this.stats+"\n"+this.lastUpdate.getTime();
	}
	
}
