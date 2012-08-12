package ece.uprm.vzw.broadcast;

import ece.uprm.vzw.Settings;
import ece.uprm.vzw.gatherers.DataCollectorAsynch;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class DataBroadcast extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Settings settings = new Settings(context);

		if(settings.isAutomaticEnabled()){
			try {
				//Indication of Data Mining 
				Log.d("DEBUG_Broadcast", "Entre");
				Toast.makeText(context, "Extracting Data from Phone", Toast.LENGTH_SHORT).show();
				String email = "-";
				if(settings.getSavedEMail() != null)
					email = settings.getSavedEMail();
				new DataCollectorAsynch(context, email, "AUTO-SUBMISSION" ).execute(context);

			} catch (Exception e) {
				e.printStackTrace();

			}
		}
	}

}