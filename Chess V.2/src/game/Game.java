package game;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import game.GameData.Players;
import game.Renderer.BoardPanel;
import game.Renderer.Panels;
import game.Renderer.Player1Panel;
import game.Renderer.Player2Panel;
import pieces.Bishop;
import pieces.King;
import pieces.Knight;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;

public class Game implements ActionListener, MouseListener {

	public static Game game;
	private JFrame frame;
	private JLabel player1TimerLabel, player2TimerLabel;
	private Renderer renderer;
	private Renderer.Player1Panel player1Panel;
	private Renderer.Player2Panel player2Panel;
	private Renderer.BoardPanel boardPanel;
	private Timer timer;
	private Tile[][] tiles;
	private Tile selectedTile;
	private ArrayList<Piece> player1Pieces, deadPlayer1Pieces;
	private ArrayList<Piece> player2Pieces, deadPlayer2Pieces;
	private Players playerTurn;

	public Game() {
		initFrame();
		initGame();
		timer = new Timer(GameData.UPDATE_SPEED_MS, this);
		System.out.println("ok");
	}

	private void initFrame() {
		frame = new JFrame("Chess Revamped!");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		player1Panel = new Player1Panel();
		player1Panel.setPreferredSize(new Dimension(GameData.PLAYER_PANEL_WIDTH, GameData.PLAYER_PANEL_HEIGHT));
		player1Panel.setBackground(GameData.PLAYER_PANEL_BACKGROUND_COLOR);
		player2Panel = new Player2Panel();
		player2Panel.setPreferredSize(new Dimension(GameData.PLAYER_PANEL_WIDTH, GameData.PLAYER_PANEL_HEIGHT));
		player2Panel.setBackground(GameData.PLAYER_PANEL_BACKGROUND_COLOR);
		boardPanel = new BoardPanel();
		boardPanel.setPreferredSize(new Dimension(GameData.BOARD_WIDTH, GameData.BOARD_HEIGHT));
		renderer = () -> {
			player1Panel.repaint();
			player2Panel.repaint();
			boardPanel.repaint();
		};
		frame.getContentPane().addMouseListener(this);
		frame.add(player1Panel, BorderLayout.LINE_START);
		frame.add(boardPanel, BorderLayout.CENTER);
		frame.add(player2Panel, BorderLayout.LINE_END);
		frame.setVisible(true);
		frame.pack();
		frame.setLocationRelativeTo(null);
		player1Panel.setLayout(null);
		player2Panel.setLayout(null);
		player1TimerLabel = new JLabel("Time remaining: ##:##");
		player1TimerLabel.setFont(new Font("Arial", Font.BOLD, 14));
		player1TimerLabel.setBounds(
				(GameData.PLAYER_PANEL_WIDTH / 2) - ((int) player1TimerLabel.getPreferredSize().getWidth() / 2), 50,
				(int) player1TimerLabel.getPreferredSize().getWidth(),
				(int) player1TimerLabel.getPreferredSize().getHeight());
		player1Panel.add(player1TimerLabel);
		player2TimerLabel = new JLabel("Time remaining: ##:##");
		player2TimerLabel.setFont(new Font("Arial", Font.BOLD, 14));
		player2TimerLabel.setBounds((GameData.PLAYER_PANEL_WIDTH / 2) - ((int) player2TimerLabel.getPreferredSize().getWidth() / 2), 50,
				(int) player2TimerLabel.getPreferredSize().getWidth(), (int) player2TimerLabel.getPreferredSize().getHeight());
		player2Panel.add(player2TimerLabel);
	}

	private void initGame() {
		initTiles();
		initPlayer1Pieces();
		initPlayer2Pieces();
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
			for (int row = 0; row < tiles.length; row++) {
				for (int col = 0; col < tiles[row].length; col++) {
					tiles[row][col].render(g);
				}
			}
			player1Pieces.forEach((piece) -> piece.render(g));
			player2Pieces.forEach((piece) -> piece.render(g));
			break;
		case PLAYER_1:
			int p1Minutes = GameData.PLAYER_1_TIMER_SECONDS / 60;
			int p1Seconds = GameData.PLAYER_1_TIMER_SECONDS % 60;
			deadPlayer1Pieces.forEach((piece) -> piece.render(g));
			player1TimerLabel.setText("Time remaining: " + p1Minutes + ":" + (p1Seconds < 10 ? "0" + p1Seconds : p1Seconds));
			break;
		case PLAYER_2:
			int p2Minutes = GameData.PLAYER_2_TIMER_SECONDS / 60;
			int p2Seconds = GameData.PLAYER_2_TIMER_SECONDS % 60;
			deadPlayer2Pieces.forEach((piece) -> piece.render(g));
			player2TimerLabel.setText("Time remaining: " + p2Minutes + ":" + (p2Seconds < 10 ? "0" + p2Seconds : p2Seconds));
			break;
		}
	}

	public void processTileSelection(int sRow, int sCol) {
		Piece selTilePiece = tiles[sRow][sCol].getPiece();
		// If no tile is currently selected
		if (selectedTile == null) {
			// If tile selected has a piece and it is the player's own piece
			if (selTilePiece != null && playerTurn == selTilePiece.getPlayer()) {
				selectedTile = tiles[sRow][sCol];
				displayPiecesMoves(selTilePiece, true);
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
		if (playerTurn == Players.PLAYER_1) {
			playerTurn = Players.PLAYER_2;
		} else if (playerTurn == Players.PLAYER_2) {
			playerTurn = Players.PLAYER_1;
		}
		King king = King.getKing(playerTurn);
		if (king.isInCheck()) {
			tiles[king.getRow()][king.getColumn()].setAsCheckedTile(true);
			ArrayList<Piece> piecesThatChecked = king.getPiecesThatChecked();
			System.out.println(piecesThatChecked.size());
			for (int i = 0; i < piecesThatChecked.size(); i++) {
				tiles[piecesThatChecked.get(i).getRow()][piecesThatChecked.get(i).getColumn()].setAsCheckedTile(true);
			}
		}
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
		renderer.repaint();
		GameData.PLAYER_1_TIMER_SECONDS--;
		GameData.PLAYER_2_TIMER_SECONDS--;
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

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				game = new Game();
			}
		});
	}

}
