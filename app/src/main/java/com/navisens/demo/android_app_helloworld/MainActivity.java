package com.navisens.demo.android_app_helloworld;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.navisens.motiondnaapi.MotionDna;
import com.navisens.motiondnaapi.MotionDnaApplication;
import com.navisens.motiondnaapi.MotionDnaInterface;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import static android.os.SystemClock.elapsedRealtime;

public class MainActivity extends AppCompatActivity implements MotionDnaInterface{

    MotionDnaApplication motionDnaApplication;
    Hashtable<String, MotionDna> networkUsers = new Hashtable<String, MotionDna>();
    Hashtable<String, Double> networkUsersTimestamps = new Hashtable<String, Double>();
    TextView textView;
    TextView networkTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.HELLO);
        networkTextView = findViewById(R.id.network);
        startMotionDna();
    }

    public void startMotionDna() {
        String devKey = "<ENTER YOUR DEV KEY HERE>";

        motionDnaApplication = new MotionDnaApplication(this);
        motionDnaApplication.runMotionDna(devKey);
        motionDnaApplication.setExternalPositioningState(MotionDna.ExternalPositioningState.LOW_ACCURACY);
//        motionDnaApplication.setLocationAndHeadingGPSMag();
//        motionDnaApplication.setLocationLatitudeLongitudeAndHeadingInDegrees(37.787582, -122.396627, 0);

        motionDnaApplication.setPowerMode(MotionDna.PowerConsumptionMode.LOW_CONSUMPTION);
        motionDnaApplication.startUDPHostAndPort("45.79.101.164", "6512");
        motionDnaApplication.setBinaryFileLoggingEnabled(true);
        motionDnaApplication.setCallbackUpdateRateInMs(500);
        motionDnaApplication.setBackpropagationEnabled(true);
        motionDnaApplication.setARModeEnabled(true);
        motionDnaApplication.setARModeEnabled(false);
    }

    @Override
    public void receiveMotionDna(MotionDna motionDna)
    {
        String str = "Navisens MotionDna Location Data:\n";
        str += "Lat: " + motionDna.getLocation().globalLocation.latitude + " Lon: " + motionDna.getLocation().globalLocation.longitude + "\n";
        MotionDna.XYZ location = motionDna.getLocation().localLocation;
        str += String.format(" (%.2f, %.2f, %.2f)\n",location.x, location.y, location.z);
        str += "Hdg: " + motionDna.getLocation().heading +  " \n";
        str += "motionType: " + motionDna.getMotion().motionType + "\n";
        textView.setTextColor(Color.BLACK);

        final String fstr = str;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(fstr);
            }
        });
    }

    @Override
    public void receiveNetworkData(MotionDna motionDna) {
        // This method will be called by the MotionDna core
        // if network sharing is enabled with the startUDP()
        // or related method calls you will get location
        // information for other devices using the same key
        // with network sharing enabled
        networkUsers.put(motionDna.getID(),motionDna);
        double timeSinceBootSeconds = elapsedRealtime() / 1000.0;
        networkUsersTimestamps.put(motionDna.getID(),timeSinceBootSeconds);
        StringBuilder activeNetworkUsersStringBuilder = new StringBuilder();
        List<String> toRemove = new ArrayList();

        activeNetworkUsersStringBuilder.append("Network Shared Devices:\n");
        for (MotionDna user: networkUsers.values()) {
            if (timeSinceBootSeconds - networkUsersTimestamps.get(user.getID()) > 2.0) {
                toRemove.add(user.getID());
            } else {
                activeNetworkUsersStringBuilder.append(user.getDeviceName());
                MotionDna.XYZ location = user.getLocation().localLocation;
                activeNetworkUsersStringBuilder.append(String.format(" (%.2f, %.2f, %.2f)",location.x, location.y, location.z));
                activeNetworkUsersStringBuilder.append("\n");
            }

        }
        for (String key: toRemove) {
            networkUsers.remove(key);
            networkUsersTimestamps.remove(key);
        }

        networkTextView.setText(activeNetworkUsersStringBuilder.toString());
    }

    @Override
    public void receiveNetworkData(MotionDna.NetworkCode networkCode, Map<String, ?> map) {

    }

    @Override
    public void reportError(MotionDna.ErrorCode errorCode, String s) {
        switch (errorCode) {
            case ERROR_AUTHENTICATION_FAILED:
                System.out.println("Error: authentication failed " + s);
                break;
            case ERROR_SDK_EXPIRED:
                System.out.println("Error: SDK expired " + s);
                break;
            case ERROR_PERMISSIONS:
                System.out.println("Error: permissions not granted " + s);
                break;
            case ERROR_SENSOR_MISSING:
                System.out.println("Error: sensor missing " + s);
                break;
            case ERROR_SENSOR_TIMING:
                System.out.println("Error: sensor timing " + s);
                break;
        }
    }

    @Override
    public PackageManager getPkgManager() {
        return getPkgManager();
    }

    @Override
    public Context getAppContext() {
        return getApplicationContext();
    }
}
