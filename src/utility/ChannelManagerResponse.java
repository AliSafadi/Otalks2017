package utility;

import java.util.ArrayList;

import model.Channel;
import model.ChannelParticipants;
import model.PrivateSubscription;
import model.Subscription;

public class ChannelManagerResponse {
	private boolean result;
	ArrayList<Channel> list;
	ArrayList<Subscription> subscribers;
	ArrayList<Subscription> subscriptions;
	ArrayList<PrivateSubscription> privateChats;
	ArrayList<ChannelParticipants> data;
	public boolean isResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
	public ArrayList<Channel> getList() {
		return list;
	}
	public void setList(ArrayList<Channel> list) {
		this.list = list;
	}
	public ArrayList<Subscription> getSubscribersList() {
		return subscribers;
	}
	public void setSubscribersList(ArrayList<Subscription> subsList) {
		this.subscribers = subsList;
	}
	public ArrayList<Subscription> getSubscribers() {
		return subscribers;
	}
	public void setSubscribers(ArrayList<Subscription> subscribers) {
		this.subscribers = subscribers;
	}
	public ArrayList<Subscription> getSubscriptions() {
		return subscriptions;
	}
	public void setSubscriptions(ArrayList<Subscription> subscriptions) {
		this.subscriptions = subscriptions;
	}
	public ArrayList<PrivateSubscription> getPrivateChats() {
		return privateChats;
	}
	public void setPrivateChats(ArrayList<PrivateSubscription> privateChats) {
		this.privateChats = privateChats;
	}
	public ArrayList<ChannelParticipants> getData() {
		return data;
	}
	public void setData(ArrayList<ChannelParticipants> data) {
		this.data = data;
	}
	
	
}
