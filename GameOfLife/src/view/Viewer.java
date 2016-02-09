package view;

import java.util.Observable;
import java.util.Observer;

import model.BoardSettings;
import model.GameBoard;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

/**
 * The Viewer class allows to draw rectangles on a specified canvas.
 * It implements {@link Observer} and {@link ViewerCenterBoard}.
 * 
 * 
 * @author Ali Arfan
 * @author Kent Erlend Bratteng Knudsen
 * @author Stian Tornholm Grimsgaard
 * 
 */

public class Viewer implements Observer, ViewerCenterBoard {
	private double cellBoarderSize = 1;
	
	private Canvas canvas;
	private GraphicsContext gc;
	
	private double cellSizeInner;
	private double cellSizeOuter;
	
	private double offsetLeft = 0;
	private double offsetTop = 0;
	
	private Affine xform;
	private GameBoard board;
	
	private Color boardGridColor;
	private Color boardBorderColor;
	private Color boardCellColor;
	
	private double blur = 0.0d;
	
	private ViewerListener o;
	
	
	/**
	 * <blockquote>
	 * <b><i>Viewer</i></b>
	 * <pre>{@code}public void Viewer({@link Canvas} canvas, {@link GameBoard} board)</pre>
	 * <p>The constructor takes a canvas and a game board in it's parameters.</p>
	 * 
	 * @param canvas - The canvas to be viewed.
	 * @param board - The game board to be viewer.
	 * @throws NullPointerException
	 * </blockquote>
	 */
	public Viewer(Canvas canvas, GameBoard board) throws NullPointerException {
		if(canvas == null || board == null) {
			throw new NullPointerException("Parameters cannot be null.");
		}
		
		xform = new Affine();
		
		gc = canvas.getGraphicsContext2D();
		
		this.canvas = canvas;
		this.board = board;
		
		o = new ViewerListener(canvas, this);
	}
	
	
	/**
	 * <blockquote>
	 * <b><i>centerView</i></b>
	 * <pre>{@code}public void centerView()</pre>
	 * <p>Centers the game-board on the canvas.</p>
	 * </blockquote>
	 */
	public void centerView() {
		double boardWidth = board.getWidth() * board.settings().getCellSizeOuter();
		double boardHeight = board.getHeight() * board.settings().getCellSizeOuter();
		double offsetLeft = (canvas.getWidth() + boardWidth)/2 - boardWidth;
		double offsetTop = (canvas.getHeight() + boardHeight)/2 - boardHeight;
		
		board.settings().setOffsetLeft(-offsetLeft);
		board.settings().setOffsetTop(-offsetTop);
		board.settings().update();
	}
	
	
	/**
	 * 
	 * <blockquote>
	 * <b><i>update</i></b>
	 * <pre>{@code}public void update({@link Observable} o, {@link Object} arg)</pre>
	 * <p>Updates the board, every time an observable notifies observer</p>
	 * 
	 * @param o - The class that notifies the observer
	 * @param arg - A object can be sent together with observer.
	 * </blockquote>
	 * 
	 */
	
	@Override
	public void update(Observable o, Object arg) {
		BoardSettings bs = (BoardSettings) o;
		
		cellSizeInner = bs.getCellSizeInner();
		cellSizeOuter = bs.getCellSizeOuter();
		cellBoarderSize = bs.getCellBorderSize();
		
		offsetLeft = bs.getOffsetLeft();
		offsetTop = bs.getOffsetTop();
		
		boardGridColor   = bs.getGridColor();
		boardBorderColor = bs.getBoardBorderColor();
		boardCellColor   = bs.getCellColor();
	}
	
	/**
	 * <blockquote>
	 * <b><i>setBlur</i></b>
	 * <pre>{@code}public void setBlur({@link Boolean} blur)</pre>
	 * <p>Draws a grid over the canvas.</p>
	 * 
	 * @param blur - sets blur if true.
	 * </blockquote>
	 */
	public void setBlur(boolean blur) {
		if(blur) {
			this.blur = 0.0d;
		} else {
			this.blur = 0.5d;
		}
	}
	
	/**
	 * 
	 * <blockquote>
	 * <b><i>render</i></b>
	 * <pre>{@code}public void render()</pre>
	 * <p>Draws the cells on the canvas.</p>
	 * </blockquote>
	 * 
	 */
	public void render() {
		int startX = (int) (offsetLeft / cellSizeOuter);
		int startY = (int) (offsetTop  / cellSizeOuter);
		int endX = startX + (int) Math.ceil(canvas.getWidth()  / cellSizeOuter) + 1;
		int endY = startY + (int) Math.ceil(canvas.getHeight() / cellSizeOuter) + 1;
		
		xform.setTx(-offsetLeft);
		xform.setTy(-offsetTop);
		gc.setTransform(xform);

		gc.clearRect(offsetLeft, offsetTop, canvas.getWidth(), canvas.getHeight());
		
		renderGrid();
		
		gc.setStroke(boardBorderColor);
		gc.strokeRect(0, 0, board.getWidth()*cellSizeOuter, board.getHeight()*cellSizeOuter);
		
		gc.setFill(boardCellColor);
		
		for(int j = startY; j <  endY; j++) {
			for(int i = startX; i <  endX; i++) {
				
				if(board.getCellState(i, j)) {
					gc.fillRect(
							cellBoarderSize + i * cellSizeOuter,
							cellBoarderSize + j * cellSizeOuter,
							cellSizeInner,
							cellSizeInner);
				}
			}
		}
	}
	
	/**
	 * 
	 * <blockquote>
	 * <b><i>renderGrid</i></b>
	 * <pre>{@code}public void renderGrid()</pre>
	 * <p>Draws a grid over the canvas.</p>
	 * </blockquote>
	 * 
	 */
	public void renderGrid() {
		int mislocX = (int) ((offsetLeft / cellSizeOuter) - cellSizeOuter);
		int mislocY = (int) ((offsetTop / cellSizeOuter) - cellSizeOuter);

		int rx = (int) ((canvas.getWidth() / cellSizeOuter) + cellSizeOuter * 2);
		int ry = (int) ((canvas.getHeight() / cellSizeOuter) + cellSizeOuter * 2);
		
		gc.setStroke(boardGridColor);
		
		for(double i = mislocX; i < mislocX + rx; i++) {
			gc.strokeLine(
					i * cellSizeOuter + blur, 
					mislocY * cellSizeOuter, 
					i * cellSizeOuter + blur,
					(mislocY + ry) * cellSizeOuter);
		}

		for(double i = mislocY; i < mislocY + ry; i++) {
			gc.strokeLine(
					mislocX * cellSizeOuter, 
					i * cellSizeOuter + blur, 
					(mislocX + rx) * cellSizeOuter, 
					i * cellSizeOuter + blur);
		}
	}
	
	
	/**
	 * 
	 * <blockquote>
	 * <b><i>popupBox</i></b>
	 * <pre>{@code}public static void popupBox({@link AlertType} alertType, {@link String} title, String headerText, String contentText)</pre>
	 * <p>Displays a new JavaFX {@link Alert } dialogue to the user.</p>
	 * 
	 * @param alertType - pre-built value from the <code>AlertType</code> enum.
	 * @param title - title text of the alert dialogue.
	 * @param headerText - header text of the alert dialogue.
	 * @param contentText - content text of the alert dialogue.
	 * </blockquote> 
	 * 
	 */
	public static void popupBox(AlertType alertType, String title, String headerText, String contentText) {
		Alert alert = new Alert(alertType);
		
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(contentText);
		
		alert.showAndWait();
	}


	@Override
	public void viewsizeContext(ViewerListener obj) {
		centerView();
		
		canvas.widthProperty().removeListener(o.listenerWidth());
		canvas.heightProperty().removeListener(o.listenerHeight());

	}
}