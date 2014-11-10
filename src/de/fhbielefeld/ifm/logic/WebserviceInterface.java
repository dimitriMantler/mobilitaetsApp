package de.fhbielefeld.ifm.logic;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.annotation.SuppressLint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.widget.Toast;

/**
 * This class handles the connection to the webservice
 * 
 * @author Dimitri Mantler
 *
 */
@SuppressLint("SimpleDateFormat")
public class WebserviceInterface{

	Thread t;
	String jaxrsmessage;
	String URI=PreferenceManager.getDefaultSharedPreferences(
			Singleton.getInstance().getContext()).getString("edittext_serveradress", 
			"http://192.168.0.102:8080/WebServiceREST/"); // Testserver http://192.168.0.102:8080/WebServiceREST/
	
	Long logId;
	Long tan;
	Boolean result;
	LogbookEntry log;
	Player player;
	ObjectList o;
	
	Serializer serializer = new Persister();
	StringBuilder sb =  new StringBuilder();
	ByteArrayOutputStream baos=new ByteArrayOutputStream();
	HttpClient httpclient = new DefaultHttpClient();
	Object token =new Object();
	
	public WebserviceInterface(){}
	
	//---------various-------------
	
	/**
	 * This method requests a valid sessionID from the webservice
	 * 
	 * @param email the users email
	 * @param pass the users password
	 * @return >0 = success -1=failure
	 */
	public long getAccess(String email,String pass){//GET
		final String email2=email;
		final String pass2=pass;
		t=new Thread(new Runnable() {
            public void run() {
        		HttpGet request = new HttpGet(URI+"getaccess/name/"+email2+"/pass/"+pass2);    		
        		request.addHeader("Accept", "text/plain");
        		try {
        			HttpResponse response = httpclient.execute(request);
        			HttpEntity entity = response.getEntity();
        			jaxrsmessage=readStream(entity.getContent());
        			tan = Long.parseLong(jaxrsmessage);
        		}catch (Exception e) {
					e.printStackTrace();
				}
        		finally{
	        		synchronized(token){
	        			token.notifyAll();
	        		}
        		}
            }
        });
		t.start(); 
		synchronized (token){ 
			try {
				token.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(tan!=null)
			return tan;
		else{
			showErrorDialog("getAccess");
			return -1;
		}
	}
	
	/**
	 * This method requests a player-object corresponding 
	 * to the sessionID from the webservice 
	 * 
	 * @param tan a valid sessionID or <0 to load the ID from the Playerobject
	 * @return the Playerobject or null if failure
	 */
	public Player getMyData(long tan){//GET
		System.out.println(tan);
		final long tan2;
		if(tan>-1){
			tan2=tan;
		}
		else{
			tan2=Singleton.getInstance().getPlayer().getId();
		}
		t=new Thread(new Runnable() {
            public void run() {
        		HttpGet request = new HttpGet(URI+"getmydata/tan/"+tan2);    		
        		request.addHeader("Accept", "application/xml");
        		try {
        			HttpResponse response = httpclient.execute(request);
        			HttpEntity entity = response.getEntity();
        			jaxrsmessage=readStream(entity.getContent());
        			player = serializer.read(Player.class, jaxrsmessage);
        		}catch (Exception e) {
					e.printStackTrace();
				}
        		finally{
	        		synchronized(token){
	        			token.notifyAll();
	        		}
        		}
            }
        });
		t.start(); 
		synchronized (token){ 
			try {
				token.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(player!=null){
			//conserve all LogbookEntries which are not synced
			if(Singleton.getInstance().getPlayer()!=null){
				for(int i=0;i<Singleton.getInstance().getPlayer().getLogbook().size();i++){
					if(Singleton.getInstance().getPlayer().getLogbook().get(i).getSync()==false)
					player.addLogbookEntry(Singleton.getInstance().getPlayer().getLogbook().get(i));
				}
			}
			Singleton.getInstance().setPlayer(player);
			return player;
		}
		else{
			showErrorDialog("getMyData");
			return null;
		}
	}
	
	/**
	 * This method requests the ranking between the parameters included
	 * @param from should be lower than the other param
	 * @param to should be greater than the other param
	 * @return an ArrayList containing the OtherPlayer-objects for the requested ranks
	 */
	public ArrayList<OtherPlayer> getRanking(long from, long to){//GET
		final long tan2 = Singleton.getInstance().getPlayer().getId();
		final long from2 = from;
		final long to2 = to;
		t=new Thread(new Runnable() {
            public void run() {
        		HttpGet request = new HttpGet(URI+"getranking/from/"+from2+"/to/"+to2+"/tan/"+tan2);    		
        		request.addHeader("Accept", "application/xml");
        		try {
        			HttpResponse response = httpclient.execute(request);
        			HttpEntity entity = response.getEntity();
        			jaxrsmessage=readStream(entity.getContent());
        			o = serializer.read(ObjectList.class, jaxrsmessage);
        		}catch (Exception e) {
					e.printStackTrace();
				}
        		finally{
	        		synchronized(token){
	        			token.notifyAll();
	        		}
        		}
            }
        });
		t.start(); 
		synchronized (token){ 
			try {
				token.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(o!=null){
			return (ArrayList<OtherPlayer>)o.getObjects();
		}
		else{
			showErrorDialog("getRanking");
			return new ArrayList<OtherPlayer>();
		}
	}
	
	//---------logbook-----------------
	
	/**
	 * This method requests a LogbookEntry-object from the webservice
	 * 
	 * @param logId the Id of the requested LogbookEntry-object
	 * @return the requested LogbookEntry-object or null if failure
	 */
	public LogbookEntry getLog(long logId){//GET
		final long tan2 = Singleton.getInstance().getPlayer().getId();
		final long logId2 = logId;
		t=new Thread(new Runnable() {
            public void run() {
        		HttpGet request = new HttpGet(URI+"getlog/tan/"+tan2+"/log/"+logId2);    		
        		request.addHeader("Accept", "application/xml");
        		try {
        			HttpResponse response = httpclient.execute(request);
        			HttpEntity entity = response.getEntity();
        			jaxrsmessage=readStream(entity.getContent());
        			log = serializer.read(LogbookEntry.class, jaxrsmessage);
        		}catch (Exception e) {
					e.printStackTrace();
				}
        		finally{
	        		synchronized(token){
	        			token.notifyAll();
	        		}
        		}
            }
        });
		t.start(); 
		synchronized (token){ 
			try {
				token.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(log!=null)
			return log;
		else{
			showErrorDialog("getLog");
			return null;
		}
	}
	
	/**
	 * This method requests LogbookEntry
	 * 
	 * @param ids contains all the requested logIds and the own sessionID at last
	 * @return the requested log or null if failure
	 */
	public ArrayList<LogbookEntry> getLogs(ArrayList<Long> ids){//POST
		ids.add(Singleton.getInstance().getPlayer().getId());
		final ObjectList o2=new ObjectList(ids,new ArrayList());
		t=new Thread(new Runnable() {
            public void run() {
            	baos.reset();
        		HttpPost request = new HttpPost(URI+"getlogs");    		
        		request.addHeader("Accept", "application/xml");
        		request.addHeader("Content-Type", "application/xml");
        		try {
        			serializer.write(o2, baos);
        			String s=new String(baos.toByteArray());	
                    StringEntity se = new StringEntity(s, "UTF-8");
                    se.setContentType("application/xml"); 
                    request.setEntity(se);
        			HttpResponse response = httpclient.execute(request);
        			HttpEntity entity = response.getEntity();
        			jaxrsmessage=readStream(entity.getContent());
        			o = serializer.read(ObjectList.class, jaxrsmessage);
        		}catch (Exception e) {
					e.printStackTrace();
				}
        		finally{
	        		synchronized(token){
	        			token.notifyAll();
	        		}
        		}
            }
        });
		t.start(); 
		synchronized (token){ 
			try {
				token.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(o!=null){
			return (ArrayList<LogbookEntry>)o.getObjects();
		}
		else{
			showErrorDialog("getLogs");
			return new ArrayList<LogbookEntry>();
		}
	}
	
	/**
	 * This method synchronize a single LogbookEntry-object with the webservice
	 * @param log The LogbookEntry-object to synchronize
	 * @return if the LogbookEntry-object wasn't synchronized before, the new id, 
	 * 			else the id of the LogbookEntry-object 
	 */
	public long syncLog(LogbookEntry log){//POST
		ArrayList<LogbookEntry> logs= new ArrayList<LogbookEntry>();
		logs.add(log);
		ArrayList<Long> id= new ArrayList<Long>();
		id.add(Singleton.getInstance().getPlayer().getId());
		final ObjectList o2=new ObjectList(id,logs);
		t=new Thread(new Runnable() {
            public void run() {
            	baos.reset();
        		HttpPost request = new HttpPost(URI+"synclog");    		
        		request.addHeader("Accept", "text/plain");
        		request.addHeader("Content-Type", "application/xml");
        		try {
        			serializer.write(o2, baos);
        			String s=new String(baos.toByteArray());	
                    StringEntity se = new StringEntity(s, "UTF-8");
                    se.setContentType("application/xml"); 
                    request.setEntity(se);
        			HttpResponse response = httpclient.execute(request);
        			HttpEntity entity = response.getEntity();
        			jaxrsmessage=readStream(entity.getContent());
        			logId=Long.parseLong(jaxrsmessage);
        		}catch (Exception e) {
					e.printStackTrace();
				}
        		finally{
	        		synchronized(token){
	        			token.notifyAll();
	        		}
        		}
            }
        });
		t.start(); 
		synchronized (token){ 
			try {
				token.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(logId>-1){
			return logId;
		}
		else{
			showErrorDialog("syncLog");
			return -1;
		}
	}
	
	/**
	 * This method syncs all LogbookEntry-objects which are not yet synchronized
	 * 
	 * @return true if success else false
	 */
	public boolean syncAllLogs(){//POST
		ArrayList<LogbookEntry> logs = Singleton.getInstance().getPlayer().getLogbook();
		ArrayList<Long> id= new ArrayList<Long>();
		id.add(Singleton.getInstance().getPlayer().getId());
		final ObjectList o1=new ObjectList(id,logs);
		t=new Thread(new Runnable() {
            public void run() {
            	baos.reset();
        		HttpPost request = new HttpPost(URI+"syncalllogs");    		
        		request.addHeader("Accept", "text/plain");
        		request.addHeader("Content-Type", "application/xml");
        		try {
        			serializer.write(o1, baos);
        			String s=new String(baos.toByteArray());	
                    StringEntity se = new StringEntity(s, "UTF-8");
                    se.setContentType("application/xml"); 
                    request.setEntity(se);
        			HttpResponse response = httpclient.execute(request);
        			HttpEntity entity = response.getEntity();
        			jaxrsmessage=readStream(entity.getContent());
        			result=Boolean.parseBoolean(jaxrsmessage);
        		}catch (Exception e) {
					e.printStackTrace();
				}
        		finally{
	        		synchronized(token){
	        			token.notifyAll();
	        		}
        		}
            }
        });
		t.start(); 
		synchronized (token){ 
			try {
				token.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(result!=null){
			if(result){
				//remove all LogbookEntries which are not synced to download the again synced via getMyData
				for(int i=Singleton.getInstance().getPlayer().getLogbook().size()-1;i>=0;i--){
					if(Singleton.getInstance().getPlayer().getLogbook().get(i).getSync()==false)
						Singleton.getInstance().getPlayer().getLogbook().remove(i);
				}
			}
			return result;
		}
		else{
			showErrorDialog("syncAllLogs");
			return false;
		}
	}
	
	/**
	 * This method sends a LogbookEntry anonymously
	 * 
	 * @param log the LogbookEntry which will be send
	 * @return true if success else false
	 */
	public boolean sendLogAnonymous(LogbookEntry log){//POST
		ArrayList<LogbookEntry> logs= new ArrayList<LogbookEntry>();
		logs.add(log);
		ArrayList<Long> id= new ArrayList<Long>();
		id.add(Singleton.getInstance().getPlayer().getId());
		final ObjectList o3=new ObjectList(id,logs);
		t=new Thread(new Runnable() {
            public void run() {
            	baos.reset();
        		HttpPost request = new HttpPost(URI+"sendloganonymous");    		
        		request.addHeader("Accept", "text/plain");
        		request.addHeader("Content-Type", "application/xml");
        		try {
        			serializer.write(o3, baos);
        			String s=new String(baos.toByteArray());	
                    StringEntity se = new StringEntity(s, "UTF-8");
                    se.setContentType("application/xml"); 
                    request.setEntity(se);
        			HttpResponse response = httpclient.execute(request);
        			HttpEntity entity = response.getEntity();
        			jaxrsmessage=readStream(entity.getContent());
        			result = Boolean.parseBoolean(jaxrsmessage);
        		}catch (Exception e) {
					e.printStackTrace();
				}
        		finally{
	        		synchronized(token){
	        			token.notifyAll();
	        		}
        		}
            }
        });
		t.start(); 
		synchronized (token){ 
			try {
				token.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(result!=null){
			return result;
		}
		else{
			showErrorDialog("sendLogAnonymous");
			return false;
		}
	}
	
	//---------monthrace-----------------
	
	/**
	 * This method requests the available races for the user
	 * 
	 * @return an ArrayList containing the available MonthRace-objects
	 */
	public ArrayList<MonthRace> getAvailableRaces(){//GET
		final long tan2 = Singleton.getInstance().getPlayer().getId();
		t=new Thread(new Runnable() {
            public void run() {
        		HttpGet request = new HttpGet(URI+"getavailableraces/tan/"+tan2);    		
        		request.addHeader("Accept", "application/xml");
        		try {
        			System.out.println("test1");
        			HttpResponse response = httpclient.execute(request);
        			System.out.println("test2");
        			HttpEntity entity = response.getEntity();
        			System.out.println("test3");
        			jaxrsmessage=readStream(entity.getContent());
        			System.out.println("test4");
        			o = serializer.read(ObjectList.class, jaxrsmessage);
        			System.out.println("test5");
        		}catch (Exception e) {
					e.printStackTrace();
					System.out.println("test6");
				}
        		finally{
	        		synchronized(token){
	        			token.notifyAll();
	        		}
        		}
            }
        });
		t.start(); 
		synchronized (token){ 
			try {
				token.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(o!=null){
			return (ArrayList<MonthRace>)o.getObjects();
		}
		else{
			showErrorDialog("getAvailableRaces");
			return new ArrayList<MonthRace>();
		}
	}

	/**
	 * This method sends a request to the webservice to sign up the player to
	 * a  MonthRace with the given id
	 * @param raceId the id of the MonthRace to sign up to
	 * @return true if success, else false
	 */
	public boolean signUpRace(long raceId){//GET
		final long tan2 = Singleton.getInstance().getPlayer().getId();
		final long raceId2=raceId;
		t=new Thread(new Runnable() {
            public void run() {
        		HttpGet request = new HttpGet(URI+"signuprace/tan/"+tan2+"/raceid/"+raceId2);    		
        		request.addHeader("Accept", "text/plain");
        		try {
        			HttpResponse response = httpclient.execute(request);
        			HttpEntity entity = response.getEntity();
        			jaxrsmessage=readStream(entity.getContent());
        			result = Boolean.parseBoolean(jaxrsmessage);
        		}catch (Exception e) {
					e.printStackTrace();
				}
        		finally{
	        		synchronized(token){
	        			token.notifyAll();
	        		}
        		}
            }
        });
		t.start(); 
		synchronized (token){ 
			try {
				token.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(result!=null)
			return result;
		else{
			showErrorDialog("signUpRace");
			return false;
		}
	}
	
	/**
	 * This method sends a request to the webservice to remove the player from
	 * a MonthRace with the given id
	 * @param raceId the id of the MonthRace to leave from
	 * @return true if success, else false
	 */
	public boolean leaveRace(long raceId){//GET
		final long tan2 = Singleton.getInstance().getPlayer().getId();
		final long raceId2=raceId;
		t=new Thread(new Runnable() {
            public void run() {
        		HttpGet request = new HttpGet(URI+"leaverace/tan/"+tan2+"/raceid/"+raceId2);    		
        		request.addHeader("Accept", "text/plain");
        		try {
        			HttpResponse response = httpclient.execute(request);
        			HttpEntity entity = response.getEntity();
        			jaxrsmessage=readStream(entity.getContent());
        			result = Boolean.parseBoolean(jaxrsmessage);
        		}catch (Exception e) {
					e.printStackTrace();
				}
        		finally{
	        		synchronized(token){
	        			token.notifyAll();
	        		}
        		}
            }
        });
		t.start(); 
		synchronized (token){ 
			try {
				token.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(result!=null)
			return result;
		else{
			showErrorDialog("leaveRace");
			return false;
		}
	}

	/**
	 * This method requests an ArrayList containing OtherPlayer,
	 * which participate in a Monthrace
	 * 
	 * @param ids the participants for a Monthrace
	 * @return an ArrayList containing the players or null if failure
	 */
	public ArrayList<OtherPlayer> getPlayerList(ArrayList<Long> ids){//POST
		ids.add(Singleton.getInstance().getPlayer().getId());
		final ObjectList o2=new ObjectList(ids,new ArrayList());
		t=new Thread(new Runnable() {
            public void run() {
            	baos.reset();
        		HttpPost request = new HttpPost(URI+"getplayerlist");    		
        		request.addHeader("Accept", "application/xml");
        		request.addHeader("Content-Type", "application/xml");
        		try {
        			serializer.write(o2, baos);
        			String s=new String(baos.toByteArray());	
                    StringEntity se = new StringEntity(s, "UTF-8");
                    se.setContentType("application/xml"); 
                    request.setEntity(se);
        			HttpResponse response = httpclient.execute(request);
        			HttpEntity entity = response.getEntity();
        			jaxrsmessage=readStream(entity.getContent());
        			o = serializer.read(ObjectList.class, jaxrsmessage);
        		}catch (Exception e) {
					e.printStackTrace();
				}
        		finally{
	        		synchronized(token){
	        			token.notifyAll();
	        		}
        		}
            }
        });
		t.start(); 
		synchronized (token){ 
			try {
				token.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(o!=null){
			return (ArrayList<OtherPlayer>)o.getObjects();
		}
		else{
			showErrorDialog("getPlayerList");
			return new ArrayList<OtherPlayer>();
		}
	}
	
	/**
	 * This method adds a selected medal to a specific monthrace
	 * 
	 * @param raceId the MonthRace where the medal will be donated
	 * @param medalId the donated medal
	 * @return true if success else false
	 */
	public boolean addMedalToRace(long raceId,long medalId){//GET
		final long tan2 = Singleton.getInstance().getPlayer().getId();
		final long raceId2=raceId;
		final long medalId2=medalId;
		t=new Thread(new Runnable() {
            public void run() {
        		HttpGet request = new HttpGet(URI+"addmedaltorace/tan/"+tan2+"/raceid/"+raceId2+"/medalid/"+medalId2);    
        		request.addHeader("Accept", "text/plain");
        		try {
        			HttpResponse response = httpclient.execute(request);
        			HttpEntity entity = response.getEntity();
        			jaxrsmessage=readStream(entity.getContent());
        			result = Boolean.parseBoolean(jaxrsmessage);
        		}catch (Exception e) {
					e.printStackTrace();
				}
        		finally{
	        		synchronized(token){
	        			token.notifyAll();
	        		}
        		}
            }
        });
		t.start(); 
		synchronized (token){ 
			try {
				token.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(result!=null)
			return result;
		else{
			showErrorDialog("addMedalToRace");
			return false;
		}
	}
	
	//---------friends-----------------
	
	/**
	 * This method requests the friendrequests for the player
	 * @return an ArrayList with OtherPlayer-objects 
	 */
	public ArrayList<OtherPlayer> getFriendRequests(){//GET
		final long tan2 = Singleton.getInstance().getPlayer().getId();
		t=new Thread(new Runnable() {
            public void run() {
        		HttpGet request = new HttpGet(URI+"getfriendrequests/tan/"+tan2);    		
        		request.addHeader("Accept", "application/xml");
        		try {
        			HttpResponse response = httpclient.execute(request);
        			HttpEntity entity = response.getEntity();
        			jaxrsmessage=readStream(entity.getContent());
        			o = serializer.read(ObjectList.class, jaxrsmessage);
        		}catch (Exception e) {
					e.printStackTrace();
				}
        		finally{
	        		synchronized(token){
	        			token.notifyAll();
	        		}
        		}
            }
        });
		t.start(); 
		synchronized (token){ 
			try {
				token.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(o!=null){
			return (ArrayList<OtherPlayer>)o.getObjects();
		}
		else{
			showErrorDialog("getFriendRequests");
			return new ArrayList<OtherPlayer>();
		}
	}

	/**
	 * This method removes a friend from the friendlist.
	 * The own id will also be removed from the other players friendlist
	 * 
	 * @param friendId the friend which should be removed
	 * @return true if success, else false
	 */
	public boolean removeFriend(long friendId){//GET
		final long tan2 = Singleton.getInstance().getPlayer().getId();
		final long friendId2=friendId;
		t=new Thread(new Runnable() {
            public void run() {
        		HttpGet request = new HttpGet(URI+"removefriend/tan/"+tan2+"/friendid/"+friendId2);    		
        		request.addHeader("Accept", "text/plain");
        		try {
        			HttpResponse response = httpclient.execute(request);
        			HttpEntity entity = response.getEntity();
        			jaxrsmessage=readStream(entity.getContent());
        			result = Boolean.parseBoolean(jaxrsmessage);
        		}catch (Exception e) {
					e.printStackTrace();
				}
        		finally{
	        		synchronized(token){
	        			token.notifyAll();
	        		}
        		}
            }
        });
		t.start(); 
		synchronized (token){ 
			try {
				token.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(result!=null)
			return result;
		else{
			showErrorDialog("removeFriend");
			return false;
		}
	}
	
	/**
	 * This method send an answer for a friendrequest
	 * 
	 * @param friendId the player who send the request
	 * @param answer true=accept false=deny
	 * @return true if success else false
	 */
	public boolean answerFriendRequest(long friendId, boolean answer){//GET
		final long tan2 = Singleton.getInstance().getPlayer().getId();
		final long friendId2=friendId;
		final boolean answer2=answer;
		t=new Thread(new Runnable() {
            public void run() {
        		HttpGet request = new HttpGet(URI+"answerfriendrequest/tan/"+tan2+"/friendid/"+friendId2+"/answer/"+answer2);    		
        		request.addHeader("Accept", "text/plain");
        		try {
        			HttpResponse response = httpclient.execute(request);
        			HttpEntity entity = response.getEntity();
        			jaxrsmessage=readStream(entity.getContent());
        			result = Boolean.parseBoolean(jaxrsmessage);
        		}catch (Exception e) {
					e.printStackTrace();
				}
        		finally{
	        		synchronized(token){
	        			token.notifyAll();
	        		}
        		}
            }
        });
		t.start(); 
		synchronized (token){ 
			try {
				token.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(result!=null)
			return result;
		else{
			showErrorDialog("answerFriendRequest");
			return false;
		}
	}
	
	/**
	 * This method sends a friendrequest to a certain player 
	 * @param friendId the id of the targeted player
	 * @return true if success, else false
	 */
	public boolean sendFriendRequest(long friendId){//GET
		final long tan2 = Singleton.getInstance().getPlayer().getId();
		final long friendId2=friendId;
		t=new Thread(new Runnable() {
            public void run() {
        		HttpGet request = new HttpGet(URI+"sendfriendrequest/tan/"+tan2+"/friendid/"+friendId2);    		
        		request.addHeader("Accept", "text/plain");
        		try {
        			HttpResponse response = httpclient.execute(request);
        			HttpEntity entity = response.getEntity();
        			jaxrsmessage=readStream(entity.getContent());
        			result = Boolean.parseBoolean(jaxrsmessage);
        		}catch (Exception e) {
					e.printStackTrace();
				}
        		finally{
	        		synchronized(token){
	        			token.notifyAll();
	        		}
        		}
            }
        });
		t.start(); 
		synchronized (token){ 
			try {
				token.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(result!=null)
			return result;
		else{
			showErrorDialog("sendFriendRequest");
			return false;
		}
	}
	
	/**
	 * This method requests players which match the searchstring with their names
	 * 
	 * @param searchString the search string
	 * @return ArrayList with results
	 */
	public ArrayList<OtherPlayer> searchFriend(String searchString){//GET
		final long tan2 = Singleton.getInstance().getPlayer().getId();
		final String searchString2 = searchString;
		t=new Thread(new Runnable() {
            public void run() {
        		HttpGet request = new HttpGet(URI+"searchfriend/tan/"+tan2+"/searchstring/"+searchString2);    		
        		request.addHeader("Accept", "application/xml");
        		try {
        			HttpResponse response = httpclient.execute(request);
        			HttpEntity entity = response.getEntity();
        			jaxrsmessage=readStream(entity.getContent());
        			o = serializer.read(ObjectList.class, jaxrsmessage);
        		}catch (Exception e) {
					e.printStackTrace();
				}
        		finally{
	        		synchronized(token){
	        			token.notifyAll();
	        		}
        		}
            }
        });
		t.start(); 
		synchronized (token){ 
			try {
				token.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(o!=null){
			return (ArrayList<OtherPlayer>)o.getObjects();
		}
		else{
			showErrorDialog("searchFriend");
			return new ArrayList<OtherPlayer>();
		}
	}
	
	//---------medals-----------------
	
	/**
	 * This method requests an ArrayList containing the medals with the send ids
	 * @param ids ids of the requested Medal-objects 
	 * @return an ArrayList containing the requested medals
	 */
	public ArrayList<Medal> getMedals(ArrayList<Long> ids){//POST
		ids.add(Singleton.getInstance().getPlayer().getId());
		final ObjectList o2=new ObjectList(ids,new ArrayList());
		t=new Thread(new Runnable() {
            public void run() {
            	baos.reset();
        		HttpPost request = new HttpPost(URI+"getmedals");    		
        		request.addHeader("Accept", "application/xml");
        		request.addHeader("Content-Type", "application/xml");
        		try {
        			serializer.write(o2, baos);
        			String s=new String(baos.toByteArray());	
                    StringEntity se = new StringEntity(s, "UTF-8");
                    se.setContentType("application/xml"); 
                    request.setEntity(se);
        			HttpResponse response = httpclient.execute(request);
        			HttpEntity entity = response.getEntity();
        			jaxrsmessage=readStream(entity.getContent());
        			o = serializer.read(ObjectList.class, jaxrsmessage);
        		}catch (Exception e) {
					e.printStackTrace();
				}
        		finally{
	        		synchronized(token){
	        			token.notifyAll();
	        		}
        		}
            }
        });
		t.start(); 
		synchronized (token){ 
			try {
				token.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(o!=null){
			return (ArrayList<Medal>)o.getObjects();
		}
		else{
			showErrorDialog("getMedals");
			return new ArrayList<Medal>();
		}
	}
	
	/**
	 * This method report a medal for a given reason
	 * 
	 * @param medalId the reported medal
	 * @param reason 0=the medal contains error 1=spam 2=inappropriate content
	 * @return true if success else false
	 */
	public boolean reportMedal(long medalId, int reason){//GET
		final long tan2 = Singleton.getInstance().getPlayer().getId();
		final long medalId2=medalId;
		final int reason2=reason;
		t=new Thread(new Runnable() {
            public void run() {
        		HttpGet request = new HttpGet(URI+"reportmedal/tan/"+tan2+"/medalid/"+medalId2+"/reason/"+reason2);    		
        		request.addHeader("Accept", "text/plain");
        		try {
        			HttpResponse response = httpclient.execute(request);
        			HttpEntity entity = response.getEntity();
        			jaxrsmessage=readStream(entity.getContent());
        			result = Boolean.parseBoolean(jaxrsmessage);
        		}catch (Exception e) {
					e.printStackTrace();
				}
        		finally{
	        		synchronized(token){
	        			token.notifyAll();
	        		}
        		}
            }
        });
		t.start(); 
		synchronized (token){ 
			try {
				token.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(result!=null)
			return result;
		else{
			showErrorDialog("reportMedal");
			return false;
		}
	}

//===========================================
	
	/**
	 * This method assemble the read data from the InputStream
	 * into a usable String
	 * 
	 * @param i the used inputstream
	 * @return the server-response
	 */
	private String readStream(InputStream i){
		StringBuilder sb =  new StringBuilder();
		
		InputStream instream = i;
		BufferedReader r = new BufferedReader(new InputStreamReader(instream)); 	
		try {
			for (String line = r.readLine(); line != null; line = r.readLine()) {
			     sb.append(line);	 
			}
			instream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		jaxrsmessage = sb.toString();   
		
		return jaxrsmessage;
	}
	
	/**
	 * This method displays an AlertDialog with an errormessage
	 * and a single button for confirmation
	 * 
	 * @param method the name of the method which created the error
	 */
	private void showErrorDialog(String method){
		Toast.makeText(Singleton.getInstance().getContext(), "Ein Fehler ist aufgetreten in "+method, Toast.LENGTH_SHORT).show();
	}
}
