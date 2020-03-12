import java.util.ArrayList;
import java.util.EmptyStackException;

import javafx.animation.PauseTransition;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class GridConstructor {

	private Group grid = new Group();	//The whole grid is a Group of stackpanes
	//List of StackPanes which contain the cells MyRectangle class and labels
	private ArrayList<StackPane> cellsPos = new ArrayList<StackPane>();
	private  ArrayList<Cage> cages = new ArrayList<Cage>();
	public MyRectangle current;	// The pointer the current cell in the grid
	ArrayList<MyRectangle> cells = new ArrayList<MyRectangle>();	//List of the cells, used for making cages
	private MyRectangle[][] matrix;
	private int N;
	
	public static Font font = new Font("Arial", 16);

	/**
	 * Creates the initial grid of stackpanes, but no cages are added
	 * @param N, the number of NxN grid
	 * @param width, the width of each cell
	 */
	public GridConstructor(int N, double width) {
		this.N = N;
		matrix = new MyRectangle[N][N];
		
		int counter = 0;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				MyRectangle cell = new MyRectangle(width, width);	//Create a rectangle
				StackPane cellPos = new StackPane();	// Assign it to a StackPane
				cellPos.relocate(j * width, i * width);	// Reposition the stackpane to the positon of the cell
				cell.setCellId(counter++);	// Set the proper id of the rectangle
				cell.setRow(String.valueOf(i));
				cell.setCol(String.valueOf(j));
				cell.setStrokeWidth(0.25);
				cell.setFill(Color.TRANSPARENT);
				cell.setStroke(Color.BLACK);
				
				Label label = new Label(null);
				label.setMouseTransparent(true);
				label.setFont(font);
				cellPos.getChildren().addAll(cell, label);	//Adds the cell to the stackpane, the same stackpane might contain a label 
				matrix[i][j] = cell;
				cells.add(cell);
				cellsPos.add(cellPos);
				mouseClicked(cellPos);	// Adds an Event handler for each StackPane in the gridLabel label = new Label("");	
			}
		}
	}
	
	/**
	 * Adds the cages to the grid, this method has to be called 1st before displaying the final grid
	 * @param cages the ArrayList of cages, that will be added to the grid
	 */
	public void addCages(ArrayList<Cage> cages) {
		for(Cage c : cages) {
			grid.getChildren().add(c.getCage());
			this.cages.add(c);
		}
//		System.err.println(this.cages.size());
	}
	
	/**
	 * Makes the labels of each cage
	 * this method has to be called 3rd
	 */
	public void makeLabels() {
		// loops through all the cages and stackpanes of the cells 
		for(int i = 0; i < cages.size(); i++) {			
			for(StackPane s : cellsPos) {
				System.out.println(s.getChildren().size());
				// when the rectangle of the stackpane matches the first rectangle of the cage, make a label for it
				if((s.getChildren().get(0) == cages.get(i).getCells().get(0)) &&  (s.getChildren().size() < 3)) {
					System.err.println("works");
					Label label = new Label(" " + cages.get(i).getId());	// makes a label with the operation ID of the cage
					label.setMouseTransparent(true);	// label wont register clicks
					label.setFont(font);
					s.getChildren().set(s.getChildren().size()-1, label);
					StackPane.setAlignment(label, Pos.TOP_LEFT);	// the label goes to top left
				}

			}
		}
	}
	
	/**
	 * Creates a border around the whole grid, this method has to be called 4th
	 * @param widthOfCell
	 * @param numberOfCells
	 * @param strokeWidth
	 * @param color
	 */
	public void makeBorder(double widthOfCell, int numberOfCells, double strokeWidth, Color color) {
		Rectangle border = new Rectangle(widthOfCell * numberOfCells, widthOfCell * numberOfCells);
		border.setStrokeType(StrokeType.OUTSIDE);
		border.relocate(0, 0);
		border.setStroke(Color.TOMATO);
		border.setFill(Color.TRANSPARENT);
		border.setStrokeWidth(strokeWidth);
		border.setMouseTransparent(true);
		grid.getChildren().add(border);
	}
	
	/**
	 * Returns the final grid with cages and updated labels, has to be called at the very end,
	 * when cages and labels have been created
	 * this method has to be called the last
	 * @return the game Grid
	 */
	public Group getGrid() {
		for(StackPane s : cellsPos) {
			grid.getChildren().add(s);
		}
		current = cells.get(0);
		current.setStrokeType(StrokeType.INSIDE);
		current.setStroke(Color.rgb(0, 0, 128, 0.4));
		current.setStrokeWidth(MathDoku.getWidth());
		keyTyped();
		return grid;
	}
	
	/**
	 * Gives the cell in the rectangle at the specified position
	 * @param pos the number of the requested cell
	 * @return MyRectangle the requested cell MyRectangle object
	 */
	public MyRectangle getCell(int pos) {
		pos--;
		return cells.get(pos);
	}
	
	public ArrayList<MyRectangle> getCells(){
		return cells;
	}
	
	/**
	 * EventHandler for each of the StackPanes, gets called when each stackpane is being created
	 * @param cellStackPane 
	 * @see #makeGrid(int, int)
	 */
	public void mouseClicked(StackPane cellStackPane) {
		cellStackPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
//				System.out.println(event.get
			// When the current is not empty, set current to previous state, transparent and stroke(0.25)
				if (current != null) {
					current.setStroke(Color.BLACK);
					current.setStrokeWidth(0.25);
				}
//				System.out.println("Click Pos: " + (((MyRectangle)event.getTarget()).getCellId())
//						+ "\tValue: " + ((MyRectangle) event.getTarget()).getValue() 
//						+ "\tOld Value: " + ((MyRectangle) event.getTarget()).getOldValue() 
//						+ "\tCageID: " +((MyRectangle)event.getTarget()).getCageId() 
//						+ "\tRow: " +((MyRectangle)event.getTarget()).getRow()
//						+"\tCol: " + ((MyRectangle)event.getTarget()).getCol()
//						+"\tCageID: "+((MyRectangle)event.getTarget()).isCageRed()
//						+"\tRow red: " + ((MyRectangle) event.getTarget()).isRowRed()
//						+"\tCol red: " + ((MyRectangle) event.getTarget()).isColRed()
//						+"\tCage: " + ((MyRectangle) event.getTarget()).getCage().getId()
//						+"\tSolValue: "+ ((MyRectangle) event.getTarget()).getSolution());
				// Save the cell as current 
				current = (MyRectangle) event.getTarget();
				printInfo();
				current.setStrokeType(StrokeType.INSIDE);
				current.setStroke(Color.rgb(0, 0, 128, 0.4));
//				current.setStroke(Color.rgb(80, 175, 255, 0.4));	//teal
				current.setStrokeWidth(MathDoku.getWidth());

				// When user clicks on the grid, the focus is requested for the grid
				requestFocus();
				// Calls an eventHandler for the key input
//				keyTyped();
			}
		});
	}
	
	public void printInfo() {
		System.out.println("Click Pos: " + current.getCellId() + "\tValue: " + current.getValue() 
		+ "\tOld Value: " + current.getOldValue() + "\tCageID: " + current.getCageId() 
		+ "\tRow: " + current.getRow() +"\tCol: " + current.getCol() + "\tCageID: " + current.getCageId()
		+"\tCage red: " + current .isCageRed() + "    Row red: " + current.isRowRed()
		+"   Col red: " + current.isColRed() + "\tSolValue: "+ current.getSolution());
	}
	
	public void moveWithKeys(String move) {
		if (current != null) {
			current.setStroke(Color.BLACK);
			current.setStrokeWidth(0.25);
		}
		switch (move) {
		case "up":
			if(current.getCellId() < N) {
				if(current.getCellId() == 0) current = cells.get(cells.size()-1);
				else current = cells.get((N*N-1)-(N-current.getCellId()));
			} else 
				current = cells.get(current.getCellId()-N);
			break;
			
		case "left":
			if(current.getCellId() > 0)
				current = cells.get(current.getCellId()-1);
			else
				current = cells.get((N*N)-1);
			break;
		case "down":
			if(current.getCellId() < ((N*N)-N))
				current = cells.get(current.getCellId()+N);
			else {
				if (current.getCellId() == (N*N)-1 ) current = cells.get(0);
				else current = cells.get(N+1-( (N*N)-current.getCellId() ));
			} break;
		case "right":
			if(current.getCellId() < (N*N-1))
				current = cells.get(current.getCellId()+1);
			else
				current = cells.get(0);
			break;
		}
		printInfo();
		current.setStrokeType(StrokeType.INSIDE);
		current.setStroke(Color.rgb(0, 0, 128, 0.4));
		current.setStrokeWidth(MathDoku.getWidth());
		requestFocus();
	}
	
	/**
	 * Makes the grid focus of the screen
	 */
	public void requestFocus() {
		grid.requestFocus();
	}
	
	/**
	 * EventHandler the grid, which is taking the key presses of the user
	 * Takes only digits
	 */
	public void keyTyped() {
		final KeyCombination redo = new KeyCodeCombination(KeyCode.Y, KeyCombination.SHORTCUT_DOWN);
		final KeyCombination undo = new KeyCodeCombination(KeyCode.Z, KeyCombination.SHORTCUT_DOWN);
		grid.setOnKeyPressed(e -> {
			if(undo.match(e)) {
				System.out.println("undo");
				try {
					Gui.undoAction();
				} catch (EmptyStackException emptyStack) {
					System.err.println("Undo stack is empty");
				}
			} else if(redo.match(e)) {
				System.out.println("redo");
				try {
					Gui.redoAction();
				} catch (EmptyStackException emptyStack) {
					System.err.println("Redo stack is empty");
				}
			}
		});
		
		grid.setOnKeyTyped(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent event) {
				try {
					if(Character.isDigit(event.getCharacter().toCharArray()[0])) {
						int enteredNum = Integer.valueOf(event.getCharacter());
						if(enteredNum <= N && enteredNum > 0) {
							String number = event.getCharacter().toString();
							displayNumber(number);
						}
					} else {
						switch (event.getCharacter()) {
						case "w": 
						case "i":
//							System.out.println("up");
							moveWithKeys("up");
							break;
						case "a":
						case "j":
//							System.out.println("left");
							moveWithKeys("left");
							break;
						case "s":
						case "k":
//							System.out.println("down");
							moveWithKeys("down");
							break;
						case "d":
						case "l":
//							System.out.println("right");
							moveWithKeys("right");
							break;
//						default:
//							System.out.println(event.getCharacter());
						}
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					if(event.getCharacter().equals("")) {
						System.err.println("User entered a backspace");
						displayNumber(null);
					}
				}
			}
		});
	}
	
	public void unregiSterKeys() {
		grid.setOnKeyPressed(null);
		grid.setOnKeyTyped(null);
	}
	
	public boolean checkWin() {
		Cage[] arr = cages.toArray(new Cage[cages.size()]);
		if(GameEngine.checkAllCols(matrix) && GameEngine.checkAllRows(matrix) && GameEngine.checkAllCages(false, arr)) {
			Gui.setText("Congratulations, you solved the game !!!");
			Gui.hint.setDisable(true);
			Gui.solve.setDisable(true);
			return true;
		}
		else 
			return false;
	}
	
	/**
	 * Displays the number on the grid, that user inputed
	 * @param number, takes the string value of the key input
	 */
	public void displayNumber(String number) {
		Gui.undo.setDisable(false);
		Gui.redo.setDisable(true);
		if(GameEngine.isSolvabale()) {
			Gui.hint.setDisable(false);
			Gui.solve.setDisable(false);			
		}
		for(StackPane cellPos : cellsPos) {
			// finds the cell that user clicked on last
			if (((MyRectangle)cellPos.getChildren().get(0)).getCellId() == current.getCellId()) {
				StackOperations.stackRedo.clear();
				
				MyRectangle previous = new MyRectangle();
				previous.setValue(number);
				previous.setOldValue(((MyRectangle) cellPos.getChildren().get(0)).getValue());
				previous.setCellId(((MyRectangle) cellPos.getChildren().get(0)).getCellId());
				StackOperations.push(previous);
//				System.out.println("Pushed on stack");
				
				Label label = new Label(number);
				label.setMouseTransparent(true);	// the label is not going to register mouse clicks
				label.setFont(font);
				// when the gridpane has more than two children (rectangle and the initial label("")), remove the last number
				if(cellPos.getChildren().size() > 2) {
					cellPos.getChildren().remove(cellPos.getChildren().size()-1);
				}
				String oldValue = ((MyRectangle) cellPos.getChildren().get(0)).getValue();
				((MyRectangle) cellPos.getChildren().get(0)).setOldValue(oldValue);
				// add the label with the current number to the stackpane of the cell
				((MyRectangle) cellPos.getChildren().get(0)).setValue(number);
				cellPos.getChildren().add(label);
				if(Gui.mistakes == true) {
					checkCurrentMistakes(current);
					colorAllMistakes();
				}
				if(GameEngine.isFinished(cells)) {
					if(checkWin() == true) {
//						WinAnimation.playAnimation((Pane)((BorderPane) MathDoku.getScene().getRoot()).getCenter());	
						WinAnimation.playAnimation(MathDoku.pRoot);
					}
				}
				break;
			}
		}
	}
	
	/**
	 * Method used for displaying solutions
	 * @param 
	 */
	public void displaySolved(MyRectangle r) {
		for(StackPane s : cellsPos) {
			if(((MyRectangle) s.getChildren().get(0)).getCellId() == r.getCellId()) {
				MyRectangle cell = ((MyRectangle) s.getChildren().get(0));
//				StackOperations.stackRedo.clear();
				String number = String.valueOf(cell.getSolution());
				Label label = new Label(number);
				String value = null;
				label.setMouseTransparent(true);
				label.setFont(font);
				label.setStyle("-fx-text-fill: green");
				Gui.hint.setDisable(true);
				Gui.solve.setDisable(true);
				Paint color = cell.getFill();
				if(s.getChildren().size() > 2) {
					value = ((Label) s.getChildren().get(s.getChildren().size()-1)).getText();
					s.getChildren().remove(s.getChildren().size()-1);
				}
				final String originalValue = value;
				s.getChildren().add(label);
				cell.setFill(Color.rgb(0, 255, 0, 0.3));
				
				PauseTransition pause = new PauseTransition(Duration.seconds(1));
				pause.setOnFinished(event -> {
					label.setText(originalValue);
					label.setStyle("-fx-text-fsill: black");
					cell.setFill(color);
					Gui.hint.setDisable(false);
					Gui.solve.setDisable(false);
				}); 
				pause.play();
				break;
			}
		}
	}
	
	public void displayTest(MyRectangle r) {
		for(StackPane s : cellsPos) {
			if(((MyRectangle) s.getChildren().get(0)).getCellId() == r.getCellId()) {
				MyRectangle cell = ((MyRectangle) s.getChildren().get(0));
//				StackOperations.stackRedo.clear();
				String number = String.valueOf(cell.getSolution());
				Label label = new Label(number);
				label.setMouseTransparent(true);
				label.setFont(font);
				if(s.getChildren().size() > 2) {
					s.getChildren().remove(s.getChildren().size()-1);
				}
				s.getChildren().add(label);
				break;
			}
		}
	}
	
	public void updateNumber(MyRectangle cell, boolean undo) {
		String updatedValue;
		
		if (current != null) {
			current.setStroke(Color.BLACK);
			current.setStrokeWidth(0.25);
		}
		for(StackPane cellPos : cellsPos) {
			if (((MyRectangle)cellPos.getChildren().get(0)).getCellId() == cell.getCellId()) {
				current = ((MyRectangle) cellPos.getChildren().get(0));
				if(undo == true) {
					updatedValue = cell.getOldValue();
					if(GameEngine.isSolvabale()) {
						Gui.hint.setDisable(false);						
					}
				}
				else {
					updatedValue = cell.getValue();
				}
				current.setValue(updatedValue);	
				current.setStrokeType(StrokeType.INSIDE);
				current.setStroke(Color.rgb(0, 0, 128, 0.4));
				current.setStrokeWidth(40);
				Label label = new Label(updatedValue);
				label.setFont(font);
				label.setMouseTransparent(true);
				if(cellPos.getChildren().size() > 2) {
					cellPos.getChildren().remove(cellPos.getChildren().size()-1);
				}
				cellPos.getChildren().add(label);
				GameEngine.isFinished(cells);
				if(Gui.mistakes == true) {
					checkCurrentMistakes(current);
					colorAllMistakes();
				}
				break;
			}
		}
	}
	
	public void colorAllMistakes() {
		GameEngine.colorCols(matrix, cells);
		GameEngine.colorRows(matrix, cells);
		GameEngine.colorCages(cells, cages);
	}
	
	public void checkCurrentMistakes(MyRectangle current) {
		GameEngine.checkCage(current);
		GameEngine.checkRow(cells, current);
		GameEngine.checkCol(cells, current);
	}
	
	public void clearBoard() {
		for(StackPane cellPos : cellsPos) {
			if(cellPos.getChildren().size() > 2) {
				cellPos.getChildren().remove(cellPos.getChildren().size()-1);
			}
			MyRectangle index = ((MyRectangle) cellPos.getChildren().get(0));
			if(index.getValue() != null) {
				((MyRectangle) cellPos.getChildren().get(0)).setValue(null);
				((MyRectangle) cellPos.getChildren().get(0)).setOldValue(null);
//				((MyRectangle) cellPos.getChildren().get(0)).setSolution(0);
				if(Gui.mistakes == true) {
					checkCurrentMistakes(index);				
				}
			}
		}
		Gui.setText("Grid has not been completed!");
	}
	
	public void setFont(Font font) {
		GridConstructor.font = font;
		for(StackPane cellPos : cellsPos) {
			for(int i = 1; i < cellPos.getChildren().size(); i++) {
				Label label = (Label) cellPos.getChildren().get(i);
				label.setFont(font);
			}
		}
	}
	
}