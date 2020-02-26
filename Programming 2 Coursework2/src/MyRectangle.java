import javafx.scene.shape.Rectangle;

public class MyRectangle extends Rectangle {
	
	private int cellId;
	private String cageId;
	private String value;
	private String col;
	private String row;
	
	
	public MyRectangle(double width, double height) {
		super(width, height);
		cellId = 0;
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
}