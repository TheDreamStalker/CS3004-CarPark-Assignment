import java.net.*;
import java.io.*;

//This class deals with the management of the requests that the user has written.

public class ServerRequestHandler extends Thread {
	private final CarParkServer carParkServer;
	private final Socket socket;
	
	//Create constructor
	public ServerRequestHandler(CarParkServer carParkServer, Socket socket){
		this.carParkServer = carParkServer;
		this.socket = socket;
	}
	
	//Handle the incoming request
	public void run(){
		PrintWriter out;
		BufferedReader in;
		try{
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			String request = in.readLine();
			if(request != null){
				//Split the request into two parts, as mentioned, an exit request will require the car's ID:
				String[] message = request.split(":"); 
				String reqType = message[0]; //First part of the request, either arrive, enter or exit
				String reqCarID = message[1]; //Get the car ID for the exit request
				
				
				String carEntrance = message[2]; //Number stating the entrance the car came from
				String carID = reqCarID+ "(" + carEntrance + ")";
				
				if(reqType.equals("ENTER-REQUEST")){ //handle an enter request from the EntranceClient
					boolean wasParked = carParkServer.enterCarPark(carID);
					if(wasParked){
						out.println("ALLOW"); //The car was parked successfully
						carParkServer.log("Car "+ reqCarID +" from entrance "+carEntrance +" has parked.", true);
					}
					else{
						out.println("DENY"); //Car park full, couldn't park the car
						carParkServer.log("Car "+ reqCarID +" from entrance "+carEntrance +" is waiting to enter.", true);
						return;
					}
				}
				else if(reqType.equals("EXIT-REQUEST")){
					boolean success = carParkServer.exitCarPark(carID);
					if(success){
						out.println("SUCCESSFUL"); //Car left the car park
						carParkServer.log("Car "+ reqCarID +" from entrance "+ carEntrance+ " has left the car park.", true);
					}
					else{
						out.println("UNSUCCESSFUL"); //Error happened, probably car ID was not found.
						return;
					}
				}
				else{ //If invalid request was received
					carParkServer.log("Received an invalid request: "+ reqType, false);
					return;
				}
				
				//Tidy up
				in.close();
				out.close();
				socket.close();
				
				//Print the car park after each request to show the user its current state:
				carParkServer.printCarPark();
			}
			
		} catch(IOException e){
			carParkServer.log("Error handling a request", false);
		} catch(NumberFormatException e){
			carParkServer.log("Invalid request, could not parse the car ID", false);
		} catch(IndexOutOfBoundsException e){
			carParkServer.log("Invalid request format", false);
		}
	}
}
