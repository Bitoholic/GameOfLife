package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Dynamic board, the board extends itself if the board is too small. 
 * 
 * @author Ali Arfan
 * @author Kent Erlend Knudsen
 * @author  Stian Tornholm Grimsgaard
 */
public class BoardDynamic extends Board {
	public static enum AddMode {
		ADD_ROWS,
		ADD_COLUMNS
	}

	private final int defaultColumns;
	private final int defaultRows;
	
	private List<List<Long>> board;
	
	// Since boardWidth can be Integer.MAX_VALUE * Long.SIZE, I have chosen the long datatype
	// for the variable. This is because one column element in the list represents 64 cells.
	private long boardWidth;
	private int boardHeight;
	

	/**
	 *
	 * <blockquote>
	 * <b><i>BoardDynamic</i></b>
	 * <pre>{@code public BoardDynamic(int columns, int rows)}</pre>
	 * <p> Sets the columns and rows according to the parameters, if they are greater than 1. Also creates a new gameboard.</p>
	 * @param columns - the columns to be set.
	 * @param rows - the rows to be set.
	 * @throws IllegalArgumentException - If the columns and rows are negative. 
	 * </blockquote>
	 * 
	 */
	
	public BoardDynamic(int columns, int rows) throws IllegalArgumentException {
		if(columns < 1 || rows < 1) {
			throw new IllegalArgumentException("Input parameters must be grater than 0.");
		}
		
		defaultColumns = columns;
		defaultRows = rows;
		
		board = createNewBoard();
	}
	
	/**
	 * <blockquote>
	 * <b><i>setBoard</i></b>
	 * <pre>{@code private List<List<Long>> createNewBoard()}</pre>
	 * <p>Creates a new board.</p>
	 * @return newBoard -  The list object to make the board.
	 * </blockquote>
	 */
	private List<List<Long>> createNewBoard() {
		List<List<Long>> newBoard = new ArrayList<>(new ArrayList<>());
		
		// One cells takes one bit and one long element is 64 bits wide, hence we divide board width by 64. 
		// We also round the number up so we have enough long elements in our array for the cells.
		int arrayWidth = (int) Math.ceil(defaultColumns / 64d);
		
		for(int j = 0; j < defaultRows; j++) {
			List<Long>row = new ArrayList<>();
			
			for(int i = 0; i < arrayWidth; i++) {
				row.add(0L);
			}
			
			newBoard.add(row);
		}
		
		boardWidth = defaultColumns;
		boardHeight = defaultRows;
		
		return newBoard;
	}
	/**
	 * <blockquote>
	 * <b><i>expandBoard</i></b>
	 * <pre>{@code public void expandBoard(AddMode addMode, int targetSize, long defaultValue)}</pre>
	 * <p>Extends the board. </p>
	 * @param addMode-  Add column or row
	 * @param  targetSize - The size of the new board. 
	 * @param defaultValue - The default value of new elements.
	 * </blockquote>
	 */
	
	public void expandBoard(AddMode addMode, int targetSize, long defaultValue) {
		int position = 0;
				
		if(addMode == AddMode.ADD_COLUMNS) {
			int arrayTarget = targetSize >> 6;
			int loop = arrayTarget*(-1);
			
			if(arrayTarget > board.get(0).size() - 1) {
				loop = 1 + arrayTarget - board.get(0).size();
				position = board.get(0).size();
			}
			
			for(int row = 0; row < board.size(); row++) {
				for(int col = 0; col < loop; col++) {
					board.get(row).add(position,defaultValue);
				}
			}
			
			boardWidth = board.get(0).size() * Long.SIZE;
		} else {
			int cols = board.get(0).size(); 
			int loop = targetSize*(-1);
			
			if(targetSize > board.size() - 1) {
				loop = 1 + targetSize - board.size();
				position = board.size();
			}
			
			for(int row = 0; row < loop; row++) {
				List<Long> newCol = new ArrayList<>();
				for(int col = 0; col < cols; col++) {
					newCol.add(defaultValue);
				}
				board.add(position,newCol);
			}
			boardHeight = board.size();
		}
	}
	
	/**
	 * 
	 *<blockquote>
	 * <b><i>setCellState</i></b>
	 * <pre>{@code public void setCellState(int x , int y, boolean cellState)}</pre>
	 * <p> Sets the state of the cell, where its either alive or dead on the given position.</p>
	 * @param x - The position of the cell, horizontal coordinate of the cell.
	 * @param y - The position of  the cell, vertical coordinate of the cell.
	 * @param cellState -  True if the cell is alive, else its dead. 
	 * </blockquote>
	 * 
	 */
	@Override
	public void setCellState(int x, int y, boolean cellState)  {
		if( x < 0 || x >= boardWidth || y < 0 || y >= boardHeight) {
			
			
			
			if(y < 0 || y >= boardHeight) {
				expandBoard(AddMode.ADD_ROWS, y, 0L);
				if(y < 0){ y = 0; }
				boardHeight = board.size();
			}
			if(x < 0 || x >= boardWidth) {
				expandBoard(AddMode.ADD_COLUMNS, x, 0L);
				if(x < 0) {
					x = Math.abs(x);
					x = (int) (Math.ceil(x / 64d)*Long.SIZE - x);
				}
				boardWidth = board.get(0).size() * Long.SIZE;
			}
		}

		int arrayPos = x >> 6;
		long data;
		
		data = board.get(y).get(arrayPos);
		board.get(y).set(arrayPos,setBit(data, x % Long.SIZE, cellState));
	}
	/**
	 * 
	 *<blockquote>
	 * <b><i>getCellState</i></b>
	 * <pre>{@code public long getCellState(int x , int y)}</pre>
	 * <p> Gets the state of the cell on the given position.</p>
	 * @param x - The position of the cell, horizontal coordinate of the cell.
	 * @param y - The position of  the cell, vertical coordinate of the cell.
	 * @return the cell state at the given position.
	 * </blockquote>
	 * 
	 */
	
	@Override
	public long getCellState(int x, int y) {
		int netBitX;
		int arrayPos;
		
		if( x > -1 && x < boardWidth && 
			y > -1 && y < boardHeight) {
			
			// Same as divide 64
			arrayPos = x >> 6;
			
			// Same as modulus 64
			netBitX = x - (arrayPos << 6);
			
			return getBit(board.get(y).get(arrayPos), netBitX);
		} else {
			return 0;
		}
	}
	
	/**
	 *<blockquote>
	 * <b><i>clearBoard</i></b>
	 * <pre>{@code public void clearBoard()}</pre>
	 * <p> Clears the board where all the cell states are set to dead. </p>
	 * </blockquote>
	 * 
	 */
	@Override
	public void clearBoard(){
		for(int j = 0; j < board.size(); j++) {
			for(int i = 0; i < board.get(j).size(); i++) {
				board.get(j).set(i,0L);
			}
		}
	}
	
	
	/**
	 *<blockquote>
	 * <b><i>getElement</i></b>
	 * <pre>{@code public long getElement(int columnx , int row)}</pre>
	 * <p> Gets the  element on the given position</p>
	 * @param column - The position of the cell on the column.
	 * @param row - The position of  the cell on which row. 
	 * @return returns the element at the given position.
	 * </blockquote>
	 */
	
	
	@Override
	public long getElement(int column, int row) {
		long data = 0;
		
		if( row > -1 && row < board.size() && 
			column > -1 && column < board.get(0).size()) {
		
			data = board.get(row).get(column);
		}
		
		return data;
	}

	/**
	 *<blockquote>
	 * <b><i>resetSize</i></b>
	 * <pre>{@code public void resetSize()}</pre>
	 * <p> This method is void in this class. </p>
	 * </blockquote>
	 */
	
	@Override
	public void resetSize() {
		board = createNewBoard();
	}
	/**
	 * <blockquote>
	 * <b><i>getBoardHeight</i></b>
	 * <pre>{@code public long getBoardHeight()}</pre>
	 * <p>Gets the board-height of the internal list.</p>
	 * @return bordHeight - Long value with the number of cell-rows.
	 * </blockquote>
	 */

	@Override
	public long getBoardHeight() {
		return boardHeight;
	}

	/**
	 * <blockquote>
	 * <b><i>getBoardWidth</i></b>
	 * <pre>{@code public long getBoardWidth()}</pre>
	 * <p>Gets the board-width of the internal list.</p>
	 * @return Long value with the number of cell-elements possible in one row.
	 * </blockquote>
	 */
	@Override
	public long getBoardWidth() {
		return boardWidth;
	}


	/**
	 * <blockquote>
	 * <b><i>getNumRows</i></b>
	 * <pre>{@code public long getNumRows()}</pre>
	 * <p>Gets the row-size of the internal list.</p>
	 * @return rows -Long value with the number of row-elements.
	 * </blockquote>
	 */
	
	@Override
	public long getNumRows() {
		return board.size();
	}

	/**
	 * <blockquote>
	 * <b><i>getNumColumns</i></b>
	 * <pre>{@code public long getNumColumns()}</pre>
	 * <p>Gets the numbers of columns in the internal list.</p>
	 * @return columns- Long value with the number of column-elements possible in one row.
	 * </blockquote>
	 */
	
	@Override
	public long getNumColumns() {
		return board.get(0).size();
	}

	/**
	 * <blockquote>
	 * <b><i>hasPosition</i></b>
	 * <pre>{@code 	public boolean hasPosition(int x, int y)}</pre>
	 * <p> Haspostion checks if the cordinate is inside its area.</p>
	 * @param x - The vertical cordinate of the cell.
	 * @param y - The horizontal cordinate of the cell.
	 * </blockquote>
	 */
	@Override
	public boolean hasPosition(int x, int y) {
		boolean arg = false;
		
		if(x < boardWidth && x > -1 && y < boardHeight && y > -1)
			arg = true;
		
		return arg;
	}

	@Override
	public String toString() {
	
		return null;
	}
}
