package com.pak.androidproject;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import DataModel.ItemModel;
import DataModel.OrderModel;
import DataModel.UserModel;
import Utils.DataSourceManager;
import Utils.DateTimePickerDialog;
import Utils.JSONParser;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;




public class OrderEdit2Activity extends Activity{
	 public static DataSourceManager datasource;
	 private String token="", user_id="",user_name="",shipdate="",comment="",order_status;
	 private ProgressDialog pDialog;
	 public EditText datetime,commentEdit,customEdit,cnameEdit;
	 public boolean issave;
	 private static final String SendOrder_URL="http://ezordersolution.com/ordershark/index.php/server/add_order";
		//JSON parser class
		JSONParser jsonParser = new JSONParser();
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_orderedit2);
        /****get From DATA base********/
        datasource = new DataSourceManager(this);
        datasource.open();
        List<UserModel> values = datasource.getAllDatas("tbmember");
        user_id=String.valueOf(values.get(0).getUserID());
        token=String.valueOf(values.get(0).getToken());
        user_name=String.valueOf(values.get(0).getUserName());
        List<OrderModel> order_values = datasource.getAllOrderDatas();
        if(order_values.size()>0){
	        shipdate=String.valueOf(order_values.get(0).getShipDate());
	        comment=String.valueOf(order_values.get(0).getComment());
	        user_name=String.valueOf(values.get(0).getUserName());
        }
        /*******************************/
        
        datetime = (EditText) this.findViewById(R.id.shipdate);
        datetime.setText(shipdate);
        datetime.setKeyListener(null);
        datetime.setOnClickListener(new DateTimeOnClick());
        
        commentEdit = (EditText) this.findViewById(R.id.commment);
        commentEdit.setText(comment);
        customEdit = (EditText) this.findViewById(R.id.customer);
        customEdit.setText(user_id);
        cnameEdit = (EditText) this.findViewById(R.id.cname);
        cnameEdit.setText(user_name);
        final Button orderedit1back_button = (Button) findViewById(R.id.orderedit2back);
        orderedit1back_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	ContentValues values1 = new ContentValues();
        		values1.put("datetime", android.text.format.DateFormat.format("yyyy-MM-dd hh:mm:ss", new java.util.Date()).toString());
        		values1.put("ship_date", datetime.getText().toString());
        	    values1.put("comment",  commentEdit.getText().toString());
        	    values1.put("quantity",  "5");
        	    Log.v("aaaaV",values1.toString());
        	    datasource.emptyData("tborder");
        	    long insertID = datasource.insertData("tborder", values1);
        	    Log.v("aaaaV",String.valueOf(insertID));
            	finish();
            }
        });
        final Button edit2send_button = (Button) findViewById(R.id.edit2send);
        edit2send_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	final List<UserModel> values_click = datasource.getAllDatas("tbmember");
                user_id=String.valueOf(values_click.get(0).getUserID());
                token=values_click.get(0).getToken();
                order_status=values_click.get(0).getOrderStatus();
                ContentValues values1 = new ContentValues();
        		values1.put("datetime", android.text.format.DateFormat.format("yyyy-MM-dd hh:mm:ss", new java.util.Date()).toString());
        		values1.put("ship_date", datetime.getText().toString());
        	    values1.put("comment",  commentEdit.getText().toString());
        	    values1.put("quantity",  "5");
        	    Log.v("aaaaV",values1.toString());
        	    datasource.emptyData("tborder");
        	    long insertID = datasource.insertData("tborder", values1);
        	    Log.v("aaaaV",String.valueOf(insertID));
            	if(order_status.equals("2"))
                {
            		new SendOrder().execute();
                }
            }
        });
        /*
        final Button orderedit2save_button = (Button) findViewById(R.id.orderedit2save);
        orderedit2save_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	ContentValues values1 = new ContentValues();
				values1.put("datetime", android.text.format.DateFormat.format("yyyy-MM-dd hh:mm:ss", new java.util.Date()).toString());
				values1.put("ship_date", datetime.getText().toString());
			    values1.put("comment",  commentEdit.getText().toString());
			    values1.put("quantity",  "5");
			    Log.v("aaaaV",values1.toString());
			    datasource.emptyData("tborder");
			    long insertID = datasource.insertData("tborder", values1);
			    Log.v("aaaaV",String.valueOf(insertID));
			    Toast.makeText(OrderEdit2Activity.this, "Saved..", Toast.LENGTH_LONG).show();
			    Intent intent = new Intent(OrderEdit2Activity.this, OrderEdit1Activity.class);
                startActivity(intent);
            }
        });*/
        final Button orderdelete2_button = (Button) findViewById(R.id.orderdelete2);
        orderdelete2_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	AlertDialog.Builder builder = new AlertDialog.Builder(OrderEdit2Activity.this);
                builder.setTitle("ALERT")
                .setMessage("Really delete?")
                .setCancelable(false)
                .setPositiveButton("ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                int id) {
                        	dialog.cancel();
                        	datasource.emptyData("tborder");
                        	datasource.emptyData("tbitem");
                        	ContentValues values = new ContentValues();
                 			values.put("order_status", "1");
                 			datasource.updateData("tbmember", values, "user_id='"+user_id+"'");
                        	Intent intent = new Intent(OrderEdit2Activity.this, OrderListActivity.class);
                            startActivity(intent);
                        }
                    })
                .setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                    int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        /*
        final Button orderedit2cancel_button = (Button) findViewById(R.id.orderedit2cancel);
        orderedit2cancel_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent intent = new Intent(OrderEdit2Activity.this, OrderEdit1Activity.class);
                startActivity(intent);
            }
        });*/
        final Button edit2listplus_button = (Button) findViewById(R.id.editlistplus);
        edit2listplus_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	final List<UserModel> values_click = datasource.getAllDatas("tbmember");
                user_id=String.valueOf(values_click.get(0).getUserID());
                token=values_click.get(0).getToken();
                order_status=values_click.get(0).getOrderStatus();
                ContentValues values1 = new ContentValues();
        		values1.put("datetime", android.text.format.DateFormat.format("yyyy-MM-dd hh:mm:ss", new java.util.Date()).toString());
        		values1.put("ship_date", datetime.getText().toString());
        	    values1.put("comment",  commentEdit.getText().toString());
        	    values1.put("quantity",  "5");
        	    Log.v("aaaaV",values1.toString());
        	    datasource.emptyData("tborder");
        	    long insertID = datasource.insertData("tborder", values1);
        	    Log.v("aaaaV",String.valueOf(insertID));
            	if(order_status.equals("1"))
                {
	            	AlertDialog.Builder builder = new AlertDialog.Builder(OrderEdit2Activity.this);
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
    	                        Intent intent = new Intent(OrderEdit2Activity.this, OrderAddActivity.class);
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
                	Intent intent = new Intent(OrderEdit2Activity.this, OrderAddActivity.class);
                    startActivity(intent);
                }   
            }
        });
	}
	private final class DateTimeOnClick implements OnClickListener{
		        public void onClick(View v) {
		            Log.v("aaaaV","IN dialog");
		        	DateTimePickerDialog dateTimePicKDialog = new DateTimePickerDialog(
		                    OrderEdit2Activity.this);
		            dateTimePicKDialog.dateTimePicKDialog(datetime, 1);
		        }
		    }
class SendOrder extends AsyncTask<String, String, String> {
		
		boolean failure = false;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(OrderEdit2Activity.this);
			pDialog.setMessage("Your order has been sent.");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}
		
		@Override
		protected String doInBackground(String...args) {
			//Check for success tag
			String success;
			try {
				 List<OrderModel> OrderVal = datasource.getAllOrderDatas();
				 List<ItemModel> ItemVal = datasource.getAllItemDatas();
				 String datetime = android.text.format.DateFormat.format("yyyy-MM-dd hh:mm:ss", new java.util.Date()).toString();
				 String ship_date = android.text.format.DateFormat.format("yyyy-MM-dd hh:mm:ss", new java.util.Date()).toString();
				 String comment="";
				 String quantity=String.valueOf(ItemVal.size()); 
				 if(OrderVal.size()>0){
					 datetime = OrderVal.get(0).getDateTime().toString();
					 ship_date = OrderVal.get(0).getShipDate().toString();
					 comment=OrderVal.get(0).getComment();
				 }
				 
				 String items = "";
				 for(int i=0;i<ItemVal.size();i++)
				 {
					 if(i==ItemVal.size()-1)
						 items+="quantity:"+ItemVal.get(i).getQuantity().toString()+",barcode:"+ItemVal.get(i).getBarCode().toString()+",description:"+ItemVal.get(i).getDescription().toString();
					 else items+="quantity:"+ItemVal.get(i).getQuantity().toString()+",barcode:"+ItemVal.get(i).getBarCode().toString()+",description:"+ItemVal.get(i).getDescription().toString()+";";
				 }
				 if(items==""){
					 return "noitem";
				 }
				 Log.v(datetime,items);
				 //Building Parameters
				 List<NameValuePair> params = new ArrayList<NameValuePair>();
				 params.add(new BasicNameValuePair("token", token));
				 params.add(new BasicNameValuePair("user_id", user_id));
				 params.add(new BasicNameValuePair("datetime", datetime));
				 params.add(new BasicNameValuePair("ship_date", ship_date));
				 params.add(new BasicNameValuePair("comment", comment));
				 params.add(new BasicNameValuePair("quantity", quantity));
				 params.add(new BasicNameValuePair("items", items));
				 Log.d("request!", "starting");
				 // getting product details by making HTTP request
				 JSONObject json = jsonParser.makeHttpRequest (
					 SendOrder_URL, "POST", params);
				 
				 //check your log for json response
				 Log.d("Login attempt", json.toString());
				 
				 JSONObject message=json.getJSONObject("message");
				 
				// json success tag
				 Log.d("Json data", message.toString());
				 
				 success = message.getString("type");
				 if(success.equals("success") ) {
					 return "success";
				 }else{
					 return message.getString("code");
				 }
			 }catch (JSONException e) {
				 e.printStackTrace();
			 }
			 return null;
		}
	
		// After completing background task Dismiss the progress dialog
		
		protected void onPostExecute(String file_url) {
			//dismiss the dialog once product deleted
			pDialog.dismiss();
			if (file_url == "noitem") {
				Toast.makeText(OrderEdit2Activity.this, "There is no items.", Toast.LENGTH_SHORT).show();
			}
			if (file_url == "success") {
				datasource.emptyData("tborder");
				datasource.emptyData("tbitem");
				ContentValues values = new ContentValues();
     			values.put("order_status", "1");
     			datasource.updateData("tbmember", values, "user_id='"+user_id+"'");
				Toast.makeText(OrderEdit2Activity.this, "Success", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(OrderEdit2Activity.this, OrderListActivity.class);
                startActivity(intent);
			}
			if (file_url != null) {
				Toast.makeText(OrderEdit2Activity.this, file_url, Toast.LENGTH_SHORT).show();
            }
        }	
	}
}
