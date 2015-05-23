package pl.edu.pw.elka.einis;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.PatternLayout;

/**
 * Hello world!
 *
 */
public class EinisApp extends Application {

	public static void main(String[] args) {
		String logFormat = "%d [%t] %-5p %c - %m%n";//"[%c{1}] [%p] %m%n"; // %d [%t] %-5p %c - %m%n
		ConsoleAppender consoleAppender = new ConsoleAppender(new PatternLayout(logFormat));
		BasicConfigurator.configure(consoleAppender);

		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {

		Parent root = FXMLLoader.load(getClass().getClassLoader().getResource(
				"main.fxml"));

		Scene scene = new Scene(root);

		stage.setTitle("EINIS App");
		stage.setScene(scene);
		stage.show();
	}
}
