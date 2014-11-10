package de.fhbielefeld.ifm.logic;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import org.simpleframework.xml.*;

import de.fhbielefeld.ifm.logic.Enums.*;


@Root
@Default(DefaultType.FIELD) 
public class MonthRace {

	long id;
	String name;
	ArrayList<Long> participants;
	String admin;
	Enums.Share share;
	GregorianCalendar startDate;
	int limit; // limits the amount of participants
	ArrayList<Long> medals;
	int averagePoints;
	int minLevel;
	int pot; // hold the amount of points the players can win
	GregorianCalendar lastUpdate;
	
	public MonthRace(){}
	
	public MonthRace(long id, String name, ArrayList<Long> participants, String admin, Enums.Share share, GregorianCalendar startDate, int limit, ArrayList<Long> medals, int averagePoints, int minLevel, int pot, GregorianCalendar lastUpdate){
		this.id=id;
		this.name=name;
		this.participants=participants;
		this.admin=admin;
		this.share=share;
		this.startDate=startDate;
		this.limit=limit;
		this.medals=medals;
		this.averagePoints=averagePoints;
		this.minLevel=minLevel;
		this.pot=pot;
		this.lastUpdate=lastUpdate;
	}
	
//=============getter=============
	
	public long getId(){
		return this.id;
	}
	
	public String getName(){
		return this.name;
	}
	
	//Returns List with names of befriended players
	public ArrayList<Long> getParticipants(){
		return this.participants;
	}
	
	public String getAdmin(){
		return this.admin;
	}
	
	public Share getShare(){
		return this.share;
	}
	
	public GregorianCalendar getStartDate(){
		return this.startDate;
	}
	
	public int getLimit(){
		return this.limit;
	}
	
	public ArrayList<Long> getMedals(){
		return this.medals;
	}
	
	public int getAveragePoints(){
		return this.averagePoints;
	}
	
	public int getMinLevel(){
		return this.minLevel;
	}
	
	public int getPot(){
		return this.pot;
	}
	
	public GregorianCalendar getLastUpdate(){
		return this.lastUpdate;
	}
	
//=============setter=============

	public void setId(long id){
		this.id=id;
	}
	
	public void setName(String name){
		this.name=name;
	}
	
	public void setParticipants(ArrayList<Long> participants){
		this.participants=participants;
	}
	
	public void setAdmin(String admin){
		this.admin=admin;
	}
	public void setShare(Share s){
		this.share=s;
	}
	
	public void setStartDate(GregorianCalendar gc){
		this.startDate=gc;
	}
	
	public void setLimit(int limit){
		this.limit=limit;
	}
	
	public void setMedals(ArrayList<Long> medals){
		this.medals=medals;
	}
	
	public void setAveragePoints(int averagePoints){
		this.averagePoints=averagePoints;
	}
	
	public void setMinLevel(int minLevel){
		this.minLevel=minLevel;
	}
	
	public void setPot(int pot){
		this.pot=pot;
	}
	
	public void setLastUpdate(GregorianCalendar gc){
		this.lastUpdate=gc;
	}
	
//==========================
	
	public String toString(){
		return this.id+"\n"+this.name+"\n"+this.participants+"\n"+this.admin+"\n"+this.share+"\n"+this.startDate.getTime()+"\n"+this.limit+"\n"+this.medals+"\n"+this.averagePoints+"\n"+this.minLevel+"\n"+this.pot+"\n"+this.lastUpdate.getTime();
	}
}
