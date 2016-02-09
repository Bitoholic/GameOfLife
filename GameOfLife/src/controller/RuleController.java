package controller;

import model.GameRules;
import view.Viewer;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * The ruleController controls, and handles the events created when a rule is selected from the menu, or creating a
 * new rule. This class is initialized in {@link MainController}
 * 
 * @author Kent Erlend Bratteng Knudsen
 * 
 */
public class RuleController {
	
	@FXML private TextField ruleName;
	@FXML private TextField ruleString;
	@FXML private TextArea ruleDescription;

	private static RuleController reference;
	private Stage stage;
	
	
	/**
	 * <blockquote>
	 * <b><i>setStage</i></b>
	 * <pre>{@code}public void setStage({@link Stage} stage)</pre>
	 * <p>Sets the stage of this class.</p>
	 * @param stage - Receives a stage to be set.
	 * </blockquote>
	 */
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
	/**
	 * <blockquote>
	 * <b><i>getInstance</i></b>
	 * <pre>{@code}public static {@link RuleController} getInstance()</pre>
	 * <p>Returns the instance of RuleController, if no instance exits its created.</p>
	 * @return reference - The reference to the RuleController. 
	 * </blockquote>
	 * 
	 */
	public static RuleController getInstance() {
		if(reference == null)
			reference = new RuleController();
		
		return reference;
	}
	
	/**
	 * <blockquote>
	 * <b><i>closeWindow</i></b>
	 * <pre>{@code}public void closeWindow()</pre>
	 * <p>Closes the window. The stage that is sett to the controllers stage is closed.</p>
	 * </blockquote>
	 * 
	 */
	public void closeWindow(){
		stage.close();
	}
	
	/**
	 * <blockquote>
	 * <b><i>saveRuleString</i></b>
	 * <pre>{@code}public void saveRuleString()</pre>
	 * <p>Saves a rule to the rules.ser if it's valid.</p>
	 * </blockquote>
	 * 
	 */
	public void saveRulestring() {
		if(GameRules.validRuleString(ruleString.getText())) {
			GameRules gr = GameRules.getInstance();

			try {
				gr.addRuleToCollection(
						ruleName.getText(),
						ruleString.getText(),
						ruleDescription.getText());
				
				gr.saveRulesFile();
				closeWindow();
			} catch (Exception e) {
			
				Viewer.popupBox(AlertType.ERROR,
						"Error saving the rule!",
						e.toString(), null);
			}			
		} else {
			Viewer.popupBox(AlertType.ERROR,
					"Error  could not save the rule to the file.",null, null);
		}
		
	}
	
	/**
	 * <blockquote>
	 * <b><i>ruleStringInput</i></b>
	 * <pre>{@code}public void ruleStringInput({@link KeyEvent} keyEvent)</pre>
	 * <p> Sets the received keys on the keyboard, and checks if they are valid or not.</p>
	 * @param keyEvent - the key pressed on the keyboard.
	 * </blockquote>
	 * 
	 */
	public void ruleStringInput(KeyEvent keyEvent) {
		if(ruleString.getText().length() <= 20) {
			int keyCode = (int)keyEvent.getCharacter().toUpperCase().charAt(0);
			
			
				// Consume characters not equal to /, 0-8, S or B
				// 66 = B
				// 83 = S
				// 47 = /
				// 48 - 56 = 0 - 8
				if(!(keyCode == 66 || keyCode == 83 || (keyCode >= 47 && keyCode <= 56))) {
					keyEvent.consume();
				}
			
		} else {
			keyEvent.consume();
		}
	}
}
