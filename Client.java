import java.io.*;
import java.net.*;
import java.util.*;

public class Client extends Thread {
	private final int serverPort;
	private Socket clientSocket;
	
	public Client(int serverPort) {
		this.serverPort = serverPort;
	}

	//The logic for a client object.
	//Automated
	//Between 1-4 seconds makes a request.
	public void run() {
		try {
			clientSocket = new Socket("127.0.0.1", serverPort);
			BufferedReader in = new BufferedReader(new InputStreamReader (clientSocket.getInputStream()));
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			Random rand = new Random();
			String data;
			String[] requests = new String[] {"http://clientusage.com", "http://www.facebook.com", "http://www.gmail.com", "http://www.reddit.com", "quit"};
			int counter = 0;
			while (true) {
				data = requests[rand.nextInt(4)];
				Client.sleep(rand.nextInt(4000)+1000);
				out.println(data);
				out.flush();
				String response = in.readLine();
				System.out.println(response);
				//Restrict the number of iterations the automated client can perform for.
				//(Only limits platinum clients)
				if (counter == 10 | response.equalsIgnoreCase("Client 1 has self-terminated its connection to the server") | response.equalsIgnoreCase("Client 2 has self-terminated its connection to the server") | response.equalsIgnoreCase("Client 3 has self-terminated its connection to the server") | response.equalsIgnoreCase("Assigned Requests Depleted, disconnecting from server (Client 1)") | response.equalsIgnoreCase("Assigned Requests Depleted, disconnecting from server (Client 2)") | response.equalsIgnoreCase("Assigned Requests Depleted, disconnecting from server (Client 3)")) {
					break;
				}
				counter++;
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
