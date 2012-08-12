package ece.uprm.vzw.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

public class ConnectionChangeBroadcast extends BroadcastReceiver
{
	public void onReceive(Context context, Intent intent)
	{
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE );
		
		try
		{
			connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
			
			// connection established
			
			//Toast.makeText(context, "true", Toast.LENGTH_LONG).show();
		}
		// this exception is called when the isConnectedOrConnecting() method is called and the internet connection has been lost
		catch(NullPointerException e)
		{
			// connection lost
			
			//Toast.makeText(context, "false", Toast.LENGTH_LONG).show();
		}

	}
}