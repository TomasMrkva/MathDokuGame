import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.scene.Group;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class GameGenerator {

	public static void createGrid(int N) {
		GridConstructor grid = new GridConstructor(N, MathDoku.getWidth());
		Group gameGrid = grid.getGrid();
		grid.makeBorder(MathDoku.width, N, 2, Color.TOMATO);
		Gui gui = new Gui(grid);
		
		StackPane pane = new StackPane();
		pane.getChildren().add(gameGrid);
		pane.setPickOnBounds(false);
		NumberBinding maxScale = Bindings.min(pane.widthProperty().divide((N * 0.83) * 100),
				pane.heightProperty().divide((N * 0.83) * 100));
		pane.scaleXProperty().bind(maxScale);
		pane.scaleYProperty().bind(maxScale);
		
		((BorderPane) MathDoku.pRoot.getChildren().get(0)).setTop(gui.loadGame());
		((BorderPane) MathDoku.pRoot.getChildren().get(0)).setBottom(gui.bottomSide());
		((BorderPane) MathDoku.pRoot.getChildren().get(0)).setLeft(gui.menu());
		((BorderPane) MathDoku.pRoot.getChildren().get(0)).setRight(gui.numbers(N));
		((BorderPane) MathDoku.pRoot.getChildren().get(0)).setCenter(pane);
		
		if(N > 5) {
			MathDoku.getStage().setMinHeight(MathDoku.width * N + 120);
			MathDoku.getStage().setMinWidth(MathDoku.width * N + 140);				
		} else {
			MathDoku.getStage().setMinHeight(MathDoku.width * 6 + 120);
			MathDoku.getStage().setMinWidth(MathDoku.width * 6 + 140);				
		}
		MathDoku.getStage().centerOnScreen();
		grid.requestFocus();
		Gui.setGrid(grid);
	}
}
