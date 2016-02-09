package model;


/**
 * The GameBoard class holds the information about the game board, as well as do some operations on the board, such as 
 *  {@link GameBoard#nextGeneration()}, and sets the cell states to either dead or alive. 
 * 
 * 
 * @author Ali Arfan
 * @author  Kent Erlend Bratteng Knudsen
 * @author  Stian  Tornholm Grimsgaard
 *
 */
public class GameBoard  {
	
	private final int MAX_THREADS = Runtime.getRuntime().availableProcessors();
	
	private BoardSettings boardSettings;
	private GameRules gr;
	
	private Board curGenBoard;
	private Board newGenBoard;
	private Board oldGenBoard;
	
	private Board curActiveCells;
	private Board newActiveCells;
	private Board oldActiveCells;
	
	private long generationNo;
	
	private int boardTopMisplaceX;
	private int boardTopMisplaceY;
	private int boardTopMisplaceXb;
	private int boardTopMisplaceYb;
	
	private Thread threads[];
	private BoardThread threadJob[];
	

	/**
	 * <blockquote>
	 * <b><i>GameBoard</i></b>
	 * <pre>{@code public GameBoard(int columns, int rows)}</pre>
	 * <p> The constructor of the class, creates a board with given rows and columns. Cells alive and  Generation number is
	 * 0 when the object is created.</p>
	 * 
	 * @param columns -  The amount of columns the board has.
	 * @param  rows  - The amount of rows the board has. 
	 * </blockquote>
	 */
	
	public GameBoard(int columns, int rows) {
		boardSettings = new BoardSettings();
		gr = GameRules.getInstance();
		
		generationNo = 0;
		
		boardTopMisplaceX = 0;
		boardTopMisplaceY = 0;
		
//		curGenBoard = new BoardStatic(columns, rows);
//		newGenBoard = new BoardStatic(columns, rows);
//		
//		curActiveCells = new BoardStatic(columns, rows);
//		newActiveCells = new BoardStatic(columns, rows);
		
		curGenBoard = new BoardDynamic(columns, rows);
		newGenBoard = new BoardDynamic(columns, rows);
		
		curActiveCells = new BoardDynamic(columns, rows);
		newActiveCells = new BoardDynamic(columns, rows);

		threads = new Thread[MAX_THREADS];
		threadJob = new BoardThread[MAX_THREADS];
		
		Board boardList[] = { curGenBoard, newGenBoard, curActiveCells, newActiveCells };
		
		for(int i = 0; i < MAX_THREADS; i++) {
			threadJob[i] = new BoardThread(boardList);
			threads[i] = new Thread(threadJob[i]);
		}
	}
	

	/**
	 * 
	 * <blockquote>
	 * <b><i>settings</i></b>
	 * <pre>{@code public {@link BoardSettings }settings()}</pre>
	 * <p> Returns the object containing the settings for the board.</p>
	 *	@return boardSettings -  The  settings of the board. 
	 * </blockquote>
	 * 
	 */
	public BoardSettings settings() {
		return boardSettings;
	}
	
	/**
	 * 
	 * <blockquote>
	 * <b><i>gameRules</i></b>
	 * <pre>{@code public {@link GameRules } gameRules()}</pre>
	 * <p> Returns the object containing the rules for the board.</p>
	 *	@return gr -  The  game rules of the board.
	 * </blockquote>
	 * 
	 */
	public GameRules gameRules() {
		return gr;
	}
	
	/**
	 * 
	 * <blockquote>
	 * <b><i>getWidth</i></b>
	 * <pre>{@code public long getWidth() } </pre>
	 * <p> Returns the width of the board</p>
	 *	@return boardWith -  The  width of the active board.
	 * </blockquote>
	 * 
	 */
	public long getWidth() {
		return curGenBoard.getBoardWidth();
	}
	
	/**
	 * 
	 * <blockquote>
	 * <b><i>getHeight</i></b>
	 * <pre>{@code public long getWidth() } </pre>
	 * <p> Returns the height of the board</p>
	 *	@return boardHeight -  The  height of the active board.
	 * </blockquote>
	 * 
	 */
	public long getHeight() {
		return curGenBoard.getBoardHeight();
	}
	

	/**
	 * 
	 * <blockquote>
	 * <b><i>setCellState</i></b>
	 * <pre>{@code public void setCellState(int x, int y, boolean cellState)} </pre>
	 * <p> Sets the state of the cell on the given position to either dead or alive. The coordinates can 
	 * not be less than 0. </p>
	 *	@param x-  The horizontal coordinate of the cell.
	 *	@param y - The vertical coordinate of the cell . 
	 *	@param cellState - The state of the cell, true is alive, false is dead.
	 * </blockquote>
	 * 
	 */
	public void setCellState(int x, int y, boolean cellState) {
		if(!newGenBoard.hasPosition(x, y)) {	
			double cellSize = boardSettings.getCellSizeOuter();
			double ol = boardSettings.getOffsetLeft();
			double ot = boardSettings.getOffsetTop();
			
			if(x < 0)
				ol += Math.ceil(x / -64d) * cellSize * 64;
			
			if(y < 0)
				ot += -y*cellSize;

			boardSettings.setOffsetLeft(ol);
			boardSettings.setOffsetTop(ot);
			boardSettings.update();

			newGenBoard.setCellState(x, y, false);
			newActiveCells.setCellState(x, y, false);
		}

		curGenBoard.setCellState(x, y, cellState);
		curActiveCells.setCellState(x, y, cellState);
	}
	
	/**
	 * 
	 * <blockquote>
	 * <b><i>getCellState</i></b>
	 * <pre>{@code public boolean getCellState(int x, int y)} </pre>
	 * <p> Returns the state of the cell on the given position, which is either dead or alive. The coordinates can 
	 * not be less than 0. </p>
	 *	@param x-  The horizontal coordinate of the cell.
	 *	@param y - The vertical coordinate of the cell . 
	 *	@return cellState - The state of the cell, true is alive, false is dead.
	 * </blockquote>
	 * 
	 */
	
	public boolean getCellState(int x, int y) {
		return curGenBoard.getCellState(x, y) == 1;
	}
	
	/**
	 * 
	 * <blockquote>
	 * <b><i>getGenerationNo</i></b>
	 * <pre>{@code public long getGenerationNo()} </pre>
	 * @return generationNo- the number of the current generation.
	 * </blockquote>
	 */
	public long getGenerationNo() {
		return generationNo;
	}
	
	/**
	 * 
	 * <blockquote>
	 * <b><i>nextGenCell</i></b>
	 * <pre>{@code public void nextGenCell(int x, int y)} </pre>
	 * <p> Performs next generation on a given cell, neighbors cell state around the cell is affected accordingly to
	 * which rule is active, , rules are not defined in this class, see {@link GameRules}.  </p>
	 * 	@param x-  The horizontal coordinate of the cell.
	 *	@param y - The vertical coordinate of the cell. 
	 * </blockquote>
	 * 
	 */
	public void nextGenCell(int x, int y) {
		boolean cellStateNew;
		boolean cellStateOld;
		int neighbours;
	
		if(!newGenBoard.hasPosition(x + boardTopMisplaceX, y + boardTopMisplaceY)) {
			
			newGenBoard.setCellState(x + boardTopMisplaceX, y + boardTopMisplaceY, false);
			newActiveCells.setCellState(x + boardTopMisplaceX, y + boardTopMisplaceY, false);
			
			if(x == -1)
				boardTopMisplaceX = Long.SIZE;
			
			if(y == -1)
				boardTopMisplaceY = 1;
		}
		
		cellStateOld = (curGenBoard.getCellState(x, y) == 1);
		neighbours = (int)curGenBoard.countNeighbours(x, y);
		cellStateNew = gr.checkRules(cellStateOld, neighbours);
		
		newGenBoard.setCellState(x + boardTopMisplaceX, y + boardTopMisplaceY, cellStateNew);
		newActiveCells.setCellState(x + boardTopMisplaceX, y + boardTopMisplaceY, (cellStateOld != cellStateNew));
	}
	
	
	/**
	 * 
	 * <blockquote>
	 * <b><i>nextGeneration</i></b>
	 * <pre>{@code public void nextGeneration()} </pre>
	 * <p> Performs next generation on the board, the cells on the board is affected accordingly to
	 * which rule is active, rules are not defined in this class, see {@link GameRules}. </p>
	 * </blockquote>
	 */	
	public void nextGeneration() {
		double cellSize = boardSettings.getCellSizeOuter();
		double ol = boardSettings.getOffsetLeft();
		double ot = boardSettings.getOffsetTop();

		
		boardTopMisplaceY = 0;
		boardTopMisplaceX = 0;
		boardTopMisplaceYb = 0;
		boardTopMisplaceXb = 0;
		
		if(threadJob[0].checkMisplaceTop())
			boardTopMisplaceY = -64;
		
		if(threadJob[0].checkMisplaceBot())
			boardTopMisplaceYb = (int) curGenBoard.getBoardHeight() + 63;
		
		if(threadJob[0].checkMisplaceLeft())
			boardTopMisplaceX = -64;
		
		if(threadJob[0].checkMisplaceRight())
			boardTopMisplaceXb = (int) (curGenBoard.getBoardWidth() + 63);
		
		if(boardTopMisplaceX != 0)
			ol += (64 * cellSize);
		
		if(boardTopMisplaceY != 0)
			ot += (64*cellSize);		
		
		if(boardTopMisplaceX != 0 || boardTopMisplaceY != 0) {
			
			curGenBoard.setCellState(boardTopMisplaceX , boardTopMisplaceY, false);
			newGenBoard.setCellState(boardTopMisplaceX , boardTopMisplaceY, false);
			curActiveCells.setCellState(boardTopMisplaceX, boardTopMisplaceY, false);
			newActiveCells.setCellState(boardTopMisplaceX, boardTopMisplaceY, false);
		}
		
		if(boardTopMisplaceXb != 0 || boardTopMisplaceYb != 0) {
			
			curGenBoard.setCellState(boardTopMisplaceXb , boardTopMisplaceYb, false);
			newGenBoard.setCellState(boardTopMisplaceXb , boardTopMisplaceYb, false);
			curActiveCells.setCellState(boardTopMisplaceXb, boardTopMisplaceYb, false);
			newActiveCells.setCellState(boardTopMisplaceXb, boardTopMisplaceYb, false);
		}

		boardSettings.setOffsetLeft(ol);
		boardSettings.setOffsetTop(ot);
		boardSettings.update();
		
		
		int rowsPrThread = (int) Math.ceil( curGenBoard.getBoardHeight() / (double)MAX_THREADS );
		int i = 0;
		int j = 0;
		
		while(i < curGenBoard.getBoardHeight()) {
			threadJob[j].setRowRange(i, i+rowsPrThread);
			threads[j].run();

			i += rowsPrThread;
			j++;
		}
		
		for(i = 0; i < MAX_THREADS; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		
		oldGenBoard = curGenBoard;
		curGenBoard = newGenBoard;
		newGenBoard = oldGenBoard; 
		
		oldActiveCells = curActiveCells;
		curActiveCells = newActiveCells;
		newActiveCells = oldActiveCells;
		
		generationNo++;
	}
	
	/**
	 * 
	 * <blockquote>
	 * <b><i>clear</i></b>
	 * <pre>{@code public void clear()} </pre>
	 * <p> Clears the board where all cells are set to dead. </p>
	 * </blockquote>
	 * 
	 */
	public void clear() {
		generationNo = 0;
		curGenBoard.clearBoard();
		newGenBoard.clearBoard();
		curActiveCells.clearBoard();
		newActiveCells.clearBoard();
	}
	
	/**
	 * 
	 * <blockquote>
	 * <b><i>resetSize</i></b>
	 * <pre>{@code public void resetSize()} </pre>
	 * <p> Resets the size of the board, where the size is set to default, and the amount of generation is 
	 * set to zero.</p>
	 *
	 * </blockquote>
	 * 
	 */
	public void resetSize() {
		generationNo = 0;
		curGenBoard.resetSize();
		newGenBoard.resetSize();
		curActiveCells.resetSize();
		newActiveCells.resetSize();
	}
	
	/**
	 * 
	 * <blockquote>
	 * <b><i>setPattern</i></b>
	 * <pre>{@code public void setPattern({@link PatternFormat} pattern)} </pre>
	 * <p> Sets the given pattern  into the active board.</p>
	 * 
	 * @param pattern - an object containing the pattern to be set. 
	 *
	 * </blockquote>
	 * 
	 */
	public void setPattern(PatternFormat pattern) {
		Board obj = pattern.getPattern();
		
		for(int j = 0; j < pattern.getPatternHeight(); j++) {
			for(int i = 0; i < pattern.getPatternWidth(); i++) {
				this.setCellState(i, j, (obj.getCellState(i, j) == 1));
			}
		}
	}
	
	
	/**

	 * 
	 * <blockquote>
	 * <b><i>toString</i></b>
	 * <pre>{@code public {@link String } toString()} </pre>
	 * <p> Represents the GameBoard in a String format.</p>
	 * 
	 * @return buffer - The GameBoard represented in a string format.
	 * </blockquote>
	 */

	@Override
	public String toString(){
		StringBuffer buffer = new StringBuffer();
		for(int i=0; i< curGenBoard.getBoardWidth(); i++){
			for(int j=0; j< curGenBoard.getBoardHeight(); j++){
				if(getCellState(j,i) == true) {
					buffer.append(1);
				} else {
				 buffer.append(0);
				}
			
			
			}
			buffer.append("\n");
			
		}
		
		return buffer.toString();
	}

}
