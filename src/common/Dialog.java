package common;

import java.io.Serializable;

public class Dialog implements Serializable {

    public Type type;
    public String message;
    public Serializable object;

    public Dialog(Type type, String message, Serializable object) {
        this.type = type;
        this.message = message;
        this.object = object;
    }

    public Dialog(Type type, Serializable object) {
        this.type = type;
        this.object = object;
    }

    public Dialog(Type type, String message) {
        this.type = type;
        this.message = message;
    }

    public Dialog(Type type) {

        this.type = type;
    }
}
