package network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;

import javax.swing.JOptionPane;

import game.Game;
import game.GameData;
import game.GameData.GameStates;

public class Server {

	private static String gamePassword;
	private static ServerSocket serverSocket;
	private static Socket clientSocket;
	private static String directConnectIP;

	public static void startOnlineGame() {
		int option = JOptionPane.showConfirmDialog(null, "Direct connect?", "Direct connect?", 0);
		if(option == 0) {
			directConnectIP = JOptionPane.showInputDialog(null, "Enter device IP");
			gamePassword = "";
		}else {
			gamePassword = JOptionPane.showInputDialog(null,
					"Enter game password for a private game, no password for a public game");
			directConnectIP = "";
		}
		broadcastGamePassword();
		startServer();
		Game.game.setAsOnlineGame(true, true);
	}

	private static void broadcastGamePassword() {
		DatagramSocket socket = null;
		String ipToSendTo = directConnectIP.equals("") ? getBroadcastAddress() : directConnectIP;
		try {
			socket = new DatagramSocket(GameData.NETWORK_PORT);
			for (int i = 0; i < 10; i++) {
				DatagramPacket sendingData = new DatagramPacket(gamePassword.getBytes(), gamePassword.getBytes().length,
						InetAddress.getByName(ipToSendTo), GameData.NETWORK_PORT);
				socket.send(sendingData);
				System.out.println("Data sent to " + ipToSendTo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
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

	private static String getBroadcastAddress() {
		String broadcastAddress = "";
		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
				NetworkInterface networkInterface = interfaces.nextElement();
				if (networkInterface.isLoopback())
					continue;
				for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
					InetAddress broadcast = interfaceAddress.getBroadcast();
					if (broadcast == null)
						continue;
					return interfaceAddress.getBroadcast().toString().substring(1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return broadcastAddress;
	}

	public static ServerSocket getServerSocket() {
		return serverSocket;
	}

	public static Socket getSocket() {
		return clientSocket;
	}

}
