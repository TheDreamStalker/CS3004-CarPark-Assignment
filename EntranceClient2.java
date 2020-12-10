import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Queue;

public class EntranceClient2 {
public static void main(String args[]) throws IOException{
		
		//Setting socket, in and out variables:
		Socket entranceSocket = null;
		PrintWriter out = null;
		BufferedReader in = null;
		Queue<Integer> entranceQueue = new LinkedList<>(); //The queue of cars, represented by numeric IDs
		int nextCarId = 1; //The car's ID, assume it is unique and greater than zero.
		
		BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
		String fromUser;
		String fromServer;
		
		/**
		 * //These prints below are kinda tricky since technically when this is printed it hasn't connected yet, 
		 * but if I put it on the loop it will also loop this message and I don't want that.
		 * So, let's just leave it outside, it does the job. It's just to make it more clear for the user.
		 */
		System.out.println("[ENTRANCE CLIENT 2] Up and running! Please type \"arrive\" to make a car arrive and \"enter\" to make it enter.");
		System.out.println("[ENTRANCE CLIENT 2] Please note that the order matters. However, you can make as many cars as you want wait.");
		
		//Send the keyboard input to the server for processing:
		while (true) {
            try {
                entranceSocket = new Socket("localhost", 4545);
                out = new PrintWriter(entranceSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(entranceSocket.getInputStream()));
            } catch(UnknownHostException e){
                System.out.println("Don't know host: localhost");
                System.exit(1);
            } catch(IOException e){
                System.out.println("Couldn't get I/O for the connection to: 4545");
                System.exit(1);
            }
            
          
            System.out.print("> ");
            fromUser = keyboard.readLine(); // get the user input
            if (fromUser != null) {
                if (fromUser.contains("arrive")) {  // indicate that a car is arriving with the "arrive" command
                    entranceQueue.add(nextCarId);
                    System.out.println("[ENTRANCE CLIENT 2] Car " + nextCarId + " has arrived and is now waiting.");
                    printWaitingQueue(entranceQueue);
                    nextCarId++;
                    continue;
                } 
                else if (fromUser.contains("enter")) {
                    // send the request to the server for a car to enter the car park
                    if (entranceQueue.isEmpty()) {
                        System.err.println("There are no cars waiting to enter.");
                        continue;
                    }
                    out.println("ENTER-REQUEST:" + entranceQueue.peek()+":2");   // format: "ENTER-REQUEST:X", where X is the car ID 2 for the entrance
                } 
                else {
                    System.err.println("Invalid command.");
                    continue;   // return to start of loop
                }
            }

            // wait for the server response
            fromServer = in.readLine();
            if (fromServer != null) {
                if (fromServer.equals("ALLOW")) {
                    Integer carID = entranceQueue.poll();   // remove the car from the entrance queue
                    if (carID != null) {
                        System.out.println("[ENTRANCE CLIENT 2] Car " + carID + " has been parked.");
                        printWaitingQueue(entranceQueue);
                    }
                } else if (fromServer.equals("DENY")) {
                    System.out.println("[ENTRANCE CLIENT 2] Car park is currently full, added to the waiting queue.");
                    printWaitingQueue(entranceQueue);
                }
            }
			//Tidy up
			out.close();
			in.close();
			entranceSocket.close();
		}
	}
	
	//Simple function to print the queue of cars waiting at the entrance, if it isn't empty.
	private static void printWaitingQueue(Queue<Integer> entranceQueue){
		if(!entranceQueue.isEmpty()){
			System.out.print("[ENTRANCE CLIENT 2] Waiting Queue = [");
			for(Integer car : entranceQueue){
				System.out.print(car + " ");
			}
			System.out.println("]");
		}
		else{
			System.out.println("[ENTRANCE CLIENT 2] Waiting Queue = []");
		}
	}
}