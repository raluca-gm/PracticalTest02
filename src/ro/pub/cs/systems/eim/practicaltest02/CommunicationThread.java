package ro.pub.cs.systems.eim.practicaltest02;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class CommunicationThread extends Thread {

    private ServerThread serverThread;
    private Socket socket;

    public CommunicationThread(ServerThread serverThread, Socket socket) {
        this.serverThread = serverThread;
        this.socket = socket;
    }

    @Override
    public void run() {
        if (socket != null) {
            try {
                BufferedReader bufferedReader = Utilities.getReader(socket);
                PrintWriter printWriter = Utilities.getWriter(socket);
                if (bufferedReader != null && printWriter != null) {
                    Log.i(Constants.TAG, "[COMMUNICATION THREAD] Waiting for command from client!");
                    String command = bufferedReader.readLine();
                    String result = "";
                    HashMap<String, TimeInfo> data = serverThread.getData();
                    TimeInfo timeInformation = null;
                    if (command != null && !command.isEmpty()) {
                    	
                        if (command.contains(",")) {
                        	String[] parts = command.split(",");
                        	TimeInfo ti = new TimeInfo(parts[1], parts[2]);
                        	//timeInformation.hh = parts[1];
                        	//timeInformation.mm = parts[2];
                        	serverThread.setData(parts[0], timeInformation);
                        	result = "Data is set";
                        } else {
                        	if (command.compareTo("reset") == 0) {
                        		serverThread.removeData();
                        		result = "Reset";
                        	}
                        	if (command.compareTo("poll") == 0) {
                                Log.i(Constants.TAG, "[COMMUNICATION THREAD] Getting the information from the webservice...");
                                HttpClient httpClient = new DefaultHttpClient();
                                HttpPost httpPost = new HttpPost(Constants.WEB_SERVICE_ADDRESS);
                                
                        	}
                        }
                        printWriter.println(result);
                        printWriter.flush();
                    } 
                }
                socket.close();
            } catch (IOException ioException) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
                if (Constants.DEBUG) {
                    ioException.printStackTrace();
                }
            }
        }
    }
 }
