import java.util.ArrayList;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

public class GridConstructor {

	private Group grid = new Group();	//The whole grid is a Group of stackpanes
	//List of StackPanes which contain the cells MyRectangle class and labels
	private ArrayList<StackPane> cellsPos = new ArrayList<StackPane>();
	private static ArrayList<MyRectangle> cells = new ArrayList<MyRectangle>();	//List of the cells, used for making cages
	private static ArrayList<Cage> cages = new ArrayList<Cage>();
	private static MyRectangle current;	// The pointer the current cell in the grid
	private static MyRectangle[][] matrix = new MyRectangle[MathDoku.getN()][MathDoku.getN()];

	/**
	 * Creates the initial grid of stackpanes, but no cages are added
	 * This method has to be called 1st
	 * @param N, the number of NxN grid
	 * @param width, the width of each cell
	 */
	public void makeGrid(int N, double width) {
		int counter = 0;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				MyRectangle cell = new MyRectangle(width, width);	//Create a rectangle
				StackPane cellPos = new StackPane();	// Assign it to a StackPane
				cellPos.relocate(j * width, i * width);	// Reposition the stackpane to the positon of the cell
				cell.setCellId(counter++);	// Set the proper id of the rectangle
				cell.setRow(String.valueOf(i));
				cell.setCol(String.valueOf(j));
				matrix[i][j] = cell;
				cell.setStrokeWidth(0.25);
				cell.setFill(Color.TRANSPARENT);
				cell.setStroke(Color.BLACK);
				Label label = new Label(null);
				label.setMouseTransparent(true);
				cellPos.getChildren().addAll(cell, label);	// Add the cell to the stackpane, the same stackpane might contain a label 
				cells.add(cell);
				cellsPos.add(cellPos);
//				System.out.println(cellPos.getChildren().size());
				mouseClicked(cellPos);	// Adds an Event handler for each StackPane in the gridLabel label = new Label("");	
			}
		}
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
		return grid;
	}
	
	public void removeAll() {
		for(int i=0; i<grid.getChildren().size(); i++) {
			grid.getChildren().remove(i);
		}
		for(StackPane s : cellsPos) {
			for (int i=0; i<s.getChildren().size(); i++) {
				s.getChildren().remove(i);
			}
		}
	}
	
	/**
	 * Adds the cages to the grid, this method has to be called 2nd before displaying the final grid
	 * @param cages the ArrayList of cages, that will be added to the grid
	 */
	public void addCages(ArrayList<Cage> cages) {
		for(Cage c : cages) {
			grid.getChildren().add(c.getCage());
			this.cages.add(c);
		}
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
	 * Gives the cell in the rectangle at the specified position
	 * @param pos the number of the requested cell
	 * @return MyRectangle the requested cell MyRectangle object
	 */
	public MyRectangle getCell(int pos) {
		pos--;
		return cells.get(pos);
	}
	
	public static ArrayList<MyRectangle> getCells(){
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
//					if(current.getFill() == Color.TRANSPARENT) {
//						if(current.isRowRed() || current.isColRed() || current.isCageRed()) 
//							current.setFill(Color.rgb(255, 0, 0, 0.2));
//					} else {
//						current.setFill(Color.rgb(255, 0, 0, 0.2));
//					}
				}
				System.out.println("Click: Pos: " + (((MyRectangle)event.getTarget()).getCellId())
						+ "\tValue: " + ((MyRectangle) event.getTarget()).getValue() + "\tCageID: " 
						+((MyRectangle)event.getTarget()).getCageId() 
						+ "\tRow: " +((MyRectangle)event.getTarget()).getRow()
						+"\tCol: " + ((MyRectangle)event.getTarget()).getCol()
						+"\tCage : "+((MyRectangle)event.getTarget()).isCageRed()
						+"\tCol: " + ((MyRectangle) event.getTarget()).isRowRed()
						+"\tRow: " + ((MyRectangle) event.getTarget()).isColRed());
						
				// Save the cell as current 
				current = (MyRectangle) event.getTarget();
				current.setStrokeType(StrokeType.INSIDE);
//				current.setStroke(Color.rgb(80, 175, 255, 0.4));	//teal
				current.setStroke(Color.rgb(0, 0, 128, 0.4));
				current.setStrokeWidth(MathDoku.width);
				
//				GameEngine.checkCol(grid, cells, current);
//				GameEngine.checkRow(grid, cells, current);

				// When user clicks on the grid, the focus is requested for the grid
				requestFocus();
				// Calls an eventHandler for the key input
				keyTyped();
			}
		});
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
		grid.setOnKeyTyped(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				try {
					int N = MathDoku.getN();
					if (Character.isDigit(event.getCharacter().toCharArray()[0])) {
						int enteredNum = Integer.valueOf(event.getCharacter());
						if(enteredNum <= N && enteredNum > 0) {
//							System.out.println(event.getCharacter().toCharArray()[0]);
							String number = event.getCharacter().toString();
							
							displayNumber(number);
//							GameEngine.checkCol(grid, cells, current);
						}
						else System.out.println("User entered a wrong number!");
					}
				} catch (ArrayIndexOutOfBoundsException e) {
					if(event.getCharacter().equals("")) {
						System.err.println("User entered a backspace");
						displayNumber(null);
					} else System.err.println("User entered a wrong key!");
				}
				
//				GameEngine.checkRow(grid, cells, current);
//				GameEngine.checkCol(grid, cells, current);

//				GameEngine.checkAllCages(cages);
				if(GameEngine.isFinished(cells)) {
					Cage[] arr = cages.toArray(new Cage[cages.size()]);
					if(GameEngine.checkAllCols(matrix) && GameEngine.checkAllRows(matrix) && GameEngine.checkAllCages(arr)) {
						MathDoku.setText("YAYY!!");
					}
//					if(GameEngine.checkAllCages(cages)) {
//						MathDoku.setText("Works");
//					}
				}
			}
		});
	}
	
	public static void checkAllMistakes() {
		GameEngine.colorCols(matrix, GridConstructor.cells);
		GameEngine.colorRows(matrix, GridConstructor.cells);
		GameEngine.colorCages(cells, cages);
	}
	
	/**
	 * Displays the number on the grid, that user inputed
	 * @param number, takes the string value of the key input
	 */
	public void displayNumber(String number) {
		for(StackPane cellPos : cellsPos) {
			// finds the cell that user clicked on last
			if (((MyRectangle)cellPos.getChildren().get(0)).getCellId() == current.getCellId()) {
				Label label = new Label(number);
				label.setMouseTransparent(true);	// the label is not going to register mouse clicks
				// when the gridpane has more than two children (rectangle and the initial label("")), remove the last number
				if(cellPos.getChildren().size() > 2) {
					cellPos.getChildren().remove(cellPos.getChildren().size()-1);
				}
				// add the label with the current number to the stackpane of the cell
				((MyRectangle) cellPos.getChildren().get(0)).setValue(number);
				cellPos.getChildren().add(label);
			}
		}
		if(Gui.mistakes == true) {
			GameEngine.checkCage(cells, current, cages);
			GameEngine.checkRow(cells, current);
			GameEngine.checkCol(cells, current);
			
			GameEngine.colorCols(matrix, GridConstructor.cells);
			GameEngine.colorRows(matrix, GridConstructor.cells);
			GameEngine.colorCages(cells, cages);
		}
	}
	
}