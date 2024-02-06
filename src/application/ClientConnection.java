package application;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
/*
 * It is server！！
 * 
 */
public class ClientConnection{
	 public static void main(String[] args) throws SQLException, IOException {
	        int port = 9090;
	        ServerSocket serverSocket = null;

	        try {
	           serverSocket = new ServerSocket(port);
	            System.out.println("Server is listening on port " + port);
	            
	        while (true) {
	                Socket socket = serverSocket.accept();
	                System.out.println("Client connected: " + socket.getInetAddress());

	                // Receive username and password from client
	                DataInputStream dis = new DataInputStream(socket.getInputStream());
	                String isRegistered = dis.readUTF();
	                
	                if (isRegistered.equals("no")) {
	                String CaesarEncryptdiaryname = dis.readUTF(); 
	                String CaesarEncryptname = dis.readUTF();	                
	                String CaesarEncryptemail = dis.readUTF();
	                String CaesarEncryptmd5password = dis.readUTF();

	                String DiaryName = CaesaDecrypt(CaesarEncryptdiaryname, 3);
	                String UserName = CaesaDecrypt(CaesarEncryptname, 3);
	                String Email = CaesaDecrypt(CaesarEncryptemail, 3);
	                String Password = CaesaDecrypt(CaesarEncryptmd5password, 3);        
	                //System.out.println("name"+DiaryName+"UserName"+UserName+"email:"+Email+"password"+Password);
	                    // Store registration information in the database
	                    Connection connection = DriverManager.getConnection("jdbc:mysql://54.196.19.67:3306/test", "root", "NtoAWfub3pS6Gs0wlitp");
	                    int result = saveToDatabase(connection, DiaryName, UserName, Email, Password);
	                 
	                    	String response = "Register successful";
	    	                socket.getOutputStream().write(response.getBytes());
	                
	                }else if(isRegistered.equals("yes")){
	                	  String CaesarEncryptname2 = dis.readUTF();	                
		                    String CaesarEncryptemail2 = dis.readUTF();
		                    String CaesarEncryptmd5password2 = dis.readUTF();
		                    //CaesaDecrypt
		                    String UserName2 = CaesaDecrypt(CaesarEncryptname2, 3);
		                    String Email2 = CaesaDecrypt(CaesarEncryptemail2, 3);
		                    String Password2 = CaesaDecrypt(CaesarEncryptmd5password2, 3);       
		                    // Send a response to the client based on the verification result. If the verification is successful, "Login successful" will be sent, otherwise "Login failed" will be sent.
		                    boolean isVerified = performVerification(UserName2, Email2, Password2);
		                    
		                    String response = isVerified ? "Login successful" : "Login failed";

		                    socket.getOutputStream().write(response.getBytes());
	                }else if(isRegistered.equals("one")){
	                	 String Email= dis.readUTF();
	                	 String Color = dis.readUTF();
	                	 String Encryption = dis.readUTF();
	                	 String Date = dis.readUTF();
	                	 String EncryptDiary = dis.readUTF();
	                	 String KeyName = dis.readUTF();
	                	 boolean issaved =saveToDiaryinfo(Email,Color, Encryption, Date,EncryptDiary,KeyName);

//	                	 int ID = 0 ;
//	   	        	  try (Connection connection = DriverManager.getConnection("jdbc:mysql://54.196.19.67:3306/test", "root", "NtoAWfub3pS6Gs0wlitp") ){
//	   	   			   
//	   	        	   String sql = "SELECT ID FROM accountinfo WHERE Email = ?";
//	   	   			   PreparedStatement preparedStatement = connection.prepareStatement(sql);
//	   	   	           preparedStatement.setString(1, Email);
//	   	   	           ResultSet resultSet = preparedStatement.executeQuery(); 
//	   	   	           if (resultSet.next()) {
//	   	   	             ID = resultSet.getInt("ID");
//	   	   	           }
//	   	            String sql2 = "SELECT Color FROM diaryinfo WHERE ID = ?ORDER BY Date DESC LIMIT 1";
//	   	         PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);
//	             preparedStatement2.setInt(1, ID);
//	             ResultSet resultSet2 = preparedStatement.executeQuery();
//	             String color;
//	             if (resultSet2.next()) {
//	                  color = resultSet2.getString("Color");
//	                 System.out.println("User's latest Color: " + color);
//
//	                 // 将 color 传送到客户端，这里可以通过网络传输将 color 发送到客户端
//	             } else {
//	            	 color = "Pink";
//	                 System.out.println("No records found for the user.Set color to pink!");
//	                 // 可以设置一个默认的颜色或执行其他操作
//	             }
//	             OutputStream outputStream = socket.getOutputStream();
//	   	        	DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
//	   	        	dataOutputStream.writeUTF(color); 
//	             
//	         } catch (SQLException e) {
//	             e.printStackTrace();
//	         }
	   	        	
	                	 String response = issaved ? "Saved successful" : "Saved failed";

		                    socket.getOutputStream().write(response.getBytes());
	          }else {
                  System.out.println("show diary");

	        	  String Email= dis.readUTF(); 


	        	  int ID = 0 ;
	        	  try (Connection connection = DriverManager.getConnection("jdbc:mysql://54.196.19.67:3306/test", "root", "NtoAWfub3pS6Gs0wlitp") ){
	   			   
	        	   String sql = "SELECT ID FROM accountinfo WHERE Email = ?";
	   			   PreparedStatement preparedStatement = connection.prepareStatement(sql);
	   	           preparedStatement.setString(1, Email);
	   	           ResultSet resultSet = preparedStatement.executeQuery(); 
	   	           if (resultSet.next()) {
	   	             ID = resultSet.getInt("ID");
	   	           }
	               String sql1 = "SELECT Encryption,Date, EncryptDiary, KeyName FROM diaryinfo WHERE ID = ?";
	               PreparedStatement preparedStatement1 = connection.prepareStatement(sql1);
		           preparedStatement1.setInt(1, ID);
		           ResultSet resultSet1 = preparedStatement1.executeQuery(); 
		           while (resultSet1.next()) {
		        	   String Encryption = resultSet1.getString("Encryption");
		        	    String Date = resultSet1.getString("Date");
		        	    String EncryptDiary = resultSet1.getString("EncryptDiary");
		        	    String KeyName = resultSet1.getString("KeyName");

		        	    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		        	 // get data and use ByteArrayOutputStream
		        	 outputStream.write(Encryption.getBytes());
		        	 outputStream.write("#".getBytes()); 
		        	 outputStream.write(Date.getBytes());
		        	 outputStream.write("#".getBytes()); 
		        	 outputStream.write(EncryptDiary.getBytes());
		        	 outputStream.write("#".getBytes()); 
		        	 outputStream.write(KeyName.getBytes());
		        	// Write newlines or other delimiters (as appropriate)
		        	 outputStream.write(System.lineSeparator().getBytes());

		        	 // Get all written data and send to client
		        	 byte[] combinedBytes = outputStream.toByteArray();
		        	 socket.getOutputStream().write(combinedBytes);
		        	    
		        	}

	   		         } catch (SQLException e) {
	   		            e.printStackTrace();
	   		        }
	          }
	        }     
	      
	    
	        }catch (IOException e) {
	            e.printStackTrace();
	        }  
	        finally {
	    		serverSocket.close();
	    		System.out. println("program finished!");
	    		}	
	 }
	
	 
	 
	 
	 public static Boolean saveToDiaryinfo( String Email, String Color, String Encryption, String Date, String EncryptDiary,String KeyName) {
		 int ID ;
		 try (Connection connection = DriverManager.getConnection("jdbc:mysql://54.196.19.67:3306/test", "root", "NtoAWfub3pS6Gs0wlitp") ){
			   String sql = "SELECT ID FROM accountinfo WHERE Email = ?";
			   PreparedStatement preparedStatement = connection.prepareStatement(sql);
	           preparedStatement.setString(1, Email);
	           ResultSet resultSet = preparedStatement.executeQuery(); 
	           if (resultSet.next()) {
	             ID = resultSet.getInt("ID");
	           }else {
	           return false;
	           }
	          
	           int No = 1;
		        // 查询数据库中最大的 NO，然后在其基础上加 1
		        String query = "SELECT MAX(No) FROM diaryinfo";
		        PreparedStatement preparedStatement2 = connection.prepareStatement(query);
		        java.sql.ResultSet rs = preparedStatement2.executeQuery();
		        if (rs.next()) {
		            int maxNO = rs.getInt(1);
		            if (maxNO > 0) {
		            	No = maxNO + 1;
		            }
		        }
		        
		        String insertQuery = "INSERT INTO diaryinfo (No,ID, Color, Encryption, Date, EncryptDiary,KeyName) VALUES (?, ?, ?, ?, ?, ?, ?)";
			     
	            // 创建 PreparedStatement 对象，并设置参数
		        PreparedStatement preparedStatement3 = connection.prepareStatement(insertQuery);
	            preparedStatement3.setInt(1, No);
	            preparedStatement3.setInt(2, ID);
	            preparedStatement3.setString(3, Color);
	            preparedStatement3.setString(4, Encryption);
	            preparedStatement3.setString(5, Date);
	            preparedStatement3.setString(6, EncryptDiary);
	            preparedStatement3.setString(7, KeyName);

	            // 执行 SQL 插入操作
	            preparedStatement3.executeUpdate();
	            System.out.println("Data inserted successfully!");
	            return true;	

		         } catch (SQLException e) {
		            e.printStackTrace();
		        }

			 return false; 
	 }
	 
	 
	 //把获取的注册信息储存到数据库
	 public static int saveToDatabase(Connection connection,String DiaryName, String UserName, String Email, String Password) {
		 // SQL 插入语句
	        String insertQuery = "INSERT INTO accountinfo (ID, DiaryName, UserName, Email, Password) VALUES (?, ?, ?, ?, ?)";

	        try {
	            // 创建 PreparedStatement 对象，并设置参数
	            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
	            int id = generateNextID(connection); // 生成下一个 ID
	            preparedStatement.setInt(1, id);
	            preparedStatement.setString(2, DiaryName);
	            preparedStatement.setString(3, UserName);
	            preparedStatement.setString(4, Email);
	            preparedStatement.setString(5, Password);

	            // 执行 SQL 插入操作
	            preparedStatement.executeUpdate();
	            System.out.println("Data inserted successfully!");
	            return 1;	

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
			return -1;	 
   
	 }

	 
	 // 生成下一个 ID（从 1001 开始递增）
	  private static int generateNextID(Connection connection) throws SQLException {
	        int nextID = 1001;

	        // 查询数据库中最大的 ID，然后在其基础上加 1
	        String query = "SELECT MAX(ID) FROM accountinfo";
	        PreparedStatement preparedStatement = connection.prepareStatement(query);
	        java.sql.ResultSet rs = preparedStatement.executeQuery();

	        if (rs.next()) {
	            int maxID = rs.getInt(1);
	            if (maxID > 0) {
	                nextID = maxID + 1;
	            }
	        }

	        return nextID;
	    }	 

	 //判断传过来的用户名、密码是否跟database中的一致
	private static boolean performVerification(String username,String Email, String password) throws SQLException {
		// TODO Auto-generated method stub
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://54.196.19.67:3306/test", "root", "NtoAWfub3pS6Gs0wlitp") ){
	            String sql = "SELECT Password FROM accountinfo WHERE UserName = ? and Email = ?";
		            PreparedStatement preparedStatement = connection.prepareStatement(sql);
		            preparedStatement.setString(1, username);
		            preparedStatement.setString(2, Email);

		            ResultSet resultSet = preparedStatement.executeQuery();

		           
		            if (resultSet.next()) {
		                String dbPassword = resultSet.getString("password");
		                // 检查用户输入的密码与数据库中的密码是否匹配
		                return password.equals(dbPassword);
		            }else {
		            return false;
		            }
		     
		        } catch (SQLException e) {
		            e.printStackTrace();
		        }
		        return false;
	}



//凯撒解密方法
public static String CaesaDecrypt(String text, int key) {
    StringBuilder result = new StringBuilder();

    for (char ch : text.toCharArray()) {
        if (Character.isLetter(ch)) {
            char base = Character.isUpperCase(ch) ? 'A' : 'a';
            char decryptedChar = (char) (base + (ch - base - key + 26) % 26);
            result.append(decryptedChar);
        } else {
            result.append(ch);
        }
    }

    return result.toString();
}
}
