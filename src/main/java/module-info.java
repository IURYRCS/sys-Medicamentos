module org.sysmedicamentos {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.sysmedicamentos to javafx.fxml;
    exports org.sysmedicamentos;
}