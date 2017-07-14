package com.pak.androidproject;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;







import com.pak.androidproject.OrderAddActivity.SendOrder;

import DataModel.ItemModel;
import DataModel.OrderModel;
import DataModel.UserModel;
import Utils.DataSourceManager;
import Utils.ItemAdapter;
import Utils.JSONParser;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

public class OrderEdit1Activity extends Activity{
	public static DataSourceManager datasource;
	private ListView orderitemlist;
	public String token, user_id,order_status;
	public SearchView searchView;
	private ProgressDialog pDialog;
	JSONParser jsonParser = new JSONParser();
	private static final String SendOrder_URL="http://ezordersolution.com/ordershark/index.php/server/add_order";
	List<ItemModel> listItems = new ArrayList<ItemModel>();
	ItemAdapter adapter;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_orderedit1);
        /****get From DATA base********/
        datasource = new DataSourceManager(this);
        datasource.open();
        List<UserModel> values = datasource.getAllDatas("tbmember");
        user_id=String.valueOf(values.get(0).getUserID());
        Log.v("aaaavvvv",String.valueOf(values.get(0).getUserID()));
        token=values.get(0).getToken();
        Log.v("aaaaToken",token);
        /*******************************/
        listItems = datasource.getAllItemDatas();
        orderitemlist= (ListView) findViewById(R.id.orderitemlist);
        adapter = new ItemAdapter(getApplicationContext(),R.layout.row, listItems);
        //listItems.add(new ItemModel(123,"fdasf","fasdf","fdsaf"));
        Log.v(String.valueOf(listItems.size()),"aaaaaaa");
        //adapter.addAll(listItems);
        orderitemlist.setAdapter(adapter);
        //orderitemlist.setTextFilterEnabled(true);
        searchView= (SearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                callSearch(query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                callSearch(newText);
                return true;
            }
            public void callSearch(String query) {
                //Do searching
            	adapter.getFilter().filter(query);
            }
        });
        final Button orderedit1back_button = (Button) findViewById(R.id.orderedit1back);
        orderedit1back_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	//Intent intent = new Intent(OrderEdit1Activity.this, OrderAddActivity.class);
                //startActivity(intent);
            	 
            	finish();
            }
        });
        final Button ordereditshippinginfo_button = (Button) findViewById(R.id.ordereditshippinginfo);
        ordereditshippinginfo_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent intent = new Intent(OrderEdit1Activity.this, OrderEdit2Activity.class);
                startActivity(intent);
            }
        });
        final Button edit1listplus_button = (Button) findViewById(R.id.edit1listplus);
        edit1listplus_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	final List<UserModel> values_click = datasource.getAllDatas("tbmember");
                user_id=String.valueOf(values_click.get(0).getUserID());
                token=values_click.get(0).getToken();
                order_status=values_click.get(0).getOrderStatus();
            	if(order_status.equals("1"))
                {
	            	AlertDialog.Builder builder = new AlertDialog.Builder(OrderEdit1Activity.this);
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
    	                        Intent intent = new Intent(OrderEdit1Activity.this, OrderAddActivity.class);
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
                	Intent intent = new Intent(OrderEdit1Activity.this, OrderAddActivity.class);
                    startActivity(intent);
                }   
            }
        });
        orderitemlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
            	ItemModel OrderItemListObject = (ItemModel) orderitemlist.getItemAtPosition(position);
            	Intent intent = new Intent(OrderEdit1Activity.this, OrderAddActivity.class);
            	//intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            	intent.putExtra("EXTRA_DESCRIPTION", OrderItemListObject.getDescription());
            	intent.putExtra("EXTRA_QUANTITY", OrderItemListObject.getQuantity());
            	intent.putExtra("EXTRA_BARCODE", OrderItemListObject.getBarCode());
            	startActivity(intent);
            }
        });
        
        final Button ordereditsend_button = (Button) findViewById(R.id.ordereditsend);
        ordereditsend_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	final List<UserModel> values_click = datasource.getAllDatas("tbmember");
                user_id=String.valueOf(values_click.get(0).getUserID());
                token=values_click.get(0).getToken();
                order_status=values_click.get(0).getOrderStatus();
            	if(order_status.equals("2"))
                {
            		new SendOrder().execute();
                }
            }
        });
        final Button orderdelete1_button = (Button) findViewById(R.id.orderdel1);
        orderdelete1_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	AlertDialog.Builder builder = new AlertDialog.Builder(OrderEdit1Activity.this);
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
                        	Intent intent = new Intent(OrderEdit1Activity.this, OrderListActivity.class);
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
	}
	
class SendOrder extends AsyncTask<String, String, String> {
		
		boolean failure = false;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(OrderEdit1Activity.this);
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
				 String datetime =android.text.format.DateFormat.format("yyyy-MM-dd hh:mm:ss", new java.util.Date()).toString();
				 String ship_date = android.text.format.DateFormat.format("yyyy-MM-dd hh:mm:ss", new java.util.Date()).toString();
				 String comment="";
				 String quantity=String.valueOf(ItemVal.size());
				 if(OrderVal.size()>0){
					 datetime = OrderVal.get(0).getDateTime().toString();
					 ship_date = OrderVal.get(0).getShipDate().toString();
					 comment=OrderVal.get(0).getComment();
				 }
				 quantity=String.valueOf(ItemVal.size());
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
				 Log.d("OrderADD", items);
				 // getting product details by making HTTP request
				 JSONObject json = jsonParser.makeHttpRequest (
					 SendOrder_URL, "POST", params);
				 
				 //check your log for json response
				 Log.d("Login attempt", json.toString());
				 
				 JSONObject message=json.getJSONObject("message");
				 
				// json success tag
				 Log.d("Json data", message.toString());
				 Log.v(message.toString(),"aaaaV");
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
				Toast.makeText(OrderEdit1Activity.this, "There is no items.", Toast.LENGTH_SHORT).show();
			}
			if (file_url == "success") {
				datasource.emptyData("tborder");
				datasource.emptyData("tbitem");
				ContentValues values = new ContentValues();
     			values.put("order_status", "1");
     			datasource.updateData("tbmember", values, "user_id='"+user_id+"'");
				Toast.makeText(OrderEdit1Activity.this, "Success", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(OrderEdit1Activity.this, OrderListActivity.class);
                startActivity(intent);
			}
			if (file_url != null) {
				Toast.makeText(OrderEdit1Activity.this, file_url, Toast.LENGTH_SHORT).show();
            }
        }	
	}

}
