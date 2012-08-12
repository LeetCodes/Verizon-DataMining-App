package ece.uprm.vzw;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.Window;

public class DisclaimerActivity extends Activity {
	public static final String PREF_FILE_NAME = "iniPref";
	private SharedPreferences preferences;
	
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        this.requestWindowFeature(Window.FEATURE_NO_TITLE); // Removes title bar
	        setContentView(R.layout.disclaimer_layout); //Sets the layout for this activity
	        
	        final Intent mainActivityIntent = new Intent(DisclaimerActivity.this, MainActivity.class);
	        
	        String version = "";
			try {
				version = getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
			
			String message = "Error Loading Disclaimer...";
			try {
				InputStream is = getAssets().open("disclaimer.txt");
				byte[] buffer = new byte[is.available()];
				is.read(buffer);
				message = new String(new String(buffer));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        //Check if first time start
	        preferences = this.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
	        Boolean firsTimeRun = preferences.getBoolean("firstTimeRun", true);
	        if(firsTimeRun){
	        	//Create disclaimer dialog and show it
				AlertDialog.Builder disclaimerDialog = new AlertDialog.Builder(this);
				disclaimerDialog.setTitle(getString(R.string.app_name) + " - " + version);
				disclaimerDialog.setMessage(message);
				disclaimerDialog.setCancelable(true);
				disclaimerDialog.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								SharedPreferences.Editor editor = preferences.edit();
								editor.putBoolean("firstTimeRun", false);
								editor.commit();
								startActivity(mainActivityIntent);
								finish();
							}
						});
				disclaimerDialog.setNegativeButton("Don't Accept",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								finish();
							}
						});
				disclaimerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
							public void onCancel(DialogInterface dialog) {
								finish();
							}
						});
				AlertDialog dialogLoadStart = disclaimerDialog.create(); // Creates dialog based on builder (builder above)
				dialogLoadStart.show(); // Shows dialog
	        } else {
	        	//Start Main Activity
	        	startActivity(mainActivityIntent);
				finish();
	        }
	 }
}
