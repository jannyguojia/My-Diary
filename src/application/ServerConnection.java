package application;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;


//把客户端连接到服务器
public class ServerConnection {
	       String serverAddress="127.0.0.1";//
	       int port= 8080;//
	        private Socket socket;

	        public ServerConnection(String serverAddress, int port) {
	            this.serverAddress = serverAddress;
	            this.port = port;
	        }
	        
	        public void connect() throws IOException {
	            socket = new Socket(serverAddress, port);
	            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                System.out.println("ServerConnection success!");
	        }

	        public Socket getSocket() {
	            return socket;
	        }

	        public void close() throws IOException {
	            if (socket != null && !socket.isClosed()) {
	                socket.close();
	            }
	        }
            
	    
}
