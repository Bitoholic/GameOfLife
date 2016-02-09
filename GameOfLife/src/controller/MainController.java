package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.Slider;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.Filehandling;
import model.GameBoard;
import model.GameRules;
import model.PatternFormatException;
import model.Rule;
import view.GoLInfoBox;
import view.Viewer;

/**
 * The main controller of this program.  Controls and notifies every action taken,
 * except from mouse events.  The controller implements {@link Initializable}.
 *
 * @author Ali Arfan
 * @author  Kent Erlend Bratteng Knudsen
 * @author Stian Tornholm Grimsgaard
 * 
 */
public class MainController implements Initializable {
	
	@FXML private VBox root;
	@FXML private Pane pane;
	@FXML private Canvas canvas;
	@FXML private Label cellsAliveCnt;
	@FXML private Label cellsDeadCnt;
	@FXML private Button startPauseButton;
	@FXML private Slider speedSlider;
	@FXML private Slider zoomSlider;
	@FXML private CheckMenuItem soundMute;
	@FXML private ColorPicker colorPicker;
	@FXML private Menu mnuRuleList;
	@FXML private Label labelDelay;
	@FXML private Label labelGen;
	
	private Viewer viewer;
	private GameBoard board;
	private MouseController mc;
	private RuleController ruleController;
	
	private AnimationTimer at;
	
	private long delayNanoseconds; 
	private long previousNanotime = 0;
	
	private boolean userDrawActivity;
	
	private Map<String, Integer> hashMapRulesMenu = new HashMap<String, Integer>();
	
	
	
	/**
	 * <blockquote>
	 * <b><i>initialize</i></b>
	 * <pre>{@code public void initialize({@link URL} location,{@link ResourceBundle} resources)}</pre>
	 * <p>initializes needed methods and objects to run Game of Life. The board,rules and  view is initialized. see {@link initialize} for more information. </p>
	 * 
	 * @param location - location of the root object.
	 * @param resources - resources needed to localize the root object. 
	 * </blockquote>
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		board  = new GameBoard(11, 11);
		viewer = new Viewer(canvas, board);
		mc     = new MouseController(board);
		
		ruleController = RuleController.getInstance();

		board.settings().addObserver(viewer);
		board.settings().setDefaultCellSize(zoomSlider.getValue());
		board.settings().update();
		
		zoomSlider.valueProperty().addListener((obsValue, prevValue, newValue) -> {
			board.settings().setCellSizeInner(newValue.doubleValue());
			board.settings().update();
			viewer.render();
		});
		
		pane.widthProperty().addListener((oldV, prevV, newV) -> {
			canvas.setWidth(newV.doubleValue());
			viewer.render();
		});
		
		pane.heightProperty().addListener((oldV, prevV, newV) -> {
			canvas.setHeight(newV.doubleValue());
			viewer.render();
		});
		
		canvas.setOnScroll((ScrollEvent se) -> {
			mc.scrollZoom(se, zoomSlider.getMin(), zoomSlider.getMax());
			zoomSlider.setValue(board.settings().getCellSizeInner());
			viewer.render();
		});
		
		canvas.setOnMouseMoved((MouseEvent me) -> {
			mc.mouseMoved(me);
		});
		
		canvas.setOnMouseReleased((MouseEvent me) -> {
			if(me.getButton() == MouseButton.PRIMARY) { 
				userDrawActivity = true;
			}
			
			mc.mouseButtonReleased(me);
			viewer.render();
		});
		
		canvas.setOnMouseDragged((MouseEvent me) -> {
			mc.mouseDragged(me);
			viewer.render();
		});
		
	
		loadRuleMenu();
		
		delayNanoseconds = (long)speedSlider.getValue();
		speedSlider.valueProperty().addListener((obsValue, prevValue, newValue) -> {
			delayNanoseconds = newValue.longValue();
		});
		
		at = new AnimationTimer() {
			
			@Override
			public void handle(long now) {
				if((now - previousNanotime) > delayNanoseconds){
					long startProcess = System.currentTimeMillis();
					
					board.nextGeneration();
					viewer.render();
					
					labelGen.setText(Long.toString(board.getGenerationNo()));
					labelDelay.setText(Long.toString(System.currentTimeMillis() - startProcess));
					
					
					previousNanotime = now;
				}
			}
		};
	}
	
	/**
	 * <blockquote>
	 * <b><i>loadRuleMenu</i></b>
	 * <pre>{@code public void loadRuleMenu()}</pre>
	 * <p>Loads the  game rules, and displays them in a horizontal row</p>
	 * </blockquote>
	 */
	public void loadRuleMenu() {
		GameRules gr = GameRules.getInstance();
	
		Rule rule;
		Label label;
		
		WritableImage img = new WritableImage(16,16);
		ImageView x = new ImageView(img);
		
		
		while(gr.hasNextRulesCollection()) {
			rule = gr.getNextRulesCollection();
			
			label = new Label(rule.getRuleName());
			
			label.setGraphic(x);
			label.setTooltip(new Tooltip(rule.getRuleDescription()));
			
			CustomMenuItem cmi = new CustomMenuItem(label);			
			cmi.setHideOnClick(false);
			cmi.setOnAction((event) -> {
				menuClicked(event);
			});
			
			mnuRuleList.getItems().add(cmi);
			hashMapRulesMenu.put(rule.getRuleName(), rule.getRuleIndex());
		}
	}
	
	/**
	 * <blockquote>
	 * <b><i>menuClicked</i></b>
	 * <pre>{@code public void menuCicked({@link ActionEvent} event)}</pre>
	 * <p>Gets the text, and content of the given rules.</p>
	 * 
	 * @param event - The menu item pressed. 
	 * </blockquote>
	 */
	public void menuClicked(ActionEvent event ) {
		GameRules gr = GameRules.getInstance();
		
		CustomMenuItem menuItem;
		menuItem = (CustomMenuItem) event.getSource();
		Label label = (Label)menuItem.getContent();
		gr.setNextRulesCollection(hashMapRulesMenu.get(label.getText()));
		Rule rule = gr.getNextRulesCollection();
		gr.setRules(rule.getRuleString());
		
	}
	
	/**
	 * <blockquote>
	 * <b><i>menuCreateRule</i></b>
	 * <pre>{@code public void menuCreateRule({@link ActionEvent} event)}</pre>
	 * <p>Opens a new window, where you can create  and save new game rules. </p>
	 * 
	 * @param event -  The event of the menu item pressed.
	 * </blockquote>
	 */
	public void menuCreateRule(ActionEvent event) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../View/AddRule.fxml"));
			fxmlLoader.setController(ruleController);
			Parent parent = fxmlLoader.load();
			
			Stage stage = new Stage();
			stage.setTitle("Add a new rule");
			stage.setScene(new Scene(parent));
			stage.initModality(Modality.WINDOW_MODAL);
			Window owner= root.getScene().getWindow();

			stage.initOwner(owner);
			
			ruleController.setStage(stage);
			
			stage.showAndWait();
			loadRuleMenu();
		} 
		
		catch(Exception e) {
			Viewer.popupBox(AlertType.ERROR,
					"Error opening the rule file.",
					e.toString(), null);
		}
	}
	
	/**
	 * <blockquote>
	 * <b><i>handleStartBtn</i></b>
	 * <pre>{@code public void handleStartBtn()}</pre>
	 * <p>Starts and pauses the animation of the cells on the board.</p>
	 * </blockquote>
	 */
	public void handleStartBtn() {
		if(startPauseButton.getText().equals("Start")) {
		    at.start();
		    startPauseButton.setText("Pause");
		} else {
		    at.stop();
		    startPauseButton.setText("Start");
		}
	}
	
	/**
	 * <blockquote>
	 * <b><i>handleResetBtn</i></b>
	 * <pre>{@code public void handleResetBtn()}</pre>
	 * <p>Resets the board, and sets all element in the array to dead, and the dead cells have the same color
	 * as the background color. </p>
	 * </blockquote>
	 */
	public void handleResetBtn() {
		at.stop();
		startPauseButton.setText("Start");
		labelDelay.setText("0");
		labelGen.setText("0");
		
		
		if(userDrawActivity == true) {
			board.clear();
			userDrawActivity = false;
		} else {
			board.resetSize();
			board.settings().resetCellSize();
			viewer.centerView();
		}
		
		zoomSlider.setValue(board.settings().getDefaultCellSize());
		
		viewer.render();
	}
	
	/**
	 * <blockquote>
	 * <b><i>loadRLE</i></b>
	 * <pre>{@code public void loadRle()}</pre>
	 * <p>opens a {@link FileChooser} which lets you select and load a RLE file to the game board. </p>
	 *@throws PatternFormatException - throws an exception if the pattern in the specified
	 *file is incorrect.  
	 * </blockquote>
	 */
	public void loadRLE() throws PatternFormatException {
		FileChooser fc = new FileChooser();
		fc.getExtensionFilters().addAll(new ExtensionFilter("RLE files", "*.rle"));
		
		// This is for making the FileChooser modal to the main GOL window.
		Window owner = root.getScene().getWindow();
		File selectedFile = fc.showOpenDialog(owner);
		
		if(selectedFile != null) {
			Filehandling fh = new Filehandling();
			try {
				fh.readGameBoardFromDisk(selectedFile.toPath());
				board.setPattern(fh.getFilePattern());
				viewer.render();
			} catch (IOException e) {
				Viewer.popupBox(AlertType.ERROR,
						"Error opening file",
						"Could not open file\n" + selectedFile,
						e.toString());
			} catch (PatternFormatException e) {
				Viewer.popupBox(AlertType.ERROR,
						"Error opening RLE file",
						e.toString(), null);
			}
		}
	}
	
	
	public void loadURL() {
		TextInputDialog dialog = new TextInputDialog("");
		dialog.setTitle("Load RLE file from URL");
		dialog.setHeaderText("Please enter a URL adress pointing to the RLE file.");
		
		Optional<String> result = dialog.showAndWait();
		if(result.isPresent()) {
			Filehandling fh = new Filehandling();
			try {
				fh.readGameBoardFromURL(result.get());
				board.setPattern(fh.getFilePattern());
				viewer.render();
			} catch (IOException e) {
				Viewer.popupBox(AlertType.ERROR,
						"Error opening file",
						"Could not open file\n" + result.get(),
						e.toString());
			} catch (PatternFormatException e) {
				Viewer.popupBox(AlertType.ERROR,
						"Error opening RLE file",
						e.toString(), null);
			}
		}
	}
	
	/**
	 * <blockquote>
	 * <b><i>closeGame</i></b>
	 * <pre>{@code public void closeGame()}</pre>
	 * <p>Stops the sound played, and exists the platform the game is running on. </p>
	 *
	 * </blockquote>
	 */
	public void closeGame() {
		Platform.exit();
	}
	

	
	/**
	 * <blockquote>
	 * <b><i>cellColor</i></b>
	 * <pre>{@code public void cellColor()}</pre>
	 * <p>Lets you choose the cellColor, and updates the canvas. </p>
	 * </blockquote>
	 */
	public void cellColor() {
	    board.settings().setCellColor(colorPicker.getValue());
	    board.settings().update();
	    viewer.render();
	}
	
	/**
	 * <blockquote>
	 * <b><i>howToPlay</i></b>
	 * <pre>{@code public void howtoPlay()}</pre>
	 * <p>opens and displays a window with information about how to play the game.  </p> 
	 * </blockquote>
	 */
	public void howToPlay() {		
		GoLInfoBox.displayBox("How to Play",
				"\n\n1. Any living cell with fewer than two alive neighbours dies,  due to under-population.",
				"2. Any living cell with two or three living neighbours lives on to the next generation.",
				"3. Any living cell with more than three living neighbours dies, due to over-population.",
				"4. Any dead cell with exactly three living neighbours becomes a live cell,due to reproduction.",
				"",
				"Ok");
	}
	
	/**
	 * <blockquote>
	 * <b><i>aboutMenu</i></b>
	 * <pre>{@code public void aboutMenu()}</pre>
	 * <p>opens and displays a window with information about the game and the authors.</p> 
	 * </blockquote>
	 */
	public void aboutMenu() {
		GoLInfoBox.displayBox("About",
				"\n\nThis game is a school procjet made by",
				"Ali Arfan, Kent Erlend Bratteng Knudsen and Stian Tornholm Grimsgaard.",
				"", 
				"It is our implementation of John Conway's popular Game of Life.", 
				"",
				"Ok");
	}
	
	/**
	 * <blockquote>
	 * <b><i>controllersInfo</i></b>
	 * <pre>{@code public void controllersInfo()}</pre>
	 * <p>Opens and displays a window with information about the controllers of the game.</p> 
	 * </blockquote>
	 */
	public void controllersInfo() {
	    Stage primaryStage = new Stage();
	    primaryStage.setTitle("How To Play");
	    primaryStage.setMinWidth(600);
	    primaryStage.setMinHeight(400);
	    ImageView imageView = new ImageView();
	    Image image;
	    try {
		image = new Image(new FileInputStream(new File(System.getProperty("user.dir") + "/src/model/gol.png")));
		imageView.setImage(image);
	    } catch (FileNotFoundException e1) {
		System.out.print("Can't find file!");
		e1.printStackTrace();
	    }
	    
	    
	    Button button = new Button("Ok");
	    button.defaultButtonProperty().bind(button.focusedProperty());
	    button.setOnAction(e -> primaryStage.close());
	    
	    VBox layout = new VBox(10);
	    layout.setAlignment(Pos.CENTER);
	    layout.getChildren().addAll(imageView, button);
	    
	    Scene scene = new Scene(layout);
	    primaryStage.setScene(scene);
	    primaryStage.show();
	}
}