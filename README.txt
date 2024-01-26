Hello! Welcome to my Fetch Application

This is a very basic Android application written in Java that
performs the tasks listed on https://fetch-hiring.s3.amazonaws.com/mobile.html

A simple explanation of the structure/files:
1 activity - main avticity
1 fragement - FirstFrag, bound to the main activity
SQLHelper - SQLlite helper. Creates, stores and retrieves data
fRow - class to store each row


On clicking the fetch button
- ExecutorSerivce asynchronously fetchs the JSON file from the URL
- SQLlite on Android to stores data on a local database and retrieves based on the option