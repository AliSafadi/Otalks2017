package model;

import java.sql.Timestamp;
	/**
	 * public message model
	 * @author Baselscs
	 *
	 */
public class PublicMessage {
	private int messageID;
	private String channelName;
	private String username;
	private String content;
	private Timestamp time;
	private Timestamp lastModified;
	private boolean isReply;
	private int replyTo;
	private boolean repliable;
	private String nickName;
	private String imgUrl;
	




	public PublicMessage(int messageID, String channelName, String username, String content, Timestamp time,
			Timestamp lastModified, boolean isReply, int replyTo, boolean repliable, String nickName,String imgUrl) {
		super();
		this.messageID = messageID;
		this.channelName = channelName;
		this.username = username;
		this.content = content;
		this.time = time;
		this.lastModified = lastModified;
		this.isReply = isReply;
		this.replyTo = replyTo;
		this.repliable = repliable;
		this.nickName = nickName;
		this.imgUrl = imgUrl;
	}
	
	public PublicMessage( String channelName, String username, String content, Timestamp time,
			Timestamp lastModified, boolean isReply, int replyTo, boolean repliable, String nickName,String imgUrl) {
		super();
		this.channelName = channelName;
		this.username = username;
		this.content = content;
		this.time = time;
		this.lastModified = lastModified;
		this.isReply = isReply;
		this.replyTo = replyTo;
		this.repliable = repliable;
		this.nickName = nickName;
		this.imgUrl = imgUrl;
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



	public Timestamp getLastModified() {
		return lastModified;
	}



	public void setLastModified(Timestamp lastModified) {
		this.lastModified = lastModified;
	}



	public boolean getIsReply() {
		return isReply;
	}



	public void setIsReply(boolean isReply) {
		this.isReply = isReply;
	}



	public int getReplyTo() {
		return replyTo;
	}



	public void setReplyTo(int replyTo) {
		this.replyTo = replyTo;
	}



	public boolean isRepliable() {
		return repliable;
	}



	public void setRepliable(boolean repliable) {
		this.repliable = repliable;
	}



	public void setReply(boolean isReply) {
		this.isReply = isReply;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	
	
	
	
	
	
}
