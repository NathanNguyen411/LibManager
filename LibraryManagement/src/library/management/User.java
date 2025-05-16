package library.management;

public class User {
    private int userId;
    private String username;
    private String password;
    private int readerId;
    
    public User() {
    }
    
    public User(int userId, String username, String password, int readerId) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.readerId = readerId;
    }
    
    // Getters and Setters
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
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
    
    public int getReaderId() {
        return readerId;
    }
    
    public void setReaderId(int readerId) {
        this.readerId = readerId;
    }
}