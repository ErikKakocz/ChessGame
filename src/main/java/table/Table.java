package table;

import java.util.Arrays;

import pieces.Color;
import pieces.Piece;
import pieces.Type;

public class Table {

	Piece[][] table=new Piece[8][8];
	
	public Table(){
		for(Piece[] row:table)
			for(Piece piece:row)
				piece=new Piece();
	}

	@Override
	public String toString() {
		return "Table [table=" + Arrays.toString(table) + "]";
	}
	
	public void setPiece(int row,int col,Type type,Color color){
		table[row][col]=new Piece(color,type);
	}
	
	public Piece getPiece(int row,int col){
		return table[row][col];
	}
	
	public void movePiece(int fromRow,int fromCol,int toRow,int toCol){
		table[toRow][toCol]=table[fromRow][fromCol];
		table[fromRow][fromCol]=new Piece();
	}
	
	
}
