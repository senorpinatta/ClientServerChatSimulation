public class Main {
	
	//Main method of program, establishes the server port number and spawns the clients
	public static void main(String[] args) {
		int serverPort = 8818;
		Server server = new Server(serverPort);
		server.start();
		int i;
		for (i = 0; i < 4; i++) {
			if (i == 3) {
				try {
					Thread.sleep(30000);
					Client client = new Client(serverPort);
					client.start();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			else {
				Client client = new Client(serverPort);
				client.start();
			}
		}
	}
}
