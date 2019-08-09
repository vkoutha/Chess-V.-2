package game;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class GameData {

	public static final int FRAME_HEIGHT = 800;
	public static final int PLAYER_PANEL_WIDTH = 200;
	public static final int PLAYER_PANEL_HEIGHT = FRAME_HEIGHT;
	public static final int BOARD_WIDTH = 800;
	public static final int BOARD_HEIGHT = FRAME_HEIGHT;
	public static final int FRAME_WIDTH = BOARD_WIDTH + PLAYER_PANEL_WIDTH;
	public static final int PROMOTION_MENU_WIDTH = 400;
	public static final int PROMOTION_MENU_HEIGHT = 200;
	public static final int UPDATE_SPEED_MS = 1000;

	public static final int ROWS = 8;
	public static final int COLUMNS = 8;

	public static final int TILE_WIDTH = BOARD_WIDTH / COLUMNS;
	public static final int TILE_HEIGHT = BOARD_HEIGHT / ROWS;

	public static final int DEATH_SLOT_WIDTH = PLAYER_PANEL_WIDTH / 2;
	public static final int DEATH_SLOT_HEIGHT = PLAYER_PANEL_HEIGHT / 9;

	public static final int PIECE_SHRINK_SCALE = 10;
	public static final int PIECE_SHRINK_SCALE_WHEN_DEAD = 10;
	public static final int VALID_MOVE_CIRCLE_SHRINK_SCALE = 42;

	public static int PLAYER_1_TIMER_SECONDS = 10 * 60;
	public static int PLAYER_2_TIMER_SECONDS = 10 * 60;

	public static final String FILE_DIRECTORY = "";

	public static final int CONNECTION_TIMEOUT_MS = 5000;
	public static final int NETWORK_PORT = 1119;

	public static final Color BROWN_TILE_COLOR = new Color(153, 76, 0);
	public static final Color WHITE_TILE_COLOR = new Color(255, 178, 102);
	public static final Color SELECTED_TILE_COLOR = new Color(255, 220, 46);
	public static final Color VALID_MOVE_TILE_COLOR = new Color(7, 140, 0);
	public static final Color IN_CHECK_TILE_COLOR = new Color(135, 0, 0);
	public static final Color PLAYER_PANEL_BACKGROUND_COLOR = new Color(240, 205, 120);

	private static BufferedImage pieceSpriteSheet;
	// index 0 corresponds to Player 1 sprite, index 1 corresponds to Player 2
	// sprite
	public static BufferedImage[] PAWN_SPRITE, KNIGHT_SPRITE, BISHOP_SPRITE, ROOK_SPRITE, QUEEN_SPRITE, KING_SPRITE;

	public static Icon[] SINGLE_PLAYER_ICON, TWO_PLAYER_ICON, START_LOCAL_GAME_ICON, JOIN_LOCAL_GAME_ICON, LOAD_GAME_ICON;
	public static Icon[] KNIGHT_ICON, BISHOP_ICON, ROOK_ICON, QUEEN_ICON;

	public static AudioInputStream PIECE_SOUND_EFFECT;
	public static Clip SOUND_PLAYER;

	public enum GameStates {
		MENU, SEARCHING, LOAD_GAME, IN_GAME
	}

	public enum Players {
		PLAYER_1, PLAYER_2
	}

	public enum DataTransferHeaders {
		PIECE_MOVE, PAWN_PROMOTION
	}

	public static void removeBackground(JButton button) {
		button.setContentAreaFilled(false);
		button.setFocusPainted(false);
		button.setBorderPainted(false);
	}

	private static void initButtonIcons() {
		initMainMenuButtonIcons();
		initPromotionMenuButtonIcons();
	}

	private static void initMainMenuButtonIcons() {
		try {
			// Befunky.com Komilka Axis 81px
			BufferedImage[] singlePlayerImg = { getImageFromFile("/img/buttons/unselected/singlePlayerUnselected.png"),
					getImageFromFile("/img/buttons/selected/singlePlayerSelected.png") };
			BufferedImage[] twoPlayerImg = { getImageFromFile("/img/buttons/unselected/twoPlayerUnselected.png"),
					getImageFromFile("/img/buttons/selected/twoPlayerSelected.png") };
			BufferedImage[] startLocalGameImg = {
					getImageFromFile("/img/buttons/unselected/startLocalGameUnselected.png"),
					getImageFromFile("/img/buttons/selected/startLocalGameSelected.png") };
			BufferedImage[] joinLocalGameImg = {
					getImageFromFile("/img/buttons/unselected/joinLocalGameUnselected.png"),
					getImageFromFile("/img/buttons/selected/joinLocalGameSelected.png") };
			BufferedImage[] loadGameImg = { getImageFromFile("/img/buttons/unselected/loadGameUnselected.png"),
					getImageFromFile("/img/buttons/selected/loadGameSelected.png") };
			SINGLE_PLAYER_ICON = new Icon[2];
			TWO_PLAYER_ICON = new Icon[2];
			START_LOCAL_GAME_ICON = new Icon[2];
			JOIN_LOCAL_GAME_ICON = new Icon[2];
			LOAD_GAME_ICON = new Icon[2];
			for (int i = 0; i < 2; i++) {
				TWO_PLAYER_ICON[i] = new ImageIcon(
						twoPlayerImg[i].getScaledInstance(GameData.BOARD_WIDTH / 2, 80, Image.SCALE_DEFAULT));
				SINGLE_PLAYER_ICON[i] = new ImageIcon(
						singlePlayerImg[i].getScaledInstance(GameData.BOARD_WIDTH / 2, 80, Image.SCALE_DEFAULT));
				START_LOCAL_GAME_ICON[i] = new ImageIcon(
						startLocalGameImg[i].getScaledInstance(GameData.BOARD_WIDTH / 2, 80, Image.SCALE_DEFAULT));
				JOIN_LOCAL_GAME_ICON[i] = new ImageIcon(
						joinLocalGameImg[i].getScaledInstance(GameData.BOARD_WIDTH / 2, 80, Image.SCALE_DEFAULT));
				LOAD_GAME_ICON[i] = new ImageIcon(
						loadGameImg[i].getScaledInstance(GameData.BOARD_WIDTH / 2, 80, Image.SCALE_DEFAULT));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void initPromotionMenuButtonIcons() {
		int width = 75;
		int height = 150;
		try {
			KNIGHT_ICON = new Icon[2];
			for (int i = 0; i < KNIGHT_ICON.length; i++) {
				KNIGHT_ICON[i] = new ImageIcon(KNIGHT_SPRITE[i].getScaledInstance(width, height, Image.SCALE_DEFAULT));
			}
			BISHOP_ICON = new Icon[2];
			for (int i = 0; i < BISHOP_ICON.length; i++) {
				BISHOP_ICON[i] = new ImageIcon(BISHOP_SPRITE[i].getScaledInstance(width, height, Image.SCALE_DEFAULT));
			}
			ROOK_ICON = new Icon[2];
			for (int i = 0; i < ROOK_ICON.length; i++) {
				ROOK_ICON[i] = new ImageIcon(ROOK_SPRITE[i].getScaledInstance(width, height, Image.SCALE_DEFAULT));
			}
			QUEEN_ICON = new Icon[2];
			for (int i = 0; i < QUEEN_ICON.length; i++) {
				QUEEN_ICON[i] = new ImageIcon(QUEEN_SPRITE[i].getScaledInstance(width, height, Image.SCALE_DEFAULT));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void initSprites() {
		try {
			pieceSpriteSheet = ImageIO.read(GameData.class.getResource("/img/chessSpriteSheet.png"));

			PAWN_SPRITE = new BufferedImage[2];
			PAWN_SPRITE[0] = pieceSpriteSheet.getSubimage(1113, 39, 1228 - 1113, 190 - 39);
			PAWN_SPRITE[1] = pieceSpriteSheet.getSubimage(1113, 253, 1228 - 1113, 403 - 253);

			KNIGHT_SPRITE = new BufferedImage[2];
			KNIGHT_SPRITE[0] = pieceSpriteSheet.getSubimage(665, 30, 823 - 665, 188 - 30);
			KNIGHT_SPRITE[1] = pieceSpriteSheet.getSubimage(665, 243, 823 - 665, 401 - 243);

			BISHOP_SPRITE = new BufferedImage[2];
			BISHOP_SPRITE[0] = pieceSpriteSheet.getSubimage(452, 23, 614 - 452, 187 - 23);
			BISHOP_SPRITE[1] = pieceSpriteSheet.getSubimage(452, 236, 614 - 452, 400 - 236);

			ROOK_SPRITE = new BufferedImage[2];
			ROOK_SPRITE[0] = pieceSpriteSheet.getSubimage(892, 39, 1027 - 892, 188 - 39);
			ROOK_SPRITE[1] = pieceSpriteSheet.getSubimage(892, 253, 1027 - 892, 401 - 253);

			QUEEN_SPRITE = new BufferedImage[2];
			QUEEN_SPRITE[0] = pieceSpriteSheet.getSubimage(229, 23, 410 - 229, 189 - 23);
			QUEEN_SPRITE[1] = pieceSpriteSheet.getSubimage(229, 238, 410 - 229, 408 - 238);

			KING_SPRITE = new BufferedImage[2];
			KING_SPRITE[0] = pieceSpriteSheet.getSubimage(24, 25, 188 - 24, 191 - 25);
			KING_SPRITE[1] = pieceSpriteSheet.getSubimage(24, 238, 188 - 24, 404 - 238);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void resetSoundStreams() {
		try {
			PIECE_SOUND_EFFECT = AudioSystem
					.getAudioInputStream(GameData.class.getResource("/sound/pieceSoundEffect.wav"));
			SOUND_PLAYER = AudioSystem.getClip();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static BufferedImage getImageFromFile(String path) {
		try {
			return ImageIO.read(GameData.class.getResource(path));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	static {
		initSprites();
		initButtonIcons();
		resetSoundStreams();
	}

}
