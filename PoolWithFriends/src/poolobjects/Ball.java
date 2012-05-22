package poolobjects;

import java.awt.Color;
import java.awt.Graphics;

public class Ball {

	public static final int  BALL_WIDTH = 24;

	public Ball(int x, int y){
		this.x = x;
		this.y = y;
		direction = 0;
		velocity = 0;
		color = Color.BLACK;
	}
	
	public double x;
	public double y;
	public double direction;
	public double velocity;	
	public Color color;

	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		g.setColor(color);
		g.fillOval((int)x-(BALL_WIDTH/2), (int)y-(BALL_WIDTH/2), BALL_WIDTH, BALL_WIDTH);
	}
	
}
