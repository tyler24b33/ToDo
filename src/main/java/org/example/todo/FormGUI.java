package org.example.todo;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class FormGUI {

    //Show all events on a given date
    public static void show(LocalDate date, List<Event> events, Stage owner) {
        Stage popup = new Stage();
        popup.setTitle("Events on " + date.format(DateTimeFormatter.ofPattern("MMMM d, yyyy")));

        VBox content = new VBox(10);
        content.setPadding(new Insets(20));
        content.setAlignment(Pos.TOP_LEFT);
        // event null checking
        if (events == null || events.isEmpty()) {
            content.getChildren().add(new Label("No events."));
            //event box for loop logic
        } else {
            for (Event event : events) {
                VBox eventBox = new VBox(2);
                eventBox.setPadding(new Insets(10));
                eventBox.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-background-color: #f4f4f4;");
                eventBox.setSpacing(3);

                Label title = new Label("Title: " + event.getTitle());
                Label time = new Label("Time: " + (event.getTime() != null ? event.getTime().toString() : "N/A"));

                eventBox.getChildren().addAll(title, time);

                //Make event box clickable to open full details
                eventBox.setOnMouseClicked(_ -> {
                    Stage detailStage = new Stage();
                    detailStage.setTitle("Event Details");

                    VBox detailLayout = new VBox(10);
                    detailLayout.setPadding(new Insets(15));
                    detailLayout.setAlignment(Pos.CENTER_LEFT);

                    detailLayout.getChildren().addAll(
                            new Label("Title: " + event.getTitle()),
                            new Label("Time: " + (event.getTime() != null ? event.getTime().toString() : "N/A")),
                            new Label("Date: " + date),
                            new Label("Reminder: " + (event.getReminder() != null ? event.getReminder().toString() : "None")),
                            new Label("Description: " + event.getDescription())
                    );
                    // detail scene layout properties
                    Scene detailScene = new Scene(detailLayout, 300, 200);
                    detailStage.setScene(detailScene);
                    detailStage.initOwner(owner);
                    detailStage.initModality(Modality.WINDOW_MODAL);
                    detailStage.show();
                });

                content.getChildren().add(eventBox);
            }
        }
        // close button
        Button closeButton = new Button("Close");
        closeButton.setOnAction(_ -> popup.close());
        content.getChildren().add(closeButton);
        // content scene
        Scene scene = new Scene(content, 300, 300);
        popup.setScene(scene);
        popup.initOwner(owner);
        popup.initModality(Modality.WINDOW_MODAL);
        popup.show();
    }

    // Show Add Task form method
    public static void showAddTaskForm(EventManager eventManager, Stage owner, Runnable onSaveCallback) {
        Stage popup = new Stage();
        popup.setTitle("Add Task");

        VBox form = new VBox(10);
        form.setPadding(new Insets(20));
        form.setAlignment(Pos.CENTER_LEFT);

        Label nameLabel = new Label("Task Name:");
        TextField nameField = new TextField();

        Label descLabel = new Label("Description:");
        TextArea descArea = new TextArea();
        descArea.setPrefRowCount(3);

        Label dateLabel = new Label("Date:");
        DatePicker datePicker = new DatePicker(LocalDate.now());

        Label timeLabel = new Label("Time (HH:mm): 24HR-format");
        TextField timeField = new TextField();
        timeField.setPromptText("e.g. 09:00");

        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");

        HBox buttons = new HBox(10, saveButton, cancelButton);
        buttons.setAlignment(Pos.CENTER_RIGHT);

        form.getChildren().addAll(
                nameLabel, nameField,
                descLabel, descArea,
                dateLabel, datePicker,
                timeLabel, timeField,
                buttons
        );
        // save button saves event details
        saveButton.setOnAction(_ -> {
            String title = nameField.getText().trim();
            LocalDate date = datePicker.getValue();
            String timeStr = timeField.getText().trim();
            String description = descArea.getText().trim();

            if (title.isEmpty()) {
                showAlert("Event title is required.");
                return;
            }
            //gets local time
            LocalTime time;
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                time = LocalTime.parse(timeStr, formatter);
            } catch (DateTimeParseException ex) {
                showAlert("Invalid time format. Use HH:mm (e.g. 14:30).");
                return;
            }

            Event newEvent = new Event(title, date, time, description);
            eventManager.addEvent(newEvent);

            popup.close();
            onSaveCallback.run(); // update UI
        });
        // button to close window
        cancelButton.setOnAction(_ -> popup.close());

        Scene scene = new Scene(form, 300, 300);
        popup.setScene(scene);
        popup.initOwner(owner);
        popup.initModality(Modality.WINDOW_MODAL);
        popup.show();
    }
    // show Alert Method for Invalid
    private static void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING, message, ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
