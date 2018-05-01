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
import model.PublicMessage;
import model.User;
import utility.myResponse;

/**
 * Servlet implementation class SignUpServlet
 */
@WebServlet("/SignUpServlet")
public class SignUpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SignUpServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*// TODO Auto-generated method stub
		doGet(request, response);*/
		User user = null;
		Gson gson = new Gson();
		DatabaseController dbc = new DatabaseController();
		User myUser = gson.fromJson(request.getParameter("data"), User.class);


		try {
			user = dbc.bringMeUserInfo(this.getServletContext(), myUser.getUsername());
		} catch (SQLException | NamingException e) {
			getServletContext().log("Error while closing connection", e);
    		response.sendError(500);//internal server error
		}
		if(user != null){
			PrintWriter writer = response.getWriter();
	    	writer.println(gson.toJson(new myResponse(false)));
	    	writer.close();
			return;
		}
		/*User usr = new User();
		usr.setUsername(request.getParameter("username"));
		usr.setPassword(request.getParameter("password"));
		usr.setNickName(request.getParameter("nickName"));
		String description = request.getParameter("description");
		description = (description != null)? description: "";
		usr.setDescription(description);
		usr.setImageUrl(request.getParameter("imageUrl"));*/
		
		boolean result = false;
		
		try {
			result = dbc.insertNewUser(getServletContext(), myUser);
		} catch (NamingException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		request.getSession().setAttribute("Logged", new Boolean(true));
		request.getSession().setAttribute("username", myUser.getUsername());
		request.getSession().setAttribute("nickname", myUser.getNickName());
		PrintWriter writer = response.getWriter();
    	writer.println(gson.toJson(new myResponse(result)));
    	writer.close();
	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
}
