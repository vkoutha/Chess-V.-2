package pieces;

import java.util.ArrayList;

import game.Game;
import game.GameData.Players;

public class Queen extends Piece{

	public Queen(int row, int column, Players player) {
		super(row, column, player);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ArrayList<int[]> getAllMoves() {
		// TODO Auto-generated method stub
		ArrayList<int[]> availableMoves = new ArrayList<int[]>();
		for (int r = row + 1; !inInvalidLocation(r, column); r++) {
			if (Game.game.getTiles()[r][column].getPiece() != null
					&& Game.game.getTiles()[r][column].containsEnemy(player)) {
				availableMoves.add(new int[] { r, column });
				break;
			} else if (Game.game.getTiles()[r][column].getPiece() != null
					&& Game.game.getTiles()[r][column].containsAlly(player)) {
				break;
			} else {
				availableMoves.add(new int[] { r, column });
			}
		}
		for (int r = row - 1; !inInvalidLocation(r, column); r--) {
			if (Game.game.getTiles()[r][column].getPiece() != null
					&& Game.game.getTiles()[r][column].containsEnemy(player)) {
				availableMoves.add(new int[] { r, column });
				break;
			} else if (Game.game.getTiles()[r][column].getPiece() != null
					&& Game.game.getTiles()[r][column].containsAlly(player)) {
				break;
			} else {
				availableMoves.add(new int[] { r, column });
			}
		}
		for (int c = column + 1; !inInvalidLocation(row, c); c++) {
			if (Game.game.getTiles()[row][c].getPiece() != null
					&& Game.game.getTiles()[row][c].containsEnemy(player)) {
				availableMoves.add(new int[] { row, c });
				break;
			} else if (Game.game.getTiles()[row][c].getPiece() != null
					&& Game.game.getTiles()[row][c].containsAlly(player)) {
				break;
			} else {
				availableMoves.add(new int[] { row, c });
			}
		}
		for (int c = column - 1; !inInvalidLocation(row, c); c--) {
			if (Game.game.getTiles()[row][c].getPiece() != null
					&& Game.game.getTiles()[row][c].containsEnemy(player)) {
				availableMoves.add(new int[] { row, c });
				break;
			} else if (Game.game.getTiles()[row][c].getPiece() != null
					&& Game.game.getTiles()[row][c].containsAlly(player)) {
				break;
			} else {
				availableMoves.add(new int[] { row, c });
			}
		}
		for (int r = row + 1, c = column + 1; !inInvalidLocation(r, c); r++, c++) {
			if (Game.game.getTiles()[r][c].getPiece() != null && Game.game.getTiles()[r][c].containsEnemy(player)) {
				availableMoves.add(new int[] { r, c });
				break;
			} else if (Game.game.getTiles()[r][c].getPiece() != null
					&& Game.game.getTiles()[r][c].containsAlly(player)) {
				break;
			} else {
				availableMoves.add(new int[] { r, c });
			}
		}
		for (int r = row + 1, c = column - 1; !inInvalidLocation(r, c); r++, c--) {
			if (Game.game.getTiles()[r][c].getPiece() != null && Game.game.getTiles()[r][c].containsEnemy(player)) {
				availableMoves.add(new int[] { r, c });
				break;
			} else if (Game.game.getTiles()[r][c].getPiece() != null
					&& Game.game.getTiles()[r][c].containsAlly(player)) {
				break;
			}else {
				availableMoves.add(new int[] { r, c });
			}
		}
		for (int r = row - 1, c = column - 1; !inInvalidLocation(r, c); r--, c--) {
			if (Game.game.getTiles()[r][c].getPiece() != null && Game.game.getTiles()[r][c].containsEnemy(player)) {
				availableMoves.add(new int[] { r, c });
				break;
			} else if (Game.game.getTiles()[r][c].getPiece() != null
					&& Game.game.getTiles()[r][c].containsAlly(player)) {
				break;
			}else {
				availableMoves.add(new int[] { r, c });
			}
		}
		for (int r = row - 1, c = column + 1; !inInvalidLocation(r, c); r--, c++) {
			if (Game.game.getTiles()[r][c].getPiece() != null && Game.game.getTiles()[r][c].containsEnemy(player)) {
				availableMoves.add(new int[] { r, c });
				break;
			} else if (Game.game.getTiles()[r][c].getPiece() != null
					&& Game.game.getTiles()[r][c].containsAlly(player)) {
				break;
			}else {
				availableMoves.add(new int[] { r, c });
			}
		}
		return availableMoves;
	}

}
