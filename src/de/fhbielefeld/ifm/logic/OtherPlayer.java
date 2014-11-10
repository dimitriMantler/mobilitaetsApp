package de.fhbielefeld.ifm.logic;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Root;

@Root(name = "otherPlayer")
@Default(DefaultType.FIELD)
public class OtherPlayer {

	long id;
	String name;
	int level;
	int points;
	float shareGood;
	float shareMedium;
	float shareBad;
	
	public OtherPlayer(){}
	
	public OtherPlayer(long id, String name, int level, int points, float shareGood, float shareMedium, float shareBad){
		this.id=id;
		this.name=name;
		this.level=level;
		this.points=points;
		this.shareGood=shareGood;
		this.shareMedium=shareMedium;
		this.shareBad=shareBad;
	}
	
//=======getter========
	
	public long getId(){
		return this.id;
	}
	
	public String getName(){
		return  this.name;
	}
	
	public int getLevel(){
		return this.level;
	}
	
	public int getPoints(){
		return this.points;
	}
	
	public float getShareGood(){
		return this.shareGood;
	}
	
	public float getShareMedium(){
		return this.shareMedium;
	}
	
	public float getShareBad(){
		return this.shareBad;
	}
	
//=======setter========
	
	public void setId(long id){
		this.id=id;
	}
	
	public void setName(String name){
		this.name=name;
	}
	
	public void setLevel(int level){
		this.level=level;
	}
	
	public void setPoints(int points){
		this.points=points;
	}
	
	public void setShareGood(float shareGood){
		this.shareGood=shareGood;
	}
	
	public void setShareMedium(float shareMedium){
		this.shareMedium=shareMedium;
	}
	
	public void setShareBad(float shareBad){
		this.shareBad=shareBad;
	}

//===============
	public String toString(){
		return this.id+"\n"+this.name+"\n"+this.level+"\n"+this.points+"\n"+this.shareGood+"\n"+this.shareMedium+"\n"+this.shareBad;
	}
}
