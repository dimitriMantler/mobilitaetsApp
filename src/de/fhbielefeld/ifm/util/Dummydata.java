package de.fhbielefeld.ifm.util;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Random;

import android.graphics.Color;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import de.fhbielefeld.ifm.logic.*;

/**
 * This class contains methods only for testing purpose
 * 
 * @author Dimitri Mantler
 *
 */
public class Dummydata {

	/**
	 * This method returns true if password and email are "test"
	 * 
	 * @param email the email
	 * @param pass the password
	 * @return true if valid else false
	 */
	public static boolean validateLogin(String email, String pass){
		if(email.equals("test")&&pass.equals("test"))
			return true;
		else
			return false;
	}
	
	/**
	 * This method loads playerdata with dummyvalues
	 */
	public static void loadPlayerData(){
		Player player=Dummydata.createDummyPlayer();
		Singleton.getInstance().setPlayer(player);
	}
	
	/**
	 * This method creates a player-object with dummyvalues
	 * 
	 * @return a player-object containing dummyvalues
	 */
	public static Player createDummyPlayer(){
		GregorianCalendar gc=new GregorianCalendar();//future
		gc.set(GregorianCalendar.YEAR, 2016);
		gc.set(GregorianCalendar.MONTH, GregorianCalendar.FEBRUARY);
		
		GregorianCalendar gc2=new GregorianCalendar();//current date/time - 1 week
		gc2.set(GregorianCalendar.WEEK_OF_YEAR, gc2.get(GregorianCalendar.WEEK_OF_YEAR)-1);
		
		PolylineOptions path=new PolylineOptions();
	    path.color(Color.RED);
	    path.width(6);
	    path.add(new LatLng(52.20227, 8.48052)); // N 52° 20.227 E 008° 48.052 
	    path.add(new LatLng(52.20277, 8.481));
	    path.add(new LatLng(52.20237, 8.48150));
		ArrayList<LogbookEntry> logbook=new ArrayList<LogbookEntry>();
		logbook.add(new LogbookEntry(1252626,Util.getStringFromPolyline(path), "Strecke1", 
				"", 60, 45, 12,gc, Enums.Frequency.once, Enums.Transportation.bicycle, Enums.Share.no, true, gc));
		ArrayList<Long> participants=new ArrayList<Long>();
		participants.add(151313l);
		participants.add(2666262l);
		participants.add(241l);
		participants.add(373737l);
		
		ArrayList<Long> medals=new ArrayList<Long>();
		medals.add(151313l);
		medals.add(2666262l);
		medals.add(241l);
		medals.add(373737l);
		
		ArrayList<MonthRace> monthraces=new ArrayList<MonthRace>();
		monthraces.add(new MonthRace(252462286L, "Testrennen", participants, 
				"affe3", Enums.Share.friends, gc2, 4, medals, 45, 0, 15, new GregorianCalendar()));
		monthraces.add(new MonthRace(9876L, "2tes rennen", participants, "blubber", 
				Enums.Share.friends, new GregorianCalendar(), 4, new ArrayList<Long>(), 45, 0, 15, new GregorianCalendar()));
		monthraces.add(new MonthRace(6597474L, "Zukunftsrennen", participants, 
				"johannes098", Enums.Share.global, gc, 10, medals, 45, 5, 15, new GregorianCalendar()));

		
		ArrayList<Long> logs=new ArrayList<Long>();
		logs.add(252l);
		logs.add(3736l);
		ArrayList<Long> medalsf=new ArrayList<Long>();
		medalsf.add(3736l);
		medalsf.add(3736l);
		ArrayList<Friend> friends=new ArrayList<Friend>();
		
		friends.add(new Friend(123l, "Freund1", 5, 14, 20, 40, 40, logs, 
				medalsf, 5, 20.6f, 15, 51.4f));
		friends.add(new Friend(321l, "Freund2", 6, 45, 33f, 33f, 33f, new ArrayList<Long>(), 
				new ArrayList<Long>(), 5, 20.6f, 15, 51.4f));
		friends.add(new Friend(3636L, "Brain", 5, 16, 33.3f, 33.3f, 33.3f, new ArrayList<Long>(), 
				new ArrayList<Long>(), 5, 20.6f, 15, 51.4f));
		
		ArrayList<Long> sharedlogs= new ArrayList<Long>();
		sharedlogs.add(363636l);
		friends.add(new Friend(26265, "Freund1", 7, 34, 33.3f, 0, 66.6f, sharedlogs, medals, 5, 46.6f, 56, 99.9f));
		
		ArrayList<Medal> ownMedals=new ArrayList<Medal>();
		ownMedals.add(new Medal(3737, "Medaille1212", "Hier ist eine etwas längere Beschreibung " +
				"um die Größe und die Lesbarkeit der textbox zu Testen, ausserdem muss " +
				"der Text enfach so lang sein um den Bereich voll auszufüllen", "Testspieler002",  
				new GregorianCalendar(), 5, "http://www.odeki.de/hochschulen/Wappen/nrw.png"));
		ownMedals.add(new Medal(848484, "NRW-Best", "beschreibung", "Testspieler002",  
				new GregorianCalendar(), 15, "http://www.codeweavers.com/images/medals/gold_64_sup.png"));
		ownMedals.add(new Medal(474, "MedaillenName", "beschreibung", "Testspieler002",  
				new GregorianCalendar(), 50, "http://forum.machentertainment.com/data/medal/9_1374984274m.jpg"));
		ownMedals.add(new Medal(484, "MedaillenName", "beschreibung", "Testspieler002",  
				new GregorianCalendar(), 20, "http://ww1.hdnux.com/photos/24/00/53/5245636/3/gallery_thumb.jpg"));
		ownMedals.add(new Medal(848, "MedaillenName", "beschreibung", "Testspieler002",  
				new GregorianCalendar(), 35, "http://media.steampowered.com/steamcommunity/public/images/avatars/" +
						"4b/4b237ca2a0d5a158bbe87b1f8321b6ee0778ad54_medium.jpg"));
		ownMedals.add(new Medal(848, "DuckDuckGo", "Wie Google nur anders", "Testspieler002",  
				new GregorianCalendar(), 5, "https://addons.opera.com/media/extensions/65/61565/1.4.2-rev1/icons/icon_64x64.png"));
		
		ArrayList<Medal> earnedMedals=new ArrayList<Medal>();
		earnedMedals.add(new Medal(333, "Medaille1212", "Eine spezielle Medaille vom Server", 
				"Server",  new GregorianCalendar(), 15, "http://www.hunter.de/fileadmin/nl_econ_connector/" +
						"images/tags/attributes/HC_icon_made_in_germany.png"));
		earnedMedals.add(new Medal(888, "NRW-Best", "beschreibung", "xXTTTXx",  
				new GregorianCalendar(), 15, "http://wiki.openstreetmap.org/w/images/thumb/8/86/" +
						"Symbol_Eichhoernchen_Braun.svg/64px-Symbol_Eichhoernchen_Braun.svg.png"));
		earnedMedals.add(new Medal(444, "MedaillenName", "beschreibung", "JohnDoe",  
				new GregorianCalendar(), 15, "http://stadtplan.magdeburg.de/images/meetingPlaces/stern.gif"));
		earnedMedals.add(new Medal(555, "MedaillenName", "Woher die wohl kommt?", "",  
				new GregorianCalendar(), 15, "http://www.zeldadungeon.net/wiki/images/c/ca/Bow-Arrows-Sprite.png"));
		earnedMedals.add(new Medal(777, "MedaillenName", "beschreibung", "Mürmel",  
				new GregorianCalendar(), 15, "https://cdn1.iconfinder.com/data/icons/free-crystal-icons/64/Gemstone.png"));

		
		ArrayList<Float> shares= new ArrayList<Float>();
		shares.add(67f);
		shares.add(12f);
		shares.add(21f);
		shares.add(45f);
		shares.add(7f);
		shares.add(48f);
		shares.add(50f);
		shares.add(30f);
		shares.add(20f);
		
		Random ran=new Random();
		ArrayList<Integer> waysCount= new ArrayList<Integer>();
		for(int i=0;i<36;i++){
			waysCount.add(ran.nextInt(100));
		}
		
		ArrayList<Float> waysDistance= new ArrayList<Float>();
		for(int i=0;i<36;i++){
			waysDistance.add((float)ran.nextInt(1000)/10);
		}
		
		Stats stats=new Stats(shares, waysCount, waysDistance);
		
		GregorianCalendar lastUpdate=new GregorianCalendar();
		
		Player player=new Player(123L, "nickname", "test", "test", 6, 15, 17, 2156,logbook, 
				monthraces, friends, ownMedals, earnedMedals, stats, lastUpdate);
		
		return player;
	}
	
	/**
	 * This method generates a random Medal independent from the id.
	 * @param id Medalid
	 * @return a random Medal
	 */
	public static Medal getMedal(long id){
		int number=new Random().nextInt(5);
		switch(number){
		case 0:
			return new Medal(346, "NRW-Best", "beschreibung", "Server", new GregorianCalendar(), 15, 
					"http://wiki.openstreetmap.org/w/images/thumb/8/86/Symbol_+" +
					"Eichhoernchen_Braun.svg/64px-Symbol_Eichhoernchen_Braun.svg.png");
		case 1:
			return new Medal(3737, "Medaille1212", "beschreibung", "blubber", new GregorianCalendar(), 15, 
					"http://www.odeki.de/hochschulen/Wappen/nrw.png");
		case 2:
			return new Medal(848484, "MausZeiger", "beschreibung", "blubber",  new GregorianCalendar(), 15, 
					"http://de.selfhtml.org/grafik/anim_gif3.gif");
		case 3:
			return new Medal(21, "Montag", "beschreibung", "blubber",  new GregorianCalendar(), 15, 
					"https://cdn1.iconfinder.com/data/icons/free-crystal-icons/64/Gemstone.png");
		case 4:
			return new Medal(24626, "Freitag", "beschreibung", "",  new GregorianCalendar(), 15, 
					"http://stadtplan.magdeburg.de/images/meetingPlaces/stern.gif");
		default:
			return new Medal(21, "MedaillenName", "beschreibung", "blubber",  new GregorianCalendar(), 15, 
					"https://cdn1.iconfinder.com/data/icons/free-crystal-icons/64/Gemstone.png");
		}
	}
	
	/**
	 * This method generates a dummy LogbookEntry-object
	 * @param id LogbookEntryId
	 * @return genereated dummy LogbookEntry-object
	 */
	public static LogbookEntry getLog(long id){
		PolylineOptions path=new PolylineOptions();
	    path.color(Color.RED);
	    path.width(6);
	    path.add(new LatLng(52.20227, 8.48052)); // N 52° 20.227 E 008° 48.052 
	    path.add(new LatLng(52.20277, 8.481));
	    path.add(new LatLng(52.20237, 8.48150));
		
		return new LogbookEntry(1252626,Util.getStringFromPolyline(path), "Strecke1", 
				"", 660, 45, 12,new GregorianCalendar(), Enums.Frequency.once, 
				Enums.Transportation.bicycle, Enums.Share.no, true, new GregorianCalendar());
	}
	
	/**
	 * This method generates a random OtherPlayer-object independent from the id.
	 * @param id the id of the OtherPlayer-object
	 * @return a random OtherPlayer-object
	 */
	public static OtherPlayer getOtherPlayer(long id){
		int number=new Random().nextInt(5);
		switch(number){
		case 0:
			return new OtherPlayer(2624626, "Rivale0", 3, 2, 50, 50, 0);
		case 1:
			return new OtherPlayer(2624626, "Rivale1", 5, 9, 0, 0, 100.0f);
		case 2:
			return new OtherPlayer(2624626, "Rivale2", 6, 29, 33.3f,33.3f,33.3f);
		case 3:
			return new OtherPlayer(2624626, "Rivale3", 2, 21, 50, 20, 30);
		case 4:
			return new OtherPlayer(2624626, "Rivale4", 1, 32, 0, 0, 0);
		default:
			return new OtherPlayer(2624626, "Rivale5", 5, 56, 25, 25, 50);
		}
		
	}
	
	/**
	 * This method returns a set ArrayList containing dummy MonthRace-objects
	 * @return set ArrayList containing dummy MonthRace-objects
	 */
	public static ArrayList<MonthRace> getAvailableDummyRaces(){
		ArrayList<MonthRace> races=new ArrayList<MonthRace>();
		races.add(new MonthRace(252462286L, "Testrennen", new ArrayList<Long>(), 
				"affe3", Enums.Share.friends, new GregorianCalendar(), 4, 
				new ArrayList<Long>(), 45, 0, 15, new GregorianCalendar()));
		races.add(new MonthRace(252462286L, "Testrennen", new ArrayList<Long>(), 
				"affe3", Enums.Share.friends, new GregorianCalendar(), 4, 
				new ArrayList<Long>(), 45, 0, 15, new GregorianCalendar()));
		races.add(new MonthRace(252462286L, "Testrennen", new ArrayList<Long>(), 
				"affe3", Enums.Share.friends, new GregorianCalendar(), 4, 
				new ArrayList<Long>(), 45, 0, 15, new GregorianCalendar()));
		return races;
	}	
}
