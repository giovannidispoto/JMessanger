/**
 * Created by giovannidispoto on 24/10/15.
 * @author: Giovanni Dispoto
 * @description: Questo thread di occupa di inviare i messaggi agli utenti attivi.
 */

import org.json.simple.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;


public class ClientThreadOut extends Thread{

    private int port;
    private boolean stop = false;
    private Database db;
    private String ip,number;
    private Socket socket;
    private ArrayList<ArrayList> result;
    private PrintWriter out;
    private ArrayList<ArrayList> messages;
    private JSONObject jsonObj;


    public ClientThreadOut(Socket socket, Database db,String number){
        this.socket = socket;
        this.db = db;
        this.ip = socket.getInetAddress().toString();
        this.number = number;
        messages  = new ArrayList<ArrayList>();
        try {
            out = new PrintWriter(socket.getOutputStream());
        }catch(IOException e){
            e.printStackTrace();
        }
        this.start();
    }

    public void run(){
        System.out.println("[+] Thread in output avviato");

        while(!stop){

                messages = null;
                messages = db.getMessages(number);

            //ArrayList<Integer> messages_id = messages.get(0); //id destinazione
            ArrayList<String> messages_mess = messages.get(0);
            ArrayList<String> messages_mitt = messages.get(1);
            System.out.println(messages_mess+"\n"+messages_mitt);
            send( messages_mess, messages_mitt, "localhost");

           // }
        }

    }

    public void requestStop(){
        stop = true;
    }

    public void send( ArrayList<String> messages_mess,ArrayList<String> messages_mitt, String ip) {

        int id;
        String message_body,message,message_mit;



        for(int i = 0; i < messages_mess.size(); i++) {

               // id = messages_id.get(i);
                jsonObj = new JSONObject();
                message_body = messages_mess.get(i);
                message_mit = String.valueOf(messages_mitt.get(i));
                jsonObj.put("message_sender",message_mit);
                jsonObj.put("message",message_body);
                out.write(jsonObj.toString());
            }

    }

    public boolean sendPhoto(String numbers[]){
        for(int i = 0;i< numbers.length;i++){
            JSONObject jsonObja = new JSONObject();
            jsonObja.put("numberPhotoProfile",numbers[i]);
            jsonObja.put("photo",db.getPhotoFile(numbers[i]));
            System.out.println(jsonObja.toJSONString());
            return true;

        }

        return false;
    }
}
