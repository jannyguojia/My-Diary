package application;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class CreateAccountPage extends StackPane {
	String encryptedPassword;
	//ClientConnection clientConnection;
    ServerConnection serverConnection;
	//AccountManagement accountManagement = new AccountManagement();
	String isRegistered;
	String CaesarEncryptdiaryname ;	
	String CaesarEncryptname ;
	String CaesarEncryptemail ;
	String CaesarEncryptmd5password ;
	String resp;
	
	Stage stage;
	StackPane rootStackPane = new StackPane();

	// homepage
	BorderPane homePageBorderPane = new BorderPane();
	VBox buttonHomepage = new VBox();
	Button createMyAccount = new Button("CREATE A NEW ACCOUNT");
	Button existAccount = new Button("I HAVE AN ACCOUNT");
	Image logoImage = new Image(
			"https://www.shutterstock.com/image-vector/my-diary-vector-illustration-handdrawn-260nw-1742248805.jpg");
	ImageView logo = new ImageView(logoImage);

	// create account page
	Label titleLabel = new Label("Create an Account");
	GridPane createAccountGridPane = new GridPane();
	Label householdLabel = new Label("Step 1: Name My Diary");
	TextField myNameField = new TextField();
	Label creatorLabel = new Label("Step 2: Register");
	TextField nameField = new TextField();
	TextField emailField = new TextField();
	PasswordField passwordField = new PasswordField();
	PasswordField confirmPasswordField = new PasswordField();
	Button registerButton = new Button("Create");

	// response page of registerButton
	StackPane responseStackPane = new StackPane();
	Label outputLabel = new Label("");
	Button ok = new Button("OK");
	Button login = new Button("Log In");

	public CreateAccountPage(Stage stage) {
	
		

		this.stage = stage;

		// set homepage(borderPane)
		createAccountGridPane.setVisible(false);
		createAccountGridPane.setDisable(true);
		responseStackPane.setVisible(false);
		responseStackPane.setDisable(true);

		this.buttonHomepage.setSpacing(6);
		BorderPane.setMargin(buttonHomepage, new javafx.geometry.Insets(0, 0, 20, 0));

		this.createMyAccount.setPrefSize(400, 35);
		this.createMyAccount.setStyle("-fx-font-weight: bold;");
		this.createMyAccount.setFont(new Font(15));
		this.createMyAccount.getStyleClass().add("button");
		this.createMyAccount.setTextFill(Color.web("#ffffff"));

		this.existAccount.setPrefSize(400, 35);
		this.existAccount.setStyle("-fx-font-weight: bold;");
		this.existAccount.setFont(new Font(15));
		this.existAccount.getStyleClass().add("button");
		this.existAccount.setTextFill(Color.web("#ffffff"));

		this.buttonHomepage.getChildren().addAll(createMyAccount, existAccount);
		this.homePageBorderPane.setBottom(buttonHomepage);

		this.homePageBorderPane.setCenter(logo);
		this.homePageBorderPane.setStyle("-fx-background-color: #ffffff");

		createMyAccount.setOnAction(e -> {
			homePageBorderPane.setVisible(false);
			homePageBorderPane.setDisable(true);
			createAccountGridPane.setVisible(true);
			createAccountGridPane.setDisable(false);
		});

		// set response page (stackPane)
		this.responseStackPane.setMaxWidth(220);
		this.responseStackPane.setMaxHeight(150);
		this.responseStackPane.setStyle("-fx-background-color: lightblue;");
		this.responseStackPane.setMargin(outputLabel, new javafx.geometry.Insets(8, 15, 60, 15));
		this.outputLabel.setWrapText(true);
		this.outputLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");
		this.outputLabel.setContentDisplay(ContentDisplay.CENTER);

		this.ok.setPrefSize(40, 25);
		this.ok.setStyle("-fx-font-weight: bold;");
		this.ok.setFont(new Font(12));
		this.ok.getStyleClass().add("button");
		this.ok.setTextFill(Color.web("#ffffff"));
		this.ok.setTranslateX(0);
		this.ok.setTranslateY(30);

		this.login.setPrefSize(60, 25);
		this.login.setStyle("-fx-font-weight: bold;");
		this.login.setFont(new Font(12));
		this.login.getStyleClass().add("button");
		this.login.setTextFill(Color.web("#ffffff"));
		this.login.setTranslateX(0);
		this.login.setTranslateY(30);
		login.setVisible(false);
		login.setDisable(true);

		this.responseStackPane.getChildren().addAll(outputLabel, ok, login);

		// set gridPane
		String backgroundImage1 = "url('https://www.shutterstock.com/image-vector/my-diary-vector-illustration-handdrawn-260nw-1742248805.jpg')"; 
		this.createAccountGridPane.setStyle("-fx-background-image: " + backgroundImage1 + "; " +
			    "-fx-background-size: center; " +
			    "-fx-background-position: center bottom; " +
			    "-fx-background-repeat: no-repeat;"
			);
		this.createAccountGridPane.setPadding(new Insets(35, 30, 20, 30));
		ColumnConstraints col1 = new ColumnConstraints(350);
		this.createAccountGridPane.getColumnConstraints().add(col1);
		this.createAccountGridPane.setVgap(15);
//		this.grid.setHgap(10);
		this.titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
		this.titleLabel.setMaxWidth(400);
		this.titleLabel.setAlignment(Pos.CENTER);
		this.householdLabel.setPrefWidth(350);
//		this.householdLabel.setPrefHeight(30);
		this.householdLabel.setFont(new Font(15));
		this.creatorLabel.setPrefWidth(350);
		this.creatorLabel.setFont(new Font(15));

		this.registerButton.setPrefSize(350, 35);
		this.registerButton.setStyle("-fx-font-weight: bold;");
		this.registerButton.setFont(new Font(20));
		this.registerButton.getStyleClass().add("button");
		this.registerButton.setTextFill(Color.web("#ffffff"));

		this.myNameField.setStyle("-fx-font-size: 14px; -fx-font-style: italic;");
		myNameField.setPromptText("diary's name (maximum 10 characters)");
		myNameField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.length() > 10) {
				myNameField.setText(oldValue);
			}
		});

		this.nameField.setStyle("-fx-font-size: 14px; -fx-font-style: italic;");
		nameField.setPromptText("Your name (maximum 10 characters)");
		nameField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.length() > 10) {
				nameField.setText(oldValue);
			}
		});

		this.emailField.setStyle("-fx-font-size: 14px; -fx-font-style: italic;");
		emailField.setPromptText("Your email (example@example.com)");
		emailField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!Util.isValidEmailFormat(newValue)) {
				emailField.setStyle("-fx-text-box-border: red ; -fx-focus-color: red ;");
			} else {
				emailField.setStyle(""); // Restore default style
			}
		});

		this.passwordField.setStyle("-fx-font-size: 14px; -fx-font-style: italic;");
		passwordField.setPromptText("Set a password");

		this.confirmPasswordField.setStyle("-fx-font-size: 14px; -fx-font-style: italic;");
		confirmPasswordField.setPromptText("Confirm the password");

		this.createAccountGridPane.add(titleLabel, 0, 0);
		this.createAccountGridPane.add(householdLabel, 0, 3);
		this.createAccountGridPane.add(myNameField, 0, 4);
		this.createAccountGridPane.add(creatorLabel, 0, 6);
		this.createAccountGridPane.add(nameField, 0, 7);
		this.createAccountGridPane.add(emailField, 0, 8);
		this.createAccountGridPane.add(passwordField, 0, 9);
		this.createAccountGridPane.add(confirmPasswordField, 0, 10);
		this.createAccountGridPane.add(registerButton, 0, 12);


		this.registerButton.setOnAction(e -> {	
		
		    	isRegistered = "no";
		    	String diaryname = myNameField.getText();
				String name = nameField.getText();
				String email = emailField.getText();
				String password = passwordField.getText();
				String confirmPassword = confirmPasswordField.getText();
				responseStackPane.setVisible(true);
				responseStackPane.setDisable(false);
				//利用MD5方式给密码加密
				String md5password = MD5Password(password);
	            System.out.println("MD5: " + md5password);

				//利用凯撒密码方式给全部信息加密，用于传输安全
				CaesarEncryptdiaryname = CaesarEncrypt(diaryname,3);	
				CaesarEncryptname = CaesarEncrypt(name,3);
				CaesarEncryptemail = CaesarEncrypt(email,3);
				CaesarEncryptmd5password = CaesarEncrypt(md5password,3);
				System.out.println("isRegistered:"+isRegistered+"  diaryname:"+CaesarEncryptdiaryname +"    name:"+CaesarEncryptname+"    email:"+CaesarEncryptemail+"    password:"+CaesarEncryptmd5password);
	
				responseStackPane.setVisible(true);
				responseStackPane.setDisable(false);

				String message = Util.createAccountValidateData(diaryname, name, email, password, confirmPassword);

				if (message.equals("Create successful!")) {
					setTextFieldUneditable();
					outputLabel.setText(message);
					ok.setVisible(false);
					ok.setDisable(true);
					login.setVisible(true);
					login.setDisable(false);
				} else {
					outputLabel.setText(message);
					setTextFieldUneditable();
				}
		
		});
		
		this.existAccount.setOnAction(e -> {
			openLoginPage();
		});
		
		ok.setOnAction(e -> {
			responseStackPane.setVisible(false);
			responseStackPane.setDisable(true);
			setTextFieldEditable();
		});

		
		    byte[] response = new byte[100];
			Task<Void> task = new Task<Void>() {
	    @Override
	    protected Void call() throws Exception {
	        // 连接和交互的部分
	        serverConnection = new ServerConnection("127.0.0.1", 9090);
	        serverConnection.connect();
	        try {

	            DataOutputStream dos = new DataOutputStream(serverConnection.getSocket().getOutputStream());  
	         // 发送用户名和密码到服务器
	            dos.writeUTF(isRegistered);
	            dos.writeUTF(CaesarEncryptdiaryname);
	            dos.writeUTF(CaesarEncryptname);
	            dos.writeUTF(CaesarEncryptemail);
	            dos.writeUTF(CaesarEncryptmd5password);
	           
	        } catch (IOException f) {
	            f.printStackTrace();
	        }
	        //接受server返回信息
	        serverConnection.getSocket().getInputStream().read(response);
            
	        resp = new String(response).trim();
	       System.out.println("Server response: " + resp);
			serverConnection.close();


	        return null;
	    }
	    
	};
	
	task.setOnSucceeded(event -> {
		 Platform.runLater(() -> {
			
			openLoginPage();
			 
			});
		});
		
		login.setOnAction(e -> {
		    
			
			// 启动任务
			Thread thread = new Thread(task);
			thread.setDaemon(true); // 设置为守护线程
			thread.start();	 

		    });

		

		this.getChildren().addAll(homePageBorderPane, createAccountGridPane, responseStackPane);

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
            System.out.println("Encrypted Password (MD5): " + encryptedPassword);

        } catch (NoSuchAlgorithmException e) {
            System.out.println("MD5 algorithm not found.");
        }
		return encryptedPassword;
		
	}

	private void setTextFieldUneditable() {
		myNameField.setEditable(false);
		nameField.setEditable(false);
		emailField.setEditable(false);
		passwordField.setEditable(false);
		confirmPasswordField.setEditable(false);
	}

	private void setTextFieldEditable() {
		myNameField.setEditable(true);
		nameField.setEditable(true);
		emailField.setEditable(true);
		passwordField.setEditable(true);
		confirmPasswordField.setEditable(true);
	}

	private void openLoginPage() {
		stage.setTitle("Login page");
		LoginPage lgoinPage = new LoginPage(stage);

		Scene scene = new Scene(lgoinPage, 400, 700);
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

    // 凯撒解密方法
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
	    
	    
	    
}
