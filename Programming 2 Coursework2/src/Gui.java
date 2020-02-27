import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class Gui {
	
	public GridConstructor grid;
	public static boolean mistakes;
	
	public Gui(GridConstructor grid) {
		this.grid = grid;
		mistakes = false;
	}
	
	public VBox numbers(int N) {
		VBox numbers = new VBox(5);
		
		for(int i=1; i<=N; i++) {
			Button button = new Button();
			button.setText(String.valueOf(i));
			button.setPrefWidth(50);
			numButtonClick(button);
			numbers.getChildren().add(button);
		}
		Button clear = new Button("Del");
		clear.setPrefWidth(50);
		numButtonClick(clear);
		numbers.getChildren().add(clear);
		numbers.setPadding(new Insets(10));
		numbers.setAlignment(Pos.CENTER);
		return numbers;
	}
	
	public VBox menu() {
		Button clear = new Button("Clear");
		clear.setPrefWidth(50);
		Button undo = new Button();
		undo.setText("<-");
		undo.setPrefWidth(50);
		Button redo = new Button();
		redo.setText("->");
		redo.setPrefWidth(50);
		VBox menu = new VBox(5);
		menu.setPadding(new Insets(10));
		menu.getChildren().addAll(clear, undo, redo);
		menu.setAlignment(Pos.CENTER);
		return menu;
	}
	
	public HBox loadGame() {
		Button loadFile = new Button();
		loadFile.setText("Load from a file");
		Button loadText = new  Button();
		loadText.setText("Load from a text");
		CheckBox mistakes = new CheckBox("Show Mistakes");
		mistakerChooser(mistakes);
		HBox load = new HBox(20);
		load.setPadding(new Insets(10));
		load.getChildren().addAll(loadFile,mistakes,loadText);
		load.setAlignment(Pos.CENTER);
		return load;
	}
	
	public void numButtonClick(Button b) {
		b.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String number = b.getText();
				try {
			        Integer.parseInt(number);
					grid.displayNumber(number);
			    }
			    catch(NumberFormatException e) {
			        grid.displayNumber(null);
			    }
				grid.requestFocus();
			}
		});
	}
	
	public void mistakerChooser(CheckBox box) {
		box.selectedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				grid.requestFocus();
				if(newValue == true) {
					mistakes = true;
					GridConstructor.checkAllMistakes();
				} else {
//					System.out.println("dont show mistakes anymore");
					for(MyRectangle cell : GridConstructor.getCells()) {
						cell.setFill(Color.TRANSPARENT);
						cell.setRowRed(false);
						cell.setColRed(false);
						cell.setCageRed(false);
					}
					mistakes = false;
				}
			}
		});
		
	}
}
