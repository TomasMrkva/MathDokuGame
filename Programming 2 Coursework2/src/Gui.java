import java.util.EmptyStackException;
import java.util.Optional;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class Gui {
	
	private GridConstructor grid;
	protected static boolean mistakes;
	protected static Button redo;
	protected static Button undo;
	
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
		Button del = new Button("Del");
		del.setPrefWidth(50);
		numButtonClick(del);
		numbers.getChildren().add(del);
		numbers.setPadding(new Insets(10));
		numbers.setAlignment(Pos.CENTER);
		return numbers;
	}
	
	public VBox menu() {
		Button clear = new Button("Clear");
		clearClick(clear);
		clear.setPrefWidth(50);
		undo = new Button();
		undo.setText("<-");
		undo.setPrefWidth(50);
		undoClick(undo);
		undo.setDisable(true);
		
		redo = new Button();
		redo.setText("->");
		redo.setPrefWidth(50);
		redoClick(redo);
		redo.setDisable(true);
		
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
	
	//GUI Handling methods
	
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
					grid.checkAllMistakes();
				} else {
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
	
	public void clearClick(Button clear) {
		clear.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to clear the whole grid?\n"
						+ "Your previous steps will be removed!");
				Optional<ButtonType> result = alert.showAndWait();
				if (result.isPresent() && result.get() == ButtonType.OK) {
					grid.clearBoard();
					StackOperations.stackUndo.clear();
					StackOperations.stackRedo.clear();
					redo.setDisable(true);
					undo.setDisable(true);
				}
				grid.requestFocus();
			}
		});
	}
	
	public void undoClick(Button undoButton) {
		undoButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				MyRectangle previous = StackOperations.undo();
				grid.updateNumber(previous, true);
				try {
					StackOperations.stackUndo.peek();
					redo.setDisable(false);
				} catch (EmptyStackException e) {
					System.err.println("Undo stack is empty");
					undo.setDisable(true);
				}
				grid.requestFocus();
			}
		});
	}
	
	public void redoClick(Button redoButton) {
		redoButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				MyRectangle next = StackOperations.redo();
				grid.updateNumber(next, false);
				try {
					StackOperations.stackRedo.peek();
					undo.setDisable(false);
				} catch (EmptyStackException e) {
					System.err.println("Redo stack is empty");
					redo.setDisable(true);
				}
				grid.requestFocus();
			}
		});
	}
	
}
