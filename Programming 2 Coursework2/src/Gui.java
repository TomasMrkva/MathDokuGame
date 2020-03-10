import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Optional;
import java.util.Random;
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
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Gui {
	
	private static GridConstructor grid;
	protected static boolean mistakes;
	protected static Button redo;
	protected static Button undo;
	protected static Button hint;
	protected static Button solve;
	private static Label label = new Label("Grid has not been completed!");
	
	public Gui(GridConstructor grid) {
		Gui.grid = grid;
	}
	
	public static void setText(String text) {
		label.setText(text);
	}
	
	public static void setGrid(GridConstructor g) {
		grid = g;
	}
	
	public static GridConstructor getGrid() {
		return grid;
	}
	
	public VBox numbers(int N) {
		VBox numbers = new VBox(5);
//		numbers.setStyle("-fx-border-color: blue");
		for(int i=1; i <= N; i++) {
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
		numbers.setPadding(new Insets(MathDoku.getWidth()*1, 10, MathDoku.getWidth()*1, 10));
		numbers.setAlignment(Pos.CENTER);
		VBox.setVgrow(del, Priority.ALWAYS);
		del.setMaxHeight(Double.MAX_VALUE);
		return numbers;
	}
	
	public VBox menu() {
		Button clear = new Button("Clear");
		solve = new Button("Solve");
		hint = new Button("Hint");
		hint.setPrefWidth(50);
		hintClick(hint);
		solveClick(solve);
		solve.setPrefWidth(50);
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
		menu.getChildren().addAll(hint, solve, clear, undo, redo);
		menu.setAlignment(Pos.CENTER);
		
		menu.setPadding(new Insets(MathDoku.getWidth()*2, 10, MathDoku.getWidth()*2, 10));
		menu.setAlignment(Pos.CENTER);
		VBox.setVgrow(solve, Priority.ALWAYS);
		VBox.setVgrow(clear, Priority.ALWAYS);
		VBox.setVgrow(undo, Priority.ALWAYS);
		VBox.setVgrow(redo, Priority.ALWAYS);
		VBox.setVgrow(hint, Priority.ALWAYS);

		hint.setMaxHeight(Double.MAX_VALUE);
		solve.setMaxHeight(Double.MAX_VALUE);
		clear.setMaxHeight(Double.MAX_VALUE);
		undo.setMaxHeight(Double.MAX_VALUE);
		redo.setMaxHeight(Double.MAX_VALUE);
		return menu;
	}
	
	public VBox initialSetup() {
		Button newGame = new Button("New Game");
		newGame.setPrefSize(100, 40);
		Button preset = new Button("Play");
		preset.setPrefSize(100, 40);
		preset.setOnAction(e -> MathDoku.PresetGrid());
		newGame.setOnMouseClicked(new FileLoaderHandler());
		VBox vbox = new VBox(10);
		vbox.getChildren().addAll(newGame, preset);
		vbox.setAlignment(Pos.CENTER);
		return vbox;
	}
	
	public HBox loadGame() {
		Slider slider = new Slider(12, 20, 16);
		slider.setShowTickMarks(true);
		slider.setShowTickLabels(true);
        slider.setMinorTickCount(1);
        slider.setMajorTickUnit(2);
		slider.setPadding(new Insets(5, 10, 5, 10));
		slider.setPrefWidth(100);
		fontMaker(slider);
		Button loadFile = new Button();
		loadFile.setText("Load a new game");
		CheckBox mistakes = new CheckBox("Show Mistakes");
		mistakerChooser(mistakes);
		loadFile.setOnMouseClicked(new FileLoaderHandler());
		HBox load = new HBox(20);
		load.setPadding(new Insets(5, 10, 5, 10));
		load.getChildren().addAll(loadFile, mistakes, slider);
		load.setAlignment(Pos.CENTER);
		HBox.setHgrow(loadFile, Priority.ALWAYS);
		HBox.setHgrow(slider, Priority.ALWAYS);
		slider.setMaxWidth(400);
		loadFile.setMaxWidth(300);
		return load;
	}
	
	public HBox bottomSide() {
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
					grid.colorAllMistakes();
				} else {
					for(MyRectangle cell : grid.getCells()) {
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
					if(GameEngine.isSolvabale()) {
						hint.setDisable(false);
						solve.setDisable(false);
					}
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
//				System.out.println("works");
				grid.setFont(font);
			}

		});
	}
	
	public void solveClick(Button solveButton) {
		solveButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				for(MyRectangle cell : grid.getCells()) {
					cell.setSolution(0);
				}
				if(GameEngine.solve(grid.getCells(), grid.getCells().size())) {
					for(MyRectangle r : grid.getCells()) {
						grid.displaySolved(r);
					}
				}
				grid.requestFocus();
			}
		});
	}
	
	public void hintClick(Button hintButton) {
		hintButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				ArrayList<MyRectangle> wrongCells = new ArrayList<MyRectangle>();
				for(MyRectangle cell : grid.getCells()) {
					if(cell.getValue() == null || Integer.valueOf(cell.getValue()) != cell.getSolution()) {
						wrongCells.add(cell);
					}
				}
				if(!wrongCells.isEmpty()) {
					MyRectangle r = wrongCells.get(new Random().nextInt(wrongCells.size()));	
					grid.displaySolved(r);
				}
				grid.requestFocus();
			}
		});
	}
	
}
