package model;

import java.util.ArrayList;
import java.util.List;

import model.BoardDynamic.AddMode;

@Deprecated
public class BoardExpand {

	private static BoardExpand reference = null;
	
	private int boardTopMisplaceX;
	private int boardTopMisplaceY;

	
	
	public void setMisplaceTop(int misplaceTop) {
		boardTopMisplaceY = misplaceTop;
	}
	
	
	public int getMisplaceTop() {
		return boardTopMisplaceY;
	}
	
	
	public void setMisplaceLeft(int misplaceLeft) {
		boardTopMisplaceX = misplaceLeft;
	}
	
	
	public int getMisplaceLeft() {
		return boardTopMisplaceX;
	}
	
	
	private BoardExpand() {
		boardTopMisplaceX = 0;
		boardTopMisplaceY = 0;
	}
	
	
	public static BoardExpand getInstance() {
		if(reference == null) {
			reference = new BoardExpand();
		} 
		
		return reference;
	}
	
	
	public synchronized void expandBoard(AddMode addMode, int targetSize, List<List<Long>> board, long defaultValue) {
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
		}
	}
}
