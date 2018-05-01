package utility;

import model.User;

public class LoginResponse {
		boolean logInResult;
		User usr;

		public LoginResponse(boolean logInResult) {
			super();
			this.logInResult = logInResult;
		}

		public boolean isLogInResult() {
			return logInResult;
		}

		public void setLogInResult(boolean logInResult) {
			this.logInResult = logInResult;
		}

		public User getUsr() {
			return usr;
		}

		public void setUsr(User usr) {
			this.usr = usr;
		}

		public LoginResponse(boolean logInResult, User usr) {
			super();
			this.logInResult = logInResult;
			this.usr = usr;
		}
		
		
}
