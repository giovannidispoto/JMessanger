import java.io.IOException;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.net.Socket;

class TestingTCP{

    private static Scanner scSocket;
    private static Scanner scTast = new Scanner(System.in);
    private static PrintWriter print;


    public static void main(String[] args){
        ReceiveTCP rcv;
        String address = args[0];
        int port = Integer.parseInt(args[1]);
        String input;


        try {
            Socket socket = new Socket(address, port);
            print = new PrintWriter(socket.getOutputStream(),true);
            scSocket = new Scanner(socket.getInputStream());
        }catch(IOException e){
            e.printStackTrace();
        }
        rcv = new ReceiveTCP();
        while (true) {


                if (scTast.hasNext()){
                    input = scTast.nextLine();
                    print.println(input);
                }
        }

    }
private static class ReceiveTCP extends Thread{
    public ReceiveTCP(){
        this.start();
    }
      public void run(){
        if (scSocket.hasNextLine()) {
                    System.out.println("From Server: "+scSocket.nextLine());
                }
    }
}

}