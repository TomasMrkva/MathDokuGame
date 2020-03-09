import java.awt.Button;
import java.util.ArrayList;

import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Mathdoku game main class
 * @author tomasmrkva
 *
 */
public class MathDoku extends Application {

	private static ArrayList<Cage> cages = new ArrayList<Cage>();
	private static int N=6;
//	private static Label label;
	public static double width = 80;
	public static Stage pStage;
	public static Scene pScene;
	public static Gui gui;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public static Scene getScene() {
		return pScene;
	}
	
	public static Stage getStage() {
		return pStage;
	}
	
	public static double getWidth() {
		return width;
	}
	
	public static Gui getGui() {
		return gui;
	}
	
	@Override
	public void start(Stage stage) {
		pStage = stage;
		
		stage.setTitle("Mathdoku");
		BorderPane root = new BorderPane();
		
		
//		GridConstructor grid = new GridConstructor(N, width);
//		MakeCages(grid);
//		grid.addCages(cages);
//		grid.makeLabels();
//		grid.makeBorder(width, N, 2, Color.TOMATO);
//		Group gameGrid = grid.getGrid();
//		gui = new Gui(grid);
//		GameEngine.solve(grid.getCells(), grid.getCells().size());
//		
//        StackPane pane = new StackPane();
//        pane.getChildren().add(gameGrid);
//        pane.setPickOnBounds(false);
//        
//        NumberBinding maxScale = Bindings.min(pane.widthProperty().divide((N*0.83)*100), pane.heightProperty().divide((N*0.83)*100));
//        pane.scaleXProperty().bind(maxScale);
//        pane.scaleYProperty().bind(maxScale);
//        
//        root.setCenter(pane);
//        root.setTop(gui.loadGame());
//        root.setLeft(gui.menu());
//        root.setRight(gui.numbers(N));
//        root.setBottom(gui.bottomSide());
		
		Gui gui = new Gui(null);
        root.setCenter(gui.initialSetup());
		stage.setMinHeight(300);
		stage.setMinWidth(300);

//		pane.setStyle("-fx-border-color: blue");
		Scene scene = new Scene(root);
		pScene = scene;
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
		gameGrid.setMouseTransparent(true);
		
		gui = new Gui(grid);
		GameEngine.solve(grid.getCells(), grid.getCells().size());
		
        StackPane pane = new StackPane();
        pane.getChildren().add(gameGrid);
        pane.setPickOnBounds(false);
        
        NumberBinding maxScale = Bindings.min(pane.widthProperty().divide((N*0.83)*100), pane.heightProperty().divide((N*0.83)*100));
        pane.scaleXProperty().bind(maxScale);
        pane.scaleYProperty().bind(maxScale);
        
        Circle cir = new Circle();
        cir.setFill(Color.AQUAMARINE);
        cir.setRadius(30);
        cir.relocate(0, 0);
        StackPane.setAlignment(cir, Pos.TOP_LEFT);
//        cir.relo
        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.seconds(3));
        transition.setToX(100);
        transition.setToY(100);
        transition.setAutoReverse(true);
        transition.setCycleCount(Animation.INDEFINITE);
        transition.setNode(cir);
        transition.play();
        pane.getChildren().add(cir);

        ((BorderPane) getScene().getRoot()).setTop(gui.loadGame());
        ((BorderPane) getScene().getRoot()).setLeft(gui.menu());
        ((BorderPane) getScene().getRoot()).setRight(gui.numbers(N));
        ((BorderPane) getScene().getRoot()).setBottom(gui.bottomSide());
        ((BorderPane) getScene().getRoot()).setCenter(pane);

        
        MathDoku.getStage().setMinHeight(MathDoku.width * N + 120);
		MathDoku.getStage().setMinWidth(MathDoku.width * N + 140);
		MathDoku.getStage().centerOnScreen();
	}
	
}
