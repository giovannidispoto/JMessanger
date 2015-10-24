/**
 * Created by giovannidispoto on 24/10/15.
 * @author: Giovanni Dispoto
 * @description: Questo thread di occupa di inviare i messaggi agli utenti attivi.
 */

import org.json.simple.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;


public class ClientThreadOut extends Thread{

    private int port;
    private Database db;
    private Socket socket;
    private  ArrayList<ArrayList> result;
    private ArrayList<ArrayList> messages;
    private JSONObject jsonObj;


    public ClientThreadOut(Socket socket, Database db){
        this.socket = socket;
        this.db = db;
        messages  = new ArrayList<ArrayList>();
        this.start();
    }

    public void run(){

        while(true){
            result = db.getActiveIP();
            ArrayList<String> id  = result.get(0);
            ArrayList<String> ip = result.get(1);
            ArrayList<String> number = result.get(2);

            messages = null;
            for(int i = 0; i < id.size(); i++){
                //System.out.println("Id active: " + id.get(i)+", IP:"+ip.get(i));
                messages = db.getMessages(number.get(i));

                ArrayList<Integer> messages_id = messages.get(0); //id destinazione
                ArrayList<String> messages_mess = messages.get(1);
                ArrayList<String> message_mitt = messages.get(2);
                if(messages_id.size() == 0) break;
                send(messages_id, messages_mess, message_mitt,ip.get(i));
            }
        }

    }

    public void send(ArrayList<Integer> messages_id, ArrayList<String> messages_mess,ArrayList<String> message_mitt, String ip) {

        int id;
        String message_body,message,message_mit;
        try {
            InetAddress clientIP = InetAddress.getByName(ip);


            for(int i = 0; i < messages.size(); i++) {

                id = messages_id.get(i);
                jsonObj = new JSONObject();
                message_body = messages_mess.get(i);
                message_mit = message_mitt.get(i);
                jsonObj.put("message_sender",message_mit);
                jsonObj.put("message",message_body);
                socket = new Socket(clientIP,port);
                DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());
                outToClient.writeBytes(jsonObj.toString());
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
