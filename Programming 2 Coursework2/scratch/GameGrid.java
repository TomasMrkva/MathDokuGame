

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class GameGrid extends Application{

	Group grid = new Group();
	private int N=6;
	private double width=40;
	private double bordervalue=100;
	private double widthValue=0;
	private double minValue=0;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		BorderPane pane = new BorderPane();
		
		Button b = new Button("Hello");
		Button b1 = new Button("Hello");
		Button b2 = new Button("Hello");
		Button b3 = new Button("Hello");
		Button b4 = new Button("Hello");
		Button b5 = new Button("Hello");

		
		Group game = draw(grid, width);
//		HBox hbox = new HBox();
//		hbox.getChildren().addAll(b4,b5);
		pane.setTop(b);
		pane.setCenter(game);
		pane.setBottom(b1);
		pane.setLeft(b2);
		pane.setRight(b3);
		BorderPane.setAlignment(b, Pos.TOP_CENTER);
		BorderPane.setAlignment(b1, Pos.CENTER);
		BorderPane.setAlignment(b2, Pos.CENTER);
		BorderPane.setAlignment(b3, Pos.CENTER);


		
		pane.widthProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				widthValue=newValue.doubleValue();
			}
		});
		Scene scene = new Scene(pane);
		
		scene.heightProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				System.out.println("height: " + newValue + " width: " + widthValue);
					game.getChildren().clear();
					
					minValue = newValue.doubleValue();
					draw(game, newValue.doubleValue()/(1.3*N));
					System.out.println(minValue);
			}
		});
		
		scene.heightProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				System.err.println(newValue);
			}
		});

		primaryStage.setScene(scene);

//		primaryStage.setMinHeight(N * width+)
//		primaryStage.setMinWidth(N * width+(3*width));




//		root.heightProperty().addListener(new ChangeListener<Number>() {
//			@Override
//			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//				System.out.println("height: " + newValue + " width: ");
//					gameGrid.getChildren().clear();
//					root.setCenter(null);
//					gridConstructor.removeAll();
//					gridConstructor.makeGrid(N, newValue.doubleValue()/(1.3*N));
//					MakeCages(gridConstructor);
//					gridConstructor.makeBorder(newValue.doubleValue()/(1.3*N), N, 2, Color.TOMATO);
//					gameGrid = gridConstructor.getGrid();
//					root.setCenter(gameGrid);
//					
//			}
//		});
//		


		
		primaryStage.show();
	}
	
	public Group draw(Group grid, double width) {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				Rectangle r = new Rectangle(width, width);
				r.setStrokeWidth(2);
				r.setStroke(Color.BLACK);
				r.setFill(Color.WHITE);
				r.relocate(j * width, i * width);
				grid.getChildren().add(r);
			}
		}
		return grid;
	}
	
}
