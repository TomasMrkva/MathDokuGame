import java.util.ArrayList;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

public class Trial extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	
	ArrayList<MyRectangle> cells = new ArrayList<MyRectangle>();
	
	@Override
	public void start(Stage stage) {
		
		int N=6;
		int width = 50;
		
		stage.setTitle("Mathdoku");
		
		BorderPane root = new BorderPane();
		//HBox root = new HBox();
		
		VBox numberButtons = numbers(N);
        numberButtons.setStyle("-fx-background-color:yellow");
		numberButtons.setAlignment(Pos.CENTER);
		
		VBox gameMenu = menu();
		gameMenu.setAlignment(Pos.CENTER);
		
		HBox loadMenu = loadGame();
		loadMenu.setAlignment(Pos.CENTER);
		
		GridPane grid = this.drawGrid(N, width);
		//grid.setStyle("-fx-background-color:cyan");
		grid.setAlignment(Pos.CENTER);
		
		//grid.setGridLinesVisible(true);
		
		Shape combined = Shape.union((MyRectangle)grid.getChildren().get(0), (MyRectangle)grid.getChildren().get(1));
		combined.setStroke(Color.BLACK);
		combined.setFill(Color.TRANSPARENT);
		//grid.getChildren().add(combined);
		
	    grid.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					System.out.println("row: " + (((MyRectangle)event.getTarget()).getRow()+1) + "	" + "col: " +(((MyRectangle)event.getTarget()).getCol()+1));				}
		});
	    
//		for(Node n : grid.getChildren()) {
//			
//			System.out.println(((MyRectangle)n).getRow() + "," + ((MyRectangle)n).getCol());
//		}
//		System.err.println(grid.getChildren().size());

		
		root.setTop(loadMenu);
		root.setCenter(grid);
		root.setLeft(gameMenu);
		root.setRight(numberButtons);
		//root.setBottom(value);
		
		stage.setMinHeight(width*N + 100);
		stage.setMinWidth(width*N+120);
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
	
	public GridPane drawGrid(int N, int width) {
		
		GridPane grid = new GridPane();
		for (int row = 0; row < N; row++) {
			for (int col = 0; col < N; col++) {
				MyRectangle r = new MyRectangle(width,width);
//				r.setRow(row);
//				r.setCol(col);
				//r.setStroke(Color.GREY);
				r.setFill(Color.WHITE);
//				if(col%2 == 0 && row%2 ==0) {
//					r.setStrokeWidth(3);
//				}
//				else {
//					r.setStrokeWidth(1);
//				}
				r.setStroke(Color.BLACK);
				GridPane.setRowIndex(r, row);
				GridPane.setColumnIndex(r, col);
				grid.getChildren().add(r);
			}
//			Line l = new Line(50, 50, 50, 50);
//			l.setStrokeWidth(5);
//			grid.getChildren().add(l);
		}
		return grid;
	}
	
	
	public void MakeCages() {
		Shape.union(cells.get(0),cells.get(7));
	}
}
