/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hghassan
 */
public class FileProcessor implements Runnable {

    /**
     * @param host the host to set
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * @param port the port to set
     */
    public void setPort(int port) {
        this.port = port;
    }

    private String host;
    private int port;

    @Override
    public void run() {
        Hashtable<String, Integer> occurrences = Client.occurrences;
        try {
            String FromServer;
            Socket clientSocket = new Socket(host, port);
            try (BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                while (true) {
                    FromServer = inFromServer.readLine();
                    if (FromServer == null) {
                        break;
                    }
                    for (String word : FromServer.toLowerCase().replaceAll("[^a-zA-Z]", " ").trim().split(" ")) {
                        if (occurrences.containsKey(word)) {
                            occurrences.put(word, occurrences.get(word) + 1);
                        } else {
                            occurrences.put(word, 1);
                        }
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(FileProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
