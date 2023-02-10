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

public class PGA_Rankings 
{
	public Logger log;
	public PGA_Rankings()
	{
		this.log=Logger.getLogger(PGA_Rankings.class.getName());
	}
	CommonForAll c= new CommonForAll();
	public String get_PgaRankings(HashMap hout, String requestMethod, String season, String vendor) throws IOException, InterruptedException, ParseException
	{
		String url=((String) ((Map)c.getConfig("GolfLeaderBoard")).get("pga_ranking_url"))+season;
		this.log.info("URL= : "+url);
		System.out.println("URL= : "+url);
		String val= c.apiCall(requestMethod, url, vendor);
		JSONObject js= (JSONObject)new JSONParser().parse(val);
		
		ArrayList al = new ArrayList();			
		LinkedHashMap lin= new LinkedHashMap();
		int count=1;
		
		JSONObject meta= (JSONObject)js.get("meta");
		JSONObject res=(JSONObject)js.get("results");
		
		JSONArray rank=(JSONArray)res.get("rankings");
		for(Object r:rank)
		{
			JSONObject ranking=(JSONObject)r;
			lin.put(String.valueOf(count), ranking.get("player_id")+" - "+meta.get("title")+" - "+ranking.get("first_name")+" - "+ranking.get("last_name")+" - "+ranking.get("projected_points")+" - "+ranking.get("current_rank"));
			
			ArrayList alin= new ArrayList();
			alin.add(ranking.get("player_id"));
			alin.add(meta.get("title"));
			alin.add(ranking.get("first_name"));
			alin.add(ranking.get("last_name"));
			alin.add(ranking.get("projected_points"));
			alin.add(ranking.get("current_rank"));
			
			al.add(alin);			
			count++;			
		}
		ArrayList headings=(ArrayList)((Map)c.getConfig("GolfLeaderBoard")).get("PgaRankings");
		HashMap h = new HashMap();
		h.put("headings", headings);
		h.put("data", al);		
		hout.put(season+"PGA_ranking", h);		
		
		System.out.println("Displaying get_PgaRankings");
		System.out.println("S.NO || PLAYER ID || TITLE ||FIRST NAME || LAST NAME || PROJECT POINTS || CURRENT RANK");
		for(Object out:lin.keySet())
		{
			System.out.println(out+"."+lin.get(out));
		}
		String play=c.exceptions(lin,"Select Player id Based on S.NO : ");
		String[] player_id= lin.get(play).toString().split(" - ");
		this.log.info("You selected player name is = "+player_id[2]+player_id[3]);
		System.out.println("You selected player name is = "+player_id[2]+player_id[3]);
		System.out.println("You selected player_id is = "+player_id[0]);
		this.log.info("You selected player_id is = "+player_id[0]);
		return player_id[0];		
		
	}	
}

