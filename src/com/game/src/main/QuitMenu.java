package com.game.src.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class QuitMenu {

	public void render(Graphics g){
		Graphics2D g2d = (Graphics2D) g;
		
		Font fnt0 = new Font("arial", Font.BOLD, 50);
		g.setFont(fnt0);
		g.setColor(Color.red);
		g.drawString("Game Over", Game.WIDTH * Game.SCALE / 2 - 125, Game.HEIGHT * Game.SCALE / 2);
		
		fnt0 = new Font("arial", Font.BOLD, 30);
		g.setFont(fnt0);
		g.setColor(Color.gray);
		g.drawString("Score: ", Game.WIDTH * Game.SCALE / 2 - 60,  Game.HEIGHT * Game.SCALE / 2 + 50);
		g.drawString(String.valueOf(Game.SCORE), Game.WIDTH * Game.SCALE / 2 + 40, Game.HEIGHT * Game.SCALE / 2 + 50);	
		
		fnt0 = new Font("arial", Font.BOLD, 10);
		g.setFont(fnt0);
		g.setColor(Color.white);
		g.drawString("Made by Kaan Alpar - 2017", Game.WIDTH * Game.SCALE - 130, Game.HEIGHT * Game.SCALE);
	}
}
