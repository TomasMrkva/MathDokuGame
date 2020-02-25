import java.util.ArrayList;
import java.util.Arrays;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

public class MathDokuBackup extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage stage) {
		
		int N=6;
		int width = 50;
		
		stage.setTitle("Mathdoku");
		
		BorderPane root = new BorderPane();
		
		VBox numberButtons = numbers(N);
		numberButtons.setAlignment(Pos.CENTER);
		
		VBox gameMenu = menu();
		gameMenu.setAlignment(Pos.CENTER);
		
		HBox loadMenu = loadGame();
		loadMenu.setAlignment(Pos.CENTER);
				
		GridConstructor grid = new GridConstructor();
		grid.makeGrid(N, width);
//		cageConstructor.add(grid.getCell(0), grid.getCell(1), grid.getCell(2));
		
		Shape cage = cageConstructor.getCage();
//		grid.addCage(cage);
		grid.makeBorder(width, N, 2, Color.TOMATO);
		
//		Rectangle border = new Rectangle(width*N+1,width*N+1);
//		border.setStrokeType(StrokeType.OUTSIDE);
//		border.relocate(0, 0);
//		border.setStroke(Color.GREY);
//		border.setFill(Color.TRANSPARENT);
//		border.setStrokeWidth(2);
//		border.setMouseTransparent(true);
		
//		grid.addCage(cage);
		
		
//		Shape hello = add((MyRectangle)grid.getChildren().get(1),(MyRectangle)grid.getChildren().get(2),(MyRectangle)grid.getChildren().get(7));
		
//		grid.getChildren().add(hello);
		
//	    grid.setOnMouseClicked(new EventHandler<MouseEvent>() {
//				public void handle(MouseEvent event) {
//					try {
//						System.out.println("row: " + (((MyRectangle)event.getTarget()).getRow()+1) + "	" + "col: " +(((MyRectangle)event.getTarget()).getCol()+1));
//
//					} catch (Exception e) {
//						// TODO: handle exception
//					}
//				}});
	    
	    //grid.setMouseTransparent(true);

		
//	    grid.getChildren().add(border);
//		System.out.println(grid.getChildren());
		
		root.setTop(loadMenu);
		root.setCenter(grid.getGrid());
		root.setLeft(gameMenu);
		root.setRight(numberButtons);
		//root.setBottom(value);
		
		stage.setMinHeight(width*N + 100);
		stage.setMinWidth(width*N + 120);
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public VBox numbers(int N) {
		VBox numbers = new VBox(5);
		
		for(int i=1; i<=N; i++) {
			Button button = new Button();
			button.setText(String.valueOf(i));
			button.setPrefWidth(50);
			numbers.getChildren().add(button);
		}
		numbers.setPadding(new Insets(5));
		return numbers;
	}
	
	public VBox menu() {
		Button clear = new Button("Clear");
		clear.setPrefWidth(50);
		Button undo = new Button();
		undo.setText("<-");
		undo.setPrefWidth(50);
		Button redo = new Button();
		redo.setText("->");
		redo.setPrefWidth(50);
		VBox menu = new VBox(5);
		menu.setPadding(new Insets(5));
		menu.getChildren().addAll(clear, undo, redo);
		return menu;
	}
	
	public HBox loadGame() {
		Button loadFile = new Button();
		loadFile.setText("Load from a file");
		Button loadText = new  Button();
		loadText.setText("Load from a text");
		HBox load = new HBox(5);
		load.setPadding(new Insets(5));
		load.getChildren().addAll(loadFile,loadText);
		return load;
	}
		
	public Shape add(MyRectangle... r) {
		ArrayList<MyRectangle> cells;
		Shape cage = null;
		
		cells = new ArrayList<MyRectangle>(Arrays.asList(r));	
		for(int i=0; i < cells.size()-1; i++) {
			if(i == 0) 
				cage = Shape.union(cells.get(i), cells.get(i+1));
			else {
				cage = Shape.union(cage, cells.get(i+1));
			}
		}
		cage.setStrokeWidth(2);
		cage.setStroke(Color.BLACK);
		cage.setFill(Color.TRANSPARENT);
		
		cage.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				System.err.println("Clicked on a shape!!");
			}
			
		});
		cage.setMouseTransparent(true);
		
		return cage;
	}
	
}
