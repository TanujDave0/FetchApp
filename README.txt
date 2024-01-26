Hello! Welcome to my Fetch Application

This is a very basic Android application written in Java that
performs the tasks listed on https://fetch-hiring.s3.amazonaws.com/mobile.html

Quick Overview:
TextBox (scrollable) - Displays the welcome message and database query results
Fetch Button - Fetchs the data from the url
Clear Database Button - clears the database
Toggle switch - When off displays the query rows, when on displays all the rows
Results appear as -- id | listId | name

A simple explanation of the structure/files:
1 activity - main avticity
1 fragement - FirstFrag, bound to the main activity
SQLHelper - SQLlite helper. Creates, stores and retrieves data
fRow - class to store each row


On clicking the fetch button
- ExecutorSerivce asynchronously fetchs the JSON file from the URL
- SQLlite on Android to stores data on a local database and retrieves based on the option


Things to note:
- Results appear as -- id | listId | name
- This apps has persistent database storage, thus if you reopen the app, it will have the database from the previous run. Clear the database to see updated query results as required.
- The query does not change the database, thus changing the toggle switch on or off to display all or just the query rows will not alter the database

my email: tdave6@Uic.edu