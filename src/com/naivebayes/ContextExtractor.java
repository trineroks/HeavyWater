package com.naivebayes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

//Used to generate the training and validation subsets, as well as extra information.

public class ContextExtractor {

    public void extract(String path) throws IOException {
        File file = new File(path);

        BufferedReader r = null;
        BufferedWriter bw = null;
        FileWriter w = null;

        String line = null;
        String[] s = null;
        HashSet<String> uniqueWords = new HashSet<>();
        HashMap<String, Integer> uniqueTags = new HashMap<>();

        int doc = 0;

        try {
            r = new BufferedReader(new FileReader(file));

            while ((line = r.readLine()) != null) {
                s = line.split(",");
                int i = 0;
                System.out.println("Scanning document #" + doc);
                for (String str : s) {
                    if (i == 0) {
                        if (!uniqueTags.containsKey(str))
                            uniqueTags.put(str, 1);
                        else {
                            int cnt = uniqueTags.get(str);
                            cnt++;
                            uniqueTags.put(str, cnt);
                        }
                    }
                    else {
                        if (!uniqueWords.contains(str)) {
                            uniqueWords.add(str);
                        }
                    }
                    i++;
                }
                doc++;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        FileWriter tw = new FileWriter("CSV/Train.txt");
        BufferedWriter btw = new BufferedWriter(tw);

        FileWriter vw = new FileWriter("CSV/Valid.txt");
        BufferedWriter bvw = new BufferedWriter(vw);

        r = new BufferedReader(new FileReader(file));

        HashMap<String, Integer> mapCounts = new HashMap<>();

        for (String tags : uniqueTags.keySet()) {
            int totals = uniqueTags.get(tags);
            //Some document types have less than 400 occurences. In these cases,
            //place 90% of the documents in the training set so that we can
            //test with the remaining 10%. Otherwise, grab the first 400
            //occurences.
            int subsetCount = (int)Math.min((float)totals * 0.9f, 400);
            mapCounts.put(tags, subsetCount);
        }

        while ((line = r.readLine()) != null) {
            String[] str = line.split(",");
            int cnt = mapCounts.get(str[0]);
            if (cnt > 0) {
                cnt--;
                mapCounts.put(str[0], cnt);
                btw.write(line);
                btw.newLine();
            } else {
                bvw.write(line);
                bvw.newLine();
            }
        }

        w = new FileWriter("CSV/Output.txt");
        bw = new BufferedWriter(w);

        bw.write("There are " + uniqueWords.size() + " unique words in this dataset.");
        bw.newLine();

        bw.write("There are " + uniqueTags.size() + " unique tags. The unique tags are:");
        bw.newLine();

        for (String k : uniqueTags.keySet()) {
            bw.write(k + " (" + uniqueTags.get(k) + ")");
            bw.newLine();
        }

        if (bw != null)
            bw.close();
        if (w != null)
            w.close();
        if (tw != null)
            tw.close();
        if (btw != null)
            btw.close();
        if (vw != null)
            vw.close();
        if (bvw != null)
            bvw.close();

    }
}
