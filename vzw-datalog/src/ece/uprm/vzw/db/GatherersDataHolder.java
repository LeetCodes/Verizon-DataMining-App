package ece.uprm.vzw.db;

public class GatherersDataHolder 
{
	private int identifier;
	private int androidVer;
	private String kernelVer;
	private String email;
	private String message;
	private double latitude;
	private double longitude;
	private double altitude;
	private int signalStrength;
	private String signalType;
	private int memory;
	private int sdMemory;
	private int dropCalls;
	
	public GatherersDataHolder(){}
	
	public GatherersDataHolder(int identifier, int androidVer, String kernelVer,
							  String email, String message, double latitude, double longitude,
							  double altitude, int signalStrength, String signalType, int memory,
							  int sdMemory, int dropCalls) 
	{
		this.identifier = identifier;
		this.androidVer = androidVer;
		this.kernelVer = kernelVer;
		this.email = email;
		this.message = message;
		this.latitude = latitude;
		this.longitude = longitude;
		this.altitude = altitude;
		this.signalStrength = signalStrength;
		this.signalType = signalType;
		this.memory = memory;
		this.sdMemory = sdMemory;
		this.dropCalls = dropCalls;
	}
	
	public int getIdentifier() 
	{
		return identifier;
	}
	public void setIdentifier(int identifier) 
	{
		this.identifier = identifier;
	}
	public int getAndroidVer() 
	{
		return androidVer;
	}
	public void setAndroidVer(int androidVer) 
	{
		this.androidVer = androidVer;
	}
	public String getKernelVer() 
	{
		return kernelVer;
	}
	public void setKernelVer(String kernelVer) 
	{
		this.kernelVer = kernelVer;
	}
	public String getEmail() 
	{
		return email;
	}
	public void setEmail(String email) 
	{
		this.email = email;
	}
	public String getMessage() 
	{
		return message;
	}
	public void setMessage(String message) 
	{
		this.message = message;
	}
	public double getLatitude() 
	{
		return latitude;
	}
	public void setLatitude(double latitude) 
	{
		this.latitude = latitude;
	}
	public double getLongitude() 
	{
		return longitude;
	}
	public void setLongitude(double longitude)
	{
		this.longitude = longitude;
	}
	public double getAltitude() 
	{
		return altitude;
	}
	public void setAltitude(double altitude) 
	{
		this.altitude = altitude;
	}
	public int getSignalStrength() 
	{
		return signalStrength;
	}
	public void setSignalStrength(int signalStrength) 
	{
		this.signalStrength = signalStrength;
	}
	public String getSignalType() 
	{
		return signalType;
	}
	public void setSignalType(String signalType) 
	{
		this.signalType = signalType;
	}
	public int getMemory() 
	{
		return memory;
	}
	public void setMemory(int memory) 
	{
		this.memory = memory;
	}
	public int getSdMemory() 
	{
		return sdMemory;
	}
	public void setSdMemory(int sdMemory) 
	{
		this.sdMemory = sdMemory;
	}
	public int getDropCalls() 
	{
		return dropCalls;
	}
	public void setDropCalls(int dropCalls) 
	{
		this.dropCalls = dropCalls;
	}
}
