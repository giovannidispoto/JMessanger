/**
 * Created by giovannidispoto on 21/10/15.
 * @author: Giovanni Dispoto
 * @description: Questo Thread si occupa di richevere tutti i messaggi dal client (Un thread per ogni connession) e di scrivere i messaggi
 * all'interno dell'database
 */
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ClientThread extends Thread{

    private Socket socket;
    private String message;
    private Scanner in;
    private PrintWriter out;
    private Database db;
    private String regex = "^0003\\d{9}$";
    private JSONParser parser;
    private JSONArray jsonarray;
    private JSONObject obj;
    private Object objval;
    private ClientThreadOut threadOut;
    private boolean stop = false;

    public ClientThread(Socket socket,Database db){
        this.socket = socket;
        this.db = db;
        try {
              in= new Scanner(socket.getInputStream());
              out = new PrintWriter(socket.getOutputStream());
        }catch(IOException e){
            e.printStackTrace();
        }
        parser = new JSONParser();
        jsonarray = new JSONArray();

        this.start();
    }

    public void run() {
        while (!stop) {
            try {
                socket.setSoTimeout(60000);
                message = in.nextLine();
            }catch(NoSuchElementException e){
                System.out.println("Chiusura Socket input");
                stop = true;
                continue;
            }catch(SocketException e){
                System.out.println("Chiusura Socket input");
                stop = true;
                continue;
            }

            String numberM = "";
            String numberD = "";
            String chatID = "";
            String body = "";



            try {
                objval = parser.parse(message);
                jsonarray.add(objval);
                obj = (JSONObject) jsonarray.get(0);
                // System.out.println(jsonarray.get(0));

            } catch (ParseException e) {
                e.printStackTrace();
                continue;
            }

            switch((String) obj.get("action")){
                case "message":
                    numberM = (String) obj.get("numeroM");
                    numberD = (String) obj.get("numeroD");
                    chatID = (String) obj.get("chatID");
                    body = (String) obj.get("body");

                    /*System.out.println(numberM);
                    System.out.println(numberD);
                    System.out.println(chatID);
                    System.out.println(body);*/

                    try {
                        if (!numberM.matches(regex) || !numberD.matches(regex)) throw new Exception("[-] Numero Mancante");

                        if(db.getUserID(numberD).equals("null") || db.getUserID(numberM).equals("null")) throw new Exception("[-] Utente non esistente");

                        db.insertMessage("INSERT INTO messaggi SET c_id = ?,u_mitt = ?, u_dest = ?, messaggio = ?,inviato = ?", Integer.parseInt(chatID),Integer.parseInt(db.getUserID(numberM)),Integer.parseInt(db.getUserID(numberD)), body);
                        System.out.println("[" + chatID + "][" + numberM + "][" + numberD + "]" + body);
                        jsonarray.remove(0);


                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                            break;
                case "status":
                        String number = (String) obj.get("numero");
                        String status = (String) obj.get("stato");
                    try {
                        if (!number.matches(regex) || !number.matches(regex)) throw new Exception("[-] Numero Mancante");

                        if(db.getUserID(number).equals("null")) throw new Exception("[-] Utente non esistente");

                        if(status.equalsIgnoreCase("Connect")) db.setStatus(number, socket.getInetAddress().toString(),true);
                        else db.setStatus(number,false);

                        System.out.println("["+number+"]"+"Status:"+status);
                        jsonarray.remove(0);


                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    if(status.equalsIgnoreCase("Connect")) threadOut = new ClientThreadOut(socket,db,number);
                    else if(status.equalsIgnoreCase("Disconnect")){
                        threadOut.requestStop();
                        stop = true;
                    }
                            break;
                case "getPhoto":
                    String numbers = (String) obj.get("numeri");
                    String numbers_array[] = numbers.split(";");

                    boolean b = threadOut.sendPhoto(numbers_array);

                    System.out.println(b);

                    break;
            }

        }
    }

}
