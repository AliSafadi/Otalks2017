package listeners;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;


import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;


import utility.AppConstants;

/**
 * Application Lifecycle Listener implementation class ManageDB
 *
 */
@WebListener
public class ManageDB implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public ManageDB() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent event)  { 
    	ServletContext cntx = event.getServletContext();
   	 	
        //shut down database
   	 try {
    		//obtain CustomerDB data source from Tomcat's context and shutdown
    		Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(
    				cntx.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.SHUTDOWN);
    		ds.getConnection();
    		System.gc();

    		ds = null;
		} catch (SQLException | NamingException e) {
			cntx.log("Error shutting down database",e);
		}
    	/*try {
			DriverManager.getConnection("jdbc:derby:BigOTalk;shutdown=true");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent event)  { 
    	ServletContext cntx = event.getServletContext();
    	try{
    		//obtain CustomerDB data source from Tomcat's context
    		Context context = new InitialContext();
    		BasicDataSource ds = (BasicDataSource)context.lookup(
    				cntx.getInitParameter(AppConstants.DB_DATASOURCE) + AppConstants.OPEN);
    		Connection conn = ds.getConnection();
    		
    		boolean created = false;
    		try{
    			//create Customers table
    			Statement stmt = conn.createStatement();
    			stmt.executeUpdate(utility.AppConstants.CREATE_USERS_TABLE);
    			//commit update
        		conn.commit();
        		stmt.close();
        		
        		stmt = conn.createStatement();
        		stmt.executeUpdate(utility.AppConstants.CREATE_CHANNELS_TABLE);
        		conn.commit();
        		stmt.close();
        		
        		stmt = conn.createStatement();
        		stmt.executeUpdate(utility.AppConstants.CREATE_SUBSCRIPTION_TABLE);
        		conn.commit();
        		stmt.close();
        		
        		stmt = conn.createStatement();
        		stmt.executeUpdate(utility.AppConstants.CREATE_PUBLICMESSAGES_TABLE);
        		conn.commit();
        		stmt.close();
        		
        		
        		
        		stmt = conn.createStatement();
        		stmt.executeUpdate(utility.AppConstants.CREATE_PRIVATEMESSAGES_TABLE);
        		conn.commit();
        		stmt.close();
        		
        		stmt = conn.createStatement();
        		stmt.executeUpdate(utility.AppConstants.CREATE_PRIVATECHATS_TABLE);
        		conn.commit();
        		stmt.close();
        		
        		
        		
    		}catch (SQLException e){
    			//check if exception thrown since table was already created (so we created the database already 
    			//in the past
    			created = tableAlreadyExists(e);
    			if (!created){
    				throw e;//re-throw the exception so it will be caught in the
    				//external try..catch and recorded as error in the log
    			}
    		}
    		
    		//if no database exist in the past - further populate its records in the table
    		if (!created){
    			//populate customers table with customer data from json file
    			/*PreparedStatement pstmt = conn.prepareStatement(AppConstants.INSERT_USER_STMT_WITH_DESCRIPTION);
    			
    				pstmt.setString(1,"root");
    				pstmt.setString(2,"none");
    				pstmt.setString(3,"none");
    				pstmt.setString(4,"none");
    				pstmt.setString(5,"none");
    				pstmt.executeUpdate();
    			

    			//commit update
    			conn.commit();
    			pstmt.close();
    			pstmt = conn.prepareStatement(AppConstants.INSERT_NEW_CHANNEL);
    			pstmt.setString(1, "GENERAL");
    			pstmt.setString(2, "Welcome to OTalks");
    			pstmt.executeUpdate();
    			
    			conn.commit();
    			//close statements
    			pstmt.close();*/
    		}
    		//close connection
    		conn.close();

    	} catch ( SQLException | NamingException e) {
    		//log error 
    		cntx.log("Error during database initialization",e);
    	}
    	
    	
    }
    
    private boolean tableAlreadyExists(SQLException e) {
        boolean exists;
        if(e.getSQLState().equals("X0Y32")) {
            exists = true;
        } else {
            exists = false;
        }
        return exists;
    }
    
	
}
