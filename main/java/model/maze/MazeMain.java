package model.maze;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MazeMain extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MazeMain.class.getResource("GUI.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 700);
        // Enter the absolute path of the image

        stage.setTitle("Maze2/3D");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}