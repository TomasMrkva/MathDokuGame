package mathdoku;
import java.util.ArrayList;
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
	private ArrayList<StackPane> cellsPos;
	private  ArrayList<Cage> cages;
	private MyRectangle current;	// The pointer the current cell in the grid
	private ArrayList<MyRectangle> cells;	//List of the cells, used for making cages
	public MyRectangle[][] matrix;
	private int N;	// Size of the grid
	
	public static Font font = new Font("Arial", 16);
	
	/**
	 * Creates the initial grid of stackpanes, but no cages are added
	 * @param N, the number of NxN grid
	 * @param width, the width of each cell
	 */
	public GridConstructor(int N, double width) {
		cellsPos = new ArrayList<StackPane>();
		cages = new ArrayList<Cage>();
		cells = new ArrayList<MyRectangle>();
		matrix = new MyRectangle[N][N];
		this.N = N;
		
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
	}
	
	public ArrayList<Cage> getCages(){
		return cages;
	}
	
	/**
	 * Makes the labels of each cage
	 * this method has to be called 3rd
	 */
	public void makeLabels() {
		// loops through all the cages and stackpanes of the cells 
		for(int i = 0; i < cages.size(); i++) {			
			for(StackPane s : cellsPos) {
				// when the rectangle of the stackpane matches the first rectangle of the cage, make a label for it
				if((s.getChildren().get(0) == cages.get(i).getCells().get(0)) &&  (s.getChildren().size() < 3)) {
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
		current.setStrokeWidth(MathDoku.width);
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
			// When the current is not empty, set current to previous state, transparent and stroke(0.25)
				if (current != null) {
					current.setStroke(Color.BLACK);
					current.setStrokeWidth(0.25);
				}
				// Save the cell as current 
				current = (MyRectangle) event.getTarget();
				current.setStrokeType(StrokeType.INSIDE);
				current.setStroke(Color.rgb(0, 0, 128, 0.4));
//				current.setStroke(Color.rgb(80, 175, 255, 0.4));	//teal
				current.setStrokeWidth(MathDoku.width);
				// When user clicks on the grid, the focus is requested for the grid
				requestFocus();
				printInfo();
			}
		});
	}
	
	public void printInfo() {
		System.out.println("Click Pos: " + current.getCellId() + "\tValue: " + current.getValue() 
		+ "\tOld Value: " + current.getOldValue() + "\tCageID: " + current.getCageId() 
		+ "\tRow: " + current.getRow() +"\tCol: " + current.getCol()
		+"\tCage red: " + current .isCageRed() + "\tRow red: " + current.isRowRed()
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
		current.setStrokeWidth(MathDoku.width);
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
			if(e.getCode() == KeyCode.BACK_SPACE || e.getCode() == KeyCode.DELETE) {
				displayNumber(null);
				e.consume();
			} 
			else if (e.getCode() == KeyCode.UP) {
				moveWithKeys("up");
				grid.requestFocus();
				e.consume();
			} else if (e.getCode() == KeyCode.DOWN) {
				moveWithKeys("down");
				grid.requestFocus();
				e.consume();
			} else if (e.getCode() == KeyCode.LEFT) {
				moveWithKeys("left");
				grid.requestFocus();
				e.consume();
			} else if (e.getCode() == KeyCode.RIGHT) {
				moveWithKeys("right");
				grid.requestFocus();
				e.consume();
			}
			else if(undo.match(e)) {
				Gui.undoAction();
				e.consume();
			} else if(redo.match(e)) {
				Gui.redoAction();
				e.consume();
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
						case "w": case "W": case "i": case "I":
							moveWithKeys("up");
							break;
						case "a": case "A": case "j": case "J":
							moveWithKeys("left");
							break;
						case "s": case "S": case "k": case "K":
							moveWithKeys("down");
							break;
						case "d": case "D": case "l": case "L":
							moveWithKeys("right");
							break;
                        }
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					//Mac/Linux ? version
//					if(event.getCharacter().equals("")) {
//						displayNumber(null);
//					}
				}
			}
		});
	}
	
	public void unregisterKeys() {
		grid.setOnKeyPressed(null);
		grid.setOnKeyTyped(null);
	}
	
	public boolean checkWin() {
		Cage[] arr = cages.toArray(new Cage[cages.size()]);
		if(GameEngine.checkAllCols(matrix) && GameEngine.checkAllRows(matrix) && GameEngine.checkAllCages(false, arr)) {
			Gui.setText("Congratulations, you solved the game !!!");
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
//		Gui.undo.setDisable(false);
//		if(GameEngine.isSolvabale()) {
//			Gui.hint.setDisable(false);
//			Gui.solve.setDisable(false);			
//		}
		for(StackPane cellPos : cellsPos) {
			// finds the cell that user clicked on last
			if (((MyRectangle)cellPos.getChildren().get(0)).getCellId() == current.getCellId()) {
				
				MyRectangle previous = new MyRectangle();
				previous.setValue(number);
				previous.setOldValue(((MyRectangle) cellPos.getChildren().get(0)).getValue());
				previous.setCellId(((MyRectangle) cellPos.getChildren().get(0)).getCellId());

				if (previous.getValue() != null && previous.getOldValue() != null) {
					if(!previous.getValue().equals(previous.getOldValue())) {
						StackOperations.push(previous);
						StackOperations.stackRedo.clear();
						Gui.undo.setDisable(false);
						Gui.redo.setDisable(true);
					}
				} else if(previous.getOldValue() != previous.getValue()){
					StackOperations.push(previous);	
					StackOperations.stackRedo.clear();
					Gui.undo.setDisable(false);
					Gui.redo.setDisable(true);
				} 

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
					if(GameEngine.isSolvable()) {
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