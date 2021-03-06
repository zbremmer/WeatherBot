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
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import twitter4j.DirectMessage;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.io.*;

public class bot{

	/////////////////////////
	///  Final Variables
	/////////////////////////
	
	final static String WEATHERAPIKEY = "dfcf81b71ea3ec9d3d40ec9737694ac6";
	final static String USERCSVFPATH = "/Users/zbremmer/Desktop/user_names.csv";
	final static String RESPLISTFPATH = ""; 
	final static String WEATHERURL = "http://api.openweathermap.org/data/2.5/forecast/daily?zip=";  
	final static String CONSUMERKEY = "###";
	final static String CONSUMERKEYSECRET = "###;
	final static String ACCESSTOKEN = "###";
	final static String ACCESSTOKENSECRET = "###";
	final static Pattern p = Pattern.compile("^[0-9]{5}(?:-[0-9]{4})?$");
	
	/////////////////////////
	///  API Functions
	/////////////////////////

	public static String getForecast(int zip) throws MalformedURLException{
		// http://www.openweathermap.com/forecast16
		// This assumes that the user is in the US (country code needs to be passed to api)
		
		// Construct final url
		URL tmpURL = new URL(WEATHERURL + zip + ",us&APPID=" + WEATHERAPIKEY + "&mode=xml");
		    
		// Call api (include error catching) -- may return as xml object instead of string
		String result = "";
		  
	    try {
		URLConnection conn = tmpURL.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
		String inputLine; 
		while ((inputLine = in.readLine()) != null){
			result += inputLine;
		}
		
		in.close();
		
	    } catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	    
	    return result;  
	}
	
	/////////////////////////
	///  Data Processing
	/////////////////////////
	
	public static String[] parseXML(String xmlString) {

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
	
	public static String simplifyForecast(String[] fcast, String day, double yesterdayMaxTemp, Map<String, List<String>> responseList){
		/* NONE OF THIS IS TESTED YET.
		 * 
		 * Weather cutoff values:
		 * Precip - 51mm  (1.97")
		 * Snow - 255mm, 500mm (10", 19.68")
		 * Temp Change - +- 15*F
		 * 
		 * Hail is bad all the time so just use binary y/n for this. 
		 */
		
		String respCode = "";
		
		// Test the values sent through fcast. They will always be the same order so just use index get values.
		// String[] resp = {weathCode, maxTemp, minTemp, precip, clouds, humidity}
		
		// The respCode string needs to be built in the following order: temp, precip, snow, hail, day
		
		if(yesterdayMaxTemp != -11111 && Math.abs(Double.parseDouble(fcast[1]) - yesterdayMaxTemp) > 15){
			respCode += "t";
		}
		
		if(Integer.parseInt(fcast[0]) < 600 ){
			// Rain
			if(fcast[3] != "-11111" && Double.parseDouble(fcast[3]) > 1.97){
				respCode += "p2";
			} else if(fcast[3] != "-11111" && Double.parseDouble(fcast[3]) > 0){
				respCode += "p1";
			}
		} else if(Integer.parseInt(fcast[0]) < 700){
			// Snow
			if(fcast[3] != "-11111" && Double.parseDouble(fcast[3]) > 19.68){
				respCode += "s3";
			} else if(fcast[3] != "-11111" && Double.parseDouble(fcast[3]) > 10){
				respCode += "s2";
			} else if(fcast[3] != "-11111" && Double.parseDouble(fcast[3]) > 0){
				respCode += "s1";
			}
		}
		
		if(fcast[0].equals("906")){
			// If hailing, don't worry about the day of the week. 
			respCode += "h";
		} else {
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
		}
		
		// Build first half of message based on weather forecast (*NEED TO RETURN WEATHER CONDITIONS FROM XML OR LOOKUP CODE VALUES*)
		String weath = "RAIN, CLOUDY, TORNADO, WHATEVER THE CODE STANDS FOR";
		String message = weath + ", high of " + Math.round(Double.parseDouble(fcast[1])) + "\u00B0F. Low of " + Math.round(Double.parseDouble(fcast[1])) + "\u00B0F. ";
		
		// Get message from responseList and build final message. 
		// import java.lang.util.Random
		Random r = new Random();
		message += responseList.get(respCode).get(r.nextInt(3));
		
		// Need to test length and rewrite if too long. 
		if(message.length() > 140){
			System.out.println("Message is too long.");
		}
		
		return message;
	}
	 
	/////////////////////////
	///  Control 
	///////////////////////// 
	 
	public static void checkDM() throws TwitterException, IOException{
		
		// This won't work. Can't send a DM unless @Weather___bot follows you... so just do mentions and parse the message I guess. 
		
		// Initialize Twitter object and authenticate
		Twitter t = TwitterFactory.getSingleton();
		t.setOAuthConsumer(CONSUMERKEY, CONSUMERKEYSECRET);
		AccessToken at = new AccessToken(ACCESSTOKEN, ACCESSTOKENSECRET);
		t.setOAuthAccessToken(at);
		
		// Pull messages
		List<DirectMessage> messages = t.getDirectMessages();
		
		if(messages.isEmpty()){
			return; // Exit function if there are no messages
		}
		
		// Open user_names.csv
		File userNamesFile = new File(USERCSVFPATH);
		CSVReader r = new CSVReader(new FileReader(userNamesFile), ',');
		List<String[]> userNameList = r.readAll();
		r.close();
		
		// Parse messages
		for(int i = 0; i < messages.size(); i++){
			
			DirectMessage msg = messages.get(i);
			
			String msgTxt = msg.getText();
			String userName = msg.getSender().toString(); // or getScreenName()?
			long msgID = msg.getId();
			
			Matcher m = p.matcher(msgTxt);
			
			if(m.find()){
				String zipCode = m.group(0).substring(0,5); // Only want first five of zip
				
				// add username, zip to csv. Add '-11111' for maxTemp;
				userNameList.add(new String[] {userName, zipCode, "-11111"});
				
				// Send confirmation tweet / DM
				t.updateStatus(userName + " Good to go! You will now get forecasts for " + zipCode);
			}
			
			// Delete message 
			t.destroyDirectMessage(msgID);
		}
		
		// Write new records to user_names.csv
		CSVWriter w = new CSVWriter(new FileWriter(userNamesFile));
		w.writeAll(userNameList);
		w.flush();
		w.close();
	}
	
	public static void checkMentions() throws TwitterException, IOException{
		// May need to check usernames in the csv. I assume that getMentionsTimeline() will pull all 
		// mentions, including the ones I've already added. There will also be more junk since a mention could
		// be anything. Hopefully the regex will deal with this without a problem. 
		
		
		// Initialize Twitter object and authenticate
		Twitter t = TwitterFactory.getSingleton();
		t.setOAuthConsumer(CONSUMERKEY, CONSUMERKEYSECRET);
		AccessToken at = new AccessToken(ACCESSTOKEN, ACCESSTOKENSECRET);
		t.setOAuthAccessToken(at);
		
		// Get mentions
		List<Status> stats = t.getMentionsTimeline();
		
		if(stats.isEmpty()){
			return; // Exit function if there are no mentions
		}
		
		// Open user_names.csv
		File userNamesFile = new File(USERCSVFPATH);
		CSVReader r = new CSVReader(new FileReader(userNamesFile), ',');
		List<String[]> userNameList = r.readAll();
		r.close();
		
		// Parse mentions
		for(Status s : stats){
					
			String msgTxt = s.getText();
			String userName = s.getUser().toString(); // or getScreenName()?
					
			Matcher m = p.matcher(msgTxt);
					
			if(m.find()){
				String zipCode = m.group(0).substring(0,5); // Only want first five of zip
						
				// add username, zip to csv. Add '-11111' for maxTemp;
				userNameList.add(new String[] {userName, zipCode, "-11111"});
						
				// Send confirmation tweet / DM
				t.updateStatus(userName + " Good to go! You will now get forecasts for " + zipCode);
			}
					
		}
				
		// Write new records to user_names.csv
		CSVWriter w = new CSVWriter(new FileWriter(userNamesFile));
		w.writeAll(userNameList);
		w.flush();
		w.close();
		
	}
	
	@SuppressWarnings({ "unchecked", "null" })
	public static void sendDailyForecasts() throws IOException, ClassNotFoundException, TwitterException{
				
		/////////
		// Open files
		/////////

		// Open response list object
		FileInputStream fin = new FileInputStream(RESPLISTFPATH);
		ObjectInputStream oin = new ObjectInputStream(fin);
		Map<String, List<String>> responseList = (Map<String, List<String>>) oin.readObject();
		fin.close();
		oin.close();
		
		// Open user_names.csv and grab all records
		File userNamesFile = new File(USERCSVFPATH);
		CSVReader r = new CSVReader(new FileReader(userNamesFile), ',');
		List<String[]> userNameList = r.readAll();
		r.close();
			
		/////////
		// Build data structures
		////////

		List<Integer> zipList = null; // Unique zip code list
		Map<Integer, String> messageDict = new HashMap<Integer, String>(); // zip:message Dictionary
		Map<Integer, Double> temperDict = new HashMap<Integer, Double>(); // zip:maxTemp Dictionary

		// Get the day of the week for today
		Date n = new Date();
		SimpleDateFormat df = new SimpleDateFormat("EEEE");
		String day = df.format(n);
		
		// Initialize Twitter object and authenticate
		Twitter t = TwitterFactory.getSingleton();
		t.setOAuthConsumer(CONSUMERKEY, CONSUMERKEYSECRET);
		AccessToken at = new AccessToken(ACCESSTOKEN, ACCESSTOKENSECRET);
		t.setOAuthAccessToken(at);
		
		
		// Iterate through all users, send forecast, update maxTemp
		for(int i = 0; i < userNameList.size(); i++){
			
			int currZip = Integer.parseInt(userNameList.get(i)[1]);
			
			if(zipList.stream().mapToInt(Integer::intValue).anyMatch(x -> x == currZip)){
				
				// Already have forecast and message for this zip code
				String msg = messageDict.get(currZip);
				String userName = userNameList.get(i)[0];
				
				// send tweet (as tweet or as DM?)
				t.updateStatus(userName + " " + msg);
				
				// update maxTemp
				userNameList.get(i)[2] = String.valueOf(temperDict.get(currZip));
			
			} else {
				
				// New zip, need to get forecast
				int yesterdayMaxTemp = Integer.parseInt(userNameList.get(i)[2]);
				
				// get forecast
				String resp = getForecast(currZip);
				String[] respParsed = parseXML(resp);
				String msg = simplifyForecast(respParsed, day, yesterdayMaxTemp, responseList);				
				
				// add items to lists
				double maxTemp = Double.parseDouble(respParsed[1]);
				zipList.add(currZip);
				messageDict.put(currZip, msg);
				temperDict.put(currZip, maxTemp);
				
				// send tweet (as tweet or as DM?)
				String userName = userNameList.get(i)[0];
				t.updateStatus(userName + " " + msg);
				
				// update maxTemp
				userNameList.get(i)[2] = String.valueOf(maxTemp);
			}
		}
		
		CSVWriter w = new CSVWriter(new FileWriter(userNamesFile));
		w.writeAll(userNameList);
		w.flush();
		w.close();
	}
	 
	/////////////////////////
	///  Program Driver
	/////////////////////////
	
	 public static void main(String[] args) throws IOException, ClassNotFoundException{
		//Timer t1 = new Timer();
		//t1.schedule( new CheckDMsTask(), 0, 14400000); // 4 hour delay in milliseconds
		//t1.schedule(new SendForecastTask(), 300000, 86400000); // 24 hour delay in milliseconds. Delay initial run by 5 minutes.			
		//checkMentions();
		 
		 Map<String, List<String>> respList = new HashMap<String, List<String>>();
			
			try {		
			  FileInputStream fin = new FileInputStream("F://School//CS 142//Final//coded_messages");
			  ObjectInputStream oin = new ObjectInputStream(fin);
				respList = (Map<String, List<String>>) oin.readObject();
			  fin.close();
			  oin.close();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		 String[] f = {"232", "51.1", "38", "2.5", "cloudy", "92.4"};
		 System.out.println(simplifyForecast(f, "Monday", 50.1, respList));
		 
		 String[] g = {"504", "51.1", "38", "2", "cloudy", "92.4"};
		 System.out.println(simplifyForecast(g, "Monday", 20, respList));
		 
		 String[] h = {"602", "51.1", "38", "19", "cloudy", "92.4"};
		 System.out.println(simplifyForecast(h, "Monday", 65, respList));
		 
		 
	 
	 }
	 
}


/////////////////////////
///  TimerTask Classes
/////////////////////////

class CheckDMsTask extends TimerTask{

	@Override
	public void run() {
		try {
			bot.checkDM();
		} catch (TwitterException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

class SendForecastTask extends TimerTask{
	
	@Override
	public void run() {
		try {
			bot.sendDailyForecasts();
		} catch (ClassNotFoundException | IOException | TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
