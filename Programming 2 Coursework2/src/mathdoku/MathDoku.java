package mathdoku;
import java.security.InvalidParameterException;
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
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseButton;
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
	
	private static final int presetN = 6;
	private static final int BUTTON_SIZE = 100;

	private static ArrayList<Cage> cages = new ArrayList<Cage>();
	public static double width = 80;
	public static Stage pStage;
	public static Scene pScene;
	public static StackPane pRoot;
	private static boolean preset;
	
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
		newGame.setTooltip(new Tooltip("Opens a menu for loading a new game from text/file\n"
				+ "or generating a new one."));
		newGame.setPrefSize(BUTTON_SIZE, 30);
		newGame.setOnMouseClicked(new FileLoaderHandler());
		Button preset = new Button("Play example");
		preset.setTooltip(new Tooltip("Creates a default, preset MathDoku game."));
		preset.getStyleClass().add("green");
		preset.setPrefSize(BUTTON_SIZE, 30);
		preset.setDefaultButton(true);		
		preset.setOnAction(e -> MathDoku.createPreset());
		
		Button randomGame = new Button("Random Game");
		randomGame.setTooltip(new Tooltip("Opens a menu for creating a random MathDoku game."));
		randomGame.setPrefSize(BUTTON_SIZE, 30);
		randomGame.setOnAction(e -> Gui.randomGameMenu());
		
		VBox vbox = new VBox(10);
		vbox.getChildren().addAll(label,newGame, preset, randomGame);
		vbox.setAlignment(Pos.CENTER);
		
        game.setCenter(vbox);
		stage.setMinHeight(300);
		stage.setMinWidth(300);
		pRoot.getChildren().add(game);
		pScene = new Scene(pRoot);
		stage.setScene(pScene);
		stage.centerOnScreen();
		stage.show();
	}
	
	
	public static void createPreset() {
		preset = true;
		GridConstructor grid = new GridConstructor(presetN, width);
		makeCages(grid);
		grid.addCages(cages);
		createGame(grid, cages, presetN, "multiple");
	}
	
	/**
	 * Checks if the String parameter is correct
	 * @param String mode (can be only random/single/multiple)
	 * @return true if the mode is correct
	 * @throws InvalidParameterException if the mode is incorrect
	 */
	public static boolean isModeCorrect(String mode) {
		if(mode.equals("random") || mode.equals("single") || mode.equals("multiple"))
			return true;
		return false;
	}
	
	public static void createGame(GridConstructor grid, ArrayList<Cage> cages, int N, String mode) throws InvalidParameterException{
		// Removes winning animation if there was one
		if(!isModeCorrect(mode)) throw new InvalidParameterException();
		if(MathDoku.pRoot.getChildren().size() > 1) {
			for(int i=MathDoku.pRoot.getChildren().size()-1; i>=1 ;i--) {
				MathDoku.pRoot.getChildren().remove(i);
			}
		}
		if(!isModeCorrect(mode)) 
			throw new InvalidParameterException("Debugging purposes: Wrong mode for createGame(): " + mode);
		grid.makeLabels();
		grid.makeBorder(MathDoku.width, N, 2, Color.TOMATO);
		Gui gui = new Gui(grid);
		Group gameGrid = grid.getGrid();	
//		Tooltip.install(gameGrid, new Tooltip("To win, complete the whole grid with the following criteria:\n"
//				+ "- Each row and column must have only one of each number\n"
//				+ "- Each cage represents an elementary mathematical equation."));
		
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
		
		Alert solveAlert = solveAlert(mode);
		solveAlert.show();
		solvingTask(grid, solveAlert, mode);
	}
	
	public static Alert solveAlert(String mode) {
		Alert solveAlert = new Alert(AlertType.NONE);
//		solveAlert.initStyle(StageStyle.UNDECORATED);
		solveAlert.setTitle("Solving");
		if(mode.equals("single")) {
			solveAlert.setHeaderText("Please wait, finding a solution...");			
		}
		else {
			solveAlert.setHeaderText("Please wait, checking all possible solutions...");			
		}
		solveAlert.setContentText("To forcequit, doubleclick on this window");
		solveAlert.getDialogPane().setOnMouseClicked(event -> {
			if(event.getButton().equals(MouseButton.PRIMARY)){
	            if(event.getClickCount() == 2){
//	            	Platform.exit();
	            	System.exit(0);
	            }
	        }
		});
		
		ProgressIndicator pi = new ProgressIndicator();
		pi.setMaxSize(40, 40);
		solveAlert.setGraphic(pi);
		return solveAlert;
	}
	
	public static void solvingTask(GridConstructor grid, Alert solveAlert, String mode) {
		Task<Boolean> task = new Task<Boolean>() {
			@Override
			protected Boolean call() {
				if(mode.equals("random"))
					return GameEngine.solve(grid.getCells(), mode);
				else if(mode.equals("multiple"))
					return GameEngine.solve(grid.getCells(), "default");
				else 
					return GameEngine.solve(grid.getCells(), mode);
			}
		};
		task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
            public void handle(WorkerStateEvent event) {
            	solveAlert.setResult(ButtonType.CANCEL);
				solveAlert.close();
            	if(task.getValue()){
            		Gui.solve.setDisable(false);
        			Gui.hint.setDisable(false);
            	}
//            	else {
//					Gui.solve.setDisable(true);
//					Gui.solve.setDisable(true);
//				}
            	Alert info = new Alert(AlertType.INFORMATION);
        		if(GameEngine.noOfSolutions > 1) {
	    			info.setTitle("Multiple solutions!");
	    			info.setHeaderText("This grid has more than 1 solution!");
	    			info.setContentText("The number of solutions is: "+ GameEngine.noOfSolutions);
	    			info.showAndWait();
        		} else if(GameEngine.noOfSolutions == 1 && mode.equals("single")) {
        			info = null;
        		} else if(GameEngine.noOfSolutions == 1 && !mode.equals("random") && !preset){
        			info.setTitle("Single solution!");
	    			info.setHeaderText("This grid has a unique solution!");
	    			info.showAndWait();
        		} else if(!GameEngine.isSolvable()) {
        			info.setTitle("Unsolvable!");
        			info.setHeaderText("This grid has no solutions!");
        			info.setContentText("Solve and hint buttons will be disabled.");
	    			info.showAndWait();
        		} else if(preset)
        			preset = false;
        		grid.requestFocus();
            }
        });
		Thread th = new Thread(task);
		th.start();
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
