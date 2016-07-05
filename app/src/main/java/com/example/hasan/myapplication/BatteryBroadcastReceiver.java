package com.example.hasan.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by hasan on 2016-06-17.
 */
public class BatteryBroadcastReceiver extends BroadcastReceiver {

    private FileSave mFile = new FileSave("Charge.txt", true);

    @Override
    public void onReceive(Context context, Intent intent) {
        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date()) + ": ";
        String Content = currentDateTimeString + "Battary level: " + String.valueOf(level);
        mFile.appendLog(Content);


    }
}