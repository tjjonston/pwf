package poolobjects;

import java.awt.Color;
import java.awt.Graphics;

public class Table {

	public static final Color TABLE_COLOR = new Color(33660);
	
	public Ball[] balls;
	public int length;
	public int width;
	public boolean started;
	public boolean finished;
	
	public Table(int length, int width){
		balls = new Ball[2];
		balls[0] = new Ball(100, 90);
		balls[1] = new Ball(200, 100);
		this.length = length;
		this.width = width;
		started = false;
		finished = false;
	}
	
	public void step(){
	//	System.out.print("stepping");
		
		for(int i = 0; i < balls.length; i++)
			if(canMove(i))
					moveBall(i);
		for(int i = 0; i < balls.length; i++)
			if(!(canMove(i)))
					handleCollisions(i);
			
	}
	
	public void paint(Graphics g){
		if(!finished)
			step();
		if(started)
		{
			boolean done = true;
			for(Ball b : balls)
				if(b.velocity > .01)
					done = false;
			if(done)
				finished = true;
		}
		g.setColor(TABLE_COLOR);
		g.fillRect(0, 0, length, width);
		for(Ball b : balls)
			b.paint(g);	
	}
	
	void ballAction(int ballIndex){
		
		if(canMove(ballIndex))
			moveBall(ballIndex);
		else
			handleCollisions(ballIndex);
	}
	
	boolean canMove(int ballIndex){
		Ball focus = balls[ballIndex];
		float newX = (float) (focus.x + (focus.velocity * (Math.cos(focus.direction))));
		float newY = (float) (focus.y + (focus.velocity * (Math.sin(focus.direction))));
		for(int i = 0; i < balls.length; i++)
		{
			if(i != ballIndex && euclidDistance(newX, newY, balls[i].x, balls[i].y) < Ball.BALL_WIDTH && euclidDistance(newX, newY, balls[i].x, balls[i].y) < euclidDistance(balls[ballIndex].x, balls[ballIndex].y, balls[i].x, balls[i].y))
				return false;
		}
		return true;
	}
	
	void moveBall(int ballIndex){
		Ball focus = balls[ballIndex];
		focus.x = (float) (focus.x + (focus.velocity * (Math.cos(focus.direction))));
		focus.y = (float) (focus.y - (focus.velocity * (Math.sin(focus.direction))));
		focus.velocity = focus.velocity * .999;
	//	if(focus.velocity < .03)
	//		focus.velocity = 0;
//		System.out.println("Moving?");
	}
	
	void handleCollisions(int ballIndex){

		System.out.print("Collision coming from ball "+ballIndex+"\nBefore: ");
		statusReport();
		int i;
		int hit = -1;
		Ball focus = balls[ballIndex];
		double newX = (float) (focus.x + (focus.velocity * (Math.cos(focus.direction))));
		double newY = (float) (focus.y + (focus.velocity * (Math.sin(focus.direction))));

		for(i = 0; i < balls.length; i++)
			{
			if(i != ballIndex && euclidDistance(newX, newY, balls[i].x, balls[i].y) < Ball.BALL_WIDTH && euclidDistance(newX, newY, balls[i].x, balls[i].y) < euclidDistance(balls[ballIndex].x, balls[ballIndex].y, balls[i].x, balls[i].y))
					{
					hit = i;
					break;
					}
			}
		if(hit == -1)
				{
				System.out.println("ERROR IN COLLISION, COLLISION REPORTED AND NO COLLISION PRESENT");
				return;
				}
		double angleBetween = angleBetweenPoints(balls[ballIndex].x, balls[ballIndex].y, balls[hit].x, balls[hit].y);
		System.out.print("Collision with angle of "+angleBetween/Math.PI+"pi.\n");
		
		double ballA, ballB, aVel, bVel;// = (-1*Math.PI/2) + angleBetween;
		double initdir = balls[ballIndex].direction;
		double initvel = balls[ballIndex].velocity;
		ballB = angleBetween;
		if(angleBetween > Math.PI/2 && angleBetween < 3*Math.PI/2)
		{
		//	System.out.println("here");
			aVel = initvel * Math.sin(ballB - initdir);
			if(aVel < 0)
				aVel*=-1;
			bVel = Math.sqrt( (initvel*initvel) - (aVel*aVel));
			ballA = angleBetweenPoints(bVel*Math.cos(ballB), bVel*Math.sin(ballB), initvel*Math.cos(initdir), initvel*Math.sin(initdir) );
		}
		else
		{
			aVel = initvel * Math.sin(initdir - ballB);
			if(aVel < 0)
				aVel*=-1;
			bVel = Math.sqrt( (initvel*initvel) - (aVel*aVel));
			ballA = angleBetweenPoints(bVel*Math.cos(ballB),bVel*Math.sin(ballB), initvel*Math.cos(initdir), initvel*Math.sin(initdir) );
		}
		if(ballB == balls[ballIndex].direction)
		{
			balls[hit].velocity = balls[ballIndex].velocity;
			balls[ballIndex].velocity = 0;
			
			balls[ballIndex].direction = ballA;
			balls[hit].direction = ballB;
			System.out.println("easy");
			return;
		}
		balls[ballIndex].direction = ballA;
		balls[hit].direction = ballB;
		
		balls[ballIndex].velocity = aVel;// Math.sin(initdir - ballB) * initvel / Math.sin((Math.PI / 2)+(ballB - ballA));
		balls[hit].velocity = bVel;//  balls[ballIndex].velocity =  Math.sin(ballA-initdir) * initvel / Math.sin((Math.PI / 2)+(ballB - ballA));
		
		System.out.print("After: ");
		statusReport();
		
		//System.out.println("After collision Ball "+ballIndex+" has direction " +(balls[ballIndex].direction/Math.PI) +"pi which hit ball "+hit+" which now has direction "+(balls[hit].direction/Math.PI)+"pi and velocity "+balls[hit].velocity+".\n");

		
		
	}

	double euclidDistance(double newX, double newY, double x, double y){
		
		return Math.sqrt(	Math.pow(newX-x, 2)
								+ Math.pow(newY-y, 2)	);	
	}
	
	public void hit(){
//		balls[0].direction = Math.PI;
		balls[0].velocity = .5;
		started = true;
	}
	
	public static double angleBetweenPoints(double x1, double y1, double x2, double y2){
		if(x1 == x2)
			if(y1 < y2)
				return 3*Math.PI/2;
			else
				return Math.PI/2;
		double angle = Math.atan((y1-y2) / (x2-x1));
		if(angle < 0)
			angle+= 2 *Math.PI;
		else
			if(x2 < x1)
				angle += Math.PI;

		
		return angle;
	}
	
	public void statusReport(){
		double ang = 0;
		double vel = 0;
		for(int i = 0; i < balls.length; i++)
			{
			ang+=balls[i].direction;
			vel+=(balls[i].velocity * balls[i].velocity);
			System.out.print("Ball "+i+":[direction:"+balls[i].direction/Math.PI+"pi, vel:"+balls[i].velocity+"] ");
			}
		System.out.print("total: [angle:"+ang/Math.PI+"pi, vel:"+Math.sqrt(vel)+"] ");
		System.out.println();
		
	}
	
	public double fixAngle(double angle){
		while(angle < 0)
			angle+= 2*Math.PI;
		while(angle >= 2*Math.PI)
			angle -= 2*Math.PI;
		return angle;
		
	}
}
