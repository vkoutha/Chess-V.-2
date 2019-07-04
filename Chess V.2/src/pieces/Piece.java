package pieces;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import game.Game;
import game.GameData;
import game.GameData.Players;

public abstract class Piece {

	protected int row, column;
	protected Players player;
	protected BufferedImage spriteToUse;

	public Piece(int row, int column, Players player) {
		this.row = row;
		this.column = column;
		this.player = player;
		setSprite();
	}

	public abstract ArrayList<int[]> getAllMoves();

	public ArrayList<int[]> getAvailableMoves() {
		ArrayList<int[]> availableMoves = getAllMoves();
		for (int i = availableMoves.size() - 1; i >= 0; i--) {
			if (inInvalidLocation(availableMoves.get(i)[0], availableMoves.get(i)[1])) {
				availableMoves.remove(i);
			} else if (Game.game.getTiles()[availableMoves.get(i)[0]][availableMoves.get(i)[1]].getPiece() != null
					&& Game.game.getTiles()[availableMoves.get(i)[0]][availableMoves.get(i)[1]].getPiece()
							.getPlayer() == player) {
				availableMoves.remove(i);
			} else if (willBeInCheck(availableMoves.get(i)[0], availableMoves.get(i)[1])) {
				availableMoves.remove(i);
			}
		}
		return availableMoves;
	}

	protected ArrayList<int[]> getAvailableMovesDisregardCheck() {
		ArrayList<int[]> availableMoves = getAllMoves();
		for (int i = availableMoves.size() - 1; i >= 0; i--) {
			if (inInvalidLocation(availableMoves.get(i)[0], availableMoves.get(i)[1])) {
				availableMoves.remove(i);
			} else if (Game.game.getTiles()[availableMoves.get(i)[0]][availableMoves.get(i)[1]].getPiece() != null
					&& Game.game.getTiles()[availableMoves.get(i)[0]][availableMoves.get(i)[1]].getPiece()
							.getPlayer() == player) {
				availableMoves.remove(i);
			}
		}
		return availableMoves;
	}

	private boolean willBeInCheck(int row, int column) {
		Piece pieceToRemove = null;
		int ogRow = this.row;
		int ogCol = this.column;
		if (Game.game.getTiles()[row][column].getPiece() != null) {
			pieceToRemove = Game.game.getTiles()[row][column].getPiece();
			if (player == Players.PLAYER_1) {
				Game.game.getPlayer2Pieces().remove(pieceToRemove);
			} else {
				Game.game.getPlayer1Pieces().remove(pieceToRemove);
			}
		}
		this.row = row;
		this.column = column;
	//	boolean isInCheck = false;
		boolean isInCheck = King.getKing(player).isInCheck();
		if (pieceToRemove != null) {
			if (player == Players.PLAYER_1) {
				Game.game.getPlayer2Pieces().add(pieceToRemove);
			} else {
				Game.game.getPlayer1Pieces().add(pieceToRemove);
			}
		}
		this.row = ogRow;
		this.column = ogCol;
		return isInCheck;
	}

	public boolean isValidMove(int row, int column) {
		ArrayList<int[]> availableMoves = getAvailableMoves();
		for (int i = 0; i < availableMoves.size(); i++) {
			if (row == availableMoves.get(i)[0] && column == availableMoves.get(i)[1]) {
				return true;
			}
		}
		return false;
	}

	public boolean inInvalidLocation(int row, int column) {
		if (row < 0 || row >= GameData.ROWS) {
			return true;
		}
		if (column < 0 || column >= GameData.COLUMNS) {
			return true;
		}
		return false;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	public Players getPlayer() {
		return player;
	}

	private void setSprite() {
		if (this instanceof Pawn) {
			spriteToUse = player == Players.PLAYER_1 ? GameData.pawnSprite[0] : GameData.pawnSprite[1];
		} else if (this instanceof Knight) {
			spriteToUse = player == Players.PLAYER_1 ? GameData.knightSprite[0] : GameData.knightSprite[1];
		} else if (this instanceof Bishop) {
			spriteToUse = player == Players.PLAYER_1 ? GameData.bishopSprite[0] : GameData.bishopSprite[1];
		} else if (this instanceof Rook) {
			spriteToUse = player == Players.PLAYER_1 ? GameData.rookSprite[0] : GameData.rookSprite[1];
		} else if (this instanceof Queen) {
			spriteToUse = player == Players.PLAYER_1 ? GameData.queenSprite[0] : GameData.queenSprite[1];
		} else if (this instanceof King) {
			spriteToUse = player == Players.PLAYER_1 ? GameData.kingSprite[0] : GameData.kingSprite[1];
		}
	}

	public void move(int row, int column) {
		if (Game.game.getTiles()[row][column].getPiece() != null) {
			if (player == Players.PLAYER_1) {
				Game.game.getPlayer2Pieces().remove(Game.game.getTiles()[row][column].getPiece());
			} else {
				Game.game.getPlayer1Pieces().remove(Game.game.getTiles()[row][column].getPiece());
			}
		}
		this.row = row;
		this.column = column;
	}

	public void render(Graphics g) {
		g.drawImage(spriteToUse, column * GameData.TILE_WIDTH + 10, row * GameData.TILE_WIDTH + 10,
				GameData.TILE_WIDTH - 20, GameData.TILE_HEIGHT - 20, null);
	}

//	public Object clone(int row, int column, Players player) {
//		if (this instanceof Pawn) {
//			return new Pawn(row, column, player);
//		} else if (this instanceof Knight) {
//			return new Knight(row, column, player);
//		} else if (this instanceof Bishop) {
//			spriteToUse = player == Players.PLAYER_1 ? GameData.bishopSprite[0] : GameData.bishopSprite[1];
//		} else if (this instanceof Rook) {
//			spriteToUse = player == Players.PLAYER_1 ? GameData.rookSprite[0] : GameData.rookSprite[1];
//		} else if (this instanceof Queen) {
//			spriteToUse = player == Players.PLAYER_1 ? GameData.queenSprite[0] : GameData.queenSprite[1];
//		} else if (this instanceof King) {
//			spriteToUse = player == Players.PLAYER_1 ? GameData.kingSprite[0] : GameData.kingSprite[1];
//		}
//	}

}
