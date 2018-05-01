package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import db.manger.DatabaseController;
import utility.SessionResponse;


/**
 * Servlet implementation class SessionManager
 */
@WebServlet("/SessionManager")
public class SessionManager extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SessionManager() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Gson gson = new Gson();
		final int LOGIN = 1 ,LOGOUT = 2;
		int cmd = Integer.parseInt(request.getParameter("cmd"));
		SessionResponse result = null;
		//upon login this servlet checks weather the current session belnog to a logged user  
		switch(cmd){
		case LOGIN:
			{
				HttpSession userSession = request.getSession();
				
				Boolean attribute = (Boolean) userSession.getAttribute("Logged");
				String username = (String) userSession.getAttribute("username");
				String nickName = (String) userSession.getAttribute("nickname");
				result = new SessionResponse(false,username,nickName );
				
				if(attribute != null ){
					if(attribute == true){
						result.setQueryResult(true); //if he's already logged in then returns a pass sign
					}
				}
				PrintWriter writer = response.getWriter();
				writer.println(gson.toJson(result));
		    	writer.close();
		    	return;
			}
		case LOGOUT:
			{ //upon logout the session is closed
				HttpSession userSession = request.getSession();
				userSession.removeAttribute("Logged");
				userSession.removeAttribute("username");
				userSession.invalidate();
				result = new SessionResponse(true, null,null);
				PrintWriter writer = response.getWriter();
				writer.println(gson.toJson(result));
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
