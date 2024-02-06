package application;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class AddDiaryPage extends StackPane {
	  ServerConnection serverConnection;
	  Socket socket;
	  DataOutputStream dos;
	Stage stage;
	String email;
	String UserName;
	String KeyAlias;
	String KeystorePassword;
	String encodedString;
	String EncryptName;
	String keyFileName;
    private static int encryptionKey;
    AES aes;
    SecretKey AESsecretKey;
	//AccountManagement accountManagement = new AccountManagement();

	StackPane rootStackPane = new StackPane();
	BorderPane addDiaryBorderPane = new BorderPane();
	GridPane addDiary = new GridPane();

	ObservableList<String> colorOptions = FXCollections.observableArrayList("Pink", "Blue", "Red",
			"Green");
	final ComboBox colorBox = new ComboBox(colorOptions);

	 CheckBox caesarCipher = new CheckBox("CaesarCipher");
	 //caesarCipher.setSelected(true);

     CheckBox DES = new CheckBox("DES");
     CheckBox AES= new CheckBox("AES");
 	HBox checkBox = new HBox(DES,AES);

    DatePicker datePicker = new DatePicker(LocalDate.now());
	

	Label heading = new Label("Record My Day");
	Label chooseChoreHint = new Label("Mood Color: ");
	Label timingHint = new Label("Record Time: ");

	Button addButton = new Button("Save & Show List");

	// response page of addChoreButton
	StackPane responseStackPane = new StackPane();
	Label outputLabel = new Label("");
	Button ok = new Button("OK");

	@SuppressWarnings("unchecked")
	public AddDiaryPage(Stage stage, String UserName, String email) throws NoSuchAlgorithmException {
		this.stage = stage;
		this.UserName = UserName;
		this.email = email;



		responseStackPane.setVisible(false);
		responseStackPane.setDisable(true);

		//this.setStyle("-fx-background-color: pink;");

		this.heading.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
		this.heading.setMaxWidth(400);
		this.heading.setAlignment(Pos.CENTER);
		BorderPane.setMargin(heading, new javafx.geometry.Insets(20, 0, 0, 0));
		addDiaryBorderPane.setTop(heading);

		this.addDiary.setPadding(new Insets(10, 50, 10, 50));
		this.addDiary.setVgap(15);
		this.addDiary.setHgap(25);

		this.chooseChoreHint.setStyle("-fx-font-weight: bold;-fx-font-size: 15px");
		//this.frequencyHint.setStyle("-fx-font-weight: bold;-fx-font-size: 15px");
		this.timingHint.setStyle("-fx-font-weight: bold;-fx-font-size: 15px");

		this.colorBox.setPrefWidth(155);
		this.checkBox.setPrefWidth(155);
		this.datePicker.setPrefWidth(155);


		this.addDiary.add(chooseChoreHint, 0, 3);
		this.addDiary.add(colorBox, 1, 3);
		this.addDiary.add(caesarCipher, 0, 4);
		this.addDiary.add(checkBox, 1, 4);
		this.addDiary.add(timingHint, 0, 5);
		this.addDiary.add(datePicker, 1, 5);

		addDiaryBorderPane.setCenter(addDiary);

		
		 // 创建输入文本框和输出文本框
		Label tit = new Label("Write or Import Diary:");
		tit.setStyle("-fx-font-weight: bold;-fx-font-size: 15px");
		TextArea textArea = new TextArea();
        textArea.setWrapText(true); // 设置文本自动换行
        textArea.setPromptText("If the checkbox is unchecked, Caesar encryption is selected by default....");
        Button uploadButton = new Button("Upload File");
         Button encryptButton = new Button("Encrypt&Save");
        HBox buttonsHBox1 = new HBox(10); // 设置按钮之间的间距为10
        buttonsHBox1.getChildren().addAll(uploadButton,encryptButton);
        
        TextArea outputTextField = new TextArea();
        outputTextField.setWrapText(true); // 自动换行
        outputTextField.setEditable(false); // 设置输出文本框为不可编辑

        textArea.setPrefWidth(300);
        textArea.setPrefHeight(120);
        outputTextField.setPrefWidth(300);
        outputTextField.setPrefHeight(120);

		

        TextField keyAlias = new TextField();
    	TextField keystorePassword = new TextField();
        Button decryptButton = new Button("Key&Decrypt");
        keyAlias.setStyle("-fx-font-size: 12px; -fx-font-style: italic;");
        keyAlias.setPromptText("keyAlias...");

        keystorePassword.setStyle("-fx-font-size: 12px; -fx-font-style: italic;");
        keystorePassword.setPromptText("keystorePassword...");
     // 创建一个水平布局，包含两个按钮
        HBox buttonsHBox2 = new HBox(10); // 设置按钮之间的间距为10
        buttonsHBox2.getChildren().addAll(keyAlias,keystorePassword);
        
        this.addButton.setPrefSize(300, 35);
		this.addButton.setStyle("-fx-font-weight: bold;-fx-text-fill: #ffffff; -fx-background-color: #6E51E4;");
		this.addButton.setFont(new Font(15));
		BorderPane.setMargin(addButton, new javafx.geometry.Insets(0, 0, 310, 50));
        
        
        
     // 创建一个垂直布局，包含输入文本框、输出文本框和水平布局中的按钮
        VBox vbox = new VBox(15); // 设置组件之间的垂直间距为10
        vbox.setPadding(new Insets(10, 50, 10, 50));

        vbox.getChildren().addAll(tit,textArea,buttonsHBox1,outputTextField, buttonsHBox2,decryptButton,addButton);
		
		
		
        addDiaryBorderPane.setBottom(vbox);

		// set response page (stackPane)
		this.responseStackPane.setMaxWidth(220);
		this.responseStackPane.setMaxHeight(150);
		this.responseStackPane.setStyle("-fx-background-color: lightblue;");
		this.responseStackPane.setMargin(outputLabel, new javafx.geometry.Insets(8, 15, 60, 15));
		this.outputLabel.setWrapText(true);
		this.outputLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");
		this.outputLabel.setContentDisplay(ContentDisplay.CENTER);

		this.ok.setPrefSize(40, 25);
		this.ok.setStyle("-fx-font-weight: bold;-fx-text-fill: #ffffff; -fx-background-color: #6E51E4;");
		this.ok.setFont(new Font(15));
		this.ok.setTranslateX(0);
		this.ok.setTranslateY(30);

		this.responseStackPane.getChildren().addAll(outputLabel, ok);

		colorBox.setOnAction(event -> {
            String selectedColor = (String) colorBox.getSelectionModel().getSelectedItem();
            if (selectedColor != null) {
                switch (selectedColor) {
                    case "Pink":
                        this.setStyle("-fx-background-color: pink;");
                        break;
                    case "Blue":
                    	this.setStyle("-fx-background-color: lightblue;");
                        break;
                    case "Red":
                    	this.setStyle("-fx-background-color: red;");
                        break;
                    case "Green":
                    	this.setStyle("-fx-background-color: lightgreen;");
                        break;
                    default:
                        break;
                }
            }
        });
		
		caesarCipher.setOnAction(event -> {
			if (caesarCipher.isSelected()) {
				DES.setSelected(false);
				AES.setSelected(false);
			} 
		});

		DES.setOnAction(event -> {
			if (DES.isSelected()) {
				caesarCipher.setSelected(false);
				AES.setSelected(false);
			}
		});
		AES.setOnAction(event -> {
			if (AES.isSelected()) {
				DES.setSelected(false);

				caesarCipher.setSelected(false);
			}
		});
		
		
		//caesarCipher/DES/AES Encrypt
		encryptButton.setOnAction(event -> {
			if(textArea!=null&&AES.isSelected()) {              //AESEncrypt
				aes = null;
				try {
					aes = new AES();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String inputText = textArea.getText();
				byte[] encryptedText;
				try {
					// Generate AES key
		            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
		            keyGenerator.init(128);
		            AESsecretKey = keyGenerator.generateKey();
		            // Save AES key to KeyStore
		            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		            keyFileName = "AESkeystore_" + timeStamp + ".jceks";
		            String KAlias = "guojia";
		            char[] ksPassword = "123456".toCharArray();
		            aes.setSecretKey(AESsecretKey);
		            aes.saveKeyToKeyStore(keyFileName, KAlias, ksPassword);
		           
		            encryptedText = aes.encrypt(inputText, AESsecretKey);
		            encodedString = Base64.getEncoder().encodeToString(encryptedText);
		            //System.out.println("The AES encrypted message (Base64): " + encodedString);	
		            
				  outputTextField.setText(encodedString); 
				} catch (InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				}      
       
		// Save encrypted text and key to local file
		String currentDir1 = System.getProperty("user.dir");			
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        EncryptName = "AESencrypted_file_" + timeStamp + ".txt";
        File file1 = new File(currentDir1, EncryptName);
 
        if (file1 != null) {
				try (PrintWriter writer = new PrintWriter(file1)) {
				    writer.print(encodedString);
				} catch (IOException ex) {
				    ex.printStackTrace();
				}
        }
			}else if(textArea!=null&&DES.isSelected()) {        //DES Encrypt
				 DES des1 = null;
				try {
					des1 = new DES();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String inputText = textArea.getText();
				byte[] encryptedText;
				try {
				String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
			    keyFileName = "DESkeystore_" + timeStamp + ".jceks";
				    String kAlias = "guojia";
				    char[] kstorePassword = "123456".toCharArray();
				   des1.saveKeyToKeyStore(keyFileName, kAlias, kstorePassword);
				    // 从 KeyStore 文件中加载密钥
				    SecretKey loadedKey = des1.loadKeyFromKeyStore(keyFileName, kAlias, kstorePassword);
				    encryptedText = des1.encrypt(inputText, loadedKey);
				  encodedString = Base64.getEncoder().encodeToString(encryptedText);				
				  outputTextField.setText(encodedString); 
				} catch (InvalidKeyException | NoSuchPaddingException | InvalidAlgorithmParameterException
					| IllegalBlockSizeException | BadPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				}      
				// Save encrypted text to local file
				String currentDir1 = System.getProperty("user.dir");
		        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		        EncryptName = "DESencrypted_file_" + timeStamp + ".txt";
		        File file1 = new File(currentDir1, EncryptName);
		       
				if (file1 != null) {
				try (PrintWriter writer = new PrintWriter(file1)) {
				    writer.print(encodedString);
				} catch (IOException ex) {
				    ex.printStackTrace();
				}
				}
	   
				}else {                                                              //caesarCipher Encrypt
	            String inputText = textArea.getText();
	            encodedString = CaesarEncrypt(inputText);
				outputTextField.setText(encodedString); 
				// Save encrypted text to local file
				String currentDir1 = System.getProperty("user.dir");
		        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		        EncryptName = "CaesarEncrypted_file_" + timeStamp + ".txt";
				File file1 = new File(currentDir1, EncryptName);

		        String currentDir = System.getProperty("user.dir");
		        keyFileName = "Caesar_Key_" + timeStamp + ".txt";
				try {
					File file2 = new File(currentDir, keyFileName);
				    FileWriter writer = new FileWriter(file2);
				    String keyAsString = String.valueOf(encryptionKey);
				    writer.write(keyAsString);
				    writer.close();

				    System.out.println("saveKey to: " + file2.getName());
				} catch (IOException e) {
				    e.printStackTrace();
				}
	            if (file1 != null) {
	                try (PrintWriter writer = new PrintWriter(file1)) {
	                    writer.print(encodedString);
	                } catch (IOException ex) {
	                    ex.printStackTrace();
	                }
	            }
			}
		});
		//caesarCipher/DES/AES upload key and DEcrypt
				decryptButton.setOnAction(event -> {
					if(textArea!=null&&AES.isSelected()) {            //AES  upload key and DEcrypt			
						//SecretKey loadedKey = null;
						aes = new AES();
						FileChooser fileChooser = new FileChooser();
			            fileChooser.setTitle("Choose a DESkeystore File");
			            File selectedFile = fileChooser.showOpenDialog(stage); 
			            if (selectedFile != null) {
			                try {
			                	String fileName = selectedFile.getName();
			                    KeyAlias = keyAlias.getText();
			                    KeystorePassword=keystorePassword.getText();
			                    char[] kstorePassword = KeystorePassword.toCharArray();
			                 // Load AES key from KeyStore
			                    SecretKey loadedKey = aes.loadKeyFromKeyStore(fileName, KeyAlias, kstorePassword);
			                    aes.setSecretKey(loadedKey);
						       // System.out.println("AESsecretKey" + loadedKey);

			                   
			                    if(!outputTextField.getText().equals("")) {
							String encodedString = outputTextField.getText();
					        System.out.println("The outputTextFieldDES encrypted message 64:" + encodedString);

					        byte[] encText = Base64.getDecoder().decode(encodedString);
							try {
								 String decryptedText = aes.decrypt(encText, loadedKey);
						           // System.out.println("AES Decrypted text: " + decryptedText);
									outputTextField.setText(decryptedText); 
							} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
									| IllegalBlockSizeException
									| BadPaddingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
				                }else {
				            	String encodedString = textArea.getText();
				            	String trimmedString = encodedString.replaceAll("\\s", "");
						        byte[] encText1 = Base64.getDecoder().decode(trimmedString);
								System.out.println("encText:"+encText1);
							
									String decryptedText;
									try {
						                
										decryptedText = aes.decrypt(encText1, loadedKey);
										outputTextField.setText(decryptedText);
									} catch (InvalidKeyException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (NoSuchAlgorithmException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (NoSuchPaddingException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (IllegalBlockSizeException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (BadPaddingException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}	 
				            }
	
			                } catch (IOException ex) {
			                    ex.printStackTrace();
			                } catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
			            }
	
					}else if(textArea!=null&&DES.isSelected()) {           //DES upload key and DEcrypt
						DES des1 = null;
						SecretKey loadedKey = null;
						try {
							des1 = new DES();
						} catch (NoSuchAlgorithmException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						FileChooser fileChooser = new FileChooser();
			            fileChooser.setTitle("Choose a DESkeystore File");
			            File selectedFile = fileChooser.showOpenDialog(stage);  
			            if (selectedFile != null) {
			                try {
			                	String fileName = selectedFile.getName();
								System.out.println("fileName:"+fileName);

			                    KeyAlias = keyAlias.getText();
								System.out.println("KeyAlias:"+KeyAlias);
			                    KeystorePassword=keystorePassword.getText();
			                    char[] kstorePassword = KeystorePassword.toCharArray();
			                    System.out.println("KeystorePassword:"+kstorePassword);
			                    loadedKey = des1.loadKeyFromKeyStore(fileName,KeyAlias, kstorePassword);
			                    System.out.println("DESsecretKey" + loadedKey);
			                    if(!outputTextField.getText().equals("")) {
							String encodedString = outputTextField.getText();
					        System.out.println("The outputTextFieldDES encrypted message 64:" + encodedString);

					        byte[] encText = Base64.getDecoder().decode(encodedString);
							System.out.println("encText:"+encText);
							try {
								String decryptedText = des1.decrypt(encText, loadedKey);
								outputTextField.setText(decryptedText); 
							} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
									| InvalidAlgorithmParameterException | IllegalBlockSizeException
									| BadPaddingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
				            }else {
				            	String encodedString = textArea.getText();
				            	String trimmedString = encodedString.replaceAll("\\s", "");
						        byte[] encText1 = Base64.getDecoder().decode(trimmedString);
								System.out.println("encText:"+encText1);
							
									String decryptedText;
									try {
										decryptedText = des1.decrypt(encText1, loadedKey);
										outputTextField.setText(decryptedText);
									} catch (InvalidKeyException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (NoSuchAlgorithmException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (NoSuchPaddingException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (InvalidAlgorithmParameterException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (IllegalBlockSizeException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (BadPaddingException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
			 
				            }
     
			                } catch (IOException ex) {
			                    ex.printStackTrace();
			                } catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
			            }
			       
					}else {                                          //caesarCipher  upload key and DEcrypt
						int key = 0;
						FileChooser fileChooser = new FileChooser();
			            fileChooser.setTitle("Choose a Key File");
			            File selectedFile = fileChooser.showOpenDialog(stage);

			            if (selectedFile != null) {
			                try {
			                    BufferedReader reader = new BufferedReader(new FileReader(selectedFile));
			                    StringBuilder stringBuilder = new StringBuilder();
			                    String line;
			                    while ((line = reader.readLine()) != null) {
			                        stringBuilder.append(line).append("\n");
			                    }
			                  String key1 = stringBuilder.toString();
			                  String keyTrimmed = key1.trim(); 
			                 key = Integer.parseInt(keyTrimmed);
			                } catch (IOException ex) {
			                    ex.printStackTrace();
			                }
			            }
			            if(!outputTextField.getText().equals("")) {
						String text = outputTextField.getText();
						String decryptedText = decrypt(text, key);
						outputTextField.setText(decryptedText); 
			            }else {
			            	String text = textArea.getText();
			            	String decryptedText = decrypt(text, key);
							outputTextField.setText(decryptedText); 
			            }
						
					}
				});
		//上传文件显示在inputarea，然后加密或解密
		        uploadButton.setOnAction(e -> {
		            FileChooser fileChooser = new FileChooser();
		            fileChooser.setTitle("Choose a File");
		            File selectedFile = fileChooser.showOpenDialog(stage);

		            if (selectedFile != null) {
		                try {
		                    BufferedReader reader = new BufferedReader(new FileReader(selectedFile));
		                    StringBuilder stringBuilder = new StringBuilder();
		                    String line;
		                    while ((line = reader.readLine()) != null) {
		                        stringBuilder.append(line).append("\n");
		                    }
		                    textArea.setText(stringBuilder.toString());
		                } catch (IOException ex) {
		                    ex.printStackTrace();
		                }
		            }
		        });		
				
		this.addButton.setOnAction(e -> {
			 String AddDiary = "one";
			//选择颜色color
			String colorName = (String) colorBox.getValue();
            System.out.println(colorName);

			//选择密码方式Encryption
			  StringBuilder selectedOptions = new StringBuilder("");
	            if (caesarCipher.isSelected()) {
	                selectedOptions.append(caesarCipher.getText()).append("");
	            }
	            if (DES.isSelected()) {
	                selectedOptions.append(DES.getText()).append("");
	            }
	            if (AES.isSelected()) {
	                selectedOptions.append(AES.getText()).append("");
	            }

	            // Remove the last ", " from the string if options were selected
	            if (selectedOptions.length() > "Selected options: ".length()) {
	                selectedOptions.setLength(selectedOptions.length() - 2);
	            } 
	            //System.out.println(selectedOptions);
	            String method = selectedOptions.toString();
	            System.out.println(method);
	           
	            //date
	            LocalDate selectedDate = datePicker.getValue();
	         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	         String date = selectedDate.format(formatter);
	            System.out.println(date);

	            //加密diary和keyname获取
	            System.out.println(encodedString);
	            System.out.println(keyFileName);
	            String userEmail = SharedData.getuserEmail();
	            System.out.println(userEmail);
	            
	            Task<Void> task = new Task<Void>() {
	             	  @Override
	   			    protected Void call() throws Exception {
	       	 try {
					// 连接和交互的部分
				        serverConnection = new ServerConnection("127.0.0.1", 9090);
				        serverConnection.connect();
					  socket = serverConnection.getSocket();
					 if (socket != null ) {
					     // 现在可以安全地使用 socket 对象
					   dos = new DataOutputStream(socket.getOutputStream());
				            // 发送用户名和密码到服务器
					     dos.writeUTF(AddDiary);
					     dos.writeUTF(userEmail);
				            dos.writeUTF(colorName);
				            dos.writeUTF(method);
				            dos.writeUTF(date);
				            dos.writeUTF(encodedString);
				            dos.writeUTF(keyFileName);
				            byte[] response = new byte[100];
				            serverConnection.getSocket().getInputStream().read(response);
				            System.out.println("Server response: " + new String(response).trim());
	           

       
					 } else {
						  System.out.println("Unable to get output stream." );
					   
					 }
			        }catch (IOException ex) {
		                ex.printStackTrace();
			        }finally {
			            try {
			                // 关闭资源
			                if (dos != null) {
			                    dos.close();
			                }
			                if (socket != null) {
			                    socket.close();
			                }
			                if (serverConnection != null) {
			                    serverConnection.close();
			                }
			            } catch (IOException e1) {
			                e1.printStackTrace();
			            }
			        }
			return null;
	       
	          	    }
	          	    
	        	};
	        	
	        	task.setOnSucceeded(event -> {
	        		 Platform.runLater(() -> {
	        			 try {
	 						openShowDiaryListPage();
	 					} catch (NoSuchAlgorithmException | IOException e1) {
	 						// TODO Auto-generated catch block
	 						e1.printStackTrace();
	 					} catch (InvalidAlgorithmParameterException e1) {
	 						// TODO Auto-generated catch block
	 						e1.printStackTrace();
	 					}
	        		    });

	        	});
	        	// 启动任务
	        	Thread thread = new Thread(task);
	        	thread.setDaemon(true); // 设置为守护线程
	        	thread.start();
		});

		ok.setOnAction(e -> {
			responseStackPane.setVisible(false);
			responseStackPane.setDisable(true);

		});

		this.getChildren().addAll(addDiaryBorderPane, responseStackPane);
	}

	private void openShowDiaryListPage() throws NoSuchAlgorithmException, IOException, InvalidAlgorithmParameterException {

		stage.setTitle("Chore List");
		ShowDiaryListPage choreListPage = new ShowDiaryListPage(stage, UserName, email);
		Scene scene = new Scene(choreListPage, 400, 700);
		stage.setScene(scene);
		stage.show();
	}
	
	
	// 凯撒加密方法
    public static String CaesarEncrypt(String text) {
        StringBuilder result = new StringBuilder();
        Random random = new Random();
        encryptionKey = random.nextInt(6) + 2;// 生成2到7之间的随机数字作为加密密钥
        for (char ch : text.toCharArray()) {
            if (Character.isLetter(ch)) {
                char base = Character.isUpperCase(ch) ? 'A' : 'a';
                char encryptedChar = (char) (base + (ch - base + encryptionKey) % 26);
                result.append(encryptedChar);
            }else if(Character.isDigit(ch)){
            	int digit = Character.getNumericValue(ch);
                int encryptedDigit = (digit + encryptionKey) % 10; // 加密数字（0-9）
                result.append((char) (encryptedDigit + '0')); 
            }else {  
                result.append(ch);
            }
        }

        return result.toString();
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
