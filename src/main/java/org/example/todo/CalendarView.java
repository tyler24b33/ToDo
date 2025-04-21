package org.example.todo;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CalendarView {
    // Calendar view variables and initializations
    private final BorderPane root = new BorderPane();
    private final GridPane monthGrid = new GridPane();
    private YearMonth currentMonth;
    private Label monthLabel;
    private final EventManager eventManager = new EventManager();
    private boolean showingThreeDayView = false;
    private final Button hamburgerButton = new Button("☰");
    public Stage primaryStage;

    public CalendarView() {
        // set calendar view and set size based on window size
        monthGrid.setPadding(new Insets(10));
        monthGrid.setHgap(0);
        monthGrid.setVgap(0);
        monthGrid.setStyle("-fx-background-color: white;");

        for (int i = 0; i < 7; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(100.0 / 7);
            monthGrid.getColumnConstraints().add(col);
        }

        for (int i = 0; i < 8; i++) {
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(100.0 / 9);
            monthGrid.getRowConstraints().add(row);
        }

        currentMonth = YearMonth.now();
    }
    // showMonth view method to show current month view method
    public void showMonthView() {
        monthGrid.getChildren().clear();
        monthLabel = new Label(currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")));

        Button prevButton = new Button("<");
        prevButton.setOnAction(_ -> {
            currentMonth = currentMonth.minusMonths(1);
            updateMonthView();
        });

        Button nextButton = new Button(">");
        nextButton.setOnAction(_ -> {
            currentMonth = currentMonth.plusMonths(1);
            updateMonthView();
        });

        // Top bar with hamburger left, month label center
        BorderPane topBar = new BorderPane();
        HBox leftBox = new HBox(hamburgerButton);
        leftBox.setAlignment(Pos.CENTER_LEFT);
        leftBox.setPadding(new Insets(0, 10, 0, 10));
        topBar.setLeft(leftBox);

        HBox centerBox = new HBox(prevButton, monthLabel, nextButton);
        centerBox.setSpacing(10);
        centerBox.setAlignment(Pos.CENTER);
        topBar.setCenter(centerBox);

        monthGrid.add(topBar, 0, 0, 7, 1);

        updateMonthView();
        root.setCenter(monthGrid);
    }
    //updateMonth view method to refresh view when switching between prev and next months
    private void updateMonthView() {
        monthGrid.getChildren().removeIf(node -> {
            Integer rowIndex = GridPane.getRowIndex(node);
            return rowIndex != null && rowIndex > 1;
        });
        // day headers for calendar view
        String[] dayNames = {"Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"};
        for (int i = 0; i < 7; i++) {
            Label label = new Label(dayNames[i]);
            label.setStyle("-fx-font-weight: bold;");
            monthGrid.add(label, i, 1);
        }

        monthLabel.setText(currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")));

        LocalDate firstOfMonth = currentMonth.atDay(1);
        int firstDayOfWeek = firstOfMonth.getDayOfWeek().getValue() % 7;
        int daysInMonth = currentMonth.lengthOfMonth();
        YearMonth prevMonth = currentMonth.minusMonths(1);
        int daysInPrevMonth = prevMonth.lengthOfMonth();

        int totalCells = daysInMonth + firstDayOfWeek;
        int rows = (int) Math.ceil(totalCells / 7.0);
        int dayCounter = 1 - firstDayOfWeek;
        int row = 2;
        int col = 0;

        for (int i = 0; i < rows * 7; i++) {
            StackPane dayPane = new StackPane();
            dayPane.setStyle("-fx-border-color: black; -fx-border-width: 0.5px;");
            Label dayLabel;
            LocalDate dateToRepresent;

            if (dayCounter < 1) {
                int displayDay = daysInPrevMonth + dayCounter;
                dayLabel = new Label(String.valueOf(displayDay));
                dayLabel.setTextFill(Color.GRAY);
                dateToRepresent = prevMonth.atDay(displayDay);
            } else if (dayCounter > daysInMonth) {
                int displayDay = dayCounter - daysInMonth;
                dayLabel = new Label(String.valueOf(displayDay));
                dayLabel.setTextFill(Color.GRAY);
                dateToRepresent = currentMonth.plusMonths(1).atDay(displayDay);
            } else {
                dayLabel = new Label(String.valueOf(dayCounter));
                dateToRepresent = currentMonth.atDay(dayCounter);
            }
            // StackPane Alignment and positioning
            StackPane.setAlignment(dayLabel, Pos.TOP_LEFT);
            StackPane.setMargin(dayLabel, new Insets(5));
            dayPane.getChildren().add(dayLabel);
            // method to get events and update them on calendar view
            List<Event> eventsForDay = eventManager.getEvents(dateToRepresent);
            VBox eventBox = new VBox();
            eventBox.setSpacing(2);

            for (Event event : eventsForDay) {
                String time = event.getTime() != null ? event.getTime().toString() : "";
                Label eventLabel = new Label(time + " " + event.getTitle());
                eventLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: blue;");
                eventBox.getChildren().add(eventLabel);
            }
            // event box alignment and location and event box logic
            StackPane.setAlignment(eventBox, Pos.TOP_LEFT);
            StackPane.setMargin(eventBox, new Insets(20, 5, 5, 5));
            dayPane.getChildren().add(eventBox);

            LocalDate finalDate = dateToRepresent;
            dayPane.setOnMouseClicked(_ -> {
                List<Event> events = eventManager.getEvents(finalDate);
                FormGUI.show(finalDate, events, primaryStage);
            });

            monthGrid.add(dayPane, col, row);

            col++;
            if (col > 6) {
                col = 0;
                row++;
            }

            dayCounter++;
        }
        // task button and task Button action logic
        Button addTaskButton = new Button("Add Task");
        addTaskButton.setOnAction(_ -> FormGUI.showAddTaskForm(eventManager, primaryStage, this::updateMonthView));

        HBox addTaskWrapper = new HBox(addTaskButton);
        addTaskWrapper.setAlignment(Pos.BOTTOM_RIGHT);
        addTaskWrapper.setPadding(new Insets(5));
        int buttonRow = rows + 2;
        monthGrid.add(addTaskWrapper, 6, buttonRow);
    }

        // starts the primary stage and implements hamburger button logic
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        hamburgerButton.setOnAction(_ -> {
            if (showingThreeDayView) {
                showMonthView();
            } else {
                ThreeDayView threeDayView = new ThreeDayView(eventManager, primaryStage, this::showMonthView);
                root.setCenter(threeDayView.createContent());
            }
            showingThreeDayView = !showingThreeDayView;
        });

        showMonthView();
        // creates month view scene
        Scene scene = new Scene(root, 1000, 800);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }
}