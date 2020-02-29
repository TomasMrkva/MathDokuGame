import java.util.EmptyStackException;
import java.util.Optional;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Gui {
	
	private GridConstructor grid;
	protected static boolean mistakes;
	protected static Button redo;
	protected static Button undo;
	private static Label label;
	
	public Gui(GridConstructor grid) {
		this.grid = grid;
	}
	
	public static void setText(String text) {
		label.setText(text);
	}
	
	public VBox numbers(int N) {
		VBox numbers = new VBox(5);
//		FlowPane numbers = new FlowPane(0,5);
//		numbers.setOrientation(Orientation.VERTICAL);
//		numbers.setStyle("-fx-border-color: blue");

		for(int i=1; i<=N; i++) {
			Button button = new Button();
			button.setText(String.valueOf(i));
			button.setPrefWidth(50);
			button.setPrefHeight(25);
			numButtonClick(button);
			HBox hBox = new HBox();
			hBox.getChildren().add(button);
			
			numbers.getChildren().add(button);
			VBox.setVgrow(button, Priority.ALWAYS);
			button.setMaxHeight(Double.MAX_VALUE);
		}
		Button del = new Button("Del");
		del.setPrefWidth(50);
		numButtonClick(del);
		numbers.getChildren().add(del);
		numbers.setPadding(new Insets(MathDoku.getWidth()*1.5, 10, MathDoku.getWidth()*1.5, 10));
		numbers.setAlignment(Pos.CENTER);
		VBox.setVgrow(del, Priority.ALWAYS);
		del.setMaxHeight(Double.MAX_VALUE);
		return numbers;
	}
	
	public VBox menu() {
//		Button font = new Button("Font");
//		font.setPrefWidth(50);
//		fontMaker(font);
		
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
		
		menu.setPadding(new Insets(MathDoku.getWidth()*2.5, 10, MathDoku.getWidth()*2.5, 10));
		menu.setAlignment(Pos.CENTER);
		VBox.setVgrow(clear, Priority.ALWAYS);
		VBox.setVgrow(undo, Priority.ALWAYS);
		VBox.setVgrow(redo, Priority.ALWAYS);
		
		clear.setMaxHeight(Double.MAX_VALUE);
		undo.setMaxHeight(Double.MAX_VALUE);
		redo.setMaxHeight(Double.MAX_VALUE);
		return menu;
	}
	
	public HBox loadGame() {
		
		Slider slider = new Slider(12, 20, 16);
		slider.setShowTickMarks(true);
		slider.setShowTickLabels(true);
        slider.setMinorTickCount(1);
        slider.setMajorTickUnit(2);
//		slider.setSnapToTicks(true);
		slider.setPadding(new Insets(5, 10, 5, 10));
		slider.setPrefWidth(100);
		fontMaker(slider);
		
		Button loadFile = new Button();
		loadFile.setText("Load from a file");
		Button loadText = new  Button();
		loadText.setText("Load from a text");
		CheckBox mistakes = new CheckBox("Show Mistakes");
		mistakerChooser(mistakes);
		loadText.setPrefWidth(60);
		loadFile.setPrefWidth(60);
		HBox load = new HBox(20);
		load.setPadding(new Insets(5, 10, 5, 10));
		load.getChildren().addAll(loadFile, loadText, mistakes, slider);
		load.setAlignment(Pos.CENTER);
		HBox.setHgrow(loadFile, Priority.ALWAYS);
		HBox.setHgrow(loadText, Priority.ALWAYS);
		HBox.setHgrow(slider, Priority.ALWAYS);
		
		slider.setMaxWidth(400);
		loadText.setMaxWidth(200);
		loadFile.setMaxWidth(200);
	 

		return load;
	}
	
	public HBox bottomSide() {
		label = new Label("Grid has not been completed!");
		label.setPadding(new Insets(10));
		label.setAlignment(Pos.CENTER);
		label.setPrefWidth(300);
		HBox hbox = new HBox();
		hbox.getChildren().addAll(label);
		hbox.setAlignment(Pos.CENTER);
		HBox.setHgrow(label, Priority.ALWAYS);
		label.setMaxWidth(Double.MAX_VALUE);
		label.setMaxHeight(Double.MAX_VALUE);
		return hbox;
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
	
	public void fontMaker(Slider slider) {
		slider.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				Font font = new Font("Arial", newValue.intValue());
				grid.setFont(font);
				System.out.println("font changed");

			}

		});
//		slider.setOnAction(new EventHandler<ActionEvent>() {
//
//			@Override
//			public void handle(ActionEvent event) {
//				Font font = new Font("Arial", 25);
//				
//				grid.setFont(font);
//				System.out.println("font changed");
//			}
//		});
	}
	
}
