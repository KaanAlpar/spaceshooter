package com.game.src.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.JFrame;

import com.game.src.main.classes.EntityA;
import com.game.src.main.classes.EntityB;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 320;
	public static final int HEIGHT = WIDTH / 12 * 9;
	public static final int SCALE = 2;
	public final String TITLE = "2D Space Game";
	
	private boolean running = false;
	private Thread thread;
	
	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private BufferedImage spriteSheet = null;
	private BufferedImage exploSheet = null;
	private BufferedImage background = null;
	
	private boolean isShooting = false;
	
	private int enemy_count = 1;
	private int enemy_killed = 0; 
	
	private Player p;
	private Controller c;
	private Textures tex;
	private Menu menu;
	private QuitMenu qmenu;
	
	public LinkedList<EntityA> ea;
	public LinkedList<EntityB> eb;
	
	public static int HEALTH = 100 *2;
	public static int SCORE = 0;
	
	public static enum STATE{
		MENU,
		GAME,
		QUIT_MENU
	};
	public static STATE State = STATE.MENU;
	
	public void init(){
		requestFocus();
		BufferedImageLoader loader = new BufferedImageLoader();
		
		try {
			spriteSheet = loader.loadImage("/sprite_sheet.png");
			exploSheet = loader.loadImage("/exploSheet.png");
			background = loader.loadImage("/background.png");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		tex = new Textures(this);
		c = new Controller(tex, this);
		p = new Player(Game.WIDTH / 2 * Game.SCALE - 32, Game.HEIGHT * Game.SCALE, tex, this, c);
		menu = new Menu();
		qmenu = new QuitMenu();
		
		ea = c.getEntityA();
		eb = c.getEntityB();
		
		this.addKeyListener(new KeyInput(this));
		this.addMouseListener(new MouseInput());
		
		c.createEnemy(enemy_count);
	}
	
	private synchronized void start() {
		if(running)
			return;
		
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	private synchronized void stop() {
		if(!running)
			return;
		
		running = false;
	
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.exit(1);
		
	}
	
	public void run() {
		init();
		long lastTime = System.nanoTime();
		final double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int updates = 0;
		int frames = 0;
		long timer = System.currentTimeMillis();
		
		while(running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			
			if(delta >= 1) {
				tick();
				updates++;
				delta--;
			}
			render();
			frames++;
	
			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println("Ticks: " + updates + ", Frames: " + frames);
				updates = 0;
				frames = 0;
			}
			
		}
		stop();			
	}

	private void tick() {
		if(State == STATE.GAME){
			p.tick();
			c.tick();
		}
		
		if(enemy_killed >= enemy_count){
			enemy_count += 2;
			if(enemy_count >= 25)
				enemy_count += 2;
			enemy_killed = 0;
			c.createEnemy(enemy_count);
		}
	}
	
	private void render() {
		
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		//////////////////////////////////
		
		g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
		
		g.drawImage(background, 0, 0, null);

		if(State == STATE.GAME){
			Font fnt0 = new Font("arial", Font.BOLD, 10);
			g.setFont(fnt0);
			g.setColor(Color.white);
			g.drawString("Made by Kaan Alpar - 2017", Game.WIDTH * Game.SCALE - 130, Game.HEIGHT * Game.SCALE);
			
			c.render(g);
			p.render(g);
			
			g.setColor(Color.gray);
			g.fillRect(5, 5, 200, 25);
			
 			g.setColor(Color.blue);
			g.fillRect(5, 5, HEALTH, 25);
			
			g.setColor(Color.white);
			g.drawRect(5, 5, 200, 25);
			
			fnt0 = new Font("arial", Font.BOLD, 20);
			g.setFont(fnt0);
			g.setColor(Color.white);
			g.drawString("Score: ", Game.WIDTH - 100, 25);
			g.drawString(String.valueOf(SCORE), Game.WIDTH - 35, 25);
			
			
		} else if(State == STATE.MENU){
			menu.render(g);
		} else if(State == STATE.QUIT_MENU){
			qmenu.render(g);
		}
		
		//////////////////////////////////
		g.dispose();
		bs.show();
		
	}
	
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();

		if(State == STATE.GAME){
			if(key == KeyEvent.VK_RIGHT) {
				p.setVelX(5);
			} 
			if(key == KeyEvent.VK_LEFT) {
				p.setVelX(-5);
			} 
			if(key == KeyEvent.VK_UP) {
				p.setVelY(-5);
			} 
			if(key == KeyEvent.VK_DOWN) {
				p.setVelY(5);
			}	
			if(key == KeyEvent.VK_SPACE && !isShooting){
				isShooting = true;
				c.addEntity(new Bullet(p.getX(), p.getY(), tex, this));
			}
		}
	}
	
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		
		if(State == STATE.GAME){
			if(key == KeyEvent.VK_RIGHT && p.getVelX() > 0) {
				p.setVelX(0);
			} 
			if(key == KeyEvent.VK_LEFT && p.getVelX() < 0) {
				p.setVelX(0);
			} 
			if(key == KeyEvent.VK_UP && p.getVelY() < 0) {
				p.setVelY(0);
			} 
			if(key == KeyEvent.VK_DOWN && p.getVelY() > 0) {
				p.setVelY(0);
			}
			if(key == KeyEvent.VK_SPACE){
				isShooting = false;
			}
		}
	}
	
	public static void main(String[] args) {
		Game game = new Game();
		
		game.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		game.setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		game.setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		
		JFrame frame = new JFrame(game.TITLE);
		frame.add(game);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		game.start();
	}
	
	public BufferedImage getSpriteSheet() {
		return spriteSheet;
	}
	
	public BufferedImage getExploSheet() {
		return exploSheet;
	}

	public int getEnemy_count() {
		return enemy_count;
	}

	public void setEnemy_count(int enemy_count) {
		this.enemy_count = enemy_count;
	}

	public int getEnemy_killed() {
		return enemy_killed;
	}

	public void setEnemy_killed(int enemy_killed) {
		this.enemy_killed = enemy_killed;
	}	
}






















