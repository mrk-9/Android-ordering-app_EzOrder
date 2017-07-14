package DataModel;

public class UserModel {
    private long id;
    private String user_id;
    private String token;
    private String user_name;
    private String order_status;
    private String password;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserID() {
        return user_id;
    }
    public String getUserName() {
        return user_name;
    }
    public String getPassword() {
        return password;
    }
    public String getToken() {
        return token;
    }
    public String getOrderStatus() {
        return order_status;
    }
    public void setUserID(String data) {
        this.user_id = data;
    }
    public void setOrderStatus(String data) {
        this.order_status = data;
    }
    public void setUserName(String data) {
        this.user_name = data;
    }
    public void setToken(String data) {
        this.token = data;
    }
    public void setPassword(String data) {
        this.password = data;
    }
}
