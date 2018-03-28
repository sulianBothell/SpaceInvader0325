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
	public static int bulletRadius = 2;
	public int bulletY = 0;//playY;
 	public int bulletX = 0;//playX + playDiam / 2;
 	
	// 键盘输入判断
	boolean movingLeft = false, movingRight = false;
	boolean fireLive = false;// 判断是否出界，或者消失
	//boolean processedFire = false;//判断子弹是否被处理过（出界或消失）
	boolean newFire = false; // 判断是否装新子弹
	//boolean collided = false;

	List<Point> fireList = new ArrayList<Point>();//子弹夹
	List<Point> ufoList = new ArrayList<Point>();//入侵者集


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
		
		/*for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 5; j++) {
				g.fillOval(ufoX - ufoRadius + i * 30, ufoY - ufoRadius + j * 30, ufoRadius*2, ufoRadius*2);
			}
		}*/
		
		for(int i = 0; i < ufoList.size(); i++) {
			Point p = ufoList.get(i);
			g.fillOval(p.x - ufoRadius, p.y - ufoRadius, ufoRadius*2, ufoRadius*2);
		}
		
		g.fillOval(playX - playRadius, playY- playRadius, playRadius*2, playRadius*2);
		//if(hasFire) {
		//int a = fireList.size();
		for (int i = 0; i < fireList.size(); i++) {
			Point p = fireList.get(i);
			g.fillOval(p.x - bulletRadius, p.y - bulletRadius, bulletRadius *2, bulletRadius *2);
		}
		//}
//		g.drawString("Bullets Count:" + fireList.size(), 10, 50);
		g.dispose();
	}

	public static void main(String[] args) {
		new SpaceInvader();
	}

	// 当前状态
	@Override
	public void run() {
		ufoShow();
		while (true) {
			ufoStart();
			// 要修改的地方
			move();
			// 子弹
			Iterator<Point> itr = fireList.iterator();
			while(itr.hasNext()) {
				Point fire = itr.next();
				fire.y -= bulletSpeed;
				if(fire.y < 0) {
					itr.remove();
				}
				
				Iterator<Point> itrUFO = ufoList.iterator();
				while(itrUFO.hasNext()) {
					Point ufo = itrUFO.next();
					if(ufo.y >=  fire.y - ufoRadius - bulletRadius && ufo.y <=  fire.y + ufoRadius + bulletRadius 
							&& ufo.x <= fire.x + ufoRadius + bulletRadius && ufo.x >= fire.x - ufoRadius - bulletRadius) {
							itr.remove();
							itrUFO.remove();
					}
				}

			}

			/*if (!fireList.isEmpty()) {
				for (int i = 0; i < fireList.size();) {
					Point fire = fireList.get(i);
					fire.y -= bulletSpeed;
					if (fire.y < 0) {
						fireList.remove(i);	
					}
					else {
						i++;
					}*/
					
					/*if(!ufoList.isEmpty()) {
						for(int j = 0; j < ufoList.size();) {
							Point temp = ufoList.get(j);
							if(temp.y >=  fire.y - ufoRadius - bulletRadius && temp.y <=  fire.y + ufoRadius + bulletRadius 
									&& temp.x <= fire.x + ufoRadius + bulletRadius && temp.x >= fire.x - ufoRadius - bulletRadius) {
									ufoList.remove(j);
									fireList.remove(i);
								} else {
									j++;
									i++;
								}
							}
						}
						}*/

				repaint();

				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
				}
			}
}
	
	public void ufoShow() {
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 5; j++) {
				int ufoPosX = ufoX - ufoRadius + i * 30;
				int ufoPosY = ufoY - ufoRadius + j * 30;
				Point ufoPos = new Point(ufoPosX, ufoPosY);
				ufoList.add(ufoPos);
			}
		}
	}

	// ufo运动控制
	public void ufoStart() {
		//ufoX += ufoSpeed;
		//ufoShow();
		for(int i = 0; i < ufoList.size(); i++) {
			Point ufoTemp = ufoList.get(i);
			ufoTemp.x += ufoSpeed;
		
			if (ufoTemp.x == width) {
				ufoTemp.x = 0;
			}
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
			Point fire = new Point();
			fire.x = playX;
			fire.y = playY;
			fireList.add(fire);
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
