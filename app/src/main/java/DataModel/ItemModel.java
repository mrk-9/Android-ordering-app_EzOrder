package DataModel;

public class ItemModel {
	private long id;
    private String barcode;
    private String description;
    private String quantity;
    
    /*
    public ItemModel(long id,String barcode, String description, String quantity) {
    	this.id = id;
    	this.barcode = barcode;
		this.description = description;
		this.quantity = quantity;
	}*/
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBarCode() {
        return barcode;
    }
    public String getDescription() {
    	if(description.equals("null")) return "Description not found.";
        return description;
    }
    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String data) {
        this.quantity = data;
    }
    public void setDescription(String data) {
        this.description = data;
    }
    public void setBarCode(String data) {
        this.barcode = data;
    }
}
