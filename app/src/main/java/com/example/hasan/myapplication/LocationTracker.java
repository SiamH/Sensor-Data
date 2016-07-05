package com.example.hasan.myapplication;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

/**
 * Created by hasan on 2016-06-02.
 */
public class LocationTracker implements LocationListener {

    // flag for GPS status
    boolean misGPSEnabled = false;

    // flag for network status
    boolean misNetworkEnabled = false;

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    // Declaring a Location Manager
    protected LocationManager mlocationManager;

    FileSave mFile = null;

    public void CreateLocationManager(LocationManager vManager) {
        mlocationManager = vManager;
        misGPSEnabled = mlocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        misNetworkEnabled = mlocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        mFile = new FileSave("LocationData.txt", false);
        GetLocation();
    }

    @Override
    public void onLocationChanged(Location location) {

        if (location != null){
            String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date()) + ": ";
            String Content = currentDateTimeString + "Pos: " + location.getLatitude() + " " + location.getLongitude();
            mFile.appendLog(Content);
        }
    }

    public void UnRegister(){
        mFile.flushToLog();
    }

    public void GetLocation()
    {
        Location location = null;

        if (!misGPSEnabled && !misNetworkEnabled) {
            // no network provider is enabled
        } else {
            // First get location from Network Provider
            if (misGPSEnabled) {
                mlocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,  MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                location = mlocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
            // if GPS Enabled get lat/long using GPS Services
            if (location == null && misNetworkEnabled) {
                mlocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                location = mlocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            if (location != null){
                String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date()) + ": ";
                String Content = currentDateTimeString + "Pos: " + location.getLatitude() + " " + location.getLongitude();
                mFile.appendLog(Content);
            }

        }
    }


    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

}
