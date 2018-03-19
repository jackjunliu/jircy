# Team JIRCY
CS48 Team Project
## Team Members
<pre>
Jack Liu
Irene Pattarachanyakul
Ronald Hoang
Charles Gu
Yoon Lee
</pre>

Android Application for Mass Messaging and Making Friends

## Status
Travis Build testing software errors.
[![Build Status](https://travis-ci.org/jackjunliu/jircy.svg?branch=master)](https://travis-ci.org/jackjunliu/jircy)

## Building the Application from Android Phone
### Device Requirements
<pre>
* Android Phone
* Android phone must run with firmware API 24+ (Nougat 7.0+)
</pre>

### Running the Application
<pre>
1. Have an android Phone
2. Download APK from releases folder
3. Install APK
    3.1 If you cannot install due to a unknown source
    3.2 Go to Settings
    3.3 Go to Security
    3.4 Tick the boxes for Unknown Sources and then click Trust
4. Install Application
5. Click on "No New Friends" application to start
</pre>

## Running the Application without an Android Phone
### Device Requirements
<pre>
Windows
* Microsoft Windows 7/8/10 (32-bit or 64-bit)
* 2 GB RAM minimum, 8 GB RAM recommended
* 2 GB of available disk space minimum, 4 GB Recommended (500 MB for IDE + 1.5 GB for Android SDK and emulator system image)
* 1280 x 800 minimum screen resolution
* JDK 8

Mac
* Mac OS X 10.8.5 or higher, up to 10.11.4 (El Capitan)
* 2 GB RAM minimum, 8 GB RAM recommended
* 2 GB of available disk space minimum, 4 GB Recommended (500 MB for IDE + 1.5 GB for Android SDK and emulator system image)
* 1280 x 800 minimum screen resolution
* JDK 6

Linux
* GNOME or KDE desktop: Tested on Ubuntu 12.04, Precise Pangolin (64-bit distribution capable of running 32-bit applications)
* 64-bit distribution capable of running 32-bit applications
* GNU C Library (glibc) 2.11 or later
* 2 GB RAM minimum, 8 GB RAM recommended
* 2 GB of available disk space minimum, 4 GB Recommended (500 MB for IDE + 1.5 GB for Android SDK and emulator system image)
* 1280 x 800 minimum screen resolution
* JDK 8
</pre>

### Running the Application on Android Studio
<pre>
1. Download Android Studio
    https://developer.android.com/studio/index.html
2. Run Android Studio
3. Download the files from out Github
4. On Android Studio, go to File -> Open and open our Jircy folder
5. Go to Build -> Make Project
6. Go to Run -> Run'app'
    6.1 You will then have to choose an emulator, and it must have to be at least firmware API 24+ or Nougat 7.0+
7. The application will then run, and you will be able to click on "No New Friends"
</pre>


## Current Bugs in our Program
<pre>
* OneSignal sometimes does not record current location and broadcast message might be affected
* Notification sometimes does not appear for a long duration (~2-3minutes)
</pre>