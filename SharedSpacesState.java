import java.net.*;
import java.io.*;

public class SharedSpacesState {
	private SharedSpacesState mySharedSpaceObj;
	private double mySharedVariable;
	private String myThreadName; 
	private boolean parking = false; //True if a car has a spot, false otherwise.
	private int carsWaiting = 0; //number of cars waiting.
	
	//Constructor
	SharedSpacesState(double sharedVariable) {
		mySharedVariable = sharedVariable;
	}
	
	//Attempt to acquire a space:
	public synchronized void acquireSpace() throws InterruptedException{
		Thread me = Thread.currentThread(); //Get a reference to the current thread (space)
		System.out.println(me.getName() +" is attempting to acquire a space!");
		++carsWaiting;
		while(parking){ //while someone else is parking or carsWaiting > 0
			System.out.println(me.getName()+" waiting to get a space as someone else is parking");
			//wait for the bay to be free 
			wait();
		}
		//Nobody has got a space so get one:
		--carsWaiting;
		parking = true;
		System.out.println(me.getName()+ " got a space!");
	}
	
	//Releases a space when a car is finished.
	public synchronized void releaseSpace(){
		//Release the space, tell everyone.
		parking = false;
		notifyAll();
		Thread me = Thread.currentThread(); //Get a reference to the current thread (space)
		System.out.println(me.getName()+ " released a space!");
	}
	
	public synchronized String processInput(String myThreadName, String theInput){
		System.out.println(myThreadName + " received "+ theInput);
		String theOutput = null;
		//Check what the client said:
		if(theInput.equalsIgnoreCase("Enter car park")){
			//Correct request
			if(myThreadName.equals("Car Park Space 1")){
				mySharedVariable = mySharedVariable + 20; //Temporal process, will be changed.
				System.out.println(myThreadName + " made the sharedVariable: "+ mySharedVariable);
				theOutput = "Action completed. Shared variable now = "+ mySharedVariable;
			}
			else{  //Incorrect request.
				theOutput = myThreadName + "received incorrect request. Only understand \"Enter car park!\"";
			}
		}
		//Return the message to Car Park Server
		System.out.println(theOutput);
		return theOutput;
	}
}
