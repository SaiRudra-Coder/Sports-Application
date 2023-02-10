package horse_Api;
import engine.CommonForAll;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Race_cards 
{
	public Logger log;
	public Race_cards()
	{
		this.log=Logger.getLogger(Race_cards.class.getName());
	}
	CommonForAll c = new CommonForAll();
	public String get_RaceCards(HashMap hout, String requestMethod, String vendor) throws IOException, InterruptedException, ParseException
	{
		String url=(String) ((Map)c.getConfig("Horse")).get("RaceCards_url");
		this.log.info("Url = :"+url);
		
		String val =c.apiCall(requestMethod, url, vendor);
		ArrayList al = new ArrayList();
		
		LinkedHashMap lp = new LinkedHashMap();
		int count =1;		
		JSONArray js= (JSONArray)new JSONParser().parse(val);		
		for(Object obj: js)
		{
			JSONObject details=(JSONObject)obj;
			lp.put(String.valueOf(count), details.get("id_race")+" - "+details.get("course")+" - "+details.get("title")+" - "+details.get("distance")+" - "+details.get("class")+" -"+details.get("prize"));
			ArrayList alin= new ArrayList();
			alin.add(details.get("id_race"));
			alin.add(details.get("course")); alin.add(details.get("title"));  alin.add(details.get("distance"));  alin.add(details.get("class"));  alin.add(details.get("prize"));  
			al.add(alin);			
			count++;		
		}
		ArrayList headings = (ArrayList)((Map)((Map)c.getConfig("Horse")).get("Racecards")).get("getRacecards");
		HashMap hm = new HashMap();
		hm.put("headings", headings);
		hm.put("data", al);
		hout.put("_RaceCards", hm);		
		
		this.log.info("S.NO || RACE ID || COURSE || TITLE || DISTANCE || CLASS || PRIZE");
		for(Object out:lp.keySet())
		{
			this.log.info(out+"."+lp.get(out));
		}
		String choi=c.exceptions(lp,"select Race id based on s.no : ");
		String[] race_id=lp.get(choi).toString().split(" - ");
		this.log.info("Your selected horse-id is : "+race_id[0]);
		return race_id[0];		
	}
	
	public String get_RaceDetails_info(HashMap hout, String requestMeethod, String race_id, String vendor) throws IOException, InterruptedException, ParseException
	{
		String url =((String)((Map)c.getConfig("Horse")).get("race_url"))+race_id;
		this.log.info("URL = : "+url);		
		String val= c.apiCall(requestMeethod, url, vendor);
		ArrayList al = new ArrayList();
			
		JSONObject js=(JSONObject)new JSONParser().parse(val);
		LinkedHashMap lin =new LinkedHashMap();
		int count=1;
		
		JSONArray ja =(JSONArray)js.get("horses");
		for(Object h :ja)
		{
			JSONObject hors =(JSONObject)h;
			lin.put(String.valueOf(count), hors.get("id_horse")+" - "+hors.get("horse")+" - "+hors.get("owner")+" - "+hors.get("jockey")+" - "+hors.get("trainer")+" - "+hors.get("age")+" - "+hors.get("position"));
			ArrayList alin = new ArrayList();
			alin.add(hors.get("id_horse")); alin.add(hors.get("horse"));  alin.add(hors.get("owner"));   alin.add(hors.get("jockey"));   alin.add(hors.get("trainer"));   alin.add(hors.get("age"));  alin.add(hors.get("position"));
			al.add(alin);
			count++;
		}
		ArrayList headings=(ArrayList)((Map)((Map)c.getConfig("Horse")).get("Racecards")).get("getRacedetailsInfo");
		HashMap hm = new HashMap();
		hm.put("headings", headings);
		hm.put("data", al);
		hout.put(race_id+"_RaceDetails", hm);
		
		this.log.info("S.NO||HORSE ID||HORSE||OWNER||JOCKEY||TRAINER||AGE||POSITION");
		for(Object ho:lin.keySet())
		{
			this.log.info(ho+"."+lin.get(ho));
		}
		String cho=c.exceptions(lin, "select horse id based on s.no : ");
		String[] horse_id=lin.get(cho).toString().split(" - ");
		this.log.info("Your selected horse-id is : "+horse_id[0]);
		return horse_id[0];		
	}
	
	public void getHorseStats(HashMap hout, String requestMethod, String horse_id, String vendor) throws IOException, InterruptedException, ParseException
	{
		String url= ((String)((Map)c.getConfig("Horse")).get("horseStats_url"))+horse_id;
		this.log.info("URL : "+url);
		String val=c.apiCall(requestMethod, url, vendor);
		ArrayList al = new ArrayList();
		JSONObject js=(JSONObject)new JSONParser().parse(val);
		int count=1;
		this.log.info("S.NO||RACE||COURCE||TRAINER||JOCKEY||WEIGHT||DISTANCE||CLASS||POSITION||STARTING PRICE||PRIZE");
		JSONArray ja =(JSONArray)js.get("results");
		for(Object k : ja)
		{
			JSONObject re =(JSONObject)k;
			this.log.info(count+"."+re.get("race")+" - "+re.get("course")+" - "+re.get("trainer")+" - "+re.get("jockey")+" - "+re.get("weight")+" - "+re.get("distance")+" - "+re.get("class")+" - "+re.get("position")+" - "+re.get("starting_price")+" - "+re.get("prize"));
			ArrayList alin = new ArrayList();
			alin.add(re.get("race"));  alin.add(re.get("course"));  alin.add(re.get("trainer"));  alin.add(re.get("jockey"));  alin.add(re.get("weight"));  alin.add(re.get("distance"));  alin.add(re.get("class"));  alin.add(re.get("position"));
			alin.add(re.get("starting_price"));
			alin.add(re.get("prize"));
			al.add(alin);
			count++;
		}	
		ArrayList headings=(ArrayList)((Map)((Map)c.getConfig("Horse")).get("Racecards")).get("getHorseStats");
		HashMap hm = new HashMap();
		hm.put("headings", headings);
		hm.put("data", al);
		hout.put(horse_id+"_horseStats", hm);		
		
	}
}
