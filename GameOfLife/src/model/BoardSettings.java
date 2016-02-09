


package model;

import java.util.Observable;

import javafx.scene.paint.Color;
/**
 * Boardsettings sets the settings of the active board, where the offsets,cell sizes and colors are defined. The offsets 
 * variables are the context between the canvas and the board, and the cell sizes is the size of each cell shown on the board.
 * Boardsettings extends {@link Observable} 
 * 
 * 
 * @author Ali Arfan
 * @author Kent Erlend Bratteng Knudsen
 * @author  Stian Tornholm Grimsgaard
 */
public class BoardSettings extends Observable{
	
	private double defaultCellSize;
	private double cellSizeInner;
	private double cellBorderSize;
	private double cellSizeOuter;
	
	private double offsetLeft = 0;
	private double offsetTop = 0;
	
	private static Color boardCellColor = Color.GREENYELLOW;
	private static Color boardGridColor = Color.color(0.075, 0.085, 0.095);
	private static Color boardBgColor = Color.BLACK;
	private static Color boardBorderColor = Color.YELLOWGREEN;
	
	/**
	 * <blockquote>
	 * <b><i>BoardSettings</i></b>
	 * <pre>{@code public BoardSettings()} </pre>
	 * <p>The constructor, where the inner cell size is set to the default cell size 5.</p>
	 * </blockquote>
	 */
	public BoardSettings() {
		defaultCellSize = 5d;
		setCellSizeInner(defaultCellSize);
	}
	
	/**
	 *
	 *<blockquote>
	 * <b><i>BoardSettings</i></b>
	 * <pre>{@code public double getDefaultCellSize()}</pre>
	 * <p>The default cell size is returned.</p>
	 * @return defaultCellSize -  The  value of the default cell size. 
	 * </blockquote>
	 * 
	 */
	public double getDefaultCellSize() {
		return defaultCellSize;
	}
	
	/**
	 *
	 *<blockquote>
	 * <b><i>setDefaultCellSize</i></b>
	 * <pre>{@code public void setDefaultCellSize(double defaultCellSize)}</pre>
	 * <p>The default cell size is set  as the given parameter, values  smaller than 0 is
	 * ignored.</p>
	 * @param defaultCellSize - The value that is to be set to the default cell size.
	 * </blockquote>
	 *
	 */
	
	public void setDefaultCellSize(double defaultCellSize) {
		if(defaultCellSize > 0d) {
			this.defaultCellSize = defaultCellSize;
			setCellSizeInner(defaultCellSize);
		}
	}
	
	/**
	 * 
	 * <blockquote>
	 * <b><i>resetCellSize</i></b>
	 * <pre>{@code public void resetCellSize() }</pre>
	 * <p>The inner cell size is set to the current default cell size. </p>
	 * </blockquote>
	 * 
	 */
	
	public void resetCellSize() {
		setCellSizeInner(defaultCellSize);
	}
	

	/**
	 * 
	 * <blockquote>
	 * <b><i>getCellBorderSize</i></b>
	 * <pre>{@code public double getCellBorderSize() }</pre>
	 * <p> Returns the border size of the cell.</p>
	 * @return cellBorderSize - The border cell size value.
	 * </blockquote>
	 * 
	 */
	
	public double getCellBorderSize() {
		return cellBorderSize;
	}
	
	/**
	 * 
	 * <blockquote>
	 * <b><i>getCellSizeOuter</i></b>
	 * <pre>{@code public double getCellSizeOuter() }</pre>
	 * <p> Returns the outer cell size.</p>
	 * @return cellSizeOuter - The outer cell size value.
	 * </blockquote>
	 * 
	 */
	public double getCellSizeOuter() {
		return cellSizeOuter;
	}
	
	/**
	 * 
	 * <blockquote>
	 * <b><i>setCellSizeInner</i></b>
	 * <pre>{@code public void setCellSizeInner(double cellSizeInner) }</pre>
	 * <p> Sets the inner cell size.</p>
	 * @param cellSizeInner - The value that is to be set as the inner cell size.
	 * @throws IllegalArgumentException - An illegal argument is thrown when the parameter value is less than 0.
	 * </blockquote>
	 * 
	 */
	public void setCellSizeInner(double cellSizeInner) throws IllegalArgumentException {
		if(cellSizeInner < 0) {
			throw new IllegalArgumentException("cellSize must be greater than 0.");
		} else {
			if(cellSizeInner >= 1d) {
				cellBorderSize = 1d;
			} else {
				cellBorderSize = cellSizeInner * cellSizeInner;
			}
			
			this.cellSizeInner = cellSizeInner;
			cellSizeOuter = cellSizeInner + cellBorderSize;
		}
	}
	

	/**
	 * 
	 * <blockquote>
	 * <b><i>getCellSizeInner</i></b>
	 * <pre>{@code public void getCellSizeInner() }</pre>
	 * <p> Returns the inner cells size.</p>
	 * @return cellSizeInner - The value of the current inner cell size.
	 * </blockquote>
	 * 
	 */
	public double getCellSizeInner() {
		return cellSizeInner;
	}
	
	/**
	 * 
	 * <blockquote>
	 * <b><i>setOffsetLeft</i></b>
	 * <pre>{@code public void setOffsetLeft(double offsetLeft)}</pre>
	 * <p> Sets  the   left offset the distance between the canvas and the board from the left.</p>
	 * @param offsetLeft - The  value of the offset to be set.
	 * </blockquote>
	 * 
	 */
	public void setOffsetLeft(double offsetLeft) {
		this.offsetLeft = offsetLeft; 
	}
	
	/**
	 * 
	 * <blockquote>
	 * <b><i>getOffsetLeft</i></b>
	 * <pre>{@code public void getOffsetLeft()}</pre>
	 * <p> Returns the current distance between the board and the canvas from the left .</p>
	 * @return offsetLeft - Returns the left offset value. 
	 * </blockquote>
	 * 
	 */
	public double getOffsetLeft() {
		return offsetLeft;
	}
	
	/**
	 * 
	 * <blockquote>
	 * <b><i>setOffsetTop</i></b>
	 * <pre>{@code public void setOffsetTop(double offsetTop)}</pre>
	 * <p> Sets  the   top offset the distance between the canvas and the board from the top.</p>
	 * @param offsetTop - The  value of the offset to be set.
	 * </blockquote>
	 * 
	 */
	public void setOffsetTop(double offsetTop) {
		this.offsetTop = offsetTop;
	}
	
	/**
	 * 
	 * <blockquote>
	 * <b><i>getOffsetLeft</i></b>
	 * <pre>{@code public void getOffsetLeft()}</pre>
	 * <p> Returns the current distance between the board and the canvas from the left .</p>
	 * @return offsetLeft - Returns the left offset value. 
	 * </blockquote>
	 * 
	 */
	public double getOffsetTop() {
		return offsetTop;
	}
	
	/**
	 * 
	 * <blockquote>
	 * <b><i>setCellColor</i></b>
	 * <pre>{@code public void setCellColor({@link Color} cellColor)}</pre>
	 * <p>Sets the color of the cell.</p>
	 * @param cellColor - The color to be set as the cell color.  
	 * </blockquote>
	 * 
	 */
	public void setCellColor(Color cellColor) {
		boardCellColor = cellColor;
	}
	
	/**
	 * 
	 * <blockquote>
	 * <b><i>getCellColor</i></b>
	 * <pre>{@code  public {@link Color} getCellColor()}</pre>
	 * <p> Returns the current  color of the cells.</p>
	 * @return boardCellColor - The color of the cells on the board.
	 * </blockquote>
	 * 
	 */
	public Color getCellColor() {
		return boardCellColor;
	}
	
	/**
	 * 
	 * <blockquote>
	 * <b><i>setGridColor</i></b>
	 * <pre>{@code public void setGridColor({@link Color} gridColor)}</pre>
	 * <p> Sets  the  color of the grid. </p>
	 * @param gridColor - The color to be set as the grid color.
	 * </blockquote>
	 * 
	 */
	public void setGridColor(Color gridColor) {
		boardGridColor = gridColor;
	}
	
	/**
	 * 
	 * <blockquote>
	 * <b><i>getGridColor</i></b>
	 * <pre>{@code  public {@link Color} getGridColor()}</pre>
	 * <p> Returns the current  color of the grid.</p>
	 * @return boardCellColor - The color of the cells on the grid.
	 * </blockquote>
	 * 
	 */
	public Color getGridColor() {
		return boardGridColor;
	}
	
	/**
	 * 
	 * <blockquote>
	 * <b><i>setBgColor</i></b>
	 * <pre>{@code public void setBgColor({@link Color} bgColor)}</pre>
	 * <p> Sets the color of background.</p>
	 * @param bgColor - The color to be set as the background color.
	 * </blockquote>
	 * 
	 */
	public void setBgColor(Color bgColor) {
		boardBgColor = bgColor;
	}

	/**
	 * 
	 * <blockquote>
	 * <b><i>getGridColor</i></b>
	 * <pre>{@code  public {@link Color} getBgColor()}</pre>
	 * <p> Returns the current  color of the  background.</p>
	 * @return boardCellColor - The color of the background on the board.
	 * </blockquote>
	 * 
	 */
	public Color getBgColor() {
		return boardBgColor;
	}
	
	/**
	 * 
	 * <blockquote>
	 * <b><i>setBoardBorderColor</i></b>
	 * <pre>{@code public void setBgColor({@link Color} borderColor)}</pre>
	 * <p> Sets  the  color of   border of the active board, the  border is shown around the board. The border changes
	 * size as the board resizes.</p>
	 * @param borderColor - The color to be set as the border color.
	 * </blockquote>
	 * 
	 */
	public void setBoardBorderColor(Color borderColor) {
		boardBorderColor = borderColor;
	}
	
	/**
	 * 
	 * <blockquote>
	 * <b><i>getBoardBorderColor</i></b>
	 * <pre>{@code  public {@link Color} getBoardBorderColor()}</pre>
	 * <p> Returns the current  color of the board..</p>
	 * @return boardBorderColor - The color of the border on the board.
	 * </blockquote>
	 * 
	 */
	public Color getBoardBorderColor() {
		return boardBorderColor;
	}
	
	/**
	 * 
	 * <blockquote>
	 * <b><i>update</i></b>
	 * <pre>{@code  public void update() }</pre>
	 * <p> sets the changes, and notifies {@link BoardObservable}
	 * </p>
	 * </blockquote>
	 * 
	 */
	public void update() {
		setChanged();
		notifyObservers();
	}
}
