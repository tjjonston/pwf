package poolobjects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public class Table {

	public static final Color TABLE_COLOR = new Color(33660);
	public static final double STEP_TIME = 1;
	public static final int LEFT_TURN = 0;	
	public static final int RIGHT_TURN = 1;

	public Ball[] balls;
	public double initialDirection;
	public double initialSpeed = .5;
	public int length;
	public int width;
	public boolean started;
	public boolean finished;
	private final int ARR_SIZE = 4;
	private final int[][] pockets;
	private final int POCKET_WIDTH = 55;
	private boolean check = false;
	
	public Table(int length, int width){
		balls = new Ball[15];
		initialDirection = Math.PI * (.5);
		balls[0] = new Ball(105, 300);
		balls[0].color = Color.white;
		balls[0].direction = Math.PI * .5;
		int added = 1;
		
		while(added < 15)
		{
			int xt = (int)((length-80)*Math.random()) + 40;
			int yt = (int)((width-80)*Math.random()) + 40;

			boolean b = true;
			for(int i = 0; i < added; i++)
				if(euclidDistance(balls[i].x, balls[i].y, xt, yt) < 2*Ball.BALL_WIDTH)
					b = false;
			if(b)
			{
				balls[added] = new Ball(xt, yt);
				if(added%2 == 0)
					balls[added].color = new Color(0xFFDD55);
				else
					balls[added].color = new Color(0xCC2200);
				if(added == 8)
					balls[added].color = Color.black;
				added++;
			}
		}
		
		this.length = length;
		this.width = width;
		int[][] ps = { {0,0}, {length/2, 0}, {length, 0}, {0, width}, {length/2, width}, {length, width}};
		pockets = ps;
		started = false;
		finished = true;
	}
	
	public void step(){
	//	System.out.print("stepping");
		long time = System.currentTimeMillis();
		ArrayList<Integer> queue = new ArrayList<Integer>();
		for(int i = 0; i < balls.length; i++)
			if(canMove(i))
					moveBall(i);
			else
				queue.add(i);
		for(Integer i : queue)
			handleCollisions(i);
		while(System.currentTimeMillis() - time < STEP_TIME)
			;
	}
	
	public void paint(Graphics g){
	g.setColor(new Color(0x662200));
	g.fillRect(0, 0, 1000, 1000);
	g.translate(10, 10);
	if(!finished){
		if(!finished)
			step();
		if(started)
		{
			boolean done = true;
			for(Ball b : balls)
				if(b != null)
					if(b.velocity != 0)
						done = false;
			if(done)
				{
				finished = true;
				started = false;
				}
		}
		if(finished)
			System.out.println("Done!");

	}
	
	g.setColor(TABLE_COLOR);
	g.fillRect(0, 0, length, width);
	g.setColor(Color.black);
	for(int[] p : pockets)
		g.fillOval(p[0]-(POCKET_WIDTH/2), p[1]-(POCKET_WIDTH/2), POCKET_WIDTH, POCKET_WIDTH);
	for(Ball b : balls)
		if(b != null)
			b.paint(g);	
	
	if(!started)
	{
		g.setColor(Color.RED);
		if(balls[0] != null)
			{
			drawArrow(g, (int)balls[0].x, (int)balls[0].y, (int)balls[0].x + (int)(initialSpeed * 40 * Math.cos(balls[0].direction)),  (int)balls[0].y-(int)(initialSpeed * 40 * Math.sin(balls[0].direction)));

			}
	
	}
	
	if(check)
		{
		g.setColor(Color.DARK_GRAY);
		drawArrow(g, (int)balls[0].x+ (int)(Ball.BALL_WIDTH * Math.cos(balls[0].direction+Math.PI/2))/2, (int)balls[0].y - (int)(Ball.BALL_WIDTH * Math.sin(balls[0].direction+Math.PI/2))/2, (int)balls[0].x+ (int)(Ball.BALL_WIDTH * Math.cos(balls[0].direction+Math.PI/2))/2 + (int)(800 * Math.cos(balls[0].direction)),  (int)balls[0].y - (int)(Ball.BALL_WIDTH * Math.sin(balls[0].direction+Math.PI/2))/2-(int)(800 * Math.sin(balls[0].direction)));
		drawArrow(g, (int)balls[0].x+ (int)(Ball.BALL_WIDTH * Math.cos(balls[0].direction-Math.PI/2))/2, (int)balls[0].y - (int)(Ball.BALL_WIDTH * Math.sin(balls[0].direction-Math.PI/2))/2, (int)balls[0].x+ (int)(Ball.BALL_WIDTH * Math.cos(balls[0].direction-Math.PI/2))/2 + (int)(800 * Math.cos(balls[0].direction)),  (int)balls[0].y - (int)(Ball.BALL_WIDTH * Math.sin(balls[0].direction-Math.PI/2))/2-(int)(800 * Math.sin(balls[0].direction)));
		
		g.setColor(Color.BLACK);
		
		drawArrow(g, (int)balls[0].x, (int)balls[0].y, (int)balls[0].x + (int)( 800 * Math.cos(balls[0].direction)),  (int)balls[0].y-(int)( 800 * Math.sin(balls[0].direction)));
		}
	

	}
	

	boolean canMove(int ballIndex){
		Ball focus = balls[ballIndex];
		if(focus == null)
			return false;
		float newX = (float) (focus.x + (STEP_TIME * focus.velocity * (Math.cos(focus.direction))));
		float newY = (float) (focus.y - (STEP_TIME * focus.velocity * (Math.sin(focus.direction))));
		for(int i = 0; i < balls.length; i++)
		{
			if(balls[i] != null && i != ballIndex && euclidDistance(newX, newY, balls[i].x, balls[i].y) < Ball.BALL_WIDTH && euclidDistance(newX, newY, balls[i].x, balls[i].y) < euclidDistance(balls[ballIndex].x, balls[ballIndex].y, balls[i].x, balls[i].y))
				return false;
		}
		
		if(newX < 0+Ball.BALL_WIDTH/2 || newX > length-Ball.BALL_WIDTH/2)
			{
			focus.direction = ((Math.PI)-focus.direction);
			focus.direction = fixAngle(focus.direction);
			}
		if( newY < 0+Ball.BALL_WIDTH/2)
			{
			focus.direction = (3*Math.PI*.5) - (focus.direction-(.5*Math.PI));
			focus.direction = fixAngle(focus.direction);
			}
		if(newY > width-Ball.BALL_WIDTH/2)
			{
			focus.direction = (3*Math.PI*.5) - (focus.direction-(.5*Math.PI));
			focus.direction = fixAngle(focus.direction);
			}
		
		boolean kill = false;
		for(int[] p : pockets)
			if(euclidDistance(newX, newY, p[0], p[1]) < (POCKET_WIDTH/2-(Ball.BALL_WIDTH/4)))
				kill = true;
		if(kill)
		{
			balls[ballIndex] = null;
		}
		return true;
	}
	
	void moveBall(int ballIndex){
		
		Ball focus = balls[ballIndex];
		if(focus == null)
			return;
		focus.x = (float) (focus.x + (STEP_TIME * focus.velocity * (Math.cos(focus.direction))));
		focus.y = (float) (focus.y - (STEP_TIME * focus.velocity * (Math.sin(focus.direction))));
		focus.velocity = focus.velocity * .999;
		if(focus.velocity < .4)
			focus.velocity *= .999;
		if(focus.velocity < .08)
			focus.velocity = 0;
//		System.out.println("Moving?");
	}
	
	void handleCollisions(int ballIndex){
	//	System.out.println("Collision");
	//	System.out.print("Collision coming from ball "+ballIndex+"\nBefore: ");
	//	statusReport();
		int i;
		int hit = -1;
		Ball focus = balls[ballIndex];
		if(focus == null)
			return;
		double newX = (float) (focus.x + (focus.velocity * (Math.cos(focus.direction))));
		double newY = (float) (focus.y - (focus.velocity * (Math.sin(focus.direction))));

		for(i = 0; i < balls.length; i++)
			{
			Ball focus2 = balls[i];
			if(focus2 == null)
				continue;
			if(i != ballIndex && euclidDistance(newX, newY, balls[i].x, balls[i].y) < Ball.BALL_WIDTH && euclidDistance(newX, newY, balls[i].x, balls[i].y) < euclidDistance(balls[ballIndex].x, balls[ballIndex].y, balls[i].x, balls[i].y))
					{
					hit = i;
					break;
					}
			}
		if(hit == -1)
				{
//				System.out.println("ERROR IN COLLISION, COLLISION REPORTED AND NO COLLISION PRESENT");
				return;
				}
		double angleBetween = angleBetweenPoints(balls[ballIndex].x, balls[ballIndex].y, balls[hit].x, balls[hit].y);
	//	System.out.print("Collision with angle of "+angleBetween/Math.PI+"pi.\n");
		
		double ballA, ballB, aVel, bVel;// = (-1*Math.PI/2) + angleBetween;
		double initdir = balls[ballIndex].direction;
		double initvel = balls[ballIndex].velocity;
		ballB = angleBetween;
		aVel = initvel * Math.sin(initdir - ballB);
		if(aVel < 0)
			aVel *= -1;
		bVel = Math.sqrt( (initvel*initvel) - (aVel*aVel));

		ballA = angleBetweenPoints(bVel*Math.cos(ballB), -1* bVel*Math.sin(ballB), initvel*Math.cos(initdir), -1* initvel*Math.sin(initdir) );
	//	System.out.println(ballA);

		balls[ballIndex].direction = ballA;
		balls[hit].direction = ballB;
		
		balls[ballIndex].velocity = aVel;// Math.sin(initdir - ballB) * initvel / Math.sin((Math.PI / 2)+(ballB - ballA));
		balls[hit].velocity = bVel;//  balls[ballIndex].velocity =  Math.sin(ballA-initdir) * initvel / Math.sin((Math.PI / 2)+(ballB - ballA));
		
	//	System.out.print("After: ");
	//	statusReport();
		
		//System.out.println("After collision Ball "+ballIndex+" has direction " +(balls[ballIndex].direction/Math.PI) +"pi which hit ball "+hit+" which now has direction "+(balls[hit].direction/Math.PI)+"pi and velocity "+balls[hit].velocity+".\n");

		
		
	}

	double euclidDistance(double newX, double newY, double x, double y){
		
		return Math.sqrt(	Math.pow(newX-x, 2)
								+ Math.pow(newY-y, 2)	);	
	}
	
	public void hit(){
		if(finished && !check)
		{
			if(balls[0] == null)
				return;
			balls[0].velocity = 1.5*initialSpeed;
			started = true;
			finished = false;
		}
	}
	
	public void turn(int dir){
		
		if(finished)
		{			
			if(balls[0] == null)
				return;
			double ammt = .05;
			if(check)
				ammt = .005;
			if(dir == LEFT_TURN)
				balls[0].direction = fixAngle(balls[0].direction + (ammt * Math.PI));
			if(dir == RIGHT_TURN)
				balls[0].direction = fixAngle(balls[0].direction - (ammt * Math.PI));
		}
	}
	
	public void power(int power){
		
		if(finished  && !check)
			if(power==1 && initialSpeed < .9)
				initialSpeed+=.1;
			else if(power == -1 && initialSpeed > .2)
				initialSpeed-=.1;
		
	}
	
	public static double angleBetweenPoints(double x1, double y1, double x2, double y2){
		if(x1 == x2)
			if(y1 < y2)
				return 3*Math.PI/2;
			else
				return Math.PI/2;
		if(y1 == y2)
			if(x1 > x2)
				return Math.PI;
			else
				return 0;
		double op = y1-y2;
		double adj = x2-x1;
		double angle = Math.atan(op/adj);
		if(op/adj < 0)
			if(op < 0)
				return angle+(2*Math.PI);
			else
				return angle+Math.PI;
		if(op < 0 && adj < 0)
			return angle+Math.PI;

		return angle;
	}
	
	public void statusReport(){
		double ang = 0;
		double vel = 0;
		for(int i = 0; i < balls.length; i++)
			{
			if(balls[i] == null)
				continue;
			ang+=balls[i].direction;
			vel+=(balls[i].velocity * balls[i].velocity);
			System.out.print("Ball "+i+":[x:"+balls[i].x+", y:"+balls[i].y+", direction:"+balls[i].direction/Math.PI+"pi, vel:"+balls[i].velocity+"] ");
			
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
	
    void drawArrow(Graphics g1, int x1, int y1, int x2, int y2) {
        Graphics2D g = (Graphics2D) g1.create();

        double dx = x2 - x1, dy = y2 - y1;
        double angle = Math.atan2(dy, dx);
        int len = (int) Math.sqrt(dx*dx + dy*dy);
        AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
        at.concatenate(AffineTransform.getRotateInstance(angle));
        g.transform(at);

        // Draw horizontal arrow starting in (0, 0)
        g.drawLine(0, 0, len, 0);
        g.fillPolygon(new int[] {len, len-ARR_SIZE, len-ARR_SIZE, len},
                      new int[] {0, -ARR_SIZE, ARR_SIZE, 0}, 4);
    }

	public void check(boolean check) {
		if(finished)
			this.check = check;
	}
}
