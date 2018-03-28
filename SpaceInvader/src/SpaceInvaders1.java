import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class SpaceInvaders1 extends JPanel implements Runnable, KeyListener {
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
	// 子弹圆心位置
	public static int bulletRadius = 2;
	// 键盘输入判断
	boolean movingLeft = false, movingRight = false;
	boolean newFire = false; // 判断是否装新子弹

	List<Point> fireList = new ArrayList<Point>();// 子弹夹
	List<Point> ufoList = new ArrayList<Point>();// 入侵者集

	public SpaceInvaders1() {
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
		for (int i = 0; i < ufoList.size(); i++) {
			Point p = ufoList.get(i);
			g.fillOval(p.x - ufoRadius, p.y - ufoRadius, ufoRadius * 2, ufoRadius * 2);
		}
		g.fillOval(playX - playRadius, playY - playRadius, playRadius * 2, playRadius * 2);
		for (int i = 0; i < fireList.size(); i++) {
			Point p = fireList.get(i);
			g.fillOval(p.x - bulletRadius, p.y - bulletRadius, bulletRadius * 2, bulletRadius * 2);
		}
		g.dispose();
	}

	public static void main(String[] args) {
		new SpaceInvaders1();
	}

	// 当前状态
	@Override
	public void run() {
		ufoShow();
		while (true) {
			ufoStart();
			fireShow();
			move();
			Iterator<Point> itr = fireList.iterator();
			while (itr.hasNext()) {
				Point fire = itr.next();
				fire.y -= bulletSpeed;
				if (fire.y < 0) {
					itr.remove();
				}
				Iterator<Point> itrUFO = ufoList.iterator();
				while (itrUFO.hasNext()) {
					Point ufo = itrUFO.next();
					if (ufo.y >= fire.y - ufoRadius - bulletRadius && ufo.y <= fire.y + ufoRadius + bulletRadius
							&& ufo.x <= fire.x + ufoRadius + bulletRadius
							&& ufo.x >= fire.x - ufoRadius - bulletRadius) {
						itr.remove();
						itrUFO.remove();
					}
				}
			}
			repaint();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
		}
	}

	public void ufoShow() {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 5; j++) {
				int ufoPosX = ufoX - ufoRadius + i * 30;
				int ufoPosY = ufoY - ufoRadius + j * 30;
				Point ufoPos = new Point(ufoPosX, ufoPosY);
				ufoList.add(ufoPos);
			}
		}
	}

	// ufo运动控制
	public void ufoStart() {
		for (int i = 0; i < ufoList.size(); i++) {
			Point ufoTemp = ufoList.get(i);
			ufoTemp.x += ufoSpeed;
			if (ufoTemp.x >= width) {
				ufoTemp.x = 0;
			}
		}
	}
	
	public void fireShow() {
		if(newFire) {
			Point fire = new Point();
			fire.x = playX;
			fire.y = playY;
			fireList.add(fire);
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
		if (playX < playRadius) {
			playX = playRadius;
		}
		if (playX > width - playRadius) {
			playX = width - playRadius;
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
	}
}

/*
 * {direction = left, right; not-moving} {unprocessed fire count true false}
 */
