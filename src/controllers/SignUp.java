package controllers;

import common.Client;
import common.Dialog;
import common.Main;
import common.Type;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SignUp implements Initializable {
    public TextField username;
    public TextField password;
    public ImageView imageview;
    ObjectOutputStream out;
    ObjectInputStream in;
    Dialog dialog;
    boolean validUser = false;
    Socket socket;
    FileInputStream i;
    boolean hasImage;


    public void check(ActionEvent event) throws IOException, ClassNotFoundException {
        validUser = false;
        out.writeObject(new Dialog(Type.checkUser, username.getText()));
        dialog = (Dialog) in.readObject();
        if (dialog.type == Type.invalid) {
            new Alert(Alert.AlertType.ERROR, "try another username").showAndWait();
        } else {
            new Alert(Alert.AlertType.INFORMATION, "valid username").showAndWait();
            validUser = true;
        }
    }

    public void drop(DragEvent dragEvent) {
        try {
            Dragboard board = dragEvent.getDragboard();
            List<File> phil = board.getFiles();
            FileInputStream fis;
            fis = new FileInputStream(phil.get(0));
            i = new FileInputStream(phil.get(0));
            Image image = new Image(fis);
            imageview.setImage(image);
            hasImage = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void dragOver(DragEvent dragEvent) {
        Dragboard board = dragEvent.getDragboard();
        if (board.hasFiles()) {
            dragEvent.acceptTransferModes(TransferMode.ANY);
        }
    }

    public void chooser(ActionEvent event) {
    }

    public void signUp(ActionEvent event) throws IOException {
        if (validUser) {
            Client client = new Client();
            client.username = username.getText();
            client.password = password.getText();
            client.image = imageview.getImage();
            out.writeObject(new Dialog(Type.signup, client));
            if (hasImage) {
                new Thread(() -> {
                    try {
                        Socket imageSocket = new Socket("localhost", 50);
                        DataOutputStream aksSender = new DataOutputStream(imageSocket.getOutputStream());
                        byte[] buffer = new byte[1024];
                        int read;
                        while ((read = i.read(buffer)) > 0) {
                            aksSender.write(buffer, 0, read);
                        }
                        imageSocket.close();
                        i.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
            AnchorPane root = FXMLLoader.load(getClass().getResource("/gui/login.fxml"));
            Main.stage.setScene(new Scene(root));
            socket.close();
        } else {
            new Alert(Alert.AlertType.ERROR, "choose a valid username").showAndWait();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            socket = new Socket("localhost", 8080);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
