package utility;

import java.util.ArrayList;

public class ChatManagerResponse {
	private boolean queryResult;
	public ArrayList<? extends Object> data;
	String nickname;
	
	
	public ChatManagerResponse(boolean queryResult, ArrayList<? extends Object> data) {
		super();
		this.queryResult = queryResult;
		this.data = data;
	}
	public boolean isQueryResult() {
		return queryResult;
	}
	public void setQueryResult(boolean queryResult) {
		this.queryResult = queryResult;
	}
	public ArrayList<? extends Object> getData() {
		return data;
	}
	public void setData(ArrayList<? extends Object> data) {
		this.data = data;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	
}
