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

public class Stats 
{
	CommonForAll c;
	public Logger log;
	public Stats(CommonForAll cfa)
	{
		this.log=Logger.getLogger(Stats.class.getName());
		this.c=cfa;
	}
	
	public void stats_ranking(HashMap hout, String requestMethod, String cate, String typeFormat, String vendor) throws IOException, InterruptedException, ParseException
	{
		String url=((String)((Map)c.getConfig("Crickbuzz")).get("stats_ranking_url"))+cate+"?formatType="+typeFormat;
		this.log.info("URL = : "+url);
		System.out.println("URL = : "+url);
		
		String val =c.apiCall(requestMethod, url, vendor);
		try 
		{
			JSONObject js=(JSONObject)new JSONParser().parse(val);
			
			ArrayList al = new ArrayList();
			
			JSONArray ja= (JSONArray)js.get("rank");
			int count=1;
			for(Object ra:ja)
			{
				JSONObject rank=(JSONObject)ra;
				System.out.println(count+"."+rank.get("rank")+" - "+rank.get("name")+" - "+rank.get("id")+" - "+rank.get("country")+" - "+rank.get("points"));
				ArrayList alin = new ArrayList();
				alin.add(rank.get("rank")); alin.add(rank.get("name")); alin.add(rank.get("id")); alin.add(rank.get("country")); alin.add(rank.get("points")); 
				al.add(alin);
				count++;
			}
			ArrayList headings = (ArrayList)((Map)((Map) c.getConfig("Crickbuzz")).get("Stats")).get("getStatsRankings");
			HashMap hm = new HashMap();
			hm.put("headings", headings);
			hm.put("data", al);
			hout.put(cate+" "+typeFormat+"_ranking", hm);
		} 
		catch (Exception e) 
		{
			System.out.println("You have exceeded the DAILY quota for Requests on your current plan, BASIC. Upgrade your plan at https://rapidapi.com/cricketapilive/api/cricbuzz-cricket");
			this.log.error("You have exceeded the DAILY quota for Requests on your current plan, BASIC. Upgrade your plan at https://rapidapi.com/cricketapilive/api/cricbuzz-cricket");
		}		
		
	}
	
	public String stats_record_filters(HashMap hout, String requestMethod, String vendor) throws IOException, InterruptedException, ParseException
	{
		String url= (String) ((Map)c.getConfig("Crickbuzz")).get("stats_reFilter_url");
		this.log.info("URL = :"+url);
		System.out.println("URL = :"+url);
		
		String val=c.apiCall(requestMethod, url, vendor);
		JSONObject js=(JSONObject)new JSONParser().parse(val);
		
		ArrayList al = new ArrayList();		
		LinkedHashMap lik=new LinkedHashMap();
		int count=1;
		
		JSONArray ja=(JSONArray)js.get("statsTypesList");
		for(Object st:ja)
		{
			JSONObject stats =(JSONObject)st;
			JSONArray ty=(JSONArray)stats.get("types");
			for(Object tyIn:ty)
			{
				JSONObject types=(JSONObject)tyIn;
				lik.put(String.valueOf(count), types.get("value")+" - "+types.get("header")+" - "+types.get("category"));
				ArrayList alin= new ArrayList();
				alin.add(types.get("value"));
				alin.add(types.get("header"));
				alin.add(types.get("category"));
				al.add(alin);				
				count++;
			}
		}
		ArrayList headings = (ArrayList)((Map)((Map) c.getConfig("Crickbuzz")).get("Stats")).get("getStatsRecordFilter");
		HashMap hm = new HashMap();
		hm.put("headings", headings);
		hm.put("data", al);
		hout.put("statsRecord_Fileters", hm);		
		System.out.println("Displaying stats_record_filters");
		System.out.println("S.NO || VALUE || HEADER || CATEGORTY");
		for(Object r:lik.keySet())
		{
			System.out.println(r+". "+lik.get(r));
		}
		String select=c.exceptions(lik,"Select value : ");
		String[] statsType=lik.get(select).toString().split(" - ");
		System.out.println("Your selected Player category is : "+statsType[0]);
		this.log.info("Your selected Player category is : "+statsType[0]);
		this.log.info(statsType[0]);
		System.out.println(statsType[0]);
		return statsType[0];		
	}
	
	public void stats_record(HashMap hout, String requestMethod, String statsType, String vendor) throws IOException, InterruptedException, ParseException
	{
		String url=((String)((Map)c.getConfig("Crickbuzz")).get("stats_reco_url"))+"?statsType="+statsType;
		this.log.info("URL = : "+url);
		String val=c.apiCall(requestMethod, url, vendor);
		JSONObject js=(JSONObject)new JSONParser().parse(val);		
		ArrayList al= new ArrayList();
		ArrayList headings=(ArrayList)(JSONArray)js.get("headers");
		for(Object k: headings)
		{
			System.out.print(k+"|");
		}
		System.out.println();
		ArrayList va=(ArrayList)(JSONArray)js.get("values");
		for(Object x:va)
		{
			JSONObject valOut=(JSONObject)x;
			ArrayList data=(ArrayList)(JSONArray)valOut.get("values");
			al.add(data);
			for(Object values:data)
			{
				System.out.print(values+"  ");
			}
			System.out.println();
		}
		HashMap hm = new HashMap();
		hm.put("headings", headings);
		hm.put("data", al);
		hout.put(statsType+"stats_type", hm);				
	}
	
	public void stats_standings(HashMap hout, String requestMethod, String vendor) throws IOException, InterruptedException, ParseException
	{
		HashMap sh= new HashMap();
		sh.put("a", "1");
		sh.put("b", "2");
		for(Object s:sh.keySet())
		{
			System.out.println(s+"."+sh.get(s));
		}
		String user_input=c.exceptions(sh,"Choose Your Option");
		System.out.println("Your Choosen Option is : "+sh.get(user_input));
		this.log.info("Your Choosen Option is : "+sh.get(user_input));
		String matchType=sh.get(user_input).toString();
		
		String url=((String)((Map)c.getConfig("Crickbuzz")).get("stats-sta_url"))+matchType;
		this.log.info("URL = : "+url);
		System.out.println("URL = : "+url);
		String val=c.apiCall(requestMethod, url, vendor);
		JSONObject js= (JSONObject)new JSONParser().parse(val);
		
		ArrayList al = new ArrayList();
		
		int count=1;
		this.log.info(js.get("headers"));
		JSONArray v=(JSONArray)js.get("values");
		for(Object vIn:v)
		{
			JSONObject value=(JSONObject)vIn;
			System.out.println(count+"."+value.get("value"));
			ArrayList alin = new ArrayList();
			alin.add(value.get("value"));
			alin.add(js.get("headers"));
			al.add(alin);			
			count++;
		}
		ArrayList headings = (ArrayList)((Map)((Map) c.getConfig("Crickbuzz")).get("Stats")).get("getStatsStandings");
		HashMap hm =new HashMap();
		hm.put("headings", headings);
		hm.put("data", al);
		hout.put("stats_Standings", hm);	
	}
}
