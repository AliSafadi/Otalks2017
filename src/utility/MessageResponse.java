package utility;

import model.PrivateMessage;
import model.PublicMessage;

public class MessageResponse {
	boolean queryResult;
	PublicMessage pMessage;
	PrivateMessage prMessage;

	public boolean isQueryResult() {
		return queryResult;
	}

	public void setQueryResult(boolean queryResult) {
		this.queryResult = queryResult;
	}

	public MessageResponse(boolean queryResult, PrivateMessage prMessage) {
		super();
		this.queryResult = queryResult;
		this.prMessage = prMessage;
	}

	public MessageResponse(boolean queryResult, PublicMessage pMessage) {
		super();
		this.queryResult = queryResult;
		this.pMessage = pMessage;
	}

	
}
