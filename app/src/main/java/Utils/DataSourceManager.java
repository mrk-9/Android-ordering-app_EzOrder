package Utils;

import DataModel.ItemModel;
import DataModel.OrderModel;
import DataModel.UserModel;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DataSourceManager {
	
		  // Database fields
		  private SQLiteDatabase database;
		  private MySQLiteHelper dbHelper;
		  
		  public DataSourceManager(Context context) {
		    Log.v("aaa","new Myaslq");
			  dbHelper = new MySQLiteHelper(context);
		  }

		  public void open() throws SQLException {
			  Log.v("aaa","OPEN");
		    database = dbHelper.getWritableDatabase();
		  }

		  public void close() {
		    dbHelper.close();
		  }

		  public long insertData(String tbname,ContentValues values) {
		    Log.v("aaa_insertData",tbname);
		    Log.v("aaa_insertData",values.toString());
			long insertId = database.insert(tbname, null,values);
		    return insertId;
		  }
		  public void updateData(String tbname,ContentValues values,String whereClause) {
			    Log.v("aaa_insertData",tbname);
			    Log.v("aaa_insertData",values.toString());
			    long insertId = database.update(tbname, values, whereClause,null);
		  }

		  public void deleteData(String tbname,String whereClause) {
		    database.delete(tbname, whereClause, null);
		  }
		  
		  public void emptyData(String tbname) {
			    database.delete(tbname, null, null);
			  }
		  
		  //FOR USER MODEL
		  public List<UserModel> getAllDatas(String tbname) {

                List<UserModel> datas = new ArrayList<UserModel>();
                Cursor cursor = database.rawQuery("SELECT * FROM "+tbname, null);
                if(cursor!=null)	
                	cursor.moveToFirst();
                else Log.v("aaaa","cursor");
                while (!cursor.isAfterLast()) {
                    UserModel data = cursorToComment(cursor);
                    datas.add(data);
                cursor.moveToNext();
                }
                // make sure to close the cursor
                cursor.close();
                return datas;
          }

		  private UserModel cursorToComment(Cursor cursor) {
              Log.v("aaaa",cursor.toString());
			  UserModel data = new UserModel();
              data.setId(cursor.getLong(0));
              data.setUserID(cursor.getString(1));
              data.setUserName(cursor.getString(2));
              data.setPassword(cursor.getString(3));
              data.setToken(cursor.getString(4));
              data.setOrderStatus(cursor.getString(5));

            return data;
          }
		//FOR ITEM MODEL
		  public List<ItemModel> getAllItemDatas() {

                List<ItemModel> datas = new ArrayList<ItemModel>();
                Cursor cursor = database.rawQuery("SELECT * FROM tbitem", null);
                if(cursor!=null)	
                	cursor.moveToFirst();
                else Log.v("aaaa","cursor");
                while (!cursor.isAfterLast()) {
                    ItemModel data = cursorToItem(cursor);
                    datas.add(data);
                cursor.moveToNext();
                }
                // make sure to close the cursor
                cursor.close();
                return datas;
          }
		  public ItemModel getItemDataByCode(String barcode) {

              ItemModel data = new ItemModel();
              
              Cursor cursor = database.rawQuery("SELECT * FROM tbitem WHERE barcode='"+barcode+"' LIMIT 1", null);
              if(cursor!=null)	
              	cursor.moveToFirst();
              else Log.v("aaaa","cursor");
              while (!cursor.isAfterLast()) {
                  data = cursorToItem(cursor);
              cursor.moveToNext();
              }
              if(cursor.getCount()==0){
            	  cursor.close();
                  return null;
              }
              
              // make sure to close the cursor
              cursor.close();
              return data;
        }

		  private ItemModel cursorToItem(Cursor cursor) {
              Log.v("aaaa",cursor.toString());
			  ItemModel data = new ItemModel();
              data.setId(cursor.getLong(0));
              data.setBarCode(cursor.getString(1));
              data.setDescription(cursor.getString(2));
              data.setQuantity(cursor.getString(3));
            return data;
          }
		//FOR ORDER MODEL
		  public List<OrderModel> getAllOrderDatas() {

                List<OrderModel> datas = new ArrayList<OrderModel>();
                Cursor cursor = database.rawQuery("SELECT * FROM tborder", null);
                if(cursor!=null)	
                	cursor.moveToFirst();
                else Log.v("aaaa","cursor");
                while (!cursor.isAfterLast()) {
                	OrderModel data = cursorToOrder(cursor);
                    datas.add(data);
                cursor.moveToNext();
                }
                // make sure to close the cursor
                cursor.close();
                return datas;
          }

		  private OrderModel cursorToOrder(Cursor cursor) {
              Log.v("aaaa",cursor.toString());
              OrderModel data = new OrderModel();
              data.setId(cursor.getLong(0));
              data.setDateTime(cursor.getString(1));
              data.setShipDate(cursor.getString(2));
              data.setComment(cursor.getString(3));
              data.setQuantity(cursor.getString(4));
            return data;
          }
}