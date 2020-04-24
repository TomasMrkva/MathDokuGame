package mathdoku;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;

import java.io.BufferedReader;
import java.io.File;
import java.nio.file.Files;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.FileChooser.ExtensionFilter;

public class FileLoaderHandler implements EventHandler<MouseEvent> {
	
	private Stage newWindow;
	private TextArea area;

	/**
	 * Creates a new popup gui to handle config inputs
	 */
	@Override
	public void handle(MouseEvent event) {
		
		newWindow = new Stage();
		area = new TextArea();
		
		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(10));
		
		Button random = new Button("Random Game");
		random.setPrefWidth(100);
		random.setOnAction(e -> {
			newWindow.close();
			Gui.randomGameMenu();
		});
		
		Button choose = new Button("Choose File");
		choose.setPrefWidth(85);
		choose.addEventHandler(ActionEvent.ANY, new chooseFileHandler());
		
		Button cancel = new Button("Cancel");
		cancel.setPrefWidth(75);
		cancel.setOnAction(e -> {
			newWindow.close();
			if(Gui.getGrid()!=null) 
				Gui.getGrid().requestFocus();
		});
		
		Button submit = new Button("Submit");
		submit.setDefaultButton(true);
		submit.setPrefWidth(75);
		submit.addEventHandler(ActionEvent.ANY, new submitClickHandler());
		
		Label label = new Label("Specify your mathdoku here:");
		label.setPadding(new Insets(0, 0, 10, 0));
		
		HBox buttonsLeft = new HBox(5);
		buttonsLeft.getChildren().addAll(choose, random);
		buttonsLeft.setAlignment(Pos.BOTTOM_LEFT);
		
		HBox buttonsRight = new HBox(5);
		buttonsRight.getChildren().addAll(submit, cancel);
		buttonsLeft.setAlignment(Pos.BOTTOM_RIGHT);
		
		HBox buttons = new HBox(30);
		buttons.setPadding(new Insets(10, 0, 10, 0));
		buttons.setAlignment(Pos.CENTER);
		buttons.getChildren().addAll(buttonsLeft, buttonsRight);
		
		pane.setTop(label);
		pane.setCenter(area);
		pane.setBottom(buttons);
		BorderPane.setAlignment(label, Pos.BOTTOM_CENTER);
		
        Scene secondScene = new Scene(pane, 410, 320);
        newWindow.setTitle("Load a new game");
        newWindow.setScene(secondScene);
        newWindow.initModality(Modality.APPLICATION_MODAL);
        newWindow.setMinHeight(270);
        newWindow.setMinWidth(300);
        newWindow.show();
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
					BufferedReader buffered = Files.newBufferedReader(file.toPath());
//							new BufferedReader(new FileReader(file));
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
			int currValue = 0;
			String wrongPart = null;
			boolean wrongNumber = false;
			boolean duplicateCell = false;
			boolean wrongPosition = false;
			boolean wrongFormat = false;	//when there is a single cell and contains a +/-/x/÷ sign
			
			ArrayList<String[]> lines = new ArrayList<String[]>();
			Set<String> uniqueCells = new HashSet<String>();
			
			// Splits the text in the TextArea into separate arrays, each array is one line
			for (String line : area.getText().split("\\n")) {
				String[] parts = line.split("[ ,]");
				lines.add(parts);
				if(!parts[0].matches("[0-9]+[+\\-x÷]$") && !parts[0].matches("[0-9]+$")) {
					wrongFormat = true;
					wrongPart = line;
					break;
				}
				if(line.charAt(line.length()-1) == ',') {
					wrongFormat = true;
					wrongPart = line;
					break;
				}
				
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
					if((parts[0].matches("^[0-9]*$")) && parts.length > 2) {
						wrongFormat = true;
						wrongPart = line.toString();
						break;
					}
					if(parts.length == 2 && (!parts[0].matches("^[0-9]*$"))) {
						wrongFormat = true;
						wrongPart = line.toString();
						break;
					}
					numberOfCells++;
				}
			}
			int N = (int) Math.sqrt(numberOfCells);
			// When number of cells mismatch the max value, or there are no cells, or cells are duplicate
			// the grid wont be created and an error message will appear
			if(wrongPosition) displayErrorMessage("Cell positions must start from 1! "+ wrongPart);
			else if(wrongNumber) displayErrorMessage("Wrong number fromat! " + wrongPart);
			else if(duplicateCell) displayErrorMessage("Duplicate cells! " + wrongPart);
			else if(wrongFormat) displayErrorMessage("Wrong format! "+ wrongPart);
			else if((maxvalue != numberOfCells)) displayErrorMessage("Not enough cells!");
			else if(numberOfCells == 0) displayErrorMessage("No cells! " + wrongPart);
			else if((int)Math.pow(N, 2) != numberOfCells) displayErrorMessage("Not a squared grid! Number of cells: " + numberOfCells);
			else if(numberOfCells > 64) displayErrorMessage("Cell number too high: " + numberOfCells);
			else {
				ArrayList<Cage> cages = new ArrayList<Cage>();	//List of all cages for the grid
				boolean correctCage = false;
				GridConstructor grid = new GridConstructor(N, MathDoku.width);
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

//					System.out.println("******* " + line[0] + " ********");
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
//					System.out.println();
				}
				if(correctCage) {
					newWindow.close();
					boolean solutions = solutionAlert();
					grid.addCages(cages);
					if(solutions)
						MathDoku.createGame(grid, cages, N, "multiple");
					else
						MathDoku.createGame(grid, cages, N, "single");
				}
			}
		}
		
		public boolean solutionAlert() {
			Alert alert = new Alert(AlertType.CONFIRMATION, "", ButtonType.APPLY);
			CheckBox box = new CheckBox("Find all solutions");
			box.setSelected(true);
			box.selectedProperty().addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
					if(newValue == true) {
						alert.setContentText("The system will find all possible solutions, this might take a while");
					} else {
						alert.setContentText("The system will find only one solution");
					}
//					System.out.println(box.isSelected());
				}
			});
			alert.setHeaderText("The game may have multiple solutions:");
			alert.setContentText("Please select if you want to find all solutions");
			alert.initStyle(StageStyle.UNDECORATED);
			alert.setGraphic(box);
			alert.showAndWait();
			return box.isSelected();
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
				area.setEditable(true);
			}
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
//							System.out.println("CellID 0:0 : " + cell.getCellId());
						} else if(cellCol == N-1) {
							hashSet.add(cell.getCellId()+N);
							hashSet.add(cell.getCellId()-1);
//							System.out.println("CellID 0:N-1 : " + cell.getCellId());
						} else {
							hashSet.add(cell.getCellId()+N);
							hashSet.add(cell.getCellId()+1);
							hashSet.add(cell.getCellId()-1);
//							System.out.println("CellID 0:* : " + cell.getCellId());
						}
					} else if(cellRow == N-1) {
						if(cellCol == 0) {
							hashSet.add(cell.getCellId()-N);
							hashSet.add(cell.getCellId()+1);
//							System.out.println("CellID N-1:0 : " + cell.getCellId());
						} else if(cellCol == N-1) {
							hashSet.add(cell.getCellId()-N);
							hashSet.add(cell.getCellId()-1);
//							System.out.println("CellID N-1:N-1 : " + cell.getCellId());
						} else {
							hashSet.add(cell.getCellId()-N);
							hashSet.add(cell.getCellId()+1);
							hashSet.add(cell.getCellId()-1);
//							System.out.println("CellID N-1:* : " + cell.getCellId());
						}
					} else if(cellCol == 0) {
						hashSet.add(cell.getCellId()+1);
						hashSet.add(cell.getCellId()+N);
						hashSet.add(cell.getCellId()-N);
//						System.out.println("CellID *:0 : " + cell.getCellId());
					} else if(cellCol == N-1) {
						hashSet.add(cell.getCellId()-1);
						hashSet.add(cell.getCellId()+N);
						hashSet.add(cell.getCellId()-N);
//						System.out.println("CellID *:N-1 : " + cell.getCellId());
					} else {
						hashSet.add(cell.getCellId()-N);
						hashSet.add(cell.getCellId()+N);
						hashSet.add(cell.getCellId()+1);
						hashSet.add(cell.getCellId()-1);
//						System.out.println("CellID *:* : " + cell.getCellId());
					}
				}
//			System.out.println();
			} else {
				hashSet.add(cellsArray[0].getCellId());
			}
			for(MyRectangle cell : cellsArray) {
				if(hashSet.add(cell.getCellId()) == true) {
					return false;
				}
			}
			return true;
		}
	}
	
}