package Utils;

import java.util.ArrayList;
import java.util.List;

import com.pak.androidproject.R;

import DataModel.ItemModel;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;


//Custom Adapter
public class ItemAdapter extends ArrayAdapter<ItemModel>{

	// 1. Context
	// 2. Layout cua item
	private List<ItemModel> objects;
	private Filter filter;
	public ItemAdapter(Context context, int resource,List<ItemModel> listItems) {
		super(context, resource,listItems);
		this.objects=listItems;
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

		TextView upstextView = (TextView) rowView.findViewById(R.id.upstext);
		TextView descriptiontextView = (TextView) rowView.findViewById(R.id.descriptiontext);
		TextView quantitytextView = (TextView) rowView.findViewById(R.id.quantitytext);
		
		// bind data to View
		ItemModel item = getItem(position);
		upstextView.setText(item.getBarCode());
		descriptiontextView.setText(item.getDescription());
		quantitytextView.setText(item.getQuantity());

		return rowView;
	}
	@Override
	public Filter getFilter() {
		if (filter == null)
			filter = new AppFilter<ItemModel>(objects);
		return filter;
	} 
	private class AppFilter<ItemModel> extends Filter {
				 
		private ArrayList<ItemModel> sourceObjects;
		 
		public AppFilter(List<ItemModel> tmps) {
			sourceObjects = new ArrayList<ItemModel>();
			synchronized (this) {
			sourceObjects.addAll(tmps);
		}
		}
		 
		@Override
		protected FilterResults performFiltering(CharSequence chars) {
			String filterSeq = chars.toString().toLowerCase();
			FilterResults result = new FilterResults();
			if (filterSeq != null && filterSeq.length() > 0) {
				ArrayList<ItemModel> filter = new ArrayList<ItemModel>();
				for (ItemModel object : sourceObjects) {
					// the filtering itself:
					String barcode = ((DataModel.ItemModel) object).getBarCode();
					if (barcode.toLowerCase().contains(filterSeq))
					filter.add(object);
				}
				result.count = filter.size();
				result.values = filter;
			} else {
				// add all objects
				synchronized (this)
				{
					result.values = sourceObjects;
					result.count = sourceObjects.size();
				}
			}
			return result;
		}
		 
		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,FilterResults results) {
			// NOTE: this function is *always* called from the UI thread.
			ArrayList<ItemModel> filtered = (ArrayList<ItemModel>) results.values;
			notifyDataSetChanged();
			clear();
			for (int i = 0, l = filtered.size(); i < l; i++)
				add((DataModel.ItemModel) filtered.get(i));
			notifyDataSetInvalidated();
		}
	} 
}