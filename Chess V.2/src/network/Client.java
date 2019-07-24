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
	private static String gamePassword, serverIP;

	public static void joinOnlineGame() {
		// Yes == 0, 1 == no
		int option = JOptionPane.showConfirmDialog(null, "Join private game?", "Join private game?", 0);
		if (option == 0) {
			gamePassword = JOptionPane.showInputDialog(null, "Enter game password");
		} else {
			gamePassword = "";
		}
		checkForData();
		connectToServer();
		Game.game.setGameState(GameStates.IN_GAME);
		Game.game.setAsOnlineGame(true);
		Game.game.startTimer();
	}

	private static void checkForData() {
		DatagramSocket datagramSocket = null;
		try {
			while (true) {
				datagramSocket = new DatagramSocket(GameData.NETWORK_PORT);
				byte[] data = new byte[256];
				DatagramPacket recievingPacket = new DatagramPacket(data, data.length);
				System.out.println("looking for data");
				datagramSocket.receive(recievingPacket);
				System.out.println("Data recieved: " + new String(data).toString());
				String recievedStr = new String(recievingPacket.getData());
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

	private static void connectToServer() {
		try {
			// socket = new Socket(serverIP, GameData.NETWORK_PORT);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Socket getSocket() {
		return socket;
	}

}
