package model;

/**
 * 
 * @author Baselscs
 *	User model
 */
public class User {
	
	private String username;
	private String password;
	private String nickName;
	private String description;
	private String imageUrl;
	
	public User(){
		super();
	}
	
	public User(String username, String password, String nickName, String description, String imageUrl) {
		super();
		this.username = username;
		this.password = password;
		this.nickName = nickName;
		this.imageUrl = imageUrl;
		this.description = description;
	}
	
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public boolean equals(User other){
		return this.getUsername().equals(other.getUsername());
	}
	
}
