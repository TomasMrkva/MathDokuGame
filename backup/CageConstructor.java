import java.util.ArrayList;
import java.util.Arrays;

import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

public class Cage {
	
	private ArrayList<MyRectangle> cells;
	private ArrayList<Shape> cagesArray;
	private Shape cage;
	
	public Cage() {
		cagesArray = new ArrayList<Shape>();
		cells = new ArrayList<MyRectangle>();
	}
	
	public void add(String result, MyRectangle... r) {
		
		this.cells = new ArrayList<MyRectangle>(Arrays.asList(r));
		
		for(int i=0; i < this.cells.size()-1; i++) {
			if(i == 0) 
				this.cage = Shape.union(cells.get(i), cells.get(i+1));
			else {
				this.cage = Shape.union(cage, cells.get(i+1));
			}
		}
		cage.setStrokeWidth(3);
		cage.setStroke(Color.BLACK);
		cage.setFill(Color.TRANSPARENT);
		cage.setMouseTransparent(true);
		cagesArray.add(cage);
		cells.clear();
	}
	
//	public Shape getCage() {
//		return cage;
//	}
	
	public ArrayList<Shape> getCages() {
		return cagesArray;
	}
}
