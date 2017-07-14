package DataModel;

import org.json.JSONArray;

public class OrderListModel {
	private String id;
    private String user_id,datetime,ship_date,comment,quantity,itemcount;
    private JSONArray item;
    public OrderListModel(String id,String user_id,String datetime,String ship_date,String comment,String quantity,String itemcount,JSONArray item){
    	this.id = id;
    	this.user_id = user_id;
    	this.datetime = datetime;
    	this.ship_date = ship_date;
    	this.comment = comment;
    	this.quantity = quantity;
    	this.itemcount = itemcount;
    	this.item=item;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    
    public String getUserID() {
        return user_id;
    }
    public void setUserID(String data) {
        this.user_id = data;
    }
    
    public String getDateTime() {
        return datetime;
    }
    public JSONArray getItem() {
        return item;
    }
    public void setItem(JSONArray data) {
        this.item = data;
    }
    public void setDateTime(String data) {
        this.datetime = data;
    }
    
    public String getShipDate() {
        return ship_date;
    }
    public void setShipDate(String data) {
        this.ship_date = data;
    }
    
    public String getComment() {
        return comment;
    }
    public void setComment(String data) {
        this.comment = data;
    }
    
    public String getQuantity() {
        return quantity;
    }
    public void setQuantity(String data) {
        this.quantity = data;
    }
    
    public String getItemCount() {
        return itemcount;
    }
    public void setItemCount(String data) {
        this.itemcount = data;
    }
    
    
   
}
