package com.game.src.main;

import java.awt.Graphics;

import javax.swing.Timer;

import com.game.src.main.libs.Animation;

public class Explosion extends GameObject{
	Textures tex;
	Game game;
	Animation anim;
	Controller c;
	
	public static long NOW;
	public static long LATER;
	public static boolean DONE = false;
	
	public Explosion(double x, double y, Textures tex, Game game, Controller c){
		super(x, y);
		this.game = game;
		this.tex = tex;
		this.c = c;
		
		this.NOW = System.currentTimeMillis();
		
		anim = new Animation(2, tex.explosion[0], tex.explosion[1], tex.explosion[2],tex.explosion[3],tex.explosion[4],tex.explosion[5],tex.explosion[6],tex.explosion[7],
																tex.explosion[8],tex.explosion[9],tex.explosion[10],tex.explosion[11],tex.explosion[12],tex.explosion[13]);
	}
	
	public void tick(){
		anim.runAnimation();
		
		LATER = System.currentTimeMillis();
		if(LATER - NOW >= 750){
			c.removeEntity(this);
		}
	}
	
	public void render(Graphics g){
		anim.drawAnimation(g, x, y, 0);
	}
	
	public double getX(){
		return x;
	}
	
	public double getY(){
		return y;
	}
}
