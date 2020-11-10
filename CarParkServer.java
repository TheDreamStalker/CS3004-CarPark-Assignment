import java.net.*;
import java.io.*;

public class CarParkServer {
	public static void main(String args[]) throws IOException{
		ServerSocket CPServerSocket = null;
		String serverName = "Car Park Server";
		boolean listening = true;
		double sharedVariable = 100; //Temporal shared variable, will probably have to change it.
		
		//Create the shared object in global scope:
		SharedSpacesState sharedSpace = new SharedSpacesState(sharedVariable);
		
		//Make the server socket:
		try{
			CPServerSocket = new ServerSocket(4545);
		} catch(IOException e){
			System.err.println("Could not start " + serverName + "on specified port.");
			System.exit(-1);
		}
		System.out.println(serverName + " started!");
		
		while(listening){
			new CarParkServerSpace(CPServerSocket.accept(), "Car Park Space 1", sharedSpace).start();
			new CarParkServerSpace(CPServerSocket.accept(), "Car Park Space 2", sharedSpace).start();
		}
		CPServerSocket.close();
	}
}
