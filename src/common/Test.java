package common;

import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Test {
    public static String fxmlName;
    public FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlName));//common.Token constructor

//    ImageView imageView = new ImageView();
//    Image image = new Image("resources/002-telegram.png");
//    imageView.setImage(image);

//    sentCheck.setSelected(true);

    public static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    public static Date date = new Date();

    public static void main(String[] args) {
        System.out.println(dateFormat.format(date.getTime()));
    }
}
