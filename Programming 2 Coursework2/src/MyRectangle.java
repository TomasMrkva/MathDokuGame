import javafx.scene.shape.Rectangle;

public class MyRectangle extends Rectangle {
	
	private int cellId;
	private String cageId;
	private String value;
	private String col;
	private String row;
	private boolean colRed;
	private boolean rowRed;
	private boolean cageRed;
	private String oldValue;
	private int solution;
	private Cage cage;
	
	public MyRectangle() {}
	
	public MyRectangle(double width, double height) {
		super(width, height);
		solution = 0;
	}
	
	public void setCellId(int cellId){
		this.cellId = cellId;
	}
	
	public int getCellId() {
		return cellId;
	}
	
	public void setCageId(String cageId) {
		this.cageId = cageId;
	}
	
	public String getCageId() {
		return cageId;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setOldValue(String value) {
		oldValue = value;
	}
	
	public String getOldValue() {
		return oldValue;
	}
	
	public void setCol(String col) {
		this.col = col;
	}
	
	public String getCol() {
		return col;
	}
	
	public void setRow(String row) {
		this.row = row;
	}
	
	public void setCage(Cage cage) {
		this.cage = cage;
	}
	
	public Cage getCage() {
		return cage;
	}
	
	public String getRow() {
		return row;
	}

	public void setColRed(boolean value) {
		colRed = value;
	}
	
	public boolean isColRed() {
		return colRed;
	}
	
	public void setRowRed(boolean value) {
		rowRed = value;
	}
	
	public boolean isRowRed() {
		return rowRed;
	}
	
	public void setCageRed(boolean value) {
		cageRed = value;
	}
	
	public boolean isCageRed() {
		return cageRed;
	}
	
	public void setSolution(int solution) {
		this.solution = solution;
	}
	
	public int getSolution() {
		return solution;
	}
	
}