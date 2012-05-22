package gui;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

import poolobjects.Table;

public class PoolPanel extends JPanel implements KeyListener{

	private static final long serialVersionUID = 1L;
	public Table table;
	
	public PoolPanel(int x, int y){
		super();
		setSize(x, y);
		table = new Table(x, y);
	}
	
	protected void paintComponent(Graphics g){

	//	System.out.println("here!");
		super.paintComponent(g);

		table.paint(g);

		repaint();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode() == KeyEvent.VK_SPACE)
			table.hit();
		if(e.getKeyCode() == KeyEvent.VK_LEFT)
			table.turn(Table.LEFT_TURN);
		if(e.getKeyCode() == KeyEvent.VK_RIGHT)
				table.turn(Table.RIGHT_TURN);
		if(e.getKeyCode() == KeyEvent.VK_UP)
			table.power(1);
		if(e.getKeyCode() == KeyEvent.VK_DOWN)
				table.power(-1);
		if(e.getKeyCode() == KeyEvent.VK_CONTROL)
			table.check(true);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode() == KeyEvent.VK_CONTROL)
			table.check(false);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
