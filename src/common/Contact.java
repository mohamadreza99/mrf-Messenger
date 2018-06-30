package common;

import javafx.scene.image.Image;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Contact implements Serializable {
    public String username;
    public String lastMessage;
    public transient Image image;
    public List<Message> messages = new ArrayList<>();

    public Contact(String username, Message lastMessage) {
        this.username = username;
        this.lastMessage = lastMessage.message;
        this.messages.add(lastMessage);
    }

    public Contact(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return username;
    }
}
