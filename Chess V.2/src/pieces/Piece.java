package pieces;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;

import game.Game;
import game.GameData;
import game.GameData.Players;

public abstract class Piece implements Serializable{

	protected int row, column;
	private int deathSlot;
	private boolean isDead;
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
		ArrayList<int[]> availableMoves = getAvailableMovesDisregardCheck();
		availableMoves.addAll(getSpecialMoves());
		for (int i = availableMoves.size() - 1; i >= 0; i--) {
			if (willBeInCheck(availableMoves.get(i)[0], availableMoves.get(i)[1])) {
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

	public ArrayList<int[]> getSpecialMoves() {
		return new ArrayList<int[]>();
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
	
	public int[] getBoardLocation() {
		return new int[] {row, column};
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
		Piece pieceAtTile = Game.game.getTiles()[row][column].getPiece();
		if (pieceAtTile != null) {
			System.out.println("PEICE KILLED");
			pieceAtTile.kill();
		}
		this.row = row;
		this.column = column;
	}

	public void kill() {
		if (player == Players.PLAYER_1) {
			Game.game.getPlayer1Pieces().remove(this);
			Game.game.getDeadPlayer1Pieces().add(this);
			deathSlot = Game.game.getDeadPlayer1Pieces().size() - 1;
		} else {
			Game.game.getPlayer2Pieces().remove(this);
			Game.game.getDeadPlayer2Pieces().add(this);
			deathSlot = Game.game.getDeadPlayer2Pieces().size() - 1;
		}
		isDead = true;
	}

	public void render(Graphics g) {
		if (!isDead) {
			g.drawImage(spriteToUse, column * GameData.TILE_WIDTH + GameData.PIECE_SHRINK_SCALE,
					row * GameData.TILE_WIDTH + GameData.PIECE_SHRINK_SCALE,
					GameData.TILE_WIDTH - (GameData.PIECE_SHRINK_SCALE * 2),
					GameData.TILE_HEIGHT - (GameData.PIECE_SHRINK_SCALE * 2), null);
		} else {
			int row = deathSlot / 2;
			double column = deathSlot % 2;
			if(deathSlot == 14) {
				column = .5;
			}
			g.drawImage(spriteToUse, (int) ((column * GameData.DEATH_SLOT_WIDTH) + GameData.PIECE_SHRINK_SCALE_WHEN_DEAD),
					100 + (row * GameData.DEATH_SLOT_HEIGHT) + GameData.PIECE_SHRINK_SCALE_WHEN_DEAD,
					GameData.DEATH_SLOT_WIDTH - (GameData.PIECE_SHRINK_SCALE_WHEN_DEAD * 2),
					GameData.DEATH_SLOT_HEIGHT - (GameData.PIECE_SHRINK_SCALE_WHEN_DEAD * 2), null);
		}
	}

}
