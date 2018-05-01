package utility;





/**
 */
public interface AppConstants {

	
	public final String DB_NAME = "DB_NAME";
	public final String DB_DATASOURCE = "DB_DATASOURCE";
	public final String PROTOCOL = "jdbc:derby:"; 
	public final String OPEN = "Open";
	public final String SHUTDOWN = "Shutdown";
	
	public final String CREATE_USERS_TABLE = "CREATE TABLE USERS(Username varchar(10) PRIMARY KEY,"
											+ " Password varchar(8),"
											+ " Nickname varchar(20),"
											+ " Description varchar(50),"
											+ " Image varchar(100))";
	
	public final String SELECT_FROM_USERS = "SELECT * FROM USERS WHERE Username = ? ";
	
	public final String INSERT_USER_STMT_WITH_DESCRIPTION = 
						"INSERT INTO USERS (Username,Password,Nickname,Image,Description)"
						+ " VALUES(?,?,?,?,?)";
	
	public final String INSERT_USER_STMT2_WITHOUT_DESCRIPTION =
						"INSERT INTO USERS (Username,Password,Nickname,Image)"
						+ " VALUES(?,?,?,?)";
	
	public final String CREATE_CHANNELS_TABLE = "CREATE TABLE CHANNELS(ChannelName varchar(30) PRIMARY KEY,"
											+ " ChannelDescription varchar(500))";
	
	public final String SELECT_ALL_CHANNELS = "SELECT * FROM CHANNELS";
	
	public final String INSERT_NEW_CHANNEL = "INSERT INTO CHANNELS(ChannelName,ChannelDescription)"
											+ " VALUES(?,?)";
	
	public final String CREATE_SUBSCRIPTION_TABLE = "CREATE TABLE SUBSCRIPTION(Username varchar(10),"
													+ " NickName varchar(10),"
													+ " ChannelName varchar(30),"
													+ " JoiningTime TIMESTAMP,"
													+ " Mention BOOLEAN DEFAULT FALSE,"
													+ " Notif INTEGER DEFAULT 0,"
													+ " FOREIGN KEY(Username) REFERENCES USERS,"
													+ " FOREIGN KEY(ChannelName) REFERENCES CHANNELS )";
	
	public final String INSERT_NEW_SUBSCRIPTION = "INSERT INTO SUBSCRIPTION(Username,NickName,ChannelName,JoiningTime)"
													+ " VALUES(?,?,?,?)";
	public final String REMOVE_SUBSSCRIPTION = "DELETE FROM SUBSCRIPTION WHERE Username = ? AND ChannelName = ?";
	
	public final String ADD_MENTION = "UPDATE SUBSCRIPTION SET Mention = true WHERE ChannelName = ? AND Username = ?";
	
	public final String REMOVE_MENTION = "UPDATE SUBSCRIPTION SET Mention = false WHERE ChannelName = ? AND Username = ?";
	
	public final String ADD_NOTIF = "UPDATE SUBSCRIPTION SET Notif = ? WHERE ChannelName = ? AND Username = ?";
	
	public final String ADD_NOTIF_TO_ALL = "UPDATE SUBSCRIPTION SET Notif = (Notif + 1) WHERE ChannelName = ? AND Username != ?";

	
	public final String CREATE_PRIVATECHATS_TABLE = "CREATE TABLE PRIVATECHATS(Username varchar(10) NOT NULL,"
			+ " Other varchar(10) NOT NULL,"
			+ " NotifTo varchar(10),"
			+ " Notif INTEGER DEFAULT 0,"
			+ " FOREIGN KEY(Username) REFERENCES USERS,"
			+ " FOREIGN KEY(Other) REFERENCES USERS)";

	public final String INSERT_NEW_PRIVATECHAT = "INSERT INTO PRIVATECHATS(Username,Other)"
			+ " VALUES(?,?)";
	
	public final String SELECT_USER_PRIVATE_CHATS = "SELECT * FROM PRIVATECHATS WHERE Username = ? OR Other = ?";
	
	public final String FIND_PRIVATE_CHAT = "SELECT * FROM PRIVATECHATS WHERE (Username = ? AND Other = ?) OR "
											+ "(Username = ? AND Other = ?)";
	public final String DELETE_PRIVATECHAT_MESSAGES = "DELETE FROM PRIVATEMESSAGES WHERE "
			+ "(Username = ? AND Other = ?) OR (Other = ? AND Username = ?)";
	
	public final String REMOVE_PRIVATE_SUBSSCRIPTION = "DELETE FROM PRIVATECHATS WHERE Username = ? OR Username = ?"
			+ " AND Other = ? OR Other = ?";
	
	public final String ADD_NOTIF_PRIVATE = "UPDATE PRIVATECHATS SET NotifTo = ?, Notif = ? "
											+ "WHERE Username = ? AND Other = ?";
	public final String SELECT_PARTICIPANTS = "SELECT * FROM SUBSCRIPTION WHERE ChannelName = ?"; 
	
	public final String SELECT_USER_CHANNELS = "SELECT * FROM SUBSCRIPTION WHERE Username = ?"; 
	
	public final String CHECK_IF_SUBSCRIPTION_EXIST = "SELECT * FROM SUBSCRIPTION WHERE Username = ? AND ChannelName = ?";
	
	public final String CREATE_PUBLICMESSAGES_TABLE = "CREATE TABLE PUBLICMESSAGES("
			+ "MessageID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
			+ "ChannelName varchar(30),"
			+ "Username varchar(10),"
			+ "Content varchar(500),"
			+ "Time TIMESTAMP,"
			+ "LastModified TIMESTAMP,"
			+ "IsReply BOOLEAN,"
			+ "ReplyTo INTEGER,"
			
			+ "Repliable BOOLEAN DEFAULT TRUE,"
			+ "NickName varchar(20),"
			+ "ImgUrl varchar(100),"
			+ "FOREIGN KEY(Username) REFERENCES USERS,"
			+ "FOREIGN KEY(ChannelName) REFERENCES CHANNELS"
												+ ")";
	
	public final String INSERT_NEW_PUBLICMESSAGE = "INSERT INTO PUBLICMESSAGES(ChannelName,Username,"
														+ "Content,Time,LastModified,IsReply,NickName,ImgUrl)  VALUES(?,?,?,?,?,?,?,?)";
	
	public final String SELECT_PUBLIC_MESSAGES = "SELECT * FROM PUBLICMESSAGES WHERE ChannelName = ? AND IsReply = false"; 

	public final String INSERT_NEW_PUBLICREPLY = "INSERT INTO PUBLICMESSAGES(ChannelName,Username,"
			+ "Content,Time,LastModified,IsReply,ReplyTo,NickName,ImgUrl)  VALUES(?,?,?,?,?,?,?,?,?)";
	public final String SELECT_PUBLIC_REPLYS = "SELECT * FROM PUBLICMESSAGES WHERE ReplyTo = ?";
	
	public final String UPDATE_PUBLIC_MESSAGE = "UPDATE PUBLICMESSAGES SET LastModified = ? WHERE MessageID = ?";
	
	public final String UPDATE_ON_UNSUBSCRIPTION = "UPDATE PUBLICMESSAGES SET Repliable = false WHERE Username = ? AND ChannelName = ?";

	public final String UPDATE_ON_UNSUBSCRIPTION_PRIVATE = "UPDATE PRIVATEMESSAGES SET Repliable = false WHERE "
			+ "(Username = ? )  AND (Other = ? )";


	
	public final String CREATE_PRIVATEMESSAGES_TABLE = "CREATE TABLE PRIVATEMESSAGES("
			+ "MessageID INTEGER UNIQUE NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
			+ "Username varchar(10),"
			+ "Other varchar(10),"
			+ "Content varchar(500),"
			+ "Time TIMESTAMP,"
			+ "LastModified TIMESTAMP,"
			+ "IsReply BOOLEAN,"
			+ "ReplyTo INTEGER,"
			+ "Repliable BOOLEAN DEFAULT TRUE,"
			+ "NickName varchar(20),"
			+ "ImgUrl varchar(100),"
			+ "FOREIGN KEY(Username) REFERENCES USERS"
												+ ")";
	
	public final String INSERT_NEW_PRIVATEMESSAGE = "INSERT INTO PRIVATEMESSAGES(Username,Other,"
			+ "Content,Time,LastModified,IsReply,NickName,ImgUrl) VALUES(?,?,?,?,?,?,?,?)";
	
	public final String SELECT_PRIVATE_MESSAGES = "SELECT * FROM PRIVATEMESSAGES WHERE (Username=? OR Username = ?) "
						+ "AND (Other = ? OR Other = ?) AND IsReply = false"; 

	
	public final String UPDATE_PRIVATE_MESSAGE = "UPDATE PRIVATEMESSAGES SET LastModified = ? where MessageID = ?";


	
	public final String INSERT_NEW_PRIVATEREPLY = "INSERT INTO PRIVATEMESSAGES(Username,Other,"
			+ "Content,Time,LastModified,IsReply,ReplyTo,NickName,ImgUrl) VALUES(?,?,?,?,?,?,?,?,?)";
	
	public final String SELECT_PRIVATE_REPLY = "SELECT * FROM PRIVATEMESSAGES WHERE"
			+ " (Username=? OR Username = ?) AND (Other=? OR Other = ?) AND (ReplyTo=?)";
	
	
	public final String SELECT_SINGLE_MESSAGE = "SELECT * FROM PUBLICMESSAGES WHERE MessageID = ?";
	
	public final String SELECT_SINGLE_REPLY = "SELECT * FROM ? WHERE MessageID = ? AND ReplyID = ?";
	
	public final String SELECT_SINGLE_MESSAGE_PRIVATE = "SELECT * FROM PRIVATEMESSAGES WHERE MessageID = ?";
	

	
}
