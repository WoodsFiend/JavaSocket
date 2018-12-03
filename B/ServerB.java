// A Java program for a Server 
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.io.*;
import java.util.*;
import java.lang.*;
  
public class ServerB
{   
    // constructor with port 
    public static void main(String args[]) { 
    	int port = Integer.parseInt(args[0]); 
    	//initialize socket and input stream 
    	Socket          socket   = null; 
    	ServerSocket    server   = null; 
    	BufferedInputStream in   = null;
        BufferedOutputStream out = null;
        PrintWriter txtWrite = null;
        DataOutputStream endResult = null;

        byte[] packet = new byte[100];
        // starts server and waits for a connection 
        try
        { 
            server = new ServerSocket(port); 
            System.out.println("Server started"); 
  
            //loop to accept socket connections stays open
            while(true){
                System.out.println("\nWaiting for a client ..."); 
                socket = server.accept(); 
                System.out.println("Client accepted"); 
    
                // creates a buffered stream to and from client
                in = new BufferedInputStream(socket.getInputStream());
                out = new BufferedOutputStream(socket.getOutputStream());
                txtWrite = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                endResult = new DataOutputStream(socket.getOutputStream());

                //System.out.println("Streams created");
                
                //create and send ready
                String ready= "READY\n";
                //String message = new String(ready, StandardCharsets.UTF_8);
                System.out.println(ready);
                try{
                    txtWrite.write(ready);
                    txtWrite.flush();
                    //System.out.println("sent ready");
                }catch(Exception e){
                    //error
                    System.out.println("Ready not sent");
                }

                //gets operator
                byte[] operator = new byte[1];
                in.read(operator,0,1);
                //System.out.println("recieved operator " + (int)operator[0]);

                //gets number of nums in the stream
                byte[] numbernums = new byte[1];
                in.read(numbernums,0,1);
                //System.out.println("recieve numNums " + numbernums[0]);
                
                
                //addition
                if((int)operator[0] == 1){
                    int result = 0;
                    int index = 0;
                    int i = 0;
                    //loop to get byte, separate into 2 numbers and add
                    while(i < (numbernums[0])){
                        in.read(packet,index,1);
                        //System.out.println(packet[index]);
                        int first = (packet[index]&0xff) >> 4;
                        int second = packet[index] & 15;
                        //first byte
                        if(index == 0){
                            System.out.print(first + " + " + second);
                        }
                        //last byte with one number only
                        else if((numbernums[0]%2 != 0) && (i==numbernums[0]-1)){
                            System.out.print(" + " + first);
                        }
                        else{
                            System.out.print(" + " + first + " + " + second);
                        }
                        
                        result = result + first + second;
            
                        index++;
                        i = i + 2;

                    }

                    System.out.println("\nAnswer: " + result);
                    //pack result into 4 byte array
                    byte[] answer = new byte[4];
                    answer[0] = (byte) (result >> 24);
                    answer[1] = (byte) (result >> 16);
                    answer[2] = (byte) (result >> 8);
                    answer[3] = (byte) (result >> 0);
                    //send answer to client
                    out.write(answer);
                    out.flush();

                }

                //subtraction
                else if((int)operator[0] == 2){
                    int result = 0;
                    int index = 0;
                    int i = 0;
                    //loop to get byte, separate into 2 numbers and subtract
                    while(i < (numbernums[0])){
                        in.read(packet,index,1);
                        //System.out.println(packet[index]);
                        int first =(packet[index]&0xff) >> 4;
                        int second = packet[index] & 15;
                        //first byte
                        if(index == 0){
                            result = first - second;
                            System.out.print(first + " - " + second);
                        //all other bytes
                        }
                        //last byte with one number only
                        else if((numbernums[0]%2 != 0) && (i==numbernums[0]-1)){
                            result = result - first;
                            System.out.print(" - " + first);
                        }
                        else{
                            System.out.print(" - " + first + " - " + second);
                            result = (result - first) - second;
                            
                        }

                        index++;
                        i = i + 2;
                    }

                    System.out.println("\nAnswer: " + result);
                    //pack result into 4 byte array
                    byte[] answer = new byte[4];
                    answer[0] = (byte) (result >> 24);
                    answer[1] = (byte) (result >> 16);
                    answer[2] = (byte) (result >> 8);
                    answer[3] = (byte) (result >> 0);
                    //send answer to client
                    out.write(answer);
                    out.flush();
                }

                //multiplication
                else if((int)operator[0] == 4){
                    int result = 0;
                    int index = 0;
                    int i = 0;
                    //loop to get byte, separate into 2 numbers and multiply
                    while(i < (numbernums[0])){
                        in.read(packet,index,1);
                        //System.out.println(packet[index]);
                        int first = (packet[index]&0xff) >> 4;
                        int second = packet[index] & 15;
                        
                        //first byte
                        if(index == 0){
                            result = first * second;
                            System.out.print(first + " x " + second);
                        }
                        //last byte with one number only
                        else if((numbernums[0]%2 != 0) && (i==numbernums[0]-1)){
                            result = result * first;
                            System.out.print(" x " + first);
                        }
                        //all other bytes
                        else{
                            result = (result * first) * second;
                            System.out.print(" x " + first + " x " + second);
                        }
            
                        index++;
                        i = i + 2;

                    }
                    
                    System.out.println("\nAnswer: " + result);
                    //pack result into 4 byte array
                    byte[] answer = new byte[4];
                    answer[0] = (byte) (result >> 24);
                    answer[1] = (byte) (result >> 16);
                    answer[2] = (byte) (result >> 8);
                    answer[3] = (byte) (result >> 0);
                    //send answer to client
                    endResult.write(answer);
                    out.flush();
                }
                
                // close connection 
                in.close();
                out.close();
                socket.close();
            }
        } 
        catch(IOException i) 
        { 
            System.out.println(i); 
        } 
    } 
}