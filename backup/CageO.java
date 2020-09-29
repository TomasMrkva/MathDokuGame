import java.util.ArrayList;

import javafx.scene.shape.Shape;

public class CageOld extends Shape {
	
	public ArrayList<MyRectangle> cells;
	public String result;
	
	public CageOld(String result) {
		super();
		cells = new ArrayList<MyRectangle>();
		this.result = result;
	}
	
	public void addCells(MyRectangle... r) {
		for(MyRectangle cell : r) {
			cells.add(cell);		
		}
	}
	
	public String getResult() {
		return result;
	}
	
	
}
