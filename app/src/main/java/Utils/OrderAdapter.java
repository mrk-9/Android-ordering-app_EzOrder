package Utils;

import com.pak.androidproject.R;

import DataModel.OrderListModel;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

//Custom Adapter
public class OrderAdapter extends ArrayAdapter<OrderListModel> {

	// 1. Context
	// 2. Layout cua item

	public OrderAdapter(Context context, int resource) {
		super(context, resource);

		mContext = context;
		mLayout = resource;
	}

	int mLayout;
	Context mContext;

	// Bind data --> layout
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// --> tuan theo
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(mLayout, parent, false);

		TextView orderidView = (TextView) rowView.findViewById(R.id.orderid);
		TextView orderdatetimeView = (TextView) rowView.findViewById(R.id.orderdatetime);
		TextView orderitemcountView = (TextView) rowView.findViewById(R.id.orderitemcount);
		TextView ordershipdateView = (TextView) rowView.findViewById(R.id.ordershipdate);
		TextView orderdescriptionView = (TextView) rowView.findViewById(R.id.orderdescription);
		
		// bind data to View
		OrderListModel order = getItem(position);
		orderidView.setText(order.getId());
//		orderdatetimeView.setText(order.getDateTime());
		String sItemCount = order.getItemCount();
		orderitemcountView.setText(order.getItemCount());
		
		String sgetComment = order.getComment();
		ordershipdateView.setText(order.getShipDate());
		orderdescriptionView.setText(order.getComment());

		return rowView;
	}

}