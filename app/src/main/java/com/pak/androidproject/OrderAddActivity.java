package com.pak.androidproject;


import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import com.mirasense.scanditsdk.ScanditSDKBarcodePicker;
import com.mirasense.scanditsdk.interfaces.ScanditSDKListener;
import DataModel.ItemModel;
import DataModel.OrderModel;
import DataModel.UserModel;
import Utils.DataSourceManager;
import Utils.JSONParser;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
//import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class OrderAddActivity extends Activity implements ScanditSDKListener, 
LocationListener  {
	private ScanditSDKBarcodePicker mBarcodePicker;
	public static DataSourceManager datasource;
	public boolean issave;
	public boolean isupdown;
	public String token, user_id,order_status;
	public EditText barcode_value_input;
	public EditText quantity_value_input;
	public EditText description_value_input;
	public Button mkeyboard_button;
	private ProgressDialog pDialog;
	private static final String GetDescription_URL = "http://ezordersolution.com/ordershark/index.php/server/get_description_by_barcode";
	private static final String SendOrder_URL="http://ezordersolution.com/ordershark/index.php/server/add_order";
	//JSON parser class
	JSONParser jsonParser = new JSONParser();
	
	
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //camera = Camera.open();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_orderadd);
        issave=false;
        isupdown=false;
        /****get From DATA base********/
        datasource = new DataSourceManager(this);
        datasource.open();
        List<UserModel> values = datasource.getAllDatas("tbmember");
        user_id=String.valueOf(values.get(0).getUserID());
        Log.v("aaaavvvv",String.valueOf(values.get(0).getUserID()));
        token=values.get(0).getToken();
        Log.v("aaaaToken",token);
        /*******************************/
        final RelativeLayout rootView = (RelativeLayout) findViewById(R.id.root);
        final Button orderaddback = (Button) findViewById(R.id.orderaddback);
        orderaddback.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	//Intent intent = new Intent(OrderAddActivity.this, MenuActivity.class);
                //startActivity(intent);
                 finish();
            }
        });
        barcode_value_input = (EditText) findViewById(R.id.barcode_value);
    	quantity_value_input = (EditText) findViewById(R.id.quantity_value);
    	description_value_input = (EditText) findViewById(R.id.description_value);
    	
    	if(getIntent().hasExtra("EXTRA_DESCRIPTION")){
	    	description_value_input.setText(getIntent().getStringExtra("EXTRA_DESCRIPTION"));
	    }
    	if(getIntent().hasExtra("EXTRA_BARCODE")){
    		barcode_value_input.setText(getIntent().getStringExtra("EXTRA_BARCODE"));
	    }
    	if(getIntent().hasExtra("EXTRA_QUANTITY")){
    		quantity_value_input.setText(getIntent().getStringExtra("EXTRA_QUANTITY"));
	    }
    	
        barcode_value_input.setKeyListener(null);
        quantity_value_input.setKeyListener(null);
        InputMethodManager imm = (InputMethodManager)getSystemService(
        	      Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(barcode_value_input.getWindowToken(), 0);
        final Button itemdelete_button = (Button) findViewById(R.id.itemdelete);
        itemdelete_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	if(!barcode_value_input.getText().toString().equals("")){
            		datasource.deleteData("tbitem", "barcode='"+barcode_value_input.getText().toString()+"'");
	            	barcode_value_input.setText("");
	            	quantity_value_input.setText("");
	            	description_value_input.setText("");
            	}else{
            		//Toast.makeText(OrderAddActivity.this, "Enter the ", Toast.LENGTH_SHORT).show();
            		
            	}
            		
            }
        });
        ///***********NUMBER KEY*************//
        final Button numberkey0_button = (Button) findViewById(R.id.numberkey0);
        numberkey0_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	if(issave==true){
            		barcode_value_input.setText("");
            		description_value_input.setText("");
            		quantity_value_input.setText("0");
            		issave=false;
            	}
            	barcode_value_input.setText(barcode_value_input.getText()+"0");
            }
        });
        final Button numberkey1_button = (Button) findViewById(R.id.numberkey1);
        numberkey1_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	if(issave==true){
            		barcode_value_input.setText("");
            		description_value_input.setText("");
            		quantity_value_input.setText("0");
            		issave=false;
            	}
            	barcode_value_input.setText(barcode_value_input.getText()+"1");
            }
        });
        final Button numberkey2_button = (Button) findViewById(R.id.numberkey2);
        numberkey2_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	if(issave==true){
            		barcode_value_input.setText("");
            		description_value_input.setText("");
            		quantity_value_input.setText("0");
            		issave=false;
            	}
            	barcode_value_input.setText(barcode_value_input.getText()+"2");
            }
        });
        final Button numberkey3_button = (Button) findViewById(R.id.numberkey3);
        numberkey3_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	if(issave==true){
            		barcode_value_input.setText("");
            		description_value_input.setText("");
            		quantity_value_input.setText("0");
            		issave=false;
            	}
            	barcode_value_input.setText(barcode_value_input.getText()+"3");
            }
        });
        final Button numberkey4_button = (Button) findViewById(R.id.numberkey4);
        numberkey4_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	if(issave==true){
            		barcode_value_input.setText("");
            		description_value_input.setText("");
            		quantity_value_input.setText("0");
            		issave=false;
            	}
            	barcode_value_input.setText(barcode_value_input.getText()+"4");
            }
        });
        final Button numberkey5_button = (Button) findViewById(R.id.numberkey5);
        numberkey5_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	if(issave==true){
            		barcode_value_input.setText("");
            		quantity_value_input.setText("0");
            		description_value_input.setText("");
            		issave=false;
            	}
            	barcode_value_input.setText(barcode_value_input.getText()+"5");
            }
        });
        final Button numberkey6_button = (Button) findViewById(R.id.numberkey6);
        numberkey6_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	if(issave==true){
            		barcode_value_input.setText("");
            		quantity_value_input.setText("0");
            		description_value_input.setText("");
            		issave=false;
            	}
            	barcode_value_input.setText(barcode_value_input.getText()+"6");
            }
        });
        final Button numberkey7_button = (Button) findViewById(R.id.numberkey7);
        numberkey7_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	if(issave==true){
            		barcode_value_input.setText("");
            		quantity_value_input.setText("0");
            		description_value_input.setText("");
            		issave=false;
            	}
            	barcode_value_input.setText(barcode_value_input.getText()+"7");
            }
        });
        final Button numberkey8_button = (Button) findViewById(R.id.numberkey8);
        numberkey8_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	if(issave==true){
            		barcode_value_input.setText("");
            		quantity_value_input.setText("0");
            		description_value_input.setText("");
            		issave=false;
            	}
            	barcode_value_input.setText(barcode_value_input.getText()+"8");
            }
        });
        final Button numberkey9_button = (Button) findViewById(R.id.numberkey9);
        numberkey9_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	if(issave==true){
            		barcode_value_input.setText("");
            		quantity_value_input.setText("0");
            		description_value_input.setText("");
            		issave=false;
            	}
            	barcode_value_input.setText(barcode_value_input.getText()+"9");
            }
        });
        final Button numberup_button = (Button) findViewById(R.id.numberup);
        numberup_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	int val;
            	isupdown=true;
            	if(quantity_value_input.getText().toString().equals(""))
            		val=1;
            	else if(quantity_value_input.getText().toString().equals("99"))
            		val=99;
            	else 
            		val = Integer.parseInt(quantity_value_input.getText().toString())+1;
            	quantity_value_input.setText(String.valueOf(val));
            }
        });
        final Button numberdown_button = (Button) findViewById(R.id.numberdown);
        numberdown_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	int val;
            	isupdown=true;
            	if(quantity_value_input.getText().toString().equals(""))
            		val=0;
            	else 
            		val = Integer.parseInt(quantity_value_input.getText().toString())-1;
            	if(val<0) val=0;
            	quantity_value_input.setText(String.valueOf(val));
            }
        });
        final Button numberback_button = (Button) findViewById(R.id.numberback);
        numberback_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	if(!barcode_value_input.getText().toString().equals("")){
	            	Long val = Long.parseLong(barcode_value_input.getText().toString())-1;
	            	String tmp = String.valueOf(val);
	            	Log.v("aaaaA",tmp);
	            	
	            	if(tmp.length()>1)
	            		tmp= tmp.substring(0, tmp.length()-1);
	            	else if(tmp.length()<2)
	            		tmp="";
	            	barcode_value_input.setText(tmp);
            	}
            }
        });
        final Button numberenter_button = (Button) findViewById(R.id.numberenter);
        numberenter_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	if(barcode_value_input.getText().toString().equals("")) return;
            	if(quantity_value_input.getText().toString().equals("")) quantity_value_input.setText("1");
            	if(quantity_value_input.getText().toString().equals("0")) quantity_value_input.setText("1");
            	ItemModel item = datasource.getItemDataByCode(barcode_value_input.getText().toString());
            	if(item!=null && isupdown==false){
	            	int quantity_val=Integer.parseInt(item.getQuantity())+1;
	            	quantity_value_input.setText(String.valueOf(quantity_val));
            	}
        		issave=true;
        		isupdown=false;
        		new GetDescription().execute();
            }
        });
        final Button send_button = (Button) findViewById(R.id.send1);
        send_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent intent = new Intent(OrderAddActivity.this, OrderEdit1Activity.class);
            	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            	startActivity(intent);
            	/*
            	final List<UserModel> values_click = datasource.getAllDatas("tbmember");
                user_id=String.valueOf(values_click.get(0).getUserID());
                token=values_click.get(0).getToken();
                order_status=values_click.get(0).getOrderStatus();
            	if(order_status.equals("2"))
                {
            		new SendOrder().execute();
                }
                */
            }
        });
        //*************************************//	
        final Button scanbtn = (Button) findViewById(R.id.scanbtn);
        scanbtn.setOnClickListener(new View.OnClickListener() {
        	@SuppressWarnings("deprecation")
			@Override
            public void onClick(View v) {
                if (mBarcodePicker != null) {
                    return;
                }
                RelativeLayout.LayoutParams rParams;
                
                RelativeLayout r = new RelativeLayout(OrderAddActivity.this);
                rParams = new RelativeLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                r.setBackgroundColor(0x00000000);
                r.setTag(6);
                
                rootView.addView(r, rParams);
//                r.setOnClickListener(new OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        rootView.removeView(v);
//                        rootView.removeView(mBarcodePicker);
//                        mBarcodePicker.stopScanning();
//                        mBarcodePicker = null;
//                    }
//                });
                
                
                mBarcodePicker = new ScanditSDKBarcodePicker(
                		OrderAddActivity.this, 
                        ScanditSDKSampleBarcodeActivity.sScanditSdkAppKey);
               
                // Register listener, in order to be notified about relevant
                // events (e.g. a recognized bar code).
                mBarcodePicker.getOverlayView().addListener(OrderAddActivity.this);
                mBarcodePicker.getOverlayView().showSearchBar(true);
                
                // Set all settings according to the settings activity. To get a short overview and explanation
                // of the most used settings please check the ScanditSDKSampleBarcodeActivity.
                SettingsActivity.setSettingsForPicker(OrderAddActivity.this, mBarcodePicker);

                WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                Display display = wm.getDefaultDisplay();
                
                //rParams = new RelativeLayout.LayoutParams(
                 //       display.getWidth() * 3 / 4, display.getHeight() * 3 / 4);
                rParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
                //rParams.bottomMargin = 20;
                rootView.addView(mBarcodePicker, rParams);
                mBarcodePicker.startScanning();
                
                
                // keyboard button added.
                
                mkeyboard_button = new Button(OrderAddActivity.this);
                mkeyboard_button.setBackgroundResource(R.drawable.keyboard);

                mkeyboard_button.setOnClickListener(new View.OnClickListener() {
                	@SuppressWarnings("deprecation")
        			@Override
                    public void onClick(View v) {
                        rootView.removeView(v);
                        rootView.removeView(mBarcodePicker);
                        rootView.removeView(rootView.findViewWithTag(6));
                        mBarcodePicker.stopScanning();
                        mBarcodePicker = null;
                	}          
                });
                
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                		70, 
                		40);
                params.leftMargin = 0;
                
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                ((RelativeLayout) mBarcodePicker).addView(mkeyboard_button, params);   
 
                
                
               
                
            }
        });
        final Button orderaddgoreview = (Button) findViewById(R.id.orderaddgoreview);
        orderaddgoreview.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent intent = new Intent(OrderAddActivity.this, OrderEdit1Activity.class);
            	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            	startActivity(intent);
            }
        });
    }

 
    
	public void turnLightOn(Camera mCamera) {

			try{
				if (mCamera == null) {
					return;
				}
				Parameters parameters = mCamera.getParameters();
				if (parameters == null) {
					return;
				}
				List<String> flashModes = parameters.getSupportedFlashModes();
				// Check if camera flash exists
				if (flashModes == null) {
					// Use the screen as a flashlight (next best thing)
					return;
				}
				String flashMode = parameters.getFlashMode();
				if (!Parameters.FLASH_MODE_TORCH.equals(flashMode)) {
					// Turn on the flash
					if (flashModes.contains(Parameters.FLASH_MODE_TORCH)) {
						parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
						mCamera.setParameters(parameters);						
					} else {
					}
				}
			} catch(Exception ex){}

	}
	

    
	public void turnLightOff(Camera mCamera) {

			try{
				if (mCamera == null) {
					return;
				}
				Parameters parameters = mCamera.getParameters();
				if (parameters == null) {
					return;
				}
				List<String> flashModes = parameters.getSupportedFlashModes();
				String flashMode = parameters.getFlashMode();
				// Check if camera flash exists
				if (flashModes == null) {
					return;
				}
				if (!Parameters.FLASH_MODE_OFF.equals(flashMode)) {
					// Turn off the flash
					if (flashModes.contains(Parameters.FLASH_MODE_OFF)) {
						parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
						mCamera.setParameters(parameters);
					} else {
//						Log.e("FLASH_MODE_OFF not supported");
					}
				}
			} catch(Exception ex){}

	}
    
	
	@Override
    protected void onPause() {
        // When the activity is in the background immediately stop the 
        // scanning to save resources and free the camera.
      //  mBarcodePicker.stopScanning();
        
        super.onPause();
    }
    
    @Override
    protected void onResume() {
        // Once the activity is in the foreground again, restart scanning.
        super.onResume();
        if(getIntent().hasExtra("EXTRA_DESCRIPTION")){
	    	description_value_input.setText(getIntent().getStringExtra("EXTRA_DESCRIPTION"));
	    }
    	if(getIntent().hasExtra("EXTRA_BARCODE")){
    		barcode_value_input.setText(getIntent().getStringExtra("EXTRA_BARCODE"));
	    }
    	if(getIntent().hasExtra("EXTRA_QUANTITY")){
    		quantity_value_input.setText(getIntent().getStringExtra("EXTRA_QUANTITY"));
	    }
    }
    
	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
		Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
	}
	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
	}
	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "3", Toast.LENGTH_SHORT).show();
	}
	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "4", Toast.LENGTH_SHORT).show();
	}
	@Override
	public void didCancel() {
		// TODO Auto-generated method stub
		Toast.makeText(this, "5", Toast.LENGTH_SHORT).show();
	}
	@Override
	public void didManualSearch(String arg0) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "6", Toast.LENGTH_SHORT).show();
	}
	@Override
	public void didScanBarcode(String arg0, String arg1) {
		// TODO Auto-generated method stub
		Toast.makeText(this, arg0, Toast.LENGTH_SHORT).show();
		EditText text = (EditText) findViewById(R.id.barcode_value);
		text.setText(arg0);

		//go to num pad screen
		RelativeLayout rootView = (RelativeLayout) findViewById(R.id.root);
		rootView.removeView(mkeyboard_button);
        rootView.removeView(mBarcodePicker);
        rootView.removeView(rootView.findViewWithTag(6));
  
		mBarcodePicker.stopScanning();
		mBarcodePicker = null;    
        
	}
	
	class GetDescription extends AsyncTask<String, String, String> {
		
		boolean failure = false;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(OrderAddActivity.this);
			pDialog.setMessage("Get description login..");
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
				 params.add(new BasicNameValuePair("barcode", barcode_value_input.getText().toString()));
				 
				 Log.d("request!", "starting");
				 // getting product details by making HTTP request
				 JSONObject json = jsonParser.makeHttpRequest (
					 GetDescription_URL, "GET", params);
				 
				 //check your log for json response
				 Log.d("Login attempt", json.toString());
				 
				 JSONObject message=json.getJSONObject("message");
				 
				// json success tag
				 Log.d("Json data", message.toString());
				 
				 success = message.getString("type");
				 if(success.equals("success") ) {
					 JSONObject data=json.getJSONObject("data");
					 Log.d("Get DESCRIPTION!", data.toString());
					 //description_value_input.setText(data.getString("description"));
					 if(data.getString("description").equals("null"))
						 return "Description not found";
					 return data.getString("description");
				 }else{
					 //description_value_input.setText("Unknown description");
					 //Log.v("aaaaV", description_value_input.getText().toString());
					 ContentValues values1 = new ContentValues();
					 values1.put("barcode", barcode_value_input.getText().toString());
					 values1.put("description","Description not found");
					 values1.put("quantity",  quantity_value_input.getText().toString());
					 //long insertID = datasource.insertData("tbitem", values1);
				    // Log.v("aaaaV", String.valueOf(insertID));
					 return "Description not found";
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
			description_value_input.setText(file_url);
			ContentValues values = new ContentValues();
			values.put("barcode", barcode_value_input.getText().toString());
			Log.v(description_value_input.getText().toString(),"aaa");
			if(description_value_input.getText().toString().equals("null"))
				values.put("description",  "Description not found");
			else
				values.put("description",  description_value_input.getText().toString());
			values.put("quantity",  quantity_value_input.getText().toString());
			
			ItemModel item = datasource.getItemDataByCode(barcode_value_input.getText().toString());
        	if(item!=null){
        		datasource.updateData("tbitem", values," barcode='"+barcode_value_input.getText().toString()+"'");
        	}else{
        		datasource.insertData("tbitem", values);
        	}
			if (file_url != null) {
				Toast.makeText(OrderAddActivity.this, "One item added..", Toast.LENGTH_SHORT).show();
            }
        }	
	}
	class SendOrder extends AsyncTask<String, String, String> {
		
		boolean failure = false;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(OrderAddActivity.this);
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
				 String quantity="1"; 
				 if(OrderVal.size()>0){
					 datetime = OrderVal.get(0).getDateTime().toString();
					 ship_date = OrderVal.get(0).getShipDate().toString();
					 comment=OrderVal.get(0).getComment();
					 quantity=String.valueOf(ItemVal.size());
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
				Toast.makeText(OrderAddActivity.this, "There is no items.", Toast.LENGTH_SHORT).show();
			}
			if (file_url == "success") {
				datasource.emptyData("tborder");
				datasource.emptyData("tbitem");
				ContentValues values = new ContentValues();
     			values.put("order_status", "1");
     			datasource.updateData("tbmember", values, "user_id='"+user_id+"'");
				Toast.makeText(OrderAddActivity.this, "Success", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(OrderAddActivity.this, OrderListActivity.class);
                startActivity(intent);
			}
			if (file_url != null) {
				Toast.makeText(OrderAddActivity.this, file_url, Toast.LENGTH_SHORT).show();
            }
        }	
	}
	
		
   
}
