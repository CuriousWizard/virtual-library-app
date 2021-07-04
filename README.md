# My Virtual Library
This application is my final project for Udacity's Android Kotlin Developer Nanodegree Program to demonstrate core Android Development skills. My goal was to create an app which allows users to add books to a list by scanning its barcode or enter its detail manually, and stores that list in a database.

**Please note:** This application uses Moly.hu as the remote database. It can occur that you can't find your book using barcode scanning feature.
For more features please check docs.

## Screenshots
<p align="center">
<img src=screenshots/Screenshot_20210702-225442.jpg width=30% height=30%>
<img src=screenshots/Screenshot_20210702-231110.jpg width=30% height=30%>
<img src=screenshots/Screenshot_20210703-121620.jpg width=30% height=30%>
</p>
<p align="center">
<img src=screenshots/Screenshot_20210702-231129.jpg width=30% height=30%>
<img src=screenshots/Screenshot_20210702-231141.jpg width=30% height=30%>
</p>

## Getting started
Here is a little tutorial what should you 

1. Clone or download this project
2. Copy app-debug.apk to your Android device
3. Enable "install unknown apps" and install the app
4. Start the application and enjoy it!

**OR** if you want to use this source code:
- Get API key from Moly.hu website
- Create an "apikey.properties" file
- Copy and paste the following line:
```
moly_api_key="YOUR_API_KEY"
```

## Built With

* [Retrofit](https://square.github.io/retrofit/) to make api calls to an HTTP web service.
* [Moshi](https://github.com/square/moshi) which handles the deserialization of the returned JSON to Kotlin data objects. 
* [Glide](https://bumptech.github.io/glide/) to load and cache images by URL.
* [Room](https://developer.android.com/training/data-storage/room) for local database storage.
* [ML Kit](https://developers.google.com/ml-kit/vision/barcode-scanning) for barcode decoding
* [CameraX](https://developer.android.com/training/camerax) to use and analyze camera footage
