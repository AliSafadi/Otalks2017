package utility;

public class SessionResponse {
	boolean queryResult;
	String username;
	String nickName;
	
	public boolean isQueryResult() {
		return queryResult;
	}

	public void setQueryResult(boolean queryResult) {
		this.queryResult = queryResult;
	}

	public SessionResponse(boolean queryResult,String username,String nickname) {
		super();
		this.queryResult = queryResult;
		this.username = username;
		this.nickName = nickname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
}
