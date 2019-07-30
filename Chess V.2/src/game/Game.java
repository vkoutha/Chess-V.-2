package game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import game.GameData.GameStates;
import game.GameData.Players;
import game.Renderer.BoardPanel;
import game.Renderer.Panels;
import game.Renderer.Player1Panel;
import game.Renderer.Player2Panel;
import network.Client;
import network.OnlineGame;
import network.Server;
import pieces.Bishop;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;

public class Game implements ActionListener, MouseListener {

	public static Game game;
	protected JFrame frame, promotionMenu;
	protected JLabel player1TimerLabel, player2TimerLabel;
	protected Renderer renderer;
	protected Renderer.Player1Panel player1Panel;
	protected Renderer.Player2Panel player2Panel;
	protected Renderer.BoardPanel boardPanel;
	protected Timer timer;
	protected Tile[][] tiles;
	protected Tile selectedTile;
	protected ArrayList<Piece> player1Pieces, deadPlayer1Pieces;
	protected ArrayList<Piece> player2Pieces, deadPlayer2Pieces;
	protected OnlineGame onlineGame;
	protected GameStates gameState;
	protected Players playerTurn;
	protected boolean isOnlineGame;

	private Game() {
	}

	public void setGameState(GameStates state) {
		gameState = state;
		resetFrame();
		switch (state) {
		case MENU:
			FrameManager.MainMenuFrame.initMenuFrame();
			break;
		case IN_GAME:
			FrameManager.GameFrame.initGameFrame();
			initGame();
			break;
		case SEARCHING:
			FrameManager.SearchingFrame.initSearchingFrame();
			break;
		}
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}

	private void resetFrame() {
		if (frame != null) {
			frame.dispose();
		}
		frame = new JFrame("Chess Revamped!");
	}

	private void initGame() {
		initTiles();
		initPlayer1Pieces();
		initPlayer2Pieces();
		timer = new Timer(GameData.UPDATE_SPEED_MS, this);
		gameState = GameStates.IN_GAME;
		playerTurn = Players.PLAYER_1;
	}

	private void initTiles() {
		tiles = new Tile[8][8];
		for (int row = 0; row < tiles.length; row++) {
			for (int col = 0; col < tiles[row].length; col++) {
				tiles[row][col] = new Tile(row, col);
			}
		}
	}

	private void initPlayer1Pieces() {
		player1Pieces = new ArrayList<Piece>();
		deadPlayer1Pieces = new ArrayList<Piece>();
		for (int col = 0; col < GameData.COLUMNS; col++) {
			player1Pieces.add(new Pawn(6, col, Players.PLAYER_1));
		}
		player1Pieces.add(new Rook(7, 0, Players.PLAYER_1));
		player1Pieces.add(new Rook(7, 7, Players.PLAYER_1));
		player1Pieces.add(new Knight(7, 1, Players.PLAYER_1));
		player1Pieces.add(new Knight(7, 6, Players.PLAYER_1));
		player1Pieces.add(new Bishop(7, 2, Players.PLAYER_1));
		player1Pieces.add(new Bishop(7, 5, Players.PLAYER_1));
		player1Pieces.add(new Queen(7, 3, Players.PLAYER_1));
		player1Pieces.add(new King(7, 4, Players.PLAYER_1));
	}

	private void initPlayer2Pieces() {
		player2Pieces = new ArrayList<Piece>();
		deadPlayer2Pieces = new ArrayList<Piece>();
		for (int col = 0; col < GameData.COLUMNS; col++) {
			player2Pieces.add(new Pawn(1, col, Players.PLAYER_2));
		}
		player2Pieces.add(new Rook(0, 0, Players.PLAYER_2));
		player2Pieces.add(new Rook(0, 7, Players.PLAYER_2));
		player2Pieces.add(new Knight(0, 1, Players.PLAYER_2));
		player2Pieces.add(new Knight(0, 6, Players.PLAYER_2));
		player2Pieces.add(new Bishop(0, 2, Players.PLAYER_2));
		player2Pieces.add(new Bishop(0, 5, Players.PLAYER_2));
		player2Pieces.add(new Queen(0, 3, Players.PLAYER_2));
		player2Pieces.add(new King(0, 4, Players.PLAYER_2));
	}

	public void render(Graphics g, Panels panelType) {
		switch (panelType) {
		case BOARD:
			FrameManager.GameFrame.renderBoard(g);
			break;
		case PLAYER_1:
			FrameManager.GameFrame.renderPlayer1Panel(g);
			break;
		case PLAYER_2:
			FrameManager.GameFrame.renderPlayer2Panel(g);
			break;
		}
	}

	public void processTileSelection(int sRow, int sCol) {
		Piece selTilePiece = tiles[sRow][sCol].getPiece();
		// If no tile is currently selected
		if (selectedTile == null) {
			// If tile selected has a piece and it is the player's own piece
			if (selTilePiece != null && playerTurn == selTilePiece.getPlayer()) {
				if (!isOnlineGame || (isOnlineGame && playerTurn == onlineGame.getOwnPlayer())) {
					selectedTile = tiles[sRow][sCol];
					displayPiecesMoves(selTilePiece, true);
				}
			}
		} else { // If a tile has already been selected
			// If the user selects a tile that is not a valid move
			if (selTilePiece == null && !selectedTile.getPiece().isValidMove(sRow, sCol)) {
				displayPiecesMoves(selectedTile.getPiece(), false);
				selectedTile = null;
				// If the user selects another piece and it is their own piece
			} else if (selTilePiece != null && playerTurn == selTilePiece.getPlayer()) {
				displayPiecesMoves(selectedTile.getPiece(), false);
				selectedTile = tiles[sRow][sCol];
				displayPiecesMoves(selTilePiece, true);
				// If the user selects a tile that is a valid move
			} else if (selectedTile.getPiece().isValidMove(sRow, sCol)) {
				displayPiecesMoves(selectedTile.getPiece(), false);
				if (isOnlineGame) {
					onlineGame.sendMove(selectedTile.getPiece().getBoardLocation(), new int[] { sRow, sCol });
				}
				selectedTile.getPiece().move(sRow, sCol);
				selectedTile = null;
				endPlayerTurn();
			}
		}
	}

	private void displayPiecesMoves(Piece piece, boolean display) {
		ArrayList<int[]> availableMoves = piece.getAvailableMoves();
		for (int i = 0; i < availableMoves.size(); i++) {
			if (display) {
				tiles[availableMoves.get(i)[0]][availableMoves.get(i)[1]].setAsValidMoveTile(true);
			} else {
				tiles[availableMoves.get(i)[0]][availableMoves.get(i)[1]].setAsValidMoveTile(false);
			}
		}
	}

	private void endPlayerTurn() {
		playPieceSoundEffect();
		switchPlayerTurns();
		checkIfKingInCheck();
		checkIfKingInCheckmate();
	}

	private void switchPlayerTurns() {
		if (playerTurn == Players.PLAYER_1) {
			playerTurn = Players.PLAYER_2;
		} else if (playerTurn == Players.PLAYER_2) {
			playerTurn = Players.PLAYER_1;
		}
	}

	private void checkIfKingInCheck() {
		King king = King.getKing(playerTurn);
		if (king.isInCheck()) {
			tiles[king.getRow()][king.getColumn()].setAsCheckedTile(true);
			ArrayList<Piece> piecesThatChecked = king.getPiecesThatChecked();
			System.out.println(piecesThatChecked.size());
			for (int i = 0; i < piecesThatChecked.size(); i++) {
				tiles[piecesThatChecked.get(i).getRow()][piecesThatChecked.get(i).getColumn()].setAsCheckedTile(true);
			}
		}
	}

	private void checkIfKingInCheckmate() {
		King king = King.getKing(playerTurn);
		if (king.isCheckmated()) {
			if (playerTurn == Players.PLAYER_1) {
				setWinner(Players.PLAYER_2);
			} else {
				setWinner(Players.PLAYER_1);
			}
		} else if (king.isStalemated()) {
			setWinner(null);
		}
	}

	public void initPromotionMenu(Pawn pawn) {
		frame.getContentPane().removeMouseListener(this);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				promotionMenu = new PromotionMenu(pawn);
			}
		});
	}

	private void playPieceSoundEffect() {
		try {
			GameData.resetSoundStreams();
			GameData.soundPlayer.open(GameData.pieceSoundEffect);
			GameData.soundPlayer.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setWinner(Players player) {
		if (player == Players.PLAYER_1) {
			JOptionPane.showMessageDialog(null, "Player 1 wins!", "Winner", JOptionPane.INFORMATION_MESSAGE);
		} else if (player == Players.PLAYER_2) {
			JOptionPane.showMessageDialog(null, "Player 2 wins!", "Winner", JOptionPane.INFORMATION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(null, "Stalemate!", "Winner", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	public void setAsOnlineGame(boolean isOnlineGame) {
		this.isOnlineGame = isOnlineGame;
	}

	public void startTimer() {
		if (!timer.isRunning()) {
			timer.start();
		}
	}

	public boolean isOnlineGame() {
		return isOnlineGame;
	}

	public OnlineGame getOnlineGame() {
		return onlineGame;
	}

	public JFrame getFrame() {
		return frame;
	}

	public Tile[][] getTiles() {
		return tiles;
	}

	public ArrayList<Piece> getPlayerPieces() {
		ArrayList<Piece> combinedPieces = new ArrayList<Piece>();
		combinedPieces.addAll(player1Pieces);
		combinedPieces.addAll(player2Pieces);
		return combinedPieces;
	}

	public ArrayList<Piece> getPlayer1Pieces() {
		return player1Pieces;
	}

	public ArrayList<Piece> getDeadPlayer1Pieces() {
		return deadPlayer1Pieces;
	}

	public ArrayList<Piece> getPlayer2Pieces() {
		return player2Pieces;
	}

	public ArrayList<Piece> getDeadPlayer2Pieces() {
		return deadPlayer2Pieces;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (gameState == GameStates.IN_GAME) {
			renderer.repaint();
		}
		if (isOnlineGame && playerTurn != onlineGame.getOwnPlayer()) {
			checkForIncomingData();
		}
		if (playerTurn == Players.PLAYER_1) {
			GameData.PLAYER_1_TIMER_SECONDS--;
		} else {
			GameData.PLAYER_2_TIMER_SECONDS--;
		}
	}

	private void checkForIncomingData() {
		switch (onlineGame.getIncomingDataHeader()) {
		case PIECE_MOVE:
			processIncomingPieceMove();
			endPlayerTurn();
			break;
		case PAWN_PROMOTION:
			Piece[] pawnPromotion = onlineGame.getPawnPromotion();
			Pawn pawnToBePromoted = (Pawn) pawnPromotion[0];
			Piece pieceToBePromotedTo = pawnPromotion[1];
			onlineGame.ignoreDataHeader();
			processIncomingPieceMove();
			tiles[pawnToBePromoted.getRow()][pawnToBePromoted.getColumn()].getPiece().kill();
			if (pieceToBePromotedTo.getPlayer() == Players.PLAYER_1) {
				player1Pieces.add(pieceToBePromotedTo);
			} else {
				player2Pieces.add(pieceToBePromotedTo);
			}
			pieceToBePromotedTo.move(pawnToBePromoted.getRow(), pawnToBePromoted.getColumn());
			endPlayerTurn();
			break;
		}
	}

	private void processIncomingPieceMove() {
		int[][] move = onlineGame.getOpponentMove();
		tiles[move[0][0]][move[0][1]].getPiece().move(move[1][0], move[1][1]);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		if (!timer.isRunning()) {
			timer.start();
		}
		Tile.resetCheckedTiles();
		if (e.getX() > GameData.PLAYER_PANEL_WIDTH && e.getX() < GameData.BOARD_WIDTH + GameData.PLAYER_PANEL_WIDTH) {
			int sRow = (int) Math.floor((e.getY()) / GameData.TILE_HEIGHT);
			int sCol = (int) Math.floor((e.getX() - GameData.PLAYER_PANEL_WIDTH) / GameData.TILE_WIDTH);
			if (sRow < 8 && sCol < 8) {
				processTileSelection(sRow, sCol);
			}
		}
		renderer.repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	private static void addShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (Game.game.isOnlineGame) {
					Game.game.onlineGame.close();
				}
			}
		}));
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				game = new Game();
				game.setGameState(GameStates.MENU);
			}
		});
		addShutdownHook();
	}

}
