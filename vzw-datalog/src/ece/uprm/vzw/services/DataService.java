package ece.uprm.vzw.services;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import ece.uprm.vzw.R;
import ece.uprm.vzw.Settings;
import ece.uprm.vzw.broadcast.DataBroadcast;
import ece.uprm.vzw.gatherers.SignalInfoGatherer;


public class DataService extends Service {

	//Constants and Instance Fields
	private Notification serviceNotification;
	private NotificationManager notificationManager;
	
	private AlarmManager alarm;
	private PendingIntent dataManagerService;
	private SignalInfoGatherer signalInfoGatherer;

	private final static int SERVICE_NOTIFICATION_ID = 1;
	private final static int BROADCAST_ID = 2;
	private final static int DC_NOTIFICATION_ID = 3;
	
	private final int LOW_SIGNAL_VALUE = 3;
	
	private Settings settings;
	private long refreshTime;
	
	private Context context;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}


	@Override
	public void onCreate() {
		super.onCreate();
		
		context = this;

		//Starts Setting
		this.settings = new Settings(this);

		
		//Get notification manager
		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		
		//Initialize notification
		int icon = R.drawable.vzw_notification_icon;
		String tickerText = "VZW App Service Started";
		long when = System.currentTimeMillis();
		String contexntTitle = getString(R.string.app_name);
		String contexntText = "Data Mining and Drop Calls Service initialized";
		
		serviceNotification = new Notification(icon, tickerText, when);
		
		//Empty Intent (Does not fire any activity when notification is selected)
		PendingIntent emptyIntent = PendingIntent.getActivity(context, 0, new Intent(), 0); 
		
		serviceNotification.setLatestEventInfo(context, contexntTitle, contexntText, emptyIntent);
		serviceNotification.flags |= Notification.FLAG_AUTO_CANCEL;
		
		//Pass the notification to notificationBar
		notificationManager.notify(SERVICE_NOTIFICATION_ID, serviceNotification);
		
		//Initialized Alarm
		this.alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

		//Get Data Refresh Time
		this.refreshTime = this.settings.getTimeIntervalInHours()*3600*1000;
		
		//Starts listening to signal changes
		signalInfoGatherer = new SignalInfoGatherer(context);
		
	}


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		//Start listening to signal changes
		signalInfoGatherer.startSingalStrengthListener();

		//This part is to start the sending data broadcast
		Intent dataBrodcastIntent = new Intent(this, DataBroadcast.class);
		this.dataManagerService = PendingIntent.getBroadcast(this, BROADCAST_ID, dataBrodcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		//Sets for alarm repetition for the data broadcast
		//this.alarm.setRepeating(AlarmManager.ELAPSED_REALTIME, 0, refreshTime, this.dataManagerService);
		
		//This part is to set up the phone listener (For calling activity)
		//This is used to check if a call ended and is a possible drop-call
		EndCallListener callListener = new EndCallListener();
		TelephonyManager tm = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
		tm.listen(callListener, PhoneStateListener.LISTEN_CALL_STATE);



		return Service.START_STICKY;			
	}
	
	


	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d("DEBUG", "Got Destroyed");

		//Kill Alarm
		alarm.cancel(this.dataManagerService);
	}
	
	boolean OnCall;
	boolean Idle;
	
	private class EndCallListener extends PhoneStateListener {

		@Override
	    public void onCallStateChanged(int state, String incomingNumber) {
	        //A new call arrived and is ringing or waiting.
	    	if(TelephonyManager.CALL_STATE_RINGING == state) {
	            //Log.d("DEBUG", "RINGING, number: " + incomingNumber);
	            Idle = false;
	            OnCall = false;
	        }
	        //At least one call exists that is dialing, active, or on hold, and no calls are ringing or waiting. 
	        if(TelephonyManager.CALL_STATE_OFFHOOK == state) {
	            //Log.d("DEBUG", "OFFHOOK");
	            Idle = false;
	            OnCall = true;
	            
	        }
	        //No activity.
	        if(TelephonyManager.CALL_STATE_IDLE == state) {
	            //Log.d("DEBUG", "IDLE");
	            if(OnCall)
	            	checkEndedCall();

	            Idle = true;
	            OnCall = false;
	        }
		}

		private void checkEndedCall() {
			int signalStrengthValue = signalInfoGatherer.getSignalStrengthValue();
			if(signalStrengthValue <= LOW_SIGNAL_VALUE){
				
				//Initialize notification
				int icon = R.drawable.vzw_notification_icon;
				String tickerText = "Possible Drop Call Detected";
				long when = System.currentTimeMillis();
				String contexntText = "Finished Call with Low Signal";
				
				Notification dcNotification = new Notification(icon, tickerText, when);;
				
				//Initialize Drop Call Intent
				Intent dcIntent = new Intent(context, DropCallActivity.class);
				dcIntent.putExtra("signalStrength", signalStrengthValue); //Pass signal strength
				PendingIntent contentIntent = PendingIntent.getActivity(context, 0, dcIntent, 0);

				dcNotification.setLatestEventInfo(context,  getString(R.string.app_name), contexntText, contentIntent);
				dcNotification.flags |= Notification.FLAG_AUTO_CANCEL;
				
				//Show notification
				notificationManager.notify(DC_NOTIFICATION_ID, dcNotification);
			}
		}	
	}

}
