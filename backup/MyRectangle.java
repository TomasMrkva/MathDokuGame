
import javafx.scene.shape.Rectangle;

public class MyRectangle extends Rectangle {
	
	int cellId;
	String cageId;
	String value;
	
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
	
	public String getCage() {
		return cageId;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
}