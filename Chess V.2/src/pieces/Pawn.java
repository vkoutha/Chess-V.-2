package pieces;

import java.util.ArrayList;

import game.Game;
import game.GameData.Players;
import network.OnlineGame;

public class Pawn extends Piece {

	private int moveCount;

	public Pawn(int row, int column, Players player) {
		super(row, column, player);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ArrayList<int[]> getAllMoves() {
		// TODO Auto-generated method stub
		ArrayList<int[]> availableMoves = new ArrayList<int[]>();
		if (player == Players.PLAYER_1) {
			if (!inInvalidLocation(row - 1, column) && Game.game.getTiles()[row - 1][column].getPiece() == null) {
				availableMoves.add(new int[] { row - 1, column });
			}
			if (!inInvalidLocation(row - 2, column) && moveCount == 0
					&& Game.game.getTiles()[row - 2][column].getPiece() == null) {
				availableMoves.add(new int[] { row - 2, column });
			}
			if (!inInvalidLocation(row - 1, column - 1) && Game.game.getTiles()[row - 1][column - 1].getPiece() != null
					&& Game.game.getTiles()[row - 1][column - 1].containsEnemy(player)) {
				availableMoves.add(new int[] { row - 1, column - 1 });
			}
			if (!inInvalidLocation(row - 1, column + 1) && Game.game.getTiles()[row - 1][column + 1].getPiece() != null
					&& Game.game.getTiles()[row - 1][column + 1].containsEnemy(player)) {
				availableMoves.add(new int[] { row - 1, column + 1 });
			}
		} else {
			if (!inInvalidLocation(row + 1, column) && Game.game.getTiles()[row + 1][column].getPiece() == null) {
				availableMoves.add(new int[] { row + 1, column });
			}
			if (!inInvalidLocation(row + 2, column) && moveCount == 0
					&& Game.game.getTiles()[row + 2][column].getPiece() == null) {
				availableMoves.add(new int[] { row + 2, column });
			}
			if (!inInvalidLocation(row + 1, column - 1) && Game.game.getTiles()[row + 1][column - 1].getPiece() != null
					&& Game.game.getTiles()[row + 1][column - 1].containsEnemy(player)) {
				availableMoves.add(new int[] { row + 1, column - 1 });
			}
			if (!inInvalidLocation(row + 1, column + 1) && Game.game.getTiles()[row + 1][column + 1].getPiece() != null
					&& Game.game.getTiles()[row + 1][column + 1].containsEnemy(player)) {
				availableMoves.add(new int[] { row + 1, column + 1 });
			}
		}
		return availableMoves;
	}

	public void move(int row, int column) {
		super.move(row, column);
		moveCount++;
		if (row == (player == Players.PLAYER_1 ? 0 : 7)) {
			if (!Game.game.isOnlineGame()) {
				Game.game.initPromotionMenu(this);
			} else {
				if(player == Game.game.getOnlineGame().getOwnPlayer()) {
					Game.game.initPromotionMenu(this);
				}
			}
		}
	}

}
