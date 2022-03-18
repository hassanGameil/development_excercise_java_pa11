package server;

import java.io.*;
import java.net.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

class Server {

    public static void main(String[] argv) throws Exception {

        Properties props = new Properties();
        ServerSocket serverSocket;
        try {
            props.load(new FileInputStream(argv[0]));
            serverSocket = new ServerSocket(Integer.parseInt(props.getProperty("port")));
            Socket connectedSocket = serverSocket.accept();
            try (PrintWriter outToClient = new PrintWriter(connectedSocket.getOutputStream(), true)) {
                File file = new File(props.getProperty("file"));
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = reader.readLine()) != null) {
                    outToClient.println(line);
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, "Server Properties file is missing or specified location is wrong", ex);
        } catch (NumberFormatException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, "Invalid port number", ex);
        }
    }
}
