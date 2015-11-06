/**
 * Created by giovannidispoto on 22/08/15.
 * @author: Giovanni Dispoto
 * @description: Questa classe si occupa di creare utenti, messaggi e tutto ciò che riguarda la gestione del database
 *
 * TODO: Iniziare a progettare ricezione propic e controllo numeri contatti se presenti nell'app
 * TODO: Vedere invio foto tramite pacchetto HTTP
 * TODO: Scrivere script livello server PHP
 */
import sun.misc.IOUtils;
import sun.nio.ch.IOUtil;

import java.io.*;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Database {

    private String location;
    private String type;
    private String username;
    private String password;
    private String dbName;
    private String port;
    private Connection conn;
    private Statement stm;
    private ResultSet rs;
    private PreparedStatement stmt;

    public Database(String location,String port,String dbName,String type,String username,String password){
        this.location = location;
        this.type = type;
        this.username = username;
        this.password = password;
        this.dbName = dbName;
        this.port = port;
    }

    public boolean getConnection(){
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String connectionUrl = "jdbc:"+type+"://"+location+":"+port+"/"+dbName + "?useUnicode=true&characterEncoding=UTF-8";
            conn = DriverManager.getConnection(connectionUrl, username, password);
        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }catch(ClassNotFoundException e){
            e.printStackTrace();
            return false;
        }catch(InstantiationException e){
            e.printStackTrace();
            return false;
        }catch (IllegalAccessException e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    synchronized boolean insertMessage(String sql,int c_id,int u_mitt,int u_dest, String message){
        try{
            //stm = conn.createStatement();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1,c_id);
            stmt.setInt(2,u_mitt);
            stmt.setInt(3,u_dest);
            stmt.setString(4,message);
            stmt.setBoolean(5, false);
            stmt.executeUpdate();

        }catch(SQLException e){
            e.getStackTrace();
            return false;
        }
        return true;
    }



    public void disconnect(){
        try {
            conn.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }



    synchronized String getUserID(String number){
        try {
            stmt = conn.prepareStatement("SELECT id FROM users WHERE numero = ?");
            stmt.setString(1,number);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                return rs.getString("id");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return "null";
    }

    synchronized void setStatus(String number,String ipAddress,boolean status){
        try {
            stmt = conn.prepareStatement("UPDATE users SET attivo = ?,ip = ? WHERE numero = ?");
            stmt.setBoolean(1, status);
            stmt.setString(2, ipAddress);
            stmt.setString(3,number);
            stmt.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    synchronized void setStatus(String number,boolean status){
        try {
            stmt = conn.prepareStatement("UPDATE users SET attivo = ? WHERE numero = ?");
            stmt.setInt(1, (status)? 1 : 0);
            stmt.setString(2,number);
            stmt.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    synchronized ArrayList getActiveIP(){
        ArrayList<ArrayList> result = new ArrayList<ArrayList>();
        ArrayList<String> id = new ArrayList<String>(); //id
        ArrayList<String> ip =  new ArrayList<String>(); //ip
        ArrayList<String> number = new ArrayList<>();
        try{
            stmt = conn.prepareStatement("SELECT id,ip,numero FROM users WHERE attivo = true");
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                id.add(rs.getString("id"));
                ip.add(rs.getString("ip"));
                number.add(rs.getString("numero"));
            }

        }catch(SQLException e){
            e.printStackTrace();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        result.add(0, id);
        result.add(1,ip);
        result.add(2,number);
        return result;
    }

    synchronized String getIP(String number){
        try {
            stmt = conn.prepareStatement("SELECT ip FROM users WHERE numero = ?");
            stmt.setString(1,number);
            rs = stmt.executeQuery();

            while(rs.next()) {
                    return rs.getString("ip");
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }

        return "Error";

    }


    synchronized ArrayList<ArrayList> getMessages(String number) {
        int id = 0;
        //ArrayList<Integer> messages_dest= new ArrayList<Integer>();
        ArrayList<String> messages_mess= new ArrayList<String>();
        ArrayList<ArrayList> messages = new ArrayList<ArrayList>();
        ArrayList<Integer> messages_mitt = new ArrayList<>();

        id = Integer.parseInt(getUserID(number));
        try{
            stmt = conn.prepareStatement("SELECT * FROM messaggi WHERE u_dest = ?");
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            while (rs.next()) {
                //messages_dest.add(rs.getInt("u_dest"));
                messages_mess.add(rs.getString("messaggio"));
                messages_mitt.add(rs.getInt("u_mitt"));
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }

        //messages.add(messages_dest);
        messages.add(messages_mess);
        messages.add(messages_mitt);

        return messages;

    }

    public byte[] getPhoto(String number){
       Blob b = null; InputStream image = null;byte[] photo;
        System.out.println(Integer.parseInt(getUserID(number)));
        try{
            stmt = conn.prepareStatement("SELECT photo FROM users WHERE id = ?");
            stmt.setInt(1,Integer.parseInt(getUserID(number)));

            rs = stmt.executeQuery();
            while (rs.next()) {
               image  = rs.getBinaryStream(1);
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            byte buffer[] = new byte[3000000];
            int read = 0;
            try{
            OutputStream fout =new FileOutputStream("/Users/giovannidispoto/Desktop/photo.jpg");

                while((read = image.read(buffer,0,buffer.length)) != -1){
                    fout.write(buffer);
                }


            fout.close();
            }catch(IOException e){
                e.printStackTrace();
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
    }
        return null;
    }

}



