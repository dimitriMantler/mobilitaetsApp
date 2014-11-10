package de.fhbielefeld.ifm.logic;

import java.util.*;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Root;

@Root
@Default(DefaultType.FIELD)
public class Medal {

	long id;
	String name;
	String description;
	String creator; //creators name
	GregorianCalendar creationDate;
	int prize;
	String imageurl;
	
	public Medal(){}
	
	public Medal(long id, String name, String descripion, String creator, GregorianCalendar creationDate, int prize, String imageurl){
		this.id=id;
		this.name=name;
		this.description=descripion;
		this.creator=creator;
		this.creationDate=creationDate;
		this.prize=prize;
		this.imageurl=imageurl;
	}
	
//=============getter=============
	public long getId(){
		return this.id;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public String getCreator(){
		return this.creator;
	}
	
	public GregorianCalendar getCreationDate(){
		return this.creationDate;
	}
	
	public int getPrize(){
		return this.prize;
	}
	
	public String getImageurl(){
		return this.imageurl;
	}
	
//=============setter=============
	
	public void setId(long id){
		this.id=id;
	}
	
	public void setName(String name){
		this.name=name;
	}
	
	public void setDescription(String description){
		this.description=description;
	}
	
	public void setCreator(String creator){
		this.creator=creator;
	}
	
	public void setCreationDate(GregorianCalendar gc){
		this.creationDate=gc;
	}
	
	public void setPrize(int prize){
		this.prize=prize;
	}
	
	public void setImageurl(String imageurl){
		this.imageurl=imageurl;
	}
	
//==========================
	
	public String toString(){
		return this.id+"\n"+this.name+"\n"+this.description+"\n"+this.creator+"\n"+this.creationDate.getTime()+"\n"+this.prize+"\n"+this.imageurl;
	}
	
}
