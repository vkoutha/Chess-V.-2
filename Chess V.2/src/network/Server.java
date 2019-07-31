package network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import game.Game;
import game.GameData;
import game.GameData.GameStates;

public class Server {

	private static String gamePassword;
	private static ServerSocket serverSocket;
	private static Socket clientSocket;

	public static void startOnlineGame() {
		gamePassword = JOptionPane.showInputDialog(null,
				"Enter game password for a private game, no password for a public game");
		broadcastGamePassword();
		startServer();
		System.out.println("SERVER STARTED");
		Game.game.setGameState(GameStates.IN_GAME);
		System.out.println("GAME STATE SWITCHED");
		Game.game.setAsOnlineGame(true);
		Game.game.startTimer();
	}

	private static void broadcastGamePassword() {
		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket(GameData.NETWORK_PORT);
			DatagramPacket sendingData = new DatagramPacket(gamePassword.getBytes(), gamePassword.getBytes().length,
					InetAddress.getByName(getBroadcastIP()), GameData.NETWORK_PORT);
			socket.send(sendingData);
			System.out.println("Data sent to " + getBroadcastIP());
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			socket.close();
		}
	}

	private static void startServer() {
		try {
			serverSocket = new ServerSocket(GameData.NETWORK_PORT);
			clientSocket = serverSocket.accept();
			System.out.println("WAITING FOR CLIENT TO CONNECT");
			System.out.println("Server started");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String getBroadcastIP() {
		String broadcastIP = "";
		try {
			byte[] ipBytes = InetAddress.getLocalHost().getAddress();
			ipBytes[3] = (byte) 255;
			broadcastIP = InetAddress.getByAddress(ipBytes).toString().substring(1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//return "10.49.251.130";
		return broadcastIP;
	}

	public static ServerSocket getServerSocket() {
		return serverSocket;
	}

	public static Socket getSocket() {
		return clientSocket;
	}

}
