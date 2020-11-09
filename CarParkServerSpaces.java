import java.net.*;
import java.io.*;

public class CarParkServerSpaces extends Thread {
	private Socket carParkSocket = null;
	private SharedSpacesState mySharedSpaceStateObj;
	private String myCarParkThreadName;
	private double mySharedVariable;
	
	//Setup the thread
	public CarParkServerSpaces(Socket carParkSocket, String CarParkThreadName, SharedSpacesState sharedObject){
		this.carParkSocket = carParkSocket;
		mySharedSpaceStateObj = sharedObject;
		myCarParkThreadName = CarParkThreadName;
	}
	
	public void start(){
		try{
			System.out.println(myCarParkThreadName + " is open.");
			PrintWriter out = new PrintWriter(carParkSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(carParkSocket.getInputStream()));
			String inputLine, outputLine;
			
			while((inputLine = in.readLine()) != null){
				//Get a space (lock) first:
				try{
					mySharedSpaceStateObj.acquireSpace();
					outputLine = mySharedSpaceStateObj.processInput(myCarParkThreadName, inputLine);
					out.println(outputLine);
					mySharedSpaceStateObj.releaseSpace();
				} catch(InterruptedException e){
					System.err.println("Failed to get a space when reading:"+e);
				}
			}
			
			out.close();
			in.close();
			carParkSocket.close();
			
		} catch(IOException e){
			e.printStackTrace();
		}
	}
}
