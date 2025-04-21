package org.example.todo;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class ThreeDayView {
//variables access
    private final EventManager eventManager;
    private final Stage primaryStage;
    private final Runnable backToMonthView;
    
// three dayView method
    public ThreeDayView(EventManager eventManager, Stage primaryStage, Runnable backToMonthView) {
        this.eventManager = eventManager;
        this.primaryStage = primaryStage;
        this.backToMonthView = backToMonthView;
    }
    // create content box to generate 3 da view layout
    public VBox createContent() {
        VBox root = new VBox();

        LocalDate today = LocalDate.now();
        YearMonth currentMonth = YearMonth.from(today);
        String monthTitle = currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy"));
        HBox topBar = getTopBar(monthTitle);

        GridPane grid = new GridPane();

        ColumnConstraints hourCol = new ColumnConstraints(100);
        hourCol.setHgrow(Priority.NEVER);
        grid.getColumnConstraints().add(hourCol);

        for (int i = 0; i < 3; i++) {
            ColumnConstraints dayCol = new ColumnConstraints();
            dayCol.setHgrow(Priority.ALWAYS);
            dayCol.setPercentWidth(100.0 / 3);
            grid.getColumnConstraints().add(dayCol);
        }

        // === Headers row ===
        for (int day = 0; day < 3; day++) {
            LocalDate date = today.plusDays(day);
            String dayName = date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
            String label = dayName + " " + date.getMonthValue() + "/" + date.getDayOfMonth();
            StackPane header = new StackPane(new Text(label));
            header.setAlignment(Pos.CENTER);
            header.setPadding(new Insets(5));
            header.setMinHeight(40);
            grid.add(header, day + 1, 0);
        }

        // === Time rows and clickable slots ===
        for (int hour = 0; hour < 24; hour++) {
            RowConstraints row = new RowConstraints(50);
            grid.getRowConstraints().add(row);

            StackPane hourCell = new StackPane(new Text(String.format("%02d:00", hour)));
            hourCell.setAlignment(Pos.CENTER_LEFT);
            hourCell.setPadding(new Insets(5));
            grid.add(hourCell, 0, hour + 1);

            Region line = new Region();
            line.setStyle("-fx-border-color: lightgray; -fx-border-width: 0 0 1 0;");
            GridPane.setColumnSpan(line, 3);
            GridPane.setColumnIndex(line, 1);
            GridPane.setRowIndex(line, hour + 1);
            grid.getChildren().add(line);

            for (int day = 0; day < 3; day++) {
                LocalDate date = today.plusDays(day);

                // Slot region (for background click)
                Region slot = new Region();
                slot.setMinHeight(50);
                slot.setOnMouseClicked(_ -> {
                    List<Event> events = eventManager.getEvents(date);
                    FormGUI.show(date, events, primaryStage);
                });
                grid.add(slot, day + 1, hour + 1);

                // Draw any matching events
                List<Event> events = eventManager.getEvents(date);
                for (Event event : events) {
                    if (event.getTime() != null && event.getTime().getHour() == hour) {
                        String formattedTime = event.getTime().format(DateTimeFormatter.ofPattern("HH:mm"));
                        StackPane eventBox = getStackPane(event, formattedTime, date);

                        GridPane.setColumnIndex(eventBox, day + 1);
                        GridPane.setRowIndex(eventBox, hour + 1);
                        grid.getChildren().add(eventBox);
                    }
                }
            }
        }

        ScrollPane scrollPane = new ScrollPane(grid);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        root.getChildren().addAll(topBar, scrollPane);
        return root;
    }
    //stack pane to get eventBox details
    private StackPane getStackPane(Event event, String formattedTime, LocalDate date) {
        StackPane eventBox = getStackPane(event, formattedTime);
        eventBox.setPickOnBounds(true);
        eventBox.setMaxWidth(Double.MAX_VALUE);
        eventBox.setOnMouseClicked(ev -> {
            Stage detailStage = new Stage();
            VBox layout = new VBox(10);
            layout.setPadding(new Insets(15));
            layout.setAlignment(Pos.CENTER_LEFT);
            layout.getChildren().addAll(
                    new Label("Title: " + event.getTitle()),
                    new Label("Time: " + (event.getTime() != null ? event.getTime().toString() : "N/A")),
                    new Label("Date: " + date),
                    new Label("Reminder: " + (event.getReminder() != null ? event.getReminder().toString() : "None")),
                    new Label("Description: " + event.getDescription())

            );
            Scene scene = new Scene(layout, 300, 200);
            detailStage.setScene(scene);
            detailStage.setTitle("Event Details");
            detailStage.initOwner(primaryStage);
            detailStage.show();
            ev.consume();
        });
        return eventBox;
    }
// method to get stack pane
    private static StackPane getStackPane(Event event, String formattedTime) {
        String labelText = formattedTime + " - " + event.getTitle();

        Label eventLabel = new Label(labelText);
        eventLabel.setStyle(
                "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #000000;" +
                        "-fx-background-color: #add8e6;" +  // light blue
                        "-fx-border-color: #00008b;" +      // dark blue
                        "-fx-border-width: 1;" +
                        "-fx-padding: 6;"
        );

        return new StackPane(eventLabel);
    }
// method to get top-bar label and button
    private HBox getTopBar(String monthTitle) {
        Text monthLabel = new Text(monthTitle);
// switch view hamburger button to switch back to month view
        Button hamburger = new Button("☰");
        hamburger.setOnAction(_ -> backToMonthView.run());
     // TaskButton to add to 3 day view
        Button addTaskButton = new Button("Add Task");
        addTaskButton.setOnAction(_ -> FormGUI.showAddTaskForm(eventManager, primaryStage, () -> {
            ThreeDayView refreshedView = new ThreeDayView(eventManager, primaryStage, backToMonthView);
            primaryStage.setScene(new Scene(refreshedView.createContent(), 1000, 800));
        }));
        // navigation buttons for three day view
        HBox topBar = new HBox(10, hamburger, monthLabel, addTaskButton);
        topBar.setPadding(new Insets(10));
        topBar.setAlignment(Pos.CENTER_LEFT);
        return topBar;
    }
}