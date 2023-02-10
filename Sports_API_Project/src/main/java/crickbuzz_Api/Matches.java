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

public class Matches 
{
	CommonForAll c;
	public Logger log;
	public Matches(CommonForAll cfa)
	{
		this.c = cfa;
		this.log=Logger.getLogger(Matches.class.getName());
	}	
	@SuppressWarnings("unchecked")
	public String getMatch_List(HashMap hout, String requestMethod, String vendor) throws IOException, InterruptedException, ParseException
	{
		HashMap hm1= new HashMap();
		
		hm1.put("a", "recent");
		hm1.put("b", "live");
		hm1.put("c", "upcoming");
		for(Object k:hm1.keySet())
		{
			System.out.println(k+"."+hm1.get(k));
			this.log.info(k+"."+hm1.get(k));
		}
		String user_input=c.exceptions(hm1,"Choose Your Option");
		System.out.println("Your Choosen Option is : "+hm1.get(user_input.toLowerCase()));
		this.log.info("Your Choosen Option is : "+hm1.get(user_input.toLowerCase()));
		String format=hm1.get(user_input.toLowerCase()).toString();
		
		String url= ((String) ((Map)c.getConfig("Crickbuzz")).get("match_url"))+format;
		this.log.info("URL = "+url);
		String val=c.apiCall(requestMethod, url, vendor);
		
		JSONObject js= (JSONObject)new JSONParser().parse(val);			
		ArrayList al= new ArrayList();
		
		LinkedHashMap jout = new LinkedHashMap();
		
		JSONArray typ_match = (JSONArray) js.get("typeMatches");		
		for(Object k : typ_match)
		{
			int mt_counter = 1;
			JSONObject srs_k = (JSONObject) k;
			JSONArray srs_match = (JSONArray) srs_k.get("seriesMatches");
			for(Object z: srs_match)
			{
				JSONObject srs_wrap_obj = (JSONObject) z;
				JSONObject wrap_obj = (JSONObject) srs_wrap_obj.get("seriesAdWrapper");
				try 
				{
					JSONArray match_ary = (JSONArray) wrap_obj.get("matches");
					for(Object x: match_ary)
					{
						JSONObject mt_inf = (JSONObject)x;
						JSONObject mat_info = (JSONObject) mt_inf.get("matchInfo");
						jout.put(String.valueOf(mt_counter), mat_info.get("matchId")+" - "+mat_info.get("seriesId")+" - "+mat_info.get("seriesName")+" - "+mat_info.get("state")+" - "+mat_info.get("status"));
						ArrayList alin = new ArrayList();
						alin.add(mat_info.get("matchId"));
						alin.add(mat_info.get("seriesId"));
						alin.add(mat_info.get("seriesName"));
						alin.add(mat_info.get("state"));
						alin.add(mat_info.get("status"));
						al.add(alin);
						mt_counter++;
					}
					
				} 
				catch (Exception e) 
				{
					System.out.println("Not Data found for the requested API");
					this.log.error("Not Data found for the requested API");
				}
			}
		}
		ArrayList headings = (ArrayList)((Map)((Map) c.getConfig("Crickbuzz")).get("matchList")).get("get_matchListHeadings");
		HashMap hm = new HashMap();
		hm.put("headings", headings);
		hm.put("data", al);
		hout.put("matches_list", hm);
		String matchID=null;
		if(jout.size()>0)
		{
			this.log.info("Displaying getMatch_List");
			System.out.println("S.NO | Match ID  | Series ID | Series Name | State | Status");
			for(Object h : jout.keySet())
			{
				System.out.println(h+". "+jout.get(h));
//				this.log.info(h+". "+jout.get(h));
			}
			String user_ch=c.exceptions(jout, "Select match id based on s. no: ");
			String[] mat_id = jout.get(user_ch).toString().split(" - ");
			System.out.println("You selected Series "+mat_id[2]);
			this.log.info("You selected Series "+mat_id[2]);
			this.log.info(mat_id[0]);		
			matchID= mat_id[0];
			// 1234 - 2345 - balba - inco - t20
			// [1234, 2345, balba, inco, t20]
			//    0       1       2        3     4	
		}
		return matchID;		
				
	}
	
	@SuppressWarnings("unchecked")
	public String getMatch_info(HashMap hout, String requestMethod, String matchId, String vendor) throws IOException, InterruptedException, ParseException
	{
		String url=((Map)c.getConfig("Crickbuzz")).get("info_url")+matchId;
		String val=c.apiCall(requestMethod, url, vendor);
		JSONObject js= (JSONObject)new JSONParser().parse(val);	
		ArrayList al= new ArrayList();		
		LinkedHashMap jout = new LinkedHashMap();	
		int count=1;		
		
		JSONObject m_info=(JSONObject)js.get("matchInfo");
		JSONObject team1=(JSONObject)m_info.get("team1");
		JSONObject team2=(JSONObject)m_info.get("team2");
		JSONObject te1_info = new JSONObject ();
		JSONObject te2_info = new JSONObject();		
		jout.put(String.valueOf(count),team1.get("id")+" - "+ team1.get("name")+" - "+m_info.get("matchFormat")+" - "+m_info.get("matchDescription"));
		count++;
		jout.put(String.valueOf(count), team2.get("id")+" - "+team2.get("name")+" - "+m_info.get("matchFormat")+" - "+m_info.get("matchDescription"));
		ArrayList inal1= new ArrayList();
		inal1.add(team1.get("id"));
		inal1.add(team1.get("name"));
		inal1.add(m_info.get("matchFormat"));
		inal1.add(m_info.get("matchDescription"));
		ArrayList inal2= new ArrayList();
		inal2.add(team2.get("id"));
		inal2.add(team2.get("name"));
		inal2.add(m_info.get("matchFormat"));
		inal2.add(m_info.get("matchDescription"));		
		al.add(inal1);
		al.add(inal2);
		count++;
		
		ArrayList headings=(ArrayList)((Map)((Map)c.getConfig("Crickbuzz")).get("matchList")).get("get_matchInfoHeadings");
		HashMap hm = new HashMap();
		hm.put("headings", headings);
		hm.put("data", al);
		hout.put(matchId+"_info", hm);
		this.log.info("Displaying the getMatch_info");
		System.out.println("S.NO  ||  TEAMID  ||  TEAMNAME  ||  MATCHFORMATE  ||  MATCHDESCRIPTION");
		for(Object k:jout.keySet())
		{
			System.out.println(k+"."+jout.get(k));
//			this.log.info(k+"."+jout.get(k));
		}
		String user_ch = c.exceptions(jout, "Select team id based on s.no:");
		String[] team_id = jout.get(user_ch).toString().split(" - ");
		System.out.println("You selected teamID is "+team_id[1]);
		this.log.info("You selected teamID is "+team_id[1]);
		this.log.info(team_id[0]);
		return team_id[0];
	}
	
	public void getMatch_team(HashMap hout, String returnMethod, String matchId, String teamId, String vendor) throws IOException, InterruptedException, ParseException
	{
		String url=((Map)c.getConfig("Crickbuzz")).get("team_url")+matchId+"/"+"team"+"/"+teamId;
		this.log.info(url);
		String val=c.apiCall(returnMethod, url, vendor);
		JSONObject js=(JSONObject) new JSONParser().parse(val);
		
		ArrayList al = new ArrayList();
		LinkedHashMap lm= new LinkedHashMap();
		int count=1;
		
		JSONObject players=(JSONObject)js.get("players");
		try
		{
			JSONArray p11=(JSONArray)players.get("playing XI");
			for(Object k:p11)
			{
				JSONObject p11_info=(JSONObject)k;
				lm.put(String.valueOf(count), p11_info.get("id")+" - "+p11_info.get("fullName")+" - "+p11_info.get("role")+" - "+p11_info.get("captain")+" - "+p11_info.get("keeper"));
				ArrayList inal = new ArrayList();
				inal.add(p11_info.get("id"));
				inal.add(p11_info.get("fullName"));
				inal.add(p11_info.get("role"));
				inal.add(p11_info.get("captain"));
				inal.add(p11_info.get("keeper"));
				al.add(inal);
				count++;			
			}
			
			ArrayList headings=(ArrayList)((Map)((Map)c.getConfig("Crickbuzz")).get("matchList")).get("get_matchTeamHeadings");
			HashMap hm = new HashMap();
			hm.put("headings", headings);
			hm.put("data", al);
			hout.put(teamId+"_team", hm);
			this.log.info("Displaying getMatch_team");
			System.out.println("S.NO  ||  PLAYERID  ||  PLAYERNAME  || ROLE || CAPTAIN || KEEPER");
			for(Object s:lm.keySet())
			{
				System.out.println(s+"."+lm.get(s));
//				this.log.info(s+"."+lm.get(s));
			}
		} 
		catch (NullPointerException e) 
		{
			System.out.println("player keeper details not available at present");
		}
		
		
	}
	
	@SuppressWarnings("unchecked")
	public void getMatch_commentaries(HashMap hout, String requestMethod, String matchId, String vendor) throws IOException, InterruptedException, ParseException
	{
		String url=((Map)c.getConfig("Crickbuzz")).get("commen_url")+matchId+"/"+"comm";
		this.log.info(url);
		String val=c.apiCall(requestMethod, url, vendor);		
		JSONObject js= (JSONObject)new JSONParser().parse(val);
		
		ArrayList al= new ArrayList();		
		LinkedHashMap lin= new LinkedHashMap();
		int cou=1;
		
		JSONObject ma_header=(JSONObject)js.get("matchHeader");
		JSONObject toss_res=(JSONObject)ma_header.get("tossResults");
		JSONObject res=(JSONObject)ma_header.get("result");
		
		lin.put(String.valueOf(cou), toss_res.get("tossWinnerName")+" - "+ma_header.get("status")+" - "+res.get("winningTeam"));
		ArrayList alin= new ArrayList();
		alin.add(toss_res.get("tossWinnerName"));
		alin.add(ma_header.get("status"));
		alin.add(res.get("winningTeam"));
		al.add(alin);		
		cou++;
		
		ArrayList headings=(ArrayList)((Map)((Map)c.getConfig("Crickbuzz")).get("matchList")).get("get_matchCommentriesHeadings");
		HashMap hm =new HashMap();
		hm.put("headings", headings);
		hm.put("data", al);
		hout.put(matchId+"Commentries", hm);
		this.log.info("Displaying getMatch_commentaries");
		System.out.println("S.NO || TOSS WINNER NAME || CHOOSEN OPTION || MATCH WINNING TEAM");
		for(Object k:lin.keySet())
		{
			System.out.println(k+"."+lin.get(k));
		}
		
	}		
	public void getMatch_scorecard(HashMap hout, String requestMethod, String matchId, String vendor) throws IOException, InterruptedException, ParseException
	{
		String url =((Map)c.getConfig("Crickbuzz")).get("score_url")+matchId+"/"+"scard";
		this.log.info(url);		
		int counter=1;
		try 
		{ 		
			String val=c.apiCall(requestMethod, url, vendor);
			ArrayList al = new ArrayList();
			
			ArrayList bw_al = new ArrayList();
			ArrayList wk_al = new ArrayList();
			JSONObject js=(JSONObject)new JSONParser().parse(val);
			JSONArray ja=(JSONArray)js.get("scoreCard");
			
			for(Object sc:ja)
			{
				JSONObject scoreC=(JSONObject)sc;
				JSONObject batteam=(JSONObject)scoreC.get("batTeamDetails");
				JSONObject batsman=(JSONObject)batteam.get("batsmenData");
				System.out.println("Displaying getMatch_scorecard");
				System.out.println("S.NO||BATSMENID||BATSMEN NAME||RUNS||WICKET");
				for(Object k: batsman.keySet())
				{
					JSONObject batInfo = (JSONObject) batsman.get(k);
					
					this.log.info(counter+". "+batInfo.get("batId")+" - "+batInfo.get("batName")+" - "+batInfo.get("runs")+" - "+batInfo.get("wicketCode"));
					ArrayList inal = new ArrayList();
					inal.add(batInfo.get("batId"));
					inal.add(batInfo.get("batName"));
					inal.add(batInfo.get("runs"));
					inal.add(batInfo.get("wicketCode"));
					al.add(inal);
					counter++;
					System.out.println();
				}
			    ArrayList headings=(ArrayList)((Map)((Map)c.getConfig("Crickbuzz")).get("matchList")).get("get_matchScorecardHeadings1");				
				HashMap hm1 = new HashMap();
				hm1.put("headings", headings);
				hm1.put("data", al);
				hout.put(matchId+"_batsmen_details", hm1);		
				
				JSONObject bowlteam=(JSONObject)scoreC.get("bowlTeamDetails");
				JSONObject bowler=(JSONObject)bowlteam.get("bowlersData");
				System.out.println("S.NO||BOWLERID||BOWLER NAME||RUNS||WICKETS");
				for(Object b:bowler.keySet())
				{
					JSONObject bowlerInfo=(JSONObject)bowler.get(b);				
					this.log.info(counter+" . "+bowlerInfo.get("bowlerId")+" - "+bowlerInfo.get("bowlName")+" - "+bowlerInfo.get("runs")+" - "+bowlerInfo.get("wickets"));
					ArrayList alin2=new ArrayList();
					alin2.add(bowlerInfo.get("bowlerId"));
					alin2.add(bowlerInfo.get("bowlName"));
					alin2.add(bowlerInfo.get("runs"));
					alin2.add(bowlerInfo.get("wickets"));
					bw_al.add(alin2);
					counter++;
					System.out.println();
				}
				ArrayList headings1=(ArrayList)((Map)((Map)c.getConfig("Crickbuzz")).get("matchList")).get("get_matchScorecardHeadings2");
				HashMap hm_bw = new HashMap();
				hm_bw.put("headings", headings1);
				hm_bw.put("data", bw_al);
				hout.put("Bowler Details", hm_bw);
				
				JSONObject scoreD=(JSONObject)scoreC.get("scoreDetails");
				System.out.println("Score Details : RUNS||WICKETS||OVERS");
				System.out.println(scoreD.get("runs")+" - "+scoreD.get("wickets")+" - "+scoreD.get("overs"));
				
				JSONObject wick=(JSONObject)scoreC.get("wicketsData");
				System.out.println("Wickets details : "+" S.NO||BATSMAN NAME||WICKET NUM||WKT OVER");
				ArrayList in_wk = new ArrayList();
				for(Object w:wick.keySet())
				{
					JSONObject wickInfo=(JSONObject)wick.get(w);
					this.log.info(counter+" . "+wickInfo.get("batName")+" - "+wickInfo.get("wktNbr")+" - "+wickInfo.get("wktOver"));
					in_wk.add(wickInfo.get("batName"));
					in_wk.add(wickInfo.get("wktNbr"));
					in_wk.add(wickInfo.get("wktOver"));
					wk_al.add(in_wk);
					counter++;
				}
				ArrayList headings2=(ArrayList)((Map)((Map)c.getConfig("Crickbuzz")).get("matchList")).get("get_matchScorecardHeadings3");
				HashMap hm_wk = new HashMap();
				hm_wk.put("headings", headings2);
				hm_wk.put("data", wk_al);
				hout.put("Wicket Details", hm_wk);
			}
		}
		catch (Exception e) 
		{
			System.out.println("No data available at this moment.");
			this.log.error("No data available at this moment.");
		}
	
	}	
}
	
	