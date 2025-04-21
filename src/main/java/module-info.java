module org.example.todo {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.todo to javafx.fxml;
    exports org.example.todo;
}