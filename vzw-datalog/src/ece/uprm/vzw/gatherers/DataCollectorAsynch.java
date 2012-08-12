package ece.uprm.vzw.gatherers;

import java.util.Calendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.Toast;
import ece.uprm.vzw.Settings;
import ece.uprm.vzw.communication.JSONWorker;
import ece.uprm.vzw.gatherers.LocationGatherer.LocationResult;

public class DataCollectorAsynch extends AsyncTask<Context, Void, Void>
{

	
	
	private Context context;
	
	private boolean isConnected;

	private Settings settings;
	private final static String DC_FILE_NAME = "DropCalls_INF";

	private String email = "";
	private String msg = "";

	private final static String PATH = "http://136.145.116.7/write_data.php";
	private JSONWorker jw;

	private String phoneNumber;
	private String androidVersion;
	private String kernelVersion;
	private String phoneModel;
	private String phoneIMEI;

	private Object totalRAM;
	private Object totalSD;
	
	private SignalInfoGatherer signalInfoGatherer;
	private int signalStrengthValue;
	private String signalType;

	private LocationGatherer locationHelper;
	private Location currentLocation;
	private boolean hasLocation;
	private double latitude;
	private double longitude;
	private double altitude;

	private String response;	

	public DataCollectorAsynch(Context context, String email, String msg){

		this.context = context;
		settings = new Settings(context);

		if(!email.equals("E-mail (Optional)"))
			this.email = email;

		if(!msg.equals("Message"))
			this.msg = msg;

		locationHelper = new LocationGatherer();
		hasLocation = false;
		
		signalInfoGatherer = new SignalInfoGatherer(this.context);
	}

	protected void onPreExecute()
	{
		//Checks if there is an active connection
		NetworkInfo ninfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
		if(ninfo != null)
			isConnected = ninfo.isConnectedOrConnecting();

		Toast.makeText(context, "Getting Data...", Toast.LENGTH_SHORT).show();

		//If GPS enabled start getting it
		if(settings.isGpsLocationEnabled())
			locationHelper.getLocation(context, locationResult);

		//Starts listening to signal strength if enabled to do so
		if(settings.isSignalStrenghtEnabled())
			signalInfoGatherer.startSingalStrengthListener();


		//Build an object (jw) that will send the data
		jw = new JSONWorker(PATH);

	}

	protected Void doInBackground(Context... params)
	{
		if(isConnected){

			//Sleep thread until GPS gets location
			//Checks are made every second,
			//For a total time of 30 seconds
			if(settings.isGpsLocationEnabled()){
				Long t = Calendar.getInstance().getTimeInMillis(); 
				while (!hasLocation && Calendar.getInstance().getTimeInMillis() - t < 30000)
				{
					try 
					{
						Thread.sleep(1000);
					} 
					catch (InterruptedException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
				};
			}

			//Get data
			phoneNumber = SystemInfoGatherer.getPhoneNumber(this.context);
			androidVersion = SystemInfoGatherer.getAndroidVerision();
			kernelVersion = SystemInfoGatherer.getKernelVersion();
			phoneModel = android.os.Build.MODEL;
			phoneIMEI = SystemInfoGatherer.getPhoneIMEI(this.context);

			if(settings.isMemoryConsumptionEnabled())
				totalRAM = SystemInfoGatherer.getRAMAmount(this.context);
			else
				totalRAM = -1;

			if(settings.isSdStorageInfoEnabled())
				totalSD = SystemInfoGatherer.getSDAmount();
			else
				totalSD = -1;

			if(settings.isSignalStrenghtEnabled()){
				signalStrengthValue = signalInfoGatherer.getSignalStrengthValue();
				signalType = signalInfoGatherer.getSignalType(this.context);
			} else {
				signalStrengthValue = -1;
				signalType = "-1";
			}

			latitude = 0;
			longitude = 0;
			altitude = 0;
			if(currentLocation!= null){
				latitude = currentLocation.getLatitude();
				longitude = currentLocation.getLongitude();
				altitude = currentLocation.getAltitude();
			}

			int amountOfDC;
			if(settings.isDropCallsLogsEnabled()){
				SharedPreferences preferences = context.getSharedPreferences(DC_FILE_NAME, Context.MODE_PRIVATE);
				amountOfDC = preferences.getInt("count", 0);
			} else {
				amountOfDC = -1;
			}

			//Pass data to json
			jw.addData("IDENTIFIER", phoneIMEI);
			jw.addData("PHONE_NUMBER", phoneNumber);

			jw.addData("LONGITUDE", ""+longitude);  
			jw.addData("LATITUDE", ""+latitude);
			jw.addData("ALTITUDE", ""+altitude);

			jw.addData("SIGNAL_STRENGTH", ""+signalStrengthValue);
			jw.addData("SIGNAL_TYPE", signalType);

			jw.addData("MODEL", phoneModel);
			jw.addData("ANDROID_VER", androidVersion);
			jw.addData("KERNEL_VER", kernelVersion);

			jw.addData("EMAIL", email);
			jw.addData("MESSAGE", msg);

			jw.addData("MEMORY", ""+totalRAM);
			jw.addData("SD_STORAGE", ""+totalSD);

			jw.addData("DROP_CALLS", ""+amountOfDC);

			//Send json & get response
			response = jw.sendData();

		} else {
			/**
			 * No connection, save recollected data for local db for later sending
			 */
		}

		return null;
	}

	protected void onPostExecute(final Void unused){
		if(response != null){
			//Two main response codes to be aware of:
			//'200' = OK
			//'5xx' = Error
			//Source: http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html
			Toast.makeText(context, "Data Sent.\nServer Response: " + response, Toast.LENGTH_SHORT).show();
		}

		/** 
			Displays data sent:
			String dataString = "Data Collected: \n\n";
			dataString += "Phone #: " + phoneNumber + "\n";
			dataString += "Android Verison: " + androidVersion + "\n";
			dataString += "Kernel Verison: " + kernelVersion + "\n";
			dataString += "Model: " + phoneModel = "\n";
			dataString += "IMEI: " + phoneIMEI + "\n";

			dataString += "Latitude: " + latitude + "\n";
			dataString += "Longitude: " + longitude + "\n";
			dataString += "Altitude: " + altitude + "\n";;

			dataString += "RAM: " + totalRAM + "\n";
			dataString += "SD: " + totalSD + "\n";

			dataString += "SignalStrg: " + signalStrengthValue + "\n";
			dataString += "SignalType: " + signalType + "\n";
			Toast msg = Toast.makeText(context, dataString, Toast.LENGTH_LONG);
			msg.show();
		 **/
	}

	public LocationResult locationResult = new LocationResult()
	{
		public void gotLocation(final Location location)
		{
			currentLocation = location;
			hasLocation = true;
		}
	};

}
