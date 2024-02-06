package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This is the main UI class.��
 */
public class Main extends Application {

	public int ID;

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Register");

		CreateAccountPage register = new CreateAccountPage(primaryStage);

		Scene scene = new Scene(register, 400, 700);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}