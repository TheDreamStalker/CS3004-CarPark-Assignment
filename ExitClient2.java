import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ExitClient2 {
public static void main(String args[]) throws IOException{
		
		//Setting socket, in and out variables:
		Socket exitSocket = null;
		PrintWriter out = null;
		BufferedReader in = null;
		
		BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
		String fromUser;
		String fromServer;
		
		//Same story with this print as with the ones in the entrance client:
		System.out.println("[EXIT CLIENT 2] Up and running! Please type the car you wish to leave by using the following syntax: \"exit x\" where x is the car's number");
		
		while(true){
			  try {
	                exitSocket = new Socket("localhost", 4545);
	                out = new PrintWriter(exitSocket.getOutputStream(), true);
	                in = new BufferedReader(new InputStreamReader(exitSocket.getInputStream()));
	            } catch(UnknownHostException e){
	                System.out.println("Don't know host: localhost");
	                System.exit(1);
	            } catch(IOException e){
	                System.out.println("Couldn't get I/O for the connection to: 4545");
	                System.exit(1);
	            }

			  	String carID = "";
	            String carEntrance = "";
	            System.out.print("> ");
	            fromUser = keyboard.readLine(); // get the user input
	            try{
	            	 if (fromUser != null) {
	 	                if (fromUser.contains("exit")) {  // input should be in the form "exit X", where X is the car ID
	 	                    carID = fromUser.split(" ")[1]; // get the car ID number from user input
	 	                    carEntrance = fromUser.split(" ")[2]; //get the car entrance
	 	                } 
	 	                else {
	 	                    System.err.println("Invalid command.");
	 	                    continue;   // return to start of loop
	 	                }
	 	            }

	 	            out.println("EXIT-REQUEST:" + carID + ":" +carEntrance);   // send the exit request with the car ID
	            }
	           catch(Exception e){
	        	   System.out.println("Invalid request.");
	        	   continue;
	           }
	            
	            // wait for the server response
	            fromServer = in.readLine();
	            if (fromServer != null) {
	                if (fromServer.equals("SUCCESSFUL")) {
	                    System.out.println("[EXIT CLIENT 2] Car " + carID +" from entrance " + carEntrance+" has left the car park through exit 2");
	                } 
	                else if (fromServer.equals("UNSUCCESSFUL")) {
	                    System.out.println("[EXIT CLIENT 2] Car " + carID +" from entrance " + carEntrance+" is not currently in the car park (car doesn't exist).");
	                }
	            }

	            out.close();
	            in.close();
	            exitSocket.close();
		}
	}
}
