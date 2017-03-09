import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class twittertest{
	
	// http://twitter4j.org/en/configuration.html 
	// https://www.javacodegeeks.com/2011/10/java-twitter-client-with-twitter4j.html
	
	
	// Either set these below or in twitter4j.properties file in the class path. Not both. 
	public final static String CONSUMERKEY = "";
	public final static String CONSUMERKEYSECRET = "";
	public final static String ACCESSTOKEN = "";
	public final static String ACCESSTOKENSECRET = "";

	public static void main(String[] args) throws TwitterException{
		
		Twitter t = TwitterFactory.getSingleton();  // is getSingletion() right one to use?
		t.setOAuthConsumer(CONSUMERKEY, CONSUMERKEYSECRET);
		AccessToken at = new AccessToken(ACCESSTOKEN, ACCESSTOKENSECRET);
		t.setOAuthAccessToken(at);
		
		
		// Update status
		//Status s = t.updateStatus("Playing in Java");
		//System.out.println("Updated status to " + s.getText());
		
		// Check DM (let's have users DM the bot instead of just tweeting @Weather___bot
		//List<DirectMessage> messages = t.getDirectMessages();
	}
	
	
	
}