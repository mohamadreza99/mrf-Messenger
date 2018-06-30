package severSide;

import common.Client;
import common.Contact;
import common.Message;

import java.util.concurrent.CopyOnWriteArrayList;

public class DataBase {
    private static DataBase ourInstance = new DataBase();

    public static DataBase getInstance() {
        return ourInstance;
    }

    private DataBase() {
        clients=new CopyOnWriteArrayList<>();
    }

    CopyOnWriteArrayList<Client> clients;

    public boolean isValid(String username) {
        for (Client client : clients) {
            if (client.username.equals(username)) {
                return false;
            }
        }
        return true;
    }

    public void signUp(Client client) {
        clients.add(client);
    }

    /**
     * return a client with username
     * if it doesn't exist return null
     * @param username
     * @return
     */
    public Client getClient(String username) {
        for (Client client : clients) {
            if (client.username.equals(username)) {
                return client;
            }
        }
        return null;
    }

    /**
     * give a message and add it to messages list of sender client for receiver contact and so on for receiver client
     * @param m
     */
    public void messageHandle(Message m) {
        boolean exist = false;
        for (Contact c : getClient(m.senderUsername).contacts) {
            if (c.username.equals(m.senderUsername)) {
                c.messages.add(m);
                exist = true;
            }
        }
        if (!exist) {
            getClient(m.senderUsername).contacts.add(new Contact(m.receiverUsername, m));
        }
        exist = false;
        for (Contact c : getClient(m.receiverUsername).contacts) {
            if (c.username.equals(m.senderUsername)) {
                c.messages.add(m);
                exist = true;
            }
        }
        if (!exist) {
            getClient(m.receiverUsername).contacts.add(new Contact(m.senderUsername, m));
        }

    }
}
