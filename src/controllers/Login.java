package controllers;

import common.Client;
import common.Dialog;
import common.Main;
import common.Type;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Login {
    public TextField username;
    public PasswordField password;

    public void login(ActionEvent event) throws IOException, ClassNotFoundException {
        Socket socket = new Socket("localhost", 8080);
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        out.writeObject(new Dialog(Type.login, username.getText()));
        Dialog dialog = (Dialog) in.readObject();
        Client client = (Client) dialog.object;
        if (client == null) {
            new Alert(Alert.AlertType.ERROR, "there isn't such username").showAndWait();
        } else {
            ChatPage.loginData = client;
            AnchorPane root = FXMLLoader.load(getClass().getResource("/gui/fx.fxml"));
            Main.stage.setScene(new Scene(root));
            socket.close();
        }
    }

    public void signup(ActionEvent event) throws IOException {
        AnchorPane root = FXMLLoader.load(getClass().getResource("/gui/signUp.fxml"));
        Main.stage.setScene(new Scene(root));
    }
}
