package game;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import game.Game.GameInstance;
import pieces.Piece;

public class FileManager {

	public static void createNewGameFile(GameInstance game) {
		File gameFile = new File(GameData.FILE_DIRECTORY + "\\" + game.getGameName() + ".txt");
		FileOutputStream fileOutputStream = null;
		ObjectOutputStream fileWriter = null;
		if (!gameFile.exists()) {
			try {
				gameFile.createNewFile();
				fileOutputStream = new FileOutputStream(gameFile);
				fileWriter = new ObjectOutputStream(fileOutputStream);
				fileWriter.writeObject(game.getPlayerPieces());
				fileWriter.writeObject(game.getDeadPieces());
				fileWriter.flush();		
				} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					fileWriter.close();
					fileOutputStream.close();	
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			JOptionPane.showMessageDialog(null, "A game with that name already exists!");
			return;
		}
	}

	public static GameInstance loadGame(String gameName) {
		File gameFile = new File(GameData.FILE_DIRECTORY + "\\" + gameName + ".txt");
		FileInputStream fileInputStream = null;
		ObjectInputStream fileReader = null;
		try {
			fileInputStream = new FileInputStream(gameFile);
			fileReader = new ObjectInputStream(fileInputStream);
			ArrayList<Piece> playerPieces = (ArrayList<Piece>) fileReader.readObject();
			ArrayList<Piece> deadPieces = (ArrayList<Piece>) fileReader.readObject();
			return new GameInstance(playerPieces, deadPieces, gameName);
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				fileInputStream.close();
				fileReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static File[] getAllFiles() {
		File directory = new File(GameData.FILE_DIRECTORY);
		return directory.listFiles();
	}

}
