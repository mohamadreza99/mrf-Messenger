package common;

import common.MessageToken;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class Token extends VBox implements Initializable {
    public ImageView imageView;
    public Label usernameLabel;
    public Label lastMessage;
    public Label unread;
    public ImageView threeDots;
    public Line selected;
    public VBox component;


    public boolean inSelect;
    public boolean firsttime;
    public Contact contact;
    public List<MessageToken> l = new ArrayList<>();
    public ObservableList<MessageToken> messageTokens = FXCollections.observableList(l);


    public Token() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "../gui/token.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
//        imageView.setOnMouseClicked((EventHandler -> {
//            System.out.println("hello");
//        }));
//        component.setOnMouseClicked(event -> {
//
//        });

    }

    public Token(Contact contact) {
        this();
        this.usernameLabel.setText(contact.username);
        this.lastMessage.setText(contact.lastMessage);
        this.contact = contact;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (contact != null)
            if (contact.messages != null) {
                for (Message message : contact.messages) {
                    MessageToken messageToken = new MessageToken();
                    messageToken.message.setText(message.message);
                    messageToken.sentTime.setText(message.sendTime);
                    l.add(messageToken);
                }
            }
    }
}
