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

public class Trainers_Winrate 
{
	public Logger log;
	public Trainers_Winrate()
	{
		this.log= Logger.getLogger(Trainers_Winrate.class.getName());
	}
	CommonForAll hc = new CommonForAll();
	public void get_TrainersWinrate(HashMap hout, String requestMethod, String Tdays, String vendor) throws IOException, InterruptedException, ParseException
	{
		String url = ((String)((Map)hc.getConfig("Horse")).get("trainer_url"))+Tdays;
		this.log.info("URL = : "+url);
		String val =hc.apiCall(requestMethod, url, vendor);
		ArrayList al = new ArrayList();
				
		JSONObject js = (JSONObject)new JSONParser().parse(val);
		int count=1;
		this.log.info(js.get("info")+"\n");
		this.log.info("S.NO||TRAINER||WINS||RUNS||WIN-RATE"+"\n");
		JSONArray ja=(JSONArray)js.get("win_rate");
		for(Object w:ja)
		{
			JSONObject win_r =(JSONObject)w;
			this.log.info(count+" . "+win_r.get("trainer")+" - "+win_r.get("wins")+" - "+win_r.get("runs")+" - "+win_r.get("win-rate"));
			ArrayList alin = new ArrayList();
			alin.add(win_r.get("trainer"));
			alin.add(win_r.get("wins"));
			alin.add(win_r.get("runs"));
			alin.add(win_r.get("win-rate"));
			al.add(alin);
			count++;
		}
		ArrayList headings=(ArrayList)((Map)hc.getConfig("Horse")).get("TrainersWinRate");
		HashMap hm= new HashMap();
		hm.put("headings", headings);
		hm.put("data", al);
		hout.put(Tdays+"_TrainersWinRate", hm);		
		
	}
}
