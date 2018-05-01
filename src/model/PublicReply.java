package model;

import java.sql.Timestamp;

public class PublicReply {
	private int replyID;
	private String username;
	private String channelName;
	private Timestamp time;
	private String content;
	private int messageID;
	
	public PublicReply(int replyID,int messageID, String channelName,String username, String content, Timestamp time) {
		super();
		this.replyID = replyID;
		this.username = username;
		this.channelName = channelName;
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

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
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
