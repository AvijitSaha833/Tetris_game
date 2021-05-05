package Tetris_part01;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class Tetris extends JFrame{

	private JLabel statusbar;
	
	public Tetris() {
	    statusbar = new JLabel("0"); 
	    add(statusbar, BorderLayout.SOUTH);
	    Board board = new Board(this);
	    add(board);
	    board.start();		// start lines down
	    setSize(200, 400);
	    setTitle("My Tetris ");
	    setDefaultCloseOperation(EXIT_ON_CLOSE);
	  }
	public JLabel getStatusBar() {
		return statusbar;
	}
	public static void main(String[] args) {
	    Tetris myTetris = new Tetris();
	    myTetris.setLocationRelativeTo(null);  // center
	    myTetris.setVisible(true);
	  }

}
