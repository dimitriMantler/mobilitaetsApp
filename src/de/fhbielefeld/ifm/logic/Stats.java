package de.fhbielefeld.ifm.logic;

import java.util.ArrayList;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Root;

@Root(name = "stats")
@Default(DefaultType.FIELD)
public class Stats {

	ArrayList<Float> shares; //shareTotalGood,shareTotalMedium,shareTotalBad, shareCurrentGood,shareCurrentMedium,shareCurrentBad, shareLastGood,shareLastMedium,shareLastBad,
	ArrayList<Integer> waysCount; //total{total,good,medium,bad}, current{total,good,medium,bad}, last{total,good,medium,bad}
	ArrayList<Float> waysDistance; //total{total,good,medium,bad}, current{total,good,medium,bad}, last{total,good,medium,bad}
	
	
	public Stats(){}

	public Stats(ArrayList<Float> shares,ArrayList<Integer> waysCount,ArrayList<Float> waysDistance){
		this.shares=shares;
		this.waysCount=waysCount;
		this.waysDistance=waysDistance;
	}

//=========getter=========

	public ArrayList<Float> getShares(){
		return this.shares;
	}
	
	public ArrayList<Integer> getWaysCount(){
		return this.waysCount;
	}
	
	public ArrayList<Float> getWaysDistance(){
		return this.waysDistance;
	}
	
	
//=========setter============

	public void setShares(ArrayList<Float> shares){
		this.shares=shares;
	}
		
	public void setWaysCount(ArrayList<Integer> waysCount){
		this.waysCount=waysCount;
	}
		
	public void setWaysDistance(ArrayList<Float> waysDistance){
		this.waysDistance=waysDistance;
	}
	
//=====================
	
	public String toString(){
		String output="shares\n";
		for(int i=0;i<shares.size();i++){
			output+=shares.get(i)+"\n";
		}
		output+="waysCount\n";
		for(int i=0;i<waysCount.size();i++){
			output+=waysCount.get(i)+"\n";
		}
		output+="waysDistance\n";
		for(int i=0;i<waysDistance.size();i++){
			output+=waysDistance.get(i)+"\n";
		}
		return output;
	}
}
