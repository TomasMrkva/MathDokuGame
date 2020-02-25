import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Mathdoku game main class
 * @author tomasmrkva
 *
 */
public class MathDoku extends Application {

	private static int N=6;
	private int width = 60;
	private ArrayList<Cage> cages = new ArrayList<Cage>();
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public static int getN() {
		return N;
	}
	
	@Override
	public void start(Stage stage) {
		stage.setTitle("Mathdoku");
		BorderPane root = new BorderPane();
		
		GridConstructor gridConstructor = new GridConstructor();
		gridConstructor.makeGrid(N, width);
		MakeCages(gridConstructor);
		gridConstructor.addCages(cages);
		gridConstructor.makeLabels();
		gridConstructor.makeBorder(width, N, 2, Color.TOMATO);
		
		Group gameGrid = gridConstructor.getGrid();
		
		Gui gui = new Gui(gridConstructor);
		root.setTop(gui.loadGame());
		root.setCenter(gameGrid);
		root.setLeft(gui.menu());
		root.setRight(gui.numbers(N));
		//root.setBottom(value);
		
		stage.setMinHeight(width * N + 100);
		stage.setMinWidth(width * N + 120);
		Scene scene = new Scene(root);
		
		stage.setScene(scene);
		stage.show();
	}
	
//	public VBox numbers(int N) {
//		VBox numbers = new VBox(5);
//		
//		for(int i=1; i<=N; i++) {
//			Button button = new Button();
//			button.setText(String.valueOf(i));
//			button.setPrefWidth(50);
//			numbers.getChildren().add(button);
//		}
//		numbers.setPadding(new Insets(5));
//		return numbers;
//	}
//	
//	public VBox menu() {
//		Button clear = new Button("Clear");
//		clear.setPrefWidth(50);
//		Button undo = new Button();
//		undo.setText("<-");
//		undo.setPrefWidth(50);
//		Button redo = new Button();
//		redo.setText("->");
//		redo.setPrefWidth(50);
//		VBox menu = new VBox(5);
//		menu.setPadding(new Insets(5));
//		menu.getChildren().addAll(clear, undo, redo);
//		return menu;
//	}
//	
//	public HBox loadGame() {
//		Button loadFile = new Button();
//		loadFile.setText("Load from a file");
//		Button loadText = new  Button();
//		loadText.setText("Load from a text");
//		HBox load = new HBox(5);
//		load.setPadding(new Insets(5));
//		load.getChildren().addAll(loadFile,loadText);
//		return load;
//	}
	
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
