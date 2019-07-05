package game;
import java.awt.Graphics;

import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.plaf.ColorChooserUI;

public interface Renderer{
	
	void repaint();
	
	@SuppressWarnings("serial")
	public class BoardPanel extends JPanel implements Renderer{
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Game.game.render(g, Panels.BOARD);
		}
	}
	
	@SuppressWarnings("serial")
	public class Player1Panel extends JPanel implements Renderer{
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Game.game.render(g, Panels.PLAYER_1);
		}
	}
	
	@SuppressWarnings("serial")
	public class Player2Panel extends JPanel implements Renderer{
		@Override
		protected void paintComponent(Graphics g){
			super.paintComponent(g);
			Game.game.render(g, Panels.PLAYER_2);
		}
	}
	
	public enum Panels{
		BOARD,
		PLAYER_1,
		PLAYER_2,
	}

}
