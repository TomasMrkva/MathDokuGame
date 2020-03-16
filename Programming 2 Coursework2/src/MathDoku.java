import java.util.ArrayList;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Mathdoku game main class
 * @author tomasmrkva
 *
 */
public class MathDoku extends Application {

	private static ArrayList<Cage> cages = new ArrayList<Cage>();
	private static int N=6;
	public static double width = 80;
	public static Stage pStage;
	public static Scene pScene;
//	public static Gui gui;
	public static StackPane pRoot;
	
	public static void main(String[] args) {
		launch(args);
	}
	
//	public static Scene getScene() {
//		return pScene;
//	}
	
	public static Stage getStage() {
		return pStage;
	}
	
	public static double getWidth() {
		return width;
	}
	
//	public static Gui getGui() {
//		return gui;
//	}
	
	@Override
	public void start(Stage stage) {
		pStage = stage;
		
		stage.setTitle("Mathdoku");
		StackPane root = new StackPane();
		BorderPane game = new BorderPane();
		
		Gui gui = new Gui(null);
        game.setCenter(gui.initialSetup());
		stage.setMinHeight(300);
		stage.setMinWidth(300);

//		root.setStyle("-fx-border-color: blue");
		root.getChildren().add(game);
		Scene scene = new Scene(root);
//		scene.getStylesheets().add("styles.css");
		pScene = scene;
		pRoot = root;
		stage.setScene(scene);
		stage.centerOnScreen();
		stage.show();
	}
	
	/**
	 * Creates a class for each cage and adds it to the ArrayList 
	 * @param grid
	 */
	public static void MakeCages(GridConstructor grid) {
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
	
	public static void PresetGrid() {
		GridConstructor grid = new GridConstructor(N, width);
		MakeCages(grid);
		grid.addCages(cages);
		grid.makeLabels();
		grid.makeBorder(width, N, 2, Color.TOMATO);
		Group gameGrid = grid.getGrid();
		
		Gui gui = new Gui(grid);
		GameEngine.solve(grid.getCells(), grid.getCells().size());
		
        StackPane pane = new StackPane();
        pane.getChildren().add(gameGrid);
        pane.setPickOnBounds(false);
        
        NumberBinding maxScale = Bindings.min(pane.widthProperty().divide((N*0.83)*100), pane.heightProperty().divide((N*0.83)*100));
        pane.scaleXProperty().bind(maxScale);
        pane.scaleYProperty().bind(maxScale);

        ((BorderPane) pRoot.getChildren().get(0)).setTop(gui.loadGame());
        ((BorderPane) pRoot.getChildren().get(0)).setLeft(gui.menu());
        ((BorderPane) pRoot.getChildren().get(0)).setRight(gui.numbers(N));
        ((BorderPane) pRoot.getChildren().get(0)).setBottom(gui.bottomSide());
        ((BorderPane) pRoot.getChildren().get(0)).setCenter(pane);
        
        MathDoku.getStage().setMinHeight(MathDoku.width * N + 120);
		MathDoku.getStage().setMinWidth(MathDoku.width * N + 140);
		MathDoku.getStage().centerOnScreen();
		grid.requestFocus();
	}
	
	public static void createGame(GridConstructor grid, Group game, ArrayList<Cage> cages, int N) {
		// Removes winning animation if there was one
		if(MathDoku.pRoot.getChildren().size() > 1) {
			for(int i=MathDoku.pRoot.getChildren().size()-1; i>=1 ;i--) {
				MathDoku.pRoot.getChildren().remove(i);
			}
		}
		grid.addCages(cages);
		grid.makeLabels();
		grid.makeBorder(MathDoku.width, N, 2, Color.TOMATO);
		
		Gui gui = new Gui(grid);
		Group gameGrid = grid.getGrid();							
        StackPane pane = new StackPane();
        pane.getChildren().add(gameGrid);
        pane.setPickOnBounds(false);
        
        ((BorderPane) MathDoku.pRoot.getChildren().get(0)).setTop(gui.loadGame());
		((BorderPane) MathDoku.pRoot.getChildren().get(0)).setBottom(gui.bottomSide());
		((BorderPane) MathDoku.pRoot.getChildren().get(0)).setLeft(gui.menu());
		((BorderPane) MathDoku.pRoot.getChildren().get(0)).setRight(gui.numbers(N));
		((BorderPane) MathDoku.pRoot.getChildren().get(0)).setCenter(pane);
		
		NumberBinding maxScale = Bindings.min(pane.widthProperty().divide((N*0.83)*100), pane.heightProperty().divide((N*0.83)*100));
		pane.scaleXProperty().bind(maxScale);
		pane.scaleYProperty().bind(maxScale);
		if(N > 5) {
			MathDoku.getStage().setMinHeight(MathDoku.width * N + 120);
			MathDoku.getStage().setMinWidth(MathDoku.width * N + 140);				
		} else {
			MathDoku.getStage().setMinHeight(MathDoku.width * 6 + 120);
			MathDoku.getStage().setMinWidth(MathDoku.width * 6 + 140);				
		}
		pStage.centerOnScreen();
		
		StackOperations.clear();
		Gui.setText("Grid has not been completed!");
		if(!GameEngine.solve(grid.getCells(), grid.getCells().size())) {
			Gui.solve.setDisable(true);
			Gui.hint.setDisable(true);
		}
		grid.requestFocus();
		Gui.setGrid(GameGenerator.grid);
	}
	
}
