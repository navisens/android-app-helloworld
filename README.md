# Android App Hello World

An example Java Android project using the Navisens MotionDNA SDK

## What it does
This project builds and runs a bare bones implementation of our SDK core. 

The core is on startup, triggering a call to the ```startMotionDna:``` method in the MainActivity.java. After this occurs the activity checks for necessary location permission and if requirements are satisfied, begins to receive Navisens MotionDNA location estimates through the ```receiveMotionDna:``` callback method. The data received is used to update the appropriate TextView element with a user's relative x,y and z coordinated along with GPS data and motion categorizations.

If multiple devices are running the app with the same developer key and have and active network connection, their device type and delative xyz coordinates will be listed at the bottom of the screen.

Before attempting to run this project please be sure to obtain a develepment key from Navisens. A key may be acquired free for testing purposes at [this link](https://navisens.com/index.html#contact)

For more complete documentation on our SDK please visit our [NaviDocs](https://github.com/navisens/NaviDocs)

___Note: This app is designed to run on Android 4.1 or higher___
