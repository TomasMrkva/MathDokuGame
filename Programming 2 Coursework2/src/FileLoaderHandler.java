import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
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

public class FileLoaderHandler implements EventHandler<ActionEvent>{
	
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
			
			ArrayList<Cage> cages = new ArrayList<Cage>();
			ArrayList<String[]> lines = new ArrayList<String[]>();
			
			for (String line : area.getText().split("\\n")) {
				System.out.println(line);
				String[] parts = line.split("[ ,]");
				lines.add(parts);
				for (int i=1; i < parts.length; i++) {
					if((Integer.valueOf(parts[i])) > maxvalue) {
						maxvalue = Integer.valueOf(parts[i]);
					}
					numberOfCells++;
				}
			}
			
			int N = (int) Math.sqrt(numberOfCells);
			
			if((maxvalue != numberOfCells) || (numberOfCells == 0)) {
				System.err.println("wrong format of the text");
				Gui.getGrid().requestFocus();
			}
			else {
				System.out.println(numberOfCells);
				System.out.println(maxvalue);
				ArrayList<MyRectangle> cells = new ArrayList<MyRectangle>();
				
				((BorderPane) MathDoku.getScene().getRoot()).setCenter(null);
				Gui.setGrid(null);
				
				grid = new GridConstructor(N, MathDoku.getWidth());
				
				for(String[] line : lines) {
					for(int i=1; i < line.length; i++) {
			    		 cells.add(grid.getCell(Integer.valueOf(line[i])));
					}
					MyRectangle[] cellsArray = cells.toArray(new MyRectangle[cells.size()]);
					cells.clear();
					cages.add(new Cage(line[0], cellsArray));
				}
				
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
				grid.requestFocus();
			}
			newWindow.close();
		}
	}
	
}

