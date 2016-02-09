package view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Contains an infobox.
 * 
 * @author Ali Arfan
 * @author Knut Erlend Bratteng Knudsen
 * @author Stian Tornholm Grimsgaard
 *
 */

public class GoLInfoBox {
	
   /**
	 *
	 * <blockquote>
	 * <b><i>displayBox</i></b>
	 * <pre>{@link}public void displayBox({@link String} title, {@link String} message1, {@link String} message2, {@link String} message3, 
	 * {@link String} message4, {@link String} message5, {@link String} button)</pre>
	 * <p>Display an infobox with info matching the parameters.</p>
	 * 
	 * @param title - Sets the title
	 * @param message1 - Sets a text for the first message line
	 * @param message2 - Sets a text for the second message line
	 * @param message3 - Sets a text for the third message line
	 * @param message4 - Sets a text for the fourth message line
	 * @param message5 - Sets a text for the fifth message line
	 * @param button - Sets the button text
	 * </blockquote>
	 * 
	 */
	public static void displayBox(String title, String message1, String message2, String message3, 
		String message4, String message5, String button) {
		//Creating stage for InfoBox
		Stage primaryStage = new Stage();
		
		//Customizing stage
		primaryStage.setTitle(title);
		primaryStage.setMinHeight(300);
		primaryStage.setMinWidth(600);
		
		//Creatings labels and button
		Label label1 = new Label(message1);
		Label label2 = new Label(message2);
		Label label3 = new Label(message3);
		Label label4 = new Label(message4);
		Label label5 = new Label(message5);
		Button okButton = new Button(button);
		okButton.setMinSize(70, 30);
		
		//Making sure the button is set on action when Enter is pressed.
		okButton.defaultButtonProperty().bind(okButton.focusedProperty());
		
		//Handling what the button does
		okButton.setOnAction(e -> primaryStage.close());
		
		//Creating layout
		VBox layout = new VBox(10);
		
		//Setting alignment for the layout we created
		layout.setAlignment(Pos.CENTER);
		
		//Adding children to the layout we created
		layout.getChildren().addAll(label1, label2, label3, label4, label5, okButton);
		
		//Creating and setting a scene
		Scene scene = new Scene(layout);
		primaryStage.setScene(scene);
		
		primaryStage.show();
	}
}