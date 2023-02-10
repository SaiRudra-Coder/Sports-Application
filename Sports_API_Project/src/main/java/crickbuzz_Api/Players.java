package crickbuzz_Api;
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

public class Players {
	CommonForAll c;
	public Logger log;
	public Players(CommonForAll cfa)
	{
		this.log=Logger.getLogger(Players.class.getName());
		this.c=cfa;
	}
	
	public String palyers_list(HashMap hout, String requestMethod, String vendor) throws IOException, InterruptedException, ParseException
	{
		String url=(String) ((Map)c.getConfig("Crickbuzz")).get("players_url");
		System.out.println("URL =: "+url);
		this.log.info("URL =: "+url);
		
		String val=c.apiCall(requestMethod, url, vendor);
		String player_id=null;
		try 
		{
			JSONObject js=(JSONObject)new JSONParser().parse(val);
			int count=1;			
			ArrayList al = new ArrayList();
			
			LinkedHashMap mp =new LinkedHashMap();
			this.log.info("Displaying Players_List");
			System.out.println("S.NO || PLAYER ID || PLAYER NAME || TEAM NAME");
			JSONArray ja= (JSONArray)js.get("player");
			for(Object p:ja)
			{
				JSONObject player=(JSONObject)p;
				mp.put(String.valueOf(count), player.get("id")+" - "+player.get("name")+" - "+player.get("teamName"));
				ArrayList alin = new ArrayList();
				alin.add(player.get("id"));
				alin.add(player.get("name"));
				alin.add(player.get("teamName"));
				al.add(alin);
				count++;			
			}
			ArrayList headings = (ArrayList)((Map)((Map) c.getConfig("Crickbuzz")).get("Players")).get("getPlayersList");
			HashMap hm= new HashMap();
			hm.put("headings", headings);
			hm.put("data", al);
			hout.put("Players_list", hm);
			
			for(Object x:mp.keySet())
			{
				System.out.println(x+"."+mp.get(x));
			}
			String select= c.exceptions(mp, "Select player Id: ");
			String[] playerId=mp.get(select).toString().split(" - ");
			System.out.println("Your selected Player Name is : "+playerId[1]);
			this.log.info("Your selected Player Name is : "+playerId[1]);
			this.log.info(playerId[0]);
			player_id=playerId[0];
		} 
		catch (Exception e)
		{
			System.out.println("You have exceeded the DAILY quota for Requests on your current plan, BASIC. Upgrade your plan at https://rapidapi.com/cricketapilive/api/cricbuzz-cricket");
			this.log.error("You have exceeded the DAILY quota for Requests on your current plan, BASIC. Upgrade your plan at https://rapidapi.com/cricketapilive/api/cricbuzz-cricket");
		}
		return player_id;
				
	}
	
	public void players_Career(HashMap hout, String requestMethod, String playerId, String vendor) throws IOException, InterruptedException, ParseException
	{
		String url=((String)((Map)c.getConfig("Crickbuzz")).get("players_comm_url"))+playerId+"/"+"career";
		this.log.info("URL = : "+url);
		System.out.println("URL = : "+url);
		
		String val=c.apiCall(requestMethod, url, vendor);
		JSONObject js= (JSONObject)new JSONParser().parse(val);
		
		ArrayList al = new ArrayList();
		
		JSONArray ja=(JSONArray)js.get("values");
		int count=1;
		this.log.info("Displaying players_Career");
		System.out.println("S.NO || MATCH FORMAT || DEBUT MATCH || LAST PLAYED ");
		for(Object re:ja)
		{
			JSONObject value=(JSONObject)re;
			this.log.info(count+"."+value.get("name")+" - "+value.get("debut")+" - "+value.get("lastPlayed"));
			ArrayList alin = new ArrayList();
			alin.add(value.get("name"));
			alin.add(value.get("debut"));
			alin.add(value.get("lastPlayed"));
			al.add(alin);
			count++;			
		}	
		ArrayList headings = (ArrayList)((Map)((Map) c.getConfig("Crickbuzz")).get("Players")).get("getPlayersCareer");
		HashMap hm= new HashMap();
		hm.put("headings", headings);
		hm.put("data", al);
		hout.put(playerId+"Players_Career", hm);		
		
	}
	
	public void player_bowling(HashMap hout, String requestMethod, String playerId, String vendor) throws IOException, InterruptedException, ParseException
	{
		String url=((String)((Map)c.getConfig("Crickbuzz")).get("players_comm_url"))+playerId+"/"+"bowling";
		this.log.info("URL = :"+url);
		System.out.println();
		
		ArrayList al = new ArrayList();
		String val=c.apiCall(requestMethod, url, vendor);
		JSONObject js=(JSONObject)new JSONParser().parse(val);
		ArrayList headings=(ArrayList)(JSONArray)js.get("headers");
		for(Object hea:headings)
		{
			System.out.print(hea+"|");
		}
		System.out.println();
		JSONArray ja=(JSONArray)js.get("values");
		for(Object v:ja)
		{
			JSONObject valOut=(JSONObject)v;
			ArrayList valIn=(ArrayList)(JSONArray)valOut.get("values");
			al.add(valIn);
			for(Object inn:valIn)
			{
				System.out.print(inn+" ");
			}
			System.out.println();
		}
		HashMap hm= new HashMap();
		hm.put("headings", headings);
		hm.put("data", al);
		hout.put(playerId+"Players_Bowling", hm);		
	}
	
	public void players_batting(HashMap hout, String requestMethod, String playerId, String vendor) throws IOException, InterruptedException, ParseException
	{
		String url=((String)((Map)c.getConfig("Crickbuzz")).get("players_comm_url"))+playerId+"/"+"batting";
		this.log.info("URL = :"+url);	
		System.out.println("URL = :"+url);
		String val=c.apiCall(requestMethod, url, vendor);
		
		ArrayList al = new ArrayList();	
		
		JSONObject js=(JSONObject)new JSONParser().parse(val);
		ArrayList headings =(ArrayList)((JSONArray)js.get("headers"));
			for(Object k: headings)
			{
				System.out.print(k+"|");
			}
			System.out.println();
		int cout=1;
		JSONArray ja=(JSONArray)js.get("values");
		for(Object v:ja)
		{
			JSONObject valOut=(JSONObject)v;
			ArrayList JA= (ArrayList)((JSONArray)valOut.get("values"));
			al.add(JA);
			for(Object inn:JA)
			{
				System.out.print(inn+"  ");			
			}
			System.out.println();
		}
		HashMap hm= new HashMap();
		hm.put("headings", headings);
		hm.put("data", al);
		hout.put(playerId+"Players_Batting", hm);
		
		
	}
}


