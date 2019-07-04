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
	public class WhitePanel extends JPanel implements Renderer{
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Game.game.render(g, Panels.WHITE);
		}
	}
	
	@SuppressWarnings("serial")
	public class BlackPanel extends JPanel implements Renderer{
		@Override
		protected void paintComponent(Graphics g){
			super.paintComponent(g);
			Game.game.render(g, Panels.BLACK);
		}
	}
	
	public enum Panels{
		BOARD,
		WHITE,
		BLACK,
	}

}
