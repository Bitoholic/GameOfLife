import java.io.IOException;
import java.net.URL;

import view.Viewer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * 
 * This class is used to start the game.
 * @author Ali Arfan
 * @author Kent Erlend Bratteng Knudsen
 * @author Stian Tornholm Grimsgaard
 *
 */

public class MainGoL extends Application {

	private static final String fxmlFile = "./View/GoLGUI.fxml";
	
	/**
	 * <blockquote>
	 * <b><i>main</i></b>
	 * <pre>{@code}public static void main({@link String} args[])</pre>
	 * <p>This static method starts the game</p>  
	 * @param args - a collection of String's.
	 * </blockquote>
	 */
	
	public static void main(String args[]) {
		Application.launch(args);
	}

	/**
	 *
	 * <blockquote>
	 * <b><i>start</i></b>
	 * <pre>{@code}public void start({@link Stage} ps)</pre>
	 * <p>Starts the primary stage</p>
	 * 
	 * @param ps - The primary stage
	 *</blockquote>
	 *
	 */
	
	@Override
	public void start(Stage ps) throws Exception {
		URL resourceFile = getClass().getResource(fxmlFile);
		
		try {
			Parent root = FXMLLoader.load(resourceFile);
			
			ps.setTitle("Conway's Game of Life");
			ps.setScene(new Scene(root, 800, 600));
			ps.show();
		} catch (IOException e) {
			Viewer.popupBox(
					AlertType.ERROR, 
					"IOException", 
					e.getMessage(),
					"Closing program.");
			
		} catch (NullPointerException e) {
			Viewer.popupBox(
					AlertType.ERROR,
					"Error loading file",
					"File " + fxmlFile + " not found.", 
					"Closing program.");
		} catch (Exception e) {
			Viewer.popupBox(
					AlertType.ERROR,
					"Error",
					e.getMessage(), 
					"Closing program.");			
		}
	}
}
