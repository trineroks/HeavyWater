package com.naivebayes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//An offline runnable of the NaiveBayesRunnable, used to test on the validation set

public class NaiveBayesRunnableOffline {

    public static String training = "CSV/Train.txt";
    public static String validation = "CSV/Valid.txt";

    public static HashMap<String, Integer> tagCount = new HashMap<>();
    public static HashMap<String, Integer> countCorrect = new HashMap<>();

    public static void extract(Map<String, String[]> test) throws IOException {
        File file = new File(training);
        String line = null;
        String[] s = null;
        int doc = 0;

        String content = "";

        Map<String, List<String>> m = new HashMap<>();

        try {
            BufferedReader r = new BufferedReader(new FileReader(file));
            while ((line = r.readLine()) != null) {
                s = line.split(",");
                if (s.length > 1)
                    content = s[1];
                else
                    content = "";
                if (!countCorrect.containsKey(s[0]))
                    countCorrect.put(s[0], 0);
                if (m.containsKey(s[0])) {
                    m.get(s[0]).add(content);
                } else {
                    List<String> ls = new ArrayList<>();
                    ls.add(content);
                    m.put(s[0], ls);
                }
                System.out.println("Extracting training document #" + doc);
                doc++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        for (String k : m.keySet()) {
            List<String> ls = m.get(k);
            test.put(k, ls.toArray(new String[ls.size()]));
        }
    }

    public static void test(NaiveBayes nb) throws IOException {
        File file = new File(validation);
        String line = null;
        String[] s = null;
        List<String[]> set = new ArrayList<>();
        int doc = 0;
        try {
            BufferedReader r = new BufferedReader(new FileReader(file));
            while ((line = r.readLine()) != null) {
                s = line.split(",");
                set.add(s);
                System.out.println("Extracting validation document #" + doc);
                doc++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int correct = 0;

        for (String[] str : set) {
            String outputEn;
            if (str.length < 2)
                outputEn = nb.predict("");
            else
                outputEn = nb.predict(str[1]);
            System.out.format("The document classified as \"%s\" was predicted as \"%s\".%n", str[0], outputEn);
            if (!tagCount.containsKey(str[0])) {
                tagCount.put(str[0], 1);
            } else {
                int cnt = tagCount.get(str[0]);
                cnt++;
                tagCount.put(str[0], cnt);
            }
            if (str[0].equalsIgnoreCase(outputEn)) {
                int cnt = countCorrect.get(str[0]);
                cnt++;
                countCorrect.put(str[0], cnt);
                correct++;
            }
        }
        System.out.format("This model has %f accuracy %n", ((float)correct/doc) * 100.00f);

        System.out.format("A more in depth analysis: %n");

        for (String k : tagCount.keySet()) {
            int currCorrect = countCorrect.get(k);
            int currTotal = tagCount.get(k);
            float percent = ((float) currCorrect / (float) currTotal) * 100.00f;
            System.out.format("Documents under category \"%s\": Total - %d, Correct Predictions - %d, Accuracy - %f %n", k, currTotal, currCorrect, percent);
        }
    }

    public static void run() throws IOException {

        Map<String, String[]> trainingExamples = new HashMap<>();
        extract(trainingExamples);
        
        //train classifier
        NaiveBayes nb = new NaiveBayes();
        nb.setChisquareCriticalValue(6.63); //0.01 pvalue
        nb.train(trainingExamples);
        
        //get trained classifier knowledgeBase
        NaiveBayesKnowledgeBase knowledgeBase = nb.getKnowledgeBase();
        
        nb = null;
        trainingExamples = null;
        
        
        //Use classifier
        nb = new NaiveBayes(knowledgeBase);
        test(nb);
    }
    
}
