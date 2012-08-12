package ece.uprm.vzw;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Class that handles app's settings.
 * Create an instance of this class with a context as parameter and simply use it's getter/setter to manage the settings. 
 * @author Dany
 *
 */
public class Settings {
	public static final String PREF_FILE_NAME = "Settings";
	private static final int[] hoursInterval = {6, 12, 24};
	
	
	private Context context;
	private SharedPreferences preferences;
	private boolean automaticEnabled;
	private int timeInterval;
	private boolean dropCallsLogsEnabled;
	private boolean signalStrenghtEnabled;
	private boolean gpsLocationEnabled;
	private boolean batteryConsumptionEnabled;
	private boolean memoryConsumptionEnabled;
	private boolean sdStorageInfoEnabled;
	private String savedEMail;
	
	public Settings(Context c){
		this.context = c;
		preferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
		this.automaticEnabled = preferences.getBoolean("automaticEnabled", false);
		this.timeInterval = preferences.getInt("timeInterval", 1);
		this.dropCallsLogsEnabled = preferences.getBoolean("dropCallsLogsEnabled", true);
		this.signalStrenghtEnabled = preferences.getBoolean("signalStrenghtEnabled", true);
		this.gpsLocationEnabled = preferences.getBoolean("gpsLocationEnabled", true);
		this.batteryConsumptionEnabled = preferences.getBoolean("batteryConsumptionEnabled", true);
		this.memoryConsumptionEnabled = preferences.getBoolean("memoryConsumptionEnabled", true);
		this.sdStorageInfoEnabled = preferences.getBoolean("sdStorageInfoEnabled", true);
		this.savedEMail = preferences.getString("savedEMail", null);
		
	}
	
	public void saveSettings(){
		preferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean("automaticEnabled", automaticEnabled);
		editor.putInt("timeInterval", timeInterval);
		editor.putBoolean("dropCallsLogsEnabled", dropCallsLogsEnabled);
		editor.putBoolean("signalStrenghtEnabled", signalStrenghtEnabled);
		editor.putBoolean("gpsLocationEnabled", gpsLocationEnabled);
		editor.putBoolean("batteryConsumptionEnabled", batteryConsumptionEnabled);
		editor.putBoolean("memoryConsumptionEnabled", memoryConsumptionEnabled);
		editor.putBoolean("sdStorageInfoEnabled", sdStorageInfoEnabled);
		editor.putString("savedEMail", savedEMail);
		editor.commit();
	}

	public boolean isAutomaticEnabled() {
		return automaticEnabled;
	}

	public void toggleAutomaticEnabled() {
		this.automaticEnabled = !automaticEnabled;
	}

	public int getTimeInterval() {
		return timeInterval;
	}
	
	public int getTimeIntervalInHours(){
		return hoursInterval[timeInterval];
	}

	public void setTimeInterval(int timeInterval) {
		this.timeInterval = timeInterval;
	}

	public boolean isDropCallsLogsEnabled() {
		return dropCallsLogsEnabled;
	}

	public void toogleDropCallsLogsEnabled() {
		this.dropCallsLogsEnabled = !dropCallsLogsEnabled;
	}

	public boolean isSignalStrenghtEnabled() {
		return signalStrenghtEnabled;
	}

	public void toogleSignalStrenghtEnabled() {
		this.signalStrenghtEnabled = !signalStrenghtEnabled;
	}

	public boolean isGpsLocationEnabled() {
		return gpsLocationEnabled;
	}

	public void toogleGpsLocationEnabled() {
		this.gpsLocationEnabled = !gpsLocationEnabled;
	}

	public boolean isBatteryConsumptionEnabled() {
		return batteryConsumptionEnabled;
	}

	public void toogleBatteryConsumptionEnabled() {
		this.batteryConsumptionEnabled = !batteryConsumptionEnabled;
	}

	public boolean isMemoryConsumptionEnabled() {
		return memoryConsumptionEnabled;
	}

	public void toogleMemoryConsumptionEnabled() {
		this.memoryConsumptionEnabled = !memoryConsumptionEnabled;
	}

	public boolean isSdStorageInfoEnabled() {
		return sdStorageInfoEnabled;
	}

	public void toogleSdStorageInfoEnabled() {
		this.sdStorageInfoEnabled = !sdStorageInfoEnabled;
	}
	
	public String getSavedEMail() {
		return savedEMail;
	}
	
	public void setSavedEMail(String savedEMail) {
		this.savedEMail = savedEMail;
	}

	public boolean[] getDataToSendBooleanArray(){
		boolean b[] = {
				dropCallsLogsEnabled, 
				signalStrenghtEnabled,
				gpsLocationEnabled,
				batteryConsumptionEnabled,
				memoryConsumptionEnabled,
				sdStorageInfoEnabled 
				};
		return b;
	}
	
	

}
