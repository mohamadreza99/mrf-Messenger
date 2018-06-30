package common;

import javafx.scene.image.Image;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Client implements Serializable {
    public String username;
    public String password;
    public String lastSeen;
    public transient Image image;
    public List<Contact> contacts = new ArrayList<>();
    public static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    public static Date date = new Date();

    public String bio;


    @Override
    public String toString() {
        return username;
    }
}
