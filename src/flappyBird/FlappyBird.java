package flappyBird;

import java.awt.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.Timer;

import javax.swing.JFrame;

public class FlappyBird implements ActionListener, MouseListener, KeyListener{
	
	public static FlappyBird flappyBird;
	
	public final int WIDTH = 1200, HEIGHT = 800;
	
	public Renderer renderer;
	
	public Rectangle bird;
	
	public ArrayList<Rectangle> columns;
	
	public Random rand;
	
	public int ticks, yMotion, score;
	
	public boolean gameOver, started;
	
	public FlappyBird() {
		JFrame jframe = new JFrame();
		Timer timer = new Timer(20, this);
		
		renderer = new Renderer();
		rand = new Random();
		
		jframe.add(renderer);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.addMouseListener(this);
		jframe.setSize(WIDTH, HEIGHT);
		jframe.setVisible(true);
		jframe.setTitle("Flappy Bird");
		jframe.setResizable(false);
		jframe.addKeyListener(this);
		
		bird = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20);
		columns = new ArrayList<Rectangle>();
		
		addColumn(true);
		addColumn(true);
		addColumn(true);
		addColumn(true);
		
		timer.start();
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		int speed = 10;
		ticks++;
		
		if(started) {
			for(int i = 0; i < columns.size(); i++) {
				Rectangle column = columns.get(i);
				column.x -= speed;
			}
			
			if(ticks % 2 == 0 && yMotion < 15) {
				yMotion += 2;
			}
			
			for(int i = 0; i < columns.size(); i++) {
				Rectangle column = columns.get(i);
				
				if(column.x + column.width < 0)
				{
					columns.remove(column);
					
					if(column.y == 0) {
						addColumn(false);
					}
					
				}
			}
			
			bird.y += yMotion;
			
			//check for collision
			for(Rectangle column: columns) {
				if(column.y == 0 && bird.x + bird.width / 2 > column.x + column.width / 2 - 5 && bird.x + bird.width / 2 < column.x + column.width / 2 + 5) {
					score++;
				}
				
				if(column.intersects(bird)) {
					gameOver = true;
					
					if(bird.x <= column.x ) {
						bird.x = column.x - bird.width;
					}else {
						//if it is intersecting top column
						if(column.y != 0) {
							bird.y = column.x - bird.width;
						}else if(bird.y < column.height){
							bird.y = column.height;
						}
					}
					
				}
			}
			
			if(bird.y > HEIGHT - 120 || bird.y < 0) {
				gameOver = true;
			}
			
			if(bird.y + yMotion >= HEIGHT - 120) {
				bird.y = HEIGHT - 120 - bird.height;
			}
		}
		
		renderer.repaint();
	}
	
	public void paintColumn(Graphics g, Rectangle column) {
		g.setColor(Color.green.darker());
		g.fillRect(column.x, column.y, column.width, column.height);
	}
	
	public void addColumn(boolean start) {
		int space = 300;
		int width = 100;
		int height = 50 + rand.nextInt(300);
				
		if(start) {
			columns.add(new Rectangle(WIDTH + width + columns.size() * 300, HEIGHT - height -120, width, height));
			columns.add(new Rectangle(WIDTH + width + (columns.size() - 1) * 300, 0, width, HEIGHT - height - space));
		}else {
			columns.add(new Rectangle(columns.get(columns.size() -1).x + 600, HEIGHT - height -120, width, height));
			columns.add(new Rectangle(columns.get(columns.size() -1).x, 0, width, HEIGHT - height - space));
		}
		
	}
	
	public static void main(String[] args) {
		flappyBird = new FlappyBird();
	}

	public void repaint(Graphics g) {
		g.setColor(Color.cyan);	
		g.fillRect(0,  0, WIDTH, HEIGHT);
		
		g.setColor(Color.orange);
		g.fillRect(0,  HEIGHT - 150, WIDTH, 150);
		
		g.setColor(Color.green);
		g.fillRect(0, HEIGHT - 150, WIDTH, 20);
		
		g.setColor(Color.red);
		g.fillRect(bird.x,  bird.y, bird.width, bird.height);
		
		for(Rectangle column: columns) {
			paintColumn(g, column);
		}
		
		g.setColor(Color.WHITE);
		g.setFont(new Font("Verdana",Font.BOLD, 100));
		
		if(!started) {
			g.drawString("Click to start!", 75, HEIGHT / 2 - 50);
		}
		
		if(gameOver) {
			g.drawString("Game Over", 100, HEIGHT / 2 - 50);
		}
		
		if(!gameOver && !started) {
			g.drawString(String.valueOf(score), WIDTH / 2 - 25, 100);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		jump();
	}

	private void jump() {
		if(gameOver) {
			bird = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20);
			columns.clear();
			yMotion = 0;
			score = 0;
			
			addColumn(true);
			addColumn(true);
			addColumn(true);
			addColumn(true);
			
			gameOver = false;
		}
		if(!started) {
			started = true;
		}else if(!gameOver) {
			if(yMotion > 0) {
				yMotion = 0;
			}else {
				yMotion -= 20;
			}
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_SPACE) {
				jump();
			}			
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_U) {
			jump();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	
}
