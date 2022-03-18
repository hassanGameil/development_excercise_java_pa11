package client;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

class Client {
    public static Hashtable<String, Integer> occurrences = new Hashtable<>();
    public static void main(String argv[]) throws Exception {
        Properties props = new Properties();
        try {
            FileInputStream propsStream = new FileInputStream(argv[0]);            
            props.load(propsStream);
            FileProcessor firstFileProcessor = new FileProcessor();
            FileProcessor secondFileProcessor = new FileProcessor();
            firstFileProcessor.setHost(props.getProperty("first-host"));
            firstFileProcessor.setPort(Integer.parseInt(props.getProperty("first-port")));
            secondFileProcessor.setHost(props.getProperty("second-host"));
            secondFileProcessor.setPort(Integer.parseInt(props.getProperty("second-port")));
            Thread firstFileThread = new Thread(firstFileProcessor);
            Thread secondFileThread = new Thread(secondFileProcessor);
            firstFileThread.start();
            secondFileThread.start();
            firstFileThread.join();
            secondFileThread.join();
            propsStream.close();
            occurrences.remove("");
            int mostFrequentOccuerences = Collections.max(occurrences.values());
            ArrayList<ArrayList<String>> simplifiedOccuerences = new ArrayList<>(mostFrequentOccuerences + 1);
            int idx;
            ArrayList<String> words;
            for (int i = 0; i < mostFrequentOccuerences + 1; i++) {
                simplifiedOccuerences.add(null);
            }
            
            for (Map.Entry<String,Integer> entry : occurrences.entrySet()) {
                idx = (int) entry.getValue();
                if (simplifiedOccuerences.get(idx) == null) {
                    words = new ArrayList<>();
                } else {
                    words = simplifiedOccuerences.get(idx);
                }
                words.add((String) entry.getKey());
                    simplifiedOccuerences.set(idx, words);
            }

            for (int i = mostFrequentOccuerences, k = 0; i >= 0 && k < 5; i--) {
                if (simplifiedOccuerences.get(i) != null) {
                    for (int j = 0; j < simplifiedOccuerences.get(i).size() && k < 5; j++,k++) {
                        System.out.println("Word: '" + simplifiedOccuerences.get(i).get(j) + "', occurrences: " + i);
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, "Client Properties file is missing or specified location is wrong", ex);
        }
    }
}
