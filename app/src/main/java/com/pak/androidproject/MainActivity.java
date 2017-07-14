package com.pak.androidproject;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import DataModel.UserModel;
import Utils.DataSourceManager;
import Utils.JSONParser;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	public static DataSourceManager datasource;
	private ProgressDialog pDialog;
	private EditText user, pass;
	private int check_Count=0;
	//JSON parser class
	JSONParser jsonParser = new JSONParser();
	private static final String LOGIN_URL = "http://ezordersolution.com/ordershark/index.php/server/login";
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
    	//setup input fields
  		user = (EditText)findViewById(R.id.username);
  		pass = (EditText)findViewById(R.id.password);
  		//for DB SQLLite
		datasource = new DataSourceManager(this);
        datasource.open();
  		
  		final Button loginbtn_button = (Button) findViewById(R.id.loginbtn);
        loginbtn_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            	if(isNetworkConnected()==false)
				{
					List<UserModel> users_info = datasource.getAllDatas("tbmember");
					if(users_info.size() == 0)
						showSuccessAlert("No Internet Connection");
					else
					{
						int i;
						for (i = 0; i < users_info.size(); i++) {
							String username = user.getText().toString();
							String password = pass.getText().toString();
							if (username.equals(users_info.get(i).getUserName()))
							{
								check_Count++;
								if (password.equals(users_info.get(i).getPassword())) {
									Intent j = new Intent(MainActivity.this, MenuActivity.class);
									finish();
									startActivity(j);
								} else
									showSuccessAlert("Invalid Password");
							}
						}

						if(check_Count == 0)
							showSuccessAlert("This user was not registered when Internet was connected");
					}
				}else
					new AttemptLogin().execute();
            }
        });

    }

	private boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);

		return cm.getActiveNetworkInfo() != null;
	}

	private void showSuccessAlert(String message)  {
		AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
		alertDialog.setTitle("Alert");
		alertDialog.setMessage(message);
		alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		alertDialog.show();
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
class AttemptLogin extends AsyncTask<String, String, String> {
		
		boolean failure = false;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage("Attempting login..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}
		
		@Override
		protected String doInBackground(String...args) {
			//Check for success tag
			String success;
			String username = user.getText().toString();
			String password = pass.getText().toString();
			 try {
				 //Building Parameters
				 List<NameValuePair> params = new ArrayList<NameValuePair>();
				 params.add(new BasicNameValuePair("username", username));
				 params.add(new BasicNameValuePair("password", password));
				 
				 Log.d("request!", "starting");
				 // getting product details by making HTTP request
				 JSONObject json = jsonParser.makeHttpRequest (
					 LOGIN_URL, "POST", params);
				 
				 //check your log for json response
				 Log.d("Login attempt", json.toString());
				 
				 JSONObject message=json.getJSONObject("message");
				 
				// json success tag
				 Log.d("Json data", message.toString());
				 
				 success = message.getString("type");
				 if(success.equals("success") ) {
					 JSONObject data=json.getJSONObject("data");
					 Log.d("Login Successful!", data.toString());
//					 datasource.emptyData("tbmember");
//					 datasource.emptyData("tborder");
//					 datasource.emptyData("tbitem");
					
					ContentValues values1 = new ContentValues();
					values1.put("user_id", data.getString("user_id"));
					values1.put("user_name", username);
					values1.put("password", password);
				    values1.put("token",  data.getString("token"));
				    values1.put("order_status",  "1");
				    long insertID = datasource.insertData("tbmember", values1);
				    Log.v(String.valueOf(insertID),"aaaa");
				    
					 Intent i = new Intent(MainActivity.this, MenuActivity.class);
					 finish();
					 startActivity(i);
					 return message.getString("code");
				 }else{
					 Log.d("Login Falure!", json.getString("message"));
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
			if (file_url != null) {
				Toast.makeText(MainActivity.this, file_url, Toast.LENGTH_SHORT).show();
            }
        }	
	}
}
