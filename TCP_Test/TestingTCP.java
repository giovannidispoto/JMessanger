import java.io.IOException;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.net.Socket;

class TestingTCP{

    protected static Scanner scSocket;
    protected static Scanner scTast = new Scanner(System.in);
    protected static PrintWriter print;
    protected static String address;


    public TestingTCP(int port){

        Socket socket;

         try {
            socket = new Socket(address, port);
            print = new PrintWriter(socket.getOutputStream(),true);
            scSocket = new Scanner(socket.getInputStream());
        }catch(IOException e){
            e.printStackTrace();
        }
    }


public static void main(String[] args){
         String input;
         address = args[0];
         int port = Integer.parseInt(args[1]);
          new TestingTCP(port);
        ReceiveTCP rcv = new ReceiveTCP();
         while (true) {

                    input = scTast.nextLine();
                    print.println(input);

        }

    }

private static class ReceiveTCP extends Thread{
    public ReceiveTCP(){
        this.start();
    }
      public void run(){
        while(true){

                    System.out.println("From Server: "+scSocket.nextLine());

    }
 }

}
}