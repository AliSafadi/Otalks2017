package sockets;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import model.ChatUser;

/**
 * 
 */
@ServerEndpoint("/chat/Otalks/{username}")
public class ChatEndPoint{
	
	//tracks all active chat users
    private static Map<Session,ChatUser> chatUsers = Collections.synchronizedMap(new HashMap<Session,ChatUser>()); 
    
    /**
     * Joins a new client to the chat
     * @param session 
     * 			client end point session
     * @throws IOException
     */
    @OnOpen
    public void joinChat(Session session, @PathParam("username") String username, @PathParam("channelName") String channelName) throws IOException{
    	if (session.isOpen()) {
			
			//add new client to managed chat sessions
			ChatUser usr = new ChatUser(username);
			
			chatUsers.put(session,usr);
/*    	    	int A  = chatUsers.size();
*/        		//notify everyone that a new client has join the chat
			/*doNotify(null,"User <span class='username'>"+username+"</span> has joined the chat...", null,channelName);
			//welcome the new client
			session.getBasicRemote().sendText("Welcome <span class='username'>"+username +
				                          "</span>. There are currently "+channelCount(channelName)+" participants in this chat.");*/
		}
    }

    /**
     * Message delivery between chat participants
     * @param session
     * 			client end point session
     * @param msg
     * 			message to deliver		
     * @throws IOException
     */
    @OnMessage
    public void deliverChatMessege(Session session, String msg) throws IOException{
        try {
            if (session.isOpen()) {
               //deliver message
               ChatUser user = chatUsers.get(session);
               if(user == null){
            	   System.out.println("koko");
               }
               doNotify(user.username, msg, null);
            }
        } catch (IOException e) {
                /*session.close();*/
        	e.printStackTrace();
        }
    }
    
    /**
     * Removes a client from the chat
     * @param session
     * 			client end point session
     * @throws IOException
     */
    @OnClose
    public void leaveChat(Session session,@PathParam("channelName") String channelName) throws IOException{
    	/*try {*/
    		if (session.isOpen()) {
    			
    				chatUsers.remove(session);
    			
    			
    			//let other participants know that client has left the chat
    			/*for(Iterator<Map.Entry<Session, ChatUser>> it = chatUsers.entrySet().iterator(); it.hasNext(); ) {
    			      Map.Entry<Session, ChatUser> entry = it.next();
    			      if(entry.getKey().equals(user)) {
    			    	  entry.getKey().close();
    			    	  it.remove();
    			    	  break;
    			      }
    			    }*/
    			//doNotify(null,"User <span class='username'>"+"</span> has left the chat...",session);
    		}
    	/*} catch (IOException e) {
    		session.close();
        	e.printStackTrace();

    	}*/
    	session.close();
    }
   
    /*
     * Helper method for message delivery to chat participants. skip parameter is used to avoid delivering a message 
     * to a certain client (e.g., one that has just left) 
     */
    private void doNotify(String author, String message, Session skip) throws IOException{
    	for (Entry<Session,ChatUser> user : chatUsers.entrySet()){
    		Session session = user.getKey();
			if (!session.equals(skip) && session.isOpen()){
    			session.getBasicRemote().sendText(/*(author != null ? "&gt&gt <span class='username'>"+author+"</span>: " : "")+*/ message/*+ " ("+new Date()+")"*/);
    		}
    		
    		
    	}
    }
    
    /*private int channelCount(String joinedChannel){
    	int count = 0;
    	for (Entry<Session,ChatUser> user : chatUsers.entrySet()){
    		if(!user.getKey().isOpen()){
    			continue;
    		}
    		count = user.getValue().channel.equals(joinedChannel) && user.getKey() != null? count+1 : count ;
    	}
    	return count;
    }*/

}
