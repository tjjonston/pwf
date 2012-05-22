package executables;

import gui.PoolPanel;

import javax.swing.JFrame;

import poolobjects.Table;

public class runner {

	
	public static void main(String[] args){
		
		System.out.println((Table.angleBetweenPoints(0, 0, -10, 10))/Math.PI);
		
		
		JFrame frame = new JFrame();
		frame.setSize(800, 450);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		
		PoolPanel panel = new PoolPanel(800, 440);
		frame.addKeyListener(panel);
		frame.add(panel);
		System.out.println("started");
		frame.setVisible(true);
		
	}
}
