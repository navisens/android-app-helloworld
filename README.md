# Android App Hello World

An example Java Android project using the Navisens MotionDNA SDK

## What it does
This project builds and runs a bare bones implementation of our SDK core. 

The core is on startup, triggering a call to the ```startMotionDna:``` method in the MainActivity.java. After this occurs the activity checks for necessary location permission and if requirements are satisfied, begins to receive Navisens MotionDNA location estimates through the ```receiveMotionDna:``` callback method. The data received is used to update the appropriate TextView element with a user's relative x,y and z coordinated along with GPS data and motion categorizations.

If multiple devices are running the app with the same developer key and have and active network connection, their device type and delative xyz coordinates will be listed at the bottom of the screen.

Before attempting to run this project please be sure to obtain a develepment key from Navisens. A key may be acquired free for testing purposes at [this link](https://navisens.com/index.html#contact)

For more complete documentation on our SDK please visit our [NaviDocs](https://github.com/navisens/NaviDocs)

___Note: This app is designed to run on Android 4.1 or higher___


## Setup

Enter your developer key in `app/src/main/java/com/navisens/demo/android_app_helloworld/MainActivity.java` and run the app.
```java
public void startMotionDna() {
        String devKey = "<ENTER YOUR DEV KEY HERE>";
```

Walk around and see the position.

## How the SDK works

Please refer to our [NaviDoc](https://github.com/navisens/NaviDocs/blob/master/API.Android.md#api) for full documentation.

Here's some quick explaination for how to use our SDK in [android-app-helloworld](https://github.com/navisens/android-app-helloworld):

Add `implementation group: "com.navisens", name: "motiondnaapi", version: "1.9.0", changing: true` into dependencies section in `app/build.gradle` file to use our SDK.

In our SDK we provide `MotionDnaApplication` class and `MotionDnaInterface` interface. In order for MotionDna to work, we need a class implements all callback methods in the interface.  
In [android-app-helloworld](https://github.com/navisens/android-app-helloworld) it looks like this  
`public class MainActivity extends AppCompatActivity implements MotionDnaInterface`

In callback function we return `MotionDna` which contains [location, heading and motion type](https://github.com/navisens/NaviDocs/blob/master/API.Android.md#getters). Let's print it out.
```java
@Override
    public void receiveMotionDna(MotionDna motionDna)
    {
        String str = "Navisens MotionDna Location Data:\n";
        str += "Lat: " + motionDna.getLocation().globalLocation.latitude + " Lon: " + motionDna.getLocation().globalLocation.longitude + "\n";
        MotionDna.XYZ location = motionDna.getLocation().localLocation;
        str += String.format(" (%.2f, %.2f, %.2f)\n",location.x, location.y, location.z);
        str += "Hdg: " + motionDna.getLocation().heading +  " \n";
        str += "motionType: " + motionDna.getMotion().motionType + "\n";
        ...
```

Declare, and pass the class which implements `MotionDnaInterface`  
```java
MotionDnaApplication motionDnaApplication;
motionDnaApplication = new MotionDnaApplication(this);
```

Run MotionDna  
```java
motionDnaApplication.runMotionDna(devKey);
```

Add some configurations  
```java
motionDnaApplication.setLocationNavisens();
motionDnaApplication.setCallbackUpdateRateInMs(500);
```

More configurations are listed [here](https://github.com/navisens/NaviDocs/blob/master/API.Android.md#control)
