import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class SpaceInvader extends JPanel implements Runnable, KeyListener {
	private static final long serialVersionUID = 463975730322821834L;
	// Jpanel 大小
	int width = 550;
	int height = 550;
	// player圆心位置
	public static int playRadius = 15;
	int playX = playRadius;
	int playY = (int) (height * 0.9);
	// UFO圆心位置
	public static int ufoRadius = 10;
	int ufoX = ufoRadius;
	int ufoY = (int) (height * 0.2);
	// UFO移动速度
	int ufoSpeed = 1;
	// player移动速度
	int playSpeed = 2;
	// 子弹速度
	int bulletSpeed = 8;
	int speed = 0;
	//子弹圆心位置
	public static int bulletRadius = 5;
	public int bulletY = 0;//playY;
	public int bulletX = 0;//playX + playDiam / 2;

	// 键盘输入判断
	boolean movingLeft = false, movingRight = false, hasFire = false;
	boolean fireLive = true;// 判断是否出界，或者消失

	//List<Bullet> fireList = new ArrayList<Bullet>();
	//Bullet bi;

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
				g.fillOval(ufoX - ufoRadius + i * 30, ufoY - ufoRadius + j * 30, ufoRadius*2, ufoRadius*2);
			}
		}
		g.fillOval(playX - playRadius, playY- playRadius, playRadius*2, playRadius*2);
		if(hasFire) {
			g.fillOval(bulletX - bulletRadius, bulletY - bulletRadius, bulletRadius *2, bulletRadius *2);
		}
		/*for (int i = 0; i < fireList.size(); i++) {
			Bullet b = fireList.get(i);
			b.drawBullet(g);
		}
		g.drawString("Bullets Count:" + fireList.size(), 10, 50);*/
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
			move();
			// 子弹
			//fire(newFire).bulletMove();
			if(hasFire) {
				bulletY = bulletY - 3;
				if(bulletY < 0) {
					hasFire = false;
				}
			}
			
			repaint();

			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
		}
	}

	// ufo运动控制
	public void ufoStart() {
		ufoX += ufoSpeed;
		if (ufoX == width) {
			ufoX = 0;
		}
	}

	// play运动控制
	public void move() {
		speed = playSpeed;
		if (movingLeft && !movingRight) {
			playX -= speed;
		} else if (!movingLeft && movingRight) {
			playX += speed;
		} else {
			speed = 0;
		}
		if (playX < 0) {
			playX = 0;
		}
		if (playX > width - playRadius) {
			playX = width - playRadius;
		}
	}
	// new子弹
	/*public Bullet fire(boolean newFire) {
		bulletX = playX + playDiam / 2;
		bulletY = playY;
		Bullet nf = new Bullet(bulletX, bulletY, this);
		fireList.add(nf);
		return nf;

	}*/

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
			if(!hasFire) {
				hasFire = true;
				bulletX = playX;
				bulletY = playY;
			}
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
