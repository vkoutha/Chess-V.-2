package game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import game.GameData.Players;
import pieces.Piece;

public class Tile {

	private int row, column;
	private Color color;
	private boolean isValidMoveTile;

	public Tile(int row, int column) {
		this.row = row;
		this.column = column;
		if (row % 2 == 0) {
			if (column % 2 == 0) {
				color = GameData.WHITE_TILE_COLOR;
			} else {
				color = GameData.BROWN_TILE_COLOR;
			}
		} else if (row % 2 == 1) {
			if (column % 2 == 0) {
				color = GameData.BROWN_TILE_COLOR;
			} else {
				color = GameData.WHITE_TILE_COLOR;
			}
		}
	}
	
	public void setValidMoveTile(boolean validMoveTile) {
		isValidMoveTile = validMoveTile;
	}

	public boolean containsAlly(Players player) {
		if (getPiece().getPlayer() == player) {
			return true;
		}
		return false;
	}

	public boolean containsEnemy(Players player) {
		if (getPiece().getPlayer() != player) {
			return true;
		}
		return false;
	}

	public Piece getPiece() {
		ArrayList<Piece> gamePieces = Game.game.getPlayerPieces();
		for (int i = 0; i < gamePieces.size(); i++) {
			if (row == gamePieces.get(i).getRow() && column == gamePieces.get(i).getColumn()) {
				return gamePieces.get(i);
			}
		}
		return null;
	}

	public void render(Graphics g) {
		g.setColor(color);
		g.fillRect(column * GameData.TILE_WIDTH, row * GameData.TILE_HEIGHT, GameData.TILE_WIDTH, GameData.TILE_HEIGHT);
		if (isValidMoveTile) {
			if (getPiece() == null) {
				g.setColor(GameData.VALID_MOVE_TILE_COLOR);
				g.fillOval(column * GameData.TILE_WIDTH + GameData.VALID_MOVE_CIRCLE_SHRINK_SIZE,
						row * GameData.TILE_HEIGHT + GameData.VALID_MOVE_CIRCLE_SHRINK_SIZE,
						GameData.TILE_WIDTH - (GameData.VALID_MOVE_CIRCLE_SHRINK_SIZE * 2),
						GameData.TILE_WIDTH - (GameData.VALID_MOVE_CIRCLE_SHRINK_SIZE * 2));
			} else {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setStroke(new BasicStroke(3));
				g2.setColor(GameData.VALID_MOVE_TILE_COLOR);
				g2.drawRect(column * GameData.TILE_WIDTH, row * GameData.TILE_HEIGHT, GameData.TILE_WIDTH - 1,
						GameData.TILE_HEIGHT - 1);
			}
		}
	}

}
