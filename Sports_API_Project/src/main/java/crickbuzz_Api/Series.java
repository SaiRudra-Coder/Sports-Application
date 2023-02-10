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

public class Series 
{	
	CommonForAll c;
	public Logger log;
	public Series(CommonForAll cfa)
	{
		this.log= Logger.getLogger(Series.class.getName());
		this.c=cfa;
	}
	
	@SuppressWarnings("unchecked")
	public String getSeries(HashMap hout, String requestMethod, String format, String vendor) throws IOException, InterruptedException, ParseException
	{
		String url=((String)((Map)c.getConfig("Crickbuzz")).get("series_url"))+format;
		System.out.println(url);
		this.log.info(url);		
		String val=c.apiCall(requestMethod, url, vendor);
		String series_ID=null;
		try 
		{
			JSONObject js= (JSONObject)new JSONParser().parse(val);
			ArrayList al = new ArrayList();
			
			JSONArray series =(JSONArray)js.get("seriesMapProto");
			LinkedHashMap lhm=new LinkedHashMap();
			int count=1;
			for(Object se:series)
			{
				JSONObject seriesmap=(JSONObject)se;
				JSONArray seInner=(JSONArray)seriesmap.get("series");
				for(Object in:seInner)
				{
					JSONObject seriesInner=(JSONObject)in;				
					lhm.put(String.valueOf(count), seriesInner.get("id")+" - "+seriesmap.get("date")+" - "+seriesInner.get("name"));
					ArrayList alin = new ArrayList();
					alin.add(seriesInner.get("id"));
					alin.add(seriesmap.get("date"));
					alin.add(seriesInner.get("name"));
					al.add(alin);
					count++;				
				}
			}
			ArrayList headings = (ArrayList)((Map)((Map) c.getConfig("Crickbuzz")).get("Series")).get("getSeries");			
			HashMap hm = new HashMap();
			hm.put("headings", headings);
			hm.put("data", al);
			hout.put("series", hm);
			this.log.info("Displaying getSeries");
			System.out.println("S.NO || SERIES ID || DATE || SERIES NAME ");
			for(Object k:lhm.keySet())
			{
				System.out.println(k+"."+lhm.get(k));
			}
			String select=c.exceptions(lhm, "Select series Id based on s.no: ");
			String[] seriesID= lhm.get(select).toString().split(" - ");
			System.out.println();
			System.out.println("Your selected series Name is : "+seriesID[2]);
			this.log.info("Your selected series Name is : "+seriesID[2]);
			this.log.info(seriesID[0]);
			System.out.println(seriesID[0]);
			series_ID=seriesID[0];				
		}
		catch (Exception e) 
		{
			System.out.println("You have exceeded the DAILY quota for Requests on your current plan, BASIC. Upgrade your plan at https://rapidapi.com/cricketapilive/api/cricbuzz-cricket");
			this.log.error("You have exceeded the DAILY quota for Requests on your current plan, BASIC. Upgrade your plan at https://rapidapi.com/cricketapilive/api/cricbuzz-cricket");
		}
		return series_ID;
			
	}
	
	public void get_Matches(HashMap hout, String requestMethod, String SeriesId, String vendor) throws IOException, InterruptedException, ParseException
	{
		String url=((String)((Map)c.getConfig("Crickbuzz")).get("series_match_url"))+SeriesId;
		this.log.info("URL = : "+url+"\n");
		System.out.println("URL = : "+url+"\n");
		String val=c.apiCall(requestMethod, url, vendor);
		
		JSONObject js=(JSONObject)new JSONParser().parse(val);
		JSONArray match=(JSONArray)js.get("matchDetails");
		ArrayList al = new ArrayList();
		
		int count =1;
		System.out.println("Displaying get_Matches");
		System.out.println("S.NO || SERIES ID || MATCH ID || MATCH FORMAT || MATCH KEY  || SERIES NAME || STATE || STATUS"+"\n");

		for(Object k:match)
		{
			JSONObject matchD=(JSONObject)k;
			try
			{
				JSONObject matchDM=(JSONObject)matchD.get("matchDetailsMap");
				JSONArray minfo=(JSONArray)matchDM.get("match");
				for(Object m:minfo)
				{
					JSONObject math=(JSONObject)m;
					JSONObject matchInfo=(JSONObject)math.get("matchInfo");
					System.out.println(count+" . "+matchInfo.get("seriesId")+" - "+matchInfo.get("matchId")+" - "+matchInfo.get("matchFormat")+" - "+matchDM.get("key")+" - "+matchInfo.get("seriesName")+" - "+matchInfo.get("state")+" - "+matchInfo.get("status"));
					ArrayList inal =  new ArrayList();
					inal.add(matchInfo.get("seriesId"));
					inal.add(matchInfo.get("matchId"));
					inal.add(matchInfo.get("matchFormat"));
					inal.add(matchDM.get("key"));
					inal.add(matchInfo.get("seriesName"));
					inal.add(matchInfo.get("state"));
					inal.add(matchInfo.get("status"));
					al.add(inal);
					count++;
				}
				ArrayList headings = (ArrayList)((Map)((Map) c.getConfig("Crickbuzz")).get("Series")).get("getMatches");	
				HashMap hm = new HashMap();
				hm.put("headings", headings);
				hm.put("data", al);
				hout.put(SeriesId+"_matches", hm);
			} 
			catch (Exception e) 
			{
				System.out.println("No data");
				this.log.error("No data");
			}			
		}
				
	}
	
	public String getSquad(HashMap hout, String requestMethod, String SeriesId, String vendor) throws IOException, InterruptedException, ParseException
	{
		String url=((String)((Map)c.getConfig("Crickbuzz")).get("squard_url"))+SeriesId+"/"+"squads";
		this.log.info("URL = : "+url);
		System.out.println("URL = : "+url);
		
		String val=c.apiCall(requestMethod, url, vendor);
		String sqdID=null;
		try 
		{
			JSONObject js=(JSONObject)new JSONParser().parse(val);			
			ArrayList al = new ArrayList();			
			LinkedHashMap lm= new LinkedHashMap();
			int count=1;
			
			JSONArray ja=(JSONArray)js.get("squads");	
			for(Object squ:ja)
			{
				JSONObject squads=(JSONObject)squ;
				if (squads.get("squadId") != null)
				{
					lm.put(String.valueOf(count), squads.get("squadId")+" - "+squads.get("squadType")+" - "+js.get("seriesName"));
					ArrayList alin = new ArrayList();
					alin.add(squads.get("squadId"));
					alin.add(squads.get("squadType"));
					alin.add(js.get("seriesName"));
					al.add(alin);
					count++;
				}
			}
			ArrayList headings = (ArrayList)((Map)((Map) c.getConfig("Crickbuzz")).get("Series")).get("getSquad");	
			HashMap hm = new HashMap();
			hm.put("headings", headings);
			hm.put("data", al);
			hout.put(SeriesId+"squad_info", hm);
			System.out.println("Displaying getSquad");	
			System.out.println("S.NO  ||  SQUAD ID  ||  SQUAD TYPE  ||  SERIES NAME  ");
			for(Object serie:lm.keySet())
			{
				System.out.println(serie+"."+lm.get(serie));
			}
			String select=c.exceptions(lm, "Select series id : ");
			String[] squadID= lm.get(select).toString().split(" - ");
			System.out.println("Your selected series Name is : "+squadID[1]);
			this.log.info("Your selected series Name is : "+squadID[1]);
			this.log.info(squadID[0]);
			System.out.println(squadID[0]);
			sqdID=squadID[0];
		} 
		catch (Exception e) 
		{
			System.out.println("No data inthis field");
			this.log.error("No data inthis field");
		}
		return sqdID;
					
	}
	
	public void get_Players(HashMap hout, String requestMethod, String SeriesId, String squadID, String vendor) throws IOException, InterruptedException, ParseException
	{
		String url=((String)((Map)c.getConfig("Crickbuzz")).get("player_url"))+SeriesId+"/"+"squads"+"/"+squadID;
		this.log.info("URL = :"+url);
		System.out.println("URL = :"+url);
		String val=c.apiCall(requestMethod, url, vendor);
		JSONObject js=(JSONObject)new JSONParser().parse(val);
		
		ArrayList al = new ArrayList();
		
		JSONArray ja=(JSONArray)js.get("player");
		int count=1;		
		try 
		{			
			System.out.println("Displaying get_players");
			System.out.println("S.NO  || ID  ||  NAME  ||  ROLE  ||  CAPTAIN  ||  BATTING STYLE  ||  BOWLING STYLE"+"\n");
			for(Object p: ja)
			{
				JSONObject player=(JSONObject)p;
				System.out.println(count+" . "+player.get("id")+" - "+player.get("name")+" - "+player.get("role")+" - "+player.get("captain")+" - "+player.get("battingStyle")+" - "+player.get("bowlingStyle"));
				ArrayList alin = new ArrayList();
				alin.add(player.get("id"));
				alin.add(player.get("name"));
				alin.add(player.get("role"));
				alin.add(player.get("captain"));
				alin.add(player.get("battingStyle"));
				alin.add(player.get("bowlingStyle"));
				al.add(alin);
				count++;
			}
		} 
		catch (java.lang.NullPointerException e) 
		{
			System.out.println("No data available");
			this.log.error("No data available");
		}	
		
		ArrayList headings = (ArrayList)((Map)((Map) c.getConfig("Crickbuzz")).get("Series")).get("getPlayers");	
		HashMap hm = new HashMap();
		hm.put("headings", headings);
		hm.put("data", al);
		hout.put(squadID+"squad_info", hm);
	}	
	
	public void get_PointaTable(HashMap hout, String requestMethod, String SeriesId, String vendor) throws IOException, InterruptedException, ParseException
	{
		String url =((String)((Map)c.getConfig("Crickbuzz")).get("points_table_url"))+SeriesId+"/"+"points-table";
		this.log.info("URL = : "+url+"\n");	
		System.out.println("URL = : "+url+"\n");
		try
		{
			String val=c.apiCall(requestMethod, url, vendor);
			JSONObject js =(JSONObject)new JSONParser().parse(val);
			
			ArrayList al = new ArrayList();
			
			JSONArray ja=(JSONArray)js.get("pointsTable");
			int count=1;
			
			System.out.println("Displaying get_PointaTable");
			System.out.println("S.NO||TEAM ID||TEAM NAME||MATCHES PLAYED||MATCHES WON||MATCHES LOSE||POINTS||MATCH ID||MATCH NAME||OPPONENT||RESULTS");
			for(Object po:ja)
			{
				JSONObject points_table=(JSONObject)po;
				JSONArray table_det=(JSONArray)points_table.get("pointsTableInfo");
				for(Object ta:table_det)
				{
					JSONObject tb_inf=(JSONObject)ta;
					JSONArray team=(JSONArray)tb_inf.get("teamMatches");
					for(Object te:team)
					{
						JSONObject team_info=(JSONObject)te;
						System.out.println(count+"."+tb_inf.get("teamId")+" - "+tb_inf.get("teamFullName")+" - "+tb_inf.get("matchesPlayed")+" - "+tb_inf.get("matchesWon")+" - "+tb_inf.get("matchesLost")+" - "+tb_inf.get("points")+" - "+
						team_info.get("matchId")+" - "+team_info.get("matchName")+" - "+team_info.get("opponent")+" - "+team_info.get("result"));
						ArrayList alin = new ArrayList();
						alin.add(tb_inf.get("teamId"));alin.add(tb_inf.get("teamFullName"));alin.add(tb_inf.get("matchesPlayed"));alin.add(tb_inf.get("matchesWon"));alin.add(tb_inf.get("matchesLost"));alin.add(tb_inf.get("points"));
						alin.add(team_info.get("matchId"));alin.add(team_info.get("matchName"));alin.add(team_info.get("opponent"));alin.add(team_info.get("result"));al.add(alin);
						
						count++;
					}
				}
			}
			ArrayList headings = (ArrayList)((Map)((Map) c.getConfig("Crickbuzz")).get("Series")).get("getPointtable");	
			HashMap hm = new HashMap();
			hm.put("headings", headings);
			hm.put("data", al);
			hout.put(SeriesId+"points-table", hm);			
		} 
		catch (Exception e) 
		{
			System.out.println("No content!");
			this.log.error("No content!");
		}		
		
	}
	
	public String stat_filters(HashMap hout, String requestMethod, String SeriesId, String vendor) throws IOException, InterruptedException, ParseException
	{
		String url=((String)((Map)c.getConfig("Crickbuzz")).get("stat_filter_url"))+SeriesId;
		this.log.info("URL= : "+url+"\n");
		System.out.println("URL= : "+url+"\n");
		
		String val=c.apiCall(requestMethod, url, vendor);
		ArrayList al = new ArrayList();		
		LinkedHashMap l= new LinkedHashMap();
		int count=1;
		
		JSONObject js=(JSONObject)new JSONParser().parse(val);
		JSONArray ja=(JSONArray)js.get("types");
		for(Object t:ja)
		{
			JSONObject type=(JSONObject)t;
			l.put(String.valueOf(count), type.get("header")+" - "+type.get("value")+" - "+type.get("category"));
			ArrayList alin = new ArrayList();
			alin.add(type.get("header"));
			alin.add(type.get("value"));
			alin.add(type.get("category"));
			al.add(alin);
			count++;
		}
		ArrayList headings = (ArrayList)((Map)((Map) c.getConfig("Crickbuzz")).get("Series")).get("getStatFilter");
		HashMap hm = new HashMap();
		hm.put("headings", headings);
		hm.put("data", al);
		hout.put(SeriesId+"stats_filters", hm);	
		System.out.println("Displaying stat_filters");
		System.out.println("S.NO  ||  HEADER  ||  VALUE  || CATEGORY");
		for(Object j:l.keySet())
		{
			System.out.println(j+"."+l.get(j));
		}		
		String select=c.exceptions(l, "Select value: ");
		String[] value= l.get(select).toString().split(" - ");
		System.out.println("Your selected value Name is : "+value[1]);
		this.log.info("Your selected value Name is : "+value[1]);
		this.log.info(value[1]);
		System.out.println(value[1]);
		return value[1];			
	}	
	
	public void stats(HashMap hout,String requestMethod, String SeriesId, String value, String vendor) throws IOException, InterruptedException, ParseException
	{
		String url=((String)((Map)c.getConfig("Crickbuzz")).get("stats_url"))+SeriesId+"?statsType="+value;
		this.log.info("URL = : "+url);
		System.out.println("URL = : "+url);
		String val=c.apiCall(requestMethod, url, vendor);
		ArrayList al = new ArrayList();
		
		JSONObject js=(JSONObject)new JSONParser().parse(val);
		JSONObject tes =(JSONObject)js.get("testStatsList");
		JSONArray he = (JSONArray)tes.get("headers");
		for(Object k:he)
		{
			System.out.print(k+" || ");
			ArrayList alin= new ArrayList();
			alin.add(k);
			al.add(alin);
		}
		System.out.println();
		JSONArray vout=(JSONArray)tes.get("values");
		for(Object k1:vout)
		{
			JSONObject v= (JSONObject)k1;
			JSONArray v2=(JSONArray)v.get("values");
			for(Object vin:v2)
			{
				System.out.print(vin+"  ");
				ArrayList alin2= new ArrayList();
				alin2.add(vin);
				al.add(alin2);
			}
			System.out.println();
		}
		ArrayList headings = (ArrayList)((Map)((Map) c.getConfig("Crickbuzz")).get("Series")).get("getStats");
		HashMap hm =new HashMap();
		hm.put("headings", headings);
		hm.put("data", al);
		hout.put(value+"_stats", hm);
		
	}
}
