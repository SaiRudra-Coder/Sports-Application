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

public class Jockeys_WinRates 
{
	public Logger log;
	public Jockeys_WinRates()
	{
		this.log= Logger.getLogger(Jockeys_WinRates.class.getName());
	}
	CommonForAll hc = new CommonForAll();
	public void get_JockeysWinRates(HashMap hout, String requestMethod, String days, String vendor) throws IOException, InterruptedException, ParseException
	{
		String url =((String)((Map)hc.getConfig("Horse")).get("jockeys_url"))+days;
		this.log.info("URL =  : "+url);
		String val =hc.apiCall(requestMethod, url, vendor);
		JSONObject js= (JSONObject)new JSONParser().parse(val);
		
		ArrayList al = new ArrayList();		
		int count=1;
		
		this.log.info(js.get("info")+"\n");		
		this.log.info("S.NO||JOCKEY||WINS||RUNS||WIN-RATE");
		JSONArray ja =(JSONArray)js.get("win_rate");		
		for(Object in:ja)
		{
			JSONObject win =(JSONObject)in;
			this.log.info(count+" . "+win.get("jockey")+" - "+win.get("wins")+" - "+win.get("runs")+" - "+win.get("win-rate"));
			ArrayList alin= new ArrayList();
			alin.add(win.get("jockey"));  alin.add(win.get("wins"));  alin.add(win.get("runs"));  alin.add(win.get("win-rate")); 
			al.add(alin); 
			count++;
		}
		ArrayList headings=(ArrayList)((Map)hc.getConfig("Horse")).get("JockeyWinRate");
		HashMap hm = new HashMap();
		hm.put("headings", headings);
		hm.put("data", al);
		hout.put(days+"_jockeyWinrate", hm);		
		
	}
}
