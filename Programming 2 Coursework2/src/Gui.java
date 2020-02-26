import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Gui {
	
	public GridConstructor grid;
	
	public Gui(GridConstructor grid) {
		this.grid = grid;
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
		numbers.setPadding(new Insets(5));
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
		menu.setPadding(new Insets(5));
		menu.getChildren().addAll(clear, undo, redo);
		menu.setAlignment(Pos.CENTER);
		return menu;
	}
	
	public HBox loadGame() {
		Button loadFile = new Button();
		loadFile.setText("Load from a file");
		Button loadText = new  Button();
		loadText.setText("Load from a text");
		HBox load = new HBox(5);
		load.setPadding(new Insets(5));
		load.getChildren().addAll(loadFile,loadText);
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
}
