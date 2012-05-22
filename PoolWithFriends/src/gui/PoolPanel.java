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
		System.out.println("hit");
		table.hit();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
