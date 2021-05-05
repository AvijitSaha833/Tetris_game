package Tetris_part02;


import java.awt.Color;
import java.util.Random;

public class Shape {
	
	enum Tetrominoes {
		  NoShape(new int[][] { { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 } }, new Color(0, 0, 0)),
		  ZShape(new int[][] { { 0, -1 }, { 0, 0 }, { -1, 0 }, { -1, 1 } }, new Color(204, 102, 102)),
		  SShape(new int[][] { { 0, -1 }, { 0, 0 }, { 1, 0 }, { 1, 1 } }, new Color(102, 204, 102)),
		  LineShape(new int[][] { { 0, -1 }, { 0, 0 }, { 0, 1 }, { 0, 2 } }, new Color(102, 102, 204)),
		  TShape(new int[][] { { -1, 0 }, { 0, 0 }, { 1, 0 }, { 0, 1 } }, new Color(204, 204, 102)),
		  SquareShape(new int[][] { { 0, 0 }, { 1, 0 }, { 0, 1 }, { 1, 1 } }, new Color(204, 102, 204)),
		  LShape(new int[][] { { -1, -1 }, { 0, -1 }, { 0, 0 }, { 0, 1 } }, new Color(102, 204, 204)),
		  MirroredLShape(new int[][] { { 1, -1 }, { 0, -1 }, { 0, 0 }, { 0, 1 } }, new Color(218, 170, 0));
		 
		  public int[][] coords;
		  public Color color;
		 
		  private Tetrominoes(int[][] coords, Color c) {
		    this.coords = coords;
		    color = c;
		  }
		}
	private Tetrominoes pieceShape;
	  private int[][] coords;
	 
	  public Shape() {
	    coords = new int[4][2];
	    setShape(Tetrominoes.NoShape);
	  }
	 
	  public void setShape(Tetrominoes shape) {
	    for (int i = 0; i < 4; i++) {
	      for (int j = 0; j < 2; ++j) {
	        coords[i][j] = shape.coords[i][j];
	      }
	    }
	 
	    pieceShape = shape;
	  }
	 
	  private void setX(int index, int x) {
	    coords[index][0] = x;
	  }
	 
	  private void setY(int index, int y) {
	    coords[index][1] = y;
	  }
	 
	  public int x(int index) {
	    return coords[index][0];
	  }
	 
	  public int y(int index) {
	    return coords[index][1];
	  }
	 
	  public Tetrominoes getShape() {
	    return pieceShape;
	  }
	 
	  public void setRandomShape() {
	    Random r = new Random();
	    int x = Math.abs(r.nextInt()) % 7 + 1;
	    Tetrominoes[] values = Tetrominoes.values();
	    setShape(values[x]);
	  }
	 
	  public int minX() {
	    int m = coords[0][0];
	 
	    for (int i = 0; i < 4; i++) {
	      m = Math.min(m, coords[i][0]);
	    }
	 
	    return m;
	  }
	 
	  public int minY() {
	    int m = coords[0][1];
	 
	    for (int i = 0; i < 4; i++) {
	      m = Math.min(m, coords[i][1]);
	    }
	 
	    return m;
	  }
	 
	  public Shape rotateLeft() {
	    if (pieceShape == Tetrominoes.SquareShape)
	      return this;
	 
	    Shape result = new Shape();
	    result.pieceShape = pieceShape;
	 
	    for (int i = 0; i < 4; i++) {
	      result.setX(i, y(i));
	      result.setY(i, -x(i));
	    }
	 
	    return result;
	  }
	 
	  public Shape rotateRight() {
	    if (pieceShape == Tetrominoes.SquareShape)
	      return this;
	 
	    Shape result = new Shape();
	    result.pieceShape = pieceShape;
	 
	    for (int i = 0; i < 4; i++) {
	      result.setX(i, -y(i));
	      result.setY(i, x(i));
	    }
	 
	    return result;
	  }
}





