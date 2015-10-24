/**
 * @author: Giovanni Dispoto
 */
public class Main{


    public static void main(String args[]){

        //int port = Integer.parseInt(args[0]);
        int port = 4101;
        System.out.println("[+] Attivo server sulla porta "+ port);
        Database db = new Database("localhost","3306","JMessanger","mysql","JMAdmin","admin");
        db.getConnection();
        System.out.println("[+] Connesso al Database");
        Server server = new Server(port,db);
    }
}
