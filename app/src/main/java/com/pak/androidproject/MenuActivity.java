package com.pak.androidproject;

import java.util.List;





import DataModel.OrderModel;
import DataModel.UserModel;
import Utils.DataSourceManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

public class MenuActivity extends Activity {
	public static DataSourceManager datasource;
	public String user_id,token,order_status;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_menu);
        
        datasource = new DataSourceManager(this);
        datasource.open();
        List<UserModel> values = datasource.getAllDatas("tbmember");
        Log.v("aaaav",values.toString());
        user_id=String.valueOf(values.get(0).getUserID());
        token=values.get(0).getToken();
        order_status=values.get(0).getOrderStatus();
        
        final Button Orderbtn_button = (Button) findViewById(R.id.orderbtn);
        Orderbtn_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	final List<UserModel> values_click = datasource.getAllDatas("tbmember");
                user_id=String.valueOf(values_click.get(0).getUserID());
                token=values_click.get(0).getToken();
                order_status=values_click.get(0).getOrderStatus();
            	if(order_status.equals("1"))
                {
	            	AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);
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
    	                        Intent intent = new Intent(MenuActivity.this, OrderAddActivity.class);
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
                	Intent intent = new Intent(MenuActivity.this, OrderAddActivity.class);
                    startActivity(intent);
                }   
            }	
        });
        final Button contactinfobtn_button = (Button) findViewById(R.id.contactinfobtn);
        contactinfobtn_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent intent = new Intent(MenuActivity.this, ContactInfo.class);
                startActivity(intent);
            }
        });
        final Button orderlistbtn_button = (Button) findViewById(R.id.orderlistbtn);
        orderlistbtn_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent intent = new Intent(MenuActivity.this, OrderListActivity.class);
                startActivity(intent);
            }
        });
        final Button orderreviewbtn_button = (Button) findViewById(R.id.orderreviewbtn);
        orderreviewbtn_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent intent = new Intent(MenuActivity.this, OrderEdit1Activity.class);
                startActivity(intent);
            }
        });
        final Button sendmessage_button = (Button) findViewById(R.id.sendmessage);
        sendmessage_button.setOnClickListener(new View.OnClickListener() {
        	@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
        		List<OrderModel> NowOrder = datasource.getAllOrderDatas();
        		Intent i = new Intent(Intent.ACTION_SEND);
        		i.setType("message/rfc822");
        		i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"Aliatoz54@yahoo.com"});
        		i.putExtra(Intent.EXTRA_SUBJECT, "Ship Date");
        		if(NowOrder.size()>0)
        			i.putExtra(Intent.EXTRA_TEXT   , NowOrder.get(0).getShipDate());
        		else i.putExtra(Intent.EXTRA_TEXT   , "");
        		try {
        		    startActivity(Intent.createChooser(i, "Send mail..."));
        		} catch (android.content.ActivityNotFoundException ex) {
        		    Toast.makeText(MenuActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        		}
			}
		});
    }
}
