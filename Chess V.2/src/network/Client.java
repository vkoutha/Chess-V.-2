package network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

import game.Game;
import game.GameData;
import game.GameData.GameStates;

public class Client {

	private static Socket socket;
	private static String gamePassword, serverIP;

	public static void joinOnlineGame() {
		// Yes == 0, 1 == no
		int option = JOptionPane.showConfirmDialog(null, "Join private game?", "Join private game?", 0);
		if (option == 0) {
			gamePassword = JOptionPane.showInputDialog(null, "Enter game password");
		} else {
			gamePassword = "";
		}
		Game.game.setGameState(GameStates.SEARCHING);
		checkForData();
		connectToServer();
		Game.game.setGameState(GameStates.IN_GAME);
		Game.game.setAsOnlineGame(true);
	}

	private static void checkForData() {
		byte[] data = new byte[256];
		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket();
			DatagramPacket recievingPacket = new DatagramPacket(data, data.length);
			socket.receive(recievingPacket);
			String recievedStr = new String(recievingPacket.getData());
			if (gamePassword.equals(recievedStr)) {
				serverIP = recievingPacket.getAddress().getHostAddress();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			socket.close();
		}
	}

	private static void connectToServer() {
		try {
			socket = new Socket(serverIP, GameData.NETWORK_PORT);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Socket getSocket() {
		return socket;
	}

}
