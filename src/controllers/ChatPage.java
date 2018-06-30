package controllers;

import common.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ChatPage implements Initializable {
    public static Client loginData;
    public Client client;
    ObjectOutputStream out;
    ObjectInputStream in;
    Dialog dialog;
    Socket socket;
    Token inselect;

    public TextField searchField;
    public ListView<Token> tokenListView;
    public ListView<MessageToken> messagesListView;
    public ImageView attach;
    public ImageView send;
    public TextField messageField;

    public void clicked(MouseEvent event) {
        Token t = tokenListView.getSelectionModel().getSelectedItem();
        inselect = t;
        messagesListView.setItems(FXCollections.observableList(t.l));
        new Thread(() -> {
            messagesListView.setItems(inselect.messageTokens);
        }).start();
    }

    public void search(ActionEvent event) throws IOException {
        out.writeObject(new Dialog(Type.search, searchField.getText(), client));

    }

    public void send(MouseEvent event) throws IOException {
        System.out.println(messageField.getText());
        System.out.println(client.username);
        System.out.println(inselect.contact);
        Message message = new Message(messageField.getText(), client.username, inselect.contact.username);
        messageField.clear();
        MessageToken messageToken = new MessageToken(message.message, message.sendTime, message.senderUsername, message.receiverUsername);
        inselect.l.add(messageToken);
        out.writeObject(new Dialog(Type.message, message));
        messageToken.sentCheck.setSelected(true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        client = loginData;
        try {
            socket = new Socket("localhost", 8080);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(client);
        List<Token> temp = new ArrayList<>();
        if (client.contacts != null)
            for (Contact contact : client.contacts) {
                Token t = new Token();
                t.contact = contact;
                t.usernameLabel.setText(contact.username);
                t.lastMessage.setText(contact.lastMessage);
                File file = Paths.get("src/severSide/images/" + contact.username + ".png").toAbsolutePath().toFile();
                if (file.exists()) {
                    t.imageView.setImage(new Image((Paths.get("src/severSide/images/" + contact.username + ".png")).toUri().toString()));
                    //we can set an input stream with file above and set the image view too
                } else {
                    t.imageView.setImage(new Image(Paths.get("src/severSide/images/default.png").toUri().toString()));
                }
                temp.add(t);
            }

        tokenListView.setItems(FXCollections.observableList(temp));
        new Thread(() -> {
            boolean stop = false;
            while (!stop) {
                try {
                    dialog = (Dialog) in.readObject();
                    System.out.println(dialog.message);
                    if (dialog.type == Type.message) {
                        Message m = (Message) dialog.object;
                        System.out.println("ok");
                        System.out.println(m.message);
                        boolean exist = false;
                        for (Token token : tokenListView.getItems()) {
                            if (token.contact.username.equals(m.senderUsername)) {
                                inselect = token;
                                tokenListView.getSelectionModel().select(token);
                                Platform.runLater(() -> {
                                    token.messageTokens.add(new MessageToken(m.message, m.sendTime, m.senderUsername, m.receiverUsername));

                                });
                                exist = true;
                            }
                        }
                        if (!exist) {
                            Token t = new Token(new Contact(m.senderUsername, m));
                            Platform.runLater(() -> {
                                tokenListView.getItems().add(t);

                            });

                        }
                    }
                    if (dialog.type == Type.exist) {
                        Contact c = new Contact(dialog.message);
                        Token t = new Token(c);

                        Platform.runLater(() -> {
                            File file = Paths.get("src/severSide/images/" + c.username + ".png").toAbsolutePath().toFile();
                            if (file.exists()) {
                                t.imageView.setImage(new Image((Paths.get("src/severSide/images/" + c.username + ".png")).toUri().toString()));
                                //we can set an input stream with file above and set the image view too
                            } else {
                                t.imageView.setImage(new Image(Paths.get("src/severSide/images/default.png").toUri().toString()));
                            }
                            tokenListView.getItems().add(t);

                        });
                    }
                    if (dialog.type == Type.notExist) {
                        Platform.runLater(() -> {
                            new Alert(Alert.AlertType.ERROR, "No Match").showAndWait();

                        });
                    }
                } catch (SocketException e) {
                    stop = true;
                    Platform.runLater(() -> {
                        Stage stage = new Stage();
                        AnchorPane root = new AnchorPane();
                        HBox vBox = new HBox();
                        vBox.getChildren().add(new Label("reconnecting in:"));
                        vBox.getChildren().add(new Label("20 seconds"));
                        root.getChildren().add(vBox);
                        stage.setWidth(200);
                        stage.setHeight(150);
                        stage.setScene(new Scene(root));
                        stage.setTitle("server disconnected");
                        stage.show();
                    });
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void choosing(ActionEvent event) {
        Stage stage = new Stage();
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("choosing a file");
        File file = fileChooser.showOpenDialog(stage);
    }

    public void setting(MouseEvent mouseEvent) throws IOException {
        Stage stage = new Stage();
        stage.setTitle("setting");
        Setting.c = client;
        AnchorPane root = FXMLLoader.load(getClass().getResource("../gui/setting.fxml"));
        stage.setScene(new Scene(root));
        stage.show();
    }
}

//        File file = Paths.get("src/severSide/images/default.png").toAbsolutePath().toFile();
//        System.out.println(file.exists());
