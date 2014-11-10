package de.fhbielefeld.ifm.logic;

import java.util.*;
import org.simpleframework.xml.*;

@Root
@Default(DefaultType.FIELD) 
public class ObjectList {

	ArrayList<Long> ids;
	ArrayList<?> objects;
	
	public ObjectList(){}
	
	public ObjectList(ArrayList<Long> ids, ArrayList<?> objects){
		this.ids=ids;
		this.objects=objects;
	}

//getter
	public ArrayList<Long> getIds(){
		return this.ids;
	}
	
	public ArrayList<?> getObjects(){
		return this.objects;
	}
	
//setter
	public void setIds(ArrayList<Long> ids){
		this.ids=ids;
	}
	
	public void setObjects(ArrayList<?> objects){
		this.objects=objects;
	}
	
	public String toString(){
		return this.ids+"\n"+this.objects;
	}
}
