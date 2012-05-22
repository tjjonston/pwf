package executables;

import java.awt.BorderLayout;

import gui.PoolPanel;

import javax.swing.JFrame;

import poolobjects.Table;

public class runner {

	
	public static void main(String[] args){
		
		JFrame frame = new JFrame();
		frame.setSize(790, 435);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		
		PoolPanel panel = new PoolPanel(750, 375);
		frame.addKeyListener(panel);
		frame.add(panel);
		frame.setVisible(true);
		
	}
}
