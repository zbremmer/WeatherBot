import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// The code below contains the data structure for the message based on the weather forecast. 
// Each combination of weather will have its own key (t, p, s1, s2, etc.) which will be used to 
// look up the correct ArrayList in the responseList. A random int generator will then be used to pull
// a response from the ArrayList. The message will be created and tested for length (?). 

public class weatherencoding{

	public static void main(String[] args) throws IOException{
		
	
		Map<String, List<String>> responseList = new HashMap<String, List<String>>();
	
		List<String> t3 = new ArrayList<>(Arrays.asList("Warm, cold.. Make up your mind!", "What's with this weather?"));
		responseList.put("t3", t3);
				
		List<String> t2 = new ArrayList<>(Arrays.asList("Crazy temp change again", ""));
		responseList.put("t2", t2);
		
		List<String> t1 = new ArrayList<>(Arrays.asList("", ""));
		responseList.put("t1", t1);
		
		List<String> t0 = new ArrayList<>(Arrays.asList("", ""));
		responseList.put("t0", t0);
		
		List<String> p3 = new ArrayList<>(Arrays.asList("Here comes the ocean!", "Raining cats and dogs!"));
		responseList.put("p3", p3);
		
		List<String> p2 = new ArrayList<>(Arrays.asList("Stay dry out there!", ""));
		responseList.put("p2", p2);
		
		List<String> p1 = new ArrayList<>(Arrays.asList("Some people feel the rain. Others just get wet.", "Rain rain go away..."));
		responseList.put("p1", p1);
		
		List<String> p0 = new ArrayList<>(Arrays.asList("Rain rain go away...", ""));
		responseList.put("p0", p0);
		
		List<String> s13 = new ArrayList<>(Arrays.asList("", ""));
		responseList.put("s13", s13);
		
		List<String> s12 = new ArrayList<>(Arrays.asList("", ""));
		responseList.put("s12", s12);
		
		List<String> s11 = new ArrayList<>(Arrays.asList("", ""));
		responseList.put("s11", s11);
		
		List<String> s10 = new ArrayList<>(Arrays.asList("", ""));
		responseList.put("s10", s10);
		
		List<String> s23 = new ArrayList<>(Arrays.asList("Time to binge some Netflix!", ""));
		responseList.put("s23", s23);
		
		List<String> s22 = new ArrayList<>(Arrays.asList("", ""));
		responseList.put("s22", s22);
		
		List<String> s21 = new ArrayList<>(Arrays.asList("Guess you won't be going out this weekend.", "Do you want to build a snowman?"));
		responseList.put("s21", s21);
		
		List<String> s20 = new ArrayList<>(Arrays.asList("", ""));
		responseList.put("s20", s20);
		
		List<String> h = new ArrayList<>(Arrays.asList("The sky is falling!", "Make sure you wear a helmet."));
		responseList.put("h", h);
		
		List<String> tp3 = new ArrayList<>(Arrays.asList("Starting the week with a bang.", "Just in time to start the week. Of course."));
		responseList.put("tp3", tp3);
		
		List<String> tp2 = new ArrayList<>(Arrays.asList("", ""));
		responseList.put("tp2", tp2);
		
		List<String> tp1 = new ArrayList<>(Arrays.asList("", ""));
		responseList.put("tp1", tp1);
		
		List<String> tp0 = new ArrayList<>(Arrays.asList("", ""));
		responseList.put("tp0", tp0);
		
		List<String> ts13 = new ArrayList<>(Arrays.asList("", ""));
		responseList.put("ts13", ts13);
		
		List<String> ts12 = new ArrayList<>(Arrays.asList("", ""));
		responseList.put("ts12", ts12);
		
		List<String> ts11 = new ArrayList<>(Arrays.asList("", ""));
		responseList.put("ts11", ts11);
		
		List<String> ts10 = new ArrayList<>(Arrays.asList("", ""));
		responseList.put("ts10", ts10);
		
		List<String> ts23 = new ArrayList<>(Arrays.asList("", ""));
		responseList.put("ts23", ts23);
		
		List<String> ts22 = new ArrayList<>(Arrays.asList("", ""));
		responseList.put("ts22", ts22);
		
		List<String> ts21 = new ArrayList<>(Arrays.asList("", ""));
		responseList.put("ts21", ts21);
		
		List<String> ts20 = new ArrayList<>(Arrays.asList("", ""));
		responseList.put("ts20", ts20);
		
		List<String> ps13 = new ArrayList<>(Arrays.asList("", ""));
		responseList.put("ps13", ps13);
		
		List<String> ps12 = new ArrayList<>(Arrays.asList("", ""));
		responseList.put("ps12", ps12);
		
		List<String> ps11 = new ArrayList<>(Arrays.asList("", ""));
		responseList.put("ps11", ps11);
		
		List<String> ps10 = new ArrayList<>(Arrays.asList("", ""));
		responseList.put("ps10", ps10);
		
		List<String> ps23 = new ArrayList<>(Arrays.asList("", ""));
		responseList.put("ps23", ps12);
		
		List<String> ps22 = new ArrayList<>(Arrays.asList("", ""));
		responseList.put("ps22", ps22);
		
		List<String> ps21 = new ArrayList<>(Arrays.asList("", ""));
		responseList.put("ps21", ps21);
		
		List<String> ps20 = new ArrayList<>(Arrays.asList("", ""));
		responseList.put("ps20", ps20);
			
		List<String> s1h3 = new ArrayList<>(Arrays.asList("", ""));
		responseList.put("s1h3", s1h3);
		
		List<String> s1h2 = new ArrayList<>(Arrays.asList("", ""));
		responseList.put("s1h2", s1h2);
		
		List<String> s1h1 = new ArrayList<>(Arrays.asList("", ""));
		responseList.put("s1h1", s1h1);
		
		List<String> s1h0 = new ArrayList<>(Arrays.asList("", ""));
		responseList.put("s1h0", s1h0);
		
		List<String> s2h3 = new ArrayList<>(Arrays.asList("", ""));
		responseList.put("s2h3", s2h3);
		
		List<String> s2h2 = new ArrayList<>(Arrays.asList("", ""));
		responseList.put("s2h2", s2h2);
		
		List<String> s2h1 = new ArrayList<>(Arrays.asList("", ""));
		responseList.put("s2h1", s2h1);
		
		List<String> s2h0 = new ArrayList<>(Arrays.asList("", ""));
		responseList.put("s2h0", s2h0);
		
		//EDGE CASES - Single response is okay. 
		List<String> ps1h = new ArrayList<>(Arrays.asList("", ""));
		responseList.put("ps1h", ps1h);
		
		List<String> ps2h = new ArrayList<>(Arrays.asList("", ""));
		responseList.put("ps2h", ps2h);
		
		List<String> ps1ht = new ArrayList<>(Arrays.asList("", ""));
		responseList.put("ps1ht", ps1ht);
		
		List<String> ps2ht = new ArrayList<>(Arrays.asList("", ""));
		responseList.put("ps2ht", ps2ht);
		
		// Save the object so it doesn't need to be rebuilt each time
		// import java.io.*;
		FileOutputStream f = new FileOutputStream("C://Users//Zach.Bremmer//Desktop//test");
		ObjectOutputStream o = new ObjectOutputStream(f);
		o.writeObject(responseList);
		o.close();
		f.close();
		
		// Heres how to open the object
		// import java.io.*;
		Map<String, List<String>> a = new HashMap<String, List<String>>();
		
		try {		
		  FileInputStream fin = new FileInputStream("C://Users//Zach.Bremmer//Desktop//test");
		  ObjectInputStream oin = new ObjectInputStream(fin);
			a = (Map<String, List<String>>) oin.readObject();
		  fin.close();
		  oin.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Method to grab response -- int in last get() should be random. 
		responseList.get("t3").get(1);

	}

}