package network;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import game.GameData.Players;

public class OnlineGame {

	private DataOutputStream outputStream;
	private DataInputStream inputStream;
	private Players ownPlayer;

	public OnlineGame(boolean isServer) {
		try {
			if (isServer) {
				outputStream = new DataOutputStream(Server.getSocket().getOutputStream());
				inputStream = new DataInputStream(Server.getSocket().getInputStream());
				ownPlayer = Players.PLAYER_1;
			} else {
				outputStream = new DataOutputStream(Client.getSocket().getOutputStream());
				inputStream = new DataInputStream(Client.getSocket().getInputStream());
				ownPlayer = Players.PLAYER_2;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendMove(int[] initial, int[] destination) {
		try {
			outputStream.writeInt(initial[0]);
			outputStream.writeInt(initial[1]);
			outputStream.writeInt(destination[0]);
			outputStream.writeInt(destination[1]);
			outputStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int[][] getOpponentMove() {
		int[] initial = {};
		int[] destination = {};
		try {
			initial = new int[] { inputStream.readInt(), inputStream.readInt() };
			destination = new int[] { inputStream.readInt(), inputStream.readInt() };
		} catch (Exception e) {
			e.printStackTrace();
		}
		int[][] opponentMove = { initial, destination };
		return opponentMove;
	}
	
	public Players getOwnPlayer() {
		return ownPlayer;
	}
	
	public void close() {
		try {
			Client.getSocket().close();
			Server.getSocket().close();
			Server.getServerSocket().close();
			inputStream.close();
			outputStream.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
