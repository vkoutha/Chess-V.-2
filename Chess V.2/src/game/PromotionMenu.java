package game;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import game.GameData.Players;
import pieces.Bishop;
import pieces.Knight;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;

public class PromotionMenu extends JFrame {

	private Pawn pawn;
	private JPanel container;
	private JButton knightButton, bishopButton, rookButton, queenButton;
	private int iconIndexToUse;

	public PromotionMenu(Pawn pawn) {
		super("Promotion Menu");
		this.pawn = pawn;
		iconIndexToUse = (pawn.getPlayer() == Players.PLAYER_1 ? 0 : 1);
		initFrame();
	}

	private void initFrame() {
		container = new JPanel();
		container.setPreferredSize(new Dimension(GameData.PROMOTION_MENU_WIDTH, GameData.PROMOTION_MENU_HEIGHT));
		container.setLayout(new BoxLayout(container, BoxLayout.LINE_AXIS));
		initButtons();
		initButtonListeners();
		container.add(Box.createHorizontalStrut(20));
		container.add(knightButton);
		container.add(Box.createHorizontalStrut(20));
		container.add(bishopButton);
		container.add(Box.createHorizontalStrut(20));
		container.add(rookButton);
		container.add(Box.createHorizontalStrut(20));
		container.add(queenButton);
		add(container);
		pack();
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
	
	private void initButtons() {
		knightButton = new JButton(GameData.knightIcon[iconIndexToUse]);
		knightButton.setAlignmentY(Component.CENTER_ALIGNMENT);
		bishopButton = new JButton(GameData.bishopIcon[iconIndexToUse]);
		bishopButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		rookButton = new JButton(GameData.rookIcon[iconIndexToUse]);
		rookButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		queenButton = new JButton(GameData.queenIcon[iconIndexToUse]);
		queenButton.setAlignmentX(Component.CENTER_ALIGNMENT);
	}

	private void initButtonListeners() {
		ArrayList<Piece> playerPieces = (pawn.getPlayer() == Players.PLAYER_1 ? Game.game.getPlayer1Pieces() : Game.game.getPlayer2Pieces());
		knightButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				playerPieces.add(new Knight(pawn.getRow(), pawn.getColumn(), pawn.getPlayer()));
				pawn.kill();
				dispose();
				
			}
		});
		bishopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				playerPieces.add(new Bishop(pawn.getRow(), pawn.getColumn(), pawn.getPlayer()));
				pawn.kill();
				dispose();
			}
		});
		rookButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				playerPieces.add(new Rook(pawn.getRow(), pawn.getColumn(), pawn.getPlayer()));
				pawn.kill();
				dispose();
			}
		});
		queenButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				playerPieces.add(new Queen(pawn.getRow(), pawn.getColumn(), pawn.getPlayer()));
				pawn.kill();
				dispose();
			}
		});
	}

}
