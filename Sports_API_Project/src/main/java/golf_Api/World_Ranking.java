package golf_Api;
import engine.CommonForAll;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class World_Ranking 
{
	public Logger log;
	public World_Ranking()
	{
		this.log=Logger.getLogger(World_Ranking.class.getName());
	}
	CommonForAll c = new CommonForAll();
	public void get_WorldRanking(HashMap hout, String requestMethod, String vendor) throws IOException, InterruptedException, ParseException
	{
		String url =(String) ((Map)c.getConfig("GolfLeaderBoard")).get("world_ranking_url");
		this.log.info("URL = "+url+"\n");
		System.out.println("URL = "+url+"\n");
		String val=c.apiCall(requestMethod, url, vendor);
		
		JSONObject js= (JSONObject)new JSONParser().parse(val);
		ArrayList al= new ArrayList();
		int count=1;
		
		JSONObject meta=(JSONObject)js.get("meta");
		this.log.info(meta.get("description"));
		System.out.println(meta.get("description"));
		
		JSONObject re=(JSONObject)js.get("results");		
		JSONArray ran=(JSONArray)re.get("rankings");
		
		System.out.println("Displaying get_WorldRanking");
		System.out.println("S.NO || PLAYER NAME ||PLAYER ID || TOTAL POINTS || POSITION");
		
		for(Object reIn:ran)
		{
			JSONObject rank=(JSONObject)reIn;
			System.out.println(count+"  - "+rank.get("player_name")+" - "+rank.get("player_id")+" - "+rank.get("total_points")+" - "+rank.get("position"));
			ArrayList alin=new ArrayList();
			alin.add(rank.get("player_name"));
			alin.add(rank.get("player_id"));
			alin.add(rank.get("total_points"));
			alin.add(rank.get("position"));
			al.add(alin);
			count++;		
		}
		ArrayList headings=(ArrayList)((Map)c.getConfig("GolfLeaderBoard")).get("WorldRankings");	
		HashMap hm = new HashMap();
		hm.put("headings", headings);
		hm.put("data", al);
		hout.put("_worldRankings", hm);		
		
	}
}
