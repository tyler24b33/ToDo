# Tyler-CSI-2300-Course-Project
1. Your project name, Tyler Birmingham
2. I am trying to build a To Do  Task Scheduling App That has a Very Simplistic View To not Overwhelm Users, I want to Build it Because I have problems with Other apps Where you have to pay for a month view and Have a view of soon to do Tasks. It will be Useful To Schedule Asignments, Mettings, etc It will allow you to see soon ToDo events at a Glance
3.
![image](https://github.com/user-attachments/assets/b98f8520-6a28-460f-958c-5043f1e9aae5).

4. Plan and estimate of effortCoding, Devloping, Impleting etc 100% effort By Me.
 # implementation Manual
5.) updated uml diagram 

![image](https://github.com/user-attachments/assets/22cd5a6a-cf18-4b1b-adfa-b339b7424d85)

The project has 6 main classes and an additonal class for future implementation

To Do Class
extends the application class and starts and creates the calendar view class and stage. it also creates and owns the Calendarview class.

CalendarView class
contains the calander view gui methods to display the calendar view and methods to update and refresh the month view and a method to ceate and switch to the three day view. it ceates the ThreedayView class. it alls calls the FormGUI class to show date popup and eventdetails and to show taskform gui.

ThreeDay view Class 
The three day view class contains the three day view gui class methods to display the 3 day view gui. it also contains an eventmanager method to use the eventmanager class. it alls calls the FormGUI class to show date popup and eventdetails and to show taskform gui.

FormGUI class
The Form GUI has methods to show datepopup gui and events on that day and a method to sho the details of the events on that day and a method to show the addtaskform gui. it uses eventManager class to record event in event arraylist and creats and event from the event class to store event details.

EventManager class
The event manager class manages all the events through methods to add events to the application that manages the event details from the event class.

Event class
the event class stores the event details that were entered into the addtask form gui popup and saved.

Reminder class
hs reminder method for futer implemtation

#User GUIDE
