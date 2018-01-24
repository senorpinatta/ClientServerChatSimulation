import java.io.*;
import java.net.*;
import java.util.*;

//The server class of the program, stores the server port, a list of clients connected to
//the server, and a log of all the requests handled by the server
public class Server extends Thread {
	private final int serverPort;
	private ArrayList<ServerWorker> workerList = new ArrayList<>(); //list of clients connected to the server
	private ArrayList<RequestData> requestdata = new ArrayList<>(); //log of all requests handled by the server
	
	public Server(int serverPort) {
		this.serverPort = serverPort;
	}
	
	public List<ServerWorker> getWorkerList() {
		return workerList;
	}
	
	public List<RequestData> getRequestLog() {
		return requestdata;
	}
	
	public void setRequestLog(RequestData rd) {
		requestdata.add(rd);
	}
	
	//Listens and Connects new clients to the server ONLY if there are fewer than 3
	//clients already connected. Initiates automated client protocols.
	@Override
	public void run() {
		try {
			ServerSocket serverSocket = new ServerSocket(serverPort);
			while(true) {
				if (workerList.size() < 3) {
					Socket clientSocket = serverSocket.accept();
					System.out.println("Accepted connection from " + clientSocket);
					ServerWorker worker = new ServerWorker(this, clientSocket);
					workerList.add(worker);
					worker.start();
				}
				else {
					continue;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//removes client from list of connected clients
	void handleDisconnect(ServerWorker serverWorker) {
		workerList.remove(serverWorker);
	}

}
