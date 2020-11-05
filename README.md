# Android App Hello World (Java)

An example Java Android project using the Navisens MotionDNA SDK

___Note: This app is designed to run on Android 5.0 or higher___


## What it does
This project builds and runs a bare bones implementation of our SDK core.

The core is on startup, triggering a call to the ```startMotionDna()``` method in the MainActivity.java. After this occurs the activity checks for necessary location permission and if requirements are satisfied, begins to receive Navisens MotionDNA location estimates through the ```receiveMotionDna()``` callback method. The data received is used to update the appropriate TextView element with a user's relative x,y and z coordinated along with GPS data and motion categorizations.

If multiple devices are running the app with the same developer key and have and active network connection, their device type and delative xyz coordinates will be listed at the bottom of the screen.

Before attempting to run this project please be sure to obtain a develepment key from Navisens. A key may be acquired free for testing purposes at [this link](https://navisens.com/index.html#contact)

For more complete documentation on our SDK please visit our [NaviDocs](https://github.com/navisens/NaviDocs)



## Setup

Enter your developer key in `app/src/main/java/com/navisens/demo/android_app_helloworld/MainActivity.java` and run the app. If you do not have one yet then please navigate to our [developer sign up](https://www.navisens.com/index.html#contact) to request a free key.
```java
public void startMotionDna() {
        String devKey = "<--ENTER YOUR DEVELOPER KEY HERE-->";
```

Walk around and see the position.

## How the SDK works

Please refer to our [NaviDoc](https://github.com/navisens/NaviDocs/blob/master/API.Android.md#api) for full documentation.

### How you include (and update) the SDK

Add `implementation group: "com.navisens", name: "motiondnaapi", version: "2.0.1", changing: true` into dependencies section in `app/build.gradle` file to use our SDK.

### How you get your [estimated] position

In our SDK we provide `MotionDnaSDK` class and `MotionDnaSDKListener` interface. In order for MotionDna to work, we need a class implements all callback methods in the interface.
In this project it looks like this
```
public class MainActivity extends AppCompatActivity implements MotionDnaSDKListener
```
In the `receiveMotionDna()` callback method we return a `MotionDna` estimation object which contains [location, heading and motion type](https://github.com/navisens/NaviDocs/blob/master/API.Android.md#getters) among many other interesting data on a users current state. Here is how we might print it out.
```java
@Override
    public void receiveMotionDna(MotionDna motionDna)
    {
        String str = "Navisens MotionDnaSDK Estimation:\n";
        str += MotionDnaSDK.SDKVersion() + "\n";
        str += "Lat: " + motionDna.getLocation().global.latitude + " Lon: " + motionDna.getLocation().global.longitude + "\n";
        MotionDna.CartesianLocation location = motionDna.getLocation().cartesian;
        str += String.format(" (%.2f, %.2f, %.2f)\n",location.x, location.y, location.z);
        str += "Hdg: " + motionDna.getLocation().global.heading +  " \n";
        str += "motionType: " + motionDna.getClassifiers().get("motion").prediction.label + "\n";
        ...
```

### How you instantiate the SDK with a receiver

Declare `MotionDnaSDK`, and pass the class which implements `MotionDnaSDKListener`
```java
MotionDnaSDK motionDnaSDK;
motionDnaSDK = new MotionDnaSDK(this);
```
## Common Configurations (with code examples)
### Startup
```java
motionDnaSDK.start("<developer-key>");
```
### Startup with Configuration (Model Selection)
Additional configuration options will be added over time. Current configuration options are only for model seletion in motion estimation. Currently supported models are "standard", "headmount", and "chestmount".

```java
HashMap<String, Object> configuration = new HashMap<>();
configuration.put("model","standard");
motionDnaSDK.start("<developer-key>",configuration);
```

### _Assigning initial position Locally (Cartesian X and Y coordinates)_
#### Common Task:

You wish to update your X and Y positions to 3 in the X and 4 meters in the Y direction. Heading should not be affected
``` motionDnaSDK.setCartesianPosition(3,4); ```


-------------

### _Assigning initial position Globally (Latitude and Longitude coordinates)_

#### Common Tasks:
 You need to update the latitude and longitude to (37.756581, -122.419155). Heading can be taken from the device's compass

``` motionDnaSDK.setGlobalPosition(37.756581, -122.419155); ```

 You know the users location is latitude and longitude of (37.756581, -122.419155) with a heading of 3 degrees and need to indicate that to the SDK

``` motionDnaSDK.setGlobalPositionAndHeading(37.756581, -122.419155, 3.0); ```


------------

### _Observations (EXPERIMENTAL)_
#### Common Task:
A user is indoors and revisits the same areas frequently. Through some outside mechanism the developer is aware of a return to certain landmarks and would like to indicate that the user has returned to a landmark with ID of 38 to aid in the estimation of a user's position. The developer also knows that this observation was made within 3 meters of the landmark 38
``` motionDnaSDK.recordObservation(38,3.0); ```



### _More API options are listed in our [Android Documentation](https://github.com/navisens/NaviDocs/blob/master/API.Android.md#control)_
