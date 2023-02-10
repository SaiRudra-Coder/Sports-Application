package golf_Api;
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

public class Tours 
{
	public Logger log ;
	public Tours()
	{
		this.log=Logger.getLogger(Tours.class.getName());
	}
	CommonForAll c= new CommonForAll();
	public String get_Tours(HashMap hout, String requestMethod, String vendor) throws IOException, InterruptedException, ParseException
	{
		String url=(String)((Map)c.getConfig("GolfLeaderBoard")).get("tours_url");
		this.log.info("URL = : "+url+"\n");
		System.out.println("URL = : "+url+"\n");
		
		String val=c.apiCall(requestMethod, url, vendor);		
		JSONObject js=(JSONObject)new JSONParser().parse(val);
		
		ArrayList al = new ArrayList();
		
		LinkedHashMap lp = new LinkedHashMap();
		int count=1;
		
		JSONObject meta=(JSONObject)js.get("meta");
		this.log.info(meta.get("description"));
		System.out.println(meta.get("description"));
		
		JSONArray ja=(JSONArray)js.get("results");
		for(Object resOut:ja)
		{
			JSONObject results=(JSONObject)resOut;
			lp.put(String.valueOf(count), results.get("season_id")+" - "+results.get("tour_id")+" - "+results.get("tour_name"));
			ArrayList alin = new ArrayList();
			alin.add(results.get("season_id"));
			alin.add(results.get("tour_id"));
			alin.add(results.get("tour_name"));
			al.add(alin);
			count++;
		}
		ArrayList headings=(ArrayList)((Map)((Map)c.getConfig("GolfLeaderBoard")).get("Tours")).get("getTours");
		HashMap h = new HashMap();
		h.put("headings", headings);
		h.put("data", al);
		hout.put("get_tours", h);		
		
		System.out.println("Displaying get_Tours");
		System.out.println("S.NO || SEASON ID || TOUR ID || TOUR NAME");
		for(Object r :lp.keySet())
		{
			System.out.println(r+". "+lp.get(r));
		}
		
		String sea=c.exceptions(lp,"Select tour id and Season id based on  S.NO : ");
		String[] tourSeason_id= lp.get(sea).toString().split(" - ");
		System.out.println("You selected Tour name is = "+tourSeason_id[2]);
		this.log.info("You selected Tour name is = "+tourSeason_id[2]);
		System.out.println("You selected Tour_id and season_id is = "+tourSeason_id[1]+"  And  "+tourSeason_id[0]);
		this.log.info("You selected Tour_id and season_id is = "+tourSeason_id[1]+"  And  "+tourSeason_id[0]);
		return tourSeason_id[1]+"/"+tourSeason_id[0];		
    } 
	
	public void get_tourRanking(HashMap hout, String requestMethod, String tourSeason_id, String vendor) throws IOException, InterruptedException, ParseException
	{
		String url = ((String)((Map)c.getConfig("GolfLeaderBoard")).get("tours_ran_url"))+tourSeason_id;
		this.log.info("URL = : "+url);
		System.out.println("URL = : "+url);
		ArrayList al = new ArrayList();
		String val =c.apiCall(requestMethod, url, vendor);
		JSONObject js =(JSONObject)new JSONParser().parse(val);
		int count=1;
		
		JSONObject meta=(JSONObject)js.get("meta");
		this.log.info(meta.get("title"));
		System.out.println(meta.get("title"));
		System.out.println("Displaying get_tourRanking");
		System.out.println("S.NO||PLAYER ID||PLAYER NAME||NUM EVENTS||NUM WINS||POINTS||POSITION");
		JSONObject re =(JSONObject)js.get("results");
		JSONArray ja=(JSONArray)re.get("rankings");
		for(Object ra:ja)
		{
			JSONObject rank = (JSONObject)ra;
			System.out.println(count+"."+rank.get("player_id")+" - "+rank.get("player_name")+" - "+rank.get("num_events")+" - "+rank.get("num_wins")+" - "+rank.get("points")+" - "+rank.get("position"));
			ArrayList alin=new ArrayList();
			alin.add(rank.get("player_id"));
			alin.add(rank.get("player_name"));
			alin.add(rank.get("num_events"));
			alin.add(rank.get("num_wins"));
			alin.add(rank.get("points"));
			alin.add(rank.get("position"));
			al.add(alin);
			
			count++;			
		}
		ArrayList headings=(ArrayList)((Map)((Map)c.getConfig("GolfLeaderBoard")).get("Tours")).get("getTourRank");
		HashMap h =new HashMap();
		h.put("headings", headings);
		h.put("data", al);
		hout.put(tourSeason_id+"Tour_Ranking", h);		
	}
	
	public String get_Fixtures(HashMap hout, String requestMethod, String tourSeason_id, String vendor) throws IOException, InterruptedException, ParseException
	{
		String url =((String)((Map)c.getConfig("GolfLeaderBoard")).get("tours_fix_url"))+tourSeason_id;
		this.log.info("URL = : "+url);
		System.out.println("URL = : "+url);
		
		String val=c.apiCall(requestMethod, url, vendor);
		JSONObject js=(JSONObject)new JSONParser().parse(val);
		
		ArrayList al = new ArrayList();				
		LinkedHashMap lp = new LinkedHashMap();
		int count=1;
		
		JSONObject meta=(JSONObject)js.get("meta");
		this.log.info(meta.get("title"));
		System.out.println(meta.get("title"));
		
		JSONArray ja =(JSONArray)js.get("results");
		String toment_id=null;
		if(ja.size()>0)
		{
			for(Object re:ja)
			{
				JSONObject res =(JSONObject)re;
				lp.put(String.valueOf(count), res.get("id")+" - "+res.get("type")+" - "+res.get("name")+" - "+res.get("country"));
				ArrayList alin = new ArrayList();
				alin.add(res.get("id"));
				alin.add(res.get("type"));
				alin.add(res.get("name"));
				alin.add(res.get("country"));
				al.add(alin);			
				count++;		
			}
			ArrayList headings=(ArrayList)((Map)((Map)c.getConfig("GolfLeaderBoard")).get("Tours")).get("getFixtures");
			HashMap h = new HashMap();
			h.put("headings", headings);
			h.put("data", al);
			hout.put(tourSeason_id.replace("/", "_")+"get_Fixtures", h);	
			
			System.out.println("Displaying get_Fixtures");
			System.out.println("S.N  || TOURNAMENT ID || TOURNAMENT TYPE || NAME || COUNTRY");
			for(Object k:lp.keySet())
			{
				System.out.println(k+"."+lp.get(k));
			}

			String sea=c.exceptions(lp,"Select Tournament id  based on  S.NO : ");
			String[] tment_id= lp.get(sea).toString().split(" - ");
			System.out.println("You selected Tournment  name is = "+tment_id[2]);
			this.log.info("You selected Tournment  name is = "+tment_id[2]);
			toment_id=tment_id[0];		
		}
		else 
		{
			System.out.println("No data inside this field.");
			this.log.error("No data inside this field.");
		}
		return toment_id;
		
	}
	
	public void get_EntryList(HashMap hout, String requestMethod, String tment_id, String vendor) throws IOException, InterruptedException, ParseException
	{
		String url =((String)((Map)c.getConfig("GolfLeaderBoard")).get("tours_entryList_url"))+tment_id;
		this.log.info("URL = :"+url);
		System.out.println("URL = :"+url);
		String val= c.apiCall(requestMethod, url, vendor);
		JSONObject js=(JSONObject)new JSONParser().parse(val);
		
		ArrayList al = new ArrayList();
		
		int count=1;		
		JSONObject meta =(JSONObject)js.get("meta");
		this.log.info(meta.get("title"));
		
		System.out.println("Displaying get_EntryList");
		System.out.println("S.NO||PLAYER ID||FIRST NAME||LAST NAME||COUNTRY");
		
		JSONObject res =(JSONObject)js.get("results");
		JSONArray ja =(JSONArray)res.get("entry_list");
		for(Object inres: ja)
		{
			JSONObject etry_list=(JSONObject)inres;
			System.out.println(count+" . "+etry_list.get("player_id")+" - "+etry_list.get("first_name")+" - "+etry_list.get("last_name")+" - "+etry_list.get("country"));
			ArrayList alin= new ArrayList();
			alin.add(etry_list.get("player_id")); alin.add(etry_list.get("first_name")); alin.add(etry_list.get("last_name")); alin.add(etry_list.get("country"));
			al.add(alin);
			count++;			
		}
		ArrayList headings=(ArrayList)((Map)((Map)c.getConfig("GolfLeaderBoard")).get("Tours")).get("getEntryList");
		HashMap h= new HashMap();
		h.put("headings", headings);
		h.put("data", al);
		this.log.info("Tid : "+tment_id);
		hout.put(tment_id+"EntryList", h);	
	}
	
	public void get_Leaderboard(HashMap hout, String requestMethod, String tment_id, String vendor) throws IOException, InterruptedException, ParseException
	{
		String url =((String)((Map)c.getConfig("GolfLeaderBoard")).get("tours_leaderboard_url"))+tment_id;
		this.log.info("URL = : "+url);
		System.out.println("URL = : "+url);
		
		String val =c.apiCall(requestMethod, url, vendor);
		JSONObject js=(JSONObject)new JSONParser().parse(val);
		
		ArrayList al = new ArrayList();			
		int count =1;		
		
		JSONObject  meta= (JSONObject)js.get("meta");
		this.log.info(meta.get("description"));
		System.out.println();
		System.out.println(meta.get("description"));
		
		JSONObject res=(JSONObject)js.get("results");
		JSONArray ja= (JSONArray)res.get("leaderboard");
		
		System.out.println("Displaying get_Leaderboard");
		System.out.println("S.NO||PLAYER ID||FIRST NAME||LAST NAME||COUNTRY||HOLES PLAYED||CURRENT ROUND||STATUS||STROKES||ROUND NUM||COURSE NUM||POSITION ROUND||STROKES||POSITION");
		
		for(Object resIn:ja)
		{
			JSONObject lead =(JSONObject)resIn;
			JSONArray ja1 =(JSONArray)lead.get("rounds");
			for(Object ronIn :ja1)
			{
				JSONObject round=(JSONObject)ronIn;
				System.out.println(count+" . "+lead.get("player_id")+" "+lead.get("first_name")+" "+lead.get("last_name")+" - "+lead.get("country")+" - "+lead.get("holes_played")+" - "+lead.get("current_round")+" - "+lead.get("status")+" - "+lead.get("strokes")
				+" - "+round.get("round_number")+" - "+round.get("course_number")+" - "+round.get("position_round")+" - "+round.get("strokes")+" - "+lead.get("position"));
				ArrayList alin= new ArrayList();
				alin.add(lead.get("player_id")); alin.add(lead.get("first_name")); alin.add(lead.get("last_name")); alin.add(lead.get("country")); alin.add(lead.get("holes_played")); alin.add(lead.get("current_round")); alin.add(lead.get("status")); alin.add(lead.get("strokes"));
				alin.add(round.get("round_number")); alin.add(round.get("course_number")); alin.add(round.get("position_round")); alin.add(round.get("strokes")); alin.add(lead.get("position"));
				al.add(alin);
				count++;
			}		
		}
		ArrayList headings=(ArrayList)((Map)((Map)c.getConfig("GolfLeaderBoard")).get("Tours")).get("getLeaderboard");
		HashMap h = new HashMap();
		h.put("headings", headings);
		h.put("data", al);
		hout.put(tment_id+"LeaderBoard", h);
	}
	
	public void get_PgaScoreCard(HashMap hout, String requestMethod, String tment_id, String player_id, String vendor) throws IOException, InterruptedException, ParseException
	{
		String url =((String)((Map)c.getConfig("GolfLeaderBoard")).get("tours_pgascore_url"))+tment_id+"/"+player_id;
		this.log.info("URL = : "+url);
		System.out.println("URL = : "+url);
		String val=c.apiCall(requestMethod, url, vendor);
		
		JSONObject js=(JSONObject)new JSONParser().parse(val);
		
		ArrayList al = new ArrayList();				
		int count=1;
		
		JSONObject meta=(JSONObject)js.get("meta");
		this.log.info(meta.get("description")+"\n");
		
		JSONObject re=(JSONObject)js.get("results");
		JSONObject tourn=(JSONObject)re.get("tournament");
		System.out.println("Displaying get_PgaScoreCard");
		System.out.println("TOURNAMENT DETAILS : ID || NAME || COUNTRY ||COURSE"+"\n");
		System.out.println(tourn.get("id")+" - "+tourn.get("name")+" - "+tourn.get("country")+" - "+tourn.get("course")+"\n");
		ArrayList alin= new ArrayList();
		alin.add(tourn.get("id")); alin.add(tourn.get("name")); alin.add(tourn.get("country")); alin.add(tourn.get("course")); 
		al.add(alin);		
		
		JSONObject pla=(JSONObject)re.get("player");
		System.out.println("Displaying player details");
		System.out.println("PLAYER DETAILS :  ID || NAME || COUNTRY"+"\n");
		System.out.println(pla.get("id")+" - "+pla.get("name")+" - "+pla.get("country")+"\n");
		ArrayList alin2= new ArrayList();
		alin2.add(pla.get("id"));
		alin2.add(pla.get("name"));
		alin2.add(pla.get("country"));
		al.add(alin2);
		
		JSONObject rhb=(JSONObject)re.get("rounds_holes_breakdown");		
		try {
			JSONArray ja =(JSONArray)rhb.get("rounds");
			for(Object ro:ja)
			{
				JSONObject roun =(JSONObject)ro;
				this.log.info(roun.get("num"));
				System.out.println(roun.get("num"));
				JSONArray ja1 =(JSONArray)roun.get("courses");
				for(Object co :ja1)
				{
					JSONObject cou =(JSONObject)co;
					JSONArray ja3 =(JSONArray)cou.get("holes");
					for(Object ho: ja3)
					{
						JSONObject hol =(JSONObject)ho;
						System.out.println(count+" . "+roun.get("num")+" - "+cou.get("id")+" - "+cou.get("yards")+" - "+hol.get("par")+" - "+hol.get("hole_num")+" -"+hol.get("yards"));
						ArrayList alin3= new ArrayList();
						alin3.add(roun.get("num"));  alin3.add(cou.get("id"));  alin3.add(cou.get("yards"));  alin3.add(hol.get("par"));  alin3.add(hol.get("hole_num"));  alin3.add(hol.get("yards"));  
						al.add(alin3);  
						count++;
					}
				}
			}
		} 
		catch (Exception e) 
		{
			System.out.println("no content");
			this.log.error("no content");
		}
		ArrayList headings=(ArrayList)((Map)((Map)c.getConfig("GolfLeaderBoard")).get("Tours")).get("getPgaScoreCard");
		HashMap h = new HashMap();
		h.put("headings", headings);
		h.put("data", al);
		hout.put(tment_id+" "+player_id+"PgScoreCard", h);	
		
	}	
}
