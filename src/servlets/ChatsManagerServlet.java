package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import java.sql.Timestamp;

import db.manger.DatabaseController;
import model.PrivateMessage;
import model.PrivateReply;
import model.PrivateSubscription;
import model.PublicMessage;
import model.PublicReply;
import model.Subscription;
import model.User;
import utility.ChatManagerResponse;
import utility.MessageResponse;
import utility.NickNameResponse;
import utility.myResponse;

/**
 * Servlet implementation class ChatsManagerServlet
 */
@WebServlet("/ChatsManagerServlet")
public class ChatsManagerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

       
	private static final int GET_CHANNEL_MESSAGES = 1;
	private static final int GET_CHANNEL_REPLY = 2;
	private static final int GET_PRIVATE_MESSAGES = 3;
	private static final int GET_PRIVATE_REPLY = 4;
	private static final int INSERT_PUBLIC_MESSAGE = 5;
	private static final int INSERT_PRIVATE_MESSAGE = 6;
	private static final int INSERT_PUBLIC_REPLY = 7;
	private static final int INSERT_PPRIVATE_REPLY = 8;
	private static final int GET_NICKNAME = 9;


	/**
     * @see HttpServlet#HttpServlet()
     */
    public ChatsManagerServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 * 
	 * this servlet is responsible for IN CHAT MANAGEMENT
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int cmd = Integer.parseInt(request.getParameter("cmd"));
		
		switch(cmd){
			case GET_CHANNEL_MESSAGES: getMessages(request,response); return; //returns channel messages
			case GET_CHANNEL_REPLY: getReplys(request,response); return;      // returns message replies
			case GET_PRIVATE_MESSAGES: getMessagesPrivate(request,response); return; //private messages due to private chat
			case GET_PRIVATE_REPLY: getReplyPrivate(request,response); return; //replies on a private message
			case INSERT_PUBLIC_MESSAGE: insertPublicMessage(request,response); return; //saves new public message
			case INSERT_PRIVATE_MESSAGE: insertPrivateMessage(request,response); return; // saves new private message
			case INSERT_PUBLIC_REPLY: insertPublicReply(request,response);return; 		//insert public reply on public channel
			case INSERT_PPRIVATE_REPLY: insertPrivateReply(request,response);return;	//insert private reply inprivate chanel
			case GET_NICKNAME: getNickName(request,response);return;

		}
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * Those are simple functions that calls the database controllers and executes simple queries like was mentioned
	 * upwards. It's like mapping
	 */
	
	private void getNickName(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Gson gson = new Gson();
		DatabaseController dbc = new DatabaseController();
		User usr = null;
		
			try {
				usr = dbc.bringMeUserInfo(this.getServletContext(), request.getParameter("username"));
			} catch (SQLException | NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
		PrintWriter writer = response.getWriter();
    	writer.println(gson.toJson(new NickNameResponse(usr.getNickName(),true)));
    	writer.close();
    	return;
		
	}
	
	
	private void insertPrivateReply(HttpServletRequest request, HttpServletResponse response) throws IOException {
		DatabaseController dbc = new DatabaseController();
		Gson gson = new Gson();
		boolean flag = false;
		PrivateMessage parentMessage = null;
		PrivateMessage reply = gson.fromJson(request.getParameter("reply"), PrivateMessage.class);
		if(request.getParameter("parentMessage") != null){
			flag = !flag;
			parentMessage = gson.fromJson(request.getParameter("parentMessage"), PrivateMessage.class);
			if(parentMessage == null){
				flag=!flag;
			}
		}

		Timestamp t = new Timestamp(new Date().getTime());
		reply.setTime(t);
		reply.setLastModified(t);
		try {
			PrivateMessage message = dbc.bringMePrivateMessage(this.getServletContext(), reply.getReplyTo());
			message.setLastModified(t);
			dbc.updatePrivateMessage(this.getServletContext(),message);
			
			if(flag){
				parentMessage.setLastModified(t);
				dbc.updatePrivateMessage(this.getServletContext(),parentMessage);
			}
			 reply = dbc.insertNewPrivateReply(this.getServletContext(), reply);
			 PrivateSubscription ps = dbc.bringMePrivateChat(getServletContext(), message.getUsername(), message.getOther());
				//if(ps.getNotifTo().equals(message.getOther()))
				dbc.addNotifPrivate(getServletContext(), ps, reply.getOther(),ps.getNotif() + 1);
		} catch (NamingException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		PrintWriter writer = response.getWriter();
		writer.println(gson.toJson(new MessageResponse(true,reply)));
    	writer.close();
    	return;
		
	}


	private void insertPublicReply(HttpServletRequest request, HttpServletResponse response) throws IOException {
		DatabaseController dbc = new DatabaseController();
		Gson gson = new Gson();
		boolean flag = false;
		PublicMessage parentMessage = null;
		PublicMessage reply = gson.fromJson(request.getParameter("reply"), PublicMessage.class);
		if(request.getParameter("parentMessage") != null){
			flag = !flag;
			parentMessage = gson.fromJson(request.getParameter("parentMessage"), PublicMessage.class);
			if(parentMessage == null){
				flag=!flag;
			}
		}

		Timestamp t = new Timestamp(new Date().getTime());
		reply.setTime(t);
		reply.setLastModified(t);
		reply.setRepliable(true);
		try {
			PublicMessage message = dbc.bringMePublicMessage(this.getServletContext(), reply.getReplyTo());
			message.setLastModified(t);
			dbc.updatePublicMessage(this.getServletContext(),message);
			if(flag){
				parentMessage.setLastModified(t);
				dbc.updatePublicMessage(this.getServletContext(),parentMessage);
			}
			 reply = dbc.insertNewPublicReply(this.getServletContext(), reply);
				dbc.addNotiftoAll(getServletContext(), reply.getChannelName(), reply.getUsername());
		} catch (NamingException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		PrintWriter writer = response.getWriter();
		writer.println(gson.toJson(new MessageResponse(true,reply)));
    	writer.close();
    	return;
		
	}
	

	private void insertPrivateMessage(HttpServletRequest request, HttpServletResponse response) throws IOException {
		DatabaseController dbc = new DatabaseController();
		Gson gson = new Gson();
		PrivateMessage message = gson.fromJson(request.getParameter("message"), PrivateMessage.class);
		Timestamp t = new Timestamp(new Date().getTime());
		message.setTime(t);
		message.setLastModified(t);
		message.setIsReply(false);
		message.setRepliable(true);
		try {
			message = dbc.insertNewPrivateMessage(this.getServletContext(), message);
			PrivateSubscription ps = dbc.bringMePrivateChat(getServletContext(), message.getUsername(), message.getOther());
			//if(ps.getNotifTo().equals(message.getOther()))
			dbc.addNotifPrivate(getServletContext(), ps, message.getOther(),ps.getNotif() + 1);
		} catch (NamingException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		PrintWriter writer = response.getWriter();
		writer.println(gson.toJson(new MessageResponse(true,message)));
    	writer.close();
    	return;
		
	}
	
	private void insertPublicMessage(HttpServletRequest request, HttpServletResponse response) throws  IOException {
		DatabaseController dbc = new DatabaseController();
		Gson gson = new Gson();
		
		Timestamp t = new Timestamp(new Date().getTime());
		String chan,user,cont;
		chan = request.getParameter("channel");
		user =request.getParameter("username");
		cont = request.getParameter("content");
		String nickname = request.getParameter("nickName");
		String imgUrl = request.getParameter("imgUrl");
		PublicMessage message = new PublicMessage(chan, user, cont, t, t, false, 0, true,nickname,imgUrl);
		try {
			message = dbc.insertNewPublicMessage(this.getServletContext(), message);
			dbc.addNotiftoAll(getServletContext(), chan, user);
		} catch (NamingException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		PrintWriter writer = response.getWriter();
		writer.println(gson.toJson(new MessageResponse(true,message)));
    	writer.close();
    	return;
		
	}
	
	private void getReplyPrivate(HttpServletRequest request, HttpServletResponse response) throws IOException {
		DatabaseController dbc = new DatabaseController();
		ChatManagerResponse myResponse = new ChatManagerResponse(false,null);
		Gson gson = new Gson();
		
		PrivateMessage message = gson.fromJson(request.getParameter("message"), PrivateMessage.class); 
		ArrayList<PrivateMessage> myList = null;
		
		try {
			myList = dbc.bringMePrivateReplys(this.getServletContext(), message);
			User usr = dbc.bringMeUserInfo(this.getServletContext(), message.getUsername());
			myResponse.setNickname(usr.getNickName());
		} catch (SQLException | NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
			myResponse.setQueryResult(true);
			myResponse.data = new ArrayList<PrivateReply>();
			myResponse.setData(myList);
		
		PrintWriter writer = response.getWriter();
		writer.println(gson.toJson(myResponse));
    	writer.close();
    	return;
		
	}

	private void getMessagesPrivate(HttpServletRequest request, HttpServletResponse response) throws IOException {
		DatabaseController dbc = new DatabaseController();
		ChatManagerResponse myResponse = new ChatManagerResponse(false,null);
		Gson gson = new Gson();
		
		String username = request.getParameter("username");
		String other = request.getParameter("other");
		ArrayList<PrivateMessage> myList = null;
		try {
			myList = dbc.bringMePrivateMessages(this.getServletContext(), username, other);
		} catch (SQLException | NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
			if(myList != null){
					
						Collections.sort(myList, new Comparator<PrivateMessage>() {
						    public int compare(PrivateMessage m1, PrivateMessage m2) {
						        return m1.getLastModified().compareTo(m2.getLastModified());
						    }
						});
				}
			
			
			myResponse.setQueryResult(true);
			myResponse.data = new ArrayList<PrivateMessage>();
			myResponse.setData(myList);
		
		
		PrintWriter writer = response.getWriter();
		writer.println(gson.toJson(myResponse));
    	writer.close();
    	return;
	}

	private void getReplys(HttpServletRequest request, HttpServletResponse response) throws IOException {
		DatabaseController dbc = new DatabaseController();
		ChatManagerResponse myResponse = new ChatManagerResponse(false,null);
		Gson gson = new Gson();
		
		PublicMessage message = gson.fromJson(request.getParameter("message"), PublicMessage.class); 
		ArrayList<PublicMessage> myList = null;
		
		try {
			myList = dbc.bringMeMessageReply(this.getServletContext(),message);
			User usr = dbc.bringMeUserInfo(this.getServletContext(), message.getUsername());
			myResponse.setNickname(usr.getNickName());
		} catch (SQLException | NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
			myResponse.setQueryResult(true);
			myResponse.data = new ArrayList<PublicReply>();
			myResponse.setData(myList);
		
		PrintWriter writer = response.getWriter();
		writer.println(gson.toJson(myResponse));
    	writer.close();
    	return;
		
	}

	private void getMessages(HttpServletRequest request, HttpServletResponse response) throws IOException {
		DatabaseController dbc = new DatabaseController();
		ChatManagerResponse myResponse = new ChatManagerResponse(false,null);
		Gson gson = new Gson();
		
		String channel = request.getParameter("channel");
		String username = request.getParameter("username");
		Subscription subs = null;
		ArrayList<PublicMessage> myList = null;
		
		try {
			subs = dbc.bringMeSubscriptionInfo(this.getServletContext(),username,channel);
			if(subs != null){
				myList = dbc.bringMeChannelMessages(this.getServletContext(), channel);
			}
		} catch (SQLException | NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(myList != null){
			Timestamp time = subs.getJoinTime();
			Iterator<PublicMessage> it = myList.iterator();
			while(it.hasNext()){
				if(it.next().getLastModified().before(time)){
					it.remove();
				}
			}
			Collections.sort(myList, new Comparator<PublicMessage>() {
			    public int compare(PublicMessage m1, PublicMessage m2) {
			        return m1.getLastModified().compareTo(m2.getLastModified());
			    }
			});
		
			myResponse.setQueryResult(true);
			myResponse.data = new ArrayList<PublicMessage>();
			myResponse.setData(myList);
		}
		PrintWriter writer = response.getWriter();
		writer.println(gson.toJson(myResponse));
    	writer.close();
    	return;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
