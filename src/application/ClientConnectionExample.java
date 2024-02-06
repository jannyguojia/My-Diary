package application;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientConnectionExample {
	int port=9090;
    //String serverAddress = "127.0.0.1";
	private  ServerSocket serverSocket ;
	private Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;

    public ClientConnectionExample(int port) {
        try {
            this.port = port;
        	serverSocket= new ServerSocket(port);	               
        	socket = serverSocket.accept();
            System.out.println("Client connected: " + socket.getInetAddress());

            this.dos = new DataOutputStream(socket.getOutputStream());
            this.dis = new DataInputStream(socket.getInputStream());
			System.out.println("ClientConnection success!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	public boolean sendData(String data) {
        try {
            dos.writeUTF(data);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String receiveData() {
        try {
            return dis.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void closeConnection() {
        try {
            if (socket != null) {
                socket.close();
            }
            if (dos != null) {
                dos.close();
            }
            if (dis != null) {
                dis.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public DataOutputStream getDos() {
		return dos;
	}

	public void setDos(DataOutputStream dos) {
		this.dos = dos;
	}

	public DataInputStream getDis() {
		return dis;
	}

	public void setDis(DataInputStream dis) {
		this.dis = dis;
	}


}
