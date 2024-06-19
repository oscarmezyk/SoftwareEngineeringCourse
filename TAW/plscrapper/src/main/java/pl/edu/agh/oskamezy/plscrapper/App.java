package pl.edu.agh.oskamezy.plscrapper;


import java.io.IOException;
import java.sql.SQLException;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.Connection;
import org.jsoup.Connection.Request;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Random;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class App 
{
	private static String url = "https://nowyswiat.online/playlista";
	private static String baseurl = "https://nowyswiat.online/";

	public static SongsDAO Songsdb;
	
    public static void main( String[] args )
    {
        Songsdb = new SongsDAO();

		if(args.length > 0) {
			//String fakeArgs[] = {"MONDAY","22:00","23:59","20240101","20240609"};
			ArgsParser argObj = new ArgsParser();
	        argObj.parse(args);
	        runScrapper(args);
	        return;
		} 
        
        try {
            HttpServer.startHttpServer();        	
        } catch (Exception e) {
            e.printStackTrace();
        }
    
    }
    //run & add to database with args = {"MONDAY","22:00","23:59","20240101","20240609"};
    static public void runScrapper(String[] args) {

    	ArgsParser argObj = new ArgsParser();
		argObj.parse(args);

		List<String> daysToParse = new LinkedList<>(); 
		daysToParse = argObj.getDates(argObj.day, argObj.startDate, argObj.endDate);
		
        final String start_time = argObj.startTime.toString();
		final String end_time = argObj.endTime.toString();
        
        Random random = new Random();
		
        for (final String date : daysToParse) {
            // Create a new thread for each date
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                    	
                    	getSongsPerDay(date,start_time,end_time, "true");
                    	
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            // Introduce a random delay before starting the thread
            int delay = random.nextInt(500); // Delay between 0 and 499 milliseconds
            try {
                TimeUnit.MILLISECONDS.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Start the thread
            thread.start();
        }
    }
    
    //run & add to database with args = {"MONDAY","22:00","23:59","20240101","20240609"};
    static public List<String[]> runSingleScrapper(String[] args) {
    	List<String[]> arrOfSongs = new ArrayList<>();
   	
    	ArgsParser argObj = new ArgsParser();
    	List<String> daysToParse = new LinkedList<>();
    	
   		argObj.parse(args);
   		daysToParse = argObj.getDates(argObj.day, argObj.startDate, argObj.endDate);
   	
		
        final String start_time = argObj.startTime.toString();
		final String end_time = argObj.endTime.toString();
        
        for (final String date : daysToParse) {
                    try {
                    	Elements songs = getSongsPerDay(date,start_time,end_time,"false");
                        for (Element song : songs) {
                        	
                        	Elements song_url = song.getElementsByClass("rns-vote-img");
                        	
                        	String[] row = {song.getElementsByClass("rns-vote-names").text(),
                        					song.getElementsByClass("rns-vote-title").text(),
                        					date,
                        					song.getElementsByClass("rns-switcher-time").text(),
                                        	baseurl+song_url.attr("src").toString()};
                        	
                        	arrOfSongs.add(row);  
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
               }
        
        return arrOfSongs;
    }
    
    
    static public Elements getSongsPerDay(String date, String start_time, String end_time, String save ) throws IOException {
            // Create a connection to the URL
            Connection connection = Jsoup.connect(url);

            // Set other optional settings if needed
            connection.timeout(5000); // Timeout in milliseconds (e.g., 5 seconds)
            connection.userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");

            // Execute the GET request and retrieve the response
            Response response = connection.execute();

            // Get the cookies from the response
            Map<String, String> cookies = response.cookies();

            //get the token & construct data for 
            Map<String, String> data = new HashMap<>();
            data.put("csrf_neocms", cookies.get("csrf_cookie_neocms"));
            data.put("date",date);
            data.put("time_range",start_time + " - " + end_time);

            String cookieStr = "videoIframeSrc=undefined; cookie-accept=yes; ";
            cookieStr += "csrf_cookie_neocms=" + cookies.get("csrf_cookie_neocms") + "; ";
            cookieStr += "cisession=" + cookies.get("cisession") + ";"; 
            
            connection = Jsoup.connect(url)
                    .header("Cookie", cookieStr)
                    .data("csrf_neocms", cookies.get("csrf_cookie_neocms"))
                    .data("date", data.get("date"))
                    .data("time_range", data.get("time_range"))
                    .method(Connection.Method.POST); // Setting the method to POST

            Document document = connection.post();
            
            Elements songs = new Elements();
            songs = document.getElementsByClass("rns-vote-list-element");
           
            if(save == "true") {
            	for (Element song : songs) {
            		//System.out.println(song.getElementsByClass("rns-vote-title").text() + "#  #" + song.getElementsByClass("rns-vote-names").text() + "#  #" + song.getElementsByClass("rns-switcher-time").text());

            		try {
                    	Elements song_url = song.getElementsByClass("rns-vote-img");
            			Songsdb.addSong(date, song.getElementsByClass("rns-switcher-time").text(), song.getElementsByClass("rns-vote-names").text(),song.getElementsByClass("rns-vote-title").text(),baseurl+song_url.attr("src").toString());            		
            		} catch (SQLException e) {
            			if (e.getMessage().indexOf("Duplicate entry") == -1) {
            				e.printStackTrace();            			
            			} else {
            				//System.out.println("<----Duplicate!");
            			}
            		}
            	}
            }
            return songs;
    }
   
}
