package game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import game.GameData.GameStates;
import game.Renderer.BoardPanel;
import game.Renderer.Player1Panel;
import game.Renderer.Player2Panel;
import network.Client;
import network.OnlineGame;
import network.Server;

public class FrameManager extends Game {

	private FrameManager() {

	}

	public static class MainMenuFrame {
		public static void initMenuFrame() {
			JPanel container = new JPanel();
			container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
			JLabel logoLbl = getLogoLabel();
			JButton twoPlayerBtn = getTwoPlayerButton();
			JButton singlePlayerBtn = getSinglePlayerButton();
			JButton startLocalGameBtn = getStartLocalGameButton();
			JButton joinLocalGameBtn = getJoinLocalGameButton();
			container.setBackground(GameData.PLAYER_PANEL_BACKGROUND_COLOR);
			container.setPreferredSize(new Dimension(GameData.BOARD_WIDTH, GameData.BOARD_HEIGHT));
			container.add(Box.createVerticalStrut(80));
			container.add(logoLbl);
			container.add(Box.createVerticalStrut(50));
			container.add(twoPlayerBtn);
			container.add(Box.createVerticalStrut(25));
			container.add(singlePlayerBtn);
			container.add(Box.createVerticalStrut(25));
			container.add(startLocalGameBtn);
			container.add(Box.createVerticalStrut(25));
			container.add(joinLocalGameBtn);
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

		private static JButton getTwoPlayerButton() {
			JButton twoPlayerBtn = new JButton(GameData.twoPlayerIcon[0]);
			twoPlayerBtn.setRolloverIcon(GameData.twoPlayerIcon[1]);
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

		private static JButton getSinglePlayerButton() {
			JButton singlePlayerBtn = new JButton(GameData.singlePlayerIcon[0]);
			singlePlayerBtn.setRolloverIcon(GameData.singlePlayerIcon[1]);
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

		private static JButton getStartLocalGameButton() {
			JButton startLocalGameBtn = new JButton(GameData.startLocalGameIcon[0]);
			startLocalGameBtn.setRolloverIcon(GameData.startLocalGameIcon[1]);
			startLocalGameBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					Game.game.setGameState(GameStates.SEARCHING);
					Server.startOnlineGame();
					Game.game.onlineGame = new OnlineGame(true);
					Game.game.setGameState(GameStates.IN_GAME);
				}
			});
			startLocalGameBtn.setFocusable(false);
			startLocalGameBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
			GameData.removeBackground(startLocalGameBtn);
			return startLocalGameBtn;
		}

		static private JButton getJoinLocalGameButton() {
			JButton joinLocalGameBtn = new JButton(GameData.joinLocalGameIcon[0]);
			joinLocalGameBtn.setRolloverIcon(GameData.joinLocalGameIcon[1]);
			joinLocalGameBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					Game.game.setGameState(GameStates.SEARCHING);
					Client.joinOnlineGame();
					Game.game.onlineGame = new OnlineGame(false);
				}
			});
			joinLocalGameBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
			joinLocalGameBtn.setFocusable(false);
			GameData.removeBackground(joinLocalGameBtn);
			return joinLocalGameBtn;
		}
	}

	public static class GameFrame {
		public static void initGameFrame() {
			Game.game.frame.setLayout(new BorderLayout());
			Game.game.player1Panel = new Player1Panel();
			Game.game.player1Panel
					.setPreferredSize(new Dimension(GameData.PLAYER_PANEL_WIDTH, GameData.PLAYER_PANEL_HEIGHT));
			Game.game.player1Panel.setBackground(GameData.PLAYER_PANEL_BACKGROUND_COLOR);
			Game.game.player2Panel = new Player2Panel();
			Game.game.player2Panel
					.setPreferredSize(new Dimension(GameData.PLAYER_PANEL_WIDTH, GameData.PLAYER_PANEL_HEIGHT));
			Game.game.player2Panel.setBackground(GameData.PLAYER_PANEL_BACKGROUND_COLOR);
			Game.game.boardPanel = new BoardPanel();
			Game.game.boardPanel.setPreferredSize(new Dimension(GameData.BOARD_WIDTH, GameData.BOARD_HEIGHT));
			Game.game.renderer = () -> {
				Game.game.player1Panel.repaint();
				Game.game.player2Panel.repaint();
				Game.game.boardPanel.repaint();
			};
			Game.game.frame.getContentPane().addMouseListener(Game.game);
			Game.game.frame.add(Game.game.player1Panel, BorderLayout.LINE_START);
			Game.game.frame.add(Game.game.boardPanel, BorderLayout.CENTER);
			Game.game.frame.add(Game.game.player2Panel, BorderLayout.LINE_END);
			Game.game.frame.setVisible(true);
			Game.game.frame.pack();
			Game.game.frame.setLocationRelativeTo(null);
			Game.game.player1Panel.setLayout(null);
			Game.game.player2Panel.setLayout(null);
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
	}

}
