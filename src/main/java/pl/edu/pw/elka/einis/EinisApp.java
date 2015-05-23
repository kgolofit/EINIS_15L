package pl.edu.pw.elka.einis;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Hello world!
 *
 */
public class EinisApp extends Application
{

    public static void main(String[] args) {
    	launch(args); 	
    }

	@Override
	public void start(Stage stage) throws Exception {
		
		Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("main.fxml"));
	    
        Scene scene = new Scene(root);
    
        stage.setTitle("EINIS App");
        stage.setScene(scene);
        stage.show();
	}
}
