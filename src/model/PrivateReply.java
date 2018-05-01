package model;

import java.sql.Timestamp;

public class PrivateReply {
	private int replyID;
	private String username;
	private String other;
	private Timestamp time;
	private String content;
	private int messageID;
	
	public PrivateReply(int replyID,int messageID, String username,String other,  String content, Timestamp time) {
		super();
		this.replyID = replyID;
		this.username = username;
		this.other = other;
		this.time = time;
		this.content = content;
		this.messageID = messageID;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getMessageID() {
		return messageID;
	}

	public void setMessageID(int messageID) {
		this.messageID = messageID;
	}

	public int getReplyID() {
		return replyID;
	}

	public void setReplyID(int replyID) {
		this.replyID = replyID;
	}
	
	
	
	
}
