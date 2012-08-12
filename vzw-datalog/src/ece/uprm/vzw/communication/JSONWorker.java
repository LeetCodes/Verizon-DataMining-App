package ece.uprm.vzw.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONWorker {

	private String PATH; //url to send data
	private JSONObject json;

	public JSONWorker(String path){
		this.PATH = path;
		json = new JSONObject();
	}

	public String sendData(){

		HttpClient client = new DefaultHttpClient();  
		HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); // Timeout Limit  
		HttpResponse response;  

		try {  
			HttpPost post = new HttpPost(PATH);  
			post.setHeader("json", json.toString());  
			StringEntity se = new StringEntity(json.toString());   
			se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));  
			post.setEntity(se);  
			response = client.execute(post);  

			if (response != null) {  
				String a = response.getStatusLine().toString();
				Log.d("DEBUG", "Data Sending Response:"+a);
				return response.getStatusLine().getReasonPhrase();
				
			}  
		} catch (Exception e) {  
			e.printStackTrace();  
		}
		
		return null;
		
	}//end sendData

	public void addData(String key, String data){
		
		try {
			json.put(key, data);
		} catch (JSONException e) {
			e.printStackTrace();
		}  
		
	}//end addData
	
}//end class