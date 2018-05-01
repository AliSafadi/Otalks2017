package model;

import java.sql.Timestamp;

public class Subscription {
	private String username;
	private String nickName;
	private String channelName;
	private Timestamp joinTime;
	private boolean mention;
	private int notif;
	
	public Subscription(String username,String nickName, String channelName,Timestamp joinTime,boolean mention,int notif) {
		super();
		this.username = username;
		this.channelName = channelName;
		this.joinTime = joinTime;
		this.nickName = nickName;
		this.mention = mention;
		this.notif = notif;
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

	public Timestamp getJoinTime() {
		return joinTime;
	}

	public void setJoinTime(Timestamp joinTime) {
		this.joinTime = joinTime;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public boolean isMention() {
		return mention;
	}

	public void setMention(boolean mention) {
		this.mention = mention;
	}

	public int getNotif() {
		return notif;
	}

	public void setNotif(int notif) {
		this.notif = notif;
	}
	
	
}
