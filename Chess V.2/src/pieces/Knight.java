package pieces;

import java.util.ArrayList;

import game.GameData.Players;

public class Knight extends Piece {

	public Knight(int row, int column, Players player) {
		super(row, column, player);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ArrayList<int[]> getAllMoves() {
		// TODO Auto-generated method stub
		ArrayList<int[]> availableMoves = new ArrayList<int[]>();
		availableMoves.add(new int[] { row + 2, column + 1 });
		availableMoves.add(new int[] { row + 2, column - 1 });
		availableMoves.add(new int[] { row - 2, column + 1 });
		availableMoves.add(new int[] { row - 2, column - 1 });
		availableMoves.add(new int[] { row + 1, column + 2 });
		availableMoves.add(new int[] { row - 1, column + 2 });
		availableMoves.add(new int[] { row + 1, column - 2 });
		availableMoves.add(new int[] { row - 1, column - 2 });
		return availableMoves;
	}

}
