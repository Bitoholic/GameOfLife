package model;

import java.util.Arrays;

/**
 * This class represents the static gameboard. It extends {@link Board} 
 * @author Ali Arfan
 * @author Kent Erlend Bratteng Knudsen
 * @author Stian Tornholm Grimsgaard
 *
 */
public class BoardStatic extends Board {

	private long[][] board;
	
	// Since boardWidth can be Integer.MAX_VALUE * Long.SIZE, I have chosen the long datatype
	// for the variable. This is because one column element in the list represents 64 cells.
	// The variables are final because this board is static, hence they don't need to change.
	private final long boardWidth;
	private final int boardHeight;
	
	/**
	 *
	 * <blockquote>
	 * <b><i>BoardStatic</i></b>
	 * <pre>{@code public BoardStatic(int columns, int rows)}</pre>
	 * <p> Sets the columns and rows according to the parameters, if they are greater than 1. Also creates a new gameboard.</p>
	 * @param columns - the columns to be set.
	 * @param rows - the rows to be set.
	 * @throws IllegalArgumentException - If the columns and rows are negative. 
	 * </blockquote>
	 * 
	 */
	
	public BoardStatic(int columns, int rows) throws IllegalArgumentException {
		if(columns < 1 || rows < 1) {
			throw new IllegalArgumentException("Input parameters must be grater than 0.");
		}
		
		boardWidth = columns;
		boardHeight = rows;
		
		// One cells takes one bit and one long element is 64 bits wide, hence we divide board width by 64. 
		// We also round the number up so we have enough long elements in our array for the cells.
		board = new long[rows][(int) Math.ceil(columns / 64d)];
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
	public void setCellState(int x, int y, boolean cellState) {
		int netBitX;
		int arrayPos;
		long data;
		
		if( x > -1 && x < boardWidth && 
			y > -1 && y < boardHeight) {	
			
			arrayPos = x >> 6;
			netBitX = x - (arrayPos << 6);
			
			data = board[y][arrayPos]; 
			
			board[y][arrayPos] = setBit(data, netBitX, cellState);
		}
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
	
	public long getCellState(int x, int y) {
		int netBitX;
		int arrayPos;
		
		if( x > -1 && x < boardWidth && 
			y > -1 && y < boardHeight) {	
			
			// Same as divide 64
			arrayPos = x >> 6;
			
			// Same as modulus 64
			netBitX = x - (arrayPos << 6);

			return getBit(board[y][arrayPos], netBitX);
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
	
	public void clearBoard(){
		for(int i = 0; i < board.length; i++) {
			Arrays.fill(board[i], 0);
		}
	}

	/**
	 * 
	 *<blockquote>
	 * <b><i>toString</i></b>
	 * <pre>{@code public {@link String} toString()}</pre>
	 * <p> Represents the board in a String format.</p>
	 * @return toString
	 * </blockquote>
	 * 
	 */
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		
		for(int j = 0; j < board.length; j++) {
			buffer.append("[Row:" + j + "]    ");
			for(int i = 0; i < board[j].length * Long.SIZE; i++) {
				if(getCellState(i , j) == 1) {
					if(i < 10) {
						buffer.append("[x]");
					} else {
						buffer.append("[x]");
					}
				} else {
					if(i < 10) {
						buffer.append("[ ]");
					} else {
						buffer.append("[ ]");
					}
				}
			}
			buffer.append("\n");
		}
		
		return buffer.toString();
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
		
		if(row > -1 && row < board.length &&
			column > -1 && column < board[0].length) {
			data = board[row][column];
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
		// Not used in this class
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
		return board.length;
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
		return board[0].length;
	}

	/**
	 * <blockquote>
	 * <b><i>hasPosition</i></b>
	 * <pre>{@code public boolean hasPosition(int x, int y); }</pre>
	 * <p>Checks if the coordinates given is accepted by the board.</p>
	 * @param x - horizontal coordinate
	 * @param y - vertical coordinate
	 * @return true
	 * </blockquote>
	 */
	@Override
	public boolean hasPosition(int x, int y) {
		return true;
	}


	/**
	 * <blockquote>
	 * <b><i>setBoard</i></b>
	 * <pre>{@code public void setBoard(long[][] board); }</pre>
	 * <p>Sets the board</p>
	 * @param board - the bord to be set.
	 * </blockquote>
	 */
	public void setBoard(long[][] board) {
	    this.board=board;
	    
	}
}
