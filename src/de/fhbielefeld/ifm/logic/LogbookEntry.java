package de.fhbielefeld.ifm.logic;

import java.util.*;
import org.simpleframework.xml.*;
import de.fhbielefeld.ifm.logic.Enums.*;

@Root
@Default(DefaultType.FIELD) 
public class LogbookEntry {


	long id;
	String path; 
	String name;
	String description;
	long duracy; //seconds
	float averageSpeed;
	float distance;
	GregorianCalendar date;
	Enums.Frequency frequency;
	Enums.Transportation transport;
	Enums.Share share;
	boolean synced;
	GregorianCalendar lastUpdate;
	
	public LogbookEntry(){
		
	}
	
	public LogbookEntry(long id, String path, String name, String description, long duracy, float averageSpeed, float distance, GregorianCalendar date, Enums.Frequency frequency, Enums.Transportation transport, Enums.Share share, boolean synced, GregorianCalendar lastUpdate){
		this.id=id;
		this.path=path;
		this.name=name;
		this.description=description;
		this.duracy=duracy;
		this.averageSpeed=averageSpeed;
		this.distance=distance;
		this.date=date;
		this.frequency=frequency;
		this.transport=transport;
		this.share=share;
		this.synced=synced;
		this.lastUpdate=lastUpdate;
	}
	
//============getter============

	public long getId(){
		return this.id;
	}
	
	public String getPath(){//as String
		return this.path;
	}
	
	public String getName(){
		return this.name;
	}
	

	public String getDescription(){
		return this.description;
	}
	

	public long getDuracy(){
		return this.duracy;
	}


	public float getAverageSpeed(){
		return this.averageSpeed;
	}
	

	public float getDistance(){
		return this.distance;
	}
	

	public GregorianCalendar getDate(){//Date & Time
		return this.date;
	}
	

	public Frequency getFrequency(){
		return this.frequency;
	}
	

	public Transportation getTransport(){
		return this.transport;
	}

	public Share getShare(){
		return this.share;
	}
	

	public boolean getSync(){
		return this.synced;
	}
	

	public Calendar getLastUpdate(){
		return this.lastUpdate;
	}

//============setter============
	public void setId(long id){
		this.id=id;
	}
	
	public void setName(String name){
		this.name=name;
	}
	
	public void setPath(String gm){
		this.path=gm;
	}
	
	public void setDescription(String d){
		this.description=d;
	}
	
	public void setDuracy(long c){
		this.duracy=c;
	}
	
	public void setAverageSpeed(float f){
		this.averageSpeed=f;
	}
	
	public void setDistance(float f){
		this.distance=f;
	}
	
	public void setDate(GregorianCalendar c){//Date & Time
		this.date=c;
	}
	
	public void setFrequency(Enums.Frequency f){
		this.frequency=f;
	}
	
	public void setTransport(Enums.Transportation t){
		this.transport=t;
	}
	
	public void setShare(Enums.Share s){
		this.share=s;
	}
	
	public void setSync(boolean b){
		this.synced=b;
	}
	
	public void setLastUpdate(GregorianCalendar c){
		this.lastUpdate=c;
	}

//============setter============
	
	public String toString(){
		return this.id+"\n"+this.path+"\n"+this.name+"\n"+this.description+"\n"+this.duracy+"\n"+this.averageSpeed+"\n"+this.distance+"\n"+this.date.getTime()+"\n"+this.frequency+"\n"+this.transport+"\n"+this.share+"\n"+this.synced+"\n"+this.lastUpdate.getTime();
	}
	
//	public String toString(){
//		return this.id+"\n"+this.name+"\n"+this.description+"\n"+this.duracy+"\n"+this.averageSpeed+"\n"+this.distance+"\n"+this.date+"\n"+this.frequency+"\n"+this.transport+"\n"+this.share+"\n"+this.synced+"\n"+this.lastUpdate;
//	}
}
