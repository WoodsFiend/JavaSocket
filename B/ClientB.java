import java.io.*;
import java.util.*;
import java.lang.*;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ClientB {
	public static void main(String[] args){
		String host = args[0];
		int port = Integer.parseInt(args[1]);
		int numberArgs = args.length - 3;
		String operator = args[2];
		byte[] packet = new byte[100];
		
		Socket socket = null;
		BufferedInputStream in = null;
        BufferedOutputStream out = null;
        BufferedReader txtRead = null;
        DataInputStream endResult = null;

		//get operator from arguements and assigns it to packet 0
		if(operator.contains("+")){
			packet[0] = 1;
		}
		else if(operator.contains("-")){
			packet[0] = 2;
		}
		else if(operator.contains("x")){
			packet[0] = 4;
		}
		//gets number of operands and assigns to packet 1
		packet[1] = (byte)numberArgs;
		


		//connect to server
		try{
			socket = new Socket(host, port);
			//System.out.println("Connected");
		}catch(UnknownHostException e){
			//exit here
			System.out.println("socket connection error: " + e);
		}catch(IOException e){
			//exit here
			System.out.println("socket connection error: " + e);
		}
		

		//create buffered stream in and out
		try{
			out = new BufferedOutputStream(socket.getOutputStream());
            in = new BufferedInputStream(socket.getInputStream());
            txtRead = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            endResult = new DataInputStream(socket.getInputStream());

        }catch(IOException e){
			//error
			System.out.println("did not create buffered streams");

		}


        //recieve ready from server
        String ready = "";
		try{
			ready = txtRead.readLine();
			//System.out.println(new String(ready, StandardCharsets.UTF_8));
		}catch(IOException e){
			//error
			System.out.println("did not recieve ready" + ready);
		}
		

		//loop to combine ints into byte array
		int i = 2;
		int i2 = 3;
		while (i2 != args.length){
			packet[i] = (byte)Integer.parseInt(args[i2]);
			i2++;
			//first number packed to left of byte
			packet[i] = (byte)(packet[i] << 4);

			try{
				//second number appended to first number
				packet[i] = (byte)(packet[i] | Integer.parseInt(args[i2]));
				
			}catch(ArrayIndexOutOfBoundsException e){
				//used to break out of loop when there is no more arguements to be sent
				break;
			}
			
			i2++;
			i++;

		}
		//send the packet to server
		try{
			out.write(packet);
			out.flush();
			//System.out.println("sent packet");
		}catch(IOException e){
			System.out.println("did not send a packet");
		}

		//recieve response and print answer
		byte[] answer = new byte[4];
		try{
			for(int x = 0;x<4;x++){
				endResult.read(answer,x,1);
				//System.out.println(answer[x]);
			}
			
		}catch(IOException e){
			System.out.println("could not get answer");
		}
		//bit shifting to combine into 4 byte answer
		System.out.println("Answer: " + (answer[0] << 24 | (answer[1] & 0xFF) << 16 | (answer[2] & 0xFF) << 8 | (answer[3] & 0xFF)) + "\n");

	}
	

}
