import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class SpaceInvader extends JPanel implements Runnable, KeyListener {
	private static final long serialVersionUID = 463975730322821834L;
	int width = 550;
	int height = 550;
	int playX = 0;
	int playY = (int) (height * 0.9);
	int ufoX = 0;
	int ufoY = (int) (height * 0.2);
	int ufoSpeed = 1;
	int playSpeed = 2;
	int speed = 0;
	boolean movingLeft = false, movingRight = false, newFire = false;

	public SpaceInvader() {
		JFrame jf = new JFrame();
		jf.setSize(width, height);
		jf.setLocationRelativeTo(null);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.add(this);
		jf.setVisible(true);
		jf.addKeyListener(this);
		new Thread(this).start();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 5; j++) {
				g.fillOval(ufoX + i * 30, ufoY + j * 30, 20, 20);
			}
		}
		g.fillOval(playX + 40, playY, 30, 30);
		g.dispose();
	}

	public static void main(String[] args) {
		new SpaceInvader();
	}

	@Override
	public void run() {
		while (true) {
			ufoStart();
			// 要修改的地方
			moveLeft(movingLeft);
			moveRight(movingRight);
			if (newFire) {
				// 子弹
			}
			repaint();

			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
		}
	}

	public void ufoStart() {
		ufoX += ufoSpeed;
		if (ufoX == width) {
			ufoX = 0;
		}
	}

	public void moveLeft(boolean movingLeft) {
		if (movingLeft) {
			speed = playSpeed;
		} else {
			speed = 0;
		}
		playX -= speed;
		if (playX < 0) {
			playX = 0;
		}
	}

	public void moveRight(boolean movingRight) {
		if (movingRight) {
			speed = playSpeed;
		} else {
			speed = 0;
		}
		playX += speed;
		if (playX > width) {
			playX = width;
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_LEFT) {
			movingLeft = true;
		}
		if (key == KeyEvent.VK_RIGHT) {
			movingRight = true;
		}
		if (key == KeyEvent.VK_CONTROL) {
			newFire = true;
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_LEFT) {
			movingLeft = false;
		}
		if (key == KeyEvent.VK_RIGHT) {
			movingRight = false;
		}
		if (key == KeyEvent.VK_CONTROL) {
			newFire = false;
		}

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}
}

/*
 * if left x --; else if right x ++; else {} if newfire == true{
 * firelist.add({x, y}) } foreach(firelist){ if collsion { disppear; }
 */
/*
 * {direction = left, right; not-moving} {unprocessed fire count true false}
 */
