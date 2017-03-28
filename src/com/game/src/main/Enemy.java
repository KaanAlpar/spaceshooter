package com.game.src.main;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

import com.game.src.main.classes.EntityA;
import com.game.src.main.classes.EntityB;
import com.game.src.main.libs.Animation;

public class Enemy extends GameObject implements EntityB {
	
	private Random r = new Random();
	private Textures tex;
	private double speed = r.nextInt(4) + 1;
	private Game game;
	private Controller c;
	
	Animation anim;
	
	public Enemy(double x, double y, Textures tex, Controller c, Game game){
		super(x, y);
		
		this.tex = tex;
		this.game = game;
		this.c = c;
		
		anim = new Animation(10, tex.enemy[0], tex.enemy[1], tex.enemy[2]);
	}
	
	public void tick(){
		y += speed;
		
		if(y > Game.HEIGHT * Game.SCALE + 50){
			speed = r.nextInt(4) + 1;
			y = -50;
			x = r.nextInt(Game.WIDTH * Game.SCALE);
		}
		
		for(int i = 0; i < game.ea.size(); i++){
			EntityA tempEnt = game.ea.get(i);
			
			if(Physics.Collision(this, tempEnt)){
				Explosion exp = new Explosion(x, y, tex, game, c);
				c.addEntity(exp);
				c.removeEntity(tempEnt);
				c.removeEntity(this);
				game.setEnemy_killed(game.getEnemy_killed() + 1);
				game.SCORE += 1;
			}
		}
		
		anim.runAnimation();
	}
	
	public void render(Graphics g){
		anim.drawAnimation(g, x, y, 0);
	}
	
	public Rectangle getBounds(){
		return new Rectangle((int)x, (int)y, 32, 32);
	}
	
	public double getX(){
		return x;
	}
	
	public double getY(){
		return y;
	}
}
