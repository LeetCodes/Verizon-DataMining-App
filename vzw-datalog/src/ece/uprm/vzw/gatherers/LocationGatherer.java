package ece.uprm.vzw.gatherers;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class LocationGatherer
{
	LocationManager locationManager;
    private LocationResult locationResult;
    boolean gpsEnabled = false;
    boolean networkEnabled = false;

    public boolean getLocation(Context context, LocationResult result){       
        locationResult = result;

        if(locationManager == null){
            locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        }
            //exceptions thrown if provider not enabled
            try{
                gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            }
            catch (Exception ex) {
            	System.out.println(ex);
            }

            //dont start listeners if no provider is enabled
            if(!gpsEnabled && !networkEnabled){
                return false;
            }

            if(gpsEnabled){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerGps);
            }
            if(networkEnabled){
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerNetwork);
            }


            //GetLastLocation();
            return true;
    }

    LocationListener locationListenerGps = new LocationListener() {
        public void onLocationChanged(Location location){
            locationResult.gotLocation(location);
            locationManager.removeUpdates(this);
            locationManager.removeUpdates(locationListenerNetwork);

        }
        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extra) {}
    };

    LocationListener locationListenerNetwork = new LocationListener() {
        public void onLocationChanged(Location location){
            locationResult.gotLocation(location);
            locationManager.removeUpdates(this);
            locationManager.removeUpdates(locationListenerGps);

        }
        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extra) {}

    };

    private void GetLastLocation()
    {
            locationManager.removeUpdates(locationListenerGps);
            locationManager.removeUpdates(locationListenerNetwork);

            Location gpsLocation = null;
            Location networkLocation = null;

            if(gpsEnabled){
                gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
            if(networkEnabled){
                networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            //if there are both values use the latest one
            if(gpsLocation != null && networkLocation != null)
            {
                if(gpsLocation.getTime() > networkLocation.getTime()){
                    locationResult.gotLocation(gpsLocation);
                } else {
                    locationResult.gotLocation(networkLocation);
                }

                return;
            }

            if(gpsLocation != null){
                locationResult.gotLocation(gpsLocation);
                return;
            }

            if(networkLocation != null){
                locationResult.gotLocation(networkLocation);
                return;
            }

            locationResult.gotLocation(null);
    }

    public static abstract class LocationResult
    {
        public abstract void gotLocation(Location location);
    }
}
