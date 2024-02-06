package application;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class LoginPage extends StackPane {
    ServerConnection serverConnection;
	Stage stage;
	String encryptedPassword;
	String email;
	String username;


	StackPane loginLayout = new StackPane();
	GridPane loginGrid = new GridPane();

	Label heading = new Label("LOGIN MY ACCOUNT");
	TextField MyNameField = new TextField();
	TextField emailField = new TextField();
	PasswordField passwordField = new PasswordField();
	Button loginButton = new Button("Log In");

	// error page of login
	StackPane errorPane = new StackPane();
	Label errorLabel = new Label("");
	Button ok = new Button("OK");

	public LoginPage(Stage stage) {
		this.stage = stage;
		this.errorPane.getChildren().addAll(errorLabel, ok);
		this.getChildren().addAll(loginGrid, errorPane);
		errorPane.setVisible(false);
		errorPane.setDisable(true);
		String backgroundImage1 = "url('https://www.shutterstock.com/image-vector/my-diary-vector-illustration-handdrawn-260nw-1742248805.jpg')"; 
		this.setStyle("-fx-background-image: " + backgroundImage1 + "; " +
			    "-fx-background-size: center; " +
			    "-fx-background-position: center bottom; " +
			    "-fx-background-repeat: no-repeat;" 
			);

		this.loginGrid.setPadding(new Insets(35, 30, 20, 30));
		ColumnConstraints col1 = new ColumnConstraints(350);
		this.loginGrid.getColumnConstraints().add(col1);
		this.loginGrid.setVgap(15);

//		this.heading.getStyleClass().add("heading");
		this.heading.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
		this.heading.setMaxWidth(400);
		this.heading.setAlignment(Pos.CENTER);

		this.loginButton.setPrefSize(350, 35);
		this.loginButton.setStyle("-fx-font-weight: bold;-fx-text-fill: #ffffff; -fx-background-color: #6E51E4;");
		this.loginButton.setFont(new Font(15));


		this.MyNameField.setStyle("-fx-font-size: 14px; -fx-font-style: italic;");
		MyNameField.setPromptText("Your name");

		this.emailField.setStyle("-fx-font-size: 14px; -fx-font-style: italic;");
		emailField.setPromptText("Your email");

		this.passwordField.setStyle("-fx-font-size: 14px; -fx-font-style: italic;");
		passwordField.setPromptText("Your password");

		this.loginGrid.add(heading, 0, 0);
		this.loginGrid.add(MyNameField, 0, 3);
		this.loginGrid.add(emailField, 0, 4);
		this.loginGrid.add(passwordField, 0, 5);
		this.loginGrid.add(loginButton, 0, 7);

		// set error page (stackPane)
		this.errorPane.setMaxWidth(220);
		this.errorPane.setMaxHeight(150);
		this.errorPane.setStyle("-fx-background-color: lightblue;");
		this.errorPane.setMargin(errorLabel, new javafx.geometry.Insets(8, 15, 60, 15));
		this.errorLabel.setWrapText(true);
		this.errorLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");
		this.errorLabel.setContentDisplay(ContentDisplay.CENTER);

		this.ok.setPrefSize(40, 25);
		this.ok.setStyle("-fx-font-weight: bold;-fx-text-fill: #ffffff; -fx-background-color: #6E51E4;");
		this.ok.setFont(new Font(15));
		this.ok.setTranslateX(0);
		this.ok.setTranslateY(30);

		this.loginButton.setOnAction(e -> {
			Task<Void> task = new Task<Void>() {
			    @Override
			    protected Void call() throws Exception {
			 String isRegistered = "yes";
			username = MyNameField.getText();
			email = emailField.getText();
			 SharedData.setuserEmail(email);
			String password = passwordField.getText();
			//利用MD5方式给密码加密
			String md5password = MD5Password(password);
            System.out.println("MD5: " + md5password);

			//利用凯撒密码方式给全部信息加密，用于传输安全
			String CaesarEncryptusername = CaesarEncrypt(username,3);
			String CaesarEncryptemail = CaesarEncrypt(email,3);
			String CaesarEncryptmd5password = CaesarEncrypt(md5password,3);

			 try {
				// 连接和交互的部分
			        serverConnection = new ServerConnection("127.0.0.1", 9090);
			        serverConnection.connect();
				 Socket socket = serverConnection.getSocket();
				 if (socket != null) {
				     // 现在可以安全地使用 socket 对象
				     DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			            // 发送用户名和密码到服务器
				     dos.writeUTF(isRegistered);
			            dos.writeUTF(CaesarEncryptusername);
			            dos.writeUTF(CaesarEncryptemail);
			            dos.writeUTF(CaesarEncryptmd5password);
			            
			            byte[] response = new byte[100];
			            serverConnection.getSocket().getInputStream().read(response);
			            System.out.println("Server response: " + new String(response).trim());

			            
			             Platform.runLater(() -> {
			            	 String errorMessage = Util.loginValidateData(username, email, password);
			            	 if (errorMessage.equals("") && new String(response).trim().equals("Login successful")) {
				try {
					openAddAChorePage();
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				errorPane.setVisible(true);
				errorPane.setDisable(false);
				errorLabel.setText("Login failed! Please check the user name, your email and password!");
				MyNameField.clear();
				emailField.clear();
				passwordField.clear();
				emailField.setPromptText("Your email");
				passwordField.setPromptText("Your password");
				MyNameField.setPromptText("Your name");
			}
			           	 
		    });
			            
			            
				 } else {
				     // 处理 socket 对象为空的情况
				     System.out.println("Socket is null. Unable to get output stream.");
				 }
		           serverConnection.close();
		        }catch (IOException ex) {
	                ex.printStackTrace();
		        } 
	        return null;
	    }
	    
	};
	
	task.setOnSucceeded(event -> {
		
	});

	// 启动任务
	Thread thread = new Thread(task);
	thread.setDaemon(true); // 设置为守护线程
	thread.start();
	
		});

		ok.setOnAction(e -> {
			errorPane.setVisible(false);
			errorPane.setDisable(true);
		});
	}


	private void openAddAChorePage() throws NoSuchAlgorithmException {
		stage.setTitle("Add Diary");
		AddDiaryPage AddDiaryPage = new AddDiaryPage(stage, username, email);

		Scene scene = new Scene(AddDiaryPage, 400, 700);
		stage.setScene(scene);
		stage.show();
	}
	// 凯撒加密方法
    public static String CaesarEncrypt(String text, int key) {
        StringBuilder result = new StringBuilder();

        for (char ch : text.toCharArray()) {
            if (Character.isLetter(ch)) {
                char base = Character.isUpperCase(ch) ? 'A' : 'a';
                char encryptedChar = (char) (base + (ch - base + key) % 26);
                result.append(encryptedChar);
            } else {
                result.append(ch);
            }
        }

        return result.toString();
    }
    private String MD5Password(String password) {
		//String encryptedPassword;
		try {
            // 创建 MessageDigest 实例，并指定算法为 MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // 将密码转换为字节数组
            byte[] passwordBytes = password.getBytes();

            // 计算摘要
            byte[] digest = md.digest(passwordBytes);

            // 将摘要转换为十六进制字符串
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }

           encryptedPassword = sb.toString();
            //System.out.println("Encrypted Password (MD5): " + encryptedPassword);

        } catch (NoSuchAlgorithmException e) {
            System.out.println("MD5 algorithm not found.");
        }
		return encryptedPassword;
		
	}

}
