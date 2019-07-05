package pieces;

import java.util.ArrayList;

import game.Game;
import game.GameData.Players;

public class King extends Piece {

	private int moveCount;

	public King(int row, int column, Players player) {
		super(row, column, player);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ArrayList<int[]> getAllMoves() {
		// TODO Auto-generated method stub
		ArrayList<int[]> availableMoves = new ArrayList<int[]>();
		availableMoves.add(new int[] { row + 1, column });
		availableMoves.add(new int[] { row - 1, column });
		availableMoves.add(new int[] { row, column + 1 });
		availableMoves.add(new int[] { row, column - 1 });
		availableMoves.add(new int[] { row + 1, column + 1 });
		availableMoves.add(new int[] { row + 1, column - 1 });
		availableMoves.add(new int[] { row - 1, column - 1 });
		availableMoves.add(new int[] { row - 1, column + 1 });
		return availableMoves;
	}

	@Override
	public ArrayList<int[]> getSpecialMoves() {
		ArrayList<int[]> specialMoves = new ArrayList<int[]>();
		if (!isInCheck()) {
			if (moveCount == 0) {
				Piece leftRook = Game.game.getTiles()[row][0].getPiece();
				Piece rightRook = Game.game.getTiles()[row][7].getPiece();
				if (leftRook != null && leftRook instanceof Rook && ((Rook) leftRook).getMoveCount() == 0
						&& Game.game.getTiles()[row][1].getPiece() == null
						&& Game.game.getTiles()[row][2].getPiece() == null
						&& Game.game.getTiles()[row][3].getPiece() == null) {
					specialMoves.add(new int[] { row, column - 2 });
				}
				if (rightRook != null && rightRook instanceof Rook && ((Rook) rightRook).getMoveCount() == 0
						&& Game.game.getTiles()[row][6].getPiece() == null
						&& Game.game.getTiles()[row][5].getPiece() == null) {
					specialMoves.add(new int[] { row, column + 2 });
				}
			}
		}
		return specialMoves;
	}

	public boolean isInCheck() {
		ArrayList<Piece> enemyPieces;
		if (player == Players.PLAYER_1) {
			enemyPieces = Game.game.getPlayer2Pieces();
		} else {
			enemyPieces = Game.game.getPlayer1Pieces();
		}
		int listSize = enemyPieces.size();
		for (int i = 0; i < listSize; i++) {
			ArrayList<int[]> availableMoves = enemyPieces.get(i).getAvailableMovesDisregardCheck();
			for (int j = 0; j < availableMoves.size(); j++) {
				if (row == availableMoves.get(j)[0] && column == availableMoves.get(j)[1]) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean isCheckmated() {
		ArrayList<Piece> pieces;
		if (player == Players.PLAYER_1) {
			pieces = Game.game.getPlayer1Pieces();
		} else {
			pieces = Game.game.getPlayer2Pieces();
		}
		int totalMoveCount = 0;
		for(int i = 0; i < pieces.size(); i++) {
			totalMoveCount += pieces.get(i).getAvailableMoves().size();
		}
		return totalMoveCount == 0 && isInCheck();
	}
	
	public boolean isStalemated() {
		ArrayList<Piece> pieces;
		if (player == Players.PLAYER_1) {
			pieces = Game.game.getPlayer1Pieces();
		} else {
			pieces = Game.game.getPlayer2Pieces();
		}
		int totalMoveCount = 0;
		for(int i = 0; i < pieces.size(); i++) {
			totalMoveCount += pieces.get(i).getAvailableMoves().size();
		}
		return totalMoveCount == 0 && !isInCheck();
	}

	public ArrayList<Piece> getPiecesThatChecked() {
		ArrayList<Piece> piecesThatChecked = new ArrayList<Piece>();
		ArrayList<Piece> playerPieces = new ArrayList<Piece>();
		if (player == Players.PLAYER_1) {
			playerPieces = Game.game.getPlayer2Pieces();
		} else {
			playerPieces = Game.game.getPlayer1Pieces();
		}
		for (int i = 0; i < playerPieces.size(); i++) {
			ArrayList<int[]> moves = playerPieces.get(i).getAvailableMoves();
			for (int j = 0; j < moves.size(); j++) {
				if (row == moves.get(j)[0] && column == moves.get(j)[1]) {
					piecesThatChecked.add(playerPieces.get(i));
				}
			}
		}
		return piecesThatChecked;
	}

	public static King getKing(Players player) {
		ArrayList<Piece> playerPieces;
		if (player == Players.PLAYER_1) {
			playerPieces = Game.game.getPlayer1Pieces();
		} else {
			playerPieces = Game.game.getPlayer2Pieces();
		}
		for (int i = 0; i < playerPieces.size(); i++) {
			if (playerPieces.get(i) instanceof King) {
				return (King) playerPieces.get(i);
			}
		}
		return null;
	}

	public void move(int row, int column) {
		ArrayList<int[]> specialMoves = getSpecialMoves();
		for (int i = 0; i < specialMoves.size(); i++) {
			if (row == specialMoves.get(i)[0] && column == specialMoves.get(i)[1]) {
				if (specialMoves.get(i)[1] == this.column - 2) {
					Game.game.getTiles()[row][0].getPiece().move(row, 3);
				} else if (specialMoves.get(i)[1] == this.column + 2) {
					Game.game.getTiles()[row][7].getPiece().move(row, 5);
				}
			}
		}
		super.move(row, column);
		moveCount++;
	}

}
