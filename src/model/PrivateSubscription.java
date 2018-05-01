package model;
/**
 * 
 * @author Baselscs
 * A model class that represents a private subscription between two users
 */
public class PrivateSubscription {
	private String username;
	private String other;
	private String notifTo;
	private int notif;
	
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
	public PrivateSubscription(String username, String other,String notifTo,int notif) {
		super();
		this.username = username;
		this.other = other;
		this.notifTo = notifTo;
		this.notif = notif;
	}
	public String getNotifTo() {
		return notifTo;
	}
	public void setNotifTo(String notifTo) {
		this.notifTo = notifTo;
	}
	public int getNotif() {
		return notif;
	}
	public void setNotif(int notif) {
		this.notif = notif;
	}
	
	
	
	
}
