import java.net.*;
import java.io.*;

public class CarParkServerSpace extends Thread {
	private Socket carParkSocket = null;
	private SharedSpacesState mySharedSpaceStateObj;
	private String myCarParkSpaceName;
	private double mySharedVariable;
	
	//Setup the thread
	public CarParkServerSpace(Socket carParkSocket, String CarParkSpaceName, SharedSpacesState sharedObject){
		this.carParkSocket = carParkSocket;
		mySharedSpaceStateObj = sharedObject;
		myCarParkSpaceName = CarParkSpaceName;
	}
	
	public void run(){
		try{
			System.out.println(myCarParkSpaceName + " is available.");
			PrintWriter out = new PrintWriter(carParkSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(carParkSocket.getInputStream()));
			String inputLine, outputLine;
			
			while((inputLine = in.readLine()) != null){
				//Get a space (lock) first:
				try{
					mySharedSpaceStateObj.acquireSpace();
					outputLine = mySharedSpaceStateObj.processInput(myCarParkSpaceName, inputLine);
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
