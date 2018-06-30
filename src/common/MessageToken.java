package common;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MessageToken extends HBox {
//    public ImageView image;

    public Label message;

    public Label sentTime;

    public CheckBox sentCheck;

    public CheckBox readCheck;

    public String senderUsername;
    public String receiverUsername;

    public MessageToken() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "../gui/messageToken.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

//        sentTime.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
//        sentTime.setText(new SimpleDateFormat("HH:mm:ss").format(new Date().getTime()));
    }

    public MessageToken(String message, String sentTime, String senderUsername, String receiverUsername) {
        this();
        this.message.setText(message);
        this.sentTime.setText(sentTime);
        this.senderUsername = senderUsername;
        this.receiverUsername = receiverUsername;
    }
}
