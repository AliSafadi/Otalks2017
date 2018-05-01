package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import db.manger.DatabaseController;
import model.User;
import utility.LoginResponse;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet(
		description = "A servlet to validate username and password on Login",
		urlPatterns = {
				"/LoginServlet"
		})
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Gson gson = new Gson();
		boolean decision = false;
		DatabaseController dbc = new DatabaseController();
		User user = null;
		try {	//brings user info due to his username
			user = dbc.bringMeUserInfo(this.getServletContext(), request.getParameter("username"));
		} catch (SQLException | NamingException e) {
			getServletContext().log("Error while closing connection", e);
    		response.sendError(500);//internal server error
		}
		if(user == null){
			decision = false;
		}
		else{ //validate the [assword
			if(user.getPassword().equals(request.getParameter("password"))){
				decision = true;
				//sets attributes in order to maintain the session
				request.getSession().setAttribute("Logged", new Boolean(true));
				request.getSession().setAttribute("username", request.getParameter("username"));
				request.getSession().setAttribute("nickname", user.getNickName());
			}
			else{
				decision = false;
			}
		}
		PrintWriter writer = response.getWriter();
    	writer.println(gson.toJson(new LoginResponse(decision,user)));
    	writer.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
