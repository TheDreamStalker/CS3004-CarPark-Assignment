import java.net.*;
import java.io.*;

public class CarParkServer {
	public static void main(String args[]) throws IOException{
		ServerSocket CPServerSocket = null;
		String serverName = "Car Park Server";
		boolean listening = true;
		double sharedVariable = 100; //Temporal shared variable, will probably have to change it.
		
		SharedSpacesState sharedSpace = new SharedSpacesState(sharedVariable);
		
		try{
			CPServerSocket = new ServerSocket(4545);
		} catch(IOException e){
			System.err.println("Could not start " + serverName + "on specified port.");
			System.exit(-1);
		}
		System.out.println(serverName + " started!");
		
		while(listening){
			new CarParkServerSpaces(CPServerSocket.accept(), "Car Park Space 1", sharedSpace).start();
		}
	}
}
