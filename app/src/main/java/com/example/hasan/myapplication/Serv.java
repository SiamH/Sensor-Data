package com.example.hasan.myapplication;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.IBinder;
import android.widget.Toast;

import java.util.List;

/**
 * Created by hasan on 2016-05-19.
 */
public class Serv extends Service{

    private SensorActivity mActivity = null;
    private LocationTracker mTracker = null;
    private BroadcastReceiver mReceiver = null;
    private ProcessList mList = null;

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startID)
    {
        CheckList checkList = (CheckList)intent.getSerializableExtra("CheckList");
        Toast.makeText(this, "Background Process Started", Toast.LENGTH_LONG).show();


        if (checkList.DoCalculateCharge())
        {
            mList = new ProcessList();
            ActivityManager mgr = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
            mList.CalculateProcess(mgr, checkList.DoCalculateChargeOnly());
        }

        if (checkList.LogSensorData())
        {
            mActivity = new SensorActivity();
            SensorManager SensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
            mActivity.RegsiterSensors(SensorManager, checkList);
        }


        if (checkList.mbLocation)
        {
            mTracker = new LocationTracker();
            LocationManager locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
            mTracker.CreateLocationManager(locationManager);
        }

        if (checkList.mbCharge) {
            mReceiver = new BatteryBroadcastReceiver();
            this.registerReceiver(mReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy()
    {
        if (mActivity != null)
        {
            mActivity.UnRegister();
        }
        if (mReceiver != null)
        {
            this.unregisterReceiver(mReceiver);
        }
        if (mTracker != null)
        {
            mTracker.UnRegister();
            LocationManager locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
            locationManager.removeUpdates(mTracker);
        }

        Toast.makeText(this, "Background Process Stopped", Toast.LENGTH_LONG).show();
        super.onDestroy();
    }

}
