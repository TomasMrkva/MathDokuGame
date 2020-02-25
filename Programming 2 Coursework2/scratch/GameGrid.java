import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;

public class GameGrid extends Node{

	private Group grid;
	private int N;
	private int width;

	public GameGrid(int N, int width) {

		grid = null;
		this.N = 0;
		this.width = 0;
	}

	public void draw() {
		
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {

				MyRectangle r = new MyRectangle(width, width);

				r.setRow(i);
				r.setCol(j);
				r.setStroke(Color.BLACK);
				r.setFill(Color.WHITE);
				r.relocate(i * width, j * width);
				grid.getChildren().add(r);
			}
		}
	}
	
	public Group getGrid() {
		return grid;
	}
}
