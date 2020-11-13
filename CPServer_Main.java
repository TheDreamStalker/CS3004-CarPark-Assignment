import java.net.*;
import java.io.*;

public class CPServer_Main {
	public static void main(String args[]) throws IOException{
		/***
		 *  THIS CLASS STARTS THE CAR PARK SERVER
		 */
		
		ServerSocket CPServerSocket = null;
		String serverName = "Car Park Server";
		
		//Make the server socket:
		try{
			CPServerSocket = new ServerSocket(4545);
		} catch(IOException e){
			System.err.println("Could not start " + serverName + "on specified port.");
			System.exit(-1);
		}
		
		//Start listening on the server
		int capacity = 5; //Size of the car park
		CarParkServer carParkServer = new CarParkServer(serverName, CPServerSocket, capacity);
		carParkServer.runServer();
		CPServerSocket.close();
	}
}
