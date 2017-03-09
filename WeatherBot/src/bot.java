// This class will implement a simple twitter bot that allows users to register their location by tweeting their zip code at the account
// and will respond daily with the current day's weather forecast for each registered users' location.

// OpenWeather API urls:
// api.openweathermap.org/data/2.5/forecast/city?id=524901&APPID=1111111111  << This is for a 16 day forecast http://openweathermap.org/forecast16
// http://api.openweathermap.org/data/2.5/weather?id=524901&APPID=dfcf81b71ea3ec9d3d40ec9737694ac6  << This is current weather, can use zip code 

// http://samples.openweathermap.org/data/2.5/forecast/daily?id=524901&appid=b1b15e88fa797225412429c1c50c122a1

// Below is the one that I used ::: 
// http://api.openweathermap.org/data/2.5/forecast/daily?id=524901&appid=dfcf81b71ea3ec9d3d40ec9737694ac6&mode=xml&units=imperial
// Here is for Seattle :::
// http://api.openweathermap.org/data/2.5/forecast/daily?id=5809844&appid=dfcf81b71ea3ec9d3d40ec9737694ac6&mode=xml&units=imperial
// Here is the documentation :::
// http://www.openweathermap.com/forecast16


// URL info : https://docs.oracle.com/javase/tutorial/networking/urls/creatingUrls.html
// Weather api info : http://www.openweathermap.com/appid


// Imports
import java.net.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.io.*;

public class bot{

	/////////////////////////
	///  Final Variables
	/////////////////////////
	
	final static String WEATHERAPIKEY = "dfcf81b71ea3ec9d3d40ec9737694ac6";
	final static String TWITTERAPIKEY = "";
	final static String USERCSVFPATH = "";
	final static String WEATHERCODEFPATH = "";
	final static String RESPLISTFPATH = ""; 
	final static String WEATHERURL = "http://api.openweathermap.org/data/2.5/forecast/daily?zip=";  

			
	/////////////////////////
	///  API Functions
	/////////////////////////

	public static String getForecast(int zip) throws MalformedURLException{
		// http://www.openweathermap.com/forecast16
		// This assumes that the user is in the US (country code needs to be passed to api)
		
		// Construct final url
		String args = zip + ",us&APPID=" + WEATHERAPIKEY + "&mode=xml";
		URL weathURL = new URL(WEATHERURL);
		URL tmp_url = new URL(weathURL, args);
		    
		// Call api (include error catching) -- may return as xml object instead of string
		String api_resp = " "; //call_url(tmp_url);

		return api_resp;    
	}
	
	public static void sendTweet(String username, String message){
	    // Sends tweet @username with message
		// Probably better version of this in the library.
	}

	public static void getMentions(String username, String[] messages){
		// Probably better version of this in library.
	}
	
	/////////////////////////
	///  Data Processing
	/////////////////////////
	
	public static String[] parseXML(/* File? HttpResponse? */) {

		String precip = null, clouds = null, maxTemp = null, minTemp = null, humidity = null, weathCode = null, precipVal = null;  	
		DecimalFormat format = new DecimalFormat("#");

		// Process xml
		try {
			
			// will work from response passed as arg but use file below for example. 			
			File f = new File("C://Users//Zach.Bremmer//Desktop//dailyweath.xml");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document d = db.parse(f);
			d.getDocumentElement().normalize();
			
			// Get first time element (today)
			NodeList n = (NodeList) d.getElementsByTagName("time").item(0);
			
			Element elem = (Element) n;
			elem.getElementsByTagName("symbol").item(0).getAttributes().getNamedItem("name").getNodeValue();
			elem.getElementsByTagName("temperature").item(0).getAttributes().getNamedItem("day").getNodeValue();
			
			weathCode = getElementValue("symbol", "name", elem); 
			maxTemp = getElementValue("temperature", "max", elem);
			minTemp = getElementValue("temperature", "min", elem);
			precip = getElementValue("precipitation", "type", elem);
			precipVal = getElementValue("precipitation", "value", elem);  // value in mm 
			clouds = getElementValue("clouds", "value", elem);
			humidity = getElementValue("humidity", "value", elem);
			
			// Need precip amount too.
			
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (ParserConfigurationException e2) {
			e2.printStackTrace();
		} catch (IOException e3) {
			e3.printStackTrace();
		} catch (SAXException e4) {
			e4.printStackTrace();
		}
		
		String[] resp = {weathCode, maxTemp, minTemp, precip, clouds, humidity};
		
		return resp;
		
		}

	public static String getElementValue(String tagName, String itemName, Element elem){
		try {
			return elem.getElementsByTagName(tagName).item(0).getAttributes().getNamedItem(itemName).getNodeValue();
		 } catch (NullPointerException e){
			return "-11111";			
		 }
	}	
	
	public static void addUser(String username, int zip){
		    // Add user to csv file
	}

	public static String simplifyForecast(String[] fcast, String day, double yesterdayMaxTemp, Map<String, List<String>> responseList){
		/* NONE OF THIS IS TESTED YET.
		 * 
		 * Weather cutoff values:
		 * Precip - 51mm
		 * Snow - 255mm, 500mm
		 * Temp Change - +- 20*F
		 * 
		 * Hail is bad all the time so just use binary y/n for this.
		 */
		
		String respCode = "";
		
		// Test the values sent through fcast. They will always be the same order so just use index get values.
		// String[] resp = {weathCode, maxTemp, minTemp, precip, clouds, humidity}
		
		// Test temp
		if(Math.abs(Double.parseDouble(fcast[1]) - yesterdayMaxTemp) > 19){
			respCode += "t";
		}
		
		if(Double.parseDouble(fcast[3]) > 50){
			respCode += "p";
		}
		
		switch(day){
			case "Monday": 	respCode += "3";
							break;
			case "Tuesday": respCode += "3";
							break;
			case "Wednesday":respCode += "2";
							break;
			case "Thursday":respCode += "1";
							break;
			case "Friday": 	respCode += "0";
							break;
			case "Saturday":respCode += "0";
							break;
			case "Sunday": 	respCode += "0";
							break;
		}
		
		
		
		// Get message from responseList and build final message. 
		
		// Load dict and lookup weather code
		//String weath = map.get(weathCode);
		//String tweet = weath + ", high of " + format.format(maxTemp) + "\u00B0F. Low of " + format.format(minTemp) + "\u00B0F overnight.";
		//System.out.println(tweet);
		//System.out.println("Number of Chars : " + tweet.length());
		
		// Need to test length and rewrite if too long. 
		
		String message = null;
		
		return message;
	}
	 
	/////////////////////////
	///  Control 
	///////////////////////// 
	 
	public static void checkMentions(){
	// Check to see if anyone tweeted at the bot and add them
	// if messages = TRUE :
	// test message to see if includes zip (11111 or 11111-1111). If True: 
	// get_city_id(zip);
	// add_user(username, zip, cityid);
	// send_tweet(username, "You will now receive daily forecasts for " + location_name)
		 
	}

	@SuppressWarnings("unchecked")
	public static void sendDailyForecasts() throws IOException, ClassNotFoundException{
				
		/////////
		// Open files
		/////////

		// Open response list object
		FileInputStream fin = new FileInputStream(RESPLISTFPATH);
		ObjectInputStream oin = new ObjectInputStream(fin);
		Map<String, List<String>> responseList = (Map<String, List<String>>) oin.readObject();
		fin.close();
		oin.close();
		 
		// Open weather codes list object (may not need...)
		FileInputStream fin2 = new FileInputStream("C://Users//Zach.Bremmer//Desktop//weathercodes");
		ObjectInputStream oin2 = new ObjectInputStream(fin2);
		Map<Integer, String> weatherCodes = (Map<Integer, String>) oin2.readObject();
		fin2.close();
		oin2.close();
		
		// Get the day of the week for today
		Date n = new Date();
		SimpleDateFormat df = new SimpleDateFormat("EEEE");
		String day = df.format(n);
		
		
		/////////
		// Build data structures
		////////
		
		
		// Open csv and grab each record 
		File userNames = new File("C://Users//Zach.Bremmer//Desktop//user_names.csv");
		CSVReader r = new CSVReader(new FileReader(userNames), ',');
		List<String[]> userNameList = r.readAll();
		r.close();
		
		List<Integer> zipList = null; // Unique zip code list
		Map<Integer, String> messageDict = new HashMap<Integer, String>(); // zip:message Dictionary
		Map<Integer, Double> temperDict = new HashMap<Integer, Double>(); // zip:maxTemp Dictionary
		
		// Iterate through all users, send forecast, update maxTemp
		for(int i = 0; i < userNameList.size(); i++){
			int currZip = Integer.parseInt(userNameList.get(i)[1]);
			if(zipList.stream().mapToInt(Integer::intValue).anyMatch(x -> x == currZip)){
				// Already have forecast and message for this zip code
				String msg = messageDict.get(currZip);
				
				// send tweet
				
				// update maxTemp
				userNameList.get(i)[2] = String.valueOf(temperDict.get(currZip));
			} else {
				// New zip, need to run through all functions.
				int yesterdayMaxTemp = Integer.parseInt(userNameList.get(i)[2]);
				
				// get forecast
				String resp = getForecast(currZip);
				String[] respParsed = parseXML(/*still need to figure out args for this*/);
				String message = simplifyForecast(respParsed, day, yesterdayMaxTemp, responseList);				
				
				// add items to lists
				double maxTemp = Double.parseDouble(respParsed[1]);
				zipList.add(currZip);
				messageDict.put(currZip, message);
				temperDict.put(currZip, maxTemp);
				
				// send tweet
				
				// update maxTemp
				userNameList.get(i)[2] = String.valueOf(maxTemp);
			}
		}
		
		CSVWriter w = new CSVWriter(new FileWriter(userNames));
		w.writeAll(userNameList);
		w.flush();
		w.close();
		
		
		
//		
//		
//		///////////// BELOW IS OLD AND UNFINISHED EXCEPT THE CSVWRITING FUNCTIONS WHICH WILL BE USED
//		
//		// Build out list of unique zip codes
//
//		for(int i = 0; i < userNameList.size(); i++){
//			int k = i;	
//			int[] tmpList = new int[zipList.size()];
//			
//			// Get first col from zipList to test if zip is already added (can't find a better way to do this)
//			for(int j = 0; j < zipList.size(); j++){
//				int[][] tmp = zipList.get(j);
//				tmpList[j] = tmp[0][0];
//			}
//			
//			if(! IntStream.of(tmpList).anyMatch(x -> x == Integer.parseInt(userNameList.get(k)[1]))){
//				int[][] tmp2 = new int[1][2];
//				tmp2[0][0] = Integer.parseInt(userNameList.get(i)[1]);
//				tmp2[0][1] = (int)(Math.round(Double.parseDouble(userNameList.get(i)[2]))); 
//				zipList.add(tmp2);
//			}
//		}		
//		
//
//		// Build messages for each unique zip code
//		// What data structure for this??
//		List<Integer, Double, String> messageList = new ArrayList<Integer, Double, String>();
//		for(int i = 0; i < zipList.length; i++){
//			String resp = getForecast(zipList[i][0]);
//			double maxTemp = resp[1];
//			int yesterdayMaxTemp = zipList[i][1];
//			String message = simplifyForecast(forecast, day, yesterdayMaxTemp, responseList);
//			messageList.put(zipList[i][0], maxTemp, message);
//		}
//		
//		// Iterate through for each user, send message, update maxTemp in csv
//		for(int i = 0; i < body.size(); i++){
//			
//		}
//		
//		
//		
//		
//		int zip = 98144;
//		
//		double yesterdayMaxTemp = 33.33;
//		String[] forecast = parseXML(weatherCodes);
//		
//		
//		// Save today's maxTemp in csv for tomorrow
//		
//		// send_tweet(username, message);
//			// same message for everyone in a given area code?
//		
//		// Below uses library opencsv http://opencsv.sourceforge.net/
//		File userNames = new File("C://Users//Zach.Bremmer//Desktop//user_names.csv");
//		CSVReader r = new CSVReader(new FileReader(userNames), ',');
//		List<String[]> body = r.readAll();
//		
//		//body.get(row)[col] = newVal;
//		//r.close();
//		
//		//CSVWriter w = new CSVWriter(new FileWriter(userNames), ",");
//		//w.writeAll(body);
//		//w.flush();
//		//w.close();
//		
		

	}
	
	 
	/////////////////////////
	///  Program Driver
	/////////////////////////
	
	 public static void main(String[] args) throws IOException, ClassNotFoundException{
		Timer t1 = new Timer();
		t1.schedule( new CheckMentionsTask(), 0, 14400000); // 4 hour delay in milliseconds
		t1.schedule(new SendForecastTask(), 300000, 86400000); // 24 hour delay in milliseconds. Delay initial run by 5 minutes.			
	 }
	 
}


/////////////////////////
///  TimerTask Classes
/////////////////////////

class CheckMentionsTask extends TimerTask{

	@Override
	public void run() {
		bot.checkMentions();
		// What if this method fails? How will java respond?
	}
	
}

class SendForecastTask extends TimerTask{
	
	@Override
	public void run() {
		try {
			bot.sendDailyForecasts();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// How to continue if this fails?
		}
	}
}