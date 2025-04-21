package org.example.todo;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EventManager {
    //creates arraylist to store event details
    private final List<Event> events = new ArrayList<>();
    //add event method
    public void addEvent(Event event) {
        events.add(event);
    }

    // list to get events for a specific date
    public List<Event> getEvents(LocalDate date) {
        List<Event> filteredEvents = new ArrayList<>();
        for (Event event : events) {
            if (event == null) continue;
            if (event.getDate() == null) continue;

            if (event.getDate().equals(date)) {
                filteredEvents.add(event);
            }
        }
        return filteredEvents;
    }

}

