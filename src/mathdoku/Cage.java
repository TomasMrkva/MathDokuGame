package mathdoku;
import java.util.ArrayList;

import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

public class Cage {
	
	private ArrayList<MyRectangle> cells;
	private Shape cage;
	private String cageId;
	private char opCode;
	private int result;
	
	/**
	 * Creates a new cage with a name of the operation
	 * @param cageId String version of operation and result
	 * @param r the group of cells that are added
	 */
	public Cage(String cageId, MyRectangle... r) {
		cells = new ArrayList<MyRectangle>();
		this.cageId = cageId;
		String opCode = cageId.replaceAll("[0-9]", "");
		if(opCode.isEmpty()) {
			this.opCode = ' ';
		}
		 else
			this.opCode = opCode.charAt(0);
		String number = cageId.replaceAll("\\D+", "");
		this.result = Integer.valueOf(number);
		// Calls the method, to make a cell of the inputed cells
		this.add(r);
	}
	
	/**
	 * Adds adds the cells into the cage and sets the visuals
	 * @param r the Array of MyRectangle objects
	 */
	public void add(MyRectangle... r) {
		for(MyRectangle cell : r) {
			cell.setCageId(cageId);
			cells.add(cell);
		}
		if(r.length > 1) {
			for(int i=0; i < this.cells.size()-1; i++) {
				if(i == 0) 
					this.cage = Shape.union(cells.get(i), cells.get(i+1));
				else {
					this.cage = Shape.union(cage, cells.get(i+1));
				}
			}
			for(MyRectangle cell : r) {
				cell.setCage(this);
			}
		}
		else if(r.length == 1){
			this.cage = Shape.union(cells.get(0), cells.get(0));
			r[0].setCage(this);
		}
		cage.setStrokeWidth(3);
		cage.setStroke(Color.BLACK);
		cage.setFill(Color.TRANSPARENT);
		cage.setMouseTransparent(true);
	}
	
	public Shape getCage() {
		return this.cage;
	}
	
	public String getId() {
		return cageId;
	}
	
	public ArrayList<MyRectangle> getCells() {
		return cells;
	}
	
	public char getOPSymbol() {
		return opCode;
	}
	
	public int getResult() {
		return result;
	}
	
	public boolean isFullSol() {
		for(MyRectangle r : cells) {
			if(r.getSolution() == 0) {
				return false;
			}
		}
		return true;
	}
	
	public boolean isFull() {
		for(MyRectangle r : cells) {
			if(r.getValue() == null) {
				return false;
			}
		}
		return true;
	}

}