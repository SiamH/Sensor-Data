package com.example.hasan.myapplication;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Service;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;

class CheckList implements Serializable
{
    boolean mbAcc;
    boolean mbGyr;
    boolean mbMag;
    boolean mbLight;
    boolean mbProx;
    boolean mbPress;
    boolean mbLocation;
    boolean mbCharge;

    public CheckList()
    {
       flush();
    }

    public void flush()
    {
        mbAcc = false;
        mbGyr = false;
        mbMag = false;
        mbLight = false;
        mbProx = false;
        mbPress = false;
        mbLocation = false;
        mbCharge = false;
    }

    public boolean DoCalculateCharge()
    {
        return mbCharge;
    }

    public boolean LogLocationPoints()
    {
        return  mbLocation;
    }

    public boolean LogSensorData()
    {
        return (mbAcc || mbGyr || mbMag || mbLight || mbProx || mbPress);
    }

    public boolean DoCalculateChargeOnly()
    {
        if (!DoCalculateCharge())
            return false;

        return !(LogSensorData() || LogLocationPoints());
    }
}

public class MainActivity extends AppCompatActivity {

    CheckList mCheckList = new CheckList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.Acc:
                mCheckList.mbAcc = checked;
                break;
            case R.id.Gyr:
                mCheckList.mbGyr = checked;
                break;
            case R.id.Mag:
                mCheckList.mbMag = checked;
                break;
            case R.id.Light:
                mCheckList.mbLight = checked;
                break;
            case R.id.prox:
                mCheckList.mbProx = checked;
                break;
            case R.id.press:
                mCheckList.mbPress = checked;
                break;
            case R.id.loc:
                mCheckList.mbLocation = checked;
                break;
            case R.id.charge:
                mCheckList.mbCharge = checked;
                break;
        }
    }


    public void startIt(View view)
    {
        setContentView(R.layout.activity_main);
        TextView tView = (TextView) findViewById(R.id.Message);
        tView.setText("Process Running");


        String permission = "android.permission.WRITE_EXTERNAL_STORAGE";
        if (getBaseContext().checkCallingOrSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
        {
            String[] perms = {"android.permission.WRITE_EXTERNAL_STORAGE"};

            int permsRequestCode = 200;
            this.requestPermissions(perms, permsRequestCode);
        }

        permission = "android.permission.ACCESS_FINE_LOCATION";
        if (getBaseContext().checkCallingOrSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
        {
            String[] perms = {"android.permission.ACCESS_FINE_LOCATION"};

            int permsRequestCode = 200;
            this.requestPermissions(perms, permsRequestCode);
        }

        Intent Start = new Intent(getBaseContext(), Serv.class);
        Start.putExtra("CheckList", (Serializable) mCheckList);
        startService(Start);
    }

    public void stopIt(View view)
    {
        mCheckList.flush();
        setContentView(R.layout.activity_main);
        TextView tView = (TextView) findViewById(R.id.Message);
        tView.setText("Process Stopped");
        stopService(new Intent(getBaseContext(), Serv.class));
    }
}

