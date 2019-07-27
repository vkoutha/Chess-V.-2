package network;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
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
		sendDataToAllIPs();
		startServer();
		System.out.println("SERVER STARTED");
		Game.game.setGameState(GameStates.IN_GAME);
		System.out.println("GAME STATE SWITCHED");
		Game.game.setAsOnlineGame(true);
		Game.game.startTimer();
	}

	private static void sendDataToAllIPs() {
		ArrayList<String> ips = getNetworkIPs();
		byte[] data = gamePassword.getBytes();
		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket(GameData.NETWORK_PORT);
			for (int i = 0; i < ips.size(); i++) {
				if (!ips.get(i).equals(Inet4Address.getLocalHost().getHostAddress())) {
					DatagramPacket sendingData = new DatagramPacket(data, data.length,
							InetAddress.getByName(ips.get(i)), GameData.NETWORK_PORT);
					System.out.println("Data sent to: " + ips.get(i));
					socket.send(sendingData);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			socket.close();
		}
		System.out.println("Data sent");
	}

	private static ArrayList<String> getNetworkIPs() {
		ArrayList<String> reachableIPs = new ArrayList<String>();
		final byte[] ip;
		try {
			ip = InetAddress.getLocalHost().getAddress();
		} catch (Exception e) {
			return reachableIPs;
		}
		int index = 0;
		for (int i = 1; i <= 254; i++) {
			final int j = i;
			index = i;
			new Thread(new Runnable() {
				public void run() {
					try {
						ip[3] = (byte) j;
						InetAddress address = InetAddress.getByAddress(ip);
						String output = address.toString().substring(1);
						if (address.isReachable(GameData.CONNECTION_TIMEOUT_MS)) {
							System.out
									.println(output + " with hostname " + InetAddress.getByName(output).getHostName());
							reachableIPs.add(output);
						} else {
						}
					} catch (Exception e) {

					}
				}
			}).start();
		}
		while (index != 254)
			System.out.println(index);
		;

		try {
			Thread.sleep(GameData.CONNECTION_TIMEOUT_MS);
		} catch (Exception e) {

		}
		System.out.println("Finished");
		return reachableIPs;
	}

	private static void startServer() {
		try {
			serverSocket = new ServerSocket(GameData.NETWORK_PORT);
			System.out.println("WAITING FOR CLIENT TO CONNECT");
			clientSocket = serverSocket.accept();
			System.out.println("Server started");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static ServerSocket getServerSocket() {
		return serverSocket;
	}

	public static Socket getSocket() {
		return clientSocket;
	}

}
