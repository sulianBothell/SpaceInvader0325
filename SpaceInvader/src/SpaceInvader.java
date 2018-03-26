import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class SpaceInvader extends JPanel implements Runnable {
	private static final long serialVersionUID = 463975730322821834L;
	int x = 0;

	public SpaceInvader() {
		JFrame jf = new JFrame();
		jf.setSize(555, 555);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.add(this);
		jf.setVisible(true);
		new Thread(this).start();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 5; j++) {
				g.fillOval(x + i * 30, 100 + j * 30, 20, 20);
			}
		}
		g.fillOval(x + 40, 500, 30, 30);
		g.dispose();
	}

	public static void main(String[] args) {
		new SpaceInvader();
	}

	@Override
	public void run() {
		while (true) {
			x++;
			repaint();

			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
		}
	}
}

/*
 * if left x --; else if right x ++; else {} if newfire == true{
 * firelist.add({x, y}) } foreach(firelist){ if collsion { disppear; }
 */
/*
 * {direction = left, right; not-moving} {unprocessed fire count true false}
 */
