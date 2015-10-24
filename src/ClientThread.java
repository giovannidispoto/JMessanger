/**
 * Created by giovannidispoto on 21/10/15.
 * @author: Giovanni Dispoto
 * @description: Questo Thread si occupa di richevere tutti i messaggi dal client (Un thread per ogni connession) e di scrivere i messaggi
 * all'interno dell'database
 */
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class ClientThread extends Thread{

    private Socket socket;
    private String message;
    private BufferedReader in;
    private PrintWriter out;
    private Database db;
    private String regex = "^0003\\d{9}$";
    private JSONParser parser;
    private JSONArray jsonarray;
    private JSONObject obj;
    private Object objval;
    private ClientThreadOut threadOut;

    public ClientThread(Socket socket,Database db){
        this.socket = socket;
        this.db = db;
        try {
              in= new BufferedReader(new InputStreamReader(socket.getInputStream()));
              out = new PrintWriter(socket.getOutputStream());
        }catch(IOException e){
            e.printStackTrace();
        }
        parser = new JSONParser();
        jsonarray = new JSONArray();
        threadOut = new ClientThreadOut(socket,db);
        this.start();
    }

    public void run() {
        while (true) {
            try {
                message = in.readLine();
                message = message.trim();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String numberM = "";
            String numberD = "";
            String chatID = "";
            String body = "";

            if(message.substring(0,3).equals("conn")) {
                numberM = message.substring(5,message.length() - 1);
                db.setStatus(numberM,socket.getInetAddress().toString(),true);
                continue;
            }

            if(message.substring(0,3).equals("disc")) {
                numberM = message.substring(5,message.length() - 1);
                db.setStatus(numberM,false);
                continue;
            }

            try {
                objval = parser.parse(message);
                jsonarray.add(objval);
                obj = (JSONObject) jsonarray.get(0);
                System.out.println(jsonarray.get(0));

            } catch (ParseException e) {
                e.printStackTrace();
                continue;
            }


            numberM = (String) obj.get("numeroM");
            numberD = (String) obj.get("numeroD");
            chatID = (String) obj.get("chatID");
            body = (String) obj.get("body");

            System.out.println(numberM);
            System.out.println(numberD);
            System.out.println(chatID);
            System.out.println(body);

            try {
                if (!numberM.matches(regex) || !numberD.matches(regex)) throw new Exception("[-] Numero Mancante");

                if(db.getUserID(numberD).equals("null") || db.getUserID(numberM).equals("null")) throw new Exception("[-] Utente non esistente");

                db.insertMessage("INSERT INTO messaggi SET c_id = ?,u_mitt = ?, u_dest = ?, messaggio = ?,inviato = ?", Integer.parseInt(chatID),Integer.parseInt(db.getUserID(numberM)),Integer.parseInt(db.getUserID(numberD)), body);
                System.out.println("[" + chatID + "][" + numberM + "][" + numberD + "]" + body);
                jsonarray.remove(0);

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

}
