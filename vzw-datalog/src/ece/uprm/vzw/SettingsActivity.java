package ece.uprm.vzw;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import ece.uprm.vzw.services.DataService;

public class SettingsActivity extends Activity{
	
	private Context context;
	
	private Settings settings;
	private ListView settingsListView;
	
	private EditText emailTextbox;

	private String timeIntervalList[] = {
			"Every 6 Hours", 
			"Every 12 Hours", 
			"Daily (At 12:00am)"
			};
	
	private String dataList[] = {
			"Drop Calls Logs",
			"Signal Strenght",
			"GPS Location", 
			"Battery Consumption",
			"Memory Consumption",
			"SD Storage Information"
			};
	
	//E-mail verifying RegEx
	//src: http://www.mkyong.com/regular-expressions/how-to-validate-email-address-with-regular-expression/
	private final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Settings");
		setContentView(R.layout.settings_layout);
		
		context = this;
		settings = new Settings(context);
		settingsListView = (ListView) findViewById(R.id.settings_listview); //Finds the list view
		
		//Create the array of options
		ArrayList<SettingsOption> optionsArray = new ArrayList<SettingsOption>();
		optionsArray.add(new SettingsOption("Automatic Submissions", "Enable automatic data sending", true, settings.isAutomaticEnabled()));
		optionsArray.add(new SettingsOption("Time Interval", "Sets automatic submissions time intervals", false));
		optionsArray.add(new SettingsOption("E-Mail", "To receive automatic submissions reports", false));
		optionsArray.add(new SettingsOption("Data to Send", "Choose between which data to send", false));
		
		//Create List View adapter, this handles the ListView's content
		SettingsListAdapter adapter = new SettingsListAdapter(this, R.layout.settings_list_row, optionsArray);
		
		settingsListView.setAdapter(adapter);
		//On click listener to get user input on the List View
		settingsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> av, View v, int pos,long id) {
				onListItemClick(v, pos, id);
			}
		});
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		settings.saveSettings();
	}

	//Method called by list onClick
	private void onListItemClick(View v, int pos, long id) {
		if(pos == 0){
			//Fancy Checkbox thingy
			settings.toggleAutomaticEnabled();
			CheckBox b = (CheckBox) settingsListView.getChildAt(0).findViewById(R.id.settings_row_checkBox);
			b.setChecked(settings.isAutomaticEnabled());
				
		} else if(pos == 1){
			// Single Choice Dialog Builder for selecting time interval
			Builder sourceDialog = new AlertDialog.Builder(this);
			sourceDialog.setTitle("Submissions Intervals:");
			sourceDialog.setSingleChoiceItems(timeIntervalList,settings.getTimeInterval(),new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					settings.setTimeInterval(id);
				}
			});
			sourceDialog.setPositiveButton("Done", null);
			sourceDialog.setCancelable(true);
			sourceDialog.show();
		}  else if(pos == 2){
			//Textbox dialog for e-mail
			emailTextbox = new EditText(this);
			emailTextbox.setSingleLine();
			String email = settings.getSavedEMail();
			if(email != null)
				if(email.length() > 0)
					emailTextbox.setText(email);
			Builder sourceDialog = new AlertDialog.Builder(this);
			sourceDialog.setTitle("E-Mail:");
			sourceDialog.setView(emailTextbox);
			sourceDialog.setPositiveButton("Done", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					String email = emailTextbox.getText().toString();
					if(email.length() > 0){
					      Matcher m = emailPattern.matcher(email);
					      boolean validEmail = m.matches();
					      if(validEmail){
					    	  settings.setSavedEMail(email);
					      } else {
					    	  Toast.makeText(context, "Invalid E-mail, Nothing was changed.", Toast.LENGTH_SHORT).show();
					      }
					} else {
						settings.setSavedEMail(null);
					}
				}
			});
			sourceDialog.setCancelable(true);
			sourceDialog.show();
		}  else if(pos == 3){
			// Multiple Choice Dialog Builder for selecting data to send
			Builder sourceDialog = new AlertDialog.Builder(this);
			sourceDialog.setTitle("Data to Send:");
			sourceDialog.setMultiChoiceItems(dataList, settings.getDataToSendBooleanArray(),new DialogInterface.OnMultiChoiceClickListener() {
				public void onClick(DialogInterface dialog, int which, boolean isChecked) {
					if(which == 0){
						settings.toogleDropCallsLogsEnabled();
					} else if (which == 1){
						settings.toogleSignalStrenghtEnabled();
					} else if (which == 2){
						settings.toogleGpsLocationEnabled();
					} else if (which == 3){
						settings.toogleBatteryConsumptionEnabled();
					} else if (which == 4){
						settings.toogleMemoryConsumptionEnabled();
					} else if (which == 5){
						settings.toogleSdStorageInfoEnabled();
					}
				}
			});
			sourceDialog.setPositiveButton("Done", null);
			sourceDialog.setCancelable(true);
			sourceDialog.show();
		}
	}

}
