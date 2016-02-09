package model;

public class BoardThread implements Runnable {

	private int rowStart;
	private int rowEnd;
	
	private Board curGen, newGen, oldGen;
	private Board curAct, newAct, oldAct;
	
	private GameRules gr = GameRules.getInstance();
	
	
	
	public BoardThread(Board[] gameBoard) throws NullPointerException, IllegalArgumentException {
		if(gameBoard == null) {
			throw new NullPointerException("gameBoard can not be nul.");
		}
		
		curGen = gameBoard[0];
		newGen = gameBoard[1];
		curAct = gameBoard[2];
		newAct = gameBoard[3];
	}
	
	
	public void setRowRange(int rowStart, int rowEnd) {
		this.rowStart = rowStart;
		this.rowEnd = rowEnd;
	}
	
	
	public void nextGenCell(int x, int y) {
		boolean cellStateNew;
		boolean cellStateOld;
		int neighbours;
		
		cellStateOld = (curGen.getCellState(x, y) == 1);
		neighbours = (int)curGen.countNeighbours(x, y);
		cellStateNew = gr.checkRules(cellStateOld, neighbours);
			
		newGen.setCellState(x, y, cellStateNew);
		newAct.setCellState(x, y, (cellStateOld != cellStateNew));
	}
	
	
	@Override
	public void run() {
		long data;
		
		for(int j = rowStart; j < rowEnd; j++) {
 			for(int i = 0; i < curAct.getNumColumns(); i++) {
 				data = curAct.getElement(i, j);
 				if(data != 0) {
 					for(int k = 0; k < 64; k++) {
 						if(curAct.getBit(data, k) == 1) {
 							int x = (i<<6) + k;
 							
 							nextGenCell(x - 1,j - 1);
 							nextGenCell(x    ,j - 1);
 							nextGenCell(x + 1,j - 1);
 							
 							nextGenCell(x - 1,j);
 							nextGenCell(x    ,j);
 							nextGenCell(x + 1,j);
 							
 							nextGenCell(x - 1,j + 1);
 							nextGenCell(x    ,j + 1);
 							nextGenCell(x + 1,j + 1);
 						}
 					}
 				}
 			}
		}
		
		oldGen = curGen;
		curGen = newGen;
		newGen = oldGen; 
		
		oldAct = curAct;
		curAct = newAct;
		newAct = oldAct;
	}
	
	
	public boolean checkMisplaceTop() {
		boolean ret = false;
		int i = 0;
		
		while(ret == false && i < curGen.getNumColumns()) {
			ret = (curGen.getElement(i, 0) != 0);
			i++;
		}

		return ret;
	}
	
	public boolean checkMisplaceBot() {
		boolean ret = false;
		int i = 0;
		
		while(ret == false && i < curGen.getNumColumns()) {
			ret = (curGen.getElement(i, (int) (curGen.getBoardHeight()-1)) != 0);
			i++;
		}

		return ret;
	}
	
	public boolean checkMisplaceLeft() {
		boolean ret = false;
		int i = 0;
		
		while(ret == false && i < curGen.getNumRows()) {
			ret = ((curGen.getElement(0, i) & 1L) != 0);
			i++;
		}

		return ret;
	}

	public boolean checkMisplaceRight() {
		boolean ret = false;
		int i = 0;
		
		while(ret == false && i < curGen.getNumRows()) {
			ret = (((curGen.getElement((int) (curGen.getBoardWidth()-1), i)>>64) & 1L) != 0);
			i++;
		}

		return ret;
	}
}
