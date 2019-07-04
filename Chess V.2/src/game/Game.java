package game;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import game.GameData.Players;
import game.Renderer.BlackPanel;
import game.Renderer.BoardPanel;
import game.Renderer.Panels;
import game.Renderer.WhitePanel;
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
	private Renderer renderer;
	private Renderer.WhitePanel whitePlayerPanel;
	private Renderer.BlackPanel blackPlayerPanel;
	private Renderer.BoardPanel boardPanel;
	private Timer timer;
	private Tile[][] tiles;
	private Tile selectedTile;
	private ArrayList<Piece> player1Pieces;
	private ArrayList<Piece> player2Pieces;
	private Players playerTurn;

	public Game() {
		initFrame();
		initGame();
		timer = new Timer(10, this);
		timer.start();
	}

	private void initFrame() {
		frame = new JFrame("Chess Revamped!");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		whitePlayerPanel = new WhitePanel();
		whitePlayerPanel.setPreferredSize(new Dimension(GameData.PLAYER_PANEL_WIDTH, GameData.PLAYER_PANEL_HEIGHT));
		blackPlayerPanel = new BlackPanel();
		blackPlayerPanel.setPreferredSize(new Dimension(GameData.PLAYER_PANEL_WIDTH, GameData.PLAYER_PANEL_HEIGHT));
		boardPanel = new BoardPanel();
		boardPanel.setPreferredSize(new Dimension(GameData.BOARD_WIDTH, GameData.BOARD_HEIGHT));
		renderer = () -> {
			whitePlayerPanel.repaint();
			blackPlayerPanel.repaint();
			boardPanel.repaint();
		};
		frame.getContentPane().addMouseListener(this);
		frame.add(whitePlayerPanel, BorderLayout.LINE_START);
		frame.add(boardPanel, BorderLayout.CENTER);
		frame.add(blackPlayerPanel, BorderLayout.LINE_END);
		frame.setVisible(true);
		frame.pack();
		frame.setLocationRelativeTo(null);
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
		case WHITE:

			break;
		case BLACK:

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
				System.out.println("MOVED");
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
				tiles[availableMoves.get(i)[0]][availableMoves.get(i)[1]].setValidMoveTile(true);
			} else {
				tiles[availableMoves.get(i)[0]][availableMoves.get(i)[1]].setValidMoveTile(false);
			}
		}
	}

	private void endPlayerTurn() {
		if (playerTurn == Players.PLAYER_1) {
			playerTurn = Players.PLAYER_2;
		} else if (playerTurn == Players.PLAYER_2) {
			playerTurn = Players.PLAYER_1;
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

	public ArrayList<Piece> getPlayer2Pieces() {
		return player2Pieces;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		renderer.repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
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
