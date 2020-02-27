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
	
	public MyRectangle(double width, double height) {
		super(width, height);
		cellId = 0;
		colRed = false;
		rowRed = false;
		cageRed = false;
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
	
	public void setCol(String col) {
		this.col = col;
	}
	
	public String getCol() {
		return col;
	}
	
	public void setRow(String row) {
		this.row = row;
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
	
}