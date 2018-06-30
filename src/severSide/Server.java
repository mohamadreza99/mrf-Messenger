package severSide;

import common.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    Dialog dialog;
    DataBase db = DataBase.getInstance();
    byte[] buffer = new byte[1024];
    ConcurrentHashMap<String, ObjectOutputStream> socketList = new ConcurrentHashMap<String, ObjectOutputStream>();

    public void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(8080);

            while (true) {
                Socket socket = serverSocket.accept();
                ServerThread thread = new ServerThread(socket);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class ServerThread extends Thread {
        Socket socket;
        ObjectOutputStream out;
        ObjectInputStream in;

        boolean breakOn;

        @Override
        public void run() {
            while (!breakOn) {
                try {
                    dialog = (Dialog) in.readObject();
                    System.err.println("hello");//log
                    switch (dialog.type) {
                        case checkUser: {
                            System.err.println("check user");//log
                            if (db.isValid(dialog.message)) {
                                out.writeObject(new Dialog(Type.valid));
                            } else
                                out.writeObject(new Dialog(Type.invalid));
                            break;
                        }
                        //----------------------------------------------------------------------------------------------------------------
                        case signup: {
                            System.out.println("signup");//log
                            db.signUp((Client) dialog.object);
                            ImageThread imageThread = new ImageThread((((Client) dialog.object).username));
                            imageThread.start();
                            break;
                        }
                        //------------------------------------------------------------------------------------------------------------
                        case login:{
                            Client client = db.getClient(dialog.message);
                            out.writeObject(new Dialog(Type.login, client));
                            break;
                        }
                        //================================================================================================================
                        case message:{
                            Message message = (Message) dialog.object;
                            db.messageHandle(message);
                            socketList.get(message.receiverUsername).writeObject(dialog);
                            break;
                        }
                        //==================================================================================================================
                        case search:{
                            Client client = (Client) dialog.object;
                            socketList.put(client.username, out);
                            System.out.println(this.socket);
                            Client c = db.getClient(dialog.message);

                            if (c == null) {
                                out.writeObject(new Dialog(Type.notExist));
                            } else {
                                out.writeObject(new Dialog(Type.exist, c.username));
                            }
                            break;
                        }
                    }


                } catch (IOException e) {
                    breakOn = true;
                    System.out.println("client was left please set his last seen with dialog details");
//                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        public ServerThread(Socket socket) throws IOException {
            this.socket = socket;
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        }
    }

    class ImageThread extends Thread {
        ServerSocket serverSocket = new ServerSocket(50);

        String fileName;

        @Override
        public void run() {
            try {
                Socket socket = serverSocket.accept();
                File file = Paths.get("src/severSide/images/" + fileName + ".png").toAbsolutePath().toFile();
                OutputStream o = new FileOutputStream(file);
                int read;
                DataInputStream in = new DataInputStream(socket.getInputStream());
                while ((read = in.read(buffer)) > 0) {
                    o.write(buffer, 0, read);
                }
            } catch (IOException e) {
//                File file = new File("images/" + fileName + ".png");
//                System.out.println(file.delete());
                e.printStackTrace();
            }
        }

        ImageThread(String username) throws IOException {
            this.fileName = username;
        }
    }


    public static void main(String[] args) {
        Server server = new Server();
        server.startServer();
    }
}
