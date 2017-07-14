package com.pak.androidproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class ContactInfo extends Activity {
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.activity_contactinfo);
	        
	        final Button ContactBack = (Button) findViewById(R.id.contactback);
	        ContactBack.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	            	//Intent intent = new Intent(ContactInfo.this, MenuActivity.class);
	                //startActivity(intent);
	            	finish();
	            }
	        });

	    }
}
