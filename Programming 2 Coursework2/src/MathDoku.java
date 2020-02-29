import java.util.ArrayList;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Mathdoku game main class
 * @author tomasmrkva
 *
 */
public class MathDoku extends Application {

	private ArrayList<Cage> cages = new ArrayList<Cage>();
	private static int N=6;
	private static Label label;
	private static double width = 80;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public static void setText(String text) {
		label.setText(text);
	}
	
	public static int getN() {
		return N;
	}
	
	public static double getWidth() {
		return width;
	}
	
	@Override
	public void start(Stage stage) {
		stage.setTitle("Mathdoku");
		
		BorderPane root = new BorderPane();
		label = new Label("Grid has not been completed!");
		label.setPadding(new Insets(10));
		
		GridConstructor grid = new GridConstructor(N, width);
//		gridConstructor.makeGrid(N, width);
		MakeCages(grid);
		grid.addCages(cages);
		grid.makeLabels();
		grid.makeBorder(width, N, 2, Color.TOMATO);
		
		Group gameGrid = grid.getGrid();
		
		Gui gui = new Gui(grid);
		
		root.setTop(gui.loadGame());
		root.setLeft(gui.menu());
		root.setRight(gui.numbers(N));
		root.setBottom(label);
		BorderPane.setAlignment(label, Pos.CENTER);
		root.setCenter(gameGrid);
		
		stage.setMinHeight(width * N + 100);
		stage.setMinWidth(width * N + 140);
		Scene scene = new Scene(root);
	  
		stage.setScene(scene);
		stage.show();
	}
	
	/**
	 * Creates a class for each cage and adds it to the ArrayList 
	 * @param grid
	 */
	public void MakeCages(GridConstructor grid) {
		cages.add(new Cage("11+", grid.getCell(1), grid.getCell(7)));
		cages.add(new Cage("2÷", grid.getCell(2), grid.getCell(3)));
		cages.add(new Cage("3-", grid.getCell(8), grid.getCell(9)));
		cages.add(new Cage("20x", grid.getCell(4), grid.getCell(10)));
		cages.add(new Cage("6x", grid.getCell(5), grid.getCell(6),
			grid.getCell(12), grid.getCell(18)));
		cages.add(new Cage("240x", grid.getCell(13), grid.getCell(14),
			grid.getCell(19), grid.getCell(20)));
		cages.add(new Cage("6x", grid.getCell(15), grid.getCell(16)));
		cages.add(new Cage("3÷", grid.getCell(11), grid.getCell(17)));
		cages.add(new Cage("6x", grid.getCell(25), grid.getCell(26)));
		cages.add(new Cage("6x", grid.getCell(21), grid.getCell(27)));
		cages.add(new Cage("7+", grid.getCell(22), grid.getCell(28),
			grid.getCell(29)));
		cages.add(new Cage("30x", grid.getCell(23), grid.getCell(24)));
		cages.add(new Cage("8+", grid.getCell(31), grid.getCell(32),
			grid.getCell(33)));
		cages.add(new Cage("2÷", grid.getCell(34), grid.getCell(35)));
		cages.add(new Cage("9+", grid.getCell(30), grid.getCell(36)));
	}
	
}
