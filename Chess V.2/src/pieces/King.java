package pieces;

import java.util.ArrayList;

import game.Game;
import game.GameData.Players;

public class King extends Piece {

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

	public boolean isInCheck() {
		ArrayList<Piece> enemyPieces;
		if (player == Players.PLAYER_1) {
			enemyPieces = Game.game.getPlayer2Pieces();
		} else {
			enemyPieces = Game.game.getPlayer1Pieces();
		}
		int listSize = enemyPieces.size();
		for(int i = 0; i < listSize; i++) {
			ArrayList<int[]> availableMoves = enemyPieces.get(i).getAvailableMovesDisregardCheck();
			for(int j = 0; j < availableMoves.size(); j++) {
				if(row == availableMoves.get(j)[0] && column == availableMoves.get(j)[1]) {
					return true;
				}
			}
		}
		return false;
	}

	public static King getKing(Players player) {
		ArrayList<Piece> playerPieces;
		if(player == Players.PLAYER_1) {
			playerPieces = Game.game.getPlayer1Pieces();
		}else {
			playerPieces = Game.game.getPlayer2Pieces();
		}
		for (int i = 0; i < playerPieces.size(); i++) {
			if (playerPieces.get(i) instanceof King) {
				return (King) playerPieces.get(i);
			}
		}
		return null;
	}

}
