import java.net.*;
import java.io.*;

public class EntranceClient1 {
	public static void main(String args[]) throws IOException{
		
		//Setting socket, in and out variables:
		Socket entranceSocket = null;
		PrintWriter out = null;
		BufferedReader in = null;
		String CarID = "Car 1";
		
		try{
			entranceSocket = new Socket("localhost", 4545);
			out = new PrintWriter(entranceSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(entranceSocket.getInputStream()));
		} catch(UnknownHostException e){
			System.out.println("Don't know host: localhost");
			System.exit(1);
		} catch(IOException e){
			System.out.println("Couldn't get I/O for the connection to: 4545");
		}
		
		BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
		String fromUser;
		String fromServer;
		
		System.out.println(CarID + " is waiting at entrance!");
		
		//Send the keyboard input to the server for processing:
		while(true){
			fromUser = keyboard.readLine();
			if(fromUser != null){
				System.out.println(CarID + " sending " + fromUser + " to Car Park Server");
                out.println(fromUser);
			}
			fromServer = in.readLine();
			 System.out.println(CarID + " received " + fromServer + " from Car Park Server");
		}
	}
}
