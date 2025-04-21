package org.example.todo;

import javafx.application.Application;
import javafx.stage.Stage;


/**
 * JavaFX App
 */
public class ToDoApp extends Application {
    // starts and creates Application primary stage and Calendar view
    @Override
    public void start(Stage primaryStage) {
        CalendarView calendarView = new CalendarView();
        calendarView.start(primaryStage);
        primaryStage.setTitle("ToDoApp");
    }

    public static void main(String[] args) {
        launch(args);
    }
}

