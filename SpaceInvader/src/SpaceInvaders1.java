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
	// ship圆心位置
	public static int shipRadius = 15;
	int shipX = shipRadius;
	int shipY = (int) (height * 0.9);
	// ship移动速度
	int shipSpeed = 4;
	
	// UFO圆心位置
	public static int ufoRadius = 10;
	int ufoX = ufoRadius;
	int ufoY = (int) (height * 0.2);
	// UFO移动速度
	int distanceX = 1;
	int distanceY = 10;
	
	//enemyShip
	int enemyShipX = shipRadius;
	int enemyShipY = (int) (height * 0.1);
	int enemyShipSpeed = 2;
	public static final double FIRING_FREQUENCY = 0.01;
	
	// 子弹速度
	int bulletSpeed = 8;
	// 子弹半径
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
		Color c = Color.BLACK;
		g.setColor(c);
		g.fillOval(shipX - shipRadius, shipY - shipRadius, shipRadius * 2, shipRadius * 2);
		for (int i = 0; i < fireList.size(); i++) {
			Point p = fireList.get(i);
			g.fillOval(p.x - bulletRadius, p.y - bulletRadius, bulletRadius * 2, bulletRadius * 2);
		}
		g.setColor(Color.YELLOW);
		g.fillOval(enemyShipX - shipRadius, enemyShipY - shipRadius, shipRadius * 2, shipRadius * 2);
		for (int i = 0; i < ufoList.size(); i++) {
			Point p = ufoList.get(i);
			g.fillOval(p.x - ufoRadius, p.y - ufoRadius, ufoRadius * 2, ufoRadius * 2);
		}
		g.setColor(c);
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
			enemyShip();
			ufoMove();
			fireShow();
			shipMove();
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
				Thread.sleep(20);
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
	public void ufoMove() {
		for (int i = 0; i < ufoList.size(); i++) {
			Point ufoTemp = ufoList.get(i);
			ufoTemp.x += distanceX;
			if (ufoTemp.x >= width) {
				ufoTemp.x = 0;
				ufoTemp.y += distanceY;
			}
		}
	}
	
	public void enemyShip() {
		enemyShipX += enemyShipSpeed;
		if(enemyShipX >= width) {
			enemyShipX = 0;
		}
		//敌人飞船射出子弹
		if(Math.random() < FIRING_FREQUENCY) {
			fireStart(enemyShipX, enemyShipY);
		}
	}
	
	public void fireStart(int posX, int posY) {
		Point fire = new Point();
		fire.x = posX;
		fire.y = posY;
		fireList.add(fire);
	}
	
	//玩家飞船射出子弹
	public void fireShow() {
		if(newFire) {
			fireStart(shipX, shipY);
		}
	}

	// ship运动控制
	public void shipMove() {
		int speed = shipSpeed;
		if (movingLeft && !movingRight) {
			shipX -= speed;
		} else if (!movingLeft && movingRight) {
			shipX += speed;
		} else {
			speed = 0;
		}
		if (shipX < shipRadius) {
			shipX = shipRadius;
		}
		if (shipX > width - shipRadius) {
			shipX = width - shipRadius;
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
