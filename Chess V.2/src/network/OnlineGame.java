package network;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import game.GameData.DataTransferHeaders;
import game.GameData.Players;
import pieces.Pawn;
import pieces.Piece;

public class OnlineGame {

	private Socket socket;
	private ObjectOutputStream outputStream;
	private ObjectInputStream inputStream;
	private Players ownPlayer;

	public OnlineGame(boolean isServer) {
		try {
			OutputStream outputStream;
			InputStream inputStream;
			if (isServer) {
				socket = Server.getSocket();
				outputStream = socket.getOutputStream();
				inputStream = socket.getInputStream();
				ownPlayer = Players.PLAYER_1;
			} else {
				socket = Client.getSocket();
				outputStream = socket.getOutputStream();
				inputStream = socket.getInputStream();
				ownPlayer = Players.PLAYER_2;
			}
			this.outputStream = new ObjectOutputStream(outputStream);
			this.inputStream = new ObjectInputStream(inputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendMove(int[] initial, int[] destination) {
		try {
			outputStream.writeUTF(DataTransferHeaders.PIECE_MOVE.name());
			outputStream.writeInt(initial[0]);
			outputStream.writeInt(initial[1]);
			outputStream.writeInt(destination[0]);
			outputStream.writeInt(destination[1]);
			outputStream.flush();
			System.out.println("Move sent!!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendPawnPromotion(Pawn pawn, Piece newPiece) {
		try {
			outputStream.writeUTF(DataTransferHeaders.PAWN_PROMOTION.name());
			outputStream.writeObject(pawn);
			outputStream.writeObject(newPiece);
			outputStream.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void ignoreDataHeader() {
		try {
			inputStream.readUTF();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public DataTransferHeaders getIncomingDataHeader() {
		try {
			return DataTransferHeaders.valueOf(inputStream.readUTF());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public int[][] getOpponentMove() {
		int[] initial = null;
		int[] destination = null;
		try {
			initial = new int[] { inputStream.readInt(), inputStream.readInt() };
			destination = new int[] { inputStream.readInt(), inputStream.readInt() };
		} catch (Exception e) {
			e.printStackTrace();
		}
		int[][] opponentMove = { initial, destination };
		return opponentMove;
	}

	public Piece[] getPawnPromotion() {
		Piece[] pieces = null;
		try {
			pieces = new Piece[] { (Piece) inputStream.readObject(), (Piece) inputStream.readObject() };
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pieces;
	}

	public Players getOwnPlayer() {
		return ownPlayer;
	}

	public void close() {
		try {
			Client.getSocket().close();
			Server.getSocket().close();
			Server.getServerSocket().close();
			outputStream.close();
			inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
