package engine;

import crickbuzz_Api.*;
import golf_Api.*;
import horse_Api.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.json.simple.parser.ParseException;

public class mainEngineForAll 
{
	CommonForAll cfa = null;
	public Logger log;
	public mainEngineForAll()
	{
		PropertyConfigurator pc= new PropertyConfigurator();
		pc.configure("src/main/resources/log4j.properties");
		this.log= Logger.getLogger(mainEngineForAll.class.getName());
		this.cfa = new CommonForAll();
	}
	static HashMap hout = new HashMap();
	static String requestMethod="GET";
	public void crickbuzz() throws IOException, InterruptedException, ParseException
	{
		System.out.println("Welcome to Cricbuzz-Api");
		this.log.info("Welcome to Cricbuzz-Api");
		while(true)
		{
			HashMap hm = new HashMap();
			hm.put("1", "Matches");
			hm.put("2", "Schedules");
			hm.put("3", "Series");
			hm.put("4", "Teams");
			hm.put("5", "Players");
			hm.put("6", "Stats");
			for(Object c :hm.keySet())
			{
				this.log.info(c+"."+hm.get(c));
				System.out.println(c+"."+hm.get(c));
			}
			String option=this.cfa.exceptions(hm, "Select your option: ");
			System.out.println("Your Selected Option is : "+hm.get(option));
			this.log.info("Your Selected Option is : "+hm.get(option));
			if(option.equals("1"))
			{				
				Matches m = new Matches(cfa);
				String matchId=m.getMatch_List(hout, requestMethod, "Crickbuzz");
				if(matchId!=null)
				{
					HashMap h= new HashMap();
					h.put("1", "Info");
					h.put("2", "Commentaries");
					h.put("3", "Scorecard");
					for(Object o:h.keySet())
					{
						System.out.println(o+"."+h.get(o));
						this.log.info(o+"."+h.get(o));
					}			
					String cho=this.cfa.exceptions(h, "Select your option : ");
					System.out.println("Your Selected Option is : "+h.get(cho));
					this.log.info("Your Selected Option is : "+h.get(cho));
					if(cho.equals("1"))
					{
						String teamId=m.getMatch_info(hout, requestMethod, matchId, "Crickbuzz");
						m.getMatch_team(hout, requestMethod, matchId, teamId, "Crickbuzz");
						this.cfa.multiSheetOptimize(hout, (String) ((Map) this.cfa.getConfig("Crickbuzz")).get("matches_excel"));
						boolean flag = this.cfa.exit_choice();
						if (flag == true)
						{
							continue;
						}
						else {
							System.out.println("Thanks for using crickbuzz api.");
							this.log.error("Thanks for using crickbuzz api.");
							break;
						}
					}
					else if(cho.equals("2"))
					{
						m.getMatch_commentaries(hout, requestMethod, matchId, "Crickbuzz");
						this.cfa.multiSheetOptimize(hout, (String) ((Map) this.cfa.getConfig("Crickbuzz")).get("matches_excel"));
						boolean flag = this.cfa.exit_choice();
						if (flag == true)
						{
							continue;
						}
						else {
							System.out.println("Thanks for using crickbuzz api.");
							this.log.error("Thanks for using crickbuzz api.");
							break;
						}
					}
					else if(cho.equals("3"))
					{
						m.getMatch_scorecard(hout, requestMethod, matchId, "Crickbuzz");
						this.cfa.multiSheetOptimize(hout, (String) ((Map) this.cfa.getConfig("Crickbuzz")).get("matches_excel"));						
						boolean flag = this.cfa.exit_choice();
						if (flag == true)
						{
							continue;
						}
						else {
							System.out.println("Thanks for using crickbuzz api.");
							this.log.info("Thanks for using crickbuzz api.");
							break;
						}
					}										
				}
				boolean flag = this.cfa.exit_choice();
				if (flag == true)
				{
					continue;
				}
				else {
					System.out.println("Thanks for using crickbuzz api.");
					this.log.error("Thanks for using crickbuzz api.");
					break;
				}				
			}
			
			else if (option.equals("2"))
			{
				Schedules sc= new Schedules(cfa);
				String format=this.cfa.typeMatch();
				sc.getSchedules_list(hout, requestMethod, format, "Crickbuzz");					
				this.cfa.multiSheetOptimize(hout, (String) ((Map)this.cfa.getConfig("Crickbuzz")).get("Schedule_excel"));
				boolean flag = this.cfa.exit_choice();
				if (flag == true)
				{
					continue;
				}
				else {
					System.out.println("Thanks for using crickbuzz api.");
					this.log.error("Thanks for using crickbuzz api.");
					break;
				}
			}
			
			else if(option.equals("3"))
			{
				Series se=new Series(cfa);
				String format=this.cfa.typeMatch();
				String SeriesId=se.getSeries(hout, requestMethod, format, "Crickbuzz");
				if(SeriesId!=null)
				{
					HashMap hm1= new HashMap();
					hm1.put("a", "Matches");
					hm1.put("b", "Squads");
					hm1.put("c", "Points-table");
					hm1.put("d", "Stats-Filters");
					hm1.put("e", "Stats");
					for(Object s:hm1.keySet())
					{
						System.out.println(s+"."+hm1.get(s));
						this.log.info(s+"."+hm1.get(s));
					}
					String ch=this.cfa.exceptions(hm1,"choose your option: ");
					System.out.println("Your Selected Option is : "+hm1.get(ch.toLowerCase()));
					this.log.info("Your Selected Option is : "+hm1.get(ch.toLowerCase()));
					if(ch.equalsIgnoreCase("a"))
					{
						se.get_Matches(hout, requestMethod, SeriesId, "Crickbuzz");
						this.cfa.multiSheetOptimize(hout, (String) ((Map)this.cfa.getConfig("Crickbuzz")).get("Series_excel"));
						boolean flag = this.cfa.exit_choice();
						if (flag == true)
						{
							continue;
						}
						else {
							System.out.println("Thanks for using crickbuzz api.");
							this.log.error("Thanks for using crickbuzz api.");
							break;
						}
					}
					else if (ch.equalsIgnoreCase("b"))
					{
						String squadID=se.getSquad(hout, requestMethod, SeriesId, "Crickbuzz");
						se.get_Players(hout, requestMethod, SeriesId, squadID, "Crickbuzz");
						this.cfa.multiSheetOptimize(hout, (String) ((Map)this.cfa.getConfig("Crickbuzz")).get("Series_excel"));
						boolean flag = this.cfa.exit_choice();
						if (flag == true)
						{
							continue;
						}
						else {
							System.out.println("Thanks for using crickbuzz api.");
							this.log.error("Thanks for using crickbuzz api.");
							break;
						}
					}
					else if(ch.equalsIgnoreCase("c"))
					{
						se.get_PointaTable(hout, requestMethod, SeriesId, "Crickbuzz");
						this.cfa.multiSheetOptimize(hout, (String) ((Map)this.cfa.getConfig("Crickbuzz")).get("Series_excel"));
						boolean flag = this.cfa.exit_choice();
						if (flag == true)
						{
							continue;
						}
						else {
							System.out.println("Thanks for using crickbuzz api.");
							this.log.error("Thanks for using crickbuzz api.");
							break;
						}
					}
					else if(ch.equalsIgnoreCase("d"))
					{
						String value=se.stat_filters(hout, requestMethod, SeriesId, "Crickbuzz");
						se.stats(hout, requestMethod, SeriesId, value, "Crickbuzz");
						this.cfa.multiSheetOptimize(hout, (String) ((Map)this.cfa.getConfig("Crickbuzz")).get("Series_excel"));
						boolean flag = this.cfa.exit_choice();
						if (flag == true)
						{
							continue;
						}
						else {
							System.out.println("Thanks for using crickbuzz api.");
							this.log.error("Thanks for using crickbuzz api.");
							break;
						}
					}
					else if(ch.equalsIgnoreCase("e"))
					{
						String value=se.stat_filters(hout, requestMethod, SeriesId, "Crickbuzz");
						se.stats(hout, requestMethod, SeriesId, value, "Crickbuzz");
						this.cfa.multiSheetOptimize(hout, (String) ((Map)this.cfa.getConfig("Crickbuzz")).get("Series_excel"));
						boolean flag = this.cfa.exit_choice();
						if (flag == true)
						{
							continue;
						}
						else {
							System.out.println("Thanks for using crickbuzz api.");
							this.log.error("Thanks for using crickbuzz api.");
							break;
						}
					}
					
				}
				boolean flag = this.cfa.exit_choice();
				if (flag == true)
				{
					continue;
				}
				else {
					System.out.println("Thanks for using crickbuzz api.");
					this.log.error("Thanks for using crickbuzz api.");
					break;
				}
			}
			
			else if (option.equals("4"))
			{
				Teams t= new Teams(cfa);
				String type=this.cfa.typeMatch();
				String teamId=t.teams_list(hout,requestMethod, type, "Crickbuzz");
				if(teamId!=null)
				{
					HashMap hmp=new HashMap();
					hmp.put("a", "Schedules");
					hmp.put("b", "Results");
					hmp.put("c", "Players");
					for(Object sk:hmp.keySet())
					{
						System.out.println(sk+"."+hmp.get(sk));
						this.log.info(sk+"."+hmp.get(sk));
					}
					String choi=this.cfa.exceptions(hmp,"Select your Option: ");
					System.out.println("Your Selected Option is : "+hmp.get(choi.toLowerCase()));
					this.log.info("Your Selected Option is : "+hmp.get(choi.toLowerCase()));
					
					if(choi.equalsIgnoreCase("a"))
					{
						t.team_schedule(hout, requestMethod, teamId, "Crickbuzz");
						this.cfa.multiSheetOptimize(hout, (String) ((Map)this.cfa.getConfig("Crickbuzz")).get("Teams_excel"));
						boolean flag = this.cfa.exit_choice();
						if (flag == true)
						{
							continue;
						}
						else {
							System.out.println("Thanks for using crickbuzz api.");
							this.log.error("Thanks for using crickbuzz api.");
							break;
						}
					}
					else if (choi.equalsIgnoreCase("b"))
					{
						t.team_results(hout, requestMethod, teamId, "Crickbuzz");
						this.cfa.multiSheetOptimize(hout, (String) ((Map)this.cfa.getConfig("Crickbuzz")).get("Teams_excel"));
						boolean flag = this.cfa.exit_choice();
						if (flag == true)
						{
							continue;
						}
						else {
							System.out.println("Thanks for using crickbuzz api.");
							this.log.error("Thanks for using crickbuzz api.");
							break;
						}
					}
					else if (choi.equalsIgnoreCase("c"))
					{
						t.team_players(hout, requestMethod, teamId, "Crickbuzz");
						this.cfa.multiSheetOptimize(hout, (String) ((Map)this.cfa.getConfig("Crickbuzz")).get("Teams_excel"));
						boolean flag = this.cfa.exit_choice();
						if (flag == true)
						{
							continue;
						}
						else {
							System.out.println("Thanks for using crickbuzz api.");
							this.log.error("Thanks for using crickbuzz api.");
							break;
						}						
					}					
				}				
				boolean flag = this.cfa.exit_choice();
				if (flag == true)
				{
					continue;
				}
				else {
					System.out.println("Thanks for using crickbuzz api.");
					this.log.error("Thanks for using crickbuzz api.");
					break;
				}				
			}
			
			else if (option.equals("5"))
			{
				Players p = new Players(cfa);
				String playerId=p.palyers_list(hout, requestMethod, "Crickbuzz");
				this.cfa.multiSheetOptimize(hout, (String) ((Map)this.cfa.getConfig("Crickbuzz")).get("Players_excel"));
				if(playerId!=null)
				{
					HashMap hs= new HashMap();
					hs.put("a", "Career");
					hs.put("b", "Bowling");
					hs.put("c", "Batting");
					for(Object pla:hs.keySet())
					{
						System.out.println(pla+"."+hs.get(pla));
						this.log.info(pla+"."+hs.get(pla));
					}
					String choie=this.cfa.exceptions(hs, "Select Your Option");
					System.out.println("Your Selected Option is : "+hs.get(choie.toLowerCase()));
					this.log.info("Your Selected Option is : "+hs.get(choie.toLowerCase()));
					if(choie.equalsIgnoreCase("a"))
					{
						p.players_Career(hout, requestMethod, playerId, "Crickbuzz");
						this.cfa.multiSheetOptimize(hout, (String) ((Map)this.cfa.getConfig("Crickbuzz")).get("Players_excel"));
						boolean flag = this.cfa.exit_choice();
						if (flag == true)
						{
							continue;
						}
						else {
							System.out.println("Thanks for using crickbuzz api.");
							this.log.error("Thanks for using crickbuzz api.");
							break;
						}						
					}
					else if (choie.equalsIgnoreCase("b"))
					{
						p.player_bowling(hout, requestMethod, playerId, "Crickbuzz");
						this.cfa.multiSheetOptimize(hout, (String) ((Map)this.cfa.getConfig("Crickbuzz")).get("Players_excel"));
						boolean flag = this.cfa.exit_choice();
						if (flag == true)
						{
							continue;
						}
						else {
							System.out.println("Thanks for using crickbuzz api.");
							this.log.error("Thanks for using crickbuzz api.");
							break;
						}
					}
					else if (choie.equalsIgnoreCase("c"))
					{
						p.players_batting(hout, requestMethod, playerId, "Crickbuzz");
						this.cfa.multiSheetOptimize(hout, (String) ((Map)this.cfa.getConfig("Crickbuzz")).get("Players_excel"));
						boolean flag = this.cfa.exit_choice();
						if (flag == true)
						{
							continue;
						}
						else {
							System.out.println("Thanks for using crickbuzz api.");
							this.log.error("Thanks for using crickbuzz api.");
							break;
						}
					}
				}				
				boolean flag = this.cfa.exit_choice();
				if (flag == true)
				{
					continue;
				}
				else {
					System.out.println("Thanks for using crickbuzz api.");
					this.log.error("Thanks for using crickbuzz api.");
					break;
				}
			}
			
			else if (option.equals("6"))
			{
				Stats st= new Stats(cfa);
				String cate=this.cfa.typeCategory();
				String typeFormat=this.cfa.typeFormat();
				HashMap ma=new HashMap();
				ma.put("a", "ICC-Ranking");
				ma.put("b", "ICC-Stndings");
				ma.put("c", "Record-Filters");
				
				for(Object staa:ma.keySet())
				{
					System.out.println(staa+"."+ma.get(staa));
					this.log.info(staa+"."+ma.get(staa));
				}
				String sel=this.cfa.exceptions(ma, "Select your option: ");
				System.out.println("Your selected option is : "+ma.get(sel.toLowerCase()));
				this.log.info("Your selected option is : "+ma.get(sel.toLowerCase()));
				
				if(sel.equalsIgnoreCase("a"))
				{
					st.stats_ranking(hout, requestMethod, cate, typeFormat, "Crickbuzz");
					this.cfa.multiSheetOptimize(hout, (String) ((Map)this.cfa.getConfig("Crickbuzz")).get("Stats_excel"));	
					boolean flag = this.cfa.exit_choice();
					if (flag == true)
					{
						continue;
					}
					else {
						System.out.println("Thanks for using crickbuzz api.");
						this.log.error("Thanks for using crickbuzz api.");
						break;
					}
					
				}
				else if (sel.equalsIgnoreCase("b"))
				{				
					st.stats_standings(hout, requestMethod, "Crickbuzz");
					this.cfa.multiSheetOptimize(hout, (String) ((Map)this.cfa.getConfig("Crickbuzz")).get("Stats_excel"));	
					boolean flag = this.cfa.exit_choice();
					if (flag == true)
					{
						continue;
					}
					else {
						System.out.println("Thanks for using crickbuzz api.");
						this.log.error("Thanks for using crickbuzz api.");
						break;
					}
				}
				else if (sel.equalsIgnoreCase("c"))
				{
					String statsType=st.stats_record_filters(hout, requestMethod, "Crickbuzz");
					st.stats_record(hout, requestMethod, statsType, "Crickbuzz");
					this.cfa.multiSheetOptimize(hout, (String) ((Map)this.cfa.getConfig("Crickbuzz")).get("Stats_excel"));	
					boolean flag = this.cfa.exit_choice();
					if (flag == true)
					{
						continue;
					}
					else {
						System.out.println("Thanks for using crickbuzz api.");
						this.log.error("Thanks for using crickbuzz api.");
						break;
					}
				}								
			}				
		}
	}
	
	public  void golfLeaderBoard() throws IOException, InterruptedException, ParseException
	{
		System.out.println("Welcome to Golf-LeaderBoard");
		this.log.info("Welcome to Golf-LeaderBoard");
		while(true)
		{
			HashMap ha=new HashMap();		
			ha.put("1", "PGA-Rankings");
			ha.put("2", "World-Rankings");
			ha.put("3", "Tours");
			for(Object h:ha.keySet())
			{
				System.out.println(h+"."+ha.get(h));
				this.log.info(h+"."+ha.get(h));
			}			
			String select=this.cfa.exceptions(ha,"Select Your Option  : ");
			System.out.println("Your selected option is : "+ha.get(select.toLowerCase()));
			this.log.info("Your selected option is : "+ha.get(select.toLowerCase()));			
			String season= null;			
			if(select.equals("1"))
			{			
				PGA_Rankings pg = new PGA_Rankings();
				while(true)
				{
					season=this.cfa.userOption("Enter season Year [2022/2023] : ");
					if(season.equals("2022")||(season.equals("2023")))
					{
						String player_id=pg.get_PgaRankings(hout, requestMethod, season, "GolfLeaderBoard");						
						this.cfa.multiSheetOptimize(hout, (String) ((Map)this.cfa.getConfig("GolfLeaderBoard")).get("PgaRankings_excel"));
						break;
					}
					else
					{
						System.out.println("Invaild selection. please select again..");
						this.log.error("Invaild selection. please select again..");						
					}					
				}
				boolean flag=this.cfa.exit_choice();
				if(flag==true)
				{
					continue;
				}
				else 
				{
					System.out.println("Thanks for using Golf-LeaderBoard API..");
					this.log.error("Thanks for using Golf-LeaderBoard API..");
				}				
			}			
			else if(select.equals("2"))
			{
				World_Ranking wr =new World_Ranking();
				wr.get_WorldRanking(hout, requestMethod, "GolfLeaderBoard");
				this.cfa.multiSheetOptimize(hout, (String) ((Map)this.cfa.getConfig("GolfLeaderBoard")).get("WorldRankings_excel"));
				boolean flag=this.cfa.exit_choice();
				if(flag==true)
				{
					continue;
				}
				else 
				{
					System.out.println("Thanks for using Golf-LeaderBoard API..");
					this.log.error("Thanks for using Golf-LeaderBoard API..");
					break;
				}
			}			
			else if (select.equals("3"))
			{
				Tours to= new Tours();
				String tourSeason_id=to.get_Tours(hout, requestMethod, "GolfLeaderBoard");
				this.cfa.multiSheetOptimize(hout, (String) ((Map)this.cfa.getConfig("GolfLeaderBoard")).get("Tours_excel"));
				HashMap hp = new HashMap();
				hp.put("1", "Tour-Rankings");
				hp.put("2", "Fixtures");
				for(Object sel : hp.keySet())
				{
					System.out.println(sel+"."+hp.get(sel));
					this.log.info(sel+"."+hp.get(sel));
				}
				String u_choice=this.cfa.exceptions(hp,"Select any Option : ");
				System.out.println("Your choosen option is : "+hp.get(u_choice.toLowerCase()));
				this.log.info("Your choosen option is : "+hp.get(u_choice.toLowerCase()));
				if(u_choice.equals("1"))
				{
					to.get_tourRanking(hout, requestMethod, tourSeason_id, "GolfLeaderBoard");
					this.cfa.multiSheetOptimize(hout, (String) ((Map)this.cfa.getConfig("GolfLeaderBoard")).get("Tours_excel"));
					boolean flag=this.cfa.exit_choice();
					if(flag==true)
					{
						continue;
					}
					else 
					{
						System.out.println("Thanks for using Golf-LeaderBoard API..");
						this.log.error("Thanks for using Golf-LeaderBoard API..");
						break;
					}
				}				
				else if(u_choice.equals("2"))
				{
					String tment_id=to.get_Fixtures(hout, requestMethod, tourSeason_id, "GolfLeaderBoard");
					if(tment_id!=null)
					{
						HashMap mp = new HashMap();
						mp.put("a", "Entry-List");
						mp.put("b", "Leaderboard");
						mp.put("c", "PGA-Scorecard");
						for(Object m:mp.keySet())
						{
							System.out.println(m+"."+mp.get(m));
							this.log.info(m+"."+mp.get(m));
						}
						String u_choice1=this.cfa.exceptions(mp,"Select any Option : ");
						System.out.println("Your choosen option is : "+mp.get(u_choice1.toLowerCase()));
						this.log.info("Your choosen option is : "+mp.get(u_choice1.toLowerCase()));
						if(u_choice1.equalsIgnoreCase("a"))
						{
							this.log.info("TID = "+tment_id);
							to.get_EntryList(hout, requestMethod, tment_id, "GolfLeaderBoard");
							this.cfa.multiSheetOptimize(hout, (String) ((Map)this.cfa.getConfig("GolfLeaderBoard")).get("Tours_excel"));
							boolean flag=this.cfa.exit_choice();
							if(flag==true)
							{
								continue;
							}
							else 
							{
								System.out.println("Thanks for using Golf-LeaderBoard API..");
								this.log.error("Thanks for using Golf-LeaderBoard API..");
								break;
							}
						}
						else if(u_choice1.equalsIgnoreCase("b"))
						{
							to.get_Leaderboard(hout, requestMethod, tment_id, "GolfLeaderBoard");
							this.cfa.multiSheetOptimize(hout, (String) ((Map)this.cfa.getConfig("GolfLeaderBoard")).get("Tours_excel"));
							boolean flag=this.cfa.exit_choice();
							if(flag==true)
							{
								continue;
							}
							else 
							{
								System.out.println("Thanks for using Golf-LeaderBoard API..");
								this.log.error("Thanks for using Golf-LeaderBoard API..");
								break;
							}
						}
						else if (u_choice1.equalsIgnoreCase("c"))
						{
							PGA_Rankings pg = new PGA_Rankings();
							String player_id=pg.get_PgaRankings(hout, requestMethod, tourSeason_id.split("/")[1], "GolfLeaderBoard");
							this.cfa.multiSheetOptimize(hout, (String) ((Map)this.cfa.getConfig("GolfLeaderBoard")).get("Tours_excel"));
							to.get_PgaScoreCard(hout, requestMethod, tment_id, player_id, "GolfLeaderBoard");
							this.cfa.multiSheetOptimize(hout, (String) ((Map)this.cfa.getConfig("GolfLeaderBoard")).get("Tours_excel"));
							boolean flag=this.cfa.exit_choice();
							if(flag==true)
							{
								continue;
							}
							else 
							{
								System.out.println("Thanks for using Golf-LeaderBoard API..");
								this.log.error("Thanks for using Golf-LeaderBoard API..");
								break;
							}	
						}
					}
				}			
				boolean flag=this.cfa.exit_choice();
				if(flag==true)
				{
					continue;
				}
				else 
				{
					System.out.println("Thanks for using Golf-LeaderBoard API..");
					this.log.error("Thanks for using Golf-LeaderBoard API..");
					break;
				}
			}
		}
	}
	
	public void horse() throws IOException, InterruptedException, ParseException
	{
		System.out.println("Welcome to Horse-Api");
		this.log.info("Welcome to Horse-Api");
		while(true)
		{
			HashMap hm = new HashMap();
			hm.put("1", "Race Cards");
			hm.put("2", "Results");
			hm.put("3", "Jockeys win rate");
			hm.put("4", "Trains win rate");
			hm.put("5", "Queries");
			for(Object c :hm.keySet())
			{
				System.out.println(c+"."+hm.get(c));
				this.log.info(c+"."+hm.get(c));
			}
			String choic=this.cfa.exceptions(hm, "Choose your Option:");
			System.out.println("Your Selected Option is : "+hm.get(choic.toLowerCase()));
			this.log.info("Your Selected Option is : "+hm.get(choic.toLowerCase()));			
			if(choic.equals("1"))
			{	
				Race_cards rc= new Race_cards();
				String race_id=rc.get_RaceCards(hout, requestMethod, "Horse");		
				String horse_id=rc.get_RaceDetails_info(hout, requestMethod, race_id, "Horse");	
				rc.getHorseStats(hout, requestMethod, horse_id, "Horse");			
				this.cfa.multiSheetOptimize(hout, (String) ((Map)this.cfa.getConfig("Horse")).get("Racecards_excel"));
				boolean flag=this.cfa.exit_choice();
				if(flag==true)
				{
					continue;
				}
				else
				{
					System.out.println("Thanks for using HORSE-API Call.");
					this.log.error("Thanks for using HORSE-API Call.");
					break;
				}				
				
			}
			
			else if(choic.equals("2"))
			{
				Results re = new Results();
				re.get_Results(hout, requestMethod, "Horse");
				this.cfa.multiSheetOptimize(hout, (String) ((Map)this.cfa.getConfig("Horse")).get("Results_excel"));
				boolean flag=this.cfa.exit_choice();
				if(flag==true)
				{
					continue;
				}
				else
				{
					System.out.println("Thanks for using HORSE-API Call.");
					this.log.error("Thanks for using HORSE-API Call.");
					break;
				}
			}			
			else if(choic.equals("3"))
			{
				Jockeys_WinRates j = new Jockeys_WinRates();
				int days= 0;
				while(true)
				{
					try 
					{
						days=Integer.parseInt(this.cfa.userOption("Enter last days provide from(1-15) : "));					
						if(days<15)
						{						
							break;						
						}
						else
						{
							System.out.println("Invaild selection.Please select again.");
							this.log.error("Invaild selection.Please select again.");
							continue;
						}
					} 
					catch (Exception e) 
					{
						System.out.println("Enter digits only : ");
						this.log.error("Enter digits only : ");
					}					
				}
				j.get_JockeysWinRates(hout, requestMethod, Integer.toString(days), "Horse");
				this.cfa.multiSheetOptimize(hout, (String) ((Map)this.cfa.getConfig("Horse")).get("Jockey_excel"));
				boolean flag=this.cfa.exit_choice();
				if(flag==true)
				{
					continue;
				}
				else
				{
					System.out.println("Thanks for using HORSE-API Call.");
					this.log.error("Thanks for using HORSE-API Call.");
					break;
				}
			}
			else if(choic.equals("4"))
			{
				Trainers_Winrate  tw = new Trainers_Winrate();
				String Tdays=null;
				while(true)
				{	
					try 
					{
						Tdays=this.cfa.userOption("Enter last-days provide from(1-15):");
						if(Tdays.length()<2)
						{
							break;
						}
						else
						{
							System.out.println("Invaild selection.Please select again.");
							this.log.error("Invaild selection.Please select again.");
							continue;
						}
					} 
					catch (Exception e) 
					{
						System.out.println("Enter digits only");
						this.log.error("Enter digits only");
					}					
				}					
				tw.get_TrainersWinrate(hout, requestMethod, Tdays, "Horse");
				this.cfa.multiSheetOptimize(hout, (String) ((Map)this.cfa.getConfig("Horse")).get("TrainersWinRate_excel"));
				boolean flag=this.cfa.exit_choice();
				if(flag==true)
				{
					continue;
				}
				else
				{
					System.out.println("Thanks for using HORSE-API Call.");
					this.log.error("Thanks for using HORSE-API Call.");
					break;
				}
			}
			
			else if(choic.equals("5"))
			{
				Query_Races qr = new  Query_Races();
				String name=qr.get_QueryRaces(hout, requestMethod, "Horse");
				qr.get_QueryHorses(hout,requestMethod, name, "Horse");
				this.cfa.multiSheetOptimize(hout, (String)((Map) this.cfa.getConfig("Horse")).get("Query_excel"));
				boolean flag=this.cfa.exit_choice();
				if(flag==true)
				{
					continue;
				}
				else
				{
					System.out.println("Thanks for using HORSE-API Call.");
					this.log.error("Thanks for using HORSE-API Call.");
					break;
				}
			}				
		}
	}
	public static void main(String[] args) throws IOException, InterruptedException, ParseException
	{
		mainEngineForAll ma = new mainEngineForAll();
		System.out.println("Welcome to API ");
		ma.log.info("Welcome to API ");
		HashMap hmMain= new HashMap();
		hmMain.put("1", "Crickbuzz-API");
		hmMain.put("2", "GolfLeaderBoard-API");
		hmMain.put("3", "Horse-API");
		for(Object k:hmMain.keySet())
		{
			System.out.println(k+"."+hmMain.get(k));
			ma.log.info(k+"."+hmMain.get(k));
		}
		mainEngineForAll me = new mainEngineForAll();
		String choice=me.cfa.exceptions(hmMain, "Select your option: ");	
		System.out.println("Your Selected Option is : "+hmMain.get(choice));
		ma.log.info("Your Selected Option is : "+hmMain.get(choice));	
		
		if(choice.equals("1"))
		{
			me.crickbuzz();
		}
		else if(choice.equals("2"))
		{
			me.golfLeaderBoard();
		}
		else if(choice.equals("3"))
		{
			me.horse();
		}
		
	}

}
