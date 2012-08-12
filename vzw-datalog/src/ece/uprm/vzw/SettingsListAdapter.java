package ece.uprm.vzw;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class SettingsListAdapter extends ArrayAdapter<SettingsOption> {

	private ArrayList<SettingsOption> optionsArrayList; // ArrayList to be filled into the list
	private LayoutInflater mInflater; // The layout inflater
	private Activity activity;

	// Constructor for the adapter receives the activity, the R.layout resource id and the ArrayList of options
	public SettingsListAdapter(Activity a, int layoutResourceId, ArrayList<SettingsOption> options) {
		super(a, layoutResourceId, options);

		activity = a;
		optionsArrayList = options;
		mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	// View holder stores references to the views
	public class ViewHolder {
		public TextView name;
		public TextView description;
		public CheckBox checkBox;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		ViewHolder holder;
		if (convertView == null) {
			vi = mInflater.inflate(R.layout.settings_list_row, null);
			holder = new ViewHolder();
			holder.name = (TextView) vi.findViewById(R.id.settings_row_nametextView);
			holder.description = (TextView) vi.findViewById(R.id.settings_row_descriptiontextView);
			holder.checkBox = (CheckBox) vi.findViewById(R.id.settings_row_checkBox);
			vi.setTag(holder);
		} else {
			holder = (ViewHolder) vi.getTag();
		}

		//Set the strings to the TextViews and visibility of the CheckBox
		holder.name.setText(optionsArrayList.get(position).getName());
		holder.description.setText(optionsArrayList.get(position).getDescription() + "\n");
		if(optionsArrayList.get(position).isHasCheckBox()){
			holder.checkBox.setVisibility(View.VISIBLE);
			holder.checkBox.setChecked(optionsArrayList.get(position).isCheckBoxActive());
		} else {
			holder.checkBox.setVisibility(View.INVISIBLE);
		}

		return vi;
	}
}