package org.sysmedicamentos;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.sysmedicamentos.utils.PathFXML;

import java.io.FileInputStream;
import java.io.IOException;

public class MainApp extends Application {


    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(new FileInputStream(PathFXML.pathBase() + "\\MainView.fxml"));
        Scene scene = new Scene(root, 950, 600);
        stage.setTitle("Medicamentos");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {launch();
    }
}