package Utils;

import com.pak.androidproject.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class MessageAlarm {
	static boolean result = true;
	public static void showDialog(Context context, String title, String msg) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(msg);
//		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int which) {
//				
//				android.os.Process.killProcess(android.os.Process.myPid());
//			}
//		});

//		alertDialog.setIcon(R.drawable.no_icon);
		alertDialog.show();
	}
	public static void questionDialog(Context context, String title, String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		
		builder.setTitle(title);
		builder.setIcon(R.drawable.ic_launcher);
		builder.setMessage(msg);
		
		
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				result = true;
			}
		});
		
		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				result = false;
			}
		});
		
		builder.show();
	}

}
