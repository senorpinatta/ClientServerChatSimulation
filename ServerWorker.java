import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.*;

public class ServerWorker extends Thread {
	private final Server server;
	private final Socket clientSocket;
	private final String clientName;
	private final int clientPort;
	public double remainingRequests;
	public String status;

	//Constructor for the logic on the server side that connects to a client.
	//Determines client name, status, remaining requests
	public ServerWorker(Server server, Socket clientSocket) {
		this.server = server;
		this.clientSocket = clientSocket;
		if (server.getWorkerList().size() == 0) {
			this.clientName = "Client 1";
		}
		else if (server.getWorkerList().size() == 1) {
			this.clientName = "Client 2";
		}
		else {
			this.clientName = "Client 3";
		}
		this.clientPort = clientSocket.getPort();
		if (clientPort % 10 == 0) {
			status = "platinum";
			remainingRequests = Double.POSITIVE_INFINITY;
		}
		else if (clientPort % 2 == 1) {
			status = "gold";
			remainingRequests = 5;
		}
		else {
			status = "silver";
			remainingRequests = 3;
		}
		System.out.println(clientName + " (" + status + ") " + "port is " + clientPort);
	}
	
	@Override
	public void run() {
		try {
			handleClient();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//Handles all input from clients.
	private void handleClient() throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader (clientSocket.getInputStream()));
		PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
		String data;
		while ( (data = in.readLine()) != null) {
			if (data.equalsIgnoreCase("quit")) {
				out.println(this.clientName + " has self-terminated its connection to the server");
				out.flush();
				closeConnections(in, out);
				break;
			}
			else if (data.equalsIgnoreCase("http://clientusage.com") && this.status.equalsIgnoreCase("platinum")) {
				platinumRequest(out);
			}
			else if (data.equalsIgnoreCase("http://www.facebook.com")) {
				if (checkRequest()) {
					logRequest(data);
					out.println("Facebook (" + clientName + ")");
					out.flush();
				}
				else {
					out.println("Assigned Requests Depleted, disconnecting from server ("+ clientName + ")");
					closeConnections(in, out);
					break;
				}
			}
			else if (data.equalsIgnoreCase("http://www.gmail.com")) {
				if (checkRequest()) {
					logRequest(data);
					out.println("Gmail (" + clientName + ")");
					out.flush();
				}
				else {
					out.println("Assigned Requests Depleted, disconnecting from server ("+ clientName + ")");
					closeConnections(in, out);
					break;
				}
			}
			else if (data.equalsIgnoreCase("http://www.reddit.com")) {
				if (checkRequest()) {
					logRequest(data);
					out.println("Reddit (" + clientName + ")");
					out.flush();
				}
				else {
					out.println("Assigned Requests Depleted, disconnecting from server ("+ clientName + ")");
					closeConnections(in, out);
					break;
				}
			}
			else {
				out.println("Server does not understand request, please try again. (" + clientName + ")");
				out.flush();
			}
		}
	}

	//Stores a record of a client's request on the server.
	private void logRequest(String data) {
		RequestData rd = new RequestData(this.clientName, this.status, this.remainingRequests, LocalDateTime.now(), true, data);
		server.setRequestLog(rd);
	}

	//Outputs the Network Usage Details for Platinum clients.
	private void platinumRequest(PrintWriter out) {
		
		//Commented out due to clarity issues. The logic is sound but output
		//is too erratic without semaphore control.
		
//		List<ServerWorker> clients = server.getWorkerList();
//		for (ServerWorker c : clients) {
//			out.println(c.clientName + " is Active");
//			out.flush();
//		}
		
		out.println(this.clientName + " Network Usage Request\n" + "Request Number    Client Name    Status     Remaining Requests           URL Request                Request Status         Time Completed");
		out.flush();
		List<RequestData> rd = server.getRequestLog();
		int i;
		for (i = 0; i < rd.size(); i++) {
			out.println("       " + i + "          " + rd.get(i).clientName + "       " + rd.get(i).status + "              " + rd.get(i).remainingRequests +"             " + rd.get(i).request + "        " + rd.get(i).requestCompleted + "         " + rd.get(i).timeNow);
			out.flush();
		}
		
	}

	//A method to close all connections between client and server.
	private void closeConnections(BufferedReader in, PrintWriter out) throws IOException {
		in.close();
		out.close();
		clientSocket.close();
		server.handleDisconnect(this);
	}

	//Returns true if client has sufficient URL requests remaining to make another request.
	//False otherwise.
	private boolean checkRequest() {
		if (this.remainingRequests >= 1) {
			remainingRequests -= 1;
			return true;
		}
		else {
			return false;
		}	
	}

}
