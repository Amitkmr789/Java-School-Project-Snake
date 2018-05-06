package cn.itcast.snake.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import cn.itcast.snake.listener.SnakeListener;
import cn.itcast.snake.util.Global;

public class Snake {
	
	public static final int UP = 1;
	public static final int DOWN = -1;
	public static final int LEFT = 2;
	public static final int RIGHT = -2;
	
	private int oldDirection, newDirection;
	
	private boolean life;;
	
	private Point oldTail;
	
	private LinkedList <Point> body = new LinkedList <Point>();//��LinkedList�����ߵ��ƶ�
	
	private Set<SnakeListener> listeners = new HashSet<SnakeListener>();
	
	
	public Snake(){
		init();
	}
	public void init(){
		int x = Global.WIDTH / 2;
		int y = Global.HEIGHT / 2;
		for(int i = 0; i < 3; i++){
			body.addFirst(new Point(x--, y));
		}
		oldDirection = newDirection = LEFT;
		life = true;
	}
	
	public void move(){
		System.out.println("Snake's move. ");
		
		if(!(oldDirection + newDirection == 0)){
			oldDirection = newDirection;
		}
		//ȥβ
		oldTail = body.removeLast();
			
		//�ȵõ���ͷ������
		int x = body.getFirst().x;//д��ͻ���Ƥ��λ
		int y = body.getFirst().y;
		switch(oldDirection){
		case UP:
			y--;
			if(y < 0){
				y = Global.HEIGHT - 1;
			}
			break;
		case DOWN:
			y++;
			if(y > Global.HEIGHT - 1){
				y = 0;
			}
			break;
		case LEFT:
			x--;
			if(x < 0){
				x = Global.WIDTH - 1;
			}
			break;
		case RIGHT:
			x++;
			if(x > Global.WIDTH - 1){
				x = 0;
			}
			break;
		}
		Point newHead = new Point(x, y);
		//��ͷ
		body.addFirst(newHead);
		
	}
	
	public void die(){
		life = false;
	}
	public Point getHead(){
		return body.getFirst();
	}
	
	public void changeDirection(int direction){
		System.out.println("Snake's changeDirection");
		newDirection = direction;
	}
	
	public void eatFood(){
		System.out.println("Snake's eatFood");
		Global.SPEED -= 10;
		body.addLast(oldTail);
	}
	
	public boolean isEatBody(){
		System.out.println("Snake's isEatBody");
		
		for(int i = 1; i < body.size(); i++){
			if(body.get(i).equals(this.getHead())){
				return true;
			}
		}
		return false;
	}
	
	public void drawMe(Graphics g){
		System.out.println("Snake's drawMe");
		g.setColor(Global.SNAKE_COLOR);
		for(Point p : body){
			g.fill3DRect(p.x * Global.CELL_SIZE, p.y * Global.CELL_SIZE, 
					Global.CELL_SIZE, Global.CELL_SIZE, true);
		}
	}
	
	private class SnakeDiver implements Runnable{//��ͣ����move�������߳�
		
		public void run(){
			while(life){
				move();
				for(SnakeListener l : listeners){
					l.snakeMoved(Snake.this);
				}
				try{
					Thread.sleep(Global.SPEED);
				}catch (InterruptedException e){
					e.printStackTrace();
				}
			}
		}
	}
	
	public void start(){//��������̵߳ķ���
		new Thread(new SnakeDiver()).start();
	}
	public void addSnakeListener(SnakeListener l){
		if(l != null){
			this.listeners.add(l);
		}
	}
}
