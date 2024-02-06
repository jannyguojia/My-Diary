package application;

import java.io.*;
import java.net.Socket;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;


import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ShowDiaryListPage extends BorderPane {
	  ServerConnection serverConnection;
	  DataInputStream dis;
	  Socket socket;
	Stage stage;
	String email;
	String Name;
	String UserName;
	String KeyAlias;
	String KeystorePassword;
	//String encodedString;
	String EncryptName;
	String keyFileName;
    private static int encryptionKey;
    AES aes;
    DES des1;
    SecretKey AESsecretKey;

	
	List<String> dateList =  new ArrayList<>(); 
	List<String> decryptedTextList= new ArrayList<>();
	ObservableList<DiaryEntry> dataList = FXCollections.observableArrayList();
	
	BorderPane root = new BorderPane();
	Label titleLabel = new Label("Diary List");
	
	

	Button addChoreButton = new Button("Write a diary");

	@SuppressWarnings("unchecked")
	public ShowDiaryListPage(Stage stage, String Name, String email) throws IOException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
		this.stage = stage;
		this.Name = Name;
		this.email = email;
		
		ObservableList<DiaryEntry> dataList = FXCollections.observableArrayList();
		
		// set background color
		String backgroundImage1 = "url('https://www.shutterstock.com/image-vector/my-diary-vector-illustration-handdrawn-260nw-1742248805.jpg')"; 
		this.setStyle("-fx-background-image: " + backgroundImage1 + "; " +
			    "-fx-background-size: center; " +
			    "-fx-background-position: center bottom; " +
			    "-fx-background-repeat: no-repeat;" // 设置背景图片不重复
			);
		// set heading
		this.titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
		this.titleLabel.setMaxWidth(400);
		this.titleLabel.setAlignment(Pos.CENTER);
		BorderPane.setMargin(titleLabel, new javafx.geometry.Insets(40, 0, 0, 0));
		this.setTop(titleLabel);

		// set add a  button
		this.addChoreButton.setPrefSize(120, 35);
		this.addChoreButton.setStyle("-fx-font-weight: bold;-fx-text-fill: #ffffff; -fx-background-color: #6E51E4;");
		this.addChoreButton.setFont(new Font(15));
		BorderPane.setMargin(addChoreButton, new javafx.geometry.Insets(0, 0, 50, 260));
		this.setBottom(addChoreButton);

		
		 TableView<DiaryEntry> tableView = new TableView<>();
	        TableColumn<DiaryEntry, String> dateCol = new TableColumn<>("Date");
	        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
	        
	        TableColumn<DiaryEntry, String> decryptedTextCol = new TableColumn<>("Decrypted Text");
	        decryptedTextCol.setCellValueFactory(new PropertyValueFactory<>("decryptedText"));

	        tableView.getColumns().addAll(dateCol, decryptedTextCol);
		 String AddDiary = "show";
         String userEmail = SharedData.getuserEmail();
         System.out.println("show page userEmail"+userEmail);
     
         
         Task<Void> task = new Task<Void>() {
          	  @Override
 			    protected Void call() throws Exception {
         
         
		try {
		        serverConnection = new ServerConnection("127.0.0.1", 9090);
					serverConnection.connect();
			
			    socket = serverConnection.getSocket();
			 if (socket != null) {
			     DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			     dos.writeUTF(AddDiary);
			     dos.writeUTF(userEmail);
			 } else {
			     System.out.println("Socket is null. Unable to get output stream.");
			 }


			// 创建BufferedReader以逐行读取数据
			 BufferedReader reader = new BufferedReader(new InputStreamReader(serverConnection.getSocket().getInputStream()));
			 String line;
			 String Encryption = null;
			 String date= null;
			 String encodedString= null;
			 String keyFileName= null;
			 
			 while ((line = reader.readLine()) != null) {
			     // 对每一行数据进行处理
			     String[] parts = line.trim().split("#");

			     if (parts.length >= 4) {
			          Encryption = parts[0].trim();
			          date = parts[1].trim();
			          encodedString = parts[2].trim();
			         keyFileName = parts[3].trim();

			         System.out.println("Encryption:" + Encryption);
			         System.out.println("date:" + date);
			         System.out.println("encodedString:" + encodedString);
			         System.out.println("keyFileName:" + keyFileName);
			         
			         // 进行后续操作，如存储到列表或其他处理
			     } else {
			         // 数据不完整或不符合预期格式，进行适当的错误处理
			         System.out.println("Invalid data format");
			     }
			
	           
	           if(Encryption.equals("AES")) {            //AES  upload key and DEcrypt			
					aes = new AES();
		                try {
		                	// Load AES key from KeyStore
		                    KeyAlias = "guojia";
		                    char[] kstorePassword ="123456".toCharArray();
		                    SecretKey loadedKey = aes.loadKeyFromKeyStore(keyFileName, KeyAlias, kstorePassword);
		                    aes.setSecretKey(loadedKey);
					     // Read encrypt				        	
						
				        byte[] encText = Base64.getDecoder().decode(encodedString);
						try {
							 String decryptedText = aes.decrypt(encText, loadedKey);
	
						           	    DiaryEntry entry = new DiaryEntry(date,decryptedText);
						           	    dataList.add(entry);
					                   tableView.setItems(dataList);
						} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
								| IllegalBlockSizeException
								| BadPaddingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					        } catch (IOException e) {
					            e.printStackTrace();
					        }
	           

				}
	           
	           else if(Encryption.equals("DES")) {                    //DES upload key and DEcrypt
						des1 = new DES();
					KeyAlias = "guojia";
                    char[] kstorePassword ="123456".toCharArray();      
                    SecretKey loadedKey = null;
					try {
						loadedKey = des1.loadKeyFromKeyStore(keyFileName,KeyAlias, kstorePassword);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
			        byte[] encText = Base64.getDecoder().decode(encodedString);
					try {
						 String decryptedText = des1.decrypt(encText, loadedKey);
						    dateList.add(date);
					        decryptedTextList.add(decryptedText);
					        DiaryEntry entry = new DiaryEntry(date,decryptedText);
			           	    dataList.add(entry);
		                   tableView.setItems(dataList);

					} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
							| IllegalBlockSizeException
							| BadPaddingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	

				}else {                                          //caesarCipher  upload key and DEcrypt
					int CaesarKey = 0 ;

		           
					  try (FileReader fileReader = new FileReader(keyFileName);
					             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
						  
						  String caesarKey = bufferedReader.readLine();
					        	CaesarKey= Integer.parseInt(caesarKey);
					        	System.out.println("CaesarKey"+CaesarKey);
					  }catch (IOException e) {
				            e.printStackTrace();
				        }

					String decryptedText = decrypt(encodedString, CaesarKey);
				    dateList.add(date);
			         decryptedTextList.add(decryptedText);
			            DiaryEntry entry = new DiaryEntry(date,decryptedText);
		           	    dataList.add(entry);
	                   tableView.setItems(dataList);
			       
				}
	           }
			 
			 reader.close();

	        }catch (IOException e) {
	            e.printStackTrace();
	        }

		finally {
	            try {
	                // 关闭资源
	                if (dis != null) {
	                    dis.close();
	                }
	                if (socket != null) {
	                    socket.close();
	                }
	                if (serverConnection != null) {
	                    serverConnection.close();
	                }
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
        
		
		return null;
	       
    }
    
};

task.setOnSucceeded(event -> {
	Platform.runLater(() -> {

       // tableView.setItems(dataList);
    });
});
// 启动任务
Thread thread = new Thread(task);
thread.setDaemon(true); // 设置为守护线程
thread.start();
		

		BorderPane.setMargin(tableView, new javafx.geometry.Insets(50, 25, 25, 25));
		this.setCenter(tableView);

		// action on add a chore button
		this.addChoreButton.setOnAction(e -> {
			try {
				openAddDiaryPage();
			} catch (NoSuchAlgorithmException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
	}
	

	// center the content in each cell of the tableView
	private <T> void centerAlignColumn(TableColumn<T, String> column) {
		column.setCellFactory(tc -> new TableCell<>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);

				if (item == null || empty) {
					setText(null);
					setAlignment(Pos.CENTER);
				} else {
					setText(item);
					setAlignment(Pos.CENTER);
				}
			}
		});
	}

	
	public static class DiaryEntry {
        private final String date;
        private final String decryptedText;

        public DiaryEntry(String date, String decryptedText) {
            this.date = date;
            this.decryptedText = decryptedText;
        }

        public String getDate() {
            return date;
        }

        public String getDecryptedText() {
            return decryptedText;
        }
    }
	
	private void openAddDiaryPage() throws NoSuchAlgorithmException {
		stage.setTitle("Add Another Diary");
		AddDiaryPage AddAChorePage = new AddDiaryPage(stage, Name, email);

		Scene scene = new Scene(AddAChorePage, 400, 700);
		stage.setScene(scene);
		stage.show();
	}
	
	 // 凯撒解密方法
    public static String decrypt(String text, int key) {
        StringBuilder result = new StringBuilder();

        for (char ch : text.toCharArray()) {
            if (Character.isLetter(ch)) {
                char base = Character.isUpperCase(ch) ? 'A' : 'a';
                char decryptedChar = (char) (base + (ch - base - key + 26) % 26);
                result.append(decryptedChar);
            }else if(Character.isDigit(ch)){
            	 int digit = Character.getNumericValue(ch);
                 int decryptedDigit = (digit - key + 10) % 10; // 解密数字（0-9）
                 result.append((char) (decryptedDigit + '0')); 
            } else {
                result.append(ch);
            }
        }

        return result.toString();
    }

}
