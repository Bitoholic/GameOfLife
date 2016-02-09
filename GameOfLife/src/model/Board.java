package model;

/**
 * An abstract class which contains the method to create a board. The board class allows you to 
 * change the state of the cell, and count its neighbors.
 * @see GameBoard
 * 
 * @author Ali Arfan
 * @author Kent Erlend Bratteng Knudsen
 * @author Stian Tornholm Grimsgaard 
 * 
 */

public abstract class Board {
    
	/**
	 * <blockquote>
	 * <b><i>setBit</i></b>
	 * <pre>{@code}public long setBit(long data, int pos, boolean value)</pre>
	 * <p> Sets the  bit value to either one or zero in the data on the given position.</p>
	 * @param data - The data the bit is to either be set to  one or zero.
	 * @param pos - the position of the bit in the is going to be set to one or zero.
	 * @param value - If the value is true the bit is set to one, else its set to zero.
	 * @return - 1 if the bit is set, otherwise 0.
	 * </blockquote>
	 */	
	public long setBit(long data, int pos, boolean value) {
		if(value) {
			data = data | (1L << pos);
		} else {
			data = data & ~(1L << pos);
		}
		return data;
	}
	
	
	/**
	 * <blockquote>
	 * <b><i>getBit</i></b>
	 * <pre>{@code}public long getBit(long data, int pos)</pre>
	 * <p> Gets the bit from the data on the given position.</p>
	 * @param data - The data the bit is returned from.
	 * @param pos - The position of the bit that is returned.
	 * @return - returns the bit at the specified position. 
	 * </blockquote>
	 */
	public long getBit(long data, int pos) {
		data = (data>> pos) & 1L; 
		return data;
	}
	
	
	/**
	 * <blockquote>
	 * <b><i>countNeighbours</i></b>
	 * <pre>{@code}public long countNeighbours(int x , int y)</pre>
	 * <p> Counts the neighbor of the cell on the given position.</p>
	 * @param x - The position of the cell, horizontal coordinate of the cell.
	 * @param y - The position of  the cell, vertical coordinate of the cell.
	 * @return - long value with the number of neighbours.
	 * </blockquote>
	 */
	
	public long countNeighbours(int x, int y) {
		long neighbours = 
				getCellState(x - 1, y - 1) + 
				getCellState(x    , y - 1) + 
				getCellState(x + 1, y - 1) +
				getCellState(x - 1,     y) + 
				getCellState(x + 1,     y) +
				getCellState(x - 1, y + 1) + 
				getCellState(x    , y + 1) + 
				getCellState(x + 1, y + 1);
		
		return neighbours;
	}
	
	
	/**
	 * 
	 *<blockquote>
	 * <b><i>setCellState</i></b>
	 * <pre>{@code}public abstract void setCellState(int x , int y, boolean cellState)</pre>
	 * <p> Sets the state of the cell, where its either alive or dead on the given position.</p>
	 * @param x - The position of the cell, horizontal coordinate of the cell.
	 * @param y - The position of  the cell, vertical coordinate of the cell.
	 * @param cellState -  True if the cell is alive, else its dead. 
	 * </blockquote>
	 * 
	 */
	
	public abstract void setCellState(int x, int y, boolean cellState);
	
	
	/**
	 * 
	 *<blockquote>
	 * <b><i>getCellState</i></b>
	 * <pre>{@code}public abstract long getCellState(int x , int y)</pre>
	 * <p> Gets the state of the cell on the given position.</p>
	 * @param x - The position of the cell, horizontal coordinate of the cell.
	 * @param y - The position of  the cell, vertical coordinate of the cell.
	 * @return the cell state at the given position.
	 * </blockquote>
	 * 
	 */
	
	public abstract long getCellState(int x, int y);
	
	
	/**
	 * 
	 *<blockquote>
	 * <b><i>getElement</i></b>
	 * <pre>{@code}public abstract long getElement(int columnx , int row)</pre>
	 * <p> Gets the  element on the given position</p>
	 * @param column - The position of the cell on the column.
	 * @param row - The position of  the cell on which row. 
	 * @return - long value of the given element.
	 * </blockquote>
	 * 
	 */
	public abstract long getElement(int column, int row);
	
	
	/**
	 * <blockquote>
	 * <b><i>getBoardHeight</i></b>
	 * <pre>{@code}public abstract long getBoardHeight()</pre>
	 * <p>Gets the board-height of the internal list.</p>
	 * @return boardHeight - Long value with the number of cell-rows.
	 * </blockquote>
	 */
	
	public abstract long getBoardHeight();
	
	
	/**
	 * 
	 * <blockquote>
	 * <b><i>getBoardWidth</i></b>
	 * <pre>{@code}public abstract long getBoardWidth()</pre>
	 * <p>Gets the board-width of the internal list.</p>
	 * @return Long value with the number of cell-elements possible in one row.
	 * </blockquote>
	 * 
	 */
	
	public abstract long getBoardWidth();
	
	
	/**
	 * 
	 * <blockquote>
	 * <b><i>getNumRows</i></b>
	 * <pre>public abstract long getNumRows()</pre>
	 * <p>Gets the row-size of the internal list.</p>
	 * @return rows -Long value with the number of row-elements.
	 * </blockquote>
	 * 
	 */
	
	public abstract long getNumRows();
	
	/**
	 * <blockquote>
	 * <b><i>getNumColumns</i></b>
	 * <pre>{@code}public abstract long getNumColumns()</pre>
	 * <p>Gets the numbers of columns in the internal list.</p>
	 * @return columns- Long value with the number of column-elements possible in one row.
	 * </blockquote>
	 */
	
	public abstract long getNumColumns();
	
	
	/**
	 * <blockquote>
	 * <b><i>hasPosition</i></b>
	 * <pre>{@code public abstract boolean hasPosition(int x, int y); }</pre>
	 * <p>Checks if the coordinates given is accepted by the board.</p>
	 * @param x - horizontal coordinate
	 * @param y - vertical coordinate
	 * @return boolean value of the acquired position 
	 * </blockquote>
	 * 
	 */
	
	public abstract boolean hasPosition(int x, int y);
	
	
	/**
	 *<blockquote>
	 * <b><i>clearBoard</i></b>
	 * <pre>{@code}public abstract void clearBoard()</pre>
	 * <p> Clears the board where all the cell states are set to dead. </p>
	 * </blockquote>
	 */
	public abstract void clearBoard();
	
	
	/**
	 * 
	 *<blockquote>
	 * <b><i>resetSize</i></b>
	 * <pre>{@code}public abstract void resetSize()</pre>
	 * <p> Resets the size of the board, and sets it to the boards default size. </p>
	 * </blockquote>
	 * 
	 */
	
	public abstract void resetSize();
	
	
	
	/**
	 * 
	 *<blockquote>
	 * <b><i>toString</i></b>
	 * <pre>{@code}public abstract {@link String} toString()</pre>
	 * <p> Represents the board in a String format.</p>
	 * @return toString
	 * </blockquote>
	 * 
	 */
	
	public abstract String toString();
	
}