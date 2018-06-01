package com.naivebayes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NaiveBayesRunnable {
	
	NaiveBayes nb = null;
	
	private static NaiveBayesRunnable runnable;
	
	private NaiveBayesRunnable() {
		this.initialize();
	}
	
	public static NaiveBayesRunnable getInstance() {
		if (runnable == null) {
			runnable = new NaiveBayesRunnable();
		}
		return runnable;
	}
	
	public void extract(Map<String, String[]> test) throws IOException {
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("Train.txt");
        String line = null;
        String[] s = null;

        String content = "";

        Map<String, List<String>> m = new HashMap<>();

        BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
        while ((line = r.readLine()) != null) {
            s = line.split(",");
            if (s.length > 1)
                content = s[1];
            else
                content = "";
            if (m.containsKey(s[0])) {
                m.get(s[0]).add(content);
            } else {
                List<String> ls = new ArrayList<>();
                ls.add(content);
                m.put(s[0], ls);
            }
        }

        for (String k : m.keySet()) {
            List<String> ls = m.get(k);
            test.put(k, ls.toArray(new String[ls.size()]));
        }
    }
	    
	public void initialize() {

		Map<String, String[]> trainingExamples = new HashMap<>();
	        
	    try {
	    	extract(trainingExamples);
	    } catch (IOException e) {
	       	e.printStackTrace();
	    }
	        

	    nb = new NaiveBayes();
	    nb.setChisquareCriticalValue(6.63);
	    nb.train(trainingExamples);
	        
	    NaiveBayesKnowledgeBase knowledgeBase = nb.getKnowledgeBase();
	        
	    nb = null;
	    trainingExamples = null;
	        
	    nb = new NaiveBayes(knowledgeBase);
	}
	    
	public String getCategory(String s) {
	  	String output = "";
	   	output = nb.predict(s);
	   	return output;
	}
    
}
