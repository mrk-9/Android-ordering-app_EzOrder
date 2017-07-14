package Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

	  private static final String DATABASE_NAME = "order.db";
	  private static final int DATABASE_VERSION = 1;
	  
	  
	  public MySQLiteHelper(Context context) {
	      super(context, DATABASE_NAME, null, DATABASE_VERSION);
	      Log.v("aaaa","contrat");
	  }

	  @Override
	  public void onCreate(SQLiteDatabase database) {
			Log.v("aaaa","table_create start");
			database.execSQL("create table tbmember (id integer primary key autoincrement,user_id text,user_name text,password text, token text,order_status text);");
			database.execSQL("create table tborder (id integer primary key autoincrement,datetime text,ship_date text,comment text,quantity text);");
            database.execSQL("create table tbitem (id integer primary key autoincrement,barcode text,description text,quantity text);");
            Log.v("aaaa","table_create end");
      }

	  @Override
	  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		  Log.v("aaa","onUpgrade");
		 Log.w(MySQLiteHelper.class.getName(),
	        "Upgrading database from version " + oldVersion + " to "
	            + newVersion + ", which will destroy all old data");
	    //db.execSQL("DROP TABLE IF EXISTS " + USERTB_NAME);
	    onCreate(db);
	  }
}


