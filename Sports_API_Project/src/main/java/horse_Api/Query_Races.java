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

public class Query_Races 
{
	public Logger log;
	public Query_Races()
	{
		this.log=Logger.getLogger(Query_Races.class.getName());
	}
	CommonForAll hc = new CommonForAll();
	public String get_QueryRaces(HashMap hout, String requestMethod, String vendor) throws IOException, InterruptedException, ParseException
	{
		String url=(String)((Map)hc.getConfig("Horse")).get("Query_race_url");
		this.log.info("URL = : "+url);
		
		String val = hc.apiCall(requestMethod, url, vendor);
		JSONObject js=(JSONObject)new JSONParser().parse(val);
		ArrayList al = new ArrayList();
				
		LinkedHashMap lin =new LinkedHashMap();
		int count=1;
		JSONObject sum=(JSONObject)js.get("summary");
		this.log.info("Summary : "+"total results= "+sum.get("total_results")+"  "+"total-pages= "+sum.get("total_pages")+"  "+"current-page= "+sum.get("current_page")+"\n");
		this.log.info("S.NO||RACE ID||NAME||COURSE||DISTANCE||CLASS");
		JSONArray ja =(JSONArray)js.get("races");
		for(Object ra:ja)
		{
			JSONObject race = (JSONObject)ra;
			lin.put(String.valueOf(count), race.get("id_race")+" - "+race.get("name")+" - "+race.get("course")+" - "+race.get("distance")+" - "+race.get("class"));
			ArrayList alin= new ArrayList();
			alin.add(race.get("id_race"));
			alin.add(race.get("name"));
			alin.add(race.get("course"));
			alin.add(race.get("distance"));
			alin.add(race.get("class"));
			al.add(alin);
			
			count++;		
		}
		ArrayList headings= (ArrayList)((Map)((Map)hc.getConfig("Horse")).get("Query")).get("getQueryRaces");
		HashMap hm = new HashMap();
		hm.put("headings", headings);
		hm.put("data", al);
		hout.put("_QueryRaces", hm);
		
		for(Object res:lin.keySet())
		{
			this.log.info(res+" . "+lin.get(res));
		}
		String option=hc.exceptions(lin,"select name based on s.no : ");
		String[] name=lin.get(option).toString().split(" - ");
		this.log.info("Your choosen name is : "+name[1]);
		this.log.info(name[1]);
		return name[1];		
	}
	
	public void get_QueryHorses(HashMap hout, String requestMethod, String name, String vendor) throws IOException, InterruptedException, ParseException
	{
		String url =((String)((Map)hc.getConfig("Horse")).get("Query_horse_url"))+name.split(" ")[0];
		this.log.info("URL = "+url);
		String val =hc.apiCall(requestMethod, url, vendor);
		int count=1;
		ArrayList al = new ArrayList();
		
		this.log.info("S.NO||HORSE ID||HORSE");
		JSONArray ja=(JSONArray)new JSONParser().parse(val);		
		for(Object re:ja)
		{
			JSONObject res =(JSONObject)re;
			this.log.info(count+" . "+res.get("id_horse")+" - "+res.get("horse"));
			ArrayList alin = new ArrayList();
			alin.add(res.get("id_horse"));
			alin.add(res.get("horse"));
			al.add(alin);
			count++;
		}
		ArrayList headings=(ArrayList)((Map)((Map)hc.getConfig("Horse")).get("Query")).get("getQueryHorses");
		HashMap hm = new HashMap();
		hm.put("headings", headings);
		hm.put("data", al);
		hout.put(name+"_QueryHorses", hm);			
		
	}	
}
