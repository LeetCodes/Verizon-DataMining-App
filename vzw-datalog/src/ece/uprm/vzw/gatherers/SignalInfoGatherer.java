package ece.uprm.vzw.gatherers;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;

public class SignalInfoGatherer 
{
	private Context context;
	private TelephonyManager telManager;
	private MySignalListener signalListener;
	private int sglStrengthValue = 99;

	public SignalInfoGatherer(Context context)
	{
		this.context = context;
	}
	
	public void startSingalStrengthListener()
	{
		signalListener = new MySignalListener();
		telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        telManager.listen(signalListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
	}
	
	/**Result based on the 3GPP TS 27.007 Section 8.5:
	 * (Int)	(dBm)
	 * 0        -113 dBm or less  
	 * 1        -111 dBm  
	 * 2...30   -109... -53 dBm  
	 * 31        -51 dBm or greater  
	 *
	 * 99 not known or not detectable
	 *
	 * Which means - 0 is low, 31 is good.
	 */
	public int getSignalStrengthValue(){
		return sglStrengthValue;
	}
	
	public String getSignalQuality(){
		if(sglStrengthValue == 0)
			return "No Service";
		else if(sglStrengthValue < 10)
			return "Low";
		else if (sglStrengthValue < 20)
			return "Medium";
		else if (sglStrengthValue < 31)
			return "High";
		else
			return "Unknown";
	}
	
	public String getSignalType(Context myActivity)
	{
		TelephonyManager tManager = (TelephonyManager)myActivity.getSystemService(Context.TELEPHONY_SERVICE);
		return parseNetworkTypeInt(tManager.getNetworkType());
	}

	private class MySignalListener extends PhoneStateListener{
		@Override
		public void onSignalStrengthsChanged(SignalStrength signalStrength) 
		{
			sglStrengthValue = signalStrength.getGsmSignalStrength();
        }
	}
	
	private static String parseNetworkTypeInt(int networkType) {
		switch (networkType){
		case 1:
			return "GPRS";
		case 2:
			return "EDGE";
		case 3:
			return "UMTS";
		case 4:
			return "CDMA: Either IS95A or IS95B";
		case 5:
			return "EVDO revision 0";
		case 6:
			return "EVDO revision A";
		case 7:
			return "1xRTT";
		case 8:
			return "HSDPA";
		case 9:
			return "HSUPA";
		case 10:
			return "HSPA";
		case 11:
			return "iDen";
		case 12:
			return "EVDO revision B";
		case 13:
			return "LTE";
		case 14:
			return "eHRPD";
		case 15:
			return "HSPA+";
		default:
			return "Unknown";
		}
	}
}
