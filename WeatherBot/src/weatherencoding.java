import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opencsv.CSVReader;

// The code below contains the data structure for the message based on the weather forecast. 
// Each combination of weather will have its own key (t, p, s1, s2, etc.) which will be used to 
// look up the correct ArrayList in the responseList. A random int generator will then be used to pull
// a response from the ArrayList. The message will be created and tested for length (?). 

public class weatherencoding{

	public static void main(String[] args) throws IOException{
		
		Map<String, List<String>> responseList = new HashMap<String, List<String>>();

		File f = new File("C://Users//Zach.Bremmer//Desktop//codes_messages.csv");
		CSVReader r = new CSVReader(new FileReader(f), ',');
		List<String[]> respCSV = r.readAll();
		r.close();
		
		for(int i = 0; i < respCSV.size(); i++){
			List<String> a = new ArrayList<>(Arrays.asList(respCSV.get(i)[1], respCSV.get(i)[2], respCSV.get(i)[3]));
			responseList.put(respCSV.get(i)[0], a);
			System.out.println(respCSV.get(i)[0]);
		}
		

		FileOutputStream f2 = new FileOutputStream("C://Users//Zach.Bremmer//Desktop//coded_messages");
		ObjectOutputStream o = new ObjectOutputStream(f2);
		o.writeObject(responseList);
		o.close();
		f2.close();
		
	}
}
		
		/*
		List<String> tp10 = new ArrayList<>(Arrays.asList("", "", ""));
		responseList.put("tp10", tp10);
				
		List<String> tp11  = new ArrayList<>(Arrays.asList("", "", ""));
		responseList.put("tp11", tp11);
		
		List<String> tp12  = new ArrayList<>(Arrays.asList("", "", ""));
		responseList.put("tp12", tp12);
		
		List<String> tp13  = new ArrayList<>(Arrays.asList("", "", ""));
		responseList.put("tp13", tp13);
		
		List<String> tp20  = new ArrayList<>(Arrays.asList("", "", ""));
		responseList.put("tp20", tp20);
		
		List<String> tp21  = new ArrayList<>(Arrays.asList("", "", ""));
		responseList.put("tp21", tp21);
		
		List<String> tp22  = new ArrayList<>(Arrays.asList("", "", ""));
		responseList.put("tp22", tp22);
		
		List<String> tp23  = new ArrayList<>(Arrays.asList("", "", ""));
		responseList.put("tp23", tp23);
		
		List<String> ts10  = new ArrayList<>(Arrays.asList("", "", ""));
		responseList.put("ts10", ts10);
		
		List<String> ts11  = new ArrayList<>(Arrays.asList("", "", ""));
		responseList.put("ts11", ts11);
		
		List<String> ts12 = new ArrayList<>(Arrays.asList("", "", ""));
		responseList.put("ts12", ts12);
		
		List<String> ts13  = new ArrayList<>(Arrays.asList("", "", ""));
		responseList.put("ts13", ts13);
		
		List<String> ts20  = new ArrayList<>(Arrays.asList("", "", ""));
		responseList.put("ts20", ts20);
		
		List<String> ts21 = new ArrayList<>(Arrays.asList("", "", ""));
		responseList.put("ts21", ts21);
		
		List<String> ts22  = new ArrayList<>(Arrays.asList("", "", ""));
		responseList.put("ts22", ts22);
		
		List<String> ts23  = new ArrayList<>(Arrays.asList("", "", ""));
		responseList.put("ts23", ts23);
		
		List<String> ts30  = new ArrayList<>(Arrays.asList("", "", ""));
		responseList.put("ts30", ts30);
		
		List<String> ts31  = new ArrayList<>(Arrays.asList("", "", ""));
		responseList.put("ts31", ts31);
		
		List<String> ts32  = new ArrayList<>(Arrays.asList("", "", ""));
		responseList.put("ts32", ts32);
		
		List<String> ts33  = new ArrayList<>(Arrays.asList("", "", ""));
		responseList.put("ts33", ts33);
		
		List<String> p10  = new ArrayList<>(Arrays.asList("", "", ""));
		responseList.put("p10", p10);
		
		List<String> p11  = new ArrayList<>(Arrays.asList("", "", ""));
		responseList.put("p11", p11);
		
		List<String> p12 = new ArrayList<>(Arrays.asList("", "", ""));
		responseList.put("p12", p12);
		
		List<String> p13  = new ArrayList<>(Arrays.asList("", "", ""));
		responseList.put("p13", p13);
		
		List<String> p20  = new ArrayList<>(Arrays.asList("", "", ""));
		responseList.put("p20", p20);
		
		List<String> p21  = new ArrayList<>(Arrays.asList("", "", ""));
		responseList.put("p21", p21);
		
		List<String> p22  = new ArrayList<>(Arrays.asList("", "", ""));
		responseList.put("p22", p22);
		
		List<String> p23  = new ArrayList<>(Arrays.asList("", "", ""));
		responseList.put("p23", p23);
		
		List<String> s10  = new ArrayList<>(Arrays.asList("", "", ""));
		responseList.put("s10", s10);
		
		List<String> s11 = new ArrayList<>(Arrays.asList("", "", ""));
		responseList.put("s11", s11);
		
		List<String> s12 = new ArrayList<>(Arrays.asList("", "", ""));
		responseList.put("s12", s12);
		
		List<String> s13  = new ArrayList<>(Arrays.asList("", "", ""));
		responseList.put("s13", s13);
		
		List<String> s20  = new ArrayList<>(Arrays.asList("", "", ""));
		responseList.put("s20", s20);
		
		List<String> s21  = new ArrayList<>(Arrays.asList("", "", ""));
		responseList.put("s21", s21);
		
		List<String> s22  = new ArrayList<>(Arrays.asList("", "", ""));
		responseList.put("s22", s22);
		
		List<String> s23 = new ArrayList<>(Arrays.asList("", "", ""));
		responseList.put("s23", s23);
		
		//EDGE CASES  
		List<String> h  = new ArrayList<>(Arrays.asList("", "", ""));
		responseList.put("h", h);
		
		List<String> t  = new ArrayList<>(Arrays.asList("", "", ""));
		responseList.put("t", t);
		
		List<String> tp1h  = new ArrayList<>(Arrays.asList("", "", ""));
		responseList.put("tp1h", tp1h);
		
		List<String> tp2h = new ArrayList<>(Arrays.asList("", "", ""));
		responseList.put("tp2h", tp2h);
		
		List<String> ts1h  = new ArrayList<>(Arrays.asList("", "", ""));
		responseList.put("ts1h", ts1h);
		
		List<String> ts2h  = new ArrayList<>(Arrays.asList("", "", ""));
		responseList.put("ts2h", ts2h);
		
		List<String> ts3h   = new ArrayList<>(Arrays.asList("", "", ""));
		responseList.put("ts3h",  ts3h);
		
		List<String> th = new ArrayList<>(Arrays.asList("", "", ""));
		responseList.put("th", th);
		
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

}*/