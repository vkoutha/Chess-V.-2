package game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import game.GameData.GameStates;
import game.Renderer.BoardPanel;
import game.Renderer.Player1Panel;
import game.Renderer.Player2Panel;
import network.Client;
import network.Server;

public class FrameManager {

	private FrameManager() {

	}

	public static class MainMenuFrame {
		public static void initMenuFrame() {
			JPanel container = new JPanel();
			container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
			JLabel logoLbl = getLogoLabel();
			JButton singlePlayerBtn = getSinglePlayerButton();
			JButton twoPlayerBtn = getTwoPlayerButton();
			JButton startLocalGameBtn = getStartLocalGameButton();
			JButton joinLocalGameBtn = getJoinLocalGameButton();
			JButton loadGameBtn = getLoadGameButton();
			container.setBackground(GameData.PLAYER_PANEL_BACKGROUND_COLOR);
			container.setPreferredSize(new Dimension(GameData.BOARD_WIDTH, GameData.BOARD_HEIGHT));
			container.add(Box.createVerticalStrut(80));
			container.add(logoLbl);
			container.add(Box.createVerticalStrut(50));
			container.add(singlePlayerBtn);
			container.add(Box.createVerticalStrut(25));
			container.add(twoPlayerBtn);
			container.add(Box.createVerticalStrut(25));
			container.add(startLocalGameBtn);
			container.add(Box.createVerticalStrut(25));
			container.add(joinLocalGameBtn);
			container.add(Box.createVerticalStrut(25));
			container.add(loadGameBtn);
			Game.game.frame.add(container);
			Game.game.frame.pack();
			Game.game.frame.setVisible(true);
			Game.game.frame.setLocationRelativeTo(null);
		}

		private static JLabel getLogoLabel() {
			JLabel logoLbl = new JLabel("Chess V.2");
			logoLbl.setForeground(Color.BLACK);
			logoLbl.setFont(new Font("Arial", Font.BOLD, 50));
			logoLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
			return logoLbl;
		}
		
		private static JButton getSinglePlayerButton() {
			JButton singlePlayerBtn = new JButton(GameData.SINGLE_PLAYER_ICON[0]);
			singlePlayerBtn.setRolloverIcon(GameData.SINGLE_PLAYER_ICON[1]);
			singlePlayerBtn.setFocusable(false);
			singlePlayerBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					Game.game.setGameState(GameStates.IN_GAME);
				}
			});
			singlePlayerBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
			GameData.removeBackground(singlePlayerBtn);
			return singlePlayerBtn;
		}

		private static JButton getTwoPlayerButton() {
			JButton twoPlayerBtn = new JButton(GameData.TWO_PLAYER_ICON[0]);
			twoPlayerBtn.setRolloverIcon(GameData.TWO_PLAYER_ICON[1]);
			twoPlayerBtn.setFocusable(false);
			twoPlayerBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					Game.game.setGameState(GameStates.IN_GAME);
				}
			});
			twoPlayerBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
			GameData.removeBackground(twoPlayerBtn);
			return twoPlayerBtn;
		}

		private static JButton getStartLocalGameButton() {
			JButton startLocalGameBtn = new JButton(GameData.START_LOCAL_GAME_ICON[0]);
			startLocalGameBtn.setRolloverIcon(GameData.START_LOCAL_GAME_ICON[1]);
			startLocalGameBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					Game.game.setGameState(GameStates.SEARCHING);
					Server.startOnlineGame();
					Game.game.setGameState(GameStates.IN_GAME);
					Game.game.startTimer();
				}
			});
			startLocalGameBtn.setFocusable(false);
			startLocalGameBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
			GameData.removeBackground(startLocalGameBtn);
			return startLocalGameBtn;
		}

		private static JButton getJoinLocalGameButton() {
			JButton joinLocalGameBtn = new JButton(GameData.JOIN_LOCAL_GAME_ICON[0]);
			joinLocalGameBtn.setRolloverIcon(GameData.JOIN_LOCAL_GAME_ICON[1]);
			joinLocalGameBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					Game.game.setGameState(GameStates.SEARCHING);
					Client.joinOnlineGame();
					Game.game.setGameState(GameStates.IN_GAME);
					Game.game.startTimer();
				}
			});
			joinLocalGameBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
			joinLocalGameBtn.setFocusable(false);
			GameData.removeBackground(joinLocalGameBtn);
			return joinLocalGameBtn;
		}
	}
	
	private static JButton getLoadGameButton() {
		JButton loadGameBtn = new JButton(GameData.LOAD_GAME_ICON[0]);
		loadGameBtn.setRolloverIcon(GameData.LOAD_GAME_ICON[1]);
		loadGameBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		loadGameBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		loadGameBtn.setFocusable(false);
		GameData.removeBackground(loadGameBtn);
		return loadGameBtn;
	}

	public static class GameFrame {
		public static void initGameFrame() {
			Game.game.frame.setLayout(new BorderLayout());
			initGamePanels();
			initPlayerTimers();
			Game.game.frame.setVisible(true);
			Game.game.frame.pack();
			Game.game.frame.setLocationRelativeTo(null);
			Game.game.frame.getContentPane().addMouseListener(Game.game);
		}
		
		private static void initGamePanels() {
			Game.game.player1Panel = new Player1Panel();
			Game.game.player1Panel
					.setPreferredSize(new Dimension(GameData.PLAYER_PANEL_WIDTH, GameData.PLAYER_PANEL_HEIGHT));
			Game.game.player1Panel.setLayout(null);
			Game.game.player1Panel.setBackground(GameData.PLAYER_PANEL_BACKGROUND_COLOR);
			Game.game.player2Panel = new Player2Panel();
			Game.game.player2Panel
					.setPreferredSize(new Dimension(GameData.PLAYER_PANEL_WIDTH, GameData.PLAYER_PANEL_HEIGHT));
			Game.game.player2Panel.setLayout(null);
			Game.game.player2Panel.setBackground(GameData.PLAYER_PANEL_BACKGROUND_COLOR);
			Game.game.boardPanel = new BoardPanel();
			Game.game.boardPanel.setPreferredSize(new Dimension(GameData.BOARD_WIDTH, GameData.BOARD_HEIGHT));
			Game.game.renderer = () -> {
				Game.game.player1Panel.repaint();
				Game.game.player2Panel.repaint();
				Game.game.boardPanel.repaint();
			};
			Game.game.frame.add(Game.game.player1Panel, BorderLayout.LINE_START);
			Game.game.frame.add(Game.game.boardPanel, BorderLayout.CENTER);
			Game.game.frame.add(Game.game.player2Panel, BorderLayout.LINE_END);
		}
		
		private static void initPlayerTimers() {
			Game.game.player1TimerLabel = new JLabel("Time remaining: ##:###");
			Game.game.player1TimerLabel.setFont(new Font("Arial", Font.BOLD, 14));
			Game.game.player1TimerLabel.setBounds(
					(GameData.PLAYER_PANEL_WIDTH / 2)
							- ((int) Game.game.player1TimerLabel.getPreferredSize().getWidth() / 2),
					50, (int) Game.game.player1TimerLabel.getPreferredSize().getWidth(),
					(int) Game.game.player1TimerLabel.getPreferredSize().getHeight());
			Game.game.player1Panel.add(Game.game.player1TimerLabel);
			Game.game.player2TimerLabel = new JLabel("Time remaining: ##:###");
			Game.game.player2TimerLabel.setFont(new Font("Arial", Font.BOLD, 14));
			Game.game.player2TimerLabel.setBounds(
					(GameData.PLAYER_PANEL_WIDTH / 2)
							- ((int) Game.game.player2TimerLabel.getPreferredSize().getWidth() / 2),
					50, (int) Game.game.player2TimerLabel.getPreferredSize().getWidth(),
					(int) Game.game.player2TimerLabel.getPreferredSize().getHeight());
			Game.game.player2Panel.add(Game.game.player2TimerLabel);
		}

		public static void renderBoard(Graphics g) {
			for (int row = 0; row < Game.game.tiles.length; row++) {
				for (int col = 0; col < Game.game.tiles[row].length; col++) {
					Game.game.tiles[row][col].render(g);
				}
			}
			Game.game.player1Pieces.forEach((piece) -> piece.render(g));
			Game.game.player2Pieces.forEach((piece) -> piece.render(g));
		}

		public static void renderPlayer1Panel(Graphics g) {
			int p1Minutes = GameData.PLAYER_1_TIMER_SECONDS / 60;
			int p1Seconds = GameData.PLAYER_1_TIMER_SECONDS % 60;
			g.setColor(GameData.PLAYER_PANEL_BACKGROUND_COLOR);
			g.fillRect(0, 0, GameData.PLAYER_PANEL_WIDTH, GameData.PLAYER_PANEL_HEIGHT);
			Game.game.deadPlayer1Pieces.forEach((piece) -> piece.render(g));
			Game.game.player1TimerLabel
					.setText("Time remaining: " + p1Minutes + ":" + (p1Seconds < 10 ? "0" + p1Seconds : p1Seconds));
		}

		public static void renderPlayer2Panel(Graphics g) {
			int p2Minutes = GameData.PLAYER_2_TIMER_SECONDS / 60;
			int p2Seconds = GameData.PLAYER_2_TIMER_SECONDS % 60;
			g.setColor(GameData.PLAYER_PANEL_BACKGROUND_COLOR);
			g.fillRect(0, 0, GameData.PLAYER_PANEL_WIDTH, GameData.PLAYER_PANEL_HEIGHT);
			Game.game.deadPlayer2Pieces.forEach((piece) -> piece.render(g));
			Game.game.player2TimerLabel
					.setText("Time remaining: " + p2Minutes + ":" + (p2Seconds < 10 ? "0" + p2Seconds : p2Seconds));
		}
	}

	public static class SearchingFrame {
		public static void initSearchingFrame() {
			JPanel container = new JPanel();
			container.setBackground(GameData.PLAYER_PANEL_BACKGROUND_COLOR);
			container.setPreferredSize(new Dimension(GameData.BOARD_WIDTH, GameData.BOARD_HEIGHT));
			container.setLayout(null);
			JLabel findingOpponentsLbl = new JLabel("Finding Opponent...");
			findingOpponentsLbl.setFont(new Font("Arial", Font.BOLD, 70));
			findingOpponentsLbl.setForeground(Color.BLACK);
			findingOpponentsLbl.setBounds(
					(int) ((container.getPreferredSize().getWidth() / 2)
							- (findingOpponentsLbl.getPreferredSize().getWidth() / 2)),
					(int) ((container.getPreferredSize().getHeight() / 2)
							- (findingOpponentsLbl.getPreferredSize().getHeight())),
					(int) findingOpponentsLbl.getPreferredSize().getWidth(),
					(int) findingOpponentsLbl.getPreferredSize().getHeight());
			container.add(findingOpponentsLbl);
			Game.game.frame.add(container);
			Game.game.frame.pack();
		}
	}

}
