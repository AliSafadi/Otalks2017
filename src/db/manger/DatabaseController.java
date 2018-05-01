package db.manger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;


import utility.*;
import model.Channel;
import model.PrivateMessage;
import model.PrivateSubscription;
import model.PublicMessage;
import model.Subscription;
import model.User;

public class DatabaseController {
		/**
		 * This is the Database controller class.
		 * all queries to the db pass from here
		 * 
		 * */
	
	
		/**
		 * *
		 * @param servletContext
		 * @param username
		 * @return User Object which contains the user info
		 * @throws SQLException
		 * @throws NamingException
		 */
		public User bringMeUserInfo(ServletContext servletContext,String username) throws SQLException, NamingException{
				User result = null;
				Context context = new InitialContext();
	    		BasicDataSource ds = (BasicDataSource)context.lookup(
	    				servletContext.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
	    		Connection conn = ds.getConnection();
	    		PreparedStatement stmt;
	    		stmt = conn.prepareStatement(AppConstants.SELECT_FROM_USERS);
				stmt.setString(1, username);
				ResultSet rs = stmt.executeQuery();
				
				while (rs.next()){	//saving the data in user class
					result = new User(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5));
				}
				rs.close();
				conn.commit();

				stmt.close();
				conn.close();
				return result;
			
			
		}
		/**
		 * *
		 * @param servletContext
		 * @param usr
		 * @return boolean
		 * @throws NamingException
		 * @throws SQLException
		 * 
		 * this function is ressponsible for signing up a new user
		 */
		public boolean insertNewUser(ServletContext servletContext,User usr) throws NamingException, SQLException{
			boolean result = false;
			Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(
    				servletContext.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
    		Connection conn = ds.getConnection();
    		
    		PreparedStatement pstmt = conn.prepareStatement(AppConstants.INSERT_USER_STMT_WITH_DESCRIPTION);
    		pstmt.setString(1,usr.getUsername());
			pstmt.setString(2,usr.getPassword());
			pstmt.setString(3,usr.getNickName());
			pstmt.setString(4,usr.getImageUrl());
			pstmt.setString(5,usr.getDescription());
			pstmt.executeUpdate();
			
			//commit update
			conn.commit();
			//close statements
			pstmt.close();
			conn.close();

    		result = true;
			return result;
		}
		
		/**
		 * 
		 * @param servletContext
		 * @param channelName
		 * @param username
		 * @return boolean to indicate weather the operation has succeeded or not
		 * @throws SQLException
		 * @throws NamingException
		 * 
		 * this function used to save public channel mention notification
		 */
		public boolean setMention(ServletContext servletContext,String channelName,String username) throws SQLException, NamingException{
			boolean result = false;
			Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(
    				servletContext.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
    		Connection conn = ds.getConnection();
    		
    		PreparedStatement pstmt = conn.prepareStatement(AppConstants.ADD_MENTION);
    		pstmt.setString(1, channelName);
    		pstmt.setString(2, username);

    		
			
			pstmt.executeUpdate();
			
			
			//commit update
			conn.commit();
			//close statements
			pstmt.close();
			conn.close();
    		
			return true;
		}
		
		/**
		 * 
		 * @param servletContext
		 * @param channelName
		 * @param username
		 * @return boolean 
		 * @throws SQLException
		 * @throws NamingException
		 * 
		 * removes a mention notification
		 */
		
		public boolean removeMention(ServletContext servletContext,String channelName,String username) throws SQLException, NamingException{
			boolean result = false;
			Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(
    				servletContext.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
    		Connection conn = ds.getConnection();
    		
    		PreparedStatement pstmt = conn.prepareStatement(AppConstants.REMOVE_MENTION);
    		pstmt.setString(1, channelName);
    		pstmt.setString(2, username);
			
			pstmt.executeUpdate();
			
			
			//commit update
			conn.commit();
			//close statements
			pstmt.close();
			conn.close();
    		
			return true;
		}
		
		
		/**
		 * 
		 * @param servletContext
		 * @param channelName
		 * @param notif
		 * @param username
		 * @return boolean
		 * @throws SQLException
		 * @throws NamingException
		 * 
		 * set number of notifications to a given user on a given public channel
		 */
		
		
		public boolean addNotif(ServletContext servletContext,String channelName,int notif,String username) throws SQLException, NamingException{
			boolean result = false;
			Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(
    				servletContext.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
    		Connection conn = ds.getConnection();
    		
    		PreparedStatement pstmt = conn.prepareStatement(AppConstants.ADD_NOTIF);
    		pstmt.setInt(1, notif);
    		pstmt.setString(2, channelName);
    		pstmt.setString(3, username);
    		
			
			pstmt.executeUpdate();
			
			
			//commit update
			conn.commit();
			//close statements
			pstmt.close();
			conn.close();
    		
			return true;
		}
		
		
		
		
		public boolean addNotiftoAll(ServletContext servletContext,String channelName,String username) throws SQLException, NamingException{
			boolean result = false;
			Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(
    				servletContext.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
    		Connection conn = ds.getConnection();
    		
    		PreparedStatement pstmt = conn.prepareStatement(AppConstants.ADD_NOTIF_TO_ALL);
    		//pstmt.setInt(1, notif);
    		pstmt.setString(1, channelName);
    		pstmt.setString(2, username);
    		
			
			pstmt.executeUpdate();
			
			
			//commit update
			conn.commit();
			//close statements
			pstmt.close();
			conn.close();
    		
			return true;
		} 
		
		
		
		public boolean addNotifPrivate(ServletContext servletContext,PrivateSubscription ps,String notifto,int notif) throws SQLException, NamingException{
			boolean result = false;
			Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(
    				servletContext.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
    		Connection conn = ds.getConnection();
    		
    		PreparedStatement pstmt = conn.prepareStatement(AppConstants.ADD_NOTIF_PRIVATE);
    		pstmt.setString(1, notifto);
    		pstmt.setInt(2, notif);
    		pstmt.setString(3, ps.getUsername());
    		pstmt.setString(4, ps.getOther());
    		
			
			pstmt.executeUpdate();
			
			
			//commit update
			conn.commit();
			//close statements
			pstmt.close();
			conn.close();
    		
			return true;
		}
		
		
		/*public boolean addNotifPrivateChat(ServletContext servletContext,PrivateSubscription ps,String notifto){
			boolean result = false;
			Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(
    				servletContext.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
    		Connection conn = ds.getConnection();
    		
    		PreparedStatement pstmt = conn.prepareStatement(AppConstants.ADD_NOTIF_PRIVATE_CHAT);
    		pstmt.setString(1, notifto);
    		pstmt.setInt(2, notif);
    		pstmt.setString(3, ps.getUsername());
    		pstmt.setString(4, ps.getOther());
    		
			
			pstmt.executeUpdate();
			
			
			//commit update
			conn.commit();
			//close statements
			pstmt.close();
			conn.close();
    		
			return true;
		}*/
		
		/**
		 * 
		 * @param servletContext
		 * @return arraylist of channels
		 * @throws NamingException
		 * @throws SQLException
		 * 
		 * returns a list of the public channels of the application
		 */
		
		public ArrayList<Channel> bringMeChannelsList(ServletContext servletContext) throws NamingException, SQLException{
			
			Channel result = null;
			ArrayList<Channel> channelList = null;
			Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(
    				servletContext.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
    		Connection conn = ds.getConnection();
    		
    		PreparedStatement stmt;
    		stmt = conn.prepareStatement(AppConstants.SELECT_ALL_CHANNELS);
			
			ResultSet rs = stmt.executeQuery();
			
			boolean flag = true;
			
			while (rs.next()){
				if(flag){ //Initialize the list only once
					flag=!flag;
					channelList = new ArrayList<>();
				}
				result = new Channel(rs.getString(1),rs.getString(2));
				channelList.add(result);
			}
			rs.close();
			stmt.close();
			conn.close();
			
			if(channelList == null){
				return null;
			}
			return channelList;
		}
			
		/**
		 * 
		 * @param servletContext
		 * @param channel
		 * @return boolean to indicate weather the channel was entered to the database or not
		 * @throws NamingException
		 * @throws SQLException
		 */
		public boolean insertNewChannel(ServletContext servletContext,Channel channel) throws NamingException, SQLException{
			boolean result = false;
			Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(
    				servletContext.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
    		Connection conn = ds.getConnection();
    		
    		PreparedStatement pstmt = conn.prepareStatement(AppConstants.INSERT_NEW_CHANNEL);
    		pstmt.setString(1,channel.getChannelName());
			pstmt.setString(2,channel.getDescription());
			
			pstmt.executeUpdate();
			
			//commit update
			conn.commit();
			//close statements
			pstmt.close();
			conn.close();
    		result = true;
			return result;
		}
		
		/**
		 * 
		 * @param servletContext
		 * @param subscription
		 * @return boolean to determine if a subscription was successfully entered to the database
		 * @throws NamingException
		 * @throws SQLException
		 * 
		 * subscription contains info about the user and the channel he subscribed to
		 */
		public boolean insertNewSubscription(ServletContext servletContext,Subscription subscription) throws NamingException, SQLException{
			boolean result = false;
			Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(
    				servletContext.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
    		Connection conn = ds.getConnection();
    		
    		PreparedStatement pstmt = conn.prepareStatement(AppConstants.INSERT_NEW_SUBSCRIPTION);
    		pstmt.setString(1,subscription.getUsername());
			pstmt.setString(2,subscription.getNickName());
			pstmt.setString(3,subscription.getChannelName());
			pstmt.setTimestamp(4,subscription.getJoinTime());
			
			pstmt.executeUpdate();
			
			//commit update
			conn.commit();
			//close statements
			pstmt.close();
			conn.close();

    		result = true;
			return result;
		}

		/**
		 * 
		 * @param servletContext
		 * @param user
		 * @param channel
		 * @return Subscription info of the given user to the given channel
		 * @throws SQLException
		 * @throws NamingException
		 */
		public Subscription bringMeSubscriptionInfo(ServletContext servletContext,String user,String channel) throws SQLException, NamingException{
			Subscription subs = null;
			Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(
    				servletContext.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
    		Connection conn = ds.getConnection();
    		
    		PreparedStatement stmt;
    		stmt = conn.prepareStatement(AppConstants.CHECK_IF_SUBSCRIPTION_EXIST);
			stmt.setString(1, user);
			stmt.setString(2, channel);
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()){
				subs = new Subscription(rs.getString(1),rs.getString(2),rs.getString(3),rs.getTimestamp(4),rs.getBoolean(5),rs.getInt(6));
			}
			rs.close();
			stmt.close();
			conn.close();
			return subs;
		
		
	}

		/**
		 * 
		 * @param servletContext
		 * @param username
		 * @param channelName
		 * @return boolean to indicate if the subscription was successfully removed from the database
		 * @throws NamingException
		 * @throws SQLException
		 */
		public boolean removeSubscription(ServletContext servletContext,String username,String channelName) throws NamingException, SQLException{
			boolean result = false;
			Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(
    				servletContext.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
    		Connection conn = ds.getConnection();
    		
    		PreparedStatement pstmt = conn.prepareStatement(AppConstants.REMOVE_SUBSSCRIPTION);
    		pstmt.setString(1,username);
			pstmt.setString(2,channelName);
			
			pstmt.executeUpdate();
			
			//commit update
			conn.commit();
			//close statements
			
			pstmt = conn.prepareStatement(AppConstants.UPDATE_ON_UNSUBSCRIPTION);
			pstmt.setString(1, username);
			pstmt.setString(2, channelName);
			pstmt.executeUpdate();
			
			//commit update
			conn.commit();
			//close statements
			
			pstmt.close();
			conn.close();

    		result = true;
			return result;
		}

		/**
		 * 
		 * @param servletContext
		 * @param channelName
		 * @return a list of nicknames of users who are subscribed to the given channel
		 * @throws NamingException
		 * @throws SQLException
		 */
		public ArrayList<String> bringMeChannelParticipants(ServletContext servletContext,String channelName) throws NamingException, SQLException{
			ArrayList<String> participantList = null;
			Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(
    				servletContext.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
    		Connection conn = ds.getConnection();
    		
    		PreparedStatement stmt;
    		stmt = conn.prepareStatement(AppConstants.SELECT_PARTICIPANTS);
			stmt.setString(1, channelName);
			ResultSet rs = stmt.executeQuery();
			
			boolean flag = true;
			
			while (rs.next()){
				if(flag){ //Initialize the list only once
					flag=!flag;
					participantList = new ArrayList<>();
				}
				String result = new String(rs.getString(2));
				participantList.add(result);
			}
			rs.close();
			stmt.close();
			conn.close();
			
			
			return participantList;
		}

		/**
		 * 
		 * @param servletContext
		 * @param username
		 * @return a list of the given user channels list
		 * @throws SQLException
		 * @throws NamingException
		 */
		public ArrayList<Subscription> bringMeUserChannelsList(ServletContext servletContext, String username) throws SQLException, NamingException {
			Subscription result = null;
			ArrayList<Subscription> channelList = null;
			Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(
    				servletContext.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
    		Connection conn = ds.getConnection();
    		
    		PreparedStatement stmt;
    		stmt = conn.prepareStatement(AppConstants.SELECT_USER_CHANNELS);
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			
			boolean flag = true;
			
			while (rs.next()){
				if(flag){ //Initialize the list only once
					flag=!flag;
					channelList = new ArrayList<>();
				}
				result = new Subscription(rs.getString(1),rs.getString(2),rs.getString(3),rs.getTimestamp(4),rs.getBoolean(5),rs.getInt(6));
				channelList.add(result);
			}
			rs.close();
			stmt.close();
			conn.close();
			
			if(channelList == null){
				return null;
			}
			return channelList;
		}
		
		/**
		 * 
		 * @param servletContext
		 * @param username
		 * @return list of the user private chats
		 * @throws SQLException
		 * @throws NamingException
		 */
		public ArrayList<PrivateSubscription> bringMeUserPrivateChats(ServletContext servletContext, String username) throws SQLException, NamingException {
			PrivateSubscription result = null;
			ArrayList<PrivateSubscription> chatsList = new ArrayList<>();
			Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(
    				servletContext.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
    		Connection conn = ds.getConnection();
    		
    		PreparedStatement stmt;
    		stmt = conn.prepareStatement(AppConstants.SELECT_USER_PRIVATE_CHATS);
			stmt.setString(1, username);
			stmt.setString(2, username);

			ResultSet rs = stmt.executeQuery();
			
			
			while (rs.next()){
				String me,him;
				if(rs.getString(1).equals(username)){
					me = rs.getString(1);
					him = rs.getString(2);
				}
				else{
					me = rs.getString(2);
					him = rs.getString(1);
				}
				result = new PrivateSubscription(me,him,rs.getString(3),rs.getInt(4));
				chatsList.add(result);
			}
			rs.close();
			stmt.close();
			conn.close();
			
			if(chatsList.size() == 0){
				return null;
			}
			return chatsList;
		}
		
		
		public PrivateSubscription bringMePrivateChat(ServletContext servletContext, String username,String other) throws SQLException, NamingException {
			PrivateSubscription result = null;
			Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(
    				servletContext.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
    		Connection conn = ds.getConnection();
    		
    		PreparedStatement stmt;
    		stmt = conn.prepareStatement(AppConstants.FIND_PRIVATE_CHAT);
			stmt.setString(1, username);
			stmt.setString(2, other);
			stmt.setString(3, other);
			stmt.setString(4, username);

			ResultSet rs = stmt.executeQuery();
			
			
			while (rs.next()){
				result = new PrivateSubscription(rs.getString(1), rs.getString(2),
								rs.getString(3),rs.getInt(4));
			}
			rs.close();
			stmt.close();
			conn.close();
			
			
			return result;
		}
		
		/**
		 * 
		 * @param servletContext
		 * @param username
		 * @param other
		 * @return boolean to indicate weather the new private chat was saved in the database or not
		 * @throws NamingException
		 * @throws SQLException
		 */
		public boolean insertNewPrivateChat(ServletContext servletContext,String username,String other) throws NamingException, SQLException{
			boolean result = false;
			Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(
    				servletContext.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
    		Connection conn = ds.getConnection();
    		
    		PreparedStatement pstmt = conn.prepareStatement(AppConstants.INSERT_NEW_PRIVATECHAT);
    		pstmt.setString(1,username);
			pstmt.setString(2,other);

			pstmt.executeUpdate();
			
			//commit update
			conn.commit();
			//close statements

			//close statements
			pstmt.close();
			conn.close();
    		result = true;
			return result;
		}
		
		/**
		 * 
		 * @param servletContext
		 * @param message
		 * @return the message which was sent to here in order to save it in the database along with it's id
		 * @throws NamingException
		 * @throws SQLException
		 */
		public PublicMessage insertNewPublicMessage(ServletContext servletContext,PublicMessage message) throws NamingException, SQLException{
			Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(
    				servletContext.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
    		Connection conn = ds.getConnection();
    		
    		PreparedStatement pstmt = conn.prepareStatement(AppConstants.INSERT_NEW_PUBLICMESSAGE ,
    				Statement.RETURN_GENERATED_KEYS );
    		pstmt.setString(1,message.getChannelName());
			pstmt.setString(2,message.getUsername());
			pstmt.setString(3,message.getContent());
			pstmt.setTimestamp(4,message.getTime());
			pstmt.setTimestamp(5,message.getLastModified());
			pstmt.setBoolean(6,message.getIsReply());
			pstmt.setString(7, message.getNickName());
			pstmt.setString(8, message.getImgUrl());

			pstmt.executeUpdate();
			
			ResultSet rs = pstmt.getGeneratedKeys();
			while(rs.next()){
				message.setMessageID(rs.getInt(1));
				//message.setRepliable(true);
			}
			//commit update
			conn.commit();
			//close statements
			pstmt.close();
			conn.close();
    		
			return message;
		}
		/**
		 * 
		 * @param servletContext
		 * @param message
		 * @return the message which was sent here in order to update it's 'last modified time'
		 * @throws SQLException
		 * @throws NamingException
		 */
		public PublicMessage updatePublicMessage(ServletContext servletContext, PublicMessage message) throws SQLException, NamingException{
			boolean result = false;
			Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(
    				servletContext.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
    		Connection conn = ds.getConnection();
    		
    		PreparedStatement pstmt = conn.prepareStatement(AppConstants.UPDATE_PUBLIC_MESSAGE);
    		pstmt.setTimestamp(1,message.getLastModified());
    		pstmt.setInt(2, message.getMessageID());
    		
			
			pstmt.executeUpdate();
			
			
			//commit update
			conn.commit();
			//close statements
			pstmt.close();
			conn.close();
    		
			return message;
		}
		
		/**
		 * 
		 * @param servletContext
		 * @param channel
		 * @return a list of messages that were posted in the given channel
		 * @throws SQLException
		 * @throws NamingException
		 */
		public ArrayList<PublicMessage> bringMeChannelMessages(ServletContext servletContext,String channel) throws SQLException, NamingException{
			ArrayList<PublicMessage> myList = null;
			boolean created = false;
			Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(
    				servletContext.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
    		Connection conn = ds.getConnection();
    		PreparedStatement stmt;
    		stmt = conn.prepareStatement(AppConstants.SELECT_PUBLIC_MESSAGES);
			stmt.setString(1, channel);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()){
				if(!created){
					myList = new ArrayList<>();
					created = !created;
				}
				PublicMessage message = new PublicMessage(rs.getInt(1),rs.getString(2),rs.getString(3),
										rs.getString(4),rs.getTimestamp(5),rs.getTimestamp(6),rs.getBoolean(7),
										rs.getInt(8),rs.getBoolean(9),rs.getString(10),rs.getString(11));
				myList.add(message);
			}
			rs.close();
			stmt.close();
			conn.close();
			return myList;
		
		
	}
		/**
		 * 
		 * @param servletContext
		 * @param message
		 * @return the public reply that was posted in a given channel with its id
		 * @throws NamingException
		 * @throws SQLException
		 */
		public PublicMessage insertNewPublicReply(ServletContext servletContext,PublicMessage message) throws NamingException, SQLException{
			boolean result = false;
			Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(
    				servletContext.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
    		Connection conn = ds.getConnection();
    		
    		PreparedStatement pstmt = conn.prepareStatement(AppConstants.INSERT_NEW_PUBLICREPLY,
    															Statement.RETURN_GENERATED_KEYS);
    		pstmt.setString(1, message.getChannelName());
    		pstmt.setString(2, message.getUsername());
    		pstmt.setString(3, message.getContent());
    		pstmt.setTimestamp(4, message.getTime());
    		pstmt.setTimestamp(5, message.getLastModified());
    		pstmt.setBoolean(6, message.getIsReply());
    		pstmt.setInt(7, message.getReplyTo());
    		pstmt.setString(8, message.getNickName());
			pstmt.setString(9, message.getImgUrl());
			pstmt.executeUpdate();
			
			ResultSet rs = pstmt.getGeneratedKeys();
			while(rs.next()){
				message.setMessageID(rs.getInt(1));
			}
			//commit update
			conn.commit();
			//close statements
			pstmt.close();
			conn.close();
    		
			return message;
		}
		
		/**
		 * 
		 * @param servletContext
		 * @param message
		 * @return list of replies on a given message
		 * @throws SQLException
		 * @throws NamingException
		 */
		public ArrayList<PublicMessage> bringMeMessageReply(ServletContext servletContext,PublicMessage message) throws SQLException, NamingException{
			ArrayList<PublicMessage> myList = null;
			boolean created = false;
			Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(
    				servletContext.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
    		Connection conn = ds.getConnection();
    		PreparedStatement stmt;
    		stmt = conn.prepareStatement(AppConstants.SELECT_PUBLIC_REPLYS);
			stmt.setInt(1, message.getMessageID());
			ResultSet rs = stmt.executeQuery();

			while (rs.next()){
				if(!created){
					myList = new ArrayList<>();
					created = !created;
				}
				PublicMessage reply = new PublicMessage(rs.getInt(1),rs.getString(2),rs.getString(3),
						rs.getString(4),rs.getTimestamp(5),rs.getTimestamp(6),rs.getBoolean(7),
						rs.getInt(8),rs.getBoolean(9),rs.getString(10),rs.getString(11));
				myList.add(reply);
			}
			rs.close();
			stmt.close();
			conn.close();
			return myList;
		
		
	}
		
		/**
		 * 
		 * @param servletContext
		 * @param message
		 * @return the private message that was sent here in order to save to the daatabase with its id
		 * @throws NamingException
		 * @throws SQLException
		 */
		public PrivateMessage insertNewPrivateMessage(ServletContext servletContext,PrivateMessage message) throws NamingException, SQLException{
			boolean result = false;
			Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(
    				servletContext.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
    		Connection conn = ds.getConnection();
    		
    		PreparedStatement pstmt = conn.prepareStatement(AppConstants.INSERT_NEW_PRIVATEMESSAGE,
    				Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1,message.getUsername());
			pstmt.setString(2,message.getOther());
			pstmt.setString(3,message.getContent());
			pstmt.setTimestamp(4,message.getTime());
			pstmt.setTimestamp(5,message.getLastModified());
			pstmt.setBoolean(6,message.getIsReply());
			pstmt.setString(7,message.getNickName());
			pstmt.setString(8,message.getImgUrl());
			pstmt.executeUpdate();
			
			ResultSet rs = pstmt.getGeneratedKeys();
			while(rs.next()){
				message.setMessageID(rs.getInt(1));
			}
			//commit update
			conn.commit();
			//close statements
			pstmt.close();
			conn.close();
    		
			return message;
		}

		/**
		 * 
		 * @param servletContext
		 * @param username
		 * @param other
		 * @return a list of messages to a given private chat (which consist of two users)
		 * @throws SQLException
		 * @throws NamingException
		 */
		public ArrayList<PrivateMessage> bringMePrivateMessages(ServletContext servletContext,String username,String other) throws SQLException, NamingException{
			ArrayList<PrivateMessage> myList = null;
			boolean created = false;
			Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(
    				servletContext.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
    		Connection conn = ds.getConnection();
    		PreparedStatement stmt;
    		stmt = conn.prepareStatement(AppConstants.SELECT_PRIVATE_MESSAGES);
			stmt.setString(1, username);
			stmt.setString(2, other);
			stmt.setString(3, username);
			stmt.setString(4, other);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()){
				if(!created){
					myList = new ArrayList<>();
					created = !created;
				}
				PrivateMessage message = new PrivateMessage(rs.getInt(1),rs.getString(2),
												rs.getString(3),rs.getString(4),rs.getTimestamp(5),
													rs.getTimestamp(6),rs.getBoolean(7),
													rs.getInt(8),rs.getBoolean(9),rs.getString(10),rs.getString(11));
				myList.add(message);
			}
			rs.close();
			stmt.close();
			conn.close();
			return myList;
		
		
	}
		/**
		 * 
		 * @param servletContext
		 * @param message
		 * @return a private reply that was just entered to the database with it's id
		 * @throws NamingException
		 * @throws SQLException
		 */
		public PrivateMessage insertNewPrivateReply(ServletContext servletContext,PrivateMessage message) throws NamingException, SQLException{
			boolean result = false;
			Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(
    				servletContext.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
    		Connection conn = ds.getConnection();
    		
    		PreparedStatement pstmt = conn.prepareStatement(AppConstants.INSERT_NEW_PRIVATEREPLY,
    				Statement.RETURN_GENERATED_KEYS);
    		pstmt.setString(1,message.getUsername());
			pstmt.setString(2,message.getOther());
			pstmt.setString(3,message.getContent());
			pstmt.setTimestamp(4,message.getTime());
			pstmt.setTimestamp(5,message.getLastModified());
			pstmt.setBoolean(6, true);
			pstmt.setInt(7, message.getReplyTo());
			pstmt.setString(8,message.getNickName());
			pstmt.setString(9,message.getImgUrl());

			
			pstmt.executeUpdate();
			
			ResultSet rs = pstmt.getGeneratedKeys();
			while(rs.next()){
				message.setMessageID(rs.getInt(1));
			}
			//commit update
			conn.commit();
			//close statements
			pstmt.close();
			conn.close();
    		
			return message;
		}
		
		/**
		 * 
		 * @param servletContext
		 * @param msg
		 * @return a list of private replies on some given message
		 * @throws SQLException
		 * @throws NamingException
		 */
		public ArrayList<PrivateMessage> bringMePrivateReplys(ServletContext servletContext,PrivateMessage msg) throws SQLException, NamingException{
			ArrayList<PrivateMessage> myList = null;
			boolean created = false;
			Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(
    				servletContext.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
    		Connection conn = ds.getConnection();
    		PreparedStatement stmt;
    		stmt = conn.prepareStatement(AppConstants.SELECT_PRIVATE_REPLY);
			stmt.setString(1, msg.getUsername());
			stmt.setString(2, msg.getOther());
			stmt.setString(3, msg.getUsername());
			stmt.setString(4, msg.getOther());
			stmt.setInt(5, msg.getMessageID());
			ResultSet rs = stmt.executeQuery();

			while (rs.next()){
				if(!created){
					myList = new ArrayList<>();
					created = !created;
				}
				PrivateMessage reply = new PrivateMessage(rs.getInt(1),rs.getString(2),
						rs.getString(3),rs.getString(4),rs.getTimestamp(5),
						rs.getTimestamp(6),rs.getBoolean(7),
						rs.getInt(8),rs.getBoolean(9),rs.getString(10),rs.getString(11));
				
				myList.add(reply);
			}
			rs.close();
			stmt.close();
			conn.close();
			return myList;
		
		
	}
		/**
		 * 
		 * @param servletContext
		 * @param id
		 * @return a single message by given it's id
		 * @throws SQLException
		 * @throws NamingException
		 */
		public PublicMessage bringMePublicMessage(ServletContext servletContext, int id) throws SQLException, NamingException{
			PublicMessage msg = null;
			
			Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(
    				servletContext.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
    		Connection conn = ds.getConnection();
    		PreparedStatement stmt;
    		stmt = conn.prepareStatement(AppConstants.SELECT_SINGLE_MESSAGE);
			
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()){
				
				msg = new PublicMessage(rs.getInt(1),rs.getString(2),rs.getString(3),
						rs.getString(4),rs.getTimestamp(5),rs.getTimestamp(6),rs.getBoolean(7),
						rs.getInt(8),rs.getBoolean(9),rs.getString(10),rs.getString(11));;
				
			}
			rs.close();
			stmt.close();
			conn.close();
			return msg;
		}
		
		/**
		 * 
		 * @param servletContext
		 * @param id
		 * @return a single private message ny given it's id
		 * @throws NamingException
		 * @throws SQLException
		 */
		public PrivateMessage bringMePrivateMessage(ServletContext servletContext, int id) throws NamingException, SQLException{
			PrivateMessage msg = null;
			
			Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(
    				servletContext.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
    		Connection conn = ds.getConnection();
    		PreparedStatement stmt;
    		stmt = conn.prepareStatement(AppConstants.SELECT_SINGLE_MESSAGE_PRIVATE);
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()){
				
				msg = new PrivateMessage(rs.getInt(1),rs.getString(2),
						rs.getString(3),rs.getString(4),rs.getTimestamp(5),
						rs.getTimestamp(6),rs.getBoolean(7),
						rs.getInt(8),rs.getBoolean(9),rs.getString(10),rs.getString(11));;
				
			}
			rs.close();
			stmt.close();
			conn.close();
			return msg;		
		}

		/**
		 * 
		 * 
		 * @param servletContext
		 * @param message
		 * @return the message that it's 'last modified time' was updated
		 * @throws NamingException
		 * @throws SQLException
		 */
		public PrivateMessage updatePrivateMessage(ServletContext servletContext, PrivateMessage message) throws NamingException, SQLException {
			boolean result = false;
			Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(
    				servletContext.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
    		Connection conn = ds.getConnection();
    		
    		PreparedStatement pstmt = conn.prepareStatement(AppConstants.UPDATE_PRIVATE_MESSAGE);
    		pstmt.setTimestamp(1,message.getLastModified());
    		pstmt.setInt(2, message.getMessageID());
    		
			
			pstmt.executeUpdate();
			
			
			//commit update
			conn.commit();
			//close statements
			pstmt.close();
			conn.close();
    		
			return message;
			
		}

		/**
		 * 
		 * @param servletContext
		 * @param username
		 * @param other
		 * @return boolean to indicate weather the private chat was removed or not from the database
		 * @throws NamingException
		 * @throws SQLException
		 */
		public boolean removePrivateSubscription(ServletContext servletContext, String username, String other) throws NamingException, SQLException {
			
			boolean result = false;
			Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(
    				servletContext.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
    		Connection conn = ds.getConnection();
    		
    		PreparedStatement pstmt = conn.prepareStatement(AppConstants.REMOVE_PRIVATE_SUBSSCRIPTION);
    		pstmt.setString(1,username);
			pstmt.setString(2,other);
			pstmt.setString(3,username);
			pstmt.setString(4,other);
			
			pstmt.executeUpdate();
			
			//commit update
			conn.commit();
			//close statements
			
			pstmt = conn.prepareStatement(AppConstants.DELETE_PRIVATECHAT_MESSAGES);
			pstmt.setString(1, username);
			pstmt.setString(2, other);
			pstmt.setString(3, username);
			pstmt.setString(4, other);
			
			pstmt.executeUpdate();
			
			//commit update
			conn.commit();
			//close statements
			
			pstmt.close();
			conn.close();

    		result = true;
			return result;
		
		}

		
		

}
