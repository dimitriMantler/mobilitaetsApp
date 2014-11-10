package de.fhbielefeld.ifm.logic;

import java.util.ArrayList;

import org.simpleframework.xml.*;

@Root
@Default(DefaultType.FIELD) 
public class Friend extends OtherPlayer{

	ArrayList<Long> sharedLogs;
	ArrayList<Long> medals;
	int waysCountCurrent;
	float waysDistanceCurrent;
	int waysCountTotal;
	float waysDistanceTotal;
	
	public Friend(){}
	
	public Friend(long id, String name, int level, int points, float shareGood, float shareMedium, float shareBad, ArrayList<Long> sharedLogs,ArrayList<Long> medals, int waysCountCurrent, float waysDistanceCurrent, int waysCountTotal, float waysDistanceTotal){
		super(id, name, level, points, shareGood, shareMedium, shareBad);
		this.sharedLogs=sharedLogs;
		this.medals=medals;
		this.waysCountCurrent=waysCountCurrent;
		this.waysDistanceCurrent=waysDistanceCurrent;
		this.waysCountTotal=waysCountTotal;
		this.waysDistanceTotal=waysDistanceTotal;
	}

//=========getter================	
	
	public ArrayList<Long> getSharedLogs(){
		return this.sharedLogs;
	}
	
	public ArrayList<Long> getMedals(){
		return this.medals;
	}
	
	public int getWaysCountCurrent(){
		return this.waysCountCurrent;
	}
	
	public float getWaysDistanceCurrent(){
		return this.waysDistanceCurrent;
	}
	
	public int getWaysCountTotal(){
		return this.waysCountTotal;
	}
	
	public float getWaysDistanceTotal(){
		return this.waysDistanceTotal;
	}
	
//=========setter================
	
	public void setSharedLogs(ArrayList<Long> sharedLogs){
		this.sharedLogs=sharedLogs;
	}
	
	public void setMedals(ArrayList<Long> medals){
		this.medals=medals;
	}
	
	public void setWaysCountCurrent(int waysCountCurrent){
		this.waysCountCurrent=waysCountCurrent;
	}
	
	public void setWaysDistanceCurrent(float waysDistanceCurrent){
		this.waysDistanceCurrent=waysDistanceCurrent;
	}
	
	public void setWaysCountTotal(int waysCountTotal){
		this.waysCountTotal=waysCountTotal;
	}
	
	public void setWaysDistanceTotal(float waysDistanceTotal){
		this.waysDistanceTotal=waysDistanceTotal;
	}
	
	public String toString(){
		return super.toString()+this.sharedLogs+"\n"+this.medals+"\n"+this.waysCountCurrent+"\n"+this.waysDistanceCurrent+"\n"+this.waysCountTotal+"\n"+this.waysDistanceTotal;
	}
	
}
