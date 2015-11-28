/**
 * Created by giovannidispoto on 21/08/15.
 * @author: Giovanni Dispoto
 * @description: Questo Thread si occupa di rievere le richieste di connessione e di creare un thread per ogni connessione richiesta
 *
 *
 *TODO: Controllare invio messaggi al client.
 *
 * {
 *     "numeroM":"00102902",
 *     "numeroD":"0929209",
 *     "chatID":"1",
 *     "body":"messaggio qui"
 * }
 *
 */
import java.io.IOException;
import java.net.*;



public class Server extends Thread{

    private int port;
    private Socket socket;
    private ServerSocket serverSocketIn;
    private Database db;
    private ClientThread clientThread;

    public Server(int port, Database db) {

        this.port = port;
        this.db = db;
        try {
            serverSocketIn = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("[+] Apertura server in ingresso effettuata con successo");

        this.start();

    }



    public void run() {

        while (true) {

            try {
                socket = serverSocketIn.accept();
                clientThread = new ClientThread(socket,db);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


}
