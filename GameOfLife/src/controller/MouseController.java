package controller;

import model.GameBoard;


import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

/**
 * This is the controller of the Mouse events, every action on the mouse is registered, and
 * Handled here. The MouseController is initialized in the {@link MainController}. 
 * 
 * @author Ali Arfan
 * @author Kent Erlend Bratteng Knudsen
 * @author Stian Tornholm Grimsgaard 
 * 
 */

public class MouseController {
    		
	private GameBoard board;
	
	private boolean boardCellState = false;
	
	private double boardCellSize;
	private double boardOffsetTop;
	private double boardOffsetLeft;
	
	private double prevMouseX;
	private double prevMouseY;

	private Number columnClicked;
	private Number rowClicked;
	private Number prevColumnClicked;
	private Number prevRowClicked;
	
	private double boardWidth;
	private double boardHeight;
	
	private double ratio_width;
	private double ratio_height;
	
	private double current_width;
	private double current_height;
	
	private boolean mouseDrawing = false;
	public static boolean soundMuted;
		
	/**
	 * <blockquote>
	 * <b><i>MouseController</i></b>
	 * <pre>{@code public MouseController(}{@link GameBoard} board)</pre>
	 * <p>Creates an instance of the MouseController attached to a GameBoard object</p>
	 * 
	 * @param board - the GameBoard object.
	 * @throws NullPointerException - Throws an exception if no GameBoard object is null reference.
	 * </blockquote>
	 */
	
	public MouseController(GameBoard board) throws NullPointerException {
		if(board == null) {
			throw new NullPointerException("Parameter's references cannot be null.");
		}
		
		this.board = board;
		getBoardValues();
	}
	
	/**
	 * <blockquote>
	 * <b><i>getBoardValues</i></b>
	 * <pre>{@code}private void getBoardValues()</pre>
	 * <p> Gets the offsets for the top-left corner. The offsets are the distance between the board and the canvas.
	 * There values are pulled from the GameBoard object.</p>
	 * </blockquote>
	 * 
	 */
	private void getBoardValues() {
		boardOffsetLeft = board.settings().getOffsetLeft();
		boardOffsetTop = board.settings().getOffsetTop();
		boardCellSize = board.settings().getCellSizeOuter();
	}
	
	/**
	 * <blockquote>
	 * <b><i>mouseMap</i></b>
	 * <pre>{@code}private mouseMap({@link MouseEvent} me)</pre>
	 * <p>Internal method for handling which cell on the GameBoard object was clicked.</p>
	 * 
	 * @param me - MouseEvent to be delivered.
	 * </blockquote>
	 * 
	 */
	private void mouseMap(MouseEvent me) {
	    prevColumnClicked = columnClicked;
	    prevRowClicked = rowClicked;
		columnClicked = Math.floor((me.getX() + boardOffsetLeft) / boardCellSize);
		rowClicked = Math.floor((me.getY() + boardOffsetTop) / boardCellSize);
	}
	
	/**
	 * <blockquote>
	 * <b><i>scrollZoom</i></b>
	 * <pre>{@code}public scrollZoom({@link ScrollEvent} se)</pre>
	 * <p> Receives an scroll event and handles the offsets accordingly on the GameBoard object
	 * for the zoom to be performed correctly.</p>
	 * <p>Values outside the minValue and maxValue are ignored.</p>
	 * @param se - ScrollEvent sending the produced gesture.
	 * @param minValue - min value this method will allow.
	 * @param maxValue - max value this method will allow.
	 * </blockquote>
	 * 
	 */
	public void scrollZoom(ScrollEvent se, double minValue, double maxValue) {
		getBoardValues();
		
		// Just to make the increment in zoom finer.
		double zoomDelta = se.getDeltaY() / se.getMultiplierY() / 5;
		double cellSizeInner = board.settings().getCellSizeInner();

		
		if(cellSizeInner > minValue && (cellSizeInner + zoomDelta) > minValue &&
			cellSizeInner <= maxValue && (cellSizeInner + zoomDelta) < maxValue) {
			// boardWidth and boardHeight holds the number of cells the board can
			// currently hold in for each direction.
			boardWidth = board.getWidth();
			boardHeight = board.getHeight();
			
			// We calculate the ratio between  mouse-position and board size in pixels.
			// Since mouse-position is relative to its place on the canvas, we must add
			// the board's offset.
			ratio_width  = (boardOffsetLeft + prevMouseX) / (boardWidth * boardCellSize);
			ratio_height = (boardOffsetTop + prevMouseY) / (boardHeight * boardCellSize);
			
			// A zoom event has been called and we need to change the cell-size accordingly.
			// zoomDelta holds the incremental change we want to do on the cell-size.
			// We send the new cell-size back to the board.settings(). This is because the 
			// border-size between the cells changes when the cells become smaller than one pixel.
			cellSizeInner += zoomDelta;
			board.settings().setCellSizeInner(cellSizeInner);
			boardCellSize = board.settings().getCellSizeOuter();
			
			// Because cell-size now is changed, we need to re-calculate the new image size so
			// we have the correct offset when the image is rendered again.
			current_width = boardWidth * boardCellSize;
			current_height = boardHeight * boardCellSize;
			
			boardOffsetLeft = (ratio_width  * current_width ) - prevMouseX;
			boardOffsetTop = (ratio_height * current_height) - prevMouseY;
			
			// We save the new offsets and cell-size and tells the observers about the update.
			board.settings().setOffsetLeft(boardOffsetLeft);
			board.settings().setOffsetTop(boardOffsetTop);
			board.settings().setCellSizeInner(cellSizeInner);
			board.settings().update();
		}
	}
	
	
	/**
	 * <blockquote>
	 * <b><i>mouseMoved</i></b>
	 * <pre>{@code}public mouseMoved({@link MouseEvent} me)</pre>
	 * <p>Registers mouse movement.</p>
	 * @param me - the mouse event that will be used to capture the movement.
	 * </blockquote>
	 * 
	 */
	public void mouseMoved(MouseEvent me) {
		prevMouseX = me.getX();
		prevMouseY = me.getY();
	}
	
	
	/**
	 * <blockquote>
	 * <b><i>mouseButtonReleased</i></b>
	 * <pre>{@code}public mouseButtonRelased({@link MouseEvent} me)</pre>
	 * <p> Receives an MouseEvent when the mouse button is released, and stops drawing cells.</p>
	 * @param me - The mouseEvent  of when the left mouse button is released.
	 * </blockquote>
	 * 
	 */
	public void mouseButtonReleased(MouseEvent me) {
		getBoardValues();
		mouseMap(me);

		
		if(me.getButton() == MouseButton.PRIMARY && mouseDrawing == false) {
			boardCellState = !(board.getCellState(columnClicked.intValue(), rowClicked.intValue()));
			board.setCellState(columnClicked.intValue(), rowClicked.intValue(), boardCellState);
			
			
		}
		
		mouseDrawing = false;
	}
	
	
	/**
	 * <blockquote>
	 * <b><i>mouseDragged</i></b>
	 * <pre>{@code}public mouseDragged({@link MouseEvent} me)</pre>
	 * <p> Receives an MouseEvent when the right mouse button is clicked down, and dragged.</p>
	 * @param me - The mouseEvent of  when the right mouse button is clicked and the mouse is dragged.
	 * </blockquote>
	 * 
	 */
	public void mouseDragged(MouseEvent me) {
		boolean curBoardCellState;
		
		getBoardValues();
		mouseMap(me);
		
		switch(me.getButton()) {
			default:
			case PRIMARY:
				if(!mouseDrawing) {
					mouseDrawing = true;
					boardCellState = !(board.getCellState(columnClicked.intValue(), rowClicked.intValue()));
				}
				
				curBoardCellState = board.getCellState(columnClicked.intValue(), rowClicked.intValue());
				board.setCellState(columnClicked.intValue(), rowClicked.intValue(), boardCellState);
			

				break;
			case SECONDARY:
				
				if(!mouseDrawing) {
					mouseDrawing = true;
					prevMouseX = me.getX();
					prevMouseY = me.getY();
				} else {
					boardOffsetLeft += (prevMouseX - me.getX());
					boardOffsetTop += (prevMouseY - me.getY());
					
					board.settings().setOffsetLeft(boardOffsetLeft);
					board.settings().setOffsetTop(boardOffsetTop);
					board.settings().update();
					
					prevMouseX = me.getX();
					prevMouseY = me.getY();
				}
				break;
		}
	}
}
