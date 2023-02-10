package horse_Api;
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

public class Results 
{ 
	public Logger log;
	public Results()
	{
		this.log=Logger.getLogger(Results.class.getName());
	}
	CommonForAll hc = new CommonForAll();
	public void get_Results(HashMap hout, String requestMethod, String vendor) throws IOException, InterruptedException, ParseException
	{
		String url = (String)((Map)hc.getConfig("Horse")).get("results_url");
		this.log.info("URL = : "+url);
		String val =hc.apiCall(requestMethod, url, vendor);
		ArrayList al = new ArrayList();
		
		JSONArray js=(JSONArray)new JSONParser().parse(val);
		int count=1;
		
		this.log.info("S.NO||RACE ID||COURSE||TITLE||DISTANCE||PRIZE");
		for(Object re_info:js)
		{
			JSONObject res =(JSONObject)re_info;
			this.log.info(count+"."+res.get("id_race")+" - "+res.get("course")+" - "+res.get("title")+" - "+res.get("distance")+" - "+res.get("prize"));
			ArrayList alin= new ArrayList();
			alin.add(res.get("id_race"));  alin.add(res.get("course"));   alin.add(res.get("title"));  alin.add(res.get("distance"));  alin.add(res.get("prize"));  
			al.add(alin);			
			count++;			
		}
		ArrayList headings=(ArrayList)((Map)((Map)hc.getConfig("Horse")).get("Results")).get("getResults");
		HashMap hm = new HashMap();
		hm.put("headings", headings);
		hm.put("data", al);
		hout.put("_Results", hm);
		
		
	}
}
