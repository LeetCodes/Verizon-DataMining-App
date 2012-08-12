package ece.uprm.vzw;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import ece.uprm.vzw.communication.VersionCheckAsynch;
import ece.uprm.vzw.gatherers.DataCollectorAsynch;
import ece.uprm.vzw.services.DataService;

public class MainActivity extends Activity {
	
	private final String emailBoxDefaultMsg = "E-mail (Optional)";
	private final String messageBoxDefaultMsg = "Message";
	
	private EditText email_textBox;
	private EditText message_textBox;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); // Removes title bar
        setContentView(R.layout.main_layout); //Sets the layout for this activity
        
        //Check for updates
        new VersionCheckAsynch(this).execute(this);
        
        //Start Service if not started yet
        if(!isTheServiceRunning())
        	this.startService(new Intent(this, DataService.class));
	
        //Gets Views
        email_textBox = (EditText) findViewById(R.id.email_textBox);
        message_textBox = (EditText) findViewById(R.id.message_textBox);
        
        Settings s= new Settings(this);
        if(s.getSavedEMail() != null){
        	email_textBox.setText(s.getSavedEMail());
        }
        
        //TextBox focus listener for fancy looking textboxes
        email_textBox.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				TextView tb = (TextView) v;
				String s = tb.getText().toString();
				if(hasFocus){
					if(s.equals(emailBoxDefaultMsg))
						tb.setText("");
					tb.setTextColor(0xff000000);
				} else {
					if(s.length() == 0){
						tb.setText(emailBoxDefaultMsg);
						tb.setTextColor(0xff888888);
					}
				}
			}
		});
        
        message_textBox.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				TextView tb = (TextView) v;
				String s = tb.getText().toString();
				if(hasFocus){
					if(s.equals(messageBoxDefaultMsg))
						tb.setText("");
					tb.setTextColor(0xff000000);
				} else {
					if(s.length() == 0){
						tb.setText(messageBoxDefaultMsg);
						tb.setTextColor(0xff888888);
					}
				}
			}
		});
        
    }
    
    //Method to iterate trough services names checking if vzw's is running
    private boolean isTheServiceRunning() {
    	ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
        	//Log.d("DEBUG", service.service.getClassName());
            if ("ece.uprm.vzw.services.DataService".equals(service.service.getClassName())) {
            	return true;
            }
        }
		return false;
	}

	@Override
	protected void onResume() {
		super.onResume();
		//TextBox text color set-up for fancy looking boxes.
		if(email_textBox.getText().toString().equals(emailBoxDefaultMsg))
			email_textBox.setTextColor(0xff888888);
		else
			email_textBox.setTextColor(0xff000000);
		
		if(message_textBox.getText().toString().equals(messageBoxDefaultMsg))
			message_textBox.setTextColor(0xff888888);
		else
			message_textBox.setTextColor(0xff000000);
	}

	public void SendOnClick(View v){
    	//Upon clicking the "Send" button lots of things will be happening, I suggest using an ASyncTask.
		
		//Get the data from the GUI text fields
        String email = email_textBox.getText().toString();
        String msg = message_textBox.getText().toString();
        
        // Data collector AsyncTask - in charge of recollecting the desired data
        new DataCollectorAsynch(this, email, msg ).execute(this);
    }
    
    public void SettingsOnClick(View v) {
    	startActivity(new Intent(MainActivity.this, SettingsActivity.class));
    }
} 