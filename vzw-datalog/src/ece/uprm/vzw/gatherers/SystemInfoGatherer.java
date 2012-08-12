package ece.uprm.vzw.gatherers;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;

public class SystemInfoGatherer
{

	public static String getAndroidVerision()
	{
		return parseSDK_INT(Build.VERSION.SDK_INT);
	}

	public static String getPhoneIMEI(Context myActivity)
	{
		TelephonyManager tManager = (TelephonyManager)myActivity.getSystemService(Context.TELEPHONY_SERVICE);
		return tManager.getDeviceId();
	}

	public static String getPhoneNumber(Context myActivity){
		TelephonyManager tMgr =(TelephonyManager) myActivity.getSystemService(Context.TELEPHONY_SERVICE);
		return tMgr.getLine1Number();
	}

	public static String getKernelVersion()
	{
		return System.getProperty("os.version");
	}

	public static int getRAMAmount(Context myActivity){
		MemoryInfo mi = new MemoryInfo();
		ActivityManager activityManager = (ActivityManager) myActivity.getSystemService(Context.ACTIVITY_SERVICE);
		activityManager.getMemoryInfo(mi);
		return (int) (mi.availMem / 1048576);
	}

	public static int getSDAmount()
	{
		StatFs statFs = new StatFs(Environment.getRootDirectory().getAbsolutePath());   
		int Total = (statFs.getBlockCount() * statFs.getBlockSize()) / 1048576;
		return Total;
	}

	private static String parseSDK_INT(int apiVersion)
	{

		switch(apiVersion){

		case 1:
			return "1.0";
		case 2:
			return "1.1";
		case 3: 
			return "1.5";
		case 4:
			return "1.6";
		case 5:
			return "2.0";
		case 6:
			return "2.0.1";
		case 7:
			return "2.1";
		case 8:
			return "2.2";
		case 9: 
			return "2.3.2";
		case 10:
			return "2.3.4";
		case 11:
			return "3.0";
		case 12:
			return "3.1";
		case 13:
			return "3.2";
		case 14:
			return "4.0";
		case 15:
			return "4.0.3";
		default:
			return "Undefined";
		}

	}

}
