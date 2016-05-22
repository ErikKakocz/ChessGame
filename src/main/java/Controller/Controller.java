package Controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import javafx.stage.Stage;
import pieces.Color;
import pieces.Type;
import server.Server;
import table.Table;


public final class Controller{

	static Table table;
	static int BLACKPAWNSTART = 1, WHITEPAWNSTART = 6, BLACKPAWNJUMP = 3, WHITEPAWNJUMP = 4;
	static Stage stage;
	static menu.MenuController mCont;
	static Server server;
	static Socket sock;
	static DataInputStream reader;
	static DataOutputStream writer;
	
	public Controller(){
		table = new Table();
		setupTable();
	}
	
	public void setupTable(){
		JsonParser parser = new JsonParser();
		try {
			JsonArray array = parser.parse(new FileReader(getJsonFile("chesspieces.json"))).getAsJsonObject().get("pieces").getAsJsonArray();
			for (JsonElement element : array){
				JsonObject obj = element.getAsJsonObject();
				table.setPiece(obj.get("Row").getAsInt(), obj.get("Column").getAsInt(),
						pieces.Piece.pieceTranslator(obj.get("Type").getAsString()), pieces.Piece.colorTranslator(obj.get("Row").getAsString()));

			}

		} catch (JsonIOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}
	
	
	
	@SuppressWarnings("static-access")
	public static void main(String[] args) throws IOException {
		if(args.length>0 && args[0].equals("server")){
			server=new Server();
			System.out.println("servermode");}
		else{
			System.out.println("notservermode");
		mCont=new menu.MenuController();
		mCont.main(args);
		}
	}

	public static void connectToServer(String username,String pass,String action) throws IOException{
		sock=new Socket();
		sock.connect(new InetSocketAddress(InetAddress.getLoopbackAddress(),5555));
		reader=new DataInputStream(sock.getInputStream());
		writer=new DataOutputStream(sock.getOutputStream());
		int code=reader.read();
		System.out.println(code);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(code==0){
			System.out.println("anyád");
			writer.writeUTF(username);
			writer.writeUTF(pass);
			writer.writeUTF(action);
			System.out.println("anyád");
		}
		System.out.println(reader.read());
	}
	
	static File getJsonFile(String filename) {
		ClassLoader cl = Controller.class.getClassLoader();
		File file = new File(cl.getResource(filename).getFile());
		if(file.exists())
			return file;
		else 
			return null;
	}

	

	static void movePiece(int fromRow, int fromCol, int toRow, int toCol) {
		if (validateMove(fromRow, fromCol, toRow, toCol) && notObstructed(fromRow, fromCol, toRow, toCol) && !(table
				.getPiece(toRow, toCol).getPieceColor().equals(table.getPiece(fromRow, fromCol).getPieceColor())))
			table.movePiece(fromRow, fromCol, toRow, toCol);
	}

	
	static boolean notObstructed(int fromRow, int fromCol, int toRow, int toCol) {
		if (table.getPiece(fromRow, fromCol).getPieceType().equals(Type.PAWN)
				&& !pawnAttack(fromRow, fromCol, toRow, toCol)
				&& !table.getPiece(toRow, toCol).getPieceType().equals(Type.NULLPIECE)) {
			return false;
		} else {
			int topLeftRow = Math.min(toRow, fromRow), topLeftCol = Math.min(toCol, fromCol),
					botRightRow = Math.max(toRow, fromRow), botRightCol = Math.max(toCol, fromCol);
			if (table.getPiece(fromRow, fromCol).getPieceColor().equals(table.getPiece(toRow, toCol).getPieceColor()))
				return false;
			while ((topLeftRow != botRightRow - 1) || (topLeftCol != botRightCol - 1)) {
				if (topLeftRow != botRightRow - 1)
					topLeftRow++;
				if (topLeftCol != botRightCol - 1)
					topLeftCol++;
				if (!table.getPiece(topLeftRow, topLeftCol).getPieceType().equals(Type.NULLPIECE))
					return false;
			}
		}
		return true;
	}

	static boolean validateMove(int fromRow, int fromCol, int toRow, int toCol) {
		boolean validPieceMove = false;
		pieces.Type type = table.getPiece(fromRow, fromCol).getPieceType();
		switch (type) {
		case ROOK: {
			validPieceMove = (horizontalMove(fromRow, fromCol, toRow, toCol)
					|| verticalMove(fromRow, fromCol, toRow, toCol));
			break;
		}
		case KNIGHT: {
			validPieceMove = (((Math.abs((fromRow + 1) - (toRow + 1)) == 2)
					&& (Math.abs((fromCol + 1) - (toCol + 1)) == 1))
					|| ((Math.abs((fromRow + 1) - (toRow + 1)) == 1) && (Math.abs((fromCol + 1) - (toCol + 1)) == 2)));
			break;
		}
		case BISHOP: {
			validPieceMove = diagonalMove(fromRow, fromCol, toRow, toCol);
			break;
		}
		case KING: {
			validPieceMove = (Math.abs((fromRow + 1) - (toRow + 1)) < 2 && Math.abs((fromCol + 1) - (toCol + 1)) < 2);
			break;
		}
		case QUEEN: {
			validPieceMove = (horizontalMove(fromRow, fromCol, toRow, toCol)
					|| verticalMove(fromRow, fromCol, toRow, toCol) || diagonalMove(fromRow, fromCol, toRow, toCol));
			break;
		}
		case PAWN: {
			validPieceMove = pawnJump(fromRow, fromCol, toRow, toCol) || pawnLeap(fromRow, fromCol, toRow, toCol)
					|| pawnAttack(fromRow, fromCol, toRow, toCol)
					|| pawnEnPassantAttack(fromRow, fromCol, toRow, toCol);
		}
		default:
			break;
		}
		return validPieceMove;
	}

	static boolean horizontalMove(int fromRow, int fromCol, int toRow, int toCol) {
		return (fromRow == toRow && fromCol != toCol);
	}

	static boolean verticalMove(int fromRow, int fromCol, int toRow, int toCol) {
		return (fromRow != toRow && fromCol == toCol);
	}

	static boolean diagonalMove(int fromRow, int fromCol, int toRow, int toCol) {
		return ((Math.abs((fromRow + 1) - (toRow + 1))) == (Math.abs((fromCol + 1) - (toCol + 1))));
	}

	static boolean pawnJump(int fromRow, int fromCol, int toRow, int toCol) {
		return (fromRow == WHITEPAWNSTART && toRow == WHITEPAWNJUMP)
				|| (fromRow == BLACKPAWNSTART && toRow == BLACKPAWNJUMP);
	}

	static boolean pawnLeap(int fromRow, int fromCol, int toRow, int toCol) {
		if (table.getPiece(fromRow, fromCol).getPieceColor().equals(Color.WHITE))
			return (toRow == (fromRow - 1)) && (fromCol == toCol);
		else
			return (toRow == (fromRow + 1)) && (fromCol == toCol);
	}

	static boolean pawnAttack(int fromRow, int fromCol, int toRow, int toCol) {
		if (table.getPiece(fromRow, fromCol).getPieceColor().equals(Color.WHITE))
			return (toRow == (fromRow - 1)) && (Math.abs(fromCol - toCol) == 1)
					&& (table.getPiece(toRow, toCol).getPieceColor().equals(Color.BLACK));
		else
			return (toRow == (fromRow + 1)) && (Math.abs(fromCol - toCol) == 1)
					&& (table.getPiece(toRow, toCol).getPieceColor().equals(Color.WHITE));
	}

	static boolean pawnEnPassantAttack(int fromRow, int fromCol, int toRow, int toCol) {
		if (table.getPiece(fromRow, fromCol).getPieceColor().equals(Color.WHITE))
			return (fromRow == BLACKPAWNJUMP) && (table.getPiece(fromRow, toCol).isSpecialMove());
		else
			return (fromRow == WHITEPAWNJUMP) && (table.getPiece(fromRow, toCol).isSpecialMove());
	}



	
}

