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
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Gui {
	private static GridConstructor grid;
	protected static boolean mistakes;
	protected static Button redo;
	protected static Button undo;
	protected static Button hint;
	protected static Button solve;
	protected static Label label; 
	protected static Slider slider;
//	private static int N=0;
//	private static int difficulty=0;
	
	public Gui(GridConstructor grid) {
		Gui.grid = grid;
		Gui.label = new Label("Grid has not been completed!");
		mistakes = false;
	}
	
	public static void setText(String text) {
		label.setText(text);
	}
	
	public static void setFont(Font font) {
		label.setFont(font);
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
			this.numButtonClick(button);
			HBox hBox = new HBox();
			hBox.getChildren().add(button);
			numbers.getChildren().add(button);
			VBox.setVgrow(button, Priority.ALWAYS);
			button.setMaxHeight(Double.MAX_VALUE);
		}
		Button del = new Button("Del");
		del.setPrefWidth(50);
		this.numButtonClick(del);
		numbers.getChildren().add(del);
		numbers.setPadding(new Insets(MathDoku.width, 10, MathDoku.width, 10));
		numbers.setAlignment(Pos.CENTER);
		VBox.setVgrow(del, Priority.ALWAYS);
		del.setMaxHeight(Double.MAX_VALUE);
		return numbers;
	}
	
//	public VBox initialSetup() {
//		Label label = new Label("Welcome to MathDoku");
//		label.setFont(new Font("Helvetica", 20));
//		label.setPadding(new Insets(0, 0, 15, 0));
//		Button newGame = new Button("New Game");
////		newGame.getStyleClass().add("BUTTON_DSI");
//		newGame.setPrefSize(100, 30);
//		Button preset = new Button("Play");
//		preset.setPrefSize(100, 30);
//		preset.setOnAction(e -> MathDoku.createPreset());
//		newGame.setOnMouseClicked(new FileLoaderHandler());
//		Button randomGame = new Button("Random Game");
//		randomGame.setPrefSize(100, 30);
//		VBox vbox = new VBox(10);
//		randomGame.setOnAction(e -> Gui.randomGameMenu());
//		vbox.getChildren().addAll(label,newGame, preset, randomGame);
//		vbox.setAlignment(Pos.CENTER);
//		preset.setDefaultButton(true);
//		return vbox;
//	}
	
	public VBox menu() {
		Button clear = new Button("Clear");
		solve = new Button("Solve");
		hint = new Button("Hint");
		hint.setPrefWidth(50);
		hintClick(hint);
		solveClick(solve);
		solve.setPrefWidth(50);
		this.clearClick(clear);
		clear.setPrefWidth(50);
		undo = new Button();
		undo.setText("<-");
		undo.setPrefWidth(50);
		undo.setOnAction(e -> Gui.undoAction());
		undo.setDisable(true);
		
		redo = new Button();
		redo.setText("->");
		redo.setPrefWidth(50);
		redo.setOnAction(e -> Gui.redoAction());
		redo.setDisable(true);
		
		VBox menu = new VBox(5);
		menu.setPadding(new Insets(10));
		menu.getChildren().addAll(hint, solve, clear, undo, redo);
		menu.setAlignment(Pos.CENTER);
		
		menu.setPadding(new Insets(MathDoku.width*2, 10, MathDoku.width*2, 10));
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
	
	public static void randomGameMenu() {
		Button b = new Button("Cancel");
		Button submit = new Button("Submit");
		submit.setDefaultButton(true);
		
		Stage newWindow = new Stage();
		Label label = new Label("Customize your game");
		label.setFont(new Font("Helvetica", 20));
		
		newWindow.setTitle("Random game");
		newWindow.setMinHeight(150);
		newWindow.setMinWidth(400);
		newWindow.initModality(Modality.APPLICATION_MODAL);
		
		VBox vBox = new VBox(20);
		HBox hBox = new HBox(10);
		hBox.setAlignment(Pos.CENTER);
		vBox.setAlignment(Pos.CENTER);
		
		ComboBox<String> cBox = new ComboBox<String>();
		cBox.getItems().addAll("2x2", "3x3", "4x4", "5x5", "6x6", "7x7", "8x8");
		cBox.setValue("2x2");
		
		ComboBox<String> dBox = new ComboBox<String>();
		dBox.getItems().addAll("Easy", "Medium", "Hard");
		dBox.setValue("Easy");
		
		submit.setOnAction(e -> {
			int N = 0;
			int difficulty = 0;
			switch (cBox.getValue()) {
				case "2x2": N=2; break;
				case "3x3": N=3; break;
				case "4x4": N=4; break;
				case "5x5": N=5; break;
				case "6x6": N=6; break;
				case "7x7": N=7; break;
				case "8x8": N=8; break;
			}
			switch (dBox.getValue()) {
				case "Easy": difficulty = 1; break;
				case "Medium": difficulty = 2; break;
				case "Hard": difficulty = 3; break;
			}
			System.out.println("Size:" + N);
			System.out.println("Difficulty" + difficulty);
			newWindow.close();
			RandomGame randomGame = new RandomGame(N, difficulty);
			randomGame.createRandomGame();
		});
		
		b.setOnAction(e -> newWindow.close());
		
		hBox.getChildren().addAll(cBox, dBox, b, submit);
		vBox.getChildren().addAll(label, hBox); 
		Scene scene = new Scene(vBox);
		newWindow.setScene(scene);
		newWindow.show();
	}
	
	public HBox loadGame() {
		Slider slider = new Slider(12, 20, GridConstructor.font.getSize());
		slider.setShowTickMarks(true);
		slider.setShowTickLabels(true);
        slider.setMinorTickCount(1);
        slider.setMajorTickUnit(2);
		slider.setPadding(new Insets(5, 10, 5, 10));
		slider.setPrefWidth(100);
		this.fontMaker(slider);
		Button loadFile = new Button();
		loadFile.setText("Load a new game");
		CheckBox mistakes = new CheckBox("Show Mistakes");
		mistakerChooser(mistakes);
		loadFile.setOnMouseClicked(new FileLoaderHandler());
		HBox loadBox = new HBox(20);
		loadBox.setPadding(new Insets(5, 10, 5, 10));
		loadBox.getChildren().addAll(loadFile, mistakes, slider);
		loadBox.setAlignment(Pos.CENTER);
		HBox.setHgrow(loadFile, Priority.ALWAYS);
		HBox.setHgrow(slider, Priority.ALWAYS);
		slider.setMaxWidth(400);
		loadFile.setMaxWidth(300);
		return loadBox;
	}
	
	public HBox botomPanel() {
		label.setPadding(new Insets(10));
		label.setAlignment(Pos.CENTER);
		label.setPrefWidth(300);
		HBox hbox = new HBox();
		hbox.getChildren().addAll(label);
		hbox.setAlignment(Pos.CENTER);
		HBox.setHgrow(label, Priority.ALWAYS);
		label.setMaxWidth(Double.MAX_VALUE);
		label.setMaxHeight(Double.MAX_VALUE);
//		label.setFont(GridConstructor.font);
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
	/**
	 * Used for redo with key combinations
	 */
	public static void redoAction() {
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
	
	/**
	 * Used for undo with key combinations
	 */
	public static void undoAction() {
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
	
	public void fontMaker(Slider slider) {
		slider.valueProperty().addListener(new ChangeListener<Number>() {
			
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				Font font = new Font("Arial", newValue.intValue());
				label.setFont(font);
				grid.setFont(font);
				grid.requestFocus();
			}

		});
	}
	
	public void solveClick(Button solveButton) {
		solveButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
//				for(MyRectangle cell : grid.getCells()) {
//					cell.setSolution(0);
//				}
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
