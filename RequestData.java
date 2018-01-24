import java.time.LocalDateTime;

//Just a class that holds Client Request information
public class RequestData {
	public String clientName;
	public String status;
	double remainingRequests;
	public LocalDateTime timeNow;
	public String requestCompleted;
	public String request;

	public RequestData(String clientName, String status, double remainingRequests, LocalDateTime now, boolean completed, String data) {
		this.clientName = clientName;
		this.status = status;
		this.remainingRequests = remainingRequests;
		this.timeNow = now;
		if (completed) {
			this.requestCompleted = "Completed Request";
		}
		else {
			this.requestCompleted = "Request Incomplete";
		}
		this.request = data;
	}

}
