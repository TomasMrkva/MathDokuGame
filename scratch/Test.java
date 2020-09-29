
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Test extends Application{

	public static void main(String[] args) {
		launch(args);
	}
	@Override
	public void start(Stage stage) {
		
		int N =3;
		int width = 40;
		Group grid = new Group();
		MyRectangle rectangle =  new MyRectangle(width, width);
		
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				MyRectangle cell = new MyRectangle(width, width);
				cell.setCol(i);
				cell.setRow(j);
				cell.setStrokeWidth(0.5);
				cell.setFill(Color.WHITE);
				cell.setStroke(Color.GREY);
				cell.relocate(i * width, j * width);
				grid.getChildren().add(cell);
			}
		}
		
		rectangle.setFill(Color.TRANSPARENT);
		rectangle.setStroke(Color.TOMATO);
		rectangle.relocate(100, 100);
		StackPane pane= new StackPane();
		pane.setPrefSize(80, 80);
		//Pane pane = new Pane();
		TextField text = new TextField();
		text.setText("hi");
		pane.setStyle("-fx-background-color: blue");
		
		pane.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				System.out.println("Hi");
			}
		});
		//pane.setCenter(rectangle);
		//pane.setTop(text);
		pane.getChildren().addAll(rectangle,text);
		Scene scene = new Scene(pane);
		stage.setMinHeight(400);
		stage.setMinWidth(400);
		stage.setScene(scene);
		stage.show();
	}
	 
}
