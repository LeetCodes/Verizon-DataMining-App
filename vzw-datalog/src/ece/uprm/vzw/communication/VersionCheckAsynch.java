package ece.uprm.vzw.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

public class VersionCheckAsynch extends AsyncTask<Context, Void, Void>
{
	private final static String PATH = "http://136.145.116.7/app_info.php";
	private final static String D_PATH = "http://136.145.116.7/vzw-datalog.apk";
	private Context context;
	private boolean isConnected;
	private int currVersionCode;
	private String currVersionName;
	private int latestVersionCode;
	private String latestVersionName;
	private String splitRegEx = "\\s";

	public VersionCheckAsynch(Context context)
	{
		this.context = context;

	}

	protected void onPreExecute()
	{
		//Checks if there is an active connection
		NetworkInfo ninfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
		if(ninfo != null)
			isConnected = ninfo.isConnected();
		
		//Gets current version
		PackageInfo pinfo;
		try {
			pinfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			currVersionCode = pinfo.versionCode;
			currVersionName = pinfo.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	protected Void doInBackground(Context... params)
	{
		if(isConnected){
			//Log.d("DEBUG", "Version check in progress");
			//Http Connection
			HttpClient client = new DefaultHttpClient();  
			HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); // Timeout Limit  
			HttpResponse response;  
	
			try {  
				HttpPost post = new HttpPost(PATH);  
				response = client.execute(post);  
	
				if (response != null) {  
					String responseStatus = response.getStatusLine().toString(); //Parse response status
					String responseString = convertStreamToString(response.getEntity().getContent()); //Parse response
					
					latestVersionCode = Integer.parseInt(responseString.split(splitRegEx)[0]);
					latestVersionName = responseString.split(splitRegEx)[1];
							
					//Log.d("DEBUG", "Update Check Response:"+responseStatus);
					//Log.d("DEBUG", "Response String: " + responseString);
					
				}  
			} catch (Exception e) {
				//Log.e("DEBUG", "Version Canceled, Parsing Error");
				this.cancel(true);
			}
		} else {
			//Log.d("DEBUG", "Version check skipped");
			this.cancel(true);
		}
		
		return null;
	}

	protected void onPostExecute(final Void unused){
		//If there is and update notify it.
		if(latestVersionCode > currVersionCode){
			//Create  notify dialog
			AlertDialog.Builder updateDialog = new AlertDialog.Builder(context);
			updateDialog.setTitle("App Update Available");
			updateDialog.setMessage("A new update for the app has been release, please update as soon as possible.\n" + "\nCurrent Version: " + currVersionName + "\nLatest Version: " + latestVersionName);
			updateDialog.setCancelable(true);
			updateDialog.setPositiveButton("Download", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(D_PATH));
							context.startActivity(browserIntent);
						}
					});
			AlertDialog dialogLoadStart = updateDialog.create(); // Creates dialog based on builder (builder above)
			dialogLoadStart.show(); // Shows dialog
		} else {
			//Log.d("DEBUG", "Got latest version");
		}
	}
	
	//Auxiliary method to parse response input stream
	private static String convertStreamToString(InputStream is) {  

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));  
		StringBuilder sb = new StringBuilder();  

		String line = null;  
		try {  
			while ((line = reader.readLine()) != null) {  
				sb.append(line + "\n");  
			}  
		} catch (IOException e) {  
			e.printStackTrace();  
		} finally {  
			try {  
				is.close();  
			} catch (IOException e) {  
				e.printStackTrace();  
			}  
		}  
		return sb.toString();  
	}

}
