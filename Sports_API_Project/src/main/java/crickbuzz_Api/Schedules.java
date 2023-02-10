package crickbuzz_Api;
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

public class Schedules 
{
	CommonForAll c;
	public Logger log;
	public Schedules(CommonForAll cfa)
	{
		this.log=Logger.getLogger(Schedules.class.getName());
		this.c = cfa;
	}
	
	@SuppressWarnings("unchecked")
	public void getSchedules_list(HashMap hout, String requestMethod, String format, String vendor) throws IOException, InterruptedException, ParseException
	{		
		String url=((String)((Map)c.getConfig("Crickbuzz")).get("schedule_url"))+format;
		this.log.info("URL= : "+url);	
		System.out.println("URL= : "+url);
		String val=c.apiCall(requestMethod, url, vendor);		
		ArrayList al = new ArrayList();
		try 
		{
			JSONObject js=(JSONObject)new JSONParser().parse(val);		
			JSONArray ja=(JSONArray)js.get("matchScheduleMap");
			int count=1;	
			System.out.println("Displaying getSchedules_list");
			System.out.println("S.NO||SERIES ID ||DATE||SERIES NAME");
			for(Object a:ja)
			{
				JSONObject matchsche=(JSONObject)a;
				
				try {
					JSONObject Schedulewrapper=(JSONObject)matchsche.get("scheduleAdWrapper");
					JSONArray matchshe=(JSONArray)Schedulewrapper.get("matchScheduleList");
					for(Object m:matchshe)
					{
						JSONObject match=(JSONObject)m;
						this.log.info(count+" . "+match.get("seriesId")+" - "+Schedulewrapper.get("date")+" - "+match.get("seriesName"));
						ArrayList inal = new ArrayList();
						inal.add(match.get("seriesId"));
						inal.add(Schedulewrapper.get("date"));
						inal.add(match.get("seriesName"));
						al.add(inal);
						count++;				
					}
					
				}
				catch (Exception e)
				{
					System.out.println("No data inside this field");
					this.log.error("No data inside this field");
				}
			}
			
			ArrayList headings=(ArrayList)((Map)c.getConfig("Crickbuzz")).get("ScheduleHeadings");
			HashMap hm = new HashMap();
			hm.put("headings", headings);
			hm.put("data", al);
			hout.put("Schedules", hm);
			
		}		
		catch (java.lang.NullPointerException e) 
		{
			System.out.println("You have exceeded the DAILY quota for Requests on your current plan, BASIC. Upgrade your plan at https://rapidapi.com/cricketapilive/api/cricbuzz-cricket");
			this.log.error("You have exceeded the DAILY quota for Requests on your current plan, BASIC. Upgrade your plan at https://rapidapi.com/cricketapilive/api/cricbuzz-cricket");
		}
		
		
	}
}
