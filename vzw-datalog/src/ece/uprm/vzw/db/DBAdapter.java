package ece.uprm.vzw.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.widget.Toast;

public class DBAdapter extends SQLiteOpenHelper
{
	// TODO this class has not been tested
	
	private static final String DB_NAME = "VERIZON_DATA_MINING";
	private static final String DB_TABLE_DATA = "DATA_MINING_DATA";
	
	public static final String COL_IDENTIFIER = "IDENTIFIER";
	public static final String COL_LONGITUDE= "LONGITUDE";
	public static final String COL_LATITUDE = "LATITUDE";
	public static final String COL_ALTITUDE = "ALTITUDE";
	public static final String COL_SIGNAL_STRENGTH = "SIGNAL_STRENGTH";
	public static final String COL_SIGNAL_TYPE = "SIGNAL_TYPE";
	public static final String COL_ANDROID_VER = "ANDROID_VER";
	public static final String COL_KERNEL_VER = "KERNEL_VER";
	public static final String COL_EMAIL = "EMAIL";
	public static final String COL_MESSAGE = "MESSAGE";
	public static final String COL_MEMORY = "MEMORY";
	public static final String COL_SD_MEMORY = "SD_STORAGE";
	public static final String COL_DROP_CALLS = "DROP_CALLS";
	
	private Context context;
	
	public DBAdapter(Context context)
	{
		super(context, DB_NAME, null, 2);
		this.context = context;
	}
	
	public void onCreate(SQLiteDatabase db)
	{
		
		db.execSQL("CREATE TABLE " + DB_TABLE_DATA + " (" + COL_IDENTIFIER + " INTEGER PRIMARY KEY, " +
				COL_ANDROID_VER + " INTEGER, " + COL_KERNEL_VER +  " TEXT, " + COL_EMAIL + " TEXT, " + COL_MESSAGE + " TEXT, " + COL_LONGITUDE + " TEXT, " + 
				COL_LATITUDE + " REAL, " + COL_ALTITUDE + " REAL, " + COL_SIGNAL_STRENGTH + " INTEGER, " + COL_SIGNAL_TYPE + " INTEGER, " +
				COL_MEMORY + " INTEGER, " + COL_SD_MEMORY + " INTEGER, " + COL_DROP_CALLS + " INTEGER)");
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_DATA);
		onCreate(db);
	}
	
	public void insertInfo(int identifier, int androidVer, String kernelVer, String email, String message, 
							   double latitude, double longitude, double altitude, int signalStrength, 
							   String signalType, int memory, int sdMemory, int dropCalls)
	{
		
		// getWritableDatabase calls the onCreate(...) method of the DBAdapter class the first time that it's called;
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues cv = new ContentValues();
		cv.put(COL_IDENTIFIER, identifier);
		cv.put(COL_ANDROID_VER, androidVer);
		cv.put(COL_KERNEL_VER, kernelVer);
		cv.put(COL_EMAIL, email);
		cv.put(COL_MESSAGE, message);
		cv.put(COL_LONGITUDE, latitude);
		cv.put(COL_LATITUDE, longitude);
		cv.put(COL_ALTITUDE, altitude);
		cv.put(COL_SIGNAL_STRENGTH, signalStrength);
		cv.put(COL_SIGNAL_TYPE, signalType);
		cv.put(COL_MEMORY, memory);
		cv.put(COL_SD_MEMORY, sdMemory);
		cv.put(COL_DROP_CALLS, dropCalls);
		
		try
		{
			// if the insert returns -1 it means that there was an error in the insertion
		
			// If the second parameter is not set to null, the nullColumnHack parameter (the second parameter)
			// provides the name of nullable column name to explicitly insert a NULL into in the case where your values is empty
			if(db.insertOrThrow(DB_TABLE_DATA, null, cv) == -1)
			{
				// TODO manage this exception
				Toast.makeText(context, "Error on db insertion", Toast.LENGTH_LONG).show();
			}
		}
		catch(SQLException e)
		{
			System.out.println(e);
		}

		db.close();
	}
	
	public boolean isEmpty()
	{
		String sqlStatement = "SELECT * FROM " + DB_TABLE_DATA;
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		Cursor c = db.rawQuery(sqlStatement, new String[]{});
		
		if(c != null)
		{
			return (c.getCount() == 0) ? true : false;
		}
		return true;
	}
	
	public void deleteAllData()
	{
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(DB_TABLE_DATA, null, new String[] {});
		db.close();
	}
	
	public ArrayList<GatherersDataHolder> getAllData()
	{
		ArrayList<GatherersDataHolder> dataList = new ArrayList<GatherersDataHolder>();
		GatherersDataHolder holder;
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + DB_TABLE_DATA, new String[]{});
		
		while(cursor.moveToNext())
		{
			holder = new GatherersDataHolder();
			
			holder.setIdentifier(cursor.getInt(cursor.getColumnIndex(COL_IDENTIFIER)));
			holder.setAndroidVer(cursor.getInt(cursor.getColumnIndex(COL_ANDROID_VER)));
			holder.setKernelVer(cursor.getString(cursor.getColumnIndex(COL_KERNEL_VER)));
			holder.setEmail(cursor.getString(cursor.getColumnIndex(COL_EMAIL)));
			holder.setMessage(cursor.getString(cursor.getColumnIndex(COL_MESSAGE)));
			holder.setLatitude(cursor.getDouble(cursor.getColumnIndex(COL_LATITUDE)));
			holder.setLongitude(cursor.getDouble(cursor.getColumnIndex(COL_LONGITUDE)));
			holder.setAltitude(cursor.getDouble(cursor.getColumnIndex(COL_ALTITUDE)));
			holder.setSignalStrength(cursor.getInt(cursor.getColumnIndex(COL_SIGNAL_STRENGTH)));
			holder.setSignalType(cursor.getString(cursor.getColumnIndex(COL_SIGNAL_TYPE)));
			holder.setMemory(cursor.getInt(cursor.getColumnIndex(COL_MEMORY)));
			holder.setSdMemory(cursor.getInt(cursor.getColumnIndex(COL_SD_MEMORY)));
			holder.setDropCalls(cursor.getInt(cursor.getColumnIndex(COL_DROP_CALLS)));
			
			dataList.add(holder);
		}
		
		return dataList;
		
	}
	
	// store the database on the sd card were it can be accessed from eclipse - just for testing purposes
	public void backupDatabase() throws IOException {
		
		
		//Open your local db as the input stream
		String inFileName = "/data/data/" + context.getPackageName() + "/databases/" + DB_NAME ;
		File dbFile = new File(inFileName);
		FileInputStream fis = new FileInputStream(dbFile);

		String outFileName = Environment.getExternalStorageDirectory()+"/" + DB_NAME;
		//Open the empty db as the output stream
		OutputStream output = new FileOutputStream(outFileName);
		//transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = fis.read(buffer))>0){
			output.write(buffer, 0, length);
		}
		//Close the streams
		output.flush();
		output.close();
		fis.close();
	}
}

