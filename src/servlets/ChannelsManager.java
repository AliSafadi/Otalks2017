package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import db.manger.DatabaseController;
import model.Channel;
import model.ChannelParticipants;
import model.PrivateSubscription;
import model.Subscription;
import model.User;
import utility.ChannelManagerResponse;
import utility.UserResponse;


/**
 * Servlet implementation class ChannelsManager
 */
@WebServlet("/ChannelsManager")
public class ChannelsManager extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/**
	 * This servlet is responsible for all types of channels in the applications
	 * below are the commands that it performes.
	 */
	private final int CHANNELSLIST = 1;  
	private final int ADDCHANNEL = 2;
	private final int SUBSCRIBE = 3;
	private final int UNSUBSCRIBE = 4;
	private final int USERPUBLICCHANNELS = 6;
	private final int USERPRIVATECHATS = 7;
	private final int ADD_PRIVATE_CHAT = 8;
	private final int UNSUBSCRIBE_PRIVATE = 9;
	private final int ADDMENTION = 10;
	private final int REMOVEMENTION = 11;
	private final int ADDNOTIF = 12;
	private final int USERIMG = 13;
	private final int ADDNOTIFPRIVATE = 14;



	
	
      
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChannelsManager() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
  
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ChannelManagerResponse myResponse = null;
		Gson gson = new Gson();
		DatabaseController dbc = new DatabaseController(); //database manager
		int command = Integer.parseInt(request.getParameter("cmd")); //parse the number of command
		/**
		 * returns a list of all public channels of the application
		 * @params: cmd = 1,
		 * 
		 */
		switch(command){
			case CHANNELSLIST:{
				
				
				ArrayList<Channel> channelList = null;
				ArrayList<ChannelParticipants> channelParticipants = null; // a list which will contain info about each
																		   // channel and it's participants
				
				try {
					channelList = dbc.bringMeChannelsList(getServletContext());
				} catch (NamingException | SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(channelList == null){
					myResponse = new ChannelManagerResponse(); //a special object returned from the servlet see utility
					myResponse.setResult(false);              //returns result=false if something wrong happened
					
				}else{
					myResponse = new ChannelManagerResponse();
					channelParticipants = new ArrayList<>();
					for(Channel chs : channelList){
						ArrayList<String> participants = null;
						try {
							participants = dbc.bringMeChannelParticipants //sql query
									(this.getServletContext(),chs.getChannelName());
						} catch (NamingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						ChannelParticipants channelObj = new ChannelParticipants(); 
						channelObj.channel = chs;
						channelObj.participants = participants;
						channelParticipants.add(channelObj);
						
					}
					//myResponse = new ChannelParticipants();
					myResponse.setData(channelParticipants);
					myResponse.setResult(true);
					//myResponse.setList(channelList);
				}
				PrintWriter writer = response.getWriter();
		    	writer.println(gson.toJson(myResponse));
		    	writer.close();
		    	return;
			}
			/**
			 * this operation expect to receive these params:
			 * @params: channel name, channel descreption, username, nickname
			 * it inserts a new channel to the database
			 */
			case ADDCHANNEL:{ 							
				myResponse = new ChannelManagerResponse();
				String chlName,chlDescreption,username,nickName;
				chlName = request.getParameter("channelName");
				chlDescreption = request.getParameter("channelDescription");
				username = request.getParameter("username");
				nickName = request.getParameter("nickName");
				
				if( chlName == null || chlDescreption == null || chlName.equals("") || username == null){
					myResponse.setResult(false);
				}
				else{
					
					try {
						 dbc.insertNewChannel(getServletContext(),new Channel(chlName,chlDescreption));
						 dbc.insertNewSubscription(getServletContext(), new Subscription(username, nickName,chlName,
								 											new Timestamp(new Date().getTime()),false,0));
						 myResponse.setResult(true);
					} catch (NamingException | SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
				}
				PrintWriter writer = response.getWriter();
		    	writer.println(gson.toJson(myResponse));
		    	writer.close();
				return;
			}
			/**
			 * subscribe operation, attach the user to his wanted channel
			 * @params: subscriber, channelName, nickName
			 */
			case SUBSCRIBE:{
				myResponse = new ChannelManagerResponse();
				String _subscriber = request.getParameter("subscriber");
				String _channelName = request.getParameter("channelName");
				String _nickName = request.getParameter("nickName");
				Subscription subs = new Subscription(_subscriber,_nickName, _channelName,
											new Timestamp(new Date().getTime()),false,0);
				if(_subscriber == null || _channelName == null){ //validations
					myResponse.setResult(false);
				}
				else{
					try {	//inserting to the db
							myResponse.setResult(dbc.insertNewSubscription(getServletContext(), subs));
						
					} catch (SQLException | NamingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				PrintWriter writer = response.getWriter();
		    	writer.println(gson.toJson(myResponse));
		    	writer.close();
				return;
			}
			/**
			 * unsubscribe -
			 * @params: subscriber, channelName
			 * removes a user from a given channel 
			 */
			case UNSUBSCRIBE:{
				myResponse = new ChannelManagerResponse();
				String _subscriber = request.getParameter("subscriber");
				String _channelName = request.getParameter("channelName");
				
				
				if(_subscriber == null || _channelName == null){
					myResponse.setResult(false);
				}
				else{
					try {
						myResponse.setResult(dbc.removeSubscription(getServletContext(), _subscriber,_channelName));
						
					} catch (SQLException | NamingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				
				PrintWriter writer = response.getWriter();
		    	writer.println(gson.toJson(myResponse));
		    	writer.close();
				return;
			}
			
			/**
			 * @params: username
			 * returns arraylist of the user channels that he's subscribed to
			 */
			case USERPUBLICCHANNELS:{
				ArrayList<Subscription> channelList = null;
				String userName = request.getParameter("username");
				if(userName == null){
					myResponse = new ChannelManagerResponse();
					myResponse.setResult(false);
				}
				else{
					try { // calls the database controller and execute the query
						channelList = dbc.bringMeUserChannelsList(getServletContext(),userName);
					} catch (NamingException | SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(channelList == null){
						myResponse = new ChannelManagerResponse();
						myResponse.setResult(true);
						
					}else{
						myResponse = new ChannelManagerResponse();
						myResponse.setResult(true);
						myResponse.setSubscriptions(channelList);
					}
				}
				
				PrintWriter writer = response.getWriter();
		    	writer.println(gson.toJson(myResponse));
		    	writer.close();
		    	return;
			}
			/**
			 * @params: username
			 * @returns: a list of the user's private chats
			 * 
			 */
			case USERPRIVATECHATS :{
				ArrayList<PrivateSubscription> chatsList = null;
				String userName = request.getParameter("username");
				if(userName == null){
					myResponse = new ChannelManagerResponse();
					myResponse.setResult(false);
				}
				else{
					try {	//calls the db controller
						chatsList = dbc.bringMeUserPrivateChats(getServletContext(),userName);
					} catch (NamingException | SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(chatsList == null){
						myResponse = new ChannelManagerResponse();
						myResponse.setResult(true);
						
					}else{
						myResponse = new ChannelManagerResponse();
						myResponse.setResult(true);
						myResponse.setPrivateChats(chatsList);
					}
				}
				
				PrintWriter writer = response.getWriter();
		    	writer.println(gson.toJson(myResponse));
		    	writer.close();
				
				return;
			}
			/**
			 * @params: username, other
			 * makes two users share a private chat
			 */
			case ADD_PRIVATE_CHAT:{
				myResponse = new ChannelManagerResponse();
				String username = request.getParameter("username");
				String other = request.getParameter("other");
				if(username == null || other == null){
					myResponse.setResult(false);
				}
				else{
					
						try {	
							
							myResponse.setResult(dbc.insertNewPrivateChat(getServletContext(), username,other));
						
					} catch (SQLException | NamingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
				}
				
				PrintWriter writer = response.getWriter();
		    	writer.println(gson.toJson(myResponse));
		    	writer.close();
				return;
			}
			/**
			 * @params: username,other
			 * removes a private chat shared by two users (the chat is removed from the two users)
			 */
			case UNSUBSCRIBE_PRIVATE:{
				myResponse = new ChannelManagerResponse();
				String username = request.getParameter("username");
				String other = request.getParameter("other");
				
				
				if(username == null || other == null){
					myResponse.setResult(false);
				}
				else{
					try {
							myResponse.setResult(dbc.removePrivateSubscription(getServletContext(), username,other));
						
					} catch (SQLException | NamingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				
				PrintWriter writer = response.getWriter();
		    	writer.println(gson.toJson(myResponse));
		    	writer.close();
				return;
			}
			
			/**
			 * @params: username, channelName
			 * whenever a user mentioned in a public chat the notification is sent here to be saved until he sees the reply
			 */
			case ADDMENTION:{
				myResponse = new ChannelManagerResponse();
				String username = request.getParameter("username");
				String channelName = request.getParameter("channelName");
				
				
				if(username == null || channelName == null){
					myResponse.setResult(false);
				}
				else{
					try {
							myResponse.setResult(dbc.setMention(getServletContext(),channelName,username));
						/*}*/
					} catch (SQLException | NamingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				
				PrintWriter writer = response.getWriter();
		    	writer.println(gson.toJson(myResponse));
		    	writer.close();
				return;
			}
			/**
			 * removes a mention notification
			 */
			case REMOVEMENTION:{
				myResponse = new ChannelManagerResponse();
				String username = request.getParameter("username");
				String channelName = request.getParameter("channelName");
				
				
				if(username == null || channelName == null){
					myResponse.setResult(false);
				}
				else{
					try {
							myResponse.setResult(dbc.removeMention(getServletContext(),channelName,username));
						/*}*/
					} catch (SQLException | NamingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				
				PrintWriter writer = response.getWriter();
		    	writer.println(gson.toJson(myResponse));
		    	writer.close();
				return;
			}
			/**
			 * @params: username,channelName,number of notifications that awaits for the user to open
			 */
			case ADDNOTIF:{
				myResponse = new ChannelManagerResponse();
				String username = request.getParameter("username");
				String channelName = request.getParameter("channelName");
				int notif = Integer.parseInt(request.getParameter("notif"));
				
				
				if(username == null || channelName == null){
					myResponse.setResult(false);
				}
				else{
					try {
							myResponse.setResult(dbc.addNotif(getServletContext(),channelName,notif,username));
						
					} catch (SQLException | NamingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				
				PrintWriter writer = response.getWriter();
		    	writer.println(gson.toJson(myResponse));
		    	writer.close();
				return;
			}
			case USERIMG:{
				String username = request.getParameter("username");
				UserResponse myRs = new UserResponse();
				User usr = null;
				
					try {
							usr = dbc.bringMeUserInfo(getServletContext(), username);
						
					} catch (SQLException | NamingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				
				myRs.result = true;
				myRs.imgUrl = usr.getImageUrl();
				
				PrintWriter writer = response.getWriter();
		    	writer.println(gson.toJson(myRs));
		    	writer.close();
				return;
			}
			case ADDNOTIFPRIVATE:{
				myResponse = new ChannelManagerResponse();
				String username = request.getParameter("username");
				String other = request.getParameter("other");
				String notifto = request.getParameter("notifto");
				int notif = Integer.parseInt(request.getParameter("notif"));
				
				if(username == null || other == null || notifto == null){
					myResponse.setResult(false);
				}
				else{
					try {
						PrivateSubscription ps = dbc.bringMePrivateChat(getServletContext(), username,other);
						if(ps != null)
							myResponse.setResult(dbc.addNotifPrivate(getServletContext(),ps,notifto,notif));
						
					} catch (SQLException | NamingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				
				PrintWriter writer = response.getWriter();
		    	writer.println(gson.toJson(myResponse));
		    	writer.close();
				return;
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	
}
