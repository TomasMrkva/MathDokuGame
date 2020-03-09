import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class FileLoaderHandler implements EventHandler<ActionEvent> {
	
	private GridConstructor grid;
	private Stage newWindow;
	private TextArea area;

	/**
	 * Creates a new popup gui
	 */
	@Override
	public void handle(ActionEvent event) {
		
		newWindow = new Stage();
		area = new TextArea();
		
		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(10));
		
		Button choose = new Button("Choose File");
		choose.setPrefWidth(85);
		choose.addEventHandler(ActionEvent.ANY, new chooseFileHandler());
		Button cancel = new Button("Cancel");
		cancel.setPrefWidth(85);
		Button submit = new Button("Submit");
		submit.addEventHandler(ActionEvent.ANY, new submitClickHandler());
		
		submit.setPrefWidth(85);
		
		Label label = new Label("Specify your mathdoku here:");
		label.setPadding(new Insets(0, 0, 10, 0));
		
		HBox buttons = new HBox(10);
		buttons.setPadding(new Insets(10, 0, 10, 0));
		buttons.setAlignment(Pos.CENTER);
		buttons.getChildren().addAll(choose, submit, cancel);
		
		pane.setTop(label);
		pane.setCenter(area);
		pane.setBottom(buttons);
		BorderPane.setAlignment(label, Pos.BOTTOM_CENTER);
		
        Scene secondScene = new Scene(pane, 300, 350);
        newWindow.setTitle("Load a new game");
        newWindow.setScene(secondScene);
        newWindow.initModality(Modality.APPLICATION_MODAL);
        newWindow.setMinHeight(270);
        newWindow.setMinWidth(250);
        newWindow.show();
		
		cancel.setOnAction(e -> {
			newWindow.close();
			Gui.getGrid().requestFocus();
		});
	}
	
	/**
	 * Choose from a text file handling
	 */
	class chooseFileHandler implements EventHandler<ActionEvent>{
		
		@Override
		public void handle(ActionEvent event) {
						
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Open File to Load");
			ExtensionFilter txtFilter = new ExtensionFilter("Text files","*.txt");
			fileChooser.getExtensionFilters().add(txtFilter);

			File file = fileChooser.showOpenDialog(newWindow);
			if (file != null && file.exists() && file.canRead()) {
				try {
					area.clear();
					BufferedReader buffered = new BufferedReader(new FileReader(file));
					String line;
					while ((line = buffered.readLine()) != null) {
						area.appendText(line + "\n");
					}
					area.setEditable(false);
					buffered.close();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}
	}

	/**
	 * Handler for submit button
	 */
	class submitClickHandler implements EventHandler<ActionEvent>{
		
		@Override
		public void handle(ActionEvent event) {
			int numberOfCells = 0;
			int maxvalue = 0;
			int currValue=0;
			String wrongPart = null;
			boolean wrongNumber = false;
			boolean duplicateCell = false;
			boolean wrongPosition = false;
			boolean wrongFormat = false;	//when there is a single cell and contains a +/-/x/÷ sign
			
			ArrayList<String[]> lines = new ArrayList<String[]>();
			Set<String> uniqueCells = new HashSet<String>();
			
			// Splits the text in the TextArea into separate arrays, each array is 
			// one line
			for (String line : area.getText().split("\\n")) {
//				System.out.println(line);
				String[] parts = line.split("[ ,]");
				lines.add(parts);
				//
				for (int i=1; i < parts.length; i++) {
					try {
						currValue = (Integer.valueOf(parts[i]));
					} catch (NumberFormatException e) {
						wrongNumber = true;
						wrongPart = line.toString();
						break;
					}
					//Checks for the highest value
					if(currValue > maxvalue) {
						maxvalue = Integer.valueOf(parts[i]);
					}
					if(currValue == 0) {
						wrongPosition = true;
						wrongPart = line.toString();
						break;
					}
					//Checks for duplicate cells
					if(uniqueCells.add(parts[i]) == false) {
						duplicateCell = true;
						wrongPart = line.toString();
						break;
					}
					if(parts.length == 2 && (!parts[0].matches("[0-9]*$"))) {
						wrongFormat = true;
						wrongPart = line.toString();
						break;
					}
					numberOfCells++;
				}
			}
			// When number of cells mismatch the max value, or there are no cells, or cells are duplicate
			// the grid wont be created and an error message will appear
			if(wrongPosition) displayErrorMessage("Cell positions must start from 1! "+ wrongPart);
			else if(wrongNumber) displayErrorMessage("Wrong number fromat! " + wrongPart);
			else if(numberOfCells == 0) displayErrorMessage("No cells!" + wrongPart);
			else if(duplicateCell) displayErrorMessage("Duplicate cells! " + wrongPart);
			else if(wrongFormat) displayErrorMessage("Wrong format! "+ wrongPart);
			else if((maxvalue != numberOfCells)) displayErrorMessage("Not enough cells!");
			
			else {
				ArrayList<Cage> cages = new ArrayList<Cage>();	//List of all cages for the grid
				boolean correctCage = false;
				int N = (int) Math.sqrt(numberOfCells);
				grid = new GridConstructor(N, MathDoku.getWidth());
				ArrayList<MyRectangle> cells = new ArrayList<MyRectangle>();	//Used for saving cells in a line
				
				// Loops through each array of Strings which were in one line
				for(String[] line : lines) {
					// Saves the position of each cell in the line
					for(int i=1; i < line.length; i++) {
						if(Integer.valueOf(line[i]) > 0) {
							cells.add(grid.getCell(Integer.valueOf(line[i])));
						}
					}
					//List converted to array which stores all the cells from the current line in the loop
					MyRectangle[] cellsArray = cells.toArray(new MyRectangle[cells.size()]);
//					Arrays.sort(cellsArray, Comparator.comparing(MyRectangle::getCellId));
					Arrays.sort(cellsArray);
//					for(MyRectangle cell : cellsArray) {
//						System.err.println(cell.getCellId());
//					}
					System.out.println("******* " + line[0] + "********");
					correctCage = isCageCorrect(cellsArray, N);	//Checks whether the cells in a line are neighbors
					if(correctCage) {
						//If yes: add cages to the grid
						cells.clear();
//						System.err.println(line[0]);
						cages.add(new Cage(line[0], cellsArray));
					} else {
						//If not, display message  and the grid wont be created
						displayErrorMessage("Wrong format of a cage on the line: "+ Arrays.toString(line));
						break;
					}
					System.out.println();
				}
				if(correctCage) {
					createGrid(cages, N);
				}
			}
		}
		
		public void createGrid(ArrayList<Cage> cages, int N) {
			((BorderPane) MathDoku.getScene().getRoot()).setCenter(null);
			Gui.setGrid(null);
			grid.addCages(cages);
			grid.makeLabels();
			grid.makeBorder(MathDoku.width, N, 2, Color.TOMATO);
			Group gameGrid = grid.getGrid();							
	        StackPane pane = new StackPane();
	        pane.getChildren().add(gameGrid);
	        pane.setPickOnBounds(false);
			((BorderPane) MathDoku.getScene().getRoot()).setCenter(pane);
			NumberBinding maxScale = Bindings.min(pane.widthProperty().divide((N*0.83)*100),
					pane.heightProperty().divide((N*0.83)*100));
			pane.scaleXProperty().bind(maxScale);
			pane.scaleYProperty().bind(maxScale);
			MathDoku.getStage().setMinHeight(MathDoku.width * N + 120);
			MathDoku.getStage().setMinWidth(MathDoku.width * N + 140);
			Gui.setGrid(grid);
			StackOperations.clear();
			Gui.setText("Grid has not been completed!");
			GameEngine.solve(grid.getCells(), grid.getCells().size());
			grid.requestFocus();
			newWindow.close();
		}
		
		public void displayErrorMessage(String message) {
			Alert alert;
			System.out.println("wrong format of the text");
			if(message == null) {
				alert = new Alert(AlertType.ERROR, "Wrong format of the text input!");
			} else {
				alert = new Alert(AlertType.ERROR, message);
			}
			Optional<ButtonType> result = alert.showAndWait();
			if (result.isPresent() && result.get() == ButtonType.OK) {
//				area.clear();
				area.setEditable(true);
			}
//			Gui.getGrid().requestFocus();
		}
		
		/**
		 * Checks if all cells in the cage are neighbors
		 * @param cellsArray the array of the cage
		 * @param N the size of the row/col
		 * @return true if the cage is valid, no if its not
		 */
		public boolean isCageCorrect(MyRectangle[] cellsArray, int N) {
			Set<Integer> hashSet = new HashSet<Integer>();
			if(cellsArray.length != 1) {
				for(MyRectangle cell : cellsArray) {
					int cellRow = Integer.valueOf(cell.getRow());
					int cellCol = Integer.valueOf(cell.getCol());
					if(cellRow == 0) {
						if(cellCol == 0) {
							hashSet.add(cell.getCellId()+N);
							hashSet.add(cell.getCellId()+1);
							System.out.println("CellID 0:0 : " + cell.getCellId());
						} else if(cellCol == N-1) {
							hashSet.add(cell.getCellId()+N);
							hashSet.add(cell.getCellId()-1);
							System.out.println("CellID 0:N-1 : " + cell.getCellId());
						} else {
							hashSet.add(cell.getCellId()+N);
							hashSet.add(cell.getCellId()+1);
							hashSet.add(cell.getCellId()-1);
							System.out.println("CellID 0:* : " + cell.getCellId());
						}
					} else if(cellRow == N-1) {
						if(cellCol == 0) {
							hashSet.add(cell.getCellId()-N);
							hashSet.add(cell.getCellId()+1);
							System.out.println("CellID N-1:0 : " + cell.getCellId());
						} else if(cellCol == N-1) {
							hashSet.add(cell.getCellId()-N);
							hashSet.add(cell.getCellId()-1);
							System.out.println("CellID N-1:N-1 : " + cell.getCellId());
						} else {
							hashSet.add(cell.getCellId()-N);
							hashSet.add(cell.getCellId()+1);
							hashSet.add(cell.getCellId()-1);
							System.out.println("CellID N-1:* : " + cell.getCellId());
						}
					} else if(cellCol == 0) {
						hashSet.add(cell.getCellId()+1);
						hashSet.add(cell.getCellId()+N);
						hashSet.add(cell.getCellId()-N);
						System.out.println("CellID *:0 : " + cell.getCellId());
					} else if(cellCol == N-1) {
						hashSet.add(cell.getCellId()-1);
						hashSet.add(cell.getCellId()+N);
						hashSet.add(cell.getCellId()-N);
						System.out.println("CellID *:N-1 : " + cell.getCellId());
					} else {
						hashSet.add(cell.getCellId()-N);
						hashSet.add(cell.getCellId()+N);
						hashSet.add(cell.getCellId()+1);
						hashSet.add(cell.getCellId()-1);
						System.out.println("CellID *:* : " + cell.getCellId());
					}
				}
			System.out.println();
			} else {
				hashSet.add(cellsArray[0].getCellId());
			}
			for(MyRectangle cell : cellsArray) {
				if(hashSet.add(cell.getCellId()) == true) {
//					System.out.println(cell.getCellId());
					return false;
				}
			}
			return true;
		}
	}
}

