package controllers;

import common.Client;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class Setting implements Initializable {
    public static Client c;
    Client client;

    public Label username;
    public Label bio;
    public ImageView im;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        client = c;
        username.setText(client.username);
        bio.setText(client.bio);

    }

    public void drop(DragEvent dragEvent) {
        try {
            Dragboard board = dragEvent.getDragboard();
            List<File> phil = board.getFiles();
            FileInputStream fis;
            fis = new FileInputStream(phil.get(0));
            Image image = new Image(fis);
            im.setImage(image);
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

    public void setBio(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("set bio");
        dialog.setHeaderText("set your bio here");
        Optional<String> result = dialog.showAndWait();
        bio.setText(result.get());
        client.bio = result.get();
    }

    public void changePassword(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("set password");
        dialog.setHeaderText("change your password here");
        Optional<String> result = dialog.showAndWait();
    }
}
