package com.example.hasan.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

/**
 * Created by hasan on 2016-05-20.
 */
public class SensorActivity implements SensorEventListener {
    private SensorManager mSensorManager;

    private Sensor mLight = null;
    private Sensor mAcclerator = null;
    private Sensor mGyroscope = null;
    private Sensor mMagnetometer = null;
    private Sensor mProximity = null;
    private Sensor mPressure = null;
    FileSave mFile = null;
    CheckList mCheckList;

    public void RegsiterSensors(SensorManager vManager, CheckList list)
    {
        mSensorManager = vManager;
        mCheckList = list;

        if (mCheckList.mbLight)
        {
            mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
            mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (mCheckList.mbAcc)
        {
            mAcclerator = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mSensorManager.registerListener(this, mAcclerator, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (mCheckList.mbGyr)
        {
            mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
            mSensorManager.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (mCheckList.mbMag)
        {
            mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
            mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (mCheckList.mbProx)
        {
            mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
            mSensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (mCheckList.mbPress)
        {
            mPressure = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
            mSensorManager.registerListener(this, mPressure, SensorManager.SENSOR_DELAY_NORMAL);
        }

        mFile = new FileSave("SensorData.txt", false);
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        // The light sensor returns a single value.
        // Many sensors return 3 values, one for each axis.
        Sensor sensor = event.sensor;

        String TAG = "";

        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date()) + ": ";

        if (mCheckList.mbAcc == true && sensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
            TAG = "Accelerometer ";
        }
        else if (mCheckList.mbGyr == true && sensor.getType() == Sensor.TYPE_GYROSCOPE)
        {
            TAG = "Gyroscope ";
        }
        else if (mCheckList.mbMag == true && sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
        {
            TAG = "Magnetic ";
        }
        else if (mCheckList.mbLight == true && sensor.getType() == Sensor.TYPE_LIGHT)
        {
            TAG = "Light ";
        }
        else if (mCheckList.mbProx == true && sensor.getType() == Sensor.TYPE_PROXIMITY)
        {
            TAG = "Proximity ";
        }
        else if (mCheckList.mbPress == true && sensor.getType() == Sensor.TYPE_PRESSURE)
        {
            TAG = "Pressure ";
        }

        if (TAG != "")
        {
            String Content = currentDateTimeString + TAG + " " + event.values[0] + " " + event.values[1] + " " + event.values[2];
            mFile.appendLog(Content);
        }
    }


    public void UnRegister()
    {
        mSensorManager.unregisterListener(this);
        mFile.flushToLog();
    }

}
