# Interval Timer App
by Mac Lyle

## Overview
This app started as an independent project to teach myself Java as well as learn more about UI development and interacting with various APIs. This app is designed to be a interval workout timer. This allows for pacing of workouts by queuing different length timers.

My goal is to have a countdown timer which runs fully customizable workouts made up of individual timers and sets of timers. I also want the user to have the option to create an account where they will be able to store and sync their workouts across multiple devices via a DynamoDB using AWS Datastore and AWS Cognito sign in flow.  

## Contents
This app currently contains 5 main activities/pages: Home, Settings, Workout Edit, Set Edit, and Running. 

#### Home
This displays all available workouts that are saved on the device. As of now all storage is local. From this scrollable list you are able to launch into the running mode by tapping a workout item. You can also access the settings activity through the drop down menu in the toolbar.

#### Settings
This is where all user settings will be accessed. Currently you will find the option to toggle the auto pause features. Auto pause halts the countdown timer at the end of timers and/or sets automatically. This may be useful if a user needs to move or set up for a new exercise inbetween timers or sets.

#### Workout Edit
The workout edit activity is where the user is able to create their own custom workouts. Workouts can be named and deleted from the drop down menu and new timers and sets can be added via the expandable plus button. Timers and sets will appear in a list and can be rearranged by dragging and dropping their cards. Sliding the cards side to side revels the edit and delete options. Timers are created and edited through a dialog box while sets are created and edited in the set edit activity. 

#### Set Edit
This activity is almost identical to the workout edit page with the notable exclusion of further nested sets. Here sets can be renamed and deleted through the menu and timers can be manipulated by sliding them. The set also has the ability to be iterated with the counter. 

#### Running
Here is where the actual interval workout is executed. The current timer is displayed and shows the minutes and seconds remaining as well as the timer’s name if it has one, the set name, and which rep out of the total set reps you are on. Under this display is a dynamic list of the next timers are in the queue. As timers are completed, they are popped from the queue. There is also an overall progress bar at the top. 



ReadMe Last Updated: 12/4/20
