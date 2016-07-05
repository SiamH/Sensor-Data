package com.example.hasan.myapplication;

import android.app.ActivityManager;

import java.io.File;
import java.util.Iterator;
import java.util.List;

/**
 * Created by hasan on 2016-06-17.
 */
public class ProcessList {

    public  String mLog = "";
    public void AddLog(String Token1, String Token2)
    {
        mLog = mLog + Token1 + ": " + Token2;
        mLog = mLog + System.getProperty("line.separator");
    }

    public void AddLog(String Token1, int Token2)
    {
        mLog = mLog + Token1 + ": " + String.valueOf(Token2);
        mLog = mLog + System.getProperty("line.separator");
    }

    public void CalculateProcess(ActivityManager vManager, boolean bCalculateChargeOnly)
    {
        String Log = "";
        List<ActivityManager.RunningAppProcessInfo> processes = vManager.getRunningAppProcesses();

        AddLog("Number of Process", processes.size());

        for(Iterator i = processes.iterator(); i.hasNext(); ) {
            ActivityManager.RunningAppProcessInfo p = (ActivityManager.RunningAppProcessInfo) i.next();

            AddLog("Process name", p.processName);
            AddLog("PID", p.pid);
            int[] pids = new int[1];
            pids[0] = p.pid;

            android.os.Debug.MemoryInfo[] MI = vManager.getProcessMemoryInfo(pids);
            AddLog("Total private dirty memory (KB)", MI[0].getTotalPrivateDirty());
            AddLog("Total shared memory (KB)", MI[0].getTotalSharedDirty());
            AddLog("Total pss", MI[0].getTotalPss());
        }


        String FileName = "ProcessListWithSensors.txt";
        if (bCalculateChargeOnly)
            FileName = "ProcessListWithoutSensors.txt";

        FileSave file = new FileSave(FileName, true);
        file.appendLog(mLog);
    }
}
