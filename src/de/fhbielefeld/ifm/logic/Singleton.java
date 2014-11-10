package de.fhbielefeld.ifm.logic;
import android.content.Context;

/**
 * This class holds the Playerdata and the Applicationcontext
 * during runtime. 
 * 
 * @author Dimitri Mantler
 *
 */
public class Singleton {
	
    private Player player = null;
    private Context context = null;
    private boolean offline=false;

    private static final Singleton instance = new Singleton();

    // Private constructor prevents instantiation from other classes
    private Singleton() { }

    public static Singleton getInstance() {
        return instance;
    }
    
//===========getter=================
    public Player getPlayer(){
    	return this.player;
    }
    
    public Context getContext(){
    	return this.context;
    }
    
    public boolean getOffline(){
    	return this.offline;
    }
//===========setter=================
    public void setOffline(boolean b){
    	this.offline=b;
    }
    
    // once during init
    public void setContext(Context c){
    	this.context=c;
    }
    
    public void setPlayer(Player player){
    	this.player=player;
    }
	
}
