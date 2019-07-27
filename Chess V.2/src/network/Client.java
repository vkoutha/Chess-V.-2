package network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.Socket;

import javax.swing.JOptionPane;

import game.Game;
import game.GameData;
import game.GameData.GameStates;

public class Client {

	private static Socket socket;
	private static String gamePassword;

	public static void joinOnlineGame() {
		// Yes == 0, 1 == no
		int option = JOptionPane.showConfirmDialog(null, "Join private game?", "Join private game?", 0);
		if (option == 0) {
			gamePassword = JOptionPane.showInputDialog(null, "Enter game password");
		} else {
			gamePassword = "";
		}
		checkForDataAndConnectToServer();
		Game.game.setGameState(GameStates.IN_GAME);
		Game.game.setAsOnlineGame(true);
		Game.game.startTimer();
	}

	private static void checkForDataAndConnectToServer() {
		DatagramSocket datagramSocket = null;
		try {
			datagramSocket = new DatagramSocket(GameData.NETWORK_PORT);
			while (true) {
				byte[] data = new byte[256];
				DatagramPacket recievingPacket = new DatagramPacket(data, data.length);
				datagramSocket.receive(recievingPacket);
				String recievedStr = new String(recievingPacket.getData(), recievingPacket.getOffset(), recievingPacket.getLength());
				if (gamePassword.equals(recievedStr)) {
					socket = new Socket(recievingPacket.getAddress(), GameData.NETWORK_PORT);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			datagramSocket.close();
		}
	}

	public static Socket getSocket() {
		return socket;
	}

}
