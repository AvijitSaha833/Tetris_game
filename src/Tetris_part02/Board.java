package Tetris_part02;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import Tetris_part02.Shape.Tetrominoes;


public class Board extends JPanel implements ActionListener {

	  private static final int BOARD_WIDTH = 10;
	  private static final int BOARD_HEIGHT = 22;
	  private Timer timer;
	  private boolean isFallingFinished = false;
	  private boolean isStarted = false;
	  private boolean isPaused = false;
	  private int numLinesRemoved = 0;
	  private int curX = 0;
	  private int curY = 0;
	  private JLabel statusBar;
	  private Shape curPiece;
	  private Tetrominoes[] board;
	 
	  public Board(Tetris parent) {
	    setFocusable(true);
	    curPiece = new Shape();
	    timer = new Timer(400, this); // timer for lines down
	    statusBar = parent.getStatusBar();
	    board = new Tetrominoes[BOARD_WIDTH * BOARD_HEIGHT];
	    clearBoard();
	    addKeyListener(new MyTetrisAdapter());
	  }
	 
	  public int squareWidth() {
	    return (int) getSize().getWidth() / BOARD_WIDTH;
	  }
	 
	  public int squareHeight() {
	    return (int) getSize().getHeight() / BOARD_HEIGHT;
	  }
	 
	  public Tetrominoes shapeAt(int x, int y) {
	    return board[y * BOARD_WIDTH + x];
	  }
	 
	  private void clearBoard() {
	    for (int i = 0; i < BOARD_HEIGHT * BOARD_WIDTH; i++) {
	      board[i] = Tetrominoes.NoShape;
	    }
	  }
	 
	  private void pieceDropped() {
	    for (int i = 0; i < 4; i++) {
	      int x = curX + curPiece.x(i);
	      int y = curY - curPiece.y(i);
	      board[y * BOARD_WIDTH + x] = curPiece.getShape();
	    }
	 
	    removeFullLines();
	 
	    if (!isFallingFinished) {
	      newPiece();
	    }
	  }
	 
	  public void newPiece() {
	    curPiece.setRandomShape();
	    curX = BOARD_WIDTH / 2 + 1;
	    curY = BOARD_HEIGHT - 1 + curPiece.minY();
	 
	    if (!tryMove(curPiece, curX, curY - 1)) {
	      curPiece.setShape(Tetrominoes.NoShape);
	      timer.stop();
	      isStarted = false;
	      statusBar.setText("Game Over");
	    }
	  }
	 
	  private void oneLineDown() {
	    if (!tryMove(curPiece, curX, curY - 1))
	      pieceDropped();
	  }
	 
	  @Override
	  public void actionPerformed(ActionEvent ae) {
	    if (isFallingFinished) {
	      isFallingFinished = false;
	      newPiece();
	    } else {
	      oneLineDown();
	    }
	  } 
	 
	  private void drawSquare(Graphics g, int x, int y, Tetrominoes shape) {
	    Color color = shape.color;
	    g.setColor(color);
	    g.fillRect(x + 1, y + 1, squareWidth() - 2, squareHeight() - 2);
	    g.setColor(color.brighter());
	    g.drawLine(x, y + squareHeight() - 1, x, y);
	    g.drawLine(x, y, x + squareWidth() - 1, y);
	    g.setColor(color.darker());
	    g.drawLine(x + 1, y + squareHeight() - 1, x + squareWidth() - 1, y + squareHeight() - 1);
	    g.drawLine(x + squareWidth() - 1, y + squareHeight() - 1, x + squareWidth() - 1, y + 1);
	  }
	 
	  @Override
	  public void paint(Graphics g) {
	    super.paint(g);
	    Dimension size = getSize();
	    int boardTop = (int) size.getHeight() - BOARD_HEIGHT * squareHeight();
	 
	    for (int i = 0; i < BOARD_HEIGHT; i++) {
	      for (int j = 0; j < BOARD_WIDTH; ++j) {
	        Tetrominoes shape = shapeAt(j, BOARD_HEIGHT - i - 1);
	 
	        if (shape != Tetrominoes.NoShape) {
	          drawSquare(g, j * squareWidth(), boardTop + i * squareHeight(), shape);
	        }
	      }
	    }
	 
	    if (curPiece.getShape() != Tetrominoes.NoShape) {
	      for (int i = 0; i < 4; ++i) {
	        int x = curX + curPiece.x(i);
	        int y = curY - curPiece.y(i);
	        drawSquare(g, x * squareWidth(), boardTop + (BOARD_HEIGHT - y - 1) * squareHeight(), curPiece.getShape());
	      }
	    }
	  }
	 
	  public void start() {
	    if (isPaused)
	      return;
	 
	    isStarted = true;
	    isFallingFinished = false;
	    numLinesRemoved = 0;
	    clearBoard();
	    newPiece();
	    timer.start();
	  }
	 
	  public void pause() {
	    if (!isStarted)
	      return;
	 
	    isPaused = !isPaused;
	 
	    if (isPaused) {
	      timer.stop();
	      statusBar.setText("Paused");
	    } else {
	      timer.start();
	      statusBar.setText(String.valueOf(numLinesRemoved));
	    }
	 
	    repaint();
	  }
	 
	  private boolean tryMove(Shape newPiece, int newX, int newY) {
	    for (int i = 0; i < 4; ++i) {
	      int x = newX + newPiece.x(i);
	      int y = newY - newPiece.y(i);
	 
	      if (x < 0 || x >= BOARD_WIDTH || y < 0 || y >= BOARD_HEIGHT)
	        return false;
	 
	      if (shapeAt(x, y) != Tetrominoes.NoShape)
	        return false;
	    }
	 
	    curPiece = newPiece;
	    curX = newX;
	    curY = newY;
	    repaint();
	 
	    return true;
	  }
	 
	  private void removeFullLines() {
	    int numFullLines = 0;
	 
	    for (int i = BOARD_HEIGHT - 1; i >= 0; --i) {
	      boolean lineIsFull = true;
	 
	      for (int j = 0; j < BOARD_WIDTH; ++j) {
	        if (shapeAt(j, i) == Tetrominoes.NoShape) {
	          lineIsFull = false;
	          break;
	        }
	      }
	 
	      if (lineIsFull) {
	        ++numFullLines;
	 
	        for (int k = i; k < BOARD_HEIGHT - 1; ++k) {
	          for (int j = 0; j < BOARD_WIDTH; ++j) {
	            board[k * BOARD_WIDTH + j] = shapeAt(j, k + 1);
	          }
	        }
	      }
	 
	      if (numFullLines > 0) {
	        numLinesRemoved += numFullLines;
	        statusBar.setText(String.valueOf(numLinesRemoved));
	        isFallingFinished = true;
	        curPiece.setShape(Tetrominoes.NoShape);
	        repaint();
	      }
	    }
	  }
	 
	  private void dropDown() {
	    int newY = curY;
	 
	    while (newY > 0) {
	      if (!tryMove(curPiece, curX, newY - 1))
	        break;
	 
	      --newY;
	    }
	 
	    pieceDropped();
	  }
	 
	  class MyTetrisAdapter extends KeyAdapter {
	    @Override
	    public void keyPressed(KeyEvent ke) {
	      if (!isStarted || curPiece.getShape() == Tetrominoes.NoShape)
	        return;
	 
	      int keyCode = ke.getKeyCode();
	 
	      if (keyCode == 'p' || keyCode=='P')
	        pause();
	 
	      if (isPaused)
	        return;
	 
	      switch (keyCode) {
	        case KeyEvent.VK_LEFT:
	          tryMove(curPiece, curX - 1, curY);
	          break;
	        case KeyEvent.VK_RIGHT:
	          tryMove(curPiece, curX + 1, curY);
	          break;
	        case KeyEvent.VK_DOWN:
	          tryMove(curPiece.rotateRight(), curX, curY);
	          break;
	        case KeyEvent.VK_UP:
	          tryMove(curPiece.rotateLeft(), curX, curY);
	          break;
	        case KeyEvent.VK_SPACE:
	          dropDown();
	          break;
	        case 'd':
	        case 'D':
	          oneLineDown();
	          break;
	      }
	 
	    }
	  }
}

