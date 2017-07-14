package com.pak.androidproject;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import DataModel.OrderListModel;
import DataModel.UserModel;
import Utils.DataSourceManager;
import Utils.JSONParser;
import Utils.OrderAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class OrderListActivity  extends Activity {
	public static DataSourceManager datasource;
	public static ListView orderlist;
	public String token, user_id,order_status;
	public OrderListModel OrderListObject;
	OrderAdapter adapter;
	List<OrderListModel> AllOrder = new ArrayList<OrderListModel>();
	private static final String GetOrder_URL="http://ezordersolution.com/ordershark/index.php/server/get_orders";
	JSONParser jsonParser = new JSONParser();
	private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_orderlist);
        
        /****get From DATA base********/
        datasource = new DataSourceManager(this);
        datasource.open();
        List<UserModel> values = datasource.getAllDatas("tbmember");
        user_id=String.valueOf(values.get(0).getUserID());
        Log.v("aaaavvvv",String.valueOf(values.get(0).getUserID()));
        token=values.get(0).getToken();
        order_status=values.get(0).getOrderStatus();
        Log.v("aaaaToken",token);
        /*******************************/
        orderlist= (ListView) findViewById(R.id.OrderListView);
        adapter = new OrderAdapter(getApplicationContext(),R.layout.orderrow);
        orderlist.setAdapter(adapter);
        new GetOrder().execute();
        
        orderlist.setClickable(true);
        orderlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          @Override
          public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

        	OrderListObject = (OrderListModel) orderlist.getItemAtPosition(position);
        	  if(order_status.equals("2"))
              {
        	  AlertDialog.Builder builder = new AlertDialog.Builder(OrderListActivity.this);
              builder.setTitle("ALERT")
              .setMessage("Do you want to delete old order?")
              .setCancelable(false)
              .setPositiveButton("ok",
                  new DialogInterface.OnClickListener() {
                      public void onClick(DialogInterface dialog,
                              int id) {
                      	dialog.cancel();
	                        ContentValues values = new ContentValues();
	    					values.put("datetime",OrderListObject.getDateTime());
	    					values.put("ship_date",OrderListObject.getShipDate());
	    					values.put("comment",OrderListObject.getComment());
	    					values.put("quantity",OrderListObject.getQuantity());
	    					datasource.emptyData("tborder");
	    					datasource.insertData("tborder", values);
	    					
	    					datasource.emptyData("tbitem");
	    					JSONArray items=OrderListObject.getItem();
	    					for(int i =0;i <items.length();i++)
	    					{
	    						try {
	    							values = new ContentValues();
	    							values.put("barcode", items.getJSONObject(i).getString("barcode"));
	    							values.put("description",items.getJSONObject(i).getString("description"));
	    							values.put("quantity",  items.getJSONObject(i).getString("quantity"));
	    							datasource.insertData("tbitem", values);
	    						} catch (JSONException e) {
	    							// TODO Auto-generated catch block
	    							e.printStackTrace();
	    						} 
	    					}
	    					Intent intent = new Intent(OrderListActivity.this, OrderEdit1Activity.class);
	                        startActivity(intent);
                      }
                  })
              .setNegativeButton("cancel",
                      new DialogInterface.OnClickListener() {
                          public void onClick(DialogInterface dialog,
                                  int id) {

                              //dialog.cancel();
                          }
                      });
              AlertDialog alert = builder.create();
              alert.show();
              }
        	  else{
	        		  datasource.emptyData("tborder");
	                  datasource.emptyData("tbitem");
        		  	ContentValues values = new ContentValues();
					values.put("datetime",OrderListObject.getDateTime());
					values.put("ship_date",OrderListObject.getShipDate());
					values.put("comment",OrderListObject.getComment());
					values.put("quantity",OrderListObject.getQuantity());
					datasource.emptyData("tborder");
					datasource.insertData("tborder", values);
					
					datasource.emptyData("tbitem");
					JSONArray items=OrderListObject.getItem();
					for(int i =0;i <items.length();i++)
					{
						
						try {
							values = new ContentValues();
							values.put("barcode", items.getJSONObject(i).getString("barcode"));
							values.put("description",items.getJSONObject(i).getString("description"));
							values.put("quantity",  items.getJSONObject(i).getString("quantity"));
							datasource.insertData("tbitem", values);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 
						
					}
					Intent intent = new Intent(OrderListActivity.this, OrderEdit1Activity.class);
                    startActivity(intent);
        	  }
        }
          });
        final Button button = (Button) findViewById(R.id.listback);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent intent = new Intent(OrderListActivity.this, MenuActivity.class);
                startActivity(intent);
            	finish();
            }
        });
        final Button orderlistplus_button = (Button) findViewById(R.id.orderlistplus);
        orderlistplus_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	final List<UserModel> values_click = datasource.getAllDatas("tbmember");
                user_id=String.valueOf(values_click.get(0).getUserID());
                token=values_click.get(0).getToken();
                order_status=values_click.get(0).getOrderStatus();
            	if(order_status.equals("1"))
                {
	            	AlertDialog.Builder builder = new AlertDialog.Builder(OrderListActivity.this);
	                builder.setTitle("ALERT")
	                .setMessage("Do you want to create new order?")
	                .setCancelable(false)
	                .setPositiveButton("ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                    int id) {
                            	dialog.cancel();
    	                        datasource.emptyData("tborder");
    	                        datasource.emptyData("tbitem");
    	                        ContentValues values = new ContentValues();
    	            			values.put("order_status", "2");
    	            			datasource.updateData("tbmember", values, "user_id='"+user_id+"'");
    	                        Intent intent = new Intent(OrderListActivity.this, OrderAddActivity.class);
    	                        startActivity(intent);
                            }
                        })
	                .setNegativeButton("cancel",
	                        new DialogInterface.OnClickListener() {
	                            public void onClick(DialogInterface dialog,
	                                    int id) {
	
	                                //dialog.cancel();
	                            }
	                        });
	                AlertDialog alert = builder.create();
	                alert.show();
                }else{
                	Intent intent = new Intent(OrderListActivity.this, OrderAddActivity.class);
                    startActivity(intent);
                }   
            }
        });
    }
class GetOrder extends AsyncTask<String, String, String> {
		
		boolean failure = false;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(OrderListActivity.this);
			pDialog.setMessage("Download Data..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}
		
		@Override
		protected String doInBackground(String...args) {
			//Check for success tag
			String success;
			
			 try {
				 //Building Parameters
				 List<NameValuePair> params = new ArrayList<NameValuePair>();
				 params.add(new BasicNameValuePair("token", token));
				 
				 JSONObject json = jsonParser.makeHttpRequest (
						 GetOrder_URL, "GET", params);
				 JSONObject message=json.getJSONObject("message");
				 success = message.getString("type");
				 if(success.equals("success") ) {
					 JSONObject data=json.getJSONObject("data");
					 
					 return data.getString("orders");
				 }else{
					 return "error";
				 }
			 }catch (JSONException e) {
				 e.printStackTrace();
			 }
			 return null;
		}
	
		// After completing background task Dismiss the progress dialog
		
		protected void onPostExecute(String data) {
			//dismiss the dialog once product deleted
			/*
			 * 
			 */
			pDialog.dismiss();
			if (data.equals("error")) {
				Toast.makeText(OrderListActivity.this, "Empty orders.", Toast.LENGTH_SHORT).show();
            }else{
            	try {

            		JSONArray orderlist = new JSONArray(data);
            	    for(int i = 0 ; i < orderlist.length(); i++){
    				 	//AllOrder.add(new OrderListModel(values.getString(i).   id,user_id,datetime,ship_date,comment,quantity,itemcount));
    					 JSONObject Order=orderlist.getJSONObject(i);
    					 String itemCount = String.valueOf(Order.getJSONArray("items").length());
    					 //Order.getString("itemcount")
    					 Log.v("aaaV",Order.getJSONArray("items").toString());
    					 AllOrder.add(new OrderListModel(Order.getString("id"),Order.getString("user_id"),Order.getString("datetime"),Order.getString("ship_date"),Order.getString("comment"),Order.getString("quantity"),itemCount,Order.getJSONArray("items")));
    				 }
    				 //AllOrder.add(new OrderListModel(id,user_id,datetime,ship_date,comment,quantity,itemcount));
    			       Log.v(String.valueOf(AllOrder.size()),"aaaaaaa");
    			        adapter.addAll(AllOrder);
    			} catch (Throwable t) {
            	    Log.e("My App", "Could not parse malformed JSON");
            	}
            }
        }	
	}
}