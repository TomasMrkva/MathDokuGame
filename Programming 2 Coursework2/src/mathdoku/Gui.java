package mathdoku;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Optional;
import java.util.Random;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
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
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;

public class Gui {
	private static GridConstructor grid;
	protected static boolean mistakes;
	protected static Button redo;
	protected static Button undo;
	protected static Button hint;
	protected static Button solve;
	protected static Label label; 
	protected static Slider slider;
	
	public Gui(GridConstructor grid) {
		Gui.grid = grid;
		Gui.label = new Label("Grid has not been completed!");
		Gui.label.setFont(GridConstructor.font);
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
	
	public static void randomGameMenu() {
		Button b = new Button("Cancel");
		b.setPrefWidth(70);

		Button submit = new Button("Submit");
		submit.setDefaultButton(true);
		submit.setPrefWidth(70);
		
		Stage newWindow = new Stage();
		Label label = new Label("Customize your game");
		label.setFont(new Font("Helvetica", 20));
		label.setPadding(new Insets(20, 0, 0, 0));
		
		newWindow.setTitle("Random game");
		newWindow.setMinHeight(170);
		newWindow.setMinWidth(400);
		newWindow.setMaxHeight(170);
		newWindow.setMaxWidth(400);

		newWindow.initModality(Modality.APPLICATION_MODAL);
		
		VBox vBox = new VBox(10);
		HBox hBox = new HBox(10);
		hBox.setAlignment(Pos.CENTER);
		vBox.setAlignment(Pos.CENTER);
		
		CheckBox box = new CheckBox("Unique");
		
		ComboBox<String> gridSize = new ComboBox<String>();
		gridSize.getItems().addAll("2x2", "3x3", "4x4", "5x5", "6x6", "7x7", "8x8");
		gridSize.setValue("2x2");
		gridSize.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(newValue.equals("8x8") && box.isSelected()) {
					showErrorMessage();
					submit.setDisable(true);
				} else {
					submit.setDisable(false);
				}
			}
		});
		box.selectedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue && gridSize.getValue().equals("8x8")) {
					showErrorMessage();
					submit.setDisable(true);
				} else {
					submit.setDisable(false);
				}
			}
		});
		
		ComboBox<String> gameDifficulty = new ComboBox<String>();
		gameDifficulty.getItems().addAll("Easy", "Medium", "Hard");
		gameDifficulty.setValue("Easy");
		
		submit.setOnAction(e -> {
			int N = 0;
			int difficulty = 0;
			switch (gridSize.getValue()) {
				case "2x2": N=2; break;
				case "3x3": N=3; break;
				case "4x4": N=4; break;
				case "5x5": N=5; break;
				case "6x6": N=6; break;
				case "7x7": N=7; break;
				case "8x8": N=8; break;
			}
			switch (gameDifficulty.getValue()) {
				case "Easy": difficulty = 4; break;
				case "Medium": difficulty = 5; break;
				case "Hard": difficulty = 7; break;
			}
			System.out.println("Size: " + gridSize.getValue());
			System.out.println("Difficulty: " + gameDifficulty.getValue());
			newWindow.close();
			RandomGame randomGame = new RandomGame(N, difficulty, box.isSelected());
			ProgressIndicator pi = new ProgressIndicator();
			pi.setMaxSize(40, 40);
			
			Alert generateAlert = new Alert(AlertType.NONE);
			generateAlert.setTitle("Generating a new Game");
			generateAlert.setHeaderText("Please wait,  the game is being generated...");
			generateAlert.setContentText("To forcequit, doubleclick on this window");
			generateAlert.setGraphic(new Button("Cancel"));
			generateAlert.getDialogPane().setOnMouseClicked(event -> {
				if(event.getButton().equals(MouseButton.PRIMARY)){
		            if(event.getClickCount() == 2){
		            	Platform.exit();
		            	System.exit(0);
		            }
		        }
			});
			generateAlert.setGraphic(pi);
			generateAlert.show();
			
			Task<Void> task = new Task<Void>() {
				@Override
				protected Void call() {
					randomGame.generateRandomGame();
					return null;
				}
			};
			task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
				@Override
				public void handle(WorkerStateEvent event) {

					generateAlert.setResult(ButtonType.CANCEL);
					generateAlert.close();
					randomGame.createGame();
				}
            });
			Thread thread = new Thread(task);
			thread.start();

		});
		
		b.setOnAction(e -> {
			newWindow.close();
			if (grid != null) {
				grid.requestFocus();
			}
		});
		
		HBox subCancel = new HBox(10);
		subCancel.setAlignment(Pos.BOTTOM_RIGHT);
		subCancel.getChildren().addAll(b, submit);
		subCancel.setPadding(new Insets(10, 10, 10, 10));
		
		hBox.getChildren().addAll(gridSize, gameDifficulty, box);
		vBox.getChildren().addAll(label, hBox, subCancel); 
		Scene scene = new Scene(vBox);
		newWindow.setScene(scene);
		newWindow.show();
	}
	
	public static void showErrorMessage() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Warning");
		alert.setHeaderText("Please change your selection");
		alert.setContentText("Sorry, 8x8 grid is too big for a single solution game");
		alert.showAndWait();
	}
	
	public VBox numbers(int N) {
		VBox numbers = new VBox(5);
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
			button.setFocusTraversable(false);
		}
		Button del = new Button("Del");
		del.setPrefWidth(50);
		del.setFocusTraversable(false);
		this.numButtonClick(del);
		numbers.getChildren().add(del);
		numbers.setPadding(new Insets(MathDoku.width, 10, MathDoku.width, 10));
		numbers.setAlignment(Pos.CENTER);
		VBox.setVgrow(del, Priority.ALWAYS);
		del.setMaxHeight(Double.MAX_VALUE);
		return numbers;
	}
	
	public VBox menu() {
		Button clear = new Button("Clear");
		clear.setFocusTraversable(false);
		this.clearClick(clear);
		clear.setPrefWidth(50);
		
		solve = new Button("Solve");
		solve.setFocusTraversable(false);
		solve.setPrefWidth(50);
		solve.setDisable(true);
		this.solveClick(solve);
		
		hint = new Button("Hint");
		hint.setFocusTraversable(false);
		hint.setPrefWidth(50);
		hint.setDisable(true);
		this.hintClick(hint);
		
		undo = new Button();
		undo.setText("<-");
		undo.setPrefWidth(50);
		undo.setOnAction(e -> Gui.undoAction());
		undo.setDisable(true);
		undo.setFocusTraversable(false);
		
		redo = new Button();
		redo.setText("->");
		redo.setPrefWidth(50);
		redo.setOnAction(e -> Gui.redoAction());
		redo.setDisable(true);
		redo.setFocusTraversable(false);
		
		Button config = new Button("Config");
		redo.setPrefWidth(50);
		config.setFocusTraversable(false);
		config.setOnAction(e -> configAction());
		
		VBox menu = new VBox(5);
		menu.setPadding(new Insets(10));
		menu.getChildren().addAll(hint, solve, clear, undo, redo, config);
		menu.setAlignment(Pos.CENTER);
		
//		menu.setPadding(new Insets(MathDoku.width*2, 10, MathDoku.width*2, 10));
		menu.setPadding(new Insets(MathDoku.width*1.8, 10, MathDoku.width*1.8, 10));
		menu.setAlignment(Pos.CENTER);
		VBox.setVgrow(solve, Priority.ALWAYS);
		VBox.setVgrow(clear, Priority.ALWAYS);
		VBox.setVgrow(undo, Priority.ALWAYS);
		VBox.setVgrow(redo, Priority.ALWAYS);
		VBox.setVgrow(hint, Priority.ALWAYS);
		VBox.setVgrow(config, Priority.ALWAYS);


		hint.setMaxHeight(Double.MAX_VALUE);
		solve.setMaxHeight(Double.MAX_VALUE);
		clear.setMaxHeight(Double.MAX_VALUE);
		undo.setMaxHeight(Double.MAX_VALUE);
		redo.setMaxHeight(Double.MAX_VALUE);
		config.setMaxHeight(Double.MAX_VALUE);

		return menu;
	}
	
	public HBox loadGame() {
		Slider slider = new Slider(12, 20, GridConstructor.font.getSize());
		slider.setFocusTraversable(false);
		slider.setShowTickMarks(true);
		slider.setShowTickLabels(true);
        slider.setMinorTickCount(1);
        slider.setMajorTickUnit(2);
		slider.setPadding(new Insets(5, 10, 5, 10));
		slider.setPrefWidth(100);
		this.fontMaker(slider);
		Button loadFile = new Button();

		loadFile.setText("Load a new game");
		loadFile.setFocusTraversable(false);
		CheckBox mistakes = new CheckBox("Show Mistakes");
		mistakes.setFocusTraversable(false);
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
		return hbox;
	}
	
	/*
	 * GUI EventHanxling methods
	 */
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
					if(GameEngine.isSolvable()) {
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
		try {
			MyRectangle next = StackOperations.redo();
			grid.updateNumber(next, false);
			undo.setDisable(false);
			//Checks if the redo stack is empty now
			StackOperations.stackRedo.peek();
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
		try {
			MyRectangle previous = StackOperations.undo();
			grid.updateNumber(previous, true);
			redo.setDisable(false);
			//Checks if the undo stack is empty now
			StackOperations.stackUndo.peek();
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
				boolean isEmpty = true;
				for(MyRectangle cell : grid.getCells()) {
					if(cell.getValue() != null) {
						isEmpty = false;
					}
				}
				if(GameEngine.noOfSolutions > 1 && !isEmpty) {
					int pos = personalizedHint(true).getKey();
					Integer[] solutions = GameEngine.solutionSet.get(pos);
					for(int i = 0; i < solutions.length; i++) {
						MyRectangle r = grid.getCells().get(i);
						r.setSolution(solutions[i]);
					}					
				}
				if(GameEngine.solve(grid.getCells(), "button")) {
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
				
				boolean isEmpty = true;
				for(MyRectangle cell : grid.getCells()) {
					if(cell.getValue() != null) {
						isEmpty = false;
					}
				}
				System.out.println(GameEngine.noOfSolutions);
				if(GameEngine.noOfSolutions > 1 && !isEmpty ) {
					wrongCells = personalizedHint(false).getValue();
				} else {
					for(MyRectangle cell : grid.getCells()) {
						if(cell.getValue() == null || Integer.valueOf(cell.getValue()) != cell.getSolution()) {
							wrongCells.add(cell);
						}
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
	
	public Pair<Integer, ArrayList<MyRectangle> > personalizedHint(boolean solveClick) {
		ArrayList<Integer[]> solutionSet = GameEngine.solutionSet;
		ArrayList<Integer[]> userSolutions = new ArrayList<Integer[]>();
		int[] solutionScore = new int[solutionSet.size()];
		int max = 0;
		int pos = 0;
		
		for(int i = 0; i < grid.getCells().size(); i++) {
			MyRectangle cell = grid.getCells().get(i);
			Integer[] userSol = new Integer[2];
			if(cell.getValue() != null) {
				userSol[0] = i;
				userSol[1] = Integer.parseInt(cell.getValue());
			} else {
				userSol[0] = i;
				userSol[1] = -1;
			}
			userSolutions.add(userSol);
		}
		for(int i = 0; i < solutionSet.size(); i++) {
			for(int j = 0; j < userSolutions.size(); j++) {
				int index = userSolutions.get(j)[0];
				if(userSolutions.get(j)[1] == solutionSet.get(i)[index]) {
					solutionScore[i]++;
				}
			}
		}
		for(int i = 0; i < solutionScore.length; i++) {
			if (solutionScore[i] > max) {
				max = solutionScore[i];
				pos = i;
			}
		}
		if(solveClick) {
			System.out.println("User values are similar to solution #: " + (pos+1));
			return new Pair<Integer, ArrayList<MyRectangle>>(pos, null);
		}
		ArrayList<MyRectangle> wrongCells = new ArrayList<MyRectangle>();
		Integer[] personalizedSols = solutionSet.get(pos);
		if(max > 0){
			for(int i = 0; i < personalizedSols.length; i++) {
				if(userSolutions.get(i)[0] == i  &&  userSolutions.get(i)[1] != personalizedSols[i]) {
					wrongCells.add(grid.getCells().get(i));
				}
			}
		} else {
			for(int i = 0; i < personalizedSols.length; i++ ) {
				MyRectangle cell = grid.getCells().get(i);
				if(cell.getValue() == null || Integer.valueOf(cell.getValue()) != personalizedSols[i]) {
					wrongCells.add(cell);
				}
			}
		}
		System.out.println("User values are similar to solution #: " + (pos+1));
		return new Pair<Integer, ArrayList<MyRectangle>>(pos, wrongCells);
	}
	
	public void configAction() {
		TextArea area = new TextArea();
		area.setEditable(false);
		Stage newWindow = new Stage();
		BorderPane pane = new BorderPane();
		HBox hBox = new HBox();
		
		pane.setPadding(new Insets(10));
		newWindow.setMinHeight(150+(15*grid.getCages().size()));
		newWindow.setMinWidth(250);
		newWindow.setWidth(250);
		
		Button b = new Button("Close");
		b.setOnAction(e -> newWindow.close());
		b.setPrefSize(60, 30);

		System.out.println();
		System.out.println("***Configuration***");
		
		for(int i=0; i < grid.getCages().size(); i++) {
			Cage cage = grid.getCages().get(i);
			System.out.print(cage.getId() + " ");
			area.appendText(cage.getId() + " ");
			for(int j = 0; j < cage.getCells().size(); j++) {
				MyRectangle cell = cage.getCells().get(j);
				if(j == cage.getCells().size()-1) {
					System.out.print((cell.getCellId()+1));
					area.appendText(String.valueOf(cell.getCellId()+1));		
				}
				else {
					System.out.print((cell.getCellId()+1) + ",");
					area.appendText((cell.getCellId()+1) + ",");	
				}
			}
			if(i != grid.getCages().size()-1){
				area.appendText("\n");				
			}
			System.out.println();
		}
		hBox.setAlignment(Pos.BASELINE_RIGHT);
		hBox.setPadding(new Insets(10, 0, 10, 10));
		hBox.getChildren().add(b);
		
		pane.setCenter(area);
		pane.setBottom(hBox);
		BorderPane.setAlignment(b, Pos.BOTTOM_RIGHT);
		newWindow.setScene(new Scene(pane));
		
//		Clipboard clipboard = Clipboard.getSystemClipboard();
//	    ClipboardContent content = new ClipboardContent();
//	    content.putString(area.getText());
//	    Clipboard.getSystemClipboard().setContent(content);
	 
		newWindow.show();
	}
}
