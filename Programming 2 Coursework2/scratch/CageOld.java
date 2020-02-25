import java.util.ArrayList;
import java.util.Arrays;

import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

public class CageOld extends Shape {

	private ArrayList<MyRectangle> cells;
	private Cage cage;
	
	public CageOld() {
		super();
		this.setStrokeWidth(2);
		this.setStroke(Color.BLACK);
		this.setFill(Color.TOMATO);
	}
	
//	public void add(MyRectangle... r) {
//		this.cells = new ArrayList<MyRectangle>(Arrays.asList(r));
//		
//		for(int i=0; i < this.cells.size()-1; i++) {
//			if(i == 0) 
//				cage = (Cage) Cage.union(cells.get(i), cells.get(i+1));
//			else {
//				cage = (Cage) Cage.union(cage, cells.get(i+1));
//			}
//		}
//		cage.setStrokeWidth(2);
//		cage.setStroke(Color.BLACK);
//		cage.setFill(Color.TOMATO);
//		return cage;
	}
}
