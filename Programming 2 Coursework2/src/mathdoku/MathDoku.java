package mathdoku;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Mathdoku game main class
 * @author tomasmrkva
 *
 */
public class MathDoku extends Application {

	private static ArrayList<Cage> cages = new ArrayList<Cage>();
//	private static int N=6;
	private static final int presetN = 6;
	public static double width = 80;
	public static Stage pStage;
	public static Scene pScene;
	public static StackPane pRoot;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		pStage = stage;
		pRoot = new StackPane();
		stage.setTitle("Mathdoku");
		BorderPane game = new BorderPane();
		
		Label label = new Label("Welcome to MathDoku");
		label.setFont(new Font("Helvetica", 20));
		label.setPadding(new Insets(0, 0, 15, 0));
		
		Button newGame = new Button("New Game");
		newGame.setPrefSize(100, 30);
		newGame.setOnMouseClicked(new FileLoaderHandler());
		Button preset = new Button("Play");
		preset.getStyleClass().add("green");
		preset.setPrefSize(100, 30);
		preset.setDefaultButton(true);		
		preset.setOnAction(e -> MathDoku.createPreset());
		
		Button randomGame = new Button("Random Game");
		randomGame.setPrefSize(100, 30);
		randomGame.setOnAction(e -> Gui.randomGameMenu());
		
		VBox vbox = new VBox(10);
		vbox.getChildren().addAll(label,newGame, preset, randomGame);
		vbox.setAlignment(Pos.CENTER);
		
        game.setCenter(vbox);
		stage.setMinHeight(300);
		stage.setMinWidth(300);
//		root.setStyle("-fx-border-color: blue");
		pRoot.getChildren().add(game);
		pScene = new Scene(pRoot);
//		pScene.getStylesheets().add("styles.css");
//		pScene = scene;
//		pRoot = root;
		stage.setScene(pScene);
		stage.centerOnScreen();
		stage.show();
	}
	
	
	public static void createPreset() {
		GridConstructor grid = new GridConstructor(presetN, width);
		makeCages(grid);
		grid.addCages(cages);
		createGame(grid, cages, presetN, false);
	}
	
	public static void createGame(GridConstructor grid, ArrayList<Cage> cages, int N, boolean random) {
		// Removes winning animation if there was one
		if(MathDoku.pRoot.getChildren().size() > 1) {
			for(int i=MathDoku.pRoot.getChildren().size()-1; i>=1 ;i--) {
				MathDoku.pRoot.getChildren().remove(i);
			}
		}
		grid.makeLabels();
		grid.makeBorder(MathDoku.width, N, 2, Color.TOMATO);
		Gui gui = new Gui(grid);
		Group gameGrid = grid.getGrid();						
        StackPane pane = new StackPane();
        pane.getChildren().add(gameGrid);
        pane.setPickOnBounds(false);
        
        ((BorderPane) MathDoku.pRoot.getChildren().get(0)).setTop(gui.loadGame());
		((BorderPane) MathDoku.pRoot.getChildren().get(0)).setBottom(gui.botomPanel());
		((BorderPane) MathDoku.pRoot.getChildren().get(0)).setLeft(gui.menu());
		((BorderPane) MathDoku.pRoot.getChildren().get(0)).setRight(gui.numbers(N));
		((BorderPane) MathDoku.pRoot.getChildren().get(0)).setCenter(pane);
		
		NumberBinding maxScale = Bindings.min(pane.widthProperty().divide((N*0.83)*100), pane.heightProperty().divide((N*0.83)*100));
		pane.scaleXProperty().bind(maxScale);
		pane.scaleYProperty().bind(maxScale);
		if(N > 5) {
			MathDoku.pStage.setMinHeight(MathDoku.width * N + 120);
			MathDoku.pStage.setMinWidth(MathDoku.width * N + 140);				
		} else {
			MathDoku.pStage.setMinHeight(MathDoku.width * 6 + 120);
			MathDoku.pStage.setMinWidth(MathDoku.width * 6 + 140);				
		}
		pStage.centerOnScreen();
		StackOperations.clear();
		
		Alert solveAlert = new Alert(AlertType.NONE);
		solveAlert.setTitle("Generating a new Game");
		solveAlert.setHeaderText("Please wait...");
		solveAlert.setContentText("Checking solutions...");
		ProgressIndicator pi = new ProgressIndicator();
		pi.setMaxSize(40, 40);
		solveAlert.setGraphic(pi);
		solveAlert.show();
		
		Task<Boolean> task = new Task<Boolean>() {
			@Override
			protected Boolean call() {
				if(random) {
					return GameEngine.solve(grid.getCells(), "button");
				} else {
					return GameEngine.solve(grid.getCells(), "default");
				
				}
			}
		};
		
		task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			
            public void handle(WorkerStateEvent event) {
            	solveAlert.setResult(ButtonType.CANCEL);
				solveAlert.close();
            	if(task.getValue()){
            		Gui.solve.setDisable(false);
        			Gui.hint.setDisable(false);
            	};
            }
        });
		
		Thread th = new Thread(task);
		th.start();
		
//		if(random) {
//			if(GameEngine.solve(grid.getCells(), "button")) {
//				Gui.solve.setDisable(false);
//    			Gui.hint.setDisable(false);
////    			System.out.println("hi");
//			}
//			else {
//				if(GameEngine.solve(grid.getCells(), "default")) {
//					Gui.solve.setDisable(false);
//	    			Gui.hint.setDisable(false);
////	    			System.out.println("hi");
//				}
//			}
//		}
//		grid.requestFocus();
//		if(!GameEngine.solve(grid.getCells(), "default")) {
//			Gui.solve.setDisable(true);
//			Gui.hint.setDisable(true);
//		}
//		else {
//			Gui.solve.setDisable(false);
//			Gui.hint.setDisable(false);
//		}
		grid.requestFocus();

	}
	
	/**
	 * Creates a class for each cage and adds it to the ArrayList 
	 * @param grid
	 */
	public static void makeCages(GridConstructor grid) {
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
