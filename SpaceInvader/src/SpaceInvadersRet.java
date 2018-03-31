import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class SpaceInvadersRet extends JPanel implements Runnable, KeyListener {
	private static final long serialVersionUID = 463975730322821834L;
	// Jpanel 大小
	int width = 550;
	int height = 550;
	// ship圆心位置
	public static int shipRadius = 15;
	int shipX = shipRadius;
	int shipPosY = (int) (height * 0.9);
	int shipY = shipPosY;
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
	public static final double FIRING_FREQUENCY2 = 0.001;
	
	//Rampart
	int posX = 20;
	int posY = (int) (height * 0.8);
	
	// 子弹速度
	int bulletSpeed = 8;
	// 子弹半径
	public static int bulletRadius = 4;
	
	// 键盘输入判断
	boolean movingLeft = false, movingRight = false;
	boolean newFire = false; // 判断是否装新子弹
	boolean type = false; //是敌人飞船， true是飞船
	boolean hasCollision = false;
	boolean destroied = false;//玩家飞船
	boolean destroiedEnemy = false;//敌人飞船
	boolean destroiedUFO = false; //UFO飞船
	boolean destroiedBullet = false;
	
	//Map<Point, Boolean> firePool = new HashMap<Point, Boolean>();//子弹夹
	List<Point> fireList = new ArrayList<Point>();// 子弹夹
	Map<Point, Boolean> fireMap = new HashMap<Point, Boolean>();//子弹
	List<Point> ufoList = new ArrayList<Point>();// 入侵者集
	List<Point> ramList = new ArrayList<Point>();//堡垒个人集
	

	public SpaceInvadersRet() {
		JFrame jf = new JFrame();
		jf.setTitle("Space Invaders 1.0");
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
		if (!destroied) {//玩家飞船没坏
			g.fillOval(shipX - shipRadius, shipY - shipRadius, shipRadius * 2, shipRadius * 2);
		} 
		for (Point key : fireMap.keySet()) {
			g.fillOval(key.x - bulletRadius, key.y - bulletRadius, bulletRadius * 2, bulletRadius * 2);
		}
		if (!destroiedEnemy) {
			g.fillOval(enemyShipX - shipRadius, enemyShipY - shipRadius, shipRadius * 2, shipRadius * 2);
		}
		for (int i = 0; i < ufoList.size(); i++) {
			Point p = ufoList.get(i);
			g.fillOval(p.x - ufoRadius, p.y - ufoRadius, ufoRadius * 2, ufoRadius * 2);
		}
		for(int i = 0; i < ramList.size(); i++) {
			Point p = ramList.get(i);
			g.fillRect(p.x - 1, p.y - 1, 2, 2);
		}
		g.dispose();
	}

	public static void main(String[] args) {
		new SpaceInvadersRet();
	}

	// 当前状态
	@Override
	public void run() {
		ufoShow();
		getRampart();
		while (true) {
			enemyShip();
			ufoMove();
			fireShow();
			shipMove();
			Point enemyShip = new Point(enemyShipX, enemyShipY);//敌人飞船
			Point ship = new Point(shipX, shipY);//玩家飞船
			Iterator<Map.Entry<Point, Boolean>> iterator = fireMap.entrySet().iterator();//子弹map
			while (iterator.hasNext()) {
				Map.Entry<Point, Boolean> entry = iterator.next(); // System.out.println("key = " + entry.getKey() + "
																	// and value = " + entry.getValue());
				Iterator<Point> itrRam = ramList.iterator();
				if (entry.getValue()) {// 玩家飞船打出的子弹
					entry.getKey().y -= bulletSpeed;
					// 敌人飞船和玩家子弹相撞，子弹消失，敌人飞船也消失
					if (collision(enemyShip, entry.getKey(), shipRadius, bulletRadius)) {
						iterator.remove();// 子弹消失
						destroiedEnemy = true;// 敌人飞船也消失
					}
					Iterator<Point> itrUFO = ufoList.iterator();
					while (itrUFO.hasNext()) {
						Point ufo = itrUFO.next();
						if (collision(ufo, entry.getKey(), shipRadius, bulletRadius)) {
							destroiedBullet = true;
							// iterator.remove();
							// destroiedUFO = true;
							itrUFO.remove();
						}
					}
					
					// 玩家子弹过不去堡垒
					while (itrRam.hasNext()) {
						Point ramTemp = itrRam.next();
						if (collision(ramTemp, entry.getKey(), 1, bulletRadius)) {
							destroiedBullet = true;						}
					}

					if (destroiedBullet) {
						iterator.remove();
						destroiedBullet = false;

					}
					
				} else {
					entry.getKey().y += bulletSpeed;// 敌人飞船打出子弹
					if(collision(entry.getKey(), ship, shipRadius, bulletRadius)) {
						destroied = true;
						iterator.remove();
					}
					
					
					//敌人子弹和堡垒相撞， 子弹消失，堡垒部分消失
					while(itrRam.hasNext()) {
						Point ramTemp = itrRam.next();
						if(collision(ramTemp, entry.getKey(), 1, bulletRadius)) {
							itrRam.remove();
							//iterator.remove();
							hasCollision = true;
						}
					}
					if(hasCollision) {
						iterator.remove();
						hasCollision = false;
					}
					
					
				}
				//子弹出界
				if(entry.getKey().y < 0 || entry.getKey().y > height) {
					iterator.remove();
				}
			}
			repaint();
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
			}
		}
	}
	
	public void getRampart() {
		int y = 400;
		int start = 50;
		int init = 50;
		int space = 100;
		while(start < width - init) {
			int x = start;
			start = start + space;
			for (int i = 0; i < 25; i++) {
				for (int j = 0; j < 10; j++) {
					Point ram = new Point(x + i * 2, y + j * 2);
					ramList.add(ram);
					//System.out.println(ram.toString());
				}
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
		type = false;
		for (int i = 0; i < ufoList.size(); i++) {
			Point ufoTemp = ufoList.get(i);
			ufoTemp.x += distanceX;
			if (ufoTemp.x >= width) {
				ufoTemp.x = 0;
				ufoTemp.y += distanceY;
			}
			if (Math.random() < FIRING_FREQUENCY2) {
				fireStart(ufoTemp.x, ufoTemp.y, type);
			}
		}
	}
	
	public void fireStart(int posX, int posY, boolean t) {
		Point fire = new Point();
		fire.x = posX;
		fire.y = posY;
		fireMap.put(fire, t);
	}
	
	public void enemyShip() {
		type = false;
		if (!destroiedEnemy) {//飞船没坏
			enemyShipX += enemyShipSpeed;
			if (enemyShipX >= width) {
				enemyShipX = 0;
			}
			// 敌人飞船射出子弹
			if (Math.random() < FIRING_FREQUENCY) {
				fireStart(enemyShipX, enemyShipY, type);
			}
		} else {//飞船坏了，new出一个新飞船 
			enemyShipX = shipRadius;
			destroiedEnemy = false;
		}
	}

	// 玩家飞船射出子弹
	public void fireShow() {
		type = true;
		if (newFire) {
			fireStart(shipX, shipY, true);
		}
	}

	// ship运动控制
	public void shipMove() {
		int speed = shipSpeed;
		if(!destroied) {
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
		}} else {
			shipX = shipRadius;
			destroied = false;
		}
	}
	

	//相撞
	public static boolean collision(Point p1, Point p2, int r1, int r2) {
		double dx = p1.x - p2.x;
		double dy = p1.y - p2.y;
		double radiusSum = r1 + r2;
		return dx * dx + dy * dy <= radiusSum * radiusSum;
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


