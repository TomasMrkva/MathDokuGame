import java.util.ArrayList;
import java.util.Arrays;

import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

public class Cage {
	
	private ArrayList<MyRectangle> cells;
	private Shape cage;
	private String cageId;
	
	/**
	 * Creates a new cage with a name of the operation
	 * @param result String version of operation
	 * @param r the group of cells that are added
	 */
	public Cage(String result, MyRectangle... r) {
		cells = new ArrayList<MyRectangle>();
		this.cageId = result;
		// Calls the method, to make a cell of the inputed cells
		this.add(r);
	}
	
	/**
	 * Adds adds the cells into the cage and sets the visuals
	 * @param r the Array of MyRectangle objects
	 */
	public void add(MyRectangle... r) {
		
		cells = new ArrayList<MyRectangle>(Arrays.asList(r));
		
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

	}
	public Shape getCage() {
		return this.cage;
	}
	
	//s
	public String getId() {
		return cageId;
	}
	
	public ArrayList<MyRectangle> getCells() {
		return cells;
	}

}
