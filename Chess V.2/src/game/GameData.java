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

	public static final int CONNECTION_TIMEOUT_MS = 5000;
	public static final int NETWORK_PORT = 1118;

	public static final Color BROWN_TILE_COLOR = new Color(153, 76, 0);
	public static final Color WHITE_TILE_COLOR = new Color(255, 178, 102);
	public static final Color SELECTED_TILE_COLOR = new Color(255, 220, 46);
	public static final Color VALID_MOVE_TILE_COLOR = new Color(7, 140, 0);
	public static final Color IN_CHECK_TILE_COLOR = new Color(135, 0, 0);
	public static final Color PLAYER_PANEL_BACKGROUND_COLOR = new Color(240, 205, 120);

	public static BufferedImage pieceSpriteSheet;
	// index 0 corresponds to Player 1 sprite, index 1 corresponds to Player 2
	// sprite
	public static BufferedImage[] pawnSprite, knightSprite, bishopSprite, rookSprite, queenSprite, kingSprite;

	public static Icon[] singlePlayerIcon, twoPlayerIcon, startLocalGameIcon, joinLocalGameIcon;
	public static Icon[] knightIcon, bishopIcon, rookIcon, queenIcon;

	public static AudioInputStream pieceSoundEffect;
	public static Clip soundPlayer;

	public enum GameStates {
		MENU, SEARCHING, IN_GAME
	}

	public enum Players {
		PLAYER_1, PLAYER_2
	}
	
	public enum DataTransferHeaders{
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
			BufferedImage[] twoPlayerImg = { getImageFromFile("/img/buttons/unselected/multiplayerUnselected.png"),
					getImageFromFile("/img/buttons/selected/multiplayerSelected.png") };
			BufferedImage[] singlePlayerImg = { getImageFromFile("/img/buttons/unselected/singlePlayerUnselected.png"),
					getImageFromFile("/img/buttons/selected/singlePlayerSelected.png") };
			BufferedImage[] startLocalGameImg = { getImageFromFile("/img/buttons/unselected/startGameUnselected.png"),
					getImageFromFile("/img/buttons/selected/startGameSelected.png") };
			BufferedImage[] joinLocalGameImg = { getImageFromFile("/img/buttons/unselected/joinGameUnselected.png"),
					getImageFromFile("/img/buttons/selected/joinGameSelected.png") };
			twoPlayerIcon = new Icon[2];
			singlePlayerIcon = new Icon[2];
			startLocalGameIcon = new Icon[2];
			joinLocalGameIcon = new Icon[2];
			for (int i = 0; i < 2; i++) {
				twoPlayerIcon[i] = new ImageIcon(
						twoPlayerImg[i].getScaledInstance(GameData.BOARD_WIDTH / 2, 80, Image.SCALE_DEFAULT));
				singlePlayerIcon[i] = new ImageIcon(
						singlePlayerImg[i].getScaledInstance(GameData.BOARD_WIDTH / 2, 80, Image.SCALE_DEFAULT));
				startLocalGameIcon[i] = new ImageIcon(
						startLocalGameImg[i].getScaledInstance(GameData.BOARD_WIDTH / 2, 80, Image.SCALE_DEFAULT));
				joinLocalGameIcon[i] = new ImageIcon(
						joinLocalGameImg[i].getScaledInstance(GameData.BOARD_WIDTH / 2, 80, Image.SCALE_DEFAULT));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void initPromotionMenuButtonIcons() {
		int width = 75;
		int height = 150;
		try {
			knightIcon = new Icon[2];
			for (int i = 0; i < knightIcon.length; i++) {
				knightIcon[i] = new ImageIcon(knightSprite[i].getScaledInstance(width, height, Image.SCALE_DEFAULT));
			}
			bishopIcon = new Icon[2];
			for (int i = 0; i < bishopIcon.length; i++) {
				bishopIcon[i] = new ImageIcon(bishopSprite[i].getScaledInstance(width, height, Image.SCALE_DEFAULT));
			}
			rookIcon = new Icon[2];
			for (int i = 0; i < rookIcon.length; i++) {
				rookIcon[i] = new ImageIcon(rookSprite[i].getScaledInstance(width, height, Image.SCALE_DEFAULT));
			}
			queenIcon = new Icon[2];
			for (int i = 0; i < queenIcon.length; i++) {
				queenIcon[i] = new ImageIcon(queenSprite[i].getScaledInstance(width, height, Image.SCALE_DEFAULT));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void initSprites() {
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

	public static void resetSoundStreams() {
		try {
			pieceSoundEffect = AudioSystem
					.getAudioInputStream(GameData.class.getResource("/sound/pieceSoundEffect.wav"));
			soundPlayer = AudioSystem.getClip();
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
