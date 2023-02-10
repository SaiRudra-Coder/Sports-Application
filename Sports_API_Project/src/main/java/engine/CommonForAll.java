package engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.yaml.snakeyaml.Yaml;

public class CommonForAll 
{
	public static Logger log;
	public CommonForAll()
	{
		this.log= Logger.getLogger(CommonForAll.class.getName());
	}
	public String apiCall(String requestMethod, String url, String vendor) throws IOException, InterruptedException, ParseException
	{
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder(URI.create(url))
				.header("X-RapidAPI-Key", (String) ((Map) getConfig(vendor)).get("api_key"))
				.header("X-RapidAPI-Host", (String) ((Map) getConfig(vendor)).get("api_host"))
				.GET()
				.build();
		HttpResponse response=client.send(request, HttpResponse.BodyHandlers.ofString());
		if(response.statusCode()==200)
		{
			return (String) response.body();
		}
		else if(response.statusCode()==429)
		{			
			JSONObject js= (JSONObject)new JSONParser().parse((String) response.body());
			this.log.info(js.get("message"));
			System.exit(0);
		}
		else if(response.statusCode()==204)
		{
			System.out.println("No content inside");
			this.log.info("No content inside");
			System.exit(0);
		}
		return (String) response.body();
	}

	public String userOption(String output)
	{
		Scanner scan =new Scanner (System.in);
		System.out.println(output);
		this.log.info(output);
		return scan.nextLine();		
	}
	
	public String exceptions(HashMap hm, String output)
	{
		String user_ch=null;
		while(true)
		{
			user_ch =userOption(output);
			if(hm.get(user_ch.toLowerCase())==null)
			{
				System.out.println("Invaild user selections. please select again.");
				this.log.error("Invaild user selections. please select again.");
			}
			else
			{
				break;
			}
		}
		return user_ch;
	}
	
	public Object getConfig(String key) throws FileNotFoundException
	{		
		Map hm =null;		
		try 
		{
			File fl= new File("C:\\Users\\sai\\eclipse-workspace\\seleniumtesting\\Sports_API_Project\\src\\main\\resources\\Config.yaml");
			FileReader fr = new FileReader(fl);
			Yaml ym = new Yaml();
			hm=ym.load(fr);
		} 
		catch (FileNotFoundException e) 
		{
			System.out.println("Misplaced configuration file");
			this.log.info("Misplaced configuration file");
		}	
		return hm.get(key);		
	}
	
	public boolean exit_choice()
	{
		boolean flag = false;
		while(true)
		{
			String exit_val= userOption("Do you want continue [Yes/No]: ");
			if(exit_val.equalsIgnoreCase("yes")||(exit_val.equalsIgnoreCase("Y")))
			{
				flag = true;
				break;
			}
			else if(exit_val.equalsIgnoreCase("no")||(exit_val.equalsIgnoreCase("n")))
			{
				flag=false;
				break;
			}	
			
			else
			{
				System.out.println("Invaild selection. please select again");
				this.log.error("Invaild selection. please select again");
				continue;
			}
		}
		return flag;
	}
	
	public String typeMatch()
	{
		HashMap hm=new HashMap();
		hm.put("a", "international");
		hm.put("b", "league");
		hm.put("c", "domestic");
		hm.put("d", "women");
		for(Object s:hm.keySet())
		{
			System.out.println(s+"."+hm.get(s));
			this.log.info(s+"."+hm.get(s));
		}
		String user_input=exceptions(hm,"Choose your option:");
		System.out.println("Your Choosen Option is : "+hm.get(user_input.toLowerCase()));
		this.log.info("Your Choosen Option is : "+hm.get(user_input.toLowerCase()));
		String format=hm.get(user_input.toLowerCase()).toString();
		return format;		
	}
	
	public String typeCategory()
	{
		HashMap hm=new HashMap();
		hm.put("a", "batsmen");
		hm.put("b", "bowlers");
		hm.put("c", "allrounders");
		hm.put("d", "teams");
		for(Object s:hm.keySet())
		{
			System.out.println(s+"."+hm.get(s));
			this.log.info(s+"."+hm.get(s));
		}
		String user_input=exceptions(hm,"Choose Your Option");
		System.out.println("Your Choosen Option is : "+hm.get(user_input.toLowerCase()));
		this.log.info("Your Choosen Option is : "+hm.get(user_input.toLowerCase()));
		String category=hm.get(user_input.toLowerCase()).toString();
		return category;		
	}
	
	public String typeFormat()
	{
		HashMap hm=new HashMap();
		hm.put("a", "test");
		hm.put("b", "odi");
		hm.put("c", "t20");
		for(Object s:hm.keySet())
		{
			System.out.println(s+"."+hm.get(s));
			this.log.info(s+"."+hm.get(s));
		}
		String user_input=exceptions(hm, "Choose your option: ");
		System.out.println("Your Choosen Option is : "+hm.get(user_input.toLowerCase()));
		this.log.info("Your Choosen Option is : "+hm.get(user_input.toLowerCase()));
		String typeFormat=hm.get(user_input.toLowerCase()).toString();
		return typeFormat;
	}
	
	public  void multiSheetOptimize(HashMap result, String filename) throws IOException
	{
		XSSFWorkbook wb=new XSSFWorkbook();
		for(Object key: result.keySet())
		{
			// {"sheet1": {"headings": headings, "data": al}}
			XSSFSheet ws = wb.createSheet((String) key);
			HashMap sheet_info = (HashMap) result.get(key);
			ArrayList headings = (ArrayList) sheet_info.get("headings");
			ArrayList data = (ArrayList) sheet_info.get("data");
			int row_count = 0;
			Row first_row = ws.createRow(row_count);
			int cell_cnt = 0;
			for(Object header: headings)
			{
				Cell first_cell = first_row.createCell(cell_cnt);
				first_cell.setCellValue(header.toString());
				cell_cnt++;
			}
			row_count = 1;
			if(data!=null)
			{
				for(Object row_inf: data)
				{
					Row row = ws.createRow(row_count);
					int cell_count=0;
					for(Object cell_inf: (ArrayList)row_inf)
					{
						Cell cell = row.createCell(cell_count);
						try
						{
							cell.setCellValue(cell_inf.toString());
						}
						catch (java.lang.NullPointerException e)
						{
							cell.setCellValue("no info");
						}
						cell_count++;
					}
					row_count++;
				}
			}
		}
		FileOutputStream fout = new FileOutputStream(new File(filename));
		wb.write(fout);
		wb.close();
		System.out.println("File Created !!");
		this.log.info("File Created !!");		
	}
	
	
	
	
	
	
	
	
	
	

	

}
