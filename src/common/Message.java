package common;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message implements Serializable {
    public String message;
    public String senderUsername;
    public String receiverUsername;
    public String sendTime;
    public static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    public static Date date = new Date();

    public String setTime() {
        return dateFormat.format(date.getTime());
    }

    public Message(String message, String senderUsername, String receiverUsername) {
        this.message = message;
        this.senderUsername = senderUsername;
        this.receiverUsername = receiverUsername;
        this.sendTime = setTime();
    }

    @Override
    public String toString() {
        return message;
    }
}
