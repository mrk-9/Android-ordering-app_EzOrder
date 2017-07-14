package DataModel;

public class OrderModel {
	private long id;
    private String datetime,ship_date,comment,quantity;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDateTime() {
        return datetime;
    }
    public String getShipDate() {
        return ship_date;
    }
    public String getComment() {
        return comment;
    }
    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String data) {
        this.quantity = data;
    }
    public void setDateTime(String data) {
        this.datetime = data;
    }
    public void setComment(String data) {
        this.comment = data;
    }
    public void setShipDate(String data) {
        this.ship_date = data;
    }
}
