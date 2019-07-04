package game;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import pieces.Piece;

public class GameData {

	public static final int FRAME_HEIGHT = 800;
	public static final int PLAYER_PANEL_WIDTH = 200;
	public static final int PLAYER_PANEL_HEIGHT = FRAME_HEIGHT;
	public static final int BOARD_WIDTH = 800;
	public static final int BOARD_HEIGHT = FRAME_HEIGHT;
	public static final int FRAME_WIDTH = BOARD_WIDTH + PLAYER_PANEL_WIDTH;
	public static final int UPDATE_SPEED_MS = 50;

	public static final int ROWS = 8;
	public static final int COLUMNS = 8;

	public static final int TILE_WIDTH = BOARD_WIDTH / COLUMNS;
	public static final int TILE_HEIGHT = BOARD_HEIGHT / ROWS;

	public static final int VALID_MOVE_CIRCLE_SHRINK_SIZE = 42;

	public static final Color BROWN_TILE_COLOR = new Color(153, 76, 0);
	public static final Color WHITE_TILE_COLOR = new Color(255, 178, 102);
	public static final Color SELECTED_TILE_COLOR = new Color(255, 220, 46);
	public static final Color VALID_MOVE_TILE_COLOR = new Color(7, 140, 0);
	public static final Color IN_CHECK_TILE_COLOR = new Color(135, 0, 0);

	public static BufferedImage pieceSpriteSheet;
	// index 0 corresponds to Player 1 sprite, index 1 corresponds to Player 2
	// sprite
	public static BufferedImage[] pawnSprite, knightSprite, bishopSprite, rookSprite, queenSprite, kingSprite;

	public enum Players {
		PLAYER_1, PLAYER_2
	}

	static {
		try {
			pieceSpriteSheet = ImageIO.read(GameData.class.getResource("/img/chessSpriteSheet.png"));

			pawnSprite = new BufferedImage[2];
			pawnSprite[0] = pieceSpriteSheet.getSubimage(1113, 39, 1228 - 1113, 190 - 39);
			pawnSprite[1] = pieceSpriteSheet.getSubimage(1113, 253, 1228 - 1113, 403 - 253);

			knightSprite = new BufferedImage[2];
			knightSprite[0] = pieceSpriteSheet.getSubimage(665, 30, 823 - 665, 188 - 30);
			knightSprite[1] = pieceSpriteSheet.getSubimage(665, 243, 823 - 665, 401 - 243);

			bishopSprite = new BufferedImage[2];
			bishopSprite[0] = pieceSpriteSheet.getSubimage(452, 23, 614 - 452, 187 - 23);
			bishopSprite[1] = pieceSpriteSheet.getSubimage(452, 236, 614 - 452, 400 - 236);

			rookSprite = new BufferedImage[2];
			rookSprite[0] = pieceSpriteSheet.getSubimage(892, 39, 1027 - 892, 188 - 39);
			rookSprite[1] = pieceSpriteSheet.getSubimage(892, 253, 1027 - 892, 401 - 253);

			queenSprite = new BufferedImage[2];
			queenSprite[0] = pieceSpriteSheet.getSubimage(229, 23, 410 - 229, 189 - 23);
			queenSprite[1] = pieceSpriteSheet.getSubimage(229, 238, 410 - 229, 408 - 238);

			kingSprite = new BufferedImage[2];
			kingSprite[0] = pieceSpriteSheet.getSubimage(24, 25, 188 - 24, 191 - 25);
			kingSprite[1] = pieceSpriteSheet.getSubimage(24, 238, 188 - 24, 404 - 238);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
