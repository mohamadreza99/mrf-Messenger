package common;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {
    public static Stage stage;

    @Override
    public void stop() throws Exception {
        //here we must send a dialog to server to set lastSeen
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        AnchorPane root = FXMLLoader.load(getClass().getResource("/gui/login.fxml"));
        primaryStage.setTitle("Beheshti messenger");
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("../resources/002-telegram.png")));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        stage = primaryStage;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
