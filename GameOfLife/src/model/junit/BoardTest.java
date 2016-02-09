package model.junit;

import org.junit.Test;

import model.BoardStatic;
import model.GameBoard;


public class BoardTest {
 
    GameBoard gb= new GameBoard(6,6);
    BoardStatic br= new BoardStatic(6,6);
    private long[][] board = new long[][]{
	    {0b000000}, 
	    {0b000000}, 
	    {0b001000},
	    {0b001000},
	    {0b001000},
	    {0b000000}
	    	};
@Test
public  void testNextGen(){
	br.setBoard(board);
	boolean cellstate;
	
	for(int i=0; i<board.length; i++){
	for(int j=0; j<board[j].length; j++){
	cellstate=br.getCellState(j, i)==1;	
		gb.setCellState(j, i, cellstate);
		System.out.println("The array +"+ br.toString());
	 	
		gb.nextGeneration();
		 cellstate=gb.getCellState(j, i);
		
		System.out.println("HAHAH test" + cellstate);
	}
	}
	
	
	
	
    }

   
}
