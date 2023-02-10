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

public class Teams 
{
	CommonForAll c;
	public Logger log;
	public Teams (CommonForAll cfa)
	{
		this.log=Logger.getLogger(Teams.class.getName());
		this.c=cfa;
	}
	
	public String teams_list(HashMap hout, String requestMethod, String type, String vendor) throws IOException, InterruptedException, ParseException
	{		
		String url=((String)((Map)c.getConfig("Crickbuzz")).get("teams_url"))+type;
		this.log.info("URL = : "+url);
		System.out.println("URL = : "+url);
		String val=c.apiCall(requestMethod, url, vendor);
		String team_id=null;
		try
		{
			JSONObject js=(JSONObject)new JSONParser().parse(val);
			ArrayList al = new ArrayList();			
			LinkedHashMap l= new LinkedHashMap();
			int count=1;
			
			JSONArray ja=(JSONArray)js.get("list");
			for(Object li:ja)
			{
				JSONObject list =(JSONObject)li;
				if(list.get("teamId")!=null )
				{
					l.put(String.valueOf(count), list.get("teamId")+" - "+list.get("teamName"));
					ArrayList alin = new ArrayList();
					alin.add(list.get("teamId"));
					alin.add(list.get("teamName"));
					al.add(alin);
					count++;
				}			
			}
			ArrayList headings = (ArrayList)((Map)((Map) c.getConfig("Crickbuzz")).get("Teams")).get("getTeamsList");
			HashMap hm=new HashMap();
			hm.put("headings", headings);
			hm.put("data", al);
			hout.put("Teams_list", hm);
			System.out.println("Displaying teams_list");
			System.out.println("S.NO || TEAM ID || TEAM NAME");
			for(Object list:l.keySet())
			{
				System.out.println(list+"."+l.get(list));
			}
			String select=c.exceptions(l, "Select team id:  ");
			String[] teamId= l.get(select).toString().split(" - ");
			System.out.println("Your selected Teams Name is : "+teamId[1]);
			this.log.info("Your selected Teams Name is : "+teamId[1]);
			this.log.info(teamId[0]);
			System.out.println(teamId[0]);
			team_id=teamId[0];
		}
		catch (Exception e) 
		{
			System.out.println("You have exceeded the DAILY quota for Requests on your current plan, BASIC. Upgrade your plan at https://rapidapi.com/cricketapilive/api/cricbuzz-cricket");
			this.log.error("You have exceeded the DAILY quota for Requests on your current plan, BASIC. Upgrade your plan at https://rapidapi.com/cricketapilive/api/cricbuzz-cricket");
		}
		return team_id;
			
	}
	
	 public void team_schedule(HashMap hout, String requestMethod, String teamId, String vendor) throws IOException, InterruptedException, ParseException
	 {
		 String url=((String)((Map)c.getConfig("Crickbuzz")).get("teams_url"))+teamId+"/"+"schedule";
		 this.log.info("URL= :" +url+"\n");
		 System.out.println("URL= :" +url+"\n");
		 String val= c.apiCall(requestMethod, url, vendor);
		 try 
		 {
			 JSONObject js=(JSONObject)new JSONParser().parse(val);			 
			 ArrayList al = new ArrayList();			 
			 
			 JSONArray ja=(JSONArray)js.get("teamMatchesData");
			 int count=1;		 
			 System.out.println("Displaying team_schedule");
			 System.out.println("S,NO || SERIES ID || MATCH ID || SERIES NAME || MATCH DESC"+"\n");
			 for(Object ja1:ja)
			 {
				 JSONObject te_match=(JSONObject)ja1;
				 try 
				{
					 JSONObject matD=(JSONObject)te_match.get("matchDetailsMap");
					 
					 JSONArray m =(JSONArray)matD.get("match");
					 for(Object ke:m)
					 {
						 JSONObject ma=(JSONObject)ke;
						 JSONObject ma_info=(JSONObject)ma.get("matchInfo");
						 System.out.println(count+"."+ma_info.get("seriesId")+" - "+ma_info.get("matchId")+" - "+ma_info.get("seriesName")+" - "+ma_info.get("matchDesc"));
						 ArrayList alin= new ArrayList();
						 alin.add(ma_info.get("seriesId"));
						 alin.add(ma_info.get("matchId"));
						 alin.add(ma_info.get("seriesName"));
						 alin.add(ma_info.get("matchDesc"));
						 al.add(alin);
						 count++;
					 }
				}
				catch (Exception e)
				{
					System.out.println("At this moment no more schedule prepared for this team");
					this.log.error("At this moment no more schedule prepared for this team");
				}				 
			 }
			 
			ArrayList headings = (ArrayList)((Map)((Map) c.getConfig("Crickbuzz")).get("Teams")).get("getTeamSchedule");
			 HashMap hm = new HashMap();
			 hm.put("headings", headings);
			 hm.put("data", al);
			 hout.put(teamId+"team_schedule", hm);
		 } 
		 catch (Exception e) 
		 {
			 System.out.println("No content!");
			this.log.error("No content!");
		 }	
		 
	 }	 
	 
	 public void team_results(HashMap hout, String requestMethod, String teamId, String vendor) throws IOException, InterruptedException, ParseException
	 {
		String url=((String)((Map)c.getConfig("Crickbuzz")).get("teams_url"))+teamId+"/"+"results";
		System.out.println("URL = : "+url+"\n");
		this.log.info("URL = : "+url+"\n");
		
		String val=c.apiCall(requestMethod, url, vendor);
		JSONObject js=(JSONObject)new JSONParser().parse(val);
		
		ArrayList al = new ArrayList();
		 
		 JSONArray ja=(JSONArray)js.get("teamMatchesData");
		 int count=1;	
		 System.out.println("Displaying team_results");
		 System.out.println("S,NO || SERIES ID || MATCH ID || SERIES NAME || MATCH DESC || TEAM1 SCORE & TEAM2 SCORE =>>>>>: || RUNS || WICKETS || OVERS ");
		 for(Object ja1:ja)
		 {
			 JSONObject te_match=(JSONObject)ja1;
			 try 
			{
				 JSONObject matD=(JSONObject)te_match.get("matchDetailsMap");				 
				 JSONArray m =(JSONArray)matD.get("match");
				 for(Object ke:m)
				 {
					 JSONObject ma=(JSONObject)ke;
					 JSONObject ma_info=(JSONObject)ma.get("matchInfo");
					 JSONObject ma_score=(JSONObject)ma.get("matchScore");
					 JSONObject te1=(JSONObject)ma_score.get("team1Score");
					 JSONObject te2=(JSONObject)ma_score.get("team2Score");
					 JSONObject in1=(JSONObject)te1.get("inngs1");
					 JSONObject in2=(JSONObject)te2.get("inngs1");
					 
					 System.out.println(count+"."+ma_info.get("seriesId")+" - "+ma_info.get("matchId")+" - "+ma_info.get("seriesName")+" - "+ma_info.get("matchDesc")+".   "+"Team1 Score : "+in1.get("runs")+" - "+in1.get("wickets")+" - "+in1.get("overs")+".   "+"Team2 Score : "+in2.get("runs")+" - "+in2.get("wickets")+" - "+in2.get("overs"));
					 ArrayList alin = new ArrayList();

					 alin.add(ma_info.get("seriesId"));
					 alin.add(ma_info.get("matchId"));
					 alin.add(ma_info.get("seriesName")); alin.add(ma_info.get("matchDesc")); alin.add(in1.get("runs")); alin.add(in1.get("wickets")); alin.add(in1.get("overs")); alin.add(in2.get("runs")); alin.add(in2.get("wickets")); alin.add(in2.get("overs"));
					 al.add(alin);
					 count++;
				 }
				ArrayList headings = (ArrayList)((Map)((Map) c.getConfig("Crickbuzz")).get("Teams")).get("getTeamResults");
				 HashMap hm = new HashMap();
				 hm.put("headings", headings);
				 hm.put("data", al);
				 hout.put(teamId+"Team_schedule", hm);				 
			}
			catch (Exception e)
			{
				// TODO: handle exception
			}			 
		 }
		 
	 }
	 
	 public void team_players(HashMap hout, String requestMethod, String teamId, String vendor) throws IOException, InterruptedException, ParseException
	 {
		 String url =((String)((Map)c.getConfig("Crickbuzz")).get("teams_url"))+teamId+"/"+"players";
		 this.log.info("URL = : "+url);
		 String val=c.apiCall(requestMethod, url, vendor);
		 JSONObject js= (JSONObject)new JSONParser().parse(val);
		 
		 ArrayList al = new ArrayList();		 
		 int count=1;
		 
		 JSONArray ja= (JSONArray)js.get("player");
		 for(Object p:ja)
		 {
			 JSONObject player=(JSONObject)p;
			 if(player.get("id")!=null)
			 {
				 this.log.info(count+"."+player.get("id")+" - "+player.get("name")+" - "+player.get("battingStyle")+" - "+player.get("bowlingStyle"));
				 ArrayList alin = new ArrayList();
				 alin.add(player.get("id")); alin.add(player.get("name")); alin.add(player.get("battingStyle")); alin.add(player.get("bowlingStyle")); 
				 al.add(alin);
				 count++; 
			 }	
			ArrayList headings = (ArrayList)((Map)((Map) c.getConfig("Crickbuzz")).get("Teams")).get("getTeamPlayers");
			 HashMap hm = new HashMap();
			 hm.put("headings", headings);
			 hm.put("data", al);
			 hout.put(teamId+"players", hm);
		 }
		 
		 
	 }	
}
