import java.net.*;
import java.io.*;

public class CarParkServer {
	private final String name;
	private final ServerSocket CPServerSocket;
	private final int[] spaces; //Each slot represents a parking space, where 0 means empty and non zero is a car.
	private int spacesFree; //The number of free spaces
	
	public CarParkServer(String name, ServerSocket CPServerSocket, int capacity){
		assert capacity > 0;
		this.name = name;
		this.CPServerSocket = CPServerSocket;
		this.spaces = new int[capacity];
		this.spacesFree = capacity;
	}
	
	public void runServer(){
		log("Car Park Server started, listening for requests...", true);
		//Spawn a thread for each request
		while(true){
			try{
				Socket socket = CPServerSocket.accept(); //received connection from a client
				new ServerRequestHandler(this, socket).start(); //Spawn a new thread to handle the connection, the same as in ActionServer
			} catch(IOException e){
				log("Could not accept incoming connection", false);
			}
		}
	}
	
	
	//A print function to log messages to the console
	public void log(String msg, boolean isStdout){
		if(isStdout){
			System.out.println(String.format("[%s] %s", name, msg));
		}
		else{
			System.err.println(String.format("[%s] %s", name, msg));
		}
	}
	
	
	//Returns the number of free spaces
	public synchronized int getFreeSpaces(){
		return spacesFree;
	}
	
	
	//Attempts to park a car in the first space available. Returns false if unsuccessful
	public synchronized boolean enterCarPark(int carID){
		if(spacesFree == 0){
			return false;
		}
		//Add the car to the first space available
		for(int i=0; i<spaces.length; i++){
			if(spaces[i] == 0){
				spaces[i] = carID;
				spacesFree--;
				return true;
			}
		}
		return false; //This shouldn't be reached but java requires you to return something.
	}
	
	
	//Attempts to exit a car, given the car ID. Same procedure as enterCarPark but in reverse
	public synchronized boolean exitCarPark(int carID){
		//Find the car given its ID
		for(int i=0; i<spaces.length; i++){
			if(spaces[i] == carID){
				spaces[i] = 0; //Change the spot the car was to empty
				spacesFree++;
				return true;
			}
		}
		log("Couldn't find car id", false);
		return false; //Could not find the car ID
	}
	
	
	//Print the parking bays, with the cars parked
	public synchronized void printCarPark(){
		StringBuilder sb = new StringBuilder();
		sb.append("[" + name + "]");
		sb.append("Parking spaces: = [");
		
		for(int i=0; i<spaces.length; i++){
			if(spaces[i] == 0){
				sb.append("X"); //X indicates the available spots
			} else{
				sb.append(spaces[i]);
			}
			if(i<spaces.length - 1){
				sb.append(",");
			}
		}
		sb.append("]");
		System.out.println(sb.toString());
	}
}
