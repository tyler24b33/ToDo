package org.example.todo;
import java.time.LocalDate;
import java.time.LocalTime;


   public class Event {
// define private variables
      public String title;
      public String description;
      public LocalDate date;
      public LocalTime time;
      public Reminder reminder;

// create Event object and invoke this. method
      public Event(String title, LocalDate date, LocalTime time, String description) {
         this.title = title;
         this.date = date;
         this.time = time;
         this.description = description;
      }
      // getter methods for event Title, Date, and time
      public String getTitle() {
         return title;
      }

      public LocalDate getDate() {
         return date;
      }

      public LocalTime getTime() {
         return time;
      }

       //Reminder Getter
      public Reminder getReminder() {
         return reminder;
      }

       public String getDescription() {
           return description;
       }
   }
